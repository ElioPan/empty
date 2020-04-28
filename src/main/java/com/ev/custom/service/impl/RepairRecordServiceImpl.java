package com.ev.custom.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev.framework.config.Constant;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.dao.RepairRecordDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairEventPartDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.RepairCheckService;
import com.ev.custom.service.RepairEventPartService;
import com.ev.custom.service.RepairEventService;
import com.ev.custom.service.RepairRecordService;
import com.google.common.collect.Maps;



@Service
public class RepairRecordServiceImpl implements RepairRecordService {
	@Autowired
	private RepairRecordDao repairRecordDao;
	@Autowired
	private RepairEventPartService repairEventPartService;
	@Autowired
	private RepairCheckService repairCheckService;
//	@Autowired
//	private MaterielService materielService;
	@Autowired
	private RepairEventService repairEventService;
	@Autowired
	private ContentAssocService contentAssocService;
	@Override
	public RepairRecordDO get(Long id){
		return repairRecordDao.get(id);
	}
	
	@Override
	public List<RepairRecordDO> list(Map<String, Object> map){
		return repairRecordDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return repairRecordDao.count(map);
	}
	
	@Override
	public int save(RepairRecordDO repairRecord){
		return repairRecordDao.save(repairRecord);
	}
	
	@Override
	public int update(RepairRecordDO repairRecord){
		return repairRecordDao.update(repairRecord);
	}
	
	@Override
	public int remove(Long id){
		return repairRecordDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return repairRecordDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> addRepairRecord(Long eventId, RepairRecordDO record,String partIds,String[] taglocationappearanceImage) {
		RepairEventDO event = this.repairEventService.get(eventId);
		record.setLevel(event.getLevel());
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(1);
		record.setEventId(eventId);
		this.repairRecordDao.save(record);
		saveRecordDetail(record, partIds, taglocationappearanceImage, event);
		results.put("id", record.getId());
		return results;
	}
	
	@Override
	public void saveRecordDetail(RepairRecordDO record, String partIds,
			String[] taglocationappearanceImage, RepairEventDO event) {
		Long recordId = record.getId();
		//关联备品备件
		if (StringUtils.isNotBlank(partIds)) {
			JSONArray jsonArray = JSON.parseArray(partIds);
			for(int j = 0; j < jsonArray.size(); j ++) {
				JSONObject jsonObject = jsonArray.getJSONObject(j);
				int partId = Integer.parseInt(jsonObject.getOrDefault("partId",0).toString());
				int amount = Integer.parseInt(jsonObject.getOrDefault("amount",0).toString());
				double price = Double.parseDouble(jsonObject.getOrDefault("price",0.0).toString());
				String content = jsonObject.getOrDefault("content","").toString();
//				MaterielDO sparePart =  this.materielService.get(partId);
				RepairEventPartDO part = new RepairEventPartDO();
				part.setAmount(amount);
				part.setPartId(Long.parseLong(Integer.toString(partId)));
				part.setRemark(content);
				part.setPrice(price);
				part.setTotal(amount*price);
				// 将原先保存的事件ID更新改为保存记录Id
				part.setEventId(recordId);
				this.repairEventPartService.save(part);
			}
		}
		// 完成后的照片上传
		contentAssocService.saveList(recordId, taglocationappearanceImage, Constant.REPAIR_EVENT_RECORD_IMAGE);
		// 若为暂存操作则不更新事件进度状态
		if (record.getStatus().equals(Constant.TS)) {
			return;
		}
		Long usage = record.getUsage();
		/*
		//将维修后的设备状态更改
		DeviceDO deviceDO=new DeviceDO();
		deviceDO.setId(event.getDeviceId());
		deviceDO.setUsingStatus(usage);
		this.deviceService.update(deviceDO);
		*/
		//更新事件进度状态
		event.setType(record.getType());
		event.setManHour(record.getManHour());
		event.setCost(record.getCost());
		event.setUsage(usage);
		// 设置为待验收Constant.WAITING_CHECK
		event.setStatus(Constant.WAITING_CHECK);
		this.repairEventService.update(event);
	}

	@Override
	public List<RepairRecordDO> getByEventId(Long id) {
		return this.repairRecordDao.getByEventId(id);
	}
	/*
	@Override
	public Map<String, Object> saveRepairRecord(RepairEventDO eventDO,String repairRecords) {
			Map<String, Object>result=Maps.newHashMapWithExpectedSize(1);
			JSONArray jsonArray = new JSONArray(repairRecords);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			RepairRecordDO recordDO = new RepairRecordDO();
			Integer usage = jsonObject.getInt("usage");
			recordDO.setType(jsonObject.getInt("type"));
			recordDO.setStartTime(DateFormatUtil.getDateByParttern(jsonObject.getString("startTime")));
			recordDO.setEndTime(DateFormatUtil.getDateByParttern(jsonObject.getString("endTime")));
			recordDO.setOffTime(DateFormatUtil.getDateByParttern(jsonObject.getString("offTime")));
			recordDO.setCause(jsonObject.getString("cause"));
			recordDO.setSolution(jsonObject.getString("solution"));
			recordDO.setManHour(jsonObject.getDouble("manHour"));
			recordDO.setCost(jsonObject.getBigDecimal("cost"));
			recordDO.setUsage(usage);
			recordDO.setLevel(eventDO.getLevel());
			eventDO.setType(recordDO.getType());
			eventDO.setManHour(recordDO.getManHour());
			eventDO.setCost(recordDO.getCost());
			eventDO.setUsage(recordDO.getUsage());
			eventDO.setStatus(Constant.WAITING_CHECK);
			eventDO.setWorkOrderno("GZBX"+DateFormatUtil.getWorkOrderno());
			int event = repairEventService.save(eventDO);
			if (event>0) {
				Long eventId = eventDO.getId();
				recordDO.setEventId(eventId);
				result.put("id", eventId);
				// 保存维修记录
				int record = this.save(recordDO);
				if (record>0) {
					//将维修后的设备状态更改
					DeviceDO deviceDO=new DeviceDO();
					deviceDO.setId(eventDO.getDeviceId());
					deviceDO.setUsingStatus(usage);
					this.deviceService.update(deviceDO);
					JSONArray partIdArray = jsonObject.getJSONArray("partIdArray");
					if (partIdArray.length()>0) {
						for(int j = 0; j < partIdArray.length(); j ++) {
							JSONObject part = partIdArray.getJSONObject(j);
							Long partId = part.getLong("partId");
							int amount = part.getInt("amount");
							String content = part.getString("content");
							SparePartDO sparePart = this.sparePartService.get(partId);
							RepairEventPartDO eventPart = new RepairEventPartDO();
							eventPart.setAmount(amount);
							eventPart.setPartId(sparePart.getId());
							eventPart.setRemark(content);
							eventPart.setTotal(amount*(sparePart.getPrice().doubleValue()));
							eventPart.setEventId(eventId);
							this.repairEventPartService.save(eventPart);
						}
					}
					// 保存完工拍照图片
					List<String>images=new ArrayList<>();
					JSONArray taglocationappearanceImages = jsonObject.getJSONArray("taglocationappearanceImage");
					if (taglocationappearanceImages.length()>0) {
						for(int j = 0; j < taglocationappearanceImages.length(); j ++) {
							images.add(taglocationappearanceImages.getString(j));
						}
						String[] array = images.toArray(new String[images.size()]);
						contentAssocService.saveList(eventId, array, Constant.REPAIR_EVENT_RECORD_IMAGE);
					}
					
				}
				
			}

		return result;
	}
	*/
	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return this.repairRecordDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return this.repairRecordDao.countForMap(map);
	}

