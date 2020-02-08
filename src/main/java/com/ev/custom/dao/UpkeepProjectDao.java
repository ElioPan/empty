package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepProjectDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 保养项目表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-12 16:53:16
 */
@Mapper
@Repository
public interface UpkeepProjectDao {

	UpkeepProjectDO get(Long id);
	
	List<UpkeepProjectDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UpkeepProjectDO upkeepProject);
	
	int update(UpkeepProjectDO upkeepProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<UpkeepProjectDO> objectList();

    List<UpkeepProjectDO> getByProjectIds(Long[] projectIds);

	List<Map<String,Object>> findListsPro(Map<String,Object> map);

	int countOfList(Map<String,Object> map);
}
