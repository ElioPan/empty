package com.ev.custom.service.impl;

import com.ev.framework.config.Constant;
import com.ev.custom.dao.FrdbDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.domain.FrdbDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.FrdbService;
import com.ev.system.domain.UserDO;
import com.ev.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class FrdbServiceImpl implements FrdbService {
	@Autowired
	private FrdbDao frdbDao;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private UserService userService;
	
	@Override
	public FrdbDO get(Long id){
		return frdbDao.get(id);
	}
	
	@Override
	public List<FrdbDO> list(Map<String, Object> map){
		return frdbDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return frdbDao.count(map);
	}
	
	@Override
	public int save(FrdbDO frdb){
		return frdbDao.save(frdb);
	}
	
	@Override
	public int update(FrdbDO frdb){
		return frdbDao.update(frdb);
	}
	
	@Override
	public int remove(Long id){
		return frdbDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return frdbDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return frdbDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return frdbDao.countForMap(map);
	}

	@Override
	public Map<String, Object> detail(Long id) {

		Map<String,Object> results = new HashMap<String,Object>();

		List<DictionaryDO> deviceTypeList = dictionaryService.listByType("device_type");
		results.put("deviceTypeList",deviceTypeList);
		List<DictionaryDO> frtypeList = dictionaryService.listByType("fr_type");
		results.put("frTypeList",frtypeList);
		List<UserDO> userList = userService.list(null);
		results.put("userList",userList);
		FrdbDO frdbDO = get(id);
		results.put("frDb", frdbDO);
		//获取附件
		Map<String,Object> contentMap = new HashMap<String,Object>(){{put("assocId",frdbDO.getId());put("assocType",Constant.FRDB_APPEARANCE_ATTACHMENT);}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		return results;
	}

	@Override
	public void add(FrdbDO frDb, String[] taglocationappearanceImage) {
		save(frDb);
		//保存图片信息
		contentAssocService.saveList(Long.parseLong(frDb.getId().toString()),taglocationappearanceImage,Constant.FRDB_APPEARANCE_ATTACHMENT);
	}

	@Override
	public void edit(FrdbDO frDb, String[] taglocationappearanceImage, String[] deletetag_appearanceImage) {
		update(frDb);
		contentAssocService.saveList(Long.parseLong(frDb.getId().toString()),taglocationappearanceImage,Constant.FRDB_APPEARANCE_ATTACHMENT);
		contentAssocService.deleteList(deletetag_appearanceImage);
	}

}
