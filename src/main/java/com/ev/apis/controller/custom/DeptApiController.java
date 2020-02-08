package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.common.controller.BaseController;
import com.ev.common.domain.Tree;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.DeptDO;
import com.ev.system.service.DeptService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/", tags = "部门管理")
public class DeptApiController extends BaseController {
    @Autowired
    private DeptService sysDeptService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/dept/list",method = RequestMethod.GET,apiTitle = "获取部门列表信息")
    @ApiOperation("获取部门列表信息")
    public R list(@ApiParam(value = "部门名称") @RequestParam(value = "deptName",defaultValue = "",required = false)  String deptName){
        Map<String,Object> results = Maps.newHashMap();
        List<DeptDO> sysDeptList = sysDeptService.list(new HashMap<String,Object>(1));
        if(StringUtils.isNotBlank(deptName)){
            Map<String, Object> nameQuery = new HashMap<>(1);
            nameQuery.put("name",deptName);
            List<DeptDO> deptList = sysDeptService.list(nameQuery);
            List<DeptDO> tempDeptList = new ArrayList<>();
            for(DeptDO dept : deptList){
                for(DeptDO deptAll : sysDeptList){
                    if((dept.getIdPath().indexOf(deptAll.getIdPath()) != -1 || deptAll.getIdPath().indexOf(dept.getIdPath()) != -1) && !tempDeptList.contains(deptAll)){
                        tempDeptList.add(deptAll);
                    }
                }
            }
            sysDeptList = tempDeptList;
        }

        if(sysDeptList!=null && sysDeptList.size()>0){
            for(DeptDO dept:sysDeptList){
                if(dept.getType()!=null){
                    dept.setTypeName(dictionaryService.get(dept.getType().intValue()).getName());
                }
            }
        }
        results.put("data",sysDeptList);
        return  R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dept/add",method = RequestMethod.POST,apiTitle = "新增部门")
    @ApiOperation("新增部门")
    public R save(DeptDO sysDept) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        if (sysDeptService.save(sysDept) > 0) {
            Long deptId = sysDept.getDeptId();
            Long parentId = sysDept.getParentId();
            if(parentId==0){
                sysDept.setIdPath("/"+parentId+"/"+deptId+"/");
            }else{
                DeptDO parentDept = sysDeptService.get(parentId);
                sysDept.setIdPath(parentDept.getIdPath()+deptId+"/");
            }
            sysDeptService.update(sysDept);

            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dept/update",method = RequestMethod.POST,apiTitle = "修改部门")
    @ApiOperation("修改部门")
    public R update(DeptDO sysDept) {
        DeptDO oldDept = sysDeptService.get(sysDept.getDeptId());

        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }

        String oldPath = oldDept.getIdPath();
        StringBuilder newPath = new StringBuilder("");
        //获取新的父组织
        Long newParenetId = sysDept.getParentId();
        if(newParenetId==0){
        	newPath.append("/").append(0).append("/").append(sysDept.getDeptId()).append("/");
//            newPath = "/"+0+"/"+sysDept.getDeptId()+"/";
        }else{
            DeptDO parentDept = sysDeptService.get(newParenetId);
            newPath.append(parentDept.getIdPath()).append(sysDept.getDeptId()).append("/");
//            newPath = parentDept.getIdPath()+sysDept.getDeptId()+"/";
        }
        sysDept.setIdPath(newPath.toString());
        if (sysDeptService.update(sysDept) > 0) {
            if(!oldPath.equals(newPath.toString())){
                //获取所有子孙部门
                Map<String,Object> map = new HashMap<>();
                map.put("idPath",oldPath);
                List<DeptDO> deptList = sysDeptService.list(map);
                for(DeptDO deptDO : deptList){
                    deptDO.setIdPath(deptDO.getIdPath().replace(oldPath,newPath));
                    sysDeptService.update(deptDO);
                }
            }
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dept/remove",method = RequestMethod.POST,apiTitle = "删除部门")
    @ApiOperation("删除部门")
    public R remove(Long deptId) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", deptId);
        if(sysDeptService.count(map)>0) {
            return R.error(1, messageSourceHandler.getMessage("basicInfo.dept.update",null));
        }
        if(sysDeptService.checkDeptHasUser(deptId)) {
            if (sysDeptService.remove(deptId) > 0) {
                return R.ok();
            }
        }else {
            return R.error(1, messageSourceHandler.getMessage("basicInfo.deptContainUser.update",null));
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/dept/tree",method = RequestMethod.GET,apiTitle = "获取部门树信息")
    @ApiOperation("获取部门树信息")
    public R tree(@ApiParam(value = "部门ID") @RequestParam(value = "deptId",defaultValue = "",required = false)  String deptId){
        Map<String,Object> results = Maps.newHashMap();
        Tree<DeptDO> tree = new Tree<DeptDO>();
        tree = sysDeptService.getTree(deptId);
        results.put("data",tree);
        return  R.ok(results);
    }
}
