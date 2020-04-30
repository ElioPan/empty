package com.ev.hr.service;

import com.ev.hr.domain.PostInfoDO;

import java.util.List;
import java.util.Map;

/**
 * 岗位信息
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:58
 */
public interface PostInfoService {
	
	PostInfoDO get(Long id);
	
	List<PostInfoDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PostInfoDO postInfo);
	
	int update(PostInfoDO postInfo);

	int updateAll(PostInfoDO postInfo);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
