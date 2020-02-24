package com.ev.scm.dao;

import com.ev.scm.domain.InventoryPlanItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 盘点子表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-19 14:21:57
 */
@Mapper
public interface InventoryPlanItemDao {

	InventoryPlanItemDO get(Long id);
	
	List<InventoryPlanItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanItemDO checkBody);
	
	int update(InventoryPlanItemDO checkBody);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByPlanId(Long id);

	//生成盘盈盘亏单
	List<Map<String,Object>> getProfitLossMsg(Map<String, Object> map);


	//获取盘点商品信息  数量综合
	List<Map<String,Object>> getProMsgCount(Map<String, Object> map);

	List<Map<String, Object>> getMaterialAll(Map<String, Object> map);

    //此次盘点是否产生的盈亏
	int countOfWinLoss(Map<String, Object> map);

}
