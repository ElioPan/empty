package com.ev.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.WeChatService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.config.ConstantsConfig;
import com.ev.common.domain.FileDO;
import com.ev.common.service.FileService;
import com.ev.framework.utils.*;
import com.ev.system.dao.*;
import com.ev.system.domain.*;
import com.ev.system.service.RoleService;
import com.ev.system.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.common.domain.Tree;
import com.ev.system.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

//@CacheConfig(cacheNames = "user")
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Autowired
    RoleDataDao roleDataMapper;
    @Autowired
    DeptDao deptMapper;
    @Autowired
    private FileService sysFileService;
    @Autowired
    private ConstantsConfig constantsConfig;
    @Autowired
    UserGroupDao userGroupMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    WeChatService weChatService;



    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
//    @Cacheable(key = "#id")
    public UserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        List<Long> groupIds = userGroupMapper.listGroupId(id);
        Long dataPermission = null;
        List<Long> deptDatas = Lists.newArrayList();
        if(roleIds.size()>0){
            RoleDO roleDO = roleService.get(roleIds.get(0));
            dataPermission = roleDO.getDataPermission();
            Long roleId = roleDO.getRoleId();
            if (Objects.equals(dataPermission, Constant.CUSTOM_DATA) ) {
                Map<String,Object> param = Maps.newHashMap();
                param.put("roleId",roleId);
                List<RoleDataDO> roleDataDOList = roleDataMapper.list(param);
                if(roleDataDOList.size()>0){
                    for (RoleDataDO roleDataDO : roleDataDOList) {
                        deptDatas.add(roleDataDO.getDeptId());
                    }
                }
            }
        }
        UserDO user = userMapper.get(id);
