package com.ev.scm.service;

import com.ev.scm.domain.OtherReceivablesItemDO;

import java.util.List;
import java.util.Map;

/**
 * 其他应收应付明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 16:27:57
 */

public interface OtherReceivablesItemService {
	
	OtherReceivablesItemDO get(Long id);
	
	List<OtherReceivablesItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OtherReceivablesItemDO otherReceivablesItem);
	
	int update(OtherReceivablesItemDO otherReceivablesItem);

	int updateAll(OtherReceivablesItemDO otherReceivablesItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByHeadId(Long[] ids);

	List<Map<String, Object>> getBodyDetail(Long id);

	Map<String, Object> totailAmount(Long id);

	List<Map<String, Object>> getDetailOfIntroduce(Map<String, Object> map);

	Map<String, Object> totailAmountOfIntroduce(Map<String, Object> map);

	List<OtherReceivablesItemDO> otherNoReceiptAmount(Map<String, Object> map);

}
