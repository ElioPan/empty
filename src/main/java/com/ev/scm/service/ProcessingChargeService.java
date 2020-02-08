package com.ev.scm.service;

import com.ev.scm.domain.ProcessingChargeDO;

import java.util.List;
import java.util.Map;

/**
 * 加工费用主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
public interface ProcessingChargeService {
	
	ProcessingChargeDO get(Long id);
	
	List<ProcessingChargeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessingChargeDO processingCharge);
	
	int update(ProcessingChargeDO processingCharge);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
