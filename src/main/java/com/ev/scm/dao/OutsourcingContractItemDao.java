package com.ev.scm.dao;

import com.ev.scm.domain.OutsourcingContractItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 委外合同明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
@Mapper
public interface OutsourcingContractItemDao {

	OutsourcingContractItemDO get(Long id);
	
	List<OutsourcingContractItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutsourcingContractItemDO outsourcingContractItem);
	
	int update(OutsourcingContractItemDO outsourcingContractItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    void batchRemoveByOutsourcingContractIds(Long[] outsourcingContractIds);

    List<Map<String, Object>> listByContract(Map<String, Object> params);

	Map<String, Object> countByContract(Map<String, Object> params);

    BigDecimal getCountBySource(Map<String, Object> map);

	int lineClosingNumber(Long id);
}
