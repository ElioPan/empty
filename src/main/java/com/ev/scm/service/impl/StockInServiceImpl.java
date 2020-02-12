package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.StockInDao;
import com.ev.scm.dao.StockInItemDao;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.service.StockItemService;
import com.ev.scm.service.StockInItemService;
import com.ev.scm.service.StockInService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;

@EnableTransactionManagement
@Service
public class StockInServiceImpl implements StockInService {
	@Autowired
	private StockInDao stockInDao;

	@Autowired
	private StockInService  stockInService;

	@Autowired
	private StockInItemService SstockInItemService;

	@Autowired
	private StockItemService stockDetailService;

	@Autowired
	private StockInItemDao stockInItemDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public R addOtherIn(StockInDO stockInDO , String proInbodyList) {
		Map<String, Object> query = Maps.newHashMap();
		//保寸主表信息
		stockInDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);         //10待审核；11已审核  -->178待审核；179已审核
		int rows = stockInDao.save(stockInDO);
		//保存子表信息
		if (rows > 0) {

			if (null!=proInbodyList&&!"".equals(proInbodyList)) {
				List<StockInItemDO> inbodyCDos = JSON.parseArray(proInbodyList, StockInItemDO.class);
				for (StockInItemDO propuinbody : inbodyCDos) {
					propuinbody.setInheadId(stockInDO.getId());
					int lines = stockInItemDao.save(propuinbody);
				}
				//将主表主键返回前端，审核使用。
				query.put("msg", "保存成功");
				query.put("inHead_Id", stockInDO.getId());
				return R.ok(query);
			}else{
				return R.ok("保存成功，但明细行为空！");
			}

		} else {

			return R.error("保存失败！");
		}
	}

	@Override
	public R addAuditStatus(Long inHeadId, Long auditor) {

		//更改主表审核状态为11已审核-->179已审核
		StockInDO stockInDO=new StockInDO();
		stockInDO.setAuditor(auditor);
		stockInDO.setAuditTime(new Date());
		stockInDO.setId(inHeadId);
		stockInDO.setAuditSign(ConstantForGYL.OK_AUDITED); //179已审核

		//首先判断主表中状态是否已经审核，不允许反复审核！   178待审核；179已审核
		StockInDO pInheadDO = stockInDao.get(inHeadId);
		if (pInheadDO!=null) {

			if (!(Objects.equals(pInheadDO.getAuditSign(),ConstantForGYL.OK_AUDITED))) {
				int rows = stockInDao.update(stockInDO);

				if (rows > 0) {
					//将产品分别保存至库存及明细中
					String result = SstockInItemService.addOtherAudit(inHeadId);

					if ("ok".equals(result)) {
						return R.ok();
					} else {
						return R.error("未查到数据，请检查入库明细是否有数据！");
					}
				} else {
					return R.error("审核未成功！");
				}

			} else {
				return R.error("请勿重复审核！");
			}
		}
		return R.error("主表数据不存在，请检查参数！");
	}

	@Override
	public R changeOtherAudit(Long inHeadId) {
		Long exitentryType=ConstantForGYL.OTHER_WAREHOUSE ; //入库类型  15其他入库--->= 183L
		//判断是否能够反审核：detail表中出现两次stock的主键即不能反审核
		int  result = stockDetailService.getStockIdByHeadId(exitentryType,inHeadId);
		if(result==1){

			//允许反审核   //将Stock、etail表中数据做物理删除;
			int remove = stockDetailService.dealDateOveraAudite(exitentryType,inHeadId);
			if(remove==1){
				//将主表中数据做标记为10待审核。
				this.dealOveraAudit(inHeadId);
				return R.ok();
			}else{
				return R.error("反审核失败！");
			}

		}else if(result==-1){
			//不允许反审核
			return R.error("所入产品已出库，此单据不允许反审！！");
		}else{
			return R.error("反审核失败！");
		}
	}

	@Override
	public R editOtherStorages(StockInDO stockInDO , String proOldInBodyDO, String proNewInBodyDO,
							   Long[] inBodyIds, Long inHeadId) {
		Map<String, Object> query = Maps.newHashMap();
		//修改主表信息
		stockInDO.setId(inHeadId);
		stockInDO.setInTime(new Date());
		int editRows = stockInDao.update(stockInDO);

		//修改子表信息
		if (editRows > 0) {

			int i = SstockInItemService.updateChangeDetails(proOldInBodyDO, proNewInBodyDO, inBodyIds, inHeadId);
			if (i == 1) {
				//将主表主键返回前端，审核使用。
				query.put("msg", "修改成功！");
				query.put("inHead_Id", inHeadId);
				return R.ok(query);
			}
			return R.error("修改子表明细行失败，请检查参数信息！");
		} else {
			return R.error("修改失败，请检查参数信息！");
		}
	}

	@Override
	public R remoceOtherWaitAuite(Long inHeadId) {
//        //判断是否能够删除：inhead表待审核状态10L 则允许删除
//        PropurchaseInheadDO proheadDo = this.get(inHeadId);
//        if (proheadDo != null) {
//            if (Objects.equals(proheadDo.getAuditSign(),ConstantForGYL.WAIT_AUDIT) ) {    //10  待审核--->178L;
//                //将主子数据做物理删除
//            	propurchaseInheadDao.remove(inHeadId);
//                SstockInItemService.removeByInHeadId(inHeadId);
//
//                return R.ok();
//            } else {
//                return R.everror("此单已审核不允许删除！！");
//            }
//        }
		return R.error("此主键下数据不存在，请核对！");
	}

	@Override
	public R otherHeadDetailList(Map<String, Object> params) {
		int pagesize = (int) params.get("pagesize");
		int pageno = (int) params.get("pageno");
		int counts = this.count(params);
		Map<String, Object> resuls = new HashMap<String, Object>();
		List<Map<String, Object>> detailList = this.getlistByIndata(params);
		if (detailList.size() > 0) {
			DsResultResponse dsResultResponse = new DsResultResponse() {{
				setDatas(detailList);
				setPageno(pageno);
				setPagesize(pagesize);
				setTotalRows(counts);
				setTotalPages((Integer) ((counts + pagesize - 1) / pagesize));
			}};
			resuls.put("data", dsResultResponse);
			return R.ok(resuls);
		} else {
			return R.error("无数据！");
		}
	}

	@Override
	public StockInDO get(Long id){
		return stockInDao.get(id);
	}

	@Override
	public List<StockInDO> list(Map<String, Object> map){
		return stockInDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return stockInDao.count(map);
	}

	@Override
	public int save(StockInDO stockInDO){
		return stockInDao.save(stockInDO);
	}

	@Override
	public int update(StockInDO stockInDO){
		return stockInDao.update(stockInDO);
	}

	@Override
	public int remove(Long id){
		return stockInDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return stockInDao.batchRemove(ids);
	}

	@Override
	public int dealOveraAudit(Long inHeadId) {
		StockInDO stockInDO=new StockInDO();
		stockInDO.setId(inHeadId);
		stockInDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);//10  待审核--->178L;

		return stockInDao.update(stockInDO);
	}

	@Override
	public List<Map<String, Object>> getlistByIndata(Map<String, Object> map) {
		return stockInDao.getlistByIndata(map);
	}

	@Override
	public List<Map<String, Object>> getHeadDetailById(Map<String, Object> map) {
		return stockInDao.getHeadDetailById(map);
	}

	@Override
	public Map<String, Object> dealWithMaster(StockInDO stockInDO, String propurchaseInbodyDO) {

		stockInDao.save(stockInDO);

		Map<String, Object> query = Maps.newHashMap();
		if (StringUtils.isNotEmpty(propurchaseInbodyDO)) {


			List<StockInItemDO> inbodyCDos = new ArrayList<StockInItemDO>();
			inbodyCDos = JSON.parseArray(propurchaseInbodyDO, StockInItemDO.class);

			//保存子表信息
			for (StockInItemDO propuinbody : inbodyCDos) {
				propuinbody.setInheadId(stockInDO.getId());
				int lines = SstockInItemService.save(propuinbody);
			}
			//将主表主键返回前端，审核使用。
			query.put("msg", "保存成功");
			query.put("inHeadId", stockInDO.getId());
		}
		return query;
	}


	@Override
	public String dealProcessAudit(StockInDO stockInDO, Long inHeadId,String code) {
		stockInDao.update(stockInDO);
		String result = SstockInItemService.getProByHeadId(inHeadId,code);
		return result;
	}


	@Override
	public String dealProcessCounterAudit(Long exitentryType, Long inHeadId) {
		//将Stock、etail表中数据做物理删除;
		int remove = stockDetailService.dealDateOveraAudite(exitentryType,inHeadId);
		if(remove==1){
			//将主表中数据做标记为10待审核。
			this.dealOveraAudit(inHeadId);
			return "ok";
		}
		return "反审核失败！";
	}

	@Override
	public int countOfList(Map<String, Object> map) {
		return stockInDao.countOfList(map);
	}


