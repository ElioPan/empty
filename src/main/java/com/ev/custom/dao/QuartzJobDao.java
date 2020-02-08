package com.ev.custom.dao;

import com.ev.custom.domain.QuartzJobDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-15 09:56:32
 */
@Mapper
public interface QuartzJobDao {

	QuartzJobDO get(Long id);
	
	List<QuartzJobDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(QuartzJobDO quartzJob);
	
	int update(QuartzJobDO quartzJob);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
