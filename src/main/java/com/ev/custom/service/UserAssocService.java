package com.ev.custom.service;

import com.ev.custom.domain.UserAssocDO;

import java.util.List;
import java.util.Map;

/**
 * 用户关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 14:58:02
 */
public interface UserAssocService {
	
	UserAssocDO get(Long id);
	
	List<Map<String, Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserAssocDO userAssoc);
	
	int update(UserAssocDO userAssoc);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);


	int removeByAssocIdAndUserId(Map<String, Object> map);

	int batchRemoveByAssocIdAadType(Map<String, Object> map);

	int changeOfUserAssocSign(Long userId,Long assocId,String assocType);

}
