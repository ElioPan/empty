package com.ev.mes.dao;

import com.ev.mes.domain.ProcessReportCheckDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工序检验（主表）
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:11
 */
@Mapper
public interface ProcessReportCheckDao {

	ProcessReportCheckDO get(Long id);
	
	List<ProcessReportCheckDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessReportCheckDO processReportCheck);
	
	int update(ProcessReportCheckDO processReportCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> checkHeadDetail(Map<String, Object> map);

	List<Map<String, Object>> checkBadyDetail(Map<String, Object> map);

	int canOrNoDelet(Long id);

	Map<String, Object> getdetailOfCheck(Long id);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int countDeleAboutCheck(Map<String, Object> map);


}
