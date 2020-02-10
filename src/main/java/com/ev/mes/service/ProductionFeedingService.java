package com.ev.mes.service;

import java.util.List;
import java.util.Map;

import com.ev.framework.utils.R;
import com.ev.mes.domain.ProductionFeedingDO;

/**
 * 生产投料单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:08:55
 */
public interface ProductionFeedingService {

	ProductionFeedingDO get(Long id);

	List<ProductionFeedingDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ProductionFeedingDO productionFeeding);

	int update(ProductionFeedingDO productionFeeding);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * 查看生产投料单列表数据
	 */
	List<Map<String, Object>> listForMap(Map<String, Object> params);

	/**
	 * 获得生产投料单的
	 */
	int countForMap(Map<String, Object> params);

	/**
	 * 是否审核状态
	 */
	boolean isNonAudit(Long id);

	/**
	 * 审核生产投料单
	 */
	R audit(Long id);

	/**
	 * 反审核生产投料单
	 */
	R reverseAudit(Long id);

	/**
	 * 新增生产投料单以及保存子表数据
	 */
	R add(ProductionFeedingDO feedingDO, String childArray);

	/**
	 * 获取生产投料单详情
	 */
	Map<String, Object> getDetailInfo(Long id);

	/**
	 * 修改生产投料单以及子表数据
	 */
	R edit(ProductionFeedingDO feedingDO, String childArray, Long[] ids);

	/**
	 * 删除生产投料单以及关联的子表数据
	 */
	int removeHeadAndBody(Long id);

	/**
	 * 批量删除生产投料单以及关联的子表数据
	 */
	void batchRemoveHeadAndBody(Long[] ids);

	/**
	 * 设置投料单号
	 */
	void setFeedingNo(ProductionFeedingDO feedingDO);

	/**
	 * 生产投料单是否被引用
	 */
	boolean isCited(Long id);

	ProductionFeedingDO getByOutsourcingContractItemId(Long id);
}
