package com.ev.scm.service;

import com.ev.scm.domain.StockStartDO;

import java.util.List;
import java.util.Map;

/**
 * 库存系统设置
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-14 14:03:26
 */
public interface StockStartService {
	
	StockStartDO get(Integer id);
	
	List<StockStartDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockStartDO stockStart);
	
	int update(StockStartDO stockStart);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
