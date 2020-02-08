package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.MaterialsScrapDO;

import java.util.List;
import java.util.Map;

/**
 * 用料报废单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:28
 */
public interface MaterialsScrapService {
	
	MaterialsScrapDO get(Long id);
	
	List<MaterialsScrapDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterialsScrapDO materialsScrap);
	
	int update(MaterialsScrapDO materialsScrap);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveChangeScrap(String scrapDetail, String scrapItemDetail, Long[] ids );

	R deletScrap(Long[] scrapIds);

	R getDetailOfScrap(Long scrapId);

	R reversAuditScrap(Long scrapId);

	R submit(Long scrapId,Long auditId);
}
