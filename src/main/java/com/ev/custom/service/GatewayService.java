package com.ev.custom.service;

import com.ev.custom.domain.GatewayDO;

import java.util.List;
import java.util.Map;

/**
 * 网关信息
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:23:38
 */
public interface GatewayService {
	
	GatewayDO get(Long id);
	
	List<GatewayDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(GatewayDO gateway);
	
	int update(GatewayDO gateway);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int checkSave(String serialNo);

	List<Map<String,Object>> listForMap(Map<String, Object> query);

	/**
	 * @param id
	 * @return
	 */
	int logicRemove(Long id);

	/**
	 * @param ids
	 * @return
	 */
	int logicBatchRemove(Long[] ids);
}
