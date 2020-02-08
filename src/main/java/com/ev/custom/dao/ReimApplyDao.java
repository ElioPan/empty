package com.ev.custom.dao;

import com.ev.custom.domain.ReimApplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 报销申请
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:37
 */
@Mapper
public interface ReimApplyDao {

	ReimApplyDO get(Long id);
	
	List<ReimApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(ReimApplyDO reimApply);
	
	int update(ReimApplyDO reimApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> getReimHead(Map<String, Object> map);

	List<Map<String, Object>> getReimItem (Map<String, Object> map);

	Map<String, Object> getSumOfCount(Map<String, Object> map);
}
