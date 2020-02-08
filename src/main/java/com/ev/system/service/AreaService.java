package com.ev.system.service;

import com.ev.system.domain.AreaDO;

import java.util.List;
import java.util.Map;

/**
 * 2018年最新行政区划数据库-旗舰版
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:08:25
 */
public interface AreaService {
	
	AreaDO get(Integer cityId);
	
	List<AreaDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AreaDO area);
	
	int update(AreaDO area);
	
	int remove(Integer cityId);
	
	int batchRemove(Integer[] cityIds);
	
	List<AreaDO> listByParentId(Integer cityId);
}
