package com.ev.custom.dao;

import com.ev.custom.domain.PaymentTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 收支类型
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 01:29:58
 */
@Mapper
public interface PaymentTypeDao {

	PaymentTypeDO get(Long id);
	
	List<PaymentTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentTypeDO paymentType);
	
	int update(PaymentTypeDO paymentType);

	int updateAll(PaymentTypeDO paymentType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	List<Map<String, Object>> listOfMap(Map<String, Object> map);


}
