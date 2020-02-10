package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.BankTransferDO;

import java.util.List;
import java.util.Map;

/**
 * 银行转账单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:17:25
 */
public interface BankTransferService {
	
	BankTransferDO get(Long id);
	
	List<BankTransferDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BankTransferDO bankTransfer);
	
	int update(BankTransferDO bankTransfer);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	public R addBankTransfer(BankTransferDO bankTransferDO, String transferBodys, Long[] deleItemIds);

	R audit(Long id);

	R rollBackAudit(Long id);

	R removeTransfer(Long[] ids);
}