	@Override
	public Map<String, Object> recordDetail(Long recordId) {
		Map<String,Object>result = Maps.newHashMap();
		Map<String, Object>param = Maps.newHashMapWithExpectedSize(2);
		param.put("recordId", recordId);
		List<Map<String, Object>> records = repairRecordDao.listForMap(param);
		if(records.size()>0) {
			Map<String, Object>recordInfo = records.get(0);
			List<Map<String, Object>> checkInfo = repairCheckService.listForMap(param);
			result.put("checkInfo",checkInfo);
			result.put("recordInfo", recordInfo);
			//获取维修所需备件信息
			// 这里recordId替代了以前的reventId
			Map<String, Object>params = Maps.newHashMapWithExpectedSize(3);
			params.put("recordId", recordId);
			params.put("sort", "id");
			params.put("order", "ASC");
			List<Map<String,Object>> reList = this.repairEventPartService.listForMap(params);
				result.put("mapList", reList);
				Map<String, Object> mapListTotal = this.repairEventPartService.countForMap(param);
				result.put("mapListTotal", mapListTotal);
			}
			//获取完工时的照片
			param.clear();
			param.put("assocId", recordId);
			param.put("assocType", Constant.REPAIR_EVENT_RECORD_IMAGE);
			List<ContentAssocDO> contentAssocDOs = contentAssocService.list(param);
			result.put("repairRecordImage", contentAssocDOs);
		return result;
	}

	@Override
	public Map<String, Object> getFaultRank() {
		Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
		result.put("data", this.repairRecordDao.getFaultRank());
		return result;
	}

	@Override
	public Map<String, Object> getRepairPower() {
		Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
		List<Map<String,Object>> meanRepairTime = this.repairRecordDao.getRepairPowerAvgTime();
		List<Map<String,Object>> tieFaultInterval = this.repairRecordDao.getRepairPowerTaktTime();
		result.put("meanRepairTime", meanRepairTime);
		result.put("tieFaultInterval", tieFaultInterval);
		return result;
	}

}
