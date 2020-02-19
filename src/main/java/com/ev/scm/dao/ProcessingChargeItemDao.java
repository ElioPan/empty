package com.ev.scm.dao;

import com.ev.scm.domain.ProcessingChargeItemDO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 加工费用明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
@Mapper
public interface ProcessingChargeItemDao {

	ProcessingChargeItemDO get(Long id);
	
	List<ProcessingChargeItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessingChargeItemDO processingChargeItem);
	
	int update(ProcessingChargeItemDO processingChargeItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    void batchRemoveByChargeIds(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> param);

	Map<String, Object> countForMap(Map<String, Object> param);

	BigDecimal getCountBySource(Map<String, Object> map);
}
