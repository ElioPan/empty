package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.scm.dao.StockInItemDao;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.service.StockInItemService;
import com.ev.scm.service.StockInService;
import com.ev.scm.service.StockItemService;
import com.ev.scm.service.StockService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement
@Service
public class StockInItemServiceImpl implements StockInItemService {
	@Autowired
	private StockInItemDao stockInItemDao;
	@Autowired
	private StockService stockService;
	@Autowired
	private StockItemService stockDetailService;
	@Autowired
	private StockInItemService stockInItemService;
	@Autowired
	private StockInService stockInService;
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public StockInItemDO get(Long id){
		return stockInItemDao.get(id);
	}

	@Override
	public List<StockInItemDO> list(Map<String, Object> map){
		return stockInItemDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return stockInItemDao.count(map);
	}

	@Override
	public int save(StockInItemDO propurchaseInbody){
		return stockInItemDao.save(propurchaseInbody);
	}

	@Override
	public int update(StockInItemDO propurchaseInbody){
		return stockInItemDao.update(propurchaseInbody);
	}

	@Override
	public int remove(Long id){
		return stockInItemDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return stockInItemDao.batchRemove(ids);
	}

	@Override
	public int updateExpense(Map<String, Object> map) {
		return stockInItemDao.updateExpense(map);
	}

	@Override
	public List<Map<String, Object>> getListBystockInId(Map<String, Object> map) {
		return stockInItemDao.getListBystockInId(map);
	}

	@Override
	public Map<String, Object> getTotailCountAmount(Long id) {
		return stockInItemDao.getTotailCountAmount(id);
	}

	@Override
	public int batchUpdate(List<StockInItemDO> stockOutItemDOs) {
		return stockInItemDao.batchUpdate(stockOutItemDOs);
	}

	@Override
	public int batchSave(List<StockInItemDO> stockDOs) {
		return stockInItemDao.batchSave(stockDOs);
	}

	@Override
	public 	List<StockInItemDO> getItemDetailById(Map<String, Object> map){
		return stockInItemDao.getItemDetailById(map);
	}

	@Override
	public List<Map<String, Object>> getItemDate(Map<String, Object> map) {
		return stockInItemDao.getItemDate(map);
	}

	@Override
	public int getInCountOfContract(Map<String, Object> map) {
		return stockInItemDao.getInCountOfContract(map);
	}

	@Override
	public String getProByHeadId(Long id , String code) {

		List<Map<String, Object>> inProList = stockInItemDao.getProByHeadId(id);


		if (inProList.size() > 0) {

			for (Map<String, Object> mapDertails : inProList) {

				//将数据保存至库存表;
				StockDO stockDO = new StockDO();
				stockDO.setEnteringTime(new Date());
				stockDO.setCode(code + DateFormatUtil.getWorkOrderno());
				stockDO.setMaterielId(Long.parseLong(mapDertails.get("materielId").toString()));
				stockDO.setBatch(mapDertails.get("batch").toString());
				stockDO.setCount((BigDecimal) mapDertails.get("count"));
				stockDO.setUnitPrice((BigDecimal) mapDertails.get("unitPrice"));
				stockDO.setAvailableCount((BigDecimal) mapDertails.get("count"));
				stockDO.setSourceCompany(Long.parseLong(mapDertails.get("sourceCompany").toString()));
				stockDO.setWarehouse(Long.parseLong(mapDertails.get("warehouse").toString()));
				stockDO.setWarehLocation(Long.parseLong(mapDertails.get("warehLocation").toString()));

				int saveRows = stockService.save(stockDO);
				if (saveRows > 0) {
					//保存库存明细表
					StockItemDO stockItemDo = new StockItemDO();
					stockItemDo.setStockId(stockDO.getId());
					stockItemDo.setInheadId(Long.parseLong(mapDertails.get("headId").toString()));
					stockItemDo.setInbodyId(Long.parseLong(mapDertails.get("inbodyId").toString()));
					//入库类型 stragoeType
					stockItemDo.setInOutType(Long.parseLong(mapDertails.get("storageType").toString()));
					stockItemDo.setUnitPrice((BigDecimal) mapDertails.get("unitPrice"));
					stockItemDo.setCount((BigDecimal) mapDertails.get("count"));
					if(mapDertails.containsKey("sourceSingletype")){
						stockItemDo.setSourceType(Long.parseLong(mapDertails.get("sourceType").toString()));
					}
					stockDetailService.save(stockItemDo);
				}
			}
			return "OK";
		} else {
			return "error";
		}
	}

