package com.ev.scm.dao;

import com.ev.scm.domain.SalescontractDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-10 10:18:09
 */
@Mapper
public interface SalescontractDao {

    SalescontractDO get(Long id);
	
	List<SalescontractDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

//	int countApi(Map<String, Object> map);

	int save(SalescontractDO salescontract);

	int update(SalescontractDO salescontract);

	int remove(Long id);

	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    Map<String, Object> countForMap(Map<String, Object> map);

    Map<String, Object> getDetail(Long salesContractId);

}
