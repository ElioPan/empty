package com.ev.scm.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.custom.service.FacilityService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.scm.dao.StockDao;
import com.ev.scm.domain.StockAnalysisDO;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.domain.StockStartDO;
import com.ev.scm.service.*;
import com.ev.scm.vo.StockEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StockServiceImpl implements StockService {
	@Autowired
	private StockDao stockDao;
	@Autowired
	private FacilityLocationService facilityLocationService;
	@Autowired
	private FacilityService facilityService;
	@Autowired
	private StockStartService stockStartService;
	@Autowired
	private MaterielService materielService;
	@Autowired
	private StockAnalysisService stockAnalysisService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private StockInService stockInService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private StockOutItemService stockOutItemService;
	
	@Override
	public StockDO get(Long id){
		return stockDao.get(id);
	}
	
	@Override
	public List<StockDO> list(Map<String, Object> map){
		return stockDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stockDao.count(map);
	}
	
	@Override
	public int save(StockDO stock){
		return stockDao.save(stock);
	}
	
	@Override
	public int update(StockDO stock){
		return stockDao.update(stock);
	}
	
	@Override
	public int remove(Long id){
		return stockDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return stockDao.batchRemove(ids);
	}


    @Override
    public int batchSave(List<StockDO> stockDOs) {
        return  stockDao.batchSave(stockDOs);
    }

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> results) {
		return stockDao.listForMap(results);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> results) {
		return stockDao.countForMap(results);
	}

	@Override
	public int batchUpdate(List<StockDO> batchUpdate) {
		return stockDao.batchUpdate(batchUpdate);
	}

	@Override
	public R importExcel(MultipartFile file) {
		if (file.isEmpty()) {
			return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
		}
		List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
		if (list.size() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
		}
		if (list.get(0).getStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
		}

		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		List<StockEntity> stockEntityList;
		try {
			stockEntityList  =  ExcelImportUtil.importExcel(file.getInputStream(), StockEntity.class, params);
		}catch(Exception e) {
			return R.error(messageSourceHandler.getMessage("file.upload.error", null));
		}
		String facilityLocationName;
		String facilityName;
		if (stockEntityList.size() > 0) {
			for (StockEntity stockEntity : stockEntityList) {
//                facilityLocationName = stockEntity.getFacilityLocationName();
				facilityName = stockEntity.getFacilityName();
				if (StringUtils.isEmpty(stockEntity.getSerialno())
//                        || StringUtils.isEmpty(facilityLocationName)
						|| StringUtils.isEmpty(facilityName)
						|| !NumberUtils.isNumber(stockEntity.getTotalCount())
						|| !NumberUtils.isNumber(stockEntity.getAmount())
//                        || StringUtils.isEmpty(stockEntity.getBatch()
				) {
					return R.error(messageSourceHandler.getMessage("basicInfo.correct.param", null));
				}

			}

			Map<String, Object> emptyMap = Maps.newHashMap();

			// 物料验证
			List<String> codes = stockEntityList
					.stream()
					.map(StockEntity::getSerialno)
					.distinct()
					.collect(Collectors.toList());
			emptyMap.put("codes",codes);
			List<MaterielDO> materielDOs = materielService.list(emptyMap);
			if (materielDOs.size() != codes.size()) {
				List<String> materielCodes = materielDOs
						.stream()
						.map(MaterielDO::getSerialNo)
						.collect(Collectors.toList());
				codes.removeAll(materielCodes);
				String[] notExist = {codes.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.materiel.notExist", notExist));
			}

			// 批次管理验证
			List<String> isLotError = Lists.newArrayList();
			Map<String, Integer> isLotMap = materielDOs
					.stream()
					.collect(Collectors.toMap(MaterielDO::getSerialNo, MaterielDO::getIsLot));
			for (StockEntity stockEntity : stockEntityList) {
				String serialno = stockEntity.getSerialno();
				Integer isLot = isLotMap.get(serialno);
				String batch = stockEntity.getBatch();
				if ((isLot == 1 && StringUtils.isEmpty(batch)) || (isLot == 0 && StringUtils.isNoneEmpty(batch))) {
					isLotError.add(serialno);
				}
			}
			if (isLotError.size() > 0) {
				List<String> collect = isLotError.stream().distinct().collect(Collectors.toList());
				String []args = {collect.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.materiel.isLotError", args));
			}
			Map<String, Integer> idToMap = materielDOs.stream()
					.collect(Collectors.toMap(MaterielDO::getSerialNo, MaterielDO::getId));

			// 仓库验证
			List<String> facilityCodes = stockEntityList
					.stream()
					.map(StockEntity::getFacilityName)
					.distinct()
					.collect(Collectors.toList());
			emptyMap.put("codes",facilityCodes);
			List<FacilityDO> facilityDOs = facilityService.list(emptyMap);
			if (facilityDOs.size() != facilityCodes.size()) {
				List<String> collect = facilityDOs
						.stream()
						.map(FacilityDO::getName)
						.collect(Collectors.toList());
				facilityCodes.removeAll(collect);
				String[] notExist = {facilityCodes.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.facility.notExist", notExist));
			}

			// 检查仓库与仓位是否匹配
			Map<String, Integer> facilityIdToMap = facilityDOs
					.stream()
					.collect(Collectors.toMap(FacilityDO::getName, FacilityDO::getId));
			List<Integer> facilityIds = new ArrayList<>(facilityIdToMap.values());
			emptyMap.put("codes",facilityIds);
			List<FacilityLocationDO> locationDOs = facilityLocationService.list(emptyMap);

			// 检查库位是否存在
			boolean isLocationNull = false;
			if (locationDOs.size() == 0) {
				isLocationNull = true;
			}
			if(isLocationNull){
				List<String> collect = stockEntityList.stream()
						.filter(stockEntity -> stockEntity.getFacilityLocationName() != null)
						.map(StockEntity::getFacilityLocationName)
						.collect(Collectors.toList());
				String[] args = {collect.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.location.notExist", args));
			}
			// 检验库位是否匹配
			Map<Integer, List<FacilityLocationDO>> idToLocationDOs = locationDOs
					.stream()
					.collect(Collectors.groupingBy(FacilityLocationDO::getFacilityId));
			List<FacilityLocationDO> facilityLocationDOS;
			List<Map<String,String>> locationNameList = Lists.newArrayList();
			Map<String,String> map;
			for (StockEntity stockEntity : stockEntityList) {
				String facilityLocation = stockEntity.getFacilityLocationName();
				if (facilityLocation == null) {
					continue;
				}
				Integer facility = facilityIdToMap.get(stockEntity.getFacilityName());
				facilityLocationDOS = idToLocationDOs.get(facility);
				boolean present = facilityLocationDOS
						.stream()
						.anyMatch(facilityLocationDO -> facilityLocation.equals(facilityLocationDO.getName()));
				if(!present){
					map = Maps.newHashMap();
					map.put("仓库",stockEntity.getFacilityName());
					map.put("库位",facilityLocation);
					locationNameList.add(map);
				}
			}
			if (locationNameList.size() > 0) {
				String[] args = {locationNameList.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.facility.isFacilityError", args));
			}
			Map<String, Integer> locationIdToMap = locationDOs.stream().collect(Collectors.toMap(FacilityLocationDO::getName, FacilityLocationDO::getId));


			Date now = new Date();
			Long userId = ShiroUtils.getUserId();
			List<StockDO> stockDOs = Lists.newArrayList();
			StockDO stockDO;
			for (StockEntity stockEntity : stockEntityList) {
				stockDO = new StockDO();
				// 默认仓库
				facilityName = stockEntity.getFacilityName();
				facilityLocationName = stockEntity.getFacilityLocationName();
				Integer integer = locationIdToMap.get(facilityLocationName);
				stockDO.setWarehouse(facilityIdToMap.get(facilityName).longValue());
				stockDO.setWarehLocation(integer!=null?integer.longValue():null);
				stockDO.setMaterielId(idToMap.get(stockEntity.getSerialno()).longValue());
				stockDO.setBatch(stockEntity.getBatch());
				BigDecimal count = BigDecimal.valueOf(Double.parseDouble(stockEntity.getTotalCount()));
				stockDO.setEnteringTime(now);
				stockDO.setAvailableCount(count);
				stockDO.setCount(count);
				BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(stockEntity.getAmount()));
				stockDO.setAmount(amount);
				stockDO.setUnitPrice(amount.divide(count, Constant.BIGDECIMAL_ZERO));
				stockDO.setDelFlag(0);
				stockDO.setCreateBy(userId);
				stockDO.setCreateTime(now);
				stockDOs.add(stockDO);
			}
			this.batchSave(stockDOs);
		}
		return R.ok();
	}

	@Override
	public R saveStockStartTime(String yearAndMonth) {
		Calendar input = Calendar.getInstance();
		Date inputDate = DateFormatUtil.getDateByParttern(yearAndMonth);
		if (inputDate == null) {
			return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
		}
		input.setTime(inputDate);
		int year = input.get(Calendar.YEAR);
		int month = input.get(Calendar.MONTH);

		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.YEAR) == year && month == now.get(Calendar.MONTH)) {
			Calendar start = Calendar.getInstance();

			input.set(Calendar.DAY_OF_MONTH, 1);
			// 是否为修改
			List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
			if (list.size() > 0) {
				StockStartDO stockStartDO = list.get(0);
				if (stockStartDO.getStatus() == 1) {
					return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
				}
				stockStartDO.setStartTime(start.getTime());
				stockStartService.update(stockStartDO);
				return R.ok();
			}
			// 为新增
			StockStartDO stockStartDO = new StockStartDO();
			stockStartDO.setStartTime(input.getTime());
			stockStartDO.setStatus(0);
			stockStartService.save(stockStartDO);
			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("scm.stock.timeError", null));
	}

	@Override
	public R getStartTime() {
		String stockStartTime = stringRedisTemplate.opsForValue().get("stockStartTime");
		Map<String, Object> map = Maps.newHashMap();
		if (StringUtils.isEmpty(stockStartTime)) {
			// 是否为修改
			List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
			if (list.size() > 0) {
				Date startTime = list.get(0).getStartTime();
				// 将库存启用时间写入redis中
				stringRedisTemplate.opsForValue().set("stockStartTime", DateFormatUtil.getFormateDate(startTime));
				map.put("yearAndMonth", startTime);
				return R.ok(map);
			}
		}
		map.put("yearAndMonth", stockStartTime);
		return R.ok(map);
	}

	@Override
	public R initStock(String stockList, Long[] itemIds) {
		Map<String, Object> emptyMap = Maps.newHashMap();
		List<StockStartDO> list = stockStartService.list(emptyMap);
		if (list.size() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
		}
		if (list.get(0).getStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
		}

		if (itemIds.length > 0) {
			this.batchRemove(itemIds);
		}
		Date now = new Date();
		List<StockDO> itemDOs = JSON.parseArray(stockList, StockDO.class);
		List<MaterielDO> materielDOs = materielService.list(emptyMap);
		String batch;
		Integer isLot;
		if (itemDOs.size() > 0 && materielDOs.size() > 0) {
			for (StockDO itemDO : itemDOs) {
				batch = itemDO.getBatch();
				for (MaterielDO materielDO : materielDOs) {
					if (Objects.equals(materielDO.getId().longValue(), itemDO.getMaterielId())) {
						isLot = materielDO.getIsLot();
						if (isLot == 1 && StringUtils.isEmpty(batch)) {
							String[] args = {materielDO.getName()};
							return R.error(messageSourceHandler.getMessage("basicInfo.materiel.isLotError", args));
						}
						BigDecimal count = itemDO.getCount();
						BigDecimal amount = itemDO.getAmount();
						if (StringUtils.isEmpty(itemDO.getBatch())){
							itemDO.setBatch(null);
						}
						itemDO.setAvailableCount(count);
						itemDO.setUnitPrice(amount.divide(count, Constant.BIGDECIMAL_ZERO));
						itemDO.setEnteringTime(now);
						itemDO.setCreateTime(now);
						itemDO.setDelFlag(0);
						break;
					}
				}

			}
			List<StockDO> batchSave = itemDOs.stream().filter(itemDO -> itemDO.getId() == null).collect(Collectors.toList());
			if (batchSave.size() > 0) {
				this.batchSave(batchSave);
			}
			List<StockDO> batchUpdate = itemDOs.stream().filter(itemDO -> itemDO.getId() != null).collect(Collectors.toList());
			if (batchUpdate.size() > 0) {
				this.batchUpdate(batchUpdate);
			}
			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
	}

	@Override
	public R startList() {
		Map<String, Object> results = Maps.newHashMap();
		List<StockStartDO> list = stockStartService.list(results);
		int startStatus = 0;
		if (list.size() > 0) {
			Map<String, Object> param = Maps.newHashMap();
			StockStartDO stockStartDO = list.get(0);
			startStatus = stockStartDO.getStatus();
			if (startStatus == 1) {
				param.put("endTime", DateFormatUtil.getFormateDate(stockStartDO.getUpdateTime()));
			}
			List<Map<String, Object>> data = this.listForMap(param);
			Map<String, Object> countForMap = this.countForMap(param);
			results.put("data", data);
			results.put("total", countForMap);
		}
		results.put("startStatus", startStatus);
		return R.ok(results);
	}

	@Override
	public R endInitial() {
		//TODO 保存一份初始化数据
		Map<String, Object> emptyMap = Maps.newHashMap();
		List<StockStartDO> list = stockStartService.list(emptyMap);
		if (list.size() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
		}
		if (list.get(0).getStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
		}

		List<StockDO> stockDOList = this.list(emptyMap);
		StockStartDO stockStartDO = list.get(0);
		Calendar instance = Calendar.getInstance();
		instance.setTime(stockStartDO.getStartTime());
		instance.set(Calendar.DAY_OF_MONTH,1);
		Date period = instance.getTime();

		List<StockAnalysisDO> stockAnalysisDOS = Lists.newArrayList();
		List<Long> materielIds = stockDOList
				.stream()
				.map(StockDO::getMaterielId)
				.collect(Collectors.toList());
		Map<String,Object> params = Maps.newHashMap();
		params.put("materielIdList", materielIds);
		List<MaterielDO> materielDOList = materielService.list(params);
		List<Integer> idList = Lists.newArrayList();
		if (materielDOList.size() > 0) {
			// 非批次管理的入库物料
			idList = materielDOList.stream()
					.filter(materielDO -> materielDO.getIsLot()!=1)
					.map(MaterielDO::getId)
					.collect(Collectors.toList());
		}

		Map<String, BigDecimal> materielCountMap = Maps.newHashMap();
		Map<String, BigDecimal> materielAmountMap = Maps.newHashMap();
		for (StockDO stockDO : stockDOList) {
			int materielId = stockDO.getMaterielId().intValue();
			// 无批次管理的物料
			if (idList.contains(materielId)) {
				String materielIdToString = String.valueOf(materielId);
				if (materielCountMap.containsKey(materielIdToString)) {
					materielCountMap.put(materielIdToString, materielCountMap.get(materielIdToString).add(stockDO.getCount()));
					materielAmountMap.put(materielIdToString, materielCountMap.get(materielIdToString).add(stockDO.getAmount()));
					continue;
				}
				materielCountMap.put(materielIdToString, stockDO.getCount());
				materielAmountMap.put(materielIdToString, stockDO.getAmount());
				continue;
			}
			// 有批次管理的物料
			String materielIdAndBatchToString = materielId + "&" + stockDO.getBatch();
			if (materielCountMap.containsKey(materielIdAndBatchToString)) {
				materielCountMap.put(materielIdAndBatchToString, materielCountMap.get(materielIdAndBatchToString).add(stockDO.getCount()));
				materielAmountMap.put(materielIdAndBatchToString, materielCountMap.get(materielIdAndBatchToString).add(stockDO.getAmount()));
				continue;
			}
			materielCountMap.put(materielIdAndBatchToString, stockDO.getCount());
			materielAmountMap.put(materielIdAndBatchToString, stockDO.getAmount());
		}
		if (materielCountMap.size() > 0) {
			Date now = new Date();
			StockAnalysisDO stockAnalysis;
			String[] split;
			int materielId;
			String batch;
			for (String s : materielCountMap.keySet()) {
				stockAnalysis = new StockAnalysisDO();
				split = s.split("&");
				materielId = Integer.parseInt(split[0]);
				if (split.length == 2) {
					batch = split[1];
				}else {
					batch = null;
				}
				stockAnalysis.setMaterielId(materielId);
				stockAnalysis.setBatch(batch);
				stockAnalysis.setInitialCount(materielCountMap.get(s));
				stockAnalysis.setInitialAmount(materielAmountMap.get(s));
				stockAnalysis.setInCount(BigDecimal.ZERO);
				stockAnalysis.setInAmount(BigDecimal.ZERO);
				stockAnalysis.setOutAmount(BigDecimal.ZERO);
				stockAnalysis.setOutCount(BigDecimal.ZERO);
				stockAnalysis.setPeriod(period);
				stockAnalysis.setIsClose(0);
				stockAnalysis.setDelFlag(0);
				stockAnalysis.setCreateTime(now);
				stockAnalysisDOS.add(stockAnalysis);
			}
		}
		stockAnalysisService.batchInsert(stockAnalysisDOS);
		stockStartDO.setStatus(1);
		stockStartService.update(stockStartDO);
		// 将库存启用时间写入redis中
		stringRedisTemplate.opsForValue().set("stockStartTime", DateFormatUtil.getFormateDate(stockStartDO.getStartTime()));
		// 将本期时间写入redis中
		stringRedisTemplate.opsForValue().set("period", DateFormatUtil.getFormateDate(period));

		return R.ok();
	}

	@Override
	public R stockOutAccountingTime() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("period", this.getPeriodTime());
		return R.ok(result);
	}

	@Override
	public Date getPeriodTime(){
		// 获取本次期间
		String periodTime = stringRedisTemplate.opsForValue().get("period");
		if (periodTime != null) {
			return DateFormatUtil.getDateByParttern(periodTime);
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("offset", 0);
		params.put("limit", 1);
		List<StockAnalysisDO> list = stockAnalysisService.list(params);
		if (list.size() > 0) {
			StockAnalysisDO stockAnalysisDO = list.get(0);
			Date period = stockAnalysisDO.getPeriod();
			Calendar instance = Calendar.getInstance();
			instance.setTime(period);
			instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
			Date instanceTime = instance.getTime();
			params.put("period", DateFormatUtil.getFormateDate(instanceTime));
			List<StockAnalysisDO> oldList = stockAnalysisService.list(params);
			if (oldList.size() > 0 && oldList.get(0).getIsClose() == 0) {
				stringRedisTemplate.opsForValue().set("period", DateFormatUtil.getFormateDate(instanceTime));
				return instanceTime;
			} else {
				stringRedisTemplate.opsForValue().set("period", DateFormatUtil.getFormateDate(period));
				return period;
			}
		}
		return null;
	}

	@Override
	public R checkTime(String period) {
		String periodTimeForRedis = stringRedisTemplate.opsForValue().get("period");
		if (StringUtils.isNoneEmpty(periodTimeForRedis) && periodTimeForRedis.equals(period)) {
			return null;
		}

		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		if (periodTime == null) {
			return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("offset", 0);
		params.put("limit", 1);
		List<StockAnalysisDO> list = stockAnalysisService.list(params);
		if (list.size() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
		}
		StockAnalysisDO stockAnalysisDO = list.get(0);
		Date oldPeriod = stockAnalysisDO.getPeriod();
		Calendar instance = Calendar.getInstance();
		instance.setTime(oldPeriod);
		instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
		Date instanceTime = instance.getTime();
		params.put("period", DateFormatUtil.getFormateDate(instanceTime));
		List<StockAnalysisDO> oldList = stockAnalysisService.list(params);
		if (oldList.size() > 0 && oldList.get(0).getIsClose() == 0) {
			oldPeriod = instanceTime;
		}
		instance.setTime(periodTime);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		long newMillis = instance.getTimeInMillis();
		instance.setTime(oldPeriod);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		long oldMillis = instance.getTimeInMillis();
		if (oldMillis != newMillis) {
			return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
		}
		return  null;
	}

	@Override
	public R stockOutAccounting(String period) {
		R r = this.checkTime(period);
		if (r != null) {
			return r;
		}

		Map<String, Object> params = Maps.newHashMap();
		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		// 入库单
		params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
		params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
		params.put("auditSign", ConstantForGYL.OK_AUDITED);
		List<Map<String, Object>> stockInList = stockInService.listForMap(params);
		List<StockOutItemDO> stockOutList = stockOutItemService.list(params);

		// 获取本期已存在物料列表
		params.put("period", period);
		List<StockAnalysisDO> stockAnalysisList = stockAnalysisService.list(params);

		// 先将本期入库的新的物料加入分析表中
		List<StockAnalysisDO> insertStockAnalysisList = Lists.newArrayList();

		// 加权平均法出库成本的算法：(月初结存金额+本月入库金额)/（月初结存数量+本月入库数量），算出当月加权平均
		// 分批认定法：物料属性中需要设置批次管理，入库时入库单需要录入批号，出库时出库单的单价以入库时相同批号的单价作为出库单价。

		List<Integer> weightedAverageIdList = Lists.newArrayList();

		// 入库的物料
		List<Object> materielInIdList = stockInList.stream()
				.map(stringObjectMap -> stringObjectMap.get("materielId"))
				.collect(Collectors.toList());
		List<MaterielDO> materielDOInList;
		List<Integer> weightedAverageIdInList;
		List<Integer> idAndBatchInList;
//        List<Integer> lotIdInList;
//        List<Map<String, Object>> stockInLot;
//        List<Map<String, Object>> stockInNonLot;
		List<Map<String, Object>> stockInBatchEmpty = Lists.newArrayList();
		List<Map<String, Object>> stockInBatchNonEmpty = Lists.newArrayList();
		if (materielInIdList.size() > 0) {
			params.put("materielIdList", materielInIdList);
			materielDOInList = materielService.list(params);

			// 批次管理的入库物料
//            lotIdInList = materielDOInList.stream()
//                    .filter(materielDO -> materielDO.getIsLot()==1)
//                    .map(MaterielDO::getId)
//                    .collect(Collectors.toList());

			// 加权平均的入库物料
			weightedAverageIdInList = materielDOInList.stream()
					.filter(materielDO -> Objects.equals(ConstantForGYL.WEIGHTED_AVERAGE, materielDO.getValuationMethod()))
					.map(MaterielDO::getId)
					.collect(Collectors.toList());

			// 分批认定的入库物料
			idAndBatchInList = materielDOInList.stream()
					.filter(materielDO -> Objects.equals(ConstantForGYL.BATCH_FINDS, materielDO.getValuationMethod()))
					.map(MaterielDO::getId)
					.collect(Collectors.toList());

			// 获取本期要计算入库的加权平均物料
			stockInBatchEmpty = stockInList.stream()
					.filter(stringObjectMap -> weightedAverageIdInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
					.collect(Collectors.toList());

			// 获取本期要计算入库的分批认定物料 id+&+batch
			stockInBatchNonEmpty = stockInList.stream()
					.filter(stringObjectMap -> idAndBatchInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
					.collect(Collectors.toList());

			// 获取本期要计算入库的批次管理物料
//            stockInLot = stockInList.stream()
//                    .filter(stringObjectMap -> lotIdInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
//                    .collect(Collectors.toList());

			// 获取本期要计算入库的非批次管理物料 id+&+batch
//            stockInNonLot = stockInList.stream()
//                    .filter(stringObjectMap -> !lotIdInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
//                    .collect(Collectors.toList());
			if (weightedAverageIdInList.size() > 0) {
				weightedAverageIdList.addAll(weightedAverageIdInList);
			}
		}


		// 出库的物料
		List<Integer> materielOutIdList = stockOutList.stream()
				.map(StockOutItemDO::getMaterielId)
				.collect(Collectors.toList());
		List<MaterielDO> materielDOOutList;
		List<Integer> weightedAverageIdOutList;
		List<Integer> idAndBatchOutList;
		List<StockOutItemDO> stockOutBatchEmpty = Lists.newArrayList();
		List<StockOutItemDO> stockOutBatchNonEmpty = Lists.newArrayList();
		if (materielOutIdList.size() > 0) {
			params.put("materielIdList",materielOutIdList);
			materielDOOutList = materielService.list(params);

			// 加权平均的出库物料
			weightedAverageIdOutList = materielDOOutList.stream()
					.filter(materielDO -> Objects.equals(ConstantForGYL.WEIGHTED_AVERAGE, materielDO.getValuationMethod()))
					.map(MaterielDO::getId)
					.collect(Collectors.toList());

			// 分批认定的出库物料
			idAndBatchOutList = materielDOOutList.stream()
					.filter(materielDO -> Objects.equals(ConstantForGYL.BATCH_FINDS, materielDO.getValuationMethod()))
					.map(MaterielDO::getId)
					.collect(Collectors.toList());

			// 获取本期要计算出库的加权平均物料
			stockOutBatchEmpty = stockOutList.stream()
					.filter(stockOutItemDO -> weightedAverageIdOutList.contains(stockOutItemDO.getMaterielId()))
					.collect(Collectors.toList());

			// 获取本期要计算出库的分批认定物料 id+&+batch
			stockOutBatchNonEmpty = stockOutList.stream()
					.filter(stockOutItemDO -> idAndBatchOutList.contains(stockOutItemDO.getMaterielId()))
					.collect(Collectors.toList());

			if (weightedAverageIdOutList.size() > 0) {
				weightedAverageIdList.addAll(weightedAverageIdOutList);
			}

		}

		// 分析表中现有的物料
		List<Integer> materielIdList = stockAnalysisList.stream()
				.map(StockAnalysisDO::getMaterielId)
				.collect(Collectors.toList());
		params.put("materielIdList", materielIdList);
		List<MaterielDO> materielDOList = materielService.list(params);
		// 加权平均的分析表中现有物料
		List<Integer> weightedAverageExistingIdList = materielDOList.stream()
				.filter(materielDO -> Objects.equals(ConstantForGYL.WEIGHTED_AVERAGE, materielDO.getValuationMethod()))
				.map(MaterielDO::getId)
				.collect(Collectors.toList());

		// 分批认定的分析表中现有物料
//        List<Integer> idAndBatchExistingList = materielDOList.stream()
//                .filter(materielDO -> Objects.equals(ConstantForGYL.BATCH_FINDS, materielDO.getValuationMethod()))
//                .map(MaterielDO::getId)
//                .collect(Collectors.toList());

		if (weightedAverageExistingIdList.size() > 0) {
			weightedAverageIdList.addAll(weightedAverageExistingIdList);
		}

		HashMap<String, BigDecimal> materielOutCountMap = Maps.newHashMap();
		HashMap<String, BigDecimal> materielOutAmountMap = Maps.newHashMap();
		HashMap<String, BigDecimal> materielInCountMap = Maps.newHashMap();
		HashMap<String, BigDecimal> materielInAmountMap = Maps.newHashMap();
		HashMap<String, BigDecimal> materielInUnitPriceMap = Maps.newHashMap();

		// 加权平均入库
		if (stockInBatchEmpty.size() > 0) {
			// 获取入库列表的总数量
			for (Map<String, Object> stockInBatch : stockInBatchEmpty) {
				String materielId = stockInBatch.get("materielId").toString();
				if (materielInCountMap.containsKey(materielId)) {
					materielInCountMap.put(materielId, materielInCountMap.get(materielId).add(MathUtils.getBigDecimal(stockInBatch.get("count"))));
					materielInAmountMap.put(materielId, materielInAmountMap.get(materielId).add(MathUtils.getBigDecimal(stockInBatch.get("amount"))));
					continue;
				}
				materielInCountMap.put(materielId, MathUtils.getBigDecimal(stockInBatch.get("count")));
				materielInAmountMap.put(materielId, MathUtils.getBigDecimal(stockInBatch.get("amount")));
			}
		}

		// 分批认定入库
		if (stockInBatchNonEmpty.size() > 0) {
			// 获取入库列表的总数量
			for (Map<String, Object> stockInBatch : stockInBatchNonEmpty) {
				String materielIdAndBatch = stockInBatch.get("materielId").toString() + "&" + stockInBatch.get("batch").toString();
				if (materielInCountMap.containsKey(materielIdAndBatch)) {
					materielInCountMap.put(materielIdAndBatch, materielInCountMap.get(materielIdAndBatch).add(MathUtils.getBigDecimal(stockInBatch.get("count"))));
					materielInAmountMap.put(materielIdAndBatch, materielInAmountMap.get(materielIdAndBatch).add(MathUtils.getBigDecimal(stockInBatch.get("amount"))));
					continue;
				}
				materielInCountMap.put(materielIdAndBatch, MathUtils.getBigDecimal(stockInBatch.get("count")));
				materielInAmountMap.put(materielIdAndBatch, MathUtils.getBigDecimal(stockInBatch.get("amount")));
			}
		}

		// 保存分析表内没有的物料
		if (materielInCountMap.size() > 0) {
			HashMap<String,BigDecimal> materielInCountMapCopy = Maps.newHashMap();
			materielInCountMapCopy.putAll(materielInCountMap);
			for (StockAnalysisDO analysisDO : stockAnalysisList) {
				String materielId = analysisDO.getMaterielId().toString();
				String materielIdAndBatch = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();
				materielInCountMapCopy.remove(materielId);
				materielInCountMapCopy.remove(materielIdAndBatch);
			}
			if (materielInCountMapCopy.size() > 0) {
				StockAnalysisDO stockAnalysisDO;
				String[] split;
				int materielId;
				String batch;
				for (String s : materielInCountMapCopy.keySet()) {
					stockAnalysisDO = new StockAnalysisDO();
					split = s.split("&");
					materielId = Integer.parseInt(split[0]);
					if (split.length == 2) {
						batch = split[1];
					}else {
						batch = null;
					}
					stockAnalysisDO.setMaterielId(materielId);
					stockAnalysisDO.setBatch(batch);
					stockAnalysisDO.setInitialCount(BigDecimal.ZERO);
					stockAnalysisDO.setInitialAmount(BigDecimal.ZERO);
					stockAnalysisDO.setInCount(materielInCountMapCopy.get(s));
					stockAnalysisDO.setInAmount(materielInCountMapCopy.get(s));
					stockAnalysisDO.setPeriod(DateFormatUtil.getDateByParttern(period));
					stockAnalysisDO.setIsClose(0);
					stockAnalysisDO.setDelFlag(0);
					insertStockAnalysisList.add(stockAnalysisDO);
				}
			}
			if (insertStockAnalysisList.size() > 0) {
				stockAnalysisList.addAll(insertStockAnalysisList);
			}
		}

		// 计算出库成本
		for (StockAnalysisDO analysisDO : stockAnalysisList) {
			String materielId = analysisDO.getMaterielId().toString();
			String materielIdAndBatch = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();

			// 本月已入库的物料
			// 计算加权平均出库新入物料成本单价
			if (materielInCountMap.containsKey(materielId) && weightedAverageIdList.contains(Integer.parseInt(materielId))) {
				// (月初结存金额+本月入库金额)/（月初结存数量+本月入库数量）
				BigDecimal inAmount = materielInAmountMap.get(materielId);
				BigDecimal inCount = materielInCountMap.get(materielId);
				analysisDO.setInCount(inCount);
				analysisDO.setInAmount(inAmount);
				materielInUnitPriceMap.put(materielId
						, (analysisDO.getInitialAmount().add(inAmount)).divide(analysisDO.getInitialCount().add(inCount), Constant.BIGDECIMAL_ZERO));
				continue;
			}
			// 计算批次管理出库新入物料成本单价
			if (materielInCountMap.containsKey(materielIdAndBatch) && !weightedAverageIdList.contains(Integer.parseInt(materielId))) {
				// (月初结存金额+本月入库金额)/（月初结存数量+本月入库数量）
				BigDecimal inAmount = materielInAmountMap.get(materielIdAndBatch);
				BigDecimal inCount = materielInCountMap.get(materielIdAndBatch);
				analysisDO.setInCount(inCount);
				analysisDO.setInAmount(inAmount);
				materielInUnitPriceMap.put(materielIdAndBatch
						, (analysisDO.getInitialAmount().add(inAmount)).divide(analysisDO.getInitialCount().add(inCount), Constant.BIGDECIMAL_ZERO));
				continue;
			}

			// 本月未入库，存在上个月的库存
			if (weightedAverageIdList.contains(Integer.parseInt(materielId))) {
				materielInUnitPriceMap.put(materielId
						, analysisDO.getInitialAmount().divide(analysisDO.getInitialCount(), Constant.BIGDECIMAL_ZERO));
			}else {
				materielInUnitPriceMap.put(materielIdAndBatch
						, analysisDO.getInitialAmount().divide(analysisDO.getInitialCount(), Constant.BIGDECIMAL_ZERO));
			}
			analysisDO.setInAmount(BigDecimal.ZERO);
			analysisDO.setInCount(BigDecimal.ZERO);
		}


		// 加权平均出库
		if (stockOutBatchEmpty.size() > 0) {
			for (StockOutItemDO itemDO : stockOutBatchEmpty) {
				String materielId = itemDO.getMaterielId().toString();
				if (materielInUnitPriceMap.containsKey(materielId)) {
					// 写入出库成本单价成本金额
					BigDecimal unitPrice = materielInUnitPriceMap.get(materielId);
					itemDO.setUnitPrice(unitPrice);
					BigDecimal count = itemDO.getCount();
					BigDecimal amount = count.multiply(unitPrice);
					itemDO.setAmount(amount);

					// 保存出库的总数量
					if (materielOutCountMap.containsKey(materielId)) {
						materielOutCountMap.put(materielId, materielOutCountMap.get(materielId).add(count));
						materielOutAmountMap.put(materielId, materielOutAmountMap.get(materielId).add(amount));
						continue;
					}
					materielOutCountMap.put(materielId, count);
					materielOutAmountMap.put(materielId, amount);
				}
			}
			stockOutItemService.batchUpdate(stockOutBatchEmpty);
		}

		// 分批认定出库
		if (stockOutBatchNonEmpty.size() > 0) {
			for (StockOutItemDO itemDO : stockOutBatchNonEmpty) {
				String materielIdAndBatch = itemDO.getMaterielId().toString() + "&" + itemDO.getBatch();
				if (materielInUnitPriceMap.containsKey(materielIdAndBatch)) {
					// 写入出库成本单价成本金额
					BigDecimal unitPrice = materielInUnitPriceMap.get(materielIdAndBatch);
					itemDO.setUnitPrice(unitPrice);
					BigDecimal count = itemDO.getCount();
					BigDecimal amount = count.multiply(unitPrice);
					itemDO.setAmount(amount);

					// 保存出库的总数量
					if (materielOutCountMap.containsKey(materielIdAndBatch)) {
						materielOutCountMap.put(materielIdAndBatch, materielOutCountMap.get(materielIdAndBatch).add(count));
						materielOutAmountMap.put(materielIdAndBatch, materielOutAmountMap.get(materielIdAndBatch).add(amount));
						continue;
					}
					materielOutCountMap.put(materielIdAndBatch, count);
					materielOutAmountMap.put(materielIdAndBatch, amount);
				}
			}
			stockOutItemService.batchUpdate(stockOutBatchNonEmpty);
		}

		// 分析所有物料
//        for (StockAnalysisDO analysisDO : stockAnalysisList) {
//            String materielId = analysisDO.getMaterielId().toString();
//            String materielIdAndBatch = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();
//
//            BigDecimal inCount = analysisDO.getInCount() == null ? BigDecimal.ZERO : analysisDO.getInCount();
//            BigDecimal inAmount = analysisDO.getInAmount() == null ? BigDecimal.ZERO : analysisDO.getInAmount();
		// 加权平均
//            if (materielOutCountMap.containsKey(materielId) && weightedAverageIdList.contains(Integer.parseInt(materielId))) {
//                BigDecimal outCount = materielOutCountMap.get(materielId);
//                BigDecimal outAmount = materielOutAmountMap.get(materielId);
//                analysisDO.setOutCount(outCount);
//                analysisDO.setOutAmount(outAmount);
//                // 期末结存数量=期初数量+本月入库数量-本月发出数量
//                BigDecimal finalCount = analysisDO.getInitialCount()
//                        .add(inCount)
//                        .subtract(outCount);
//                analysisDO.setFinalCount(finalCount);
//                // 期末结存金额=期初金额+本月入库金额-本月发出金额
//                BigDecimal finalAmount = analysisDO.getInitialAmount()
//                        .add(inAmount)
//                        .subtract(outAmount);
//                analysisDO.setFinalAmount(finalAmount);
//                continue;
//            }

		// 分批认定
//            if (materielOutCountMap.containsKey(materielIdAndBatch)&& !weightedAverageIdList.contains(Integer.parseInt(materielId))) {
//                BigDecimal outCount = materielOutCountMap.get(materielIdAndBatch);
//                BigDecimal outAmount = materielOutAmountMap.get(materielIdAndBatch);
//                analysisDO.setOutCount(outCount);
//                analysisDO.setOutAmount(outAmount);
//                // 期末结存数量=期初数量+本月入库数量-本月发出数量
//                BigDecimal finalCount = analysisDO.getInitialCount()
//                        .add(inCount)
//                        .subtract(outCount);
//                analysisDO.setFinalCount(finalCount);
//                // 期末结存金额=期初金额+本月入库金额-本月发出金额
//                BigDecimal finalAmount = analysisDO.getInitialAmount()
//                        .add(inAmount)
//                        .subtract(outAmount);
//                analysisDO.setFinalAmount(finalAmount);
//                continue;
//            }
//            analysisDO.setOutCount(BigDecimal.ZERO);
//            analysisDO.setOutAmount(BigDecimal.ZERO);
//
//            analysisDO.setFinalCount(
//                    analysisDO.getInitialCount()
//                            .add(inCount));
//            analysisDO.setFinalAmount(
//                    analysisDO.getInitialAmount()
//                            .add(inAmount));
//        }
//        List<StockAnalysisDO> updateList = stockAnalysisList
//                .stream()
//                .filter(stockAnalysisDO -> stockAnalysisDO.getId() != null)
//                .collect(Collectors.toList());
//
//        List<StockAnalysisDO> insertList = stockAnalysisList
//                .stream()
//                .filter(stockAnalysisDO -> stockAnalysisDO.getId() == null)
//                .collect(Collectors.toList());

//        if (updateList.size() > 0) {
//            stockAnalysisService.batchUpdate(updateList);
//        }
//        if (insertList.size() > 0) {
//            stockAnalysisService.batchInsert(insertList);
//        }
		return R.ok();

	}

	@Override
	public R stockOutAccountingCheck(String period) {
		R r = this.checkTime(period);
		if (r != null) {
			return r;
		}

		Map<String, Object> params = Maps.newHashMap();
		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		// 入库单
		params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
		params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
		params.put("auditSign", ConstantForGYL.OK_AUDITED);
		List<Map<String, Object>> stockInList = stockInService.listForMap(params);
		List<String> unitPriceEmpty = stockInList.stream()
				.filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("unitPrice")).compareTo(BigDecimal.ZERO) == 0)
				.map(stringObjectMap -> stringObjectMap.get("inheadCode").toString())
				.distinct()
				.collect(Collectors.toList());

		if (unitPriceEmpty.size() > 0) {
			String[] args = {unitPriceEmpty.toString()};
			// -1码为查询出有空的单价的错误码
			return R.error(-1, messageSourceHandler.getMessage("scm.stockIn.unitPriceEmpty", args));
		}
		return R.ok();

	}

	@Override
	public R checkEndingCarryOver(String period) {
		// 期末结账
		R r = this.checkTime(period);
		if (r != null) {
			return r;
		}

		Map<String, Object> params = Maps.newHashMap();
		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		// 入库单
		params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
		params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
		params.put("auditSign", ConstantForGYL.OK_AUDITED);
		List<Map<String, Object>> stockInList = stockInService.listForMap(params);
		List<String> stockInUnitPriceEmpty = stockInList.stream()
				.filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("unitPrice")).compareTo(BigDecimal.ZERO) == 0)
				.map(stringObjectMap -> stringObjectMap.get("inheadCode").toString())
				.distinct()
				.collect(Collectors.toList());
		String [] args = new String[2];
		if (stockInUnitPriceEmpty.size() > 0) {
			// -1码为查询出有空的单价的错误码
			args[0] = stockInUnitPriceEmpty.toString();
		}
		List<String> stockOutUnitPriceEmpty = stockOutItemService.list(params).stream()
				.filter(stockOutItemDO -> stockOutItemDO.getUnitPrice().compareTo(BigDecimal.ZERO) == 0)
				.map(StockOutItemDO::getOutCode)
				.distinct()
				.collect(Collectors.toList());
		if (stockOutUnitPriceEmpty.size() > 0) {
			// -1码为查询出有空的单价的错误码
			args[1] = stockOutUnitPriceEmpty.toString();
		}
		if (args[0] != null || args[1] != null) {
			if(args[1]==null){
				args[1]="无";
			}
			if(args[0]==null){
				args[0]="无";
			}
			return R.error(-1, messageSourceHandler.getMessage("scm.stockOut.unitPriceEmpty", args));
		}
		return R.ok();
	}

	@Override
	public R endingCarryOver(String period) {
		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		if (periodTime == null) {
			return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("offset", 0);
		params.put("limit", 1);
		List<StockAnalysisDO> list = stockAnalysisService.list(params);
		if (list.size() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
		}
		StockAnalysisDO stockAnalysisDO = list.get(0);
		Date oldPeriod = stockAnalysisDO.getPeriod();
		Calendar instance = Calendar.getInstance();
		instance.setTime(oldPeriod);
		instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
		Date instanceTime = instance.getTime();
		params.clear();
		// 上一期的列表
		params.put("period", DateFormatUtil.getFormateDate(instanceTime));
		List<StockAnalysisDO> lastTerm = stockAnalysisService.list(params);
		if (lastTerm.size() > 0 && lastTerm.get(0).getIsClose() == 0) {
			oldPeriod = instanceTime;
		}
		instance.setTime(periodTime);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		long newMillis = instance.getTimeInMillis();
		instance.setTime(oldPeriod);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		long oldMillis = instance.getTimeInMillis();
		if (oldMillis != newMillis) {
			return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
		}

		// 期末结转
		params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
		params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
		params.put("auditSign", ConstantForGYL.OK_AUDITED);
		// 入库单
		List<Map<String, Object>> stockInList = stockInService.listForMap(params);
		// 出库单
		List<StockOutItemDO> stockOutList = stockOutItemService.list(params);
		// 获取上期结存数据
		params.put("period", period);
		List<StockAnalysisDO> stockAnalysisList = stockAnalysisService.list(params);


		// 若已进行过一次结转，则初始化源数据
		if (stockAnalysisList.size() > 0 && stockAnalysisList.get(0).getFinalCount()!=null) {
			// 结转前先删除上一次结转的数据
			stockAnalysisService.batchRemoveById(stockAnalysisList.get(0).getId());

			for (StockAnalysisDO analysisDO : stockAnalysisList) {
				analysisDO.setOutCount(BigDecimal.ZERO);
				analysisDO.setOutAmount(BigDecimal.ZERO);
				analysisDO.setInCount(BigDecimal.ZERO);
				analysisDO.setInAmount(BigDecimal.ZERO);
				analysisDO.setFinalAmount(null);
				analysisDO.setFinalCount(null);
			}
		}


		List<Integer> materielIdList = Lists.newArrayList();
		// 入库的物料
		List<Integer> materielInIdList = stockInList.stream()
				.map(stringObjectMap -> Integer.parseInt(stringObjectMap.get("materielId").toString()))
				.collect(Collectors.toList());
		if (materielInIdList.size() > 0) {
			materielIdList.addAll(materielInIdList);
		}
		// 出库的物料
		List<Integer> materielOutIdList = stockOutList.stream()
				.map(StockOutItemDO::getMaterielId)
				.collect(Collectors.toList());
		if (materielOutIdList.size() > 0) {
			materielIdList.addAll(materielOutIdList);
		}
		// 分析表中现有的物料
		List<Integer> materielNowIdList = stockAnalysisList.stream()
				.map(StockAnalysisDO::getMaterielId)
				.collect(Collectors.toList());
		if (materielNowIdList.size() > 0) {
			materielIdList.addAll(materielNowIdList);
		}
		// 获取要统计的所有物料
		List<MaterielDO> materielDOList = Lists.newArrayList();
		if (materielIdList.size() > 0) {
			params.clear();
			params.put("materielIdList", materielIdList);
			materielDOList = materielService.list(params);
		}
		List<Integer> idList = Lists.newArrayList();
		if (materielDOList.size() > 0) {
			// 非批次管理的入库物料
			idList = materielDOList.stream()
					.filter(materielDO -> materielDO.getIsLot()!=1)
					.map(MaterielDO::getId)
					.collect(Collectors.toList());
		}

		// 将没有批次管理的物料加入统计表中
		StockAnalysisDO newAnalysisDO;
		for (Map<String, Object> map : stockInList) {
			boolean isFlag =true;
			String materielIdToString = map.get("materielId").toString();
			Integer materielId = Integer.parseInt(materielIdToString);
			// 非批次管理
			if (idList.contains(materielId)) {
				// 若已存在这个物料及累加
				if (materielNowIdList.contains(materielId)) {
					for (StockAnalysisDO analysisDO : stockAnalysisList) {
						if (Objects.equals(analysisDO.getMaterielId(),materielId)){
							analysisDO.setInCount(analysisDO.getInCount().add(MathUtils.getBigDecimal(map.get("count"))));
							analysisDO.setInAmount(analysisDO.getInAmount().add(MathUtils.getBigDecimal(map.get("amount"))));
							break;
						}
					}
					continue;
				}
				// 若不存则添加
				newAnalysisDO = new StockAnalysisDO();
				newAnalysisDO.setMaterielId(materielId);
				newAnalysisDO.setInitialCount(BigDecimal.ZERO);
				newAnalysisDO.setInitialAmount(BigDecimal.ZERO);
				newAnalysisDO.setInCount(MathUtils.getBigDecimal(map.get("count")));
				newAnalysisDO.setInAmount(MathUtils.getBigDecimal(map.get("amount")));
				newAnalysisDO.setOutCount(BigDecimal.ZERO);
				newAnalysisDO.setOutAmount(BigDecimal.ZERO);
				newAnalysisDO.setIsClose(0);
				newAnalysisDO.setDelFlag(0);
				newAnalysisDO.setPeriod(periodTime);
				stockAnalysisList.add(newAnalysisDO);
				continue;
			}
			// 批次管理
			String materielIdAndBatch = materielIdToString + "&" + map.get("batch").toString();
			// 若已存在这个物料及累加
			if (materielNowIdList.contains(materielId)) {
				for (StockAnalysisDO analysisDO : stockAnalysisList) {
					String materielIdAndBatchNow = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();
					if (Objects.equals(materielIdAndBatchNow,materielIdAndBatch)){
						analysisDO.setInCount(analysisDO.getInCount().add(MathUtils.getBigDecimal(map.get("count"))));
						analysisDO.setInAmount(analysisDO.getInAmount().add(MathUtils.getBigDecimal(map.get("amount"))));
						isFlag = false;
						break;
					}
				}
			}
			// 若不存则添加
			if (isFlag) {
				newAnalysisDO = new StockAnalysisDO();
				newAnalysisDO.setMaterielId(materielId);
				newAnalysisDO.setBatch(map.get("batch").toString());
				newAnalysisDO.setInitialCount(BigDecimal.ZERO);
				newAnalysisDO.setInitialAmount(BigDecimal.ZERO);
				newAnalysisDO.setInAmount(MathUtils.getBigDecimal(map.get("amount")));
				newAnalysisDO.setInCount(MathUtils.getBigDecimal(map.get("count")));
				newAnalysisDO.setInAmount(MathUtils.getBigDecimal(map.get("amount")));
				newAnalysisDO.setOutCount(BigDecimal.ZERO);
				newAnalysisDO.setOutAmount(BigDecimal.ZERO);
				newAnalysisDO.setIsClose(0);
				newAnalysisDO.setDelFlag(0);
				newAnalysisDO.setPeriod(periodTime);
				stockAnalysisList.add(newAnalysisDO);
				materielNowIdList.add(materielId);
			}

		}

		// 统计出库物料
		for (StockOutItemDO itemDO : stockOutList) {
			Integer materielId = itemDO.getMaterielId();
			String materielIdToString = materielId.toString();
			// 非批次管理
			if (idList.contains(materielId)) {
				// 若已存在这个物料及累加
				if (materielNowIdList.contains(materielId)) {
					for (StockAnalysisDO analysisDO : stockAnalysisList) {
						if (Objects.equals(analysisDO.getMaterielId(),materielId)){
							BigDecimal outCount = itemDO.getCount().add(analysisDO.getOutCount());
							BigDecimal outAmount = itemDO.getAmount().add(analysisDO.getOutAmount());
							analysisDO.setOutCount(outCount);
							analysisDO.setOutAmount(outAmount);
							// 期末结存数量=期初数量+本月入库数量-本月发出数量
							BigDecimal finalCount = analysisDO.getInitialCount()
									.add(analysisDO.getInCount())
									.subtract(outCount);
							analysisDO.setFinalCount(finalCount);
							// 期末结存金额=期初金额+本月入库金额-本月发出金额
							BigDecimal finalAmount = analysisDO.getInitialAmount()
									.add(analysisDO.getInAmount())
									.subtract(outAmount);
							analysisDO.setFinalAmount(finalAmount);
							break;
						}
					}
				}
				continue;
			}
			// 批次管理
			String materielIdAndBatch = materielIdToString + "&" + itemDO.getBatch();
			// 若已存在这个物料及累加
			if (materielNowIdList.contains(materielId)) {
				for (StockAnalysisDO analysisDO : stockAnalysisList) {
					String materielIdAndBatchNow = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();
					if (Objects.equals(materielIdAndBatchNow,materielIdAndBatch)){
						BigDecimal outCount = itemDO.getCount().add(analysisDO.getOutCount());
						BigDecimal outAmount = itemDO.getAmount().add(analysisDO.getOutAmount());
						analysisDO.setOutCount(outCount);
						analysisDO.setOutAmount(outAmount);
						// 期末结存数量=期初数量+本月入库数量-本月发出数量
						BigDecimal finalCount = analysisDO.getInitialCount()
								.add(analysisDO.getInCount())
								.subtract(outCount);
						analysisDO.setFinalCount(finalCount);
						// 期末结存金额=期初金额+本月入库金额-本月发出金额
						BigDecimal finalAmount = analysisDO.getInitialAmount()
								.add(analysisDO.getInAmount())
								.subtract(outAmount);
						analysisDO.setFinalAmount(finalAmount);
						break;
					}
				}
			}
		}
		if (stockAnalysisList.size() > 0) {
			for (StockAnalysisDO analysisDO : stockAnalysisList) {
				if (analysisDO.getFinalCount() == null) {
					// 期末结存数量=期初数量+本月入库数量-本月发出数量
					BigDecimal finalCount = analysisDO.getInitialCount()
							.add(analysisDO.getInCount())
							.subtract(analysisDO.getOutCount());
					analysisDO.setFinalCount(finalCount);
					// 期末结存金额=期初金额+本月入库金额-本月发出金额
					BigDecimal finalAmount = analysisDO.getInitialAmount()
							.add(analysisDO.getInAmount())
							.subtract(analysisDO.getOutAmount());
					analysisDO.setFinalAmount(finalAmount);
				}
			}
			List<StockAnalysisDO> updateList = stockAnalysisList
					.stream()
					.filter(analysisDO -> analysisDO.getId() != null)
					.collect(Collectors.toList());

			List<StockAnalysisDO> insertList = stockAnalysisList
					.stream()
					.filter(analysisDO -> analysisDO.getId() == null)
					.collect(Collectors.toList());

			if (updateList.size() > 0) {
				stockAnalysisService.batchUpdate(updateList);
			}
			if (insertList.size() > 0) {
				stockAnalysisService.batchInsert(insertList);
			}
		}

		params.put("period", DateFormatUtil.getFormateDate(periodTime));
		List<StockAnalysisDO> thisTermOld = stockAnalysisService.list(params);

		if (thisTermOld.size() > 0) {
			List<StockAnalysisDO> thisTerm = thisTermOld.stream()
					.filter(stockAnalysis -> stockAnalysis.getFinalCount()
							.compareTo(BigDecimal.ZERO) > 0)
					.collect(Collectors.toList());
			List<StockAnalysisDO> stockAnalysisInsertDOS = Lists.newArrayList();
			if (thisTerm.size() > 0) {
				if (thisTerm.get(0).getFinalCount()==null){
					return R.error(messageSourceHandler.getMessage("scm.stock.outError", null));
				}
				StockAnalysisDO analysisDO;
				instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) + 1);
				Date lastTermDate = instance.getTime();
				for (StockAnalysisDO stockAnalysis : thisTerm) {
					analysisDO = new StockAnalysisDO();
					analysisDO.setMaterielId(stockAnalysis.getMaterielId());
					analysisDO.setBatch(stockAnalysis.getBatch());
					analysisDO.setInitialCount(stockAnalysis.getFinalCount());
					analysisDO.setInitialAmount(stockAnalysis.getFinalAmount());
					analysisDO.setInAmount(BigDecimal.ZERO);
					analysisDO.setInCount(BigDecimal.ZERO);
					analysisDO.setOutAmount(BigDecimal.ZERO);
					analysisDO.setOutCount(BigDecimal.ZERO);
					analysisDO.setIsClose(0);
					analysisDO.setDelFlag(0);
					analysisDO.setPeriod(lastTermDate);
					stockAnalysisInsertDOS.add(analysisDO);
				}
				if (stockAnalysisInsertDOS.size() > 0) {
					stockAnalysisService.batchInsert(stockAnalysisInsertDOS);
				}
			}
		}
		return R.ok();
	}

	@Override
	public R endingClose(String period) {
		R r = this.checkTime(period);
		if (r != null) {
			return r;
		}
		// 获取可以操作的时间(数据库内本期时间的下个月第一个天)
		Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
		if (periodTime != null) {
			Calendar instance = Calendar.getInstance();
			instance.setTime(periodTime);
			instance.set(Calendar.DAY_OF_MONTH,1);
			instance.set(Calendar.MONTH, instance.get(Calendar.MONTH)+1);
			Date now = new Date();
			Date lastTerm = instance.getTime();
			if(now.before(lastTerm)){
				return R.error(messageSourceHandler.getMessage("scm.stock.time", null));
			}
			// 将本期时间写入redis中
			stringRedisTemplate.opsForValue().set("period", DateFormatUtil.getFormateDate(lastTerm));
		}

		// 期末关账
		Map<String, Object> params = Maps.newHashMap();
		params.put("period", period);
		// 上一期的列表
		List<StockAnalysisDO> thisTerm = stockAnalysisService.list(params);

		for (StockAnalysisDO stockAnalysis : thisTerm) {
			stockAnalysis.setIsClose(1);
		}
		stockAnalysisService.batchUpdate(thisTerm);
		return R.ok();
	}
}