	@Override
	public int removeByInHeadId(Long[] id) {
		return stockInItemDao.removeByInHeadId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String addOtherAudit(Long id) {

		List<Map<String, Object>> inProList = stockInItemDao.getProByHeadId(id);

		if (inProList.size() > 0) {

			for (Map<String, Object> mapDertails : inProList) {
				//将数据保存至库存表;
				StockDO stockDO = new StockDO();
				stockDO.setEnteringTime(new Date());
				stockDO.setCode(ConstantForGYL.QTRK_PREFIX + DateFormatUtil.getWorkOrderno());
				stockDO.setMaterielId(Long.parseLong(mapDertails.get("materielId").toString()));
				stockDO.setBatch(mapDertails.get("batch").toString());
				stockDO.setCount((BigDecimal) mapDertails.get("count"));
				stockDO.setAvailableCount((BigDecimal) mapDertails.get("count"));
				stockDO.setUnitPrice((BigDecimal) mapDertails.get("unitPrice"));
				stockDO.setAmount((BigDecimal) mapDertails.get("amount"));
				stockDO.setSourceCompany(Long.parseLong(mapDertails.get("sourceCompany").toString()));
				stockDO.setWarehouse(Long.parseLong(mapDertails.get("warehouse").toString()));
				stockDO.setWarehLocation(Long.parseLong(mapDertails.get("warehLocation").toString()));
				int saveRows = stockService.save(stockDO);

				if (saveRows > 0) {
					//保存库存明细表
					StockItemDO stockItemDo = new StockItemDO();
					stockItemDo.setStockId(stockDO.getId());
					stockItemDo.setInheadId(Long.parseLong(mapDertails.get("inheadId").toString()));
					stockItemDo.setInbodyId(Long.parseLong(mapDertails.get("inbodyId").toString()));

					//入库类型 stragoeType
					stockItemDo.setInOutType(Long.parseLong(mapDertails.get("storageType").toString()));
					stockItemDo.setUnitPrice((BigDecimal) mapDertails.get("unitPrice"));
					stockItemDo.setCount((BigDecimal) mapDertails.get("count"));

					stockDetailService.save(stockItemDo);
				}
			}
			return "ok";
		} else {
			return "error";
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)//@Transactional(rollbackFor=Exception.class)
	public int updateChangeDetails(String proOldInBodyDO, String proNewInBodyDO, Long[] deletIds, Long headId) {
//		List<StockInItemDO> inbodyDOS=new ArrayList<StockInItemDO>();//测试使用
//        List<StockInItemDO> inbodyDOSx=JSON.parseArray(proNewInBodyDO,StockInItemDO.class);
//        inbodyDOS.add(0,inbodyDOS);  //测试使用

		//判断前端传值是否空值
		try{
			//更新已有行
			if (null != proOldInBodyDO && !"".equals(proOldInBodyDO)) {
				List<StockInItemDO> inbodyOldDOS = JSON.parseArray(proOldInBodyDO, StockInItemDO.class);
				for (StockInItemDO inOldDo : inbodyOldDOS) {
					this.update(inOldDo);
				}
			}
			//保存新增明细行
			if (null != proNewInBodyDO && !"".equals(proNewInBodyDO)) {
				List<StockInItemDO> inbodyNewDOS = JSON.parseArray(proNewInBodyDO, StockInItemDO.class);

				for (StockInItemDO inNewDo : inbodyNewDOS) {
					inNewDo.setInheadId(headId);
					this.save(inNewDo);
				}
			}
			//删除去除行
			if (deletIds.length > 0) {
				this.batchRemove(deletIds);

			}
			return 1;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> getBodyDetalByHeadId(Map<String, Object> map) {
		return stockInItemDao.getBodyDetalByHeadId(map);
	}


	@Override
	public void ditalWithChange(StockInDO stockInDO, String proListInBodyDO, Long headId) {

		stockInService.update(stockInDO);

		//删除全部主表主键下子表信息
		Long ids[]=new  Long[1];
		ids[0]=headId;

		//TODO  以下方法检验是否错误
		this.removeByInHeadId(ids);

		List<StockInItemDO> inbodyNewDOS = JSON.parseArray(proListInBodyDO, StockInItemDO.class);
		for (StockInItemDO inNewDo : inbodyNewDOS) {
			inNewDo.setInheadId(headId);
			this.save(inNewDo);
		}

	}

	@Override
	public int countOfList(Map<String, Object> map) {
		return stockInItemDao.countOfList(map);
	}

	@Override
	public List<Map<String, Object>> getlistForMap(Map<String, Object> map) {
		return stockInItemDao.getlistForMap(map);
	}

	@Override
	public List<Map<String, Object>> deatilOfBody(Map<String, Object> map) {
		return stockInItemDao.deatilOfBody(map);
	}


	private String purchaseContractCode(Long type) {

		DictionaryDO dictionaryDO = dictionaryService.get(type.intValue());
		String typeValue=null;
		if(dictionaryDO!=null){
			typeValue=dictionaryDO.getValue();
		}
		String maxNo = DateFormatUtil.getWorkOrderno(typeValue);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<StockDO> list = stockService.list(param);
		String taskNo = null;
		if (!list.isEmpty()) {
			taskNo = list.get(0).getCode();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}



	@Override
	public  String addAllTypeInStock(Long id,Long storageTypeId){

		List<Map<String, Object>> inProList = stockInItemDao.getProByHeadId(id);

		if (inProList.size() > 0) {

			for (Map<String, Object> mapDertails : inProList) {
				//将数据保存至库存表;
				StockDO stockDO = new StockDO();
				stockDO.setEnteringTime(new Date());
				stockDO.setCode(purchaseContractCode(storageTypeId));
				stockDO.setMaterielId(Long.parseLong(mapDertails.containsKey("materielId")?mapDertails.get("materielId").toString():"0"));
				stockDO.setBatch(mapDertails.containsKey("batch")?mapDertails.get("batch").toString():null);

				stockDO.setCount((mapDertails.containsKey("count")?new BigDecimal(mapDertails.get("count").toString()): BigDecimal.ZERO));
				stockDO.setAvailableCount( (mapDertails.containsKey("count")?new BigDecimal(mapDertails.get("count").toString()):BigDecimal.ZERO));
				stockDO.setUnitPrice((mapDertails.containsKey("unitPrice")?new BigDecimal(mapDertails.get("unitPrice").toString()):BigDecimal.ZERO));
				stockDO.setAmount((mapDertails.containsKey("amount")?new BigDecimal(mapDertails.get("amount").toString()):BigDecimal.ZERO)) ;

				stockDO.setSourceCompany(Long.parseLong(mapDertails.containsKey("sourceCompany")?mapDertails.get("sourceCompany").toString():"0"));
				stockDO.setWarehouse(Long.parseLong(mapDertails.containsKey("warehouse")?mapDertails.get("warehouse").toString():null));
				stockDO.setWarehLocation(Long.parseLong(mapDertails.containsKey("warehLocation")?mapDertails.get("warehLocation").toString():"0"));
				int saveRows = stockService.save(stockDO);
				if (saveRows > 0) {
					//保存库存明细表
					StockItemDO stockItemDo = new StockItemDO();
					stockItemDo.setStockId(stockDO.getId());
					stockItemDo.setInheadId(Long.parseLong(mapDertails.containsKey("headId")?mapDertails.get("headId").toString():"0"));
					stockItemDo.setInheadId(Long.parseLong(mapDertails.containsKey("inbodyId")?mapDertails.get("headId").toString():"0"));

					//入库类型 stragoeType
					stockItemDo.setInOutType(Long.parseLong(mapDertails.containsKey("storageType")?mapDertails.get("storageType").toString():null));
					stockItemDo.setUnitPrice((BigDecimal) (mapDertails.containsKey("unitPrice")?new BigDecimal(mapDertails.get("unitPrice").toString()):BigDecimal.ZERO));
					stockItemDo.setCount((BigDecimal)(mapDertails.containsKey("count")?new BigDecimal(mapDertails.get("count").toString()):BigDecimal.ZERO));

					stockDetailService.save(stockItemDo);
				}
			}
			return "ok";
		} else {
			return "error";
		}



	}



}

