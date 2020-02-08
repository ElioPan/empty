package com.ev.scm.service;

import com.ev.scm.domain.AllotItemDO;

import java.util.List;
import java.util.Map;

/**
 * 调拨单详情表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
public interface AllotItemService {
	
	AllotItemDO get(Long id);
	
	List<AllotItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AllotItemDO allotItem);
	
	int update(AllotItemDO allotItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    int batchUpdate(List<AllotItemDO> allotItemDOs);

    List<Map<String, Object>> getDetail(Long id);

    int batchInsert(List<AllotItemDO> allotItemDOs);

    void batchRemoveByAllotId(Long[] ids);
}
