package com.ev.system.service.impl;

import com.ev.common.domain.MenuTree;
import com.ev.common.domain.Tree;
import com.ev.framework.utils.BuildMenuTree;
import com.ev.framework.utils.BuildTree;
import com.ev.system.dao.MenuDao;
import com.ev.system.dao.RoleMenuDao;
import com.ev.system.domain.MenuDO;
import com.ev.system.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {
	@Autowired
	MenuDao menuMapper;
	@Autowired
	RoleMenuDao roleMenuMapper;

	/**
	 * @param
	 * @return 树形菜单
	 */
	@Cacheable
	@Override
	public Tree<MenuDO> getSysMenuTree(Long id,String terminal) {
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",id);
		params.put("terminal",terminal);
		List<MenuDO> menuDOs = menuMapper.listMenuByUserId(params);
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId()==null?null:sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> attributes = new HashMap<>(16);
			attributes.put("url", sysMenuDO.getUrl());
			attributes.put("icon", sysMenuDO.getIcon());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public List<MenuDO> list(Map<String, Object> params) {
		List<MenuDO> menus = menuMapper.list(params);
		return menus;
	}

	@Transactional(readOnly = false,rollbackFor = Exception.class)
	@Override
	public int remove(Long id) {
		int result = menuMapper.remove(id);
		return result;
	}
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	@Override
	public int save(MenuDO menu) {
		int r = menuMapper.save(menu);
		return r;
	}

	@Transactional(readOnly = false,rollbackFor = Exception.class)
	@Override
	public int update(MenuDO menu) {
		int r = menuMapper.update(menu);
		return r;
	}

	@Override
	public MenuDO get(Long id) {
		MenuDO menuDO = menuMapper.get(id);
		return menuDO;
	}

	@Override
	public Tree<MenuDO> getTree(Map<String,Object> params) {
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.list(params);
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId()==null?null:sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String,Object> attributes = new HashMap<String,Object>();
			attributes.put("icon",sysMenuDO.getIcon());
			attributes.put("routePath",sysMenuDO.getRoutePath());
			attributes.put("perms",sysMenuDO.getPerms());
			attributes.put("type",sysMenuDO.getType());
			attributes.put("orderNumber",sysMenuDO.getOrderNum());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public Tree<MenuDO> getTree(Long id, Map<String,Object> params) {
		// 根据roleId查询权限
		List<MenuDO> menus = menuMapper.list(params);
		params.put("id",id);
		List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(params);
		List<Long> temp = menuIds;
		for (MenuDO menu : menus) {
			if (temp.contains(menu.getParentId())) {
				menuIds.remove(menu.getParentId());
			}
		}
		List<Tree<MenuDO>> trees = new ArrayList<Tree<MenuDO>>();
		List<MenuDO> menuDOs = menuMapper.list(params);
		for (MenuDO sysMenuDO : menuDOs) {
			Tree<MenuDO> tree = new Tree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId()==null?null:sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> state = new HashMap<>(16);
			Long menuId = sysMenuDO.getMenuId();
			if (menuIds.contains(menuId)) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			Map<String,Object> attributes = new HashMap<String,Object>();
			attributes.put("icon",sysMenuDO.getIcon());
			attributes.put("iconUrl",sysMenuDO.getIconUrl());
			attributes.put("routePath",sysMenuDO.getRoutePath());
			attributes.put("perms",sysMenuDO.getPerms());
			attributes.put("type",sysMenuDO.getType());
			attributes.put("terminal",sysMenuDO.getTerminal());
			attributes.put("orderNumber",sysMenuDO.getOrderNum());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<MenuDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public Set<String> listPerms(Long userId) {
		Map<String,Object> params = new HashMap<>();
		params.put("id",userId);
		List<String> perms = menuMapper.listUserPerms(params);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	@Override
	public List<MenuDO> listByIds(Long[] ids) {
		return menuMapper.listByIds(ids);
	}

	@Override
	public List<MenuTree<MenuDO>> listMenuTree(Long id,String terminal) {
		List<MenuTree<MenuDO>> trees = new ArrayList<MenuTree<MenuDO>>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id",id);
		params.put("terminal",terminal);
		List<MenuDO> menuDOs = menuMapper.listMenuByUserId(params);
		for (MenuDO sysMenuDO : menuDOs) {
			MenuTree<MenuDO> tree = new MenuTree<MenuDO>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId()==null?null:sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			tree.setIcon(sysMenuDO.getIcon());
			tree.setRoutePath(sysMenuDO.getRoutePath());
			tree.setUrl(sysMenuDO.getUrl());
			tree.setOrderNumber(sysMenuDO.getOrderNum()==null?String.valueOf(Integer.MAX_VALUE):sysMenuDO.getOrderNum().toString());
			tree.setType(sysMenuDO.getType().toString());
			tree.setTerminal(sysMenuDO.getTerminal()==null?null:sysMenuDO.getTerminal().toString());
			tree.setIconUrl(sysMenuDO.getIconUrl()==null?null:sysMenuDO.getIconUrl().toString());
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		List<MenuTree<MenuDO>> list = BuildMenuTree.buildList(trees, "0");
		return list;
	}

}
