package com.ev.scm.dao;

import com.ev.scm.domain.BankTransferDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 银行转账单主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:17:25
 */
@Mapper
public interface BankTransferDao {

	BankTransferDO get(Long id);
	
	List<BankTransferDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BankTransferDO bankTransfer);
	
	int update(BankTransferDO bankTransfer);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);
}
