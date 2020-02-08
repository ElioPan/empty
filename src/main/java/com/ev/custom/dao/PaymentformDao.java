package com.ev.custom.dao;

import com.ev.custom.domain.PaymentformDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:31
 */
@Mapper
public interface PaymentformDao {

	Map<String,Object> get(Long id);
	
	List<PaymentformDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PaymentformDO paymentform);
	
	int update(PaymentformDO paymentform);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int audit(Long id);
	
	int reverseAudit(Long id);
	
	List<Map<String,Object>> listApi(Map<String,Object> map);
	
	int countApi(Map<String,Object> map);
}
