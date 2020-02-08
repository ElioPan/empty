package com.ev.custom.dao;

import com.ev.custom.domain.LeaveApplyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 请假管理
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 15:18:26
 */
@Mapper
@Repository
public interface LeaveApplyDao {

	LeaveApplyDO get(Long id);
	
	List<LeaveApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	int save(LeaveApplyDO leaveApply);
	
	int update(LeaveApplyDO leaveApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);


	Map<String, Object> getOfDetail(Long id);

}
