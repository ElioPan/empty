package com.ev.scm.dao;

import com.ev.scm.domain.SalescontractPayDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-10 10:18:10
 */
@Mapper
public interface SalescontractPayDao {

    SalescontractPayDO get(Long pid);
	
	List<SalescontractPayDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);
	
	int save(SalescontractPayDO salescontractPay);
	
	int update(SalescontractPayDO salescontractPay);
	
	int remove(Long pid);
	
	int batchRemove(Long[] pids);

    void batchRemoveBySalesContractIds(Long[] salesContractIds);

//    List<SalescontractPayDO> listByContract(Map<String, Object> map);

    Map<String,Object> countByContract(Map<String, Object> map);

//	int removePid(Long pid);
}
