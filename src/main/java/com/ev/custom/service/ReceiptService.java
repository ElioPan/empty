package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.ReceiptDO;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:36
 */
public interface ReceiptService {
	
	ReceiptDO get(Long id);
	
	List<ReceiptDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReceiptDO receipt);
	
	int update(ReceiptDO receipt);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addReceipt(ReceiptDO receiptDO,String bodyItem);
	
	R removeReceipt(Long id);
	
	R updateReceipt(ReceiptDO receiptDO,String bodyItem,Long[] deleteId);
	
	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R listApi(Map<String,Object> map);
	
	R getdetail(Long id);
}
