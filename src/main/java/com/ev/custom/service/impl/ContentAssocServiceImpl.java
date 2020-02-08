package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ev.custom.dao.ContentAssocDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.service.ContentAssocService;



@Service
public class ContentAssocServiceImpl implements ContentAssocService {
	@Autowired
	private ContentAssocDao contentAssocDao;
	
	@Override
	public ContentAssocDO get(Integer id){
		return contentAssocDao.get(id);
	}
	
	@Override
	public List<ContentAssocDO> list(Map<String, Object> map){
		return contentAssocDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return contentAssocDao.count(map);
	}
	
	@Override
	public int save(ContentAssocDO contentAssoc){
		return contentAssocDao.save(contentAssoc);
	}
	
	@Override
	public int update(ContentAssocDO contentAssoc){
		return contentAssocDao.update(contentAssoc);
	}
	
	@Override
	public int remove(Integer id){
		return contentAssocDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return contentAssocDao.batchRemove(ids);
	}

	@Override
	public void saveList(Long assocerId, String[] paths, String type) {
		Integer d= assocerId.intValue();
		if(paths != null && paths.length>0){
			for(String path : paths){
				ContentAssocDO contentAssocDO = new ContentAssocDO(d,type,path);
				save(contentAssocDO);
			}
		}
	}

	@Override
	public void saveList(Long assocerId, JSONArray pathList, String type) {
		Integer id= assocerId.intValue();
		for(int i=0;i<pathList.size();i++){
			JSONObject obj = pathList.getJSONObject(i);
			String fileName = obj.get("fileName").toString();
			String filePath = obj.get("filePath").toString();
			ContentAssocDO contentAssocDO = new ContentAssocDO(id,type,fileName,filePath);
			save(contentAssocDO);
		}
	}


	@Override
	public void deleteList(String[] ids) {
		if(ids != null && ids.length>0){
			for(String id : ids){
				remove(Integer.parseInt(id));
			}
		}
	}

	@Override
	public int removeByAssocIdAndType(Long[] id, String type) {

		Map<String,Object> query =new HashMap<>();
		int[] ids = new int[id.length];

		for (int i = 0; i < id.length; i++) {
			ids[i] = id[i].intValue();
		}
		query.put("assocId",ids);
		query.put("assocType",type);

		return contentAssocDao.removeByAssocIdAndType(query);
	}
	
	@Override
	public int removeByAssocIdsAndTypes(Long[] id, String[] type) {

		Map<String,Object> query =new HashMap<>();
		int[] ids = new int[id.length];

		for (int i = 0; i < id.length; i++) {
			ids[i] = id[i].intValue();
		}
		query.put("assocId",ids);
		query.put("assocType",type);

		return contentAssocDao.removeByAssocIdsAndTypes(query);
	}


}
