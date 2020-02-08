package com.ev.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ev.system.domain.RoleDO;

@Service
public interface RoleService {

	RoleDO get(Long id);

	List<RoleDO> list();

	int save(RoleDO role);

	int update(RoleDO role);

	int remove(Long id);

	List<RoleDO> list(Long userId);

	int batchremove(Long[] ids);

	RoleDO getRByName(String name);

	List<RoleDO> getRoleListByUserId(Long userId);

    int insert(RoleDO role, Long[] ids);

    int edit(RoleDO role, Long[] ids);

    boolean isUserUse(Long id);
}
