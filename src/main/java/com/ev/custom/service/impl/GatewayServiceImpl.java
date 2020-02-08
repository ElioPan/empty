package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.GatewayDao;
import com.ev.custom.domain.GatewayDO;
import com.ev.custom.service.GatewayService;
import com.google.common.collect.Maps;



@Service
public class GatewayServiceImpl implements GatewayService {
	@Autowired
	private GatewayDao gatewayDao;
	
	@Override
	public GatewayDO get(Long id){
		return gatewayDao.get(id);
	}
	
	@Override
	public List<GatewayDO> list(Map<String, Object> map){
		return gatewayDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return gatewayDao.count(map);
	}
	
	@Override
	public int save(GatewayDO gateway){
		return gatewayDao.save(gateway);
	}
	
	@Override
	public int update(GatewayDO gateway){
		return gatewayDao.update(gateway);
	}
	
	@Override
	public int remove(Long id){
		return gatewayDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return gatewayDao.batchRemove(ids);
	}

	@Override
	public int checkSave(String serialNo) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
		param.put("serialNo", serialNo);
		return this.count(param);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> query) {
		return gatewayDao.listForMap(query);
	}

	@Override
	public int logicRemove(Long id) {
		GatewayDO gatewayDO = new GatewayDO();
		gatewayDO.setId(id);
		gatewayDO.setDelFlag(1);
		return this.update(gatewayDO);
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
