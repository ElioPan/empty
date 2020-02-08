package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ev.custom.dao.DictionaryDao;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;


@Service
public class DictionaryServiceImpl implements DictionaryService {
	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Override
	public DictionaryDO get(Integer id){
		return dictionaryDao.get(id);
	}

	@Override
	public DictionaryDO getByValue(String value, String type) {
		Map<String,Object>map = Maps.newHashMapWithExpectedSize(2);
		map.put("value",value);
		map.put("type",type);
		return (DictionaryDO)dictionaryDao.list(map).get(0);
	}

	@Override
	public List<DictionaryDO> list(Map<String, Object> map){
		return dictionaryDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dictionaryDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return dictionaryDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return dictionaryDao.countForMap(map);
	}

	@Override
	public int save(DictionaryDO dictionary){
		return dictionaryDao.save(dictionary);
	}
	
	@Override
	public int update(DictionaryDO dictionary){
		return dictionaryDao.update(dictionary);
	}
	
	@Override
	public int remove(Integer id){
		return dictionaryDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return dictionaryDao.batchRemove(ids);
	}

	@Override
	//@Cacheable(value = "dictionary",key = "#type")
	public List<DictionaryDO> listByType(String type) {
		return dictionaryDao.listByType(type);
	}

	@Override
	public Map<String, Object> getDictsByType(String type) {
		List<DictionaryDO> dicts = dictionaryDao.listByType(type);
		JSONArray array = new JSONArray();
		if(dicts!=null && dicts.size()>0){
			for (DictionaryDO dict:dicts) {
				JSONObject ob = new JSONObject(new LinkedHashMap<String, Object>());
				ob.put("id", dict.getId());
				ob.put("name", dict.getName() == null ? "" : dict.getName());
				ob.put("value", dict.getValue() == null ? "" : dict.getValue());
				ob.put("typeId", dict.getTypeId() == null ? "" : dict.getTypeId());
				ob.put("typeCode", type);
				array.add(ob);
			}
		}
		Map<String,Object> results = Maps.newHashMap();
		results.put("datas",array);
		results.put("type",type);
		return results;
	}

	@Override
	public Map<String, Object> getDictMap() {
		List<DictionaryDO> dicts = dictionaryDao.list(new HashMap<String,Object>(16));
		Map<String,Object> dictMap = new HashMap<>();
		if(dicts!=null && dicts.size()>0){
			for (DictionaryDO dict:dicts) {
				dictMap.put(dict.getId().toString(),dict.getName());
			}
		}
		return dictMap;
	}


}
