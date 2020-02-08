package com.ev.custom.dao;

import com.ev.custom.domain.PaymentformItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:32
 */
@Mapper
public interface PaymentformItemDao {

	List<Map<String,Object>> get(Long pid);
	
	List<PaymentformItemDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PaymentformItemDO paymentformItem);
	
	int update(PaymentformItemDO paymentformItem);
	
	int remove(Long pid);
	
	int batchRemove(Long[] pids);
}
