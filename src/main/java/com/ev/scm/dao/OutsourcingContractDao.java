package com.ev.scm.dao;

import com.ev.scm.domain.OutsourcingContractDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 委外合同
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
@Mapper
public interface OutsourcingContractDao {

	OutsourcingContractDO get(Long id);
	
	List<OutsourcingContractDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutsourcingContractDO outsourcingContract);
	
	int update(OutsourcingContractDO outsourcingContract);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    Map<String, Object> countForMap(Map<String, Object> map);

	Map<String, Object> getDetail(Long outsourcingContractId);

    int childCount(Long id);
}
