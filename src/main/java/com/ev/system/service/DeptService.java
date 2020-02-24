package com.ev.system.service;

import com.ev.common.domain.Tree;
import com.ev.system.domain.DeptDO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 */
public interface DeptService {
	
	DeptDO get(Long deptId);
	
	List<DeptDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeptDO sysDept) throws IOException, ParseException;
	
	int update(DeptDO sysDept) throws IOException, ParseException;
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);

	Tree<DeptDO> getTree(String deptId);
	
	boolean checkDeptHasUser(Long deptId);

    Map<String,Object> getDepts(Long deptId, Integer isContainChildren);

    Map<String,Object> getDeptMap();

	DeptDO getIdpathByUserId(Long userId);
}
