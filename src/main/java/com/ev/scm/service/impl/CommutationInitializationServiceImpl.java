package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.dao.CommutationInitializationDao;
import com.ev.scm.domain.CommutationInitializationDO;
import com.ev.scm.service.CommutationInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class CommutationInitializationServiceImpl implements CommutationInitializationService {
	@Autowired
	private CommutationInitializationDao commutationInitializationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public CommutationInitializationDO get(Long id){
		return commutationInitializationDao.get(id);
	}
	
	@Override
	public List<CommutationInitializationDO> list(Map<String, Object> map){
		return commutationInitializationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return commutationInitializationDao.count(map);
	}
	
	@Override
	public int save(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.save(commutationInitialization);
	}

	@Override
	public List<Map<String, Object>> getListForMap(Map<String, Object> map) {
		return commutationInitializationDao.getListForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return commutationInitializationDao.countForMap(map);
	}

	@Override
	public int update(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.update(commutationInitialization);
	}

	@Override
	public int updateAll(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.updateAll(commutationInitialization);
	}
	
	@Override
	public int remove(Long id){
		return commutationInitializationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return commutationInitializationDao.batchRemove(ids);
	}

	@Override
	public R disposeAddAndChage(String body){

		List<CommutationInitializationDO> commutationInitializationDos= JSONObject.parseArray(body,CommutationInitializationDO.class);
		if(commutationInitializationDos.size()>0){
			for(CommutationInitializationDO commuIniDo:commutationInitializationDos){

				if(Objects.nonNull(commuIniDo.getId())){
					commutationInitializationDao.update(commuIniDo);
				}else{
					commutationInitializationDao.save(commuIniDo);
				}
			}
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing", null));
		}
	}




}
