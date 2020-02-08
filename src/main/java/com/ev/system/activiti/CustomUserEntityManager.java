package com.ev.system.activiti;

import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.RoleDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.RoleService;
import com.ev.system.service.UserService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserEntityManager extends UserEntityManager {
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Override
    public List<Group> findGroupsByUser(String userId) {
        Long id = Long.valueOf(userId);
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<RoleDO> customRoles = roleService.getRoleListByUserId(id);
        List<Group> groups = new ArrayList<Group>();
        GroupEntity groupEntity = null;
        for (RoleDO customRole : customRoles) {
            groupEntity = new GroupEntity();
            groupEntity.setRevision(1);
            groupEntity.setType("assignment");
            groupEntity.setName(customRole.getRoleName());
            groups.add(groupEntity);
        }
        return groups;
    }

    @Override
    public User findUserById(String userId){
        User userEntity=new UserEntity();
        UserDO rocIdUser=userService.get(Long.parseLong(userId));
        //将自定义的user转化为activiti的类
        if(rocIdUser != null){
            userEntity.setId(userId);
        }
        //返回activiti的实体类
        return userEntity;
    }
}
