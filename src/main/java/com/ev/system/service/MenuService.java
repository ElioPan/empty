package com.ev.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ev.common.domain.MenuTree;
import org.springframework.stereotype.Service;

import com.ev.common.domain.Tree;
import com.ev.system.domain.MenuDO;

@Service
public interface MenuService {
	Tree<MenuDO> getSysMenuTree(Long id,String terminal);

	List<MenuTree<MenuDO>> listMenuTree(Long id,String terminal);

	Tree<MenuDO> getTree(Map<String,Object> params);

	Tree<MenuDO> getTree(Long id,Map<String,Object> params);

	List<MenuDO> list(Map<String, Object> params);

	int remove(Long id);

	int save(MenuDO menu);

	int update(MenuDO menu);

	MenuDO get(Long id);

	Set<String> listPerms(Long userId);

	List<MenuDO> listByIds(Long[] ids);
}
