package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.ProcessReportCheckDO;

import java.util.List;
import java.util.Map;

/**
 * 工序检验（主表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:11
 */
public interface ProcessReportCheckService {
	
	ProcessReportCheckDO get(Long id);
	
	List<ProcessReportCheckDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessReportCheckDO processReportCheck);
	
	int update(ProcessReportCheckDO processReportCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> checkHeadDetail(Map<String, Object> map);

	List<Map<String, Object>> checkBadyDetail(Map<String, Object> map);

	R saveAndChangeAndSbumit(ProcessReportCheckDO processReportCheckDO, String bodyDetail, Long[] itemIds,int sign);

	R batchRemoveCheckReport(Long[] reportCheckId);

	R getDetailOfCheck(Long reportCheckId);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int countDeleAboutCheck(Map<String, Object> map);

}
