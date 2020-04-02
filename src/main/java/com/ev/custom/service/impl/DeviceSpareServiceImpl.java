package com.ev.custom.service.impl;

import com.ev.custom.dao.DeviceSpareDao;
import com.ev.custom.domain.DeviceSpareDO;
import com.ev.custom.service.DeviceSpareService;
import com.ev.scm.domain.StockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Service
public class DeviceSpareServiceImpl implements DeviceSpareService {
	@Autowired
	private DeviceSpareDao deviceSpareDao;



	@Override
	public DeviceSpareDO get(Long id){
		return deviceSpareDao.get(id);
	}
	
	@Override
	public List<DeviceSpareDO> list(Map<String, Object> map){
		return deviceSpareDao.list(map);
	}

	@Override
	public List<Map<String, Object>> RelatedSpareParts(Map<String, Object> map) {
		return deviceSpareDao.getRelatedSpareParts(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return deviceSpareDao.count(map);
	}

	@Override
	public int countSparesNoDevice() {
		return deviceSpareDao.countSparesNoDevice();
	}

	@Override
	public int save(DeviceSpareDO deviceSpare){
		return deviceSpareDao.save(deviceSpare);
	}

	@Override
	public int update(DeviceSpareDO deviceSpare){
		return deviceSpareDao.update(deviceSpare);
	}

	@Override
	public int countRelatedSpareParts(Map<String, Object> map) {
		return deviceSpareDao.countRelatedSpareParts(map);
	}

	@Override
	public int remove(Long id){
		return deviceSpareDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return deviceSpareDao.batchRemove(ids);
	}

	@Override
	public List<StockDO> getSpartPartsCount(Long[] ids) {
		return deviceSpareDao.getSpartPartsCount(ids);
	}

	@Override
	public int saveSparesPart(Long[] spreId,Long deviceID,Integer warehouseId) {
		DeviceSpareDO deviceSpareDo=new DeviceSpareDO();
		deviceSpareDo.setDeviceId(deviceID);
		int count=0;
		for(int i=0;i<spreId.length;i++){
			deviceSpareDo.setSpareId(spreId[i]);
			deviceSpareDo.setSpareLocationid(warehouseId);
			count+=deviceSpareDao.save(deviceSpareDo);
		}
		return count;
	}

	@Override
	public int removeSpareChilden(Long[] ids,Long deviceId) {


       List<Long> list = new ArrayList<Long>();
       for(int i = 0 ; i <ids.length ; i++){
//           list.add(i+0L);
		   list.add(ids[i]);
       }
		int count=deviceSpareDao.removeDeviceSpare(list,deviceId);

		return count;
	}

}
