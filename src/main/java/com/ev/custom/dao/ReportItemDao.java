package com.ev.custom.dao;

import com.ev.custom.domain.ReportItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 报告明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
@Mapper
public interface ReportItemDao {

	ReportItemDO get(Long id);
	
	List<ReportItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReportItemDO reportItem);
	
	int update(ReportItemDO reportItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	int removeByWeekId(Long weekId);
}
