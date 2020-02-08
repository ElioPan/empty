package com.ev.custom.dao;

import com.ev.custom.domain.ReceiptDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:36
 */
@Mapper
public interface ReceiptDao {

	Map<String,Object> get(Long id);
	
	List<ReceiptDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ReceiptDO receipt);
	
	int update(ReceiptDO receipt);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int audit(Long id);
	
	int reverseAudit(Long id);
	
	List<Map<String,Object>> listApi(Map<String,Object> map);
	
	int countApi(Map<String,Object> map);
}
