package com.ev.custom.dao;

import com.ev.custom.domain.ReimApplyItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 报销申请明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-12 15:52:33
 */
@Mapper
public interface ReimApplyItemDao {

	ReimApplyItemDO get(Long id);
	
	List<ReimApplyItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReimApplyItemDO reimApplyItem);
	
	int update(ReimApplyItemDO reimApplyItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByReimId(Long reimApplayId);
}
