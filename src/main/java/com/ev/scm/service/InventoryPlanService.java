package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.InventoryPlanDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 盘点主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-19 14:22:10
 */
public interface InventoryPlanService {
	
	InventoryPlanDO get(Long id);
	
	List<InventoryPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanDO checkHead);
	
	int update(InventoryPlanDO checkHead);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listByDates(Map<String, Object> map);

	List<Map<String, Object>> getProMsgByHeadId(Map<String, Object> map);

	int countOfListByDates(Map<String, Object> map);

	int countOfStatus(Map<String, Object> map);

	R disposePlanIsOver (Long planId );

	R getMaterielCount(Long warehouse, String syntheticData);

	R addInventoryPlan( InventoryPlanDO checkHeadDO,String checkBodys,Long[] deleItemIds);

	R deletBatchPlanNow(Long id);

	R doInventoryPlan(Long planId);

	R savePlanDetail(InventoryPlanDO checkHeadDO,String checkBodys);

	R buildWinStock(Long id );

	R buildLossStock(Long id );

	R disposeCheckByQrId(Long planId,String  qrMsg, Long qrId);

	R disposePhoneCheckResuls(Long planId,String checkBodys);

	R disposeImportExcel(MultipartFile file);

}
