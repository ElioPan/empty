package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.BanktransferslipDO;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-08 13:11:30
 */
public interface BanktransferslipService {
	
	BanktransferslipDO get(Long id);
	
	List<BanktransferslipDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BanktransferslipDO banktransferslip);
	
	int update(BanktransferslipDO banktransferslip);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addBankTransferSlip(BanktransferslipDO banktransferslip,String bodyItem);
	
	R removeBankTransferSlip(Long id);
	
	R updateBankTransferSlip(BanktransferslipDO banktransferslip,String bodyItem,Long[] deleteId);
	
	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R listApi(Map<String,Object> map);
	
	R getdetail(Long id);
}
