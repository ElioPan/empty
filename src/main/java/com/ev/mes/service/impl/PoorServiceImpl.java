package com.ev.mes.service.impl;

import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.mes.dao.PoorDao;
import com.ev.mes.domain.PoorDO;
import com.ev.mes.service.PoorService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PoorServiceImpl implements PoorService {
	@Autowired
	private PoorDao poorDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public PoorDO get(Long id){
		return poorDao.get(id);
	}
	
	@Override
	public List<PoorDO> list(Map<String, Object> map){
		return poorDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return poorDao.count(map);
	}
	
	@Override
	public int save(PoorDO poor){
		return poorDao.save(poor);
	}
	
	@Override
	public int update(PoorDO poor){
		return poorDao.update(poor);
	}
	
	@Override
	public int remove(Long id){
		return poorDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return poorDao.batchRemove(ids);
	}

	@Override
	public R savePoor(PoorDO poorDO){
		if(Objects.nonNull(poorDO)){
			Map<String,Object>  map= new HashMap<>();
			map.put("code",poorDO.getCode());
			if(this.countForMap(map)>0){
				return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
			}else{
				poorDao.save(poorDO);
				return R.ok();
			}
		}else{
			//"所传明细为空！"
			return R.error(messageSourceHandler.getMessage("apis.mes.process.detaiNoull",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return poorDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return poorDao.countForMap(map);
	}


	@Override
	public R batchPoorDetail(Long[] ids ) {
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		results.put("id",ids);
		poorDao.deletOfPoor(results);
		return R.ok();
	}

}
