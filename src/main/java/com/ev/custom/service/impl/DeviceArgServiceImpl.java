package com.ev.custom.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.service.DeviceService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.ev.custom.dao.DeviceArgDao;
import com.ev.custom.domain.DeviceArgDO;
import com.ev.custom.service.DeviceArgService;


@Service
public class DeviceArgServiceImpl implements DeviceArgService {
	@Autowired
	private DeviceArgDao deviceArgDao;
	@Autowired
	private UserService userService;
	@Autowired
	private DeviceService deviceService;
	
	@Override
	public DeviceArgDO get(Long id){
		return deviceArgDao.get(id);
	}
	
	@Override
	public List<DeviceArgDO> list(Map<String, Object> map){
		return deviceArgDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return deviceArgDao.count(map);
	}
	
	@Override
	public int save(DeviceArgDO deviceArg){
		return deviceArgDao.save(deviceArg);
	}
	
	@Override
	public int update(DeviceArgDO deviceArg){
		return deviceArgDao.update(deviceArg);
	}
	
	@Override
	public int remove(Long id){
		return deviceArgDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return deviceArgDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> addDeviceArg(DeviceArgDO deviceArg) {
		Map<String,Object> results = Maps.newHashMap();
		this.deviceArgDao.save(deviceArg);
		return results;
	}

	@Override
	public Map<String, Object> deviceArgList(int pageno, int pagesize, Long deviceId) {
		Map<String, Object> userMap = this.userService.getUserMap();
		Map<String, Object> deviceMap = this.deviceService.getDeviceMap();
		Map<String, Object> results = Maps.newHashMap();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceId", deviceId);
		params.put("offset", pageno - 1);
		params.put("limit", pagesize);

		List<Map<String, Object>> maps = deviceArgDao.lisForMap(params);

		int total =  deviceArgDao.countForMap(params);
		DsResultResponse dsRet = new DsResultResponse();
		if (maps.size() > 0) {
			dsRet.setDatas(maps);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
		}
		results.put("data", dsRet);
		return results;
	}

	@Override
	public Map<String, Object> delDeviceArg(Long id) {
		Map<String,Object> results = Maps.newHashMap();
		int remove = this.deviceArgDao.remove(id);
			return results;
	}

	@Override
	public List<Map<String, Object>> lisForMap(Map<String, Object> map) {
		return deviceArgDao.lisForMap(map);
	}


}
