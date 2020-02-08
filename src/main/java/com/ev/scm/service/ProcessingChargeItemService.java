package com.ev.scm.service;

import com.ev.scm.domain.ProcessingChargeItemDO;

import java.util.List;
import java.util.Map;

/**
 * 加工费用明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
public interface ProcessingChargeItemService {
	
	ProcessingChargeItemDO get(Long id);
	
	List<ProcessingChargeItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessingChargeItemDO processingChargeItem);
	
	int update(ProcessingChargeItemDO processingChargeItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
