package com.ev.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.WeChatService;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import com.ev.common.domain.Tree;
import com.ev.framework.utils.BuildTree;
import com.ev.system.dao.DeptDao;
import com.ev.system.domain.DeptDO;
import com.ev.system.service.DeptService;



@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao sysDeptMapper;

	@Autowired
	private WeChatService weChatService;

	@Override
	public DeptDO get(Long deptId){
		return sysDeptMapper.get(deptId);
	}

	@Override
	public List<DeptDO> list(Map<String, Object> map){
		return sysDeptMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return sysDeptMapper.count(map);
	}

	@Override
	public int save(DeptDO sysDept) throws IOException, ParseException {
		int count = sysDeptMapper.save(sysDept);
		//同步企业微信部门信息
		weChatService.createDepartment(sysDept);
		return count;
	}

	@Override
	public int update(DeptDO sysDept) throws IOException, ParseException {
		int count = sysDeptMapper.update(sysDept);
		//同步企业微信部门信息
		weChatService.updateDeptment(sysDept);
		return count;
	}

	@Override
	public int remove(Long deptId){
		return sysDeptMapper.remove(deptId);
	}

	@Override
	public int batchRemove(Long[] deptIds){
		return sysDeptMapper.batchRemove(deptIds);
	}

	@Override
	public Tree<DeptDO> getTree(String deptId) {
		List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(deptId)){
			params.put("deptId",deptId);
		}
		List<DeptDO> sysDepts = sysDeptMapper.list(params);
		for (DeptDO sysDept : sysDepts) {
			Tree<DeptDO> tree = new Tree<DeptDO>();
			tree.setId(sysDept.getDeptId().toString());
			tree.setParentId(sysDept.getParentId().toString());
			tree.setText(sysDept.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DeptDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public boolean checkDeptHasUser(Long deptId) {
        // 查询部门下有无员工
		int result = sysDeptMapper.getDeptUserNumber(deptId);
		return result==0?true:false;
	}

	@Override
	public Map<String, Object> getDepts(Long deptId, Integer isContainChildren) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deptId",deptId);
		params.put("isContainChildren",isContainChildren);
		List<DeptDO> sysDepts = sysDeptMapper.list(params);
		JSONArray array = new JSONArray();
		if(sysDepts!=null && sysDepts.size()>0){
			for (DeptDO dept : sysDepts) {
				JSONObject ob = new JSONObject(new LinkedHashMap());
				ob.put("id", dept.getDeptId());
				ob.put("name", dept.getName() == null ? "" : dept.getName());
				array.add(ob);
			}
		}
		Map<String,Object> results = Maps.newHashMap();
		results.put("datas",array);
		return results;
	}

	@Override
	public Map<String, Object> getDeptMap() {
		List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String,Object>(16));
		Map<String,Object> map = new HashMap<>();
		if(sysDepts!=null && sysDepts.size()>0){
			for (DeptDO dept:sysDepts) {
				map.put(dept.getDeptId().toString(),dept.getName());
			}
		}
		return map;
	}

	@Override
	public DeptDO getIdpathByUserId(Long userId) {
		return sysDeptMapper.getIdpathByUserId(userId);
	}

}
