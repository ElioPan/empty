package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.ev.custom.dao.DeviceSuperviseDao;
import com.ev.custom.domain.DeviceSuperviseDO;
import com.ev.custom.service.DeviceSuperviseService;



@Service
public class DeviceSuperviseServiceImpl implements DeviceSuperviseService {
	@Autowired
	private DeviceSuperviseDao deviceSuperviseDao;

	@Override
	public DeviceSuperviseDO get(Long id){
		return deviceSuperviseDao.get(id);
	}

	@Override
	public List<DeviceSuperviseDO> list(Map<String, Object> map){
		return deviceSuperviseDao.list(map);
	}



	@Override
	public int count(Map<String, Object> map){
		return deviceSuperviseDao.count(map);
	}

	@Override
	public int save(DeviceSuperviseDO deviceSupervise){
		return deviceSuperviseDao.save(deviceSupervise);
	}

	@Override
	public int update(DeviceSuperviseDO deviceSupervise){
		return deviceSuperviseDao.update(deviceSupervise);
	}

	@Override
	public int remove(Long id){
		return deviceSuperviseDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return deviceSuperviseDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listPageMap(Map<String, Object> map) {
		return deviceSuperviseDao.listPageMap(map);
	}

//	public Map<String,Object> getOneDetail(Long id){
//		Map<String,Object> resultOne = Maps.newHashMap();
//		DeviceSuperviseDO ss = deviceSuperviseDao.get(id);
//		if(ss!=null){
//			resultOne.put("DATA",ss);
//		}
//		return   resultOne;
//	}
	
}
