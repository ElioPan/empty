package com.ev.custom.service.impl;

import com.ev.common.domain.Tree;
import com.ev.framework.utils.BuildTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ev.custom.dao.DictionaryTypeDao;
import com.ev.custom.domain.DictionaryTypeDO;
import com.ev.custom.service.DictionaryTypeService;



@Service
public class DictionaryTypeServiceImpl implements DictionaryTypeService {
	@Autowired
	private DictionaryTypeDao dictionaryTypeDao;
	
	@Override
	public DictionaryTypeDO get(Long id){
		return dictionaryTypeDao.get(id);
	}
	
	@Override
	public List<DictionaryTypeDO> list(Map<String, Object> map){
		return dictionaryTypeDao.list(map);
	}

	@Override
	public Tree<DictionaryTypeDO> tree() {
		List<Tree<DictionaryTypeDO>> trees = new ArrayList<>();
		List<DictionaryTypeDO> dicTypeDOs = dictionaryTypeDao.list(null);
		for (DictionaryTypeDO dictionaryTypeDO : dicTypeDOs) {
			Tree<DictionaryTypeDO> tree = new Tree<>();
			tree.setId(dictionaryTypeDO.getId().toString());
			tree.setParentId(dictionaryTypeDO.getParentId()==null?"0":dictionaryTypeDO.getParentId().toString());
			tree.setText(dictionaryTypeDO.getName());
			Map<String, Object> attributes = new HashMap<>(16);
			attributes.put("code", dictionaryTypeDO.getCode());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DictionaryTypeDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public int count(Map<String, Object> map){
		return dictionaryTypeDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return dictionaryTypeDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return dictionaryTypeDao.countForMap(map);
	}

	@Override
	public int save(DictionaryTypeDO dictionaryType){
		return dictionaryTypeDao.save(dictionaryType);
	}
	
	@Override
	public int update(DictionaryTypeDO dictionaryType){
		return dictionaryTypeDao.update(dictionaryType);
	}
	
	@Override
	public int remove(Long id){
		return dictionaryTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return dictionaryTypeDao.batchRemove(ids);
	}
	
}
