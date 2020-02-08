package com.ev.system.activiti;

import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.RoleDO;
import com.ev.system.service.RoleService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomGroupEntityManager extends GroupEntityManager {
    @Autowired
    private RoleService roleService;

    @Override
    public List<Group> findGroupsByUser(String userId) {
        Long id = Long.valueOf(userId);
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<RoleDO> customRoles = roleService.getRoleListByUserId(id);
        List<Group> groups = new ArrayList<>();
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
}
