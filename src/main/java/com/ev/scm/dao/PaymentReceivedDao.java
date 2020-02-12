package com.ev.scm.dao;

import com.ev.scm.domain.PaymentReceivedDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 收款付款单主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:04
 */
@Mapper
public interface PaymentReceivedDao {

	PaymentReceivedDO get(Long id);
	
	List<PaymentReceivedDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentReceivedDO paymentReceived);
	
	int update(PaymentReceivedDO paymentReceived);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int updateAuditSign(PaymentReceivedDO paymentReceived);

	int canDeletOfCount(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	Map<String, Object> detailOfReceived(Map<String, Object> map);


}
