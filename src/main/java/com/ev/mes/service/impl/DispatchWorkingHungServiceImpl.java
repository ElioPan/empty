package com.ev.mes.service.impl;

import com.ev.framework.utils.R;
import com.ev.mes.dao.DispatchWorkingHungDao;
import com.ev.mes.domain.DispatchWorkingHungDO;
import com.ev.mes.service.DispatchWorkingHungService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class DispatchWorkingHungServiceImpl implements DispatchWorkingHungService {
	@Autowired
	private DispatchWorkingHungDao dispatchWorkingHungDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public DispatchWorkingHungDO get(Long id){
		return dispatchWorkingHungDao.get(id);
	}
	
	@Override
	public List<DispatchWorkingHungDO> list(Map<String, Object> map){
		return dispatchWorkingHungDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dispatchWorkingHungDao.count(map);
	}
	
	@Override
	public int save(DispatchWorkingHungDO dispatchWorkingHung){
		return dispatchWorkingHungDao.save(dispatchWorkingHung);
	}
	
	@Override
	public int update(DispatchWorkingHungDO dispatchWorkingHung){
		return dispatchWorkingHungDao.update(dispatchWorkingHung);
	}
	
	@Override
	public int remove(Long id){
		return dispatchWorkingHungDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return dispatchWorkingHungDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> getWHungTime(int sign, Long dispatchId) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("sign",sign);
		params.put("dispatchItemId",dispatchId);
		return dispatchWorkingHungDao.getWHungTime(params);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return dispatchWorkingHungDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return dispatchWorkingHungDao.countForMap(map);
	}

	@Override
	public R deletOfHungOrWorking(Long[] id) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("id",id);
		int i = dispatchWorkingHungDao.canDelet(params);
		if(i==0){
			dispatchWorkingHungDao.batchRemove(id);
			return R.ok();
		}else{
			//"工单存在结案状态，禁止删除！"
			return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.OverStatus",null));
		}
	}

}
