package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.FundInitializationDao;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.service.FundInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class FundInitializationServiceImpl implements FundInitializationService {
	@Autowired
	private FundInitializationDao fundInitializationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;



	@Override
	public FundInitializationDO get(Integer id){
		return fundInitializationDao.get(id);
	}
	
	@Override
	public List<FundInitializationDO> list(Map<String, Object> map){
		return fundInitializationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return fundInitializationDao.count(map);
	}
	
	@Override
	public int save(FundInitializationDO fundInitialization){
		return fundInitializationDao.save(fundInitialization);
	}
	
	@Override
	public int update(FundInitializationDO fundInitialization){
		return fundInitializationDao.update(fundInitialization);
	}

	@Override
	public int updateAll(FundInitializationDO fundInitialization){
		return fundInitializationDao.updateAll(fundInitialization);
	}
	
	@Override
	public int remove(Integer id){
		return fundInitializationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return fundInitializationDao.batchRemove(ids);
	}



	@Override
	public R disposeAddAndChage(Integer usingStart,String body){

		if(!StringUtils.isEmpty(body)){
			List<FundInitializationDO> fundInitializationDOS = JSONObject.parseArray(body, FundInitializationDO.class);
			if(fundInitializationDOS.size()>0){

			}else{

			}

			return  R.ok();
		}else{

			return R.error();

		}
	}





}