/**
 * ======================================================以下为优化后代码================================================
 */

	/**
	 * 处理各类入库
	 * @return
	 */
	@Override
	public R addAndChangeInStockType(StockInDO stockInDO,Long inStockTypeId,String bodyDetail,Long[] ItemIds){
		String code=null;
		Long storageType=0L;

		DictionaryDO dictionaryDO = dictionaryService.get(Integer.parseInt(inStockTypeId.toString()));
		if(Objects.nonNull(dictionaryDO)){
			code = purchaseContractCode(dictionaryDO.getValue());
			storageType=inStockTypeId;
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoData",null));
		}

		Long headId = stockInDO.getId();
		if(Objects.isNull(headId)){
			//保寸主表信息
			stockInDO.setInheadCode(code);
			stockInDO.setAuditSign(ConstantForGYL.WAIT_AUDIT );
			stockInDO.setStorageType(storageType);
			stockInDao.save(stockInDO);

			if (StringUtils.isNotEmpty(bodyDetail)) {
				List<StockInItemDO>inbodyCDos = JSON.parseArray(bodyDetail, StockInItemDO.class);
				//保存子表信息
				Map<String, Object> query = Maps.newHashMap();
				for (StockInItemDO propuinbody : inbodyCDos) {
					propuinbody.setInheadId(stockInDO.getId());
					SstockInItemService.save(propuinbody);
				}
				//将主表主键返回前端，审核使用。
				query.put("msg", "保存成功");
				query.put("inHeadId", stockInDO.getId());
				return R.ok(query);
			}else{
				return R.error(messageSourceHandler.getMessage("scm.inStock.isIn.haveNoDetail",null));
			}
		}else{
			//修改
			StockInDO InheadDo = stockInDao.get(headId);

			if (InheadDo != null) {
				if (Objects.equals(ConstantForGYL.WAIT_AUDIT,InheadDo.getAuditSign())) { //10---->178 待审核
					stockInDao.update(stockInDO);

					if(ItemIds.length>0){
						SstockInItemService.batchRemove(ItemIds);
					}

					if (StringUtils.isNotEmpty(bodyDetail)) {
						List<StockInItemDO>inbodyCDos = JSON.parseArray(bodyDetail, StockInItemDO.class);
						//保存子表信息
						Map<String, Object> query = Maps.newHashMap();
						for (StockInItemDO propuinbody : inbodyCDos) {
							if(Objects.nonNull(propuinbody.getId())){
								SstockInItemService.update(propuinbody);
							}else{
								propuinbody.setInheadId(stockInDO.getId());
								SstockInItemService.save(propuinbody);
							}
						}
						//将主表主键返回前端，审核使用。
						query.put("msg", "保存成功");
						query.put("inHeadId", stockInDO.getId());
						return R.ok(query);
					}else{
						return R.error(messageSourceHandler.getMessage("scm.inStock.isIn.haveNoDetail",null));
					}
				}else{
					return R.error(messageSourceHandler.getMessage("common.approved.update.disabled",null));
				}
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
			}
		}
	}



	private String purchaseContractCode(String constant) {
		String maxNo = DateFormatUtil.getWorkOrderno(constant);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<StockInDO> list = this.list(param);
		String taskNo = null;
		if (!list.isEmpty()) {
			taskNo = list.get(0).getInheadCode();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}

	@Override
	public R auditAllTypeInStock(Long Id,Long auditor,Long storageTypeId ){

		//更改主表审核状态为11已审核-->179已审核  178待审核；
		StockInDO pInheadDO = stockInDao.get(Id);
		if (Objects.nonNull(pInheadDO)) {
			if (!(Objects.equals(pInheadDO.getAuditSign(),ConstantForGYL.OK_AUDITED))) {
				StockInDO stockInDO=new StockInDO();
				stockInDO.setAuditor(auditor);
				stockInDO.setAuditTime(new Date());
				stockInDO.setId(Id);
				stockInDO.setAuditSign(ConstantForGYL.OK_AUDITED); //179已审核
				stockInDao.update(stockInDO);

				//入库操作
				SstockInItemService.addAllTypeInStock(Id,storageTypeId);
				return R.ok();
			} else {
				return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
		}
	}


	@Override
	public R disAuditInStock(Long inHeadId,Long type){
		StockInDO inheadDO = stockInDao.get(inHeadId);
		if (inheadDO != null) {
			if (Objects.equals(inheadDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {   //179已审核
				//判断是否能够反审核：detail表中出现两次stock（出现两次表示已经做了出库）的主键即不能反审核

				int counts = stockDetailService.getStockIdByHeadIds(type, inHeadId);
				if (counts == 1) {
					//允许反审核   //将Stock、etail表中数据做物理删除;+将主表中数据做标记为10待审核。
					String resuls = this.dealProcessCounterAudit(type, inHeadId);
					if (resuls.equals("ok")) {
						return R.ok();
					} else {
						//反审核失败！
						return R.error();
					}
				} else if (counts == -1) {
					return R.error(messageSourceHandler.getMessage("apis.stock.noEntryAudit", null));
				} else {
					return R.error(messageSourceHandler.getMessage("common.massge.faildAudit", null));
				}
			} else {
				return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
			}
		}
		return R.error(messageSourceHandler.getMessage("common.massge.haveNoId", null));


	}

	@Override
	public R deleBatch(Long[] ids){

		//判断是否能够删除：inhead表待审核状态178 则允许删除
		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",ids);
		int rows= stockInDao.canDeletOfCount(map);
		if (rows == ids.length) {
			stockInDao.batchRemove(ids);
			SstockInItemService.removeByInHeadId(ids);
			return R.ok();
		} else {
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk", null));
		}
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return stockInDao.countForMap(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return stockInDao.listForMap(map);
	}

	@Override
	public Map<String, Object> deatilOfhead(Map<String, Object> map) {
		return stockInDao.deatilOfhead(map);
	}


}
