package com.ev.hr.dao;

import com.ev.hr.domain.PostInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 岗位信息
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:58
 */
@Mapper
public interface PostInfoDao {

	PostInfoDO get(Long id);
	
	List<PostInfoDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PostInfoDO postInfo);
	
	int update(PostInfoDO postInfo);

	int updateAll(PostInfoDO postInfo);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchUpdate(List<PostInfoDO> param);
}
