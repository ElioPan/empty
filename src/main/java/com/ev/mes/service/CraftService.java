package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.CraftDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 工艺路线
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:00
 */
public interface CraftService {
	
	CraftDO get(Long id);
	
	List<CraftDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CraftDO craft);
	
	int update(CraftDO craft);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveAndChangeCraft(CraftDO craftDO, String processAndProject,Long[] deletCraftItemIds,Long[] deletProcessCheckIds);

	R auditAndUnOfCraft(Long craftId,Long auditManIds,int sign);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	R getCraftOfDetail(Long craftId);

	R getProcessOfDetail(Long itemId);

	R deletOfBatch(Long[] craftId);

	List<Map<String, Object>>listBodyForMap(Map<String, Object> map);

	int countListBodyForMap(Map<String, Object> map);

    R importExcel(MultipartFile file);
}
