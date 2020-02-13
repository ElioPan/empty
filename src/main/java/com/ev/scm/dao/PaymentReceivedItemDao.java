package com.ev.scm.dao;

import com.ev.scm.domain.PaymentReceivedItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 收款/付款明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:17
 */
@Mapper
public interface PaymentReceivedItemDao {

	PaymentReceivedItemDO get(Long id);
	
	List<PaymentReceivedItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentReceivedItemDO paymentReceivedItem);
	
	int update(PaymentReceivedItemDO paymentReceivedItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByReceivedId(Long[] ids);

	List<Map<String, Object>> detailOfitem(Map<String, Object> map);

	Map<String, Object> totallAmount(Map<String, Object> map);

	int whetherTheReference(Map<String, Object> map);

}
