package com.ev.scm.dao;

import com.ev.scm.domain.BankTransferItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 银行转账单主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:17:37
 */
@Mapper
public interface BankTransferItemDao {

	BankTransferItemDO get(Long id);
	
	List<BankTransferItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BankTransferItemDO bankTransferItem);
	
	int update(BankTransferItemDO bankTransferItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByTransferId(Long[] ids);

	List<Map<String, Object>> detailOfItem(Map<String, Object> map);

	int totalAmount(Map<String, Object> map);


}
