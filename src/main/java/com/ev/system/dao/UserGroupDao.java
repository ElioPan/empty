package com.ev.system.dao;

import com.ev.system.domain.UserGroupDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与用户组对应关系
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2018-06-14 14:51:48
 */
@Mapper
public interface UserGroupDao {

	UserGroupDO get(Long id);
	
	List<UserGroupDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserGroupDO userGroup);
	
	int update(UserGroupDO userGroup);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	void removeByUserId(Long userId);

	void batchSave(List<UserGroupDO> userGroupList);

	List<Long> listGroupId(Long id);

	void batchRemoveByUserId(Long[] userIds);
}
