package com.ev.custom.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.ev.custom.dao.DeviceInsuranceDao;
import com.ev.custom.domain.DeviceInsuranceDO;
import com.ev.custom.service.DeviceInsuranceService;



@Service
public class DeviceInsuranceServiceImpl implements DeviceInsuranceService {
	@Autowired
	private DeviceInsuranceDao deviceInsuranceDao;

	@Override
	public DeviceInsuranceDO get(Long id){

		return  deviceInsuranceDao.get(id);
	}
	
	@Override
	public List<DeviceInsuranceDO> list(Map<String, Object> map){
		return deviceInsuranceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return deviceInsuranceDao.count(map);
	}
	
	@Override
	public int save(DeviceInsuranceDO deviceInsurance){
		return deviceInsuranceDao.save(deviceInsurance);
	}
	
	@Override
	public int update(DeviceInsuranceDO deviceInsurance){
		return deviceInsuranceDao.update(deviceInsurance);
	}
	
	@Override
	public int remove(Long id){
		return deviceInsuranceDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return deviceInsuranceDao.batchRemove(ids);
	}

	@Override
	public  Map<String,Object> getInturanDetail(Long id){
		Map<String,Object> result = Maps.newHashMap();
		DeviceInsuranceDO detailOne=deviceInsuranceDao.get(id);
		if(detailOne!=null){
			result.put("data",detailOne);
		}
		return  result;

	}

	@Override
	public List<Map<String, Object>> listPageMap(Map<String, Object> map) {
		return deviceInsuranceDao.listPageMap(map);
	}

}