//        user.setDeptName(deptMapper.get(user.getDeptId()).getName());
        user.setRoleIds(roleIds);
        user.setGroupIds(groupIds);
        user.setDataPermission(dataPermission);
        user.setDeptDatas(deptDatas);
        return user;
    }

    @Override
    public List<UserDO> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Override
    public List<Map<String,Object>> listForMap(Map<String, Object> map) {
        return userMapper.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return userMapper.countForMap(map);
    }

    @Transactional
    @Override
    public int save(UserDO user) throws IOException, ParseException {
        int count = userMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        if(roles!= null && roles.size()>0){
            for (Long roleId : roles) {
                RoleDO roleDO = this.roleService.get(roleId);
                UserRoleDO ur = new UserRoleDO();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchSave(list);
            }
        }
        //更新用戶和用户组关系
        String groupstr = "";
        List<Long> groupIds = user.getGroupIds();
        userGroupMapper.removeByUserId(userId);
        List<UserGroupDO> userGroupList = new ArrayList<>();
        if(groupIds!=null && groupIds.size()>0){
            for (Long groupId : groupIds) {
                UserGroupDO ug = new UserGroupDO();
                ug.setUserId(userId);
                ug.setGroupId(groupId);
                userGroupList.add(ug);
            }
        }
        if (userGroupList.size() > 0) {
            userGroupMapper.batchSave(userGroupList);
        }
        user.setGroupstr(groupstr);
        userMapper.update(user);
        //同步企业微信
        if(weChatService.checkIsUse()){
            weChatService.saveUser(user);
        }
        return count;
    }

    @Override
    public int update(UserDO user) throws IOException, ParseException {
        Long userId = user.getUserId();
        UserDO oldUser = get(userId);
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        if(roles!= null && roles.size()>0) {
            for (Long roleId : roles) {
                RoleDO roleDO = this.roleService.get(roleId);
                UserRoleDO ur = new UserRoleDO();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchSave(list);
            }
        }
        //更新用戶和用户组关系
        String groupstr = "";
        List<Long> groupIds = user.getGroupIds();
        userGroupMapper.removeByUserId(userId);
        List<UserGroupDO> userGroupList = new ArrayList<>();
        if(groupIds!=null && groupIds.size()>0){
            for (Long groupId : groupIds) {
                UserGroupDO ug = new UserGroupDO();
                ug.setUserId(userId);
                ug.setGroupId(groupId);
                userGroupList.add(ug);
            }
        }
        if (userGroupList.size() > 0) {
            userGroupMapper.batchSave(userGroupList);
        }
        user.setGroupstr(groupstr);
        int r = userMapper.update(user);
        //同步企业微信
        if(weChatService.checkIsUse()){
            weChatService.saveUser(user);
        }
        return r;
    }

    @Override
    public int remove(Long userId) {
        userRoleMapper.removeByUserId(userId);
        userGroupMapper.removeByUserId(userId);
        return userMapper.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userMapper.list(params).size() > 0;
        return exit;
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
                userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
                return userMapper.update(userDO);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    @Override
    public int adminResetPwd(UserVO userVO) throws Exception {
        UserDO userDO = get(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
        return userMapper.update(userDO);


    }

    @Transactional
    @Override
    public int batchremove(Long[] userIds) {
        int count = userMapper.batchRemove(userIds);
        userRoleMapper.batchRemoveByUserId(userIds);
        userGroupMapper.batchRemoveByUserId(userIds);
        return count;
    }

    @Override
    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> depts = deptMapper.list(new HashMap<String, Object>(16));
        Long[] pDepts = deptMapper.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (DeptDO dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<UserDO> users = userMapper.list(new HashMap<String, Object>(16));
        for (UserDO user : users) {
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DeptDO> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public int updatePersonal(UserDO userDO) {
        return userMapper.update(userDO);
    }

    @Override
    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
        //获取图片后缀
        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
        String[] str = avatar_data.split(",");
        //获取截取的x坐标
        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
        //获取截取的y坐标
        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
        //获取截取的高度
        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
        //获取截取的宽度
        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
        //获取旋转的角度
        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
        try {
            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(rotateImage, prefix, out);
            //转换后存入数据库
            byte[] b = out.toByteArray();
            FileUtil.uploadFile(b, constantsConfig.getUploadPath(), fileName);
        } catch (Exception e) {
            throw new Exception("图片裁剪错误！！");
        }
        Map<String, Object> result = new HashMap<>();
        if (sysFileService.save(sysFile) > 0) {
            UserDO userDO = new UserDO();
            userDO.setUserId(userId);
            userDO.setPicId(sysFile.getId());
            if (userMapper.update(userDO) > 0) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }

    @Override
    public UserDO findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public int appResetPasswd(UserDO userDO,String oldpasswd, String newpasswd) throws Exception {
        if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), oldpasswd), userDO.getPassword())) {
            userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), newpasswd));
            userDO.setIsfirst(1);
            return userMapper.update(userDO);
        } else {
            return 500;
        }
    }

    @Override
    public boolean isAdmin(UserDO currUser) {
        //判断用户是否有超级管理员角色
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", currUser.getUserId());
        List<UserRoleDO> list = this.userRoleMapper.list(map);
        boolean isAdmin = false;
        if(list!=null && list.size()>0){
            for (UserRoleDO userRole:list){
                RoleDO role = this.roleService.get(userRole.getRoleId());
                if(role.getRoleName().equals(Constant.ADMIN_ROLE)){
                    isAdmin = true;
                }
            }
        }
        return isAdmin;
    }

    @Override
    public Map<String, Object> getUsers(Long deptId, Integer isContainChildren) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("deptId",deptId);
        params.put("isContainChildren",isContainChildren);
        List<Map<String,Object>> users = userMapper.listForMap(params);
        JSONArray array = new JSONArray();
        if(users!=null && users.size()>0){
            for (Map<String,Object> user : users) {
                JSONObject ob = new JSONObject(new LinkedHashMap());
                ob.put("id", user.get("userId"));
                ob.put("name", user.get("NAME") == null ? "" : user.get("NAME"));
                ob.put("deptId", user.get("deptId") == null ? "0" : user.get("deptId"));
                ob.put("deptName", user.get("deptName") == null ? "暂无部门" : user.get("deptName"));
                ob.put("mobile", user.get("mobile") == null ? "暂无电话" : user.get("mobile"));
                array.add(ob);
            }
        }
        Map<String,Object> results = Maps.newHashMap();
        results.put("datas",array);
        return results;
    }

    @Override
    public Map<String, Object> getUserMap() {
        List<UserDO> users = userMapper.list(new HashMap<String,Object>(16));
        Map<String,Object> map = new HashMap<>();
        if(users!=null && users.size()>0){
            for (UserDO user:users) {
                map.put(user.getUserId().toString(),user.getName());
            }
        }
        return map;
    }

    @Override
    public boolean checkAdmin(UserDO user) {
        boolean flag = false;
        Map<String, Object> state = new HashMap<>(16);
        state.put("userId", user.getUserId());
        List<UserRoleDO> list = this.userRoleMapper.list(state);
        if(list!=null && list.size()>0){
            for (UserRoleDO userRole:list){
                RoleDO role = this.roleService.get(userRole.getRoleId());
                if (role.getRoleSign()!=null && role.getRoleSign().equals("admin")){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public int edit(UserDO user) throws IOException, ParseException {
        int count = userMapper.update(user);
        if(weChatService.checkIsUse()){
            if(user.getStatus()==1){
                weChatService.saveUser(user);
            }else{
                weChatService.deleteUser(user.getUsername());
            }
        }
        //TODO
        return count;
    }

    @Override
    public String selectByIdSet(List<Long> ids) {
        return userMapper.selectByIdSet(ids);
    }
}
