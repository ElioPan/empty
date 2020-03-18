package com.ev.custom.service;

import com.ev.custom.domain.PaymentTypeDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 收支类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 01:29:58
 */
public interface PaymentTypeService {
	
	PaymentTypeDO get(Long id);
	
	List<PaymentTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentTypeDO paymentType);
	
	int update(PaymentTypeDO paymentType);

	int updateAll(PaymentTypeDO paymentType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R disposeAddAndChage(PaymentTypeDO paymentTypeDO );

	List<Map<String, Object>> listOfMap(Map<String, Object> map);

}
