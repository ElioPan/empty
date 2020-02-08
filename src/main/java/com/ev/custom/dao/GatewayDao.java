package com.ev.custom.dao;

import com.ev.custom.domain.GatewayDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 网关信息
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:23:38
 */
@Mapper
public interface GatewayDao {

	GatewayDO get(Long id);
	
	List<GatewayDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(GatewayDO gateway);
	
	int update(GatewayDO gateway);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> query);

}
