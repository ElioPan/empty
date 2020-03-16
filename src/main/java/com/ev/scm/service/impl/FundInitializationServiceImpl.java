package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.FundInitializationDao;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.service.FundInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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
	public R disposeAddAndChage(String body ){

		if(!StringUtils.isEmpty(body)){
			List<FundInitializationDO> fundInitializationDOS = JSONObject.parseArray(body, FundInitializationDO.class);
             List<FundInitializationDO> fundInitializationDos = fundInitializationDOS.stream().filter(FundInitializationDO -> FundInitializationDO.getId() != null).collect(Collectors.toList());

               if(fundInitializationDos.size()>0){
                    FundInitializationDO fundInitializationDO = this.get(fundInitializationDos.get(0).getId());
                   if(Objects.equals(1,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
                   if(Objects.equals(0,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
               }
                //允许修改或者新增
                   for(FundInitializationDO fundInitializationDO:fundInitializationDOS){
                       if(Objects.isNull(fundInitializationDO.getId())){
                        //新增
                           this.save(fundInitializationDO);
                       }else{
                           //更新
                           this.update(fundInitializationDO);
                       }
                   }
                   return  R.ok();
		}else{
			return R.error();
		}
	}

    @Override
    public R disposeStartUsing(Long[] ids){
        Map<String,Object>  map= new HashMap<>();
        map.put("ids",ids);
        map.put("usingStart",0);
        fundInitializationDao.updateUsingStart(map);
        return R.ok();
    }


    @Override
    public R disposeForbidden(Long[] ids){
        Map<String,Object>  map= new HashMap<>();
        map.put("ids",ids);
        map.put("usingStart",1);
        fundInitializationDao.updateUsingStart(map);
        return R.ok();
    }

    @Override
    public Map<String, Object> countOfList(Map<String, Object> map) {
        return fundInitializationDao.countOfList(map);
    }

    @Override
    public List<Map<String, Object>> getlist(Map<String, Object> map) {
        return fundInitializationDao.getlist(map);
    }


}
