package com.ev.system.service.impl;

import com.ev.framework.config.Constant;
import com.ev.system.dao.*;
import com.ev.system.domain.RoleDO;
import com.ev.system.domain.RoleDataDO;
import com.ev.system.domain.RoleMenuDO;
import com.ev.system.service.RoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class RoleServiceImpl implements RoleService {

    public static final String ROLE_ALL_KEY = "\"role_all\"";

    public static final String DEMO_CACHE_NAME = "role";

    @Autowired
    RoleDao roleMapper;
    @Autowired
    RoleMenuDao roleMenuMapper;
    @Autowired
    UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Autowired
    RoleDataDao roleDataMapper;

    @Override
    public List<RoleDO> list() {
        List<RoleDO> roles = roleMapper.list(new HashMap<>(16));
        return roles;
    }


    @Override
    public List<RoleDO> list(Long userId) {
        List<Long> rolesIds = userRoleMapper.listRoleId(userId);
        List<RoleDO> roles = roleMapper.list(new HashMap<>(16));
        for (RoleDO roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), roleId)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }
    @Transactional
    @Override
    public int save(RoleDO role) {
        int count = roleMapper.save(role);
        List<Long> ids = Lists.newArrayList();
        List<Long> menuIds = role.getMenuIds();
        List<Long> appMenuIds = role.getAppMenuIds();
        List<Long> padMenuIds = role.getPadMenuIds();
        if (menuIds != null) {
            ids.addAll(menuIds);
        }
        if(appMenuIds!=null){
            ids.addAll(appMenuIds);
        }
        if(padMenuIds!=null){
            ids.addAll(padMenuIds);
        }
        if (ids.size()>0) {
            Long roleId = role.getRoleId();
            List<RoleMenuDO> rms = new ArrayList<>();
            for (Long menuId : ids) {
                RoleMenuDO rmDo = new RoleMenuDO();
                rmDo.setRoleId(roleId);
                rmDo.setMenuId(menuId);
                rms.add(rmDo);
            }
            roleMenuMapper.removeByRoleId(roleId);
            if (rms.size() > 0) {
                roleMenuMapper.batchSave(rms);
            }
        }
        return count;
    }

    @Transactional
    @Override
    public int remove(Long id) {
        if (isUserUse(id)){
            return -1;
        }
        int count = roleMapper.remove(id);
        userRoleMapper.removeByRoleId(id);
        roleMenuMapper.removeByRoleId(id);
        roleDataMapper.deleteByRoleId(id);
        return count;
    }

    @Override
    public boolean isUserUse(Long id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("roleId",id);
        return userRoleMapper.count(map)>0;
    }

    @Override
    public boolean checkSave(RoleDO role) {
        return roleMapper.checkSave(role)==0;
    }

    @Override
    public RoleDO get(Long id) {
        RoleDO roleDO = roleMapper.get(id);
        return roleDO;
    }

    @Override
    public int update(RoleDO role) {
        int r = roleMapper.update(role);
        List<Long> ids = Lists.newArrayList();
        List<Long> menuIds = role.getMenuIds();
        List<Long> appMenuIds = role.getAppMenuIds();
        List<Long> padMenuIds = role.getPadMenuIds();
        if (menuIds != null) {
            ids.addAll(menuIds);
        }
        if(appMenuIds!=null){
            ids.addAll(appMenuIds);
        }
        if(padMenuIds!=null){
            ids.addAll(padMenuIds);
        }
        Long roleId = role.getRoleId();
        roleMenuMapper.removeByRoleId(roleId);

        if (ids.size()>0){
            List<RoleMenuDO> rms = new ArrayList<>();
            for (Long menuId : ids) {
                RoleMenuDO rmDo = new RoleMenuDO();
                rmDo.setRoleId(roleId);
                rmDo.setMenuId(menuId);
                rms.add(rmDo);
            }
            if (rms.size() > 0) {
                roleMenuMapper.batchSave(rms);
            }
        }
        return r;
    }

    @Override
    public int batchremove(Long[] ids) {
        for (Long id : ids) {
            if (this.isUserUse(id)) {
                return -1;
            }
        }
        int r = roleMapper.batchRemove(ids);
        return r;
    }

    @Override
    public RoleDO getRByName(String name) {
        RoleDO roleDO = roleMapper.getRByName(name);
        return roleDO;
    }

    @Override
    public List<RoleDO> getRoleListByUserId(Long userId) {
        List<Long> userRoleDoList = userRoleMapper.listRoleId(userId);
        List<RoleDO> roleList = roleMapper.getByIds(userRoleDoList);
        return roleList;
    }

    @Transactional
    @Override
    public int insert(RoleDO role, Long[] ids) {
        int save = this.save(role);
        if (Objects.equals(role.getDataPermission(), Constant.CUSTOM_DATA)){
            Long roleId = role.getRoleId();
            if(ids.length>0){
                RoleDataDO roleDataDO;
                for (Long id : ids) {
                    roleDataDO = new RoleDataDO();
                    roleDataDO.setDeptId(id);
                    roleDataDO.setRoleId(roleId);
                    roleDataMapper.save(roleDataDO);
                }
            }
        }
        return save;
    }

    @Override
    public int edit(RoleDO role, Long[] ids) {
        int update = this.update(role);
        if (Objects.equals(role.getDataPermission(), Constant.CUSTOM_DATA)) {
            Long roleId = role.getRoleId();
            roleDataMapper.deleteByRoleId(roleId);
            if (ids.length > 0) {
                RoleDataDO roleDataDO;
                for (Long id : ids) {
                    roleDataDO = new RoleDataDO();
                    roleDataDO.setDeptId(id);
                    roleDataDO.setRoleId(roleId);
                    roleDataMapper.save(roleDataDO);
                }
            }
        }
        return update;
    }

}
