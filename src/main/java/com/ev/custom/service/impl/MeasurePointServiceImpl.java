package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.MeasurePointDao;
import com.ev.custom.domain.MeasurePointDO;
import com.ev.custom.domain.MeasurePointTypeDO;
import com.ev.custom.domain.UomDO;
import com.ev.custom.service.MeasurePointService;
import com.ev.custom.service.MeasurePointTypeService;
import com.ev.custom.service.UomService;
import com.google.common.collect.Maps;

@Service
public class MeasurePointServiceImpl implements MeasurePointService {
	@Autowired
	private MeasurePointDao measurePointDao;
	@Autowired
	private UomService uomService;
	@Autowired
	private MeasurePointTypeService measurePointTypeService;

	@Override
	public MeasurePointDO get(Long id) {
		return measurePointDao.get(id);
	}

	@Override
	public List<MeasurePointDO> list(Map<String, Object> map) {
		return measurePointDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return measurePointDao.count(map);
	}

	@Override
	public int save(MeasurePointDO measurePoint) {
		return measurePointDao.save(measurePoint);
	}

	@Override
	public int update(MeasurePointDO measurePoint) {
		return measurePointDao.update(measurePoint);
	}

	@Override
	public int remove(Long id) {
		return measurePointDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return measurePointDao.batchRemove(ids);
	}

	@Override
	public int add(MeasurePointDO measurePoint, String childTypeName, String uomName) {
		this.setTypeId(measurePoint, childTypeName, uomName);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("offset", 0);
		params.put("limit", 1);
		params.put("deviceId", measurePoint.getDeviceId());
		List<MeasurePointDO> measurePointDOs = this.list(params);
		if (measurePointDOs.size() > 0) {
			MeasurePointDO measurePointDO = measurePointDOs.get(0);
			// 每个新生成的排序号都会比当前最大的号大 10000000L 19次后数值开始相等
			long sortNo = measurePointDO.getSortNo() + 10000000L;
			if (sortNo<0) {
				// 超过Long型最大值
				sortNo = measurePointDO.getSortNo();
			}
			measurePoint.setSortNo(sortNo);
		}
		if (measurePointDOs.size() == 0) {
			// 初始排序值为 0
			measurePoint.setSortNo(0L);
		}

		return measurePointDao.save(measurePoint);
	}

	@Override
	public int edit(MeasurePointDO measurePoint, String childTypeName, String uomName) {
		this.setTypeId(measurePoint, childTypeName, uomName);
		return this.update(measurePoint);
	}

	@Override
	public int checkSave(MeasurePointDO measurePointDO) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(2);
		Long id = measurePointDO.getId();
		if (id == null) {
			param.put("serialNo", measurePointDO.getSerialNo());
			param.put("deviceId", measurePointDO.getDeviceId());
		}
		if (measurePointDO.getId() != null) {
			MeasurePointDO oldPointDO = this.get(id);
			if (Objects.equals(oldPointDO.getSerialNo(), measurePointDO.getSerialNo())) {
				return 1;
			}
			oldPointDO.setId(null);
			return this.checkSave(oldPointDO);
		}
		return measurePointDao.count(param);
	}

	private void setTypeId(MeasurePointDO measurePoint, String childTypeName, String uomName) {
		UomDO uom = uomService.getByName(uomName);
		MeasurePointTypeDO pointType = measurePointTypeService.getByName(childTypeName);
		if (Objects.nonNull(uom)) {
			measurePoint.setUom(uom.getId());
		}
		if (Objects.isNull(uom)) {
			UomDO newUom = new UomDO();
			newUom.setName(uomName);
			if (uomService.save(newUom) > 0) {
				measurePoint.setUom(newUom.getId());
			}
		}
		if (Objects.nonNull(pointType)) {
			measurePoint.setChildType(pointType.getId());
		}
		if (Objects.isNull(pointType)) {
			MeasurePointTypeDO newpointType = new MeasurePointTypeDO();
			newpointType.setName(childTypeName);
			if (measurePointTypeService.save(newpointType) > 0) {
				measurePoint.setChildType(newpointType.getId());
			}
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return measurePointDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return measurePointDao.countForMap(params);
	}

	@Override
	public int batchEdit(String pointArray, Long deviceId) {
		List<MeasurePointDO> parseArray = JSON.parseArray(pointArray, MeasurePointDO.class);
		Date now = new Date();
		Long userId = ShiroUtils.getUserId();
		for (MeasurePointDO measurePointDO : parseArray) {
			measurePointDO.setUpdateTime(now);
			measurePointDO.setUpdateBy(userId);
		}
		String serialNo;
		List<String> serialNos = new ArrayList<>();
		List<Long> ids = new ArrayList<>();
		for (MeasurePointDO measurePointDO : parseArray) {
			serialNo = measurePointDO.getSerialNo();
			if (serialNos.contains(serialNo)) {
				return -2;
			}
			ids.add(measurePointDO.getId());
			serialNos.add(serialNo);
		}
		Map<String,Object> map = Maps.newHashMapWithExpectedSize(1);
		map.put("id", deviceId);
		List<MeasurePointDO> list = measurePointDao.list(map);
		for (MeasurePointDO measurePointDO : list) {
			if(ids.contains(measurePointDO.getId())) {
				continue;
			}
			if (serialNos.contains(measurePointDO.getSerialNo())) {
				return -2;
			}
		}
		return  measurePointDao.batchEdit(parseArray)==parseArray.size()?parseArray.size():-1;
	}

	@Override
	public void formatSortNoJob() {
		Date startTime = DatesUtil.getBeginDayOfMonth();
		Date endTime = DatesUtil.getEndDayOfMonth();
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(2);
		param.put("startTime", DateFormatUtil.getFormateDate(startTime));
		param.put("endTime", DateFormatUtil.getFormateDate(endTime));
		List<MeasurePointDO> list = this.list(param);
		if (list.size()==0) {
			return;
		}
		List<Long> deviceIds = new ArrayList<>(); 
		for (MeasurePointDO measurePointDO : list) {
			deviceIds.add(measurePointDO.getDeviceId());
		}
		for (Long deviceId : deviceIds) {
			Map<String,Object> params = Maps.newHashMapWithExpectedSize(2);
			params.put("deviceId", deviceId);
			params.put("sort", "sort_no");
			params.put("order", "ASC");
			List<MeasurePointDO> updateList = this.list(params);
			long sortNo = 10000000L;
			for (MeasurePointDO measurePointDO : updateList) {
				measurePointDO.setSortNo(sortNo);
				sortNo += 10000000L;
			}
			 measurePointDao.batchEdit(updateList);
		}
		
	}

	@Override
	public int logicRemove(Long id) {
		MeasurePointDO pointDO = new MeasurePointDO();
		pointDO.setId(id);
		pointDO.setDelFlag(1);
		return this.update(pointDO);
	}

	@Override
	public int logicBatchRemove(Long[] ids) {
		int count = 0;
		for (Long id : ids) {
			count += this.logicRemove(id);
		}
		return count;
	}
}
