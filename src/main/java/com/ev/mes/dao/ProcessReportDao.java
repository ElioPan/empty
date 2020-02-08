package com.ev.mes.dao;

import com.ev.mes.domain.ProcessReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工序报工单
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:14:53
 */
@Mapper
public interface ProcessReportDao {

	ProcessReportDO get(Long id);
	
	List<ProcessReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessReportDO processReport);
	
	int update(ProcessReportDO processReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int countOfDisItemAboutWorkDetail(Map<String, Object> map);

	Map<String, Object> reportDetailById(Map<String, Object> map);

	int canDeletOfReport(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int	countForMap(Map<String, Object> map);

	Map<String, Object> getMsgForSynthesize(Map<String, Object> map);
}
