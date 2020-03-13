package com.ev.scm.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.beust.jcommander.internal.Maps;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.vo.BomEntity;
import com.ev.mes.vo.InventoryPlanEntity;
import com.ev.scm.dao.InventoryPlanDao;
import com.ev.scm.dao.InventoryPlanFitlossDao;
import com.ev.scm.domain.InventoryPlanDO;
import com.ev.scm.domain.InventoryPlanFitlossDO;
import com.ev.scm.domain.InventoryPlanItemDO;
import com.ev.scm.service.InventoryPlanFitlossService;
import com.ev.scm.service.InventoryPlanItemService;
import com.ev.scm.service.InventoryPlanService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class InventoryPlanServiceImpl implements InventoryPlanService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private InventoryPlanDao inventoryPlanDao;
	@Autowired
	private InventoryPlanItemService inventoryPlanItemService;
	@Autowired
	private InventoryPlanService inventoryPlanService;
	@Autowired
	private InventoryPlanFitlossService inventoryPlanFitlossService;
	@Autowired
	private InventoryPlanFitlossDao inventoryPlanFitlossDao;
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public InventoryPlanDO get(Long id){
		return inventoryPlanDao.get(id);
	}
	
	@Override
	public List<InventoryPlanDO> list(Map<String, Object> map){
		return inventoryPlanDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return inventoryPlanDao.count(map);
	}
	
	@Override
	public int save(InventoryPlanDO checkHead){
		return inventoryPlanDao.save(checkHead);
	}
	
	@Override
	public int update(InventoryPlanDO checkHead){
		return inventoryPlanDao.update(checkHead);
	}
	
	@Override
	public int remove(Long id){
		return inventoryPlanDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return inventoryPlanDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listByDates(Map<String, Object> map) {
		return inventoryPlanDao.listByDates(map);
	}

	@Override
	public List<Map<String, Object>> getProMsgByHeadId(Map<String, Object> map) {
		return inventoryPlanDao.getProMsgByHeadId(map);
	}

	@Override
	public int countOfListByDates(Map<String, Object> map) {
		return inventoryPlanDao.countOfListByDates(map);
	}

	@Override
	public int countOfStatus(Map<String, Object> map) {
		return inventoryPlanDao.countOfStatus(map);
	}


	@Override
	public R disposeCheckByQrId(Long planId,String  qrMsg, Long qrId){
		List<InventoryPlanItemDO> planItemDO = JSON.parseArray(qrMsg, InventoryPlanItemDO.class);
		Map<String,Object>  map= new HashMap<>();
		map.put("headId",planId);
		map.put("materielId",planItemDO.get(0).getMaterielId());
		map.put("warehouse",planItemDO.get(0).getWarehouse());
//		map.put("warehLocation",planItemDO.get(0).getWarehLocation());
		map.put("batch",planItemDO.get(0).getBatch());
		 List<InventoryPlanItemDO> list = inventoryPlanItemService.list(map);

		 if(list.isEmpty()){
		 	return R.error(messageSourceHandler.getMessage("scm.checkPlan.checkQRCode", null));
		 }else{
			 String qrIdCount = list.get(0).getQrIdCount()==null?"":list.get(0).getQrIdCount().toString();
			 if(Objects.nonNull(qrIdCount)&&StringUtils.isNotEmpty(qrIdCount)){

				 JSONObject qrIdCountMap = JSONObject.fromObject(qrIdCount);

//				 Map<String,Object>  mapGson= new HashMap<>();
//				 Gson gson=new Gson();
//				 mapGson=gson.fromJson(qrIdCount, new TypeToken<Map<String, Object>>() {}.getType());

				 Map<String, Object> qrIdCounts = (Map<String, Object>)qrIdCountMap;

				 if(qrIdCounts.containsKey(String.valueOf(qrId))){
					 String [] args = {qrIdCounts.get(String.valueOf(qrId)).toString()};
					 return R.ok(messageSourceHandler.getMessage("scm.checkPlan.checkQRCodeOK", args));
				 }else{
					 return R.ok(messageSourceHandler.getMessage("scm.checkPlan.checkQRCodenever", null));
				 }
			 }else{
				 return R.ok(messageSourceHandler.getMessage("scm.checkPlan.checkQRCodenever", null));
			 }
		 }
	}


	@Override
	public R disposePlanIsOver (Long planId ){
		//验证子表中盘点数量和盈亏数量是否为null的。
		Map<String,Object>  map= new HashMap<>();
		map.put("checkCount","OVER");
		map.put("headId", planId);
		InventoryPlanDO inventoryPlanDO = inventoryPlanService.get(planId);
		if(Objects.equals(inventoryPlanDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER)){
			return R.error(messageSourceHandler.getMessage("scm.checkPlan.checkStuts", null));
		}
		int counts = inventoryPlanItemService.countOfWinLoss(map);
		if(inventoryPlanItemService.countOfWinLoss(map)>0){
			return R.error(messageSourceHandler.getMessage("scm.checkPlan.planIsOverError", null));
		}else{
			InventoryPlanDO planDO=new InventoryPlanDO();
			planDO.setCheckStatus(ConstantForGYL.EXECUTE_OVER);
			planDO.setId(planId);
			this.update(planDO);
			return R.ok();
		}
	}

	@Override
	public R getMaterielCount(Long warehouse, String syntheticData){

		Map<String,Object> result = Maps.newHashMap();
		Map<String,Object> query = Maps.newHashMap();
		query.put("warehouse",warehouse);
		query.put("name",syntheticData);
		List<Map<String, Object>> proMsgCount = inventoryPlanItemService.getProMsgCount(query);
		List<Map<String, Object>> materialAll = inventoryPlanItemService.getMaterialAll(query);

		List<Map<String, Object>> retrunList=new ArrayList<>();
		if (!Objects.equals(0, proMsgCount.size())) {
			for (Map<String, Object> materialOne : materialAll) {
				String materialSing = materialOne.get("materielId").toString() + "-" + materialOne.get("warehouseId").toString();
				String batch = materialOne.get("batch").toString();
				Map<String, Object> map = new HashMap<>();
				BigDecimal systemCount = BigDecimal.ZERO;
				for (Map<String, Object> listMap : proMsgCount) {
					String listMaterialSing = listMap.get("materielId").toString() + "-" + listMap.get("warehouseId").toString();
					String listBatch = listMap.get("batch").toString();
					if (Objects.equals(materialSing, listMaterialSing) && Objects.equals(batch, listBatch)) {
						systemCount = systemCount.add(new BigDecimal(listMap.get("count").toString()));
						if (Objects.equals(0, map.size())) {
							map.putAll(listMap);
							map.put("systemCount", systemCount);
						}
					}
				}
				if (!Objects.equals(0, map.size())) {
					map.put("systemCount", systemCount);
//					map.remove("checkCount");
//					map.remove("profitLos");
				}
				retrunList.add(map);
			}
		} else {
			return R.ok(result);
		}
		result.put("data",retrunList);
		return R.ok(result);

	}

	@Override
	public R addInventoryPlan(InventoryPlanDO checkHeadDO, String checkBodys, Long[] deleItemIds) {
		//提箱前端当选择的仓库为“所有仓库”时 仓库字段放空值
		if (Objects.nonNull(checkHeadDO.getId())) {
            InventoryPlanDO inventoryPlanDO = inventoryPlanService.get(checkHeadDO.getId());
            if (Objects.equals(inventoryPlanDO.getCheckStatus(), ConstantForGYL.EXECUTE_NON)) {
				int rows = inventoryPlanService.update(checkHeadDO);
				if (rows > 0) {
					if (Objects.nonNull(checkBodys)) {
						List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);

						for (InventoryPlanItemDO bodyNew : lsitBodyDO) {
							if (Objects.nonNull(bodyNew.getId())) {
								inventoryPlanItemService.update(bodyNew);
							} else {
								bodyNew.setHeadId(checkHeadDO.getId());
								inventoryPlanItemService.save(bodyNew);
							}
						}
					}
					if (deleItemIds.length > 0) {
						inventoryPlanItemService.batchRemove(deleItemIds);
					}
                    Map<String,Object>  map= new HashMap<>();
                    map.put("id",checkHeadDO.getId());
                    return R.ok(map);
				} else {
					return R.error(messageSourceHandler.getMessage("apis.check.saveChangePlan", null));
				}
			} else {
				return R.error(messageSourceHandler.getMessage("apis.check.saveChangePlanTwo", null));
			}
	}else{
		checkHeadDO.setCode(purchaseContractCode());
		checkHeadDO.setCheckStatus(ConstantForGYL.EXECUTE_NON);
		int rows = inventoryPlanService.save(checkHeadDO);
		List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
		if (rows > 0) {

			for (InventoryPlanItemDO body : lsitBodyDO) {
				body.setHeadId(checkHeadDO.getId());
				inventoryPlanItemService.save(body);
			}
			Map<String,Object>  map= new HashMap<>();
			map.put("id",checkHeadDO.getId());
			return R.ok(map);
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.addCheck", null));
		}
	}
}

	@Override
	public R deletBatchPlanNow(Long planId){

		// 如果为执行中或者已执行完毕的状态则不允许删除
		InventoryPlanDO checkHeadDO = inventoryPlanService.get(planId);
		if ( Objects.equals(checkHeadDO.getCheckStatus(),ConstantForGYL.EXECUTE_NON)) {// 23-->190未执行

			//允许删除
			inventoryPlanService.remove(planId);
			inventoryPlanItemService.removeByPlanId(planId);
			return R.ok();
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.deletPlan",null));
		}
	}

	@Override
	public R doInventoryPlan(Long planId){

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("id", planId);

		List<Map<String, Object>> headDetails = inventoryPlanService.listByDates(params);
		List<Map<String, Object>> bodyDetails = inventoryPlanService.getProMsgByHeadId(params);
		params.remove("id");

		if (headDetails.size() > 0 ) {
			params.put("headDetails", headDetails);
			params.put("bodyDetails", bodyDetails);
		} else {
			params.put("headDetails", "");
			params.put("bodyDetails", "");
		}
		result.put("data", params);
		return R.ok(result);
	}

	private String purchaseContractCode() {
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.PLAN_PDFA);
		Map<String, Object> param = com.google.common.collect.Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<InventoryPlanDO> list = this.list(param);
		String taskNo = null;
		if (!list.isEmpty()) {
			taskNo = list.get(0).getCode();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}


	@Override
	public R savePlanDetail(InventoryPlanDO checkHeadDO,String checkBodys){

		//只能修改状态为24(191执行中)，且未生成盈亏单的方案，未生成盈亏的盘点结果。
		Long planId =checkHeadDO.getId();
		InventoryPlanDO checkStatus = inventoryPlanService.get(planId);
		checkHeadDO.setId(planId);
		Map<String, Object> queryPy = Maps.newHashMap();
		Map<String, Object> queryPk =Maps.newHashMap();
		queryPy.put("headId", planId);
		queryPy.put("documentType", ConstantForGYL.PYDJ);
		queryPk.put("headId", planId);
		queryPk.put("documentType", ConstantForGYL.PKDJ);
		int rowsPy = inventoryPlanFitlossDao.countOfYKCount(queryPy);
		int linesPk = inventoryPlanFitlossDao.countOfYKCount(queryPk);

		if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus()) && rowsPy != 0) {
			//此盘点已生成盘盈单，不允许修改
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResult",null));
		} else if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus()) && linesPk != 0) {
			//此盘点已生成盘亏单，不允许修改！
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResultTwo",null));
		} else if ( Objects.equals(ConstantForGYL.EXECUTE_NON ,checkStatus.getCheckStatus())) {
			//允许 且将状态改为24
			// 23-->190未执行 24-->191执行中 25--->192执行结束
			checkHeadDO.setCheckStatus(ConstantForGYL.EXECUTE_NOW );
			int rows = inventoryPlanService.update(checkHeadDO);
			if (rows > 0) {
				if (null != checkBodys && !"".equals(checkBodys)) {
					List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
					for (InventoryPlanItemDO body : lsitBodyDO) {
						body.setHeadId(checkHeadDO.getId());
						inventoryPlanItemService.update(body);
					}
				}
				return R.ok();
			} else {
				//保存方案失败，请检查参数值！
				return R.error(messageSourceHandler.getMessage("apis.check.addCheck",null));
			}
		} else if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus())  && rowsPy == 0 && linesPk == 0) {
			//允许  不用改变此时24-->191执行中
			int rows = inventoryPlanService.update(checkHeadDO);
			if (rows > 0) {
				if (null != checkBodys && !"".equals(checkBodys)) {
					List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
					for (InventoryPlanItemDO body : lsitBodyDO) {
						body.setHeadId(checkHeadDO.getId());
						inventoryPlanItemService.update(body);
					}
				}
				return R.ok();
			} else {
				return R.error(messageSourceHandler.getMessage("apis.check.addCheck",null));
			}
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResultT",null));
		}
	}


	@Override
	public R buildWinStock(Long planId) {

		InventoryPlanDO headDO = inventoryPlanService.get(planId);
		if (headDO != null) {
			if (!Objects.equals(ConstantForGYL.EXECUTE_OVER, headDO.getCheckStatus())) {
				return R.error(messageSourceHandler.getMessage("scm.checkPlan.planIsWinOrLOssError", null));
			}
			Map<String, Object> params = new HashMap<>();
			params.put("headId", planId);
			params.put("profitLoss", 1);
			//是否有盘盈（rows>0 是）
			int rows = inventoryPlanItemService.countOfWinLoss(params);
			//查找出盈数据
			List<Map<String, Object>> profitLossMsg = inventoryPlanItemService.getProfitLossMsg(params);

			params.put("documentType", ConstantForGYL.PYDJ);
			//是否已经生成其他入库 headId+documentType

			int otherInLines = inventoryPlanFitlossService.countOfOtherByPY(params);
			int linesPL = inventoryPlanFitlossService.count(params);   //是否已经生成盘赢单（lines>0 是）

			if (rows > 0 && otherInLines == 0 && linesPL == 0) {
				Map<String, Object> result = new HashMap<>();

				//将盘盈数据保存至盈亏表中,保存后并验证更改方案的状态为25
				inventoryPlanFitlossService.saveProfitORLoss(profitLossMsg, ConstantForGYL.PYDJ);
				Map<String,Object>  maps= new HashMap<>();
				maps.put("headId",planId);
				maps.put("documentType",ConstantForGYL.PYDJ);
				List<InventoryPlanFitlossDO> inventoryPlanFitlossDOS = inventoryPlanFitlossService.list(maps);
				//返回生成其他入库的数据。
				params.remove("documentType");
				DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PYDJ.intValue());
				if(dictionaryDO!=null){
					for(Map<String, Object> map:profitLossMsg){
						map.put("documentTypeId",ConstantForGYL.PYDJ);
						map.put("documentTypeName",dictionaryDO.getName());
						map.put("code",inventoryPlanFitlossDOS.get(0).getCode());
					}
				}

				result.put("BodyData", profitLossMsg);
				return R.ok(result);

			} else if (rows > 0 && otherInLines > 0) {
				//盘盈数据已生成其他入库，无法再次生成！
				return R.error(messageSourceHandler.getMessage("apis.check.buildWinStock", null));

			} else if (rows == 0) {
				//"此次盘点无盘盈！"
				return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockA", null));

			} else if (rows > 0 && otherInLines == 0 && linesPL > 0) {

				DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PYDJ.intValue());
				Map<String,Object>  maps= new HashMap<>();
				maps.put("headId",planId);
				maps.put("documentType",ConstantForGYL.PYDJ);
				List<InventoryPlanFitlossDO> inventoryPlanFitlossDOS = inventoryPlanFitlossService.list(maps);
				if(dictionaryDO!=null){
					for(Map<String, Object> map:profitLossMsg){
						map.put("documentTypeId",ConstantForGYL.PYDJ);
						map.put("documentTypeName",dictionaryDO.getName());
						map.put("code",inventoryPlanFitlossDOS.get(0).getCode());
					}
				}
				Map<String, Object> result = new HashMap<>();
				//返回生成其他入库的数据。
				result.put("BodyData", profitLossMsg);
				return R.ok(result);
			} else {
				return R.error();
			}
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD", null));
		}
	}


	@Override
	public R buildLossStock(Long planId) {
		InventoryPlanDO headDO = inventoryPlanService.get(planId);
		if (headDO != null) {
			if (!Objects.equals(ConstantForGYL.EXECUTE_OVER, headDO.getCheckStatus())) {
				return R.error(messageSourceHandler.getMessage("scm.checkPlan.planIsWinOrLOssError", null));
			}
			Map<String, Object> params = new HashMap<>();
			params.put("headId", planId);
			params.put("profitLoss", -1);
			//是否有盘亏(可以/需要生成盘盈单) （rows>0 是）
			int rows = inventoryPlanItemService.countOfWinLoss(params);
			//找出盘亏数据
			List<Map<String, Object>> profitLossMsg = inventoryPlanItemService.getProfitLossMsg(params);

			params.put("documentType", ConstantForGYL.PKDJ);
			//入库子表中是否已经存在
			int otherInLines = inventoryPlanFitlossService.countOfOutByPK(params);
			//是否已经生成盘亏单（lines>0 是）
			int linesPL = inventoryPlanFitlossService.count(params);
			if (rows > 0 && otherInLines == 0 && linesPL == 0) {
				Map<String, Object> result = new HashMap<>();
				//将盘盈数据保存至盈亏表中,保存后并验证更改方案的状态为25
				Boolean aBoolean = inventoryPlanFitlossService.saveProfitORLoss(profitLossMsg, ConstantForGYL.PKDJ);
				//允许返回生成其他入库的数据，但是盈亏单不更新
				params.remove("documentType");
				Map<String,Object>  maps= new HashMap<>();
				maps.put("headId",planId);
				maps.put("documentType",ConstantForGYL.PKDJ);
				List<InventoryPlanFitlossDO> inventoryPlanFitlossDOS = inventoryPlanFitlossService.list(maps);
				DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PKDJ.intValue());
				if(dictionaryDO!=null){
					for(Map<String, Object> map:profitLossMsg){
						map.put("documentTypeId",ConstantForGYL.PKDJ);
						map.put("documentTypeName",dictionaryDO.getName());
						map.put("code",inventoryPlanFitlossDOS.get(0).getCode());
					}
				}
				result.put("BodyData", profitLossMsg);
				return R.ok(result);

			} else if (rows > 0 && otherInLines > 0) {
				//盘亏数据已生成其他出库，无法再次生成！
				return R.error(messageSourceHandler.getMessage("apis.check.buildLossStock", null));

			} else if (rows == 0) {
				//此次盘点无盘亏
				return R.ok(messageSourceHandler.getMessage("apis.check.buildLossStockA", null));

			} else if (rows > 0 && otherInLines == 0 && linesPL > 0) {

				Map<String, Object> result = new HashMap<>();
				DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PKDJ.intValue());
				Map<String,Object>  maps= new HashMap<>();
				maps.put("headId",planId);
				maps.put("documentType",ConstantForGYL.PKDJ);
				List<InventoryPlanFitlossDO> inventoryPlanFitlossDOS = inventoryPlanFitlossService.list(maps);
				if(dictionaryDO!=null){
					for(Map<String, Object> map:profitLossMsg){
						map.put("documentTypeId",ConstantForGYL.PKDJ);
						map.put("documentTypeName",dictionaryDO.getName());
						map.put("code",inventoryPlanFitlossDOS.get(0).getCode());
					}
				}
				result.put("BodyData", profitLossMsg);
				return R.ok(result);
			} else {
				return R.error();
			}
		} else {
			return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockD", null));
		}
	}

	@Override
	public R disposePhoneCheckResuls(Long planId,String checkBodys){
		Map<String,Object>  map= new HashMap<>();
		map.put("headId",planId);
		InventoryPlanDO planDO = this.get(planId);
		List<InventoryPlanItemDO> planItemDos = inventoryPlanItemService.list(map);

		if(Objects.nonNull(planDO)){
			if (Objects.equals(planDO.getCheckStatus(), ConstantForGYL.EXECUTE_OVER)) {
				return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResultT", null));
			} else {
				JSONArray jsonArray = JSON.parseArray(checkBodys);
				for (Object jsonMap : jsonArray) {
					Map<String, Object> masp = (Map<String, Object>) jsonMap;
					String qrMw = masp.get("materielId").toString() + "-" + masp.get("warehouse").toString();
						//无批次默认不传
                    String qrBatch=masp.containsKey("batch")?masp.get("batch").toString():"";

					BigDecimal qrCheckCount = new BigDecimal(masp.get("checbatchkCount").toString());
//					Long qrId =Long.valueOf(masp.get("qrId").toString());
					String qrId=masp.get("qrId").toString();

					for (InventoryPlanItemDO inventoryPlanItemDO:planItemDos) {
						String itemMw = inventoryPlanItemDO.getMaterielId().toString() + "-" + inventoryPlanItemDO.getWarehouse().toString();
                        String itemBatch =inventoryPlanItemDO.getBatch()==null?"":inventoryPlanItemDO.getBatch().toLowerCase();

						String qrIdCount = inventoryPlanItemDO.getQrIdCount()==null?"":inventoryPlanItemDO.getQrIdCount().toString();
						String str="=";
						qrIdCount=qrIdCount.replaceAll(str,":");

						BigDecimal systemCount = inventoryPlanItemDO.getSystemCount();
						BigDecimal checkCount = inventoryPlanItemDO.getCheckCount()==null?BigDecimal.ZERO:inventoryPlanItemDO.getCheckCount();
						BigDecimal profitLoss = inventoryPlanItemDO.getProfitLoss()==null?BigDecimal.ZERO:inventoryPlanItemDO.getProfitLoss();

						if (Objects.equals(qrMw, itemMw) && Objects.equals(qrBatch, itemBatch)) {

							if(StringUtils.isNotEmpty(qrIdCount)){

								JSONObject qrIdCounts = JSONObject.fromObject(qrIdCount);

								Map<String, Object> qrIdCountMap = (Map<String, Object>)qrIdCounts;
								if(qrIdCountMap.containsKey(qrId)){
									//更改数量  先减后加
									BigDecimal newCheckCount =(checkCount.subtract(new BigDecimal(qrIdCountMap.get(qrId).toString()))).add(qrCheckCount);
									inventoryPlanItemDO.setCheckCount(newCheckCount);
									inventoryPlanItemDO.setProfitLoss(systemCount.subtract(newCheckCount));
                                    qrIdCountMap.put(qrId,qrCheckCount);
									inventoryPlanItemDO.setQrIdCount(qrIdCountMap.toString());
								}else{
                                    //直接添加盘点数量和，并将二维码的id和数量放进qrIdCount
                                    BigDecimal newCheckCount =(checkCount.add(qrCheckCount));
                                    inventoryPlanItemDO.setCheckCount(newCheckCount);
                                    inventoryPlanItemDO.setProfitLoss(systemCount.subtract(newCheckCount));
                                    qrIdCountMap.put(qrId,qrCheckCount);
                                    inventoryPlanItemDO.setQrIdCount(qrIdCountMap.toString());
                                }
							}else{
								//首次盘点
								Map<String,Object>  qrIdCountMap= new HashMap<>();
								qrIdCountMap.put(qrId,qrCheckCount);
								BigDecimal newCheckCount =(checkCount.add(qrCheckCount));
								inventoryPlanItemDO.setCheckCount(newCheckCount);
								inventoryPlanItemDO.setProfitLoss(systemCount.subtract(newCheckCount));
								inventoryPlanItemDO.setQrIdCount(qrIdCountMap.toString());
							}
							break;
						}
					}
				}
				//批量更新方案
				inventoryPlanItemService.batchUpdate(planItemDos);

				//将主表qrId做标记
				InventoryPlanDO inventoryPlanDO=new InventoryPlanDO();
				inventoryPlanDO.setId(planId);
				inventoryPlanDO.setQrSign(1);
				this.update(inventoryPlanDO);

				return R.ok();
			}
		}else{
			return  R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
		}
	}


	@Override
	public R disposeImportExcel(MultipartFile file){

		if (file.isEmpty()) {
			return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		List<InventoryPlanEntity> InventoryPlanList;
		Map<String,String> InventoryPlanReuseIMap;
		try {
			InventoryPlanList = ExcelImportUtil.importExcel(file.getInputStream(), InventoryPlanEntity.class, params);
			InventoryPlanList = InventoryPlanList.stream().filter(InventoryPlanEntity -> InventoryPlanEntity.getId() != null).collect(Collectors.toList());
            InventoryPlanReuseIMap=InventoryPlanList.stream().collect(Collectors.toMap(InventoryPlanEntity::getId,InventoryPlanEntity::getCheckCount,(k1,k2)->k1));

        } catch (Exception e) {
			return R.error(messageSourceHandler.getMessage("file.upload.error", null));
		}
		if(InventoryPlanList.size()>0){
			for(InventoryPlanEntity inventoryPlanEntity:InventoryPlanList){
				if(StringUtils.isEmpty(inventoryPlanEntity.getCheckCount())){
					return R.error(messageSourceHandler.getMessage("scm.inventoryPlan.haveNoData", null));
				}
			}
		}
		if(InventoryPlanList.size()>InventoryPlanReuseIMap.size()){
			return R.error(messageSourceHandler.getMessage("scm.inventoryPlan.haveNoId", null));
		}
        List<InventoryPlanItemDO> listItemDo=new ArrayList<>();
        for(InventoryPlanEntity inventoryPlanEntity:InventoryPlanList){
            String id=inventoryPlanEntity.getId();
            BigDecimal checkCount=new BigDecimal(inventoryPlanEntity.getCheckCount());
            InventoryPlanItemDO inventoryPlanItemDO = inventoryPlanItemService.get(Long.parseLong(id));
            BigDecimal systemCount= inventoryPlanItemDO.getSystemCount();

            inventoryPlanItemDO.setCheckCount(checkCount);
            inventoryPlanItemDO.setProfitLoss(systemCount.subtract(checkCount));
            listItemDo.add(inventoryPlanItemDO);
        }

        if(listItemDo.size()>0){
            InventoryPlanDO inventoryPlanDO = this.get(listItemDo.get(0).getHeadId());
            inventoryPlanDO.setCheckStatus(ConstantForGYL.EXECUTE_NOW);
            this.update(inventoryPlanDO);
            inventoryPlanItemService.batchUpdate(listItemDo);
            return R.ok();
        }else{
            return R.error();
        }



}












}
