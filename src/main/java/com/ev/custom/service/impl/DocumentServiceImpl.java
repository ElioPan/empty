package com.ev.custom.service.impl;

import com.ev.framework.config.ConstantForDevice;
import com.ev.custom.dao.DocumentDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.domain.DocumentDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.DocumentService;
import com.ev.system.domain.UserDO;
import com.ev.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DocumentServiceImpl implements DocumentService {
	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private UserService userService;
	
	@Override
	public DocumentDO get(Long id){
		return documentDao.get(id);
	}
	
	@Override
	public List<DocumentDO> list(Map<String, Object> map){
		return documentDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return documentDao.count(map);
	}
	
	@Override
	public int save(DocumentDO document){
		return documentDao.save(document);
	}
	
	@Override
	public int update(DocumentDO document){
		return documentDao.update(document);
	}
	
	@Override
	public int remove(Long id){
		return documentDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return documentDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return documentDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return documentDao.countForMap(map);
	}

	@Override
	public Map<String, Object> detail(Long id) {

		Map<String,Object> results = new HashMap<>();

		List<DictionaryDO> documentTypeList = dictionaryService.listByType("document_type");
		results.put("documentTypeList",documentTypeList);
		List<DictionaryDO> sourceList = dictionaryService.listByType("document_source");
		results.put("sourceList",sourceList);
		List<UserDO> userList = userService.list(null);
		results.put("userList",userList);
		DocumentDO documentDO = get(id);
		results.put("document", documentDO);
		//获取附件
		Map<String,Object> contentMap = new HashMap<String,Object>(){{put("assocId",documentDO.getId());put("assocType", ConstantForDevice.DOCUMENT_APPEARANCE_ATTACHMENT);}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		return results;
	}

	@Override
	public void add(DocumentDO document, String[] taglocationappearanceImage) {
		//保存文档信息
		save(document);
		//保存图片信息
		contentAssocService.saveList(Long.parseLong(document.getId().toString()),taglocationappearanceImage, ConstantForDevice.DOCUMENT_APPEARANCE_ATTACHMENT);
	}

	@Override
	public void edit(DocumentDO document, String[] taglocationappearanceImage, String[] deletetagAppearanceImage) {
		update(document);
		contentAssocService.saveList(Long.parseLong(document.getId().toString()),taglocationappearanceImage, ConstantForDevice.DOCUMENT_APPEARANCE_ATTACHMENT);
		contentAssocService.deleteList(deletetagAppearanceImage);
	}

}
