package com.ev.scm.dao;

import com.ev.scm.domain.AllotItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 调拨单详情表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
@Mapper
public interface AllotItemDao {

	AllotItemDO get(Long id);
	
	List<AllotItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AllotItemDO allotItem);
	
	int update(AllotItemDO allotItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String, Object>> getDetail(Long id);

    int removeByAllotId(Long id);

    int batchRemoveByAllotId(Long[] ids);

    int batchInsert(List<AllotItemDO> allotDetailDOs);

    int batchUpdate(List<AllotItemDO> allotDetailDOs);
}
