package com.ev.scm.dao;

import com.ev.scm.domain.InventoryPlanFitlossDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 盘点盈亏单
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-24 14:50:47
 */
@Mapper
public interface InventoryPlanFitlossDao {

	InventoryPlanFitlossDO get(Integer id);
	
	List<InventoryPlanFitlossDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanFitlossDO checkProfitloss);
	
	int update(InventoryPlanFitlossDO checkProfitloss);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int countOfOtherByPY(Map<String, Object> map);

	//判断盈亏单是否已经生成
	int countOfYKCount(Map<String, Object> map);

	int countOfOutByPK(Map<String, Object> map);


}

