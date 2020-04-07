package com.ev.mes.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ev.framework.utils.R;
import com.ev.mes.domain.ProductionPlanDO;
import com.ev.mes.domain.WorkingProcedurePlanDO;

/**
 * 生产计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:26:39
 */
public interface ProductionPlanService {

	ProductionPlanDO get(Long id);

	List<ProductionPlanDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ProductionPlanDO productionPlan);

	int update(ProductionPlanDO productionPlan);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * 查询生产计划列表
	 */
	List<Map<String, Object>> listForMap(Map<String, Object> params);

	/**
	 * 查询生产计划列表返回的总条数以及计划生产数量的总量
	 */
	Map<String, Object> countForMap(Map<String, Object> params);

	/**
	 * 添加生产计划（单表保存）
	 */
	int add(ProductionPlanDO inspectionDO);

	/**
	 * 根据ID查询生产计划
	 */
	Map<String, Object> getDetailInfo(Long id);

	/**
	 * 修改生产计划（单表保存）
	 */
	int edit(ProductionPlanDO inspectionDO);

	/**
	 * 删除生产计划
	 */
	R delete(Long id);

	/**
	 * 批量删除生产计划
	 */
	R batchDelete(Long[] ids);

	/**
	 * 设置计划单号
	 */
	void setPlanNo(ProductionPlanDO inspectionDO);

	/**
	 * 下达生产计划
	 * 
	 * @see  #isTecRouteEmpty 检查工艺路线
	 * @see  #isBomEmpty 检查BOM
//	 * @see  #isInspectionEmpty 检查检验方案
	 */
	R issuedPlan(Long id);

	/**
	 * 检查工艺路线
	 */
	boolean isTecRouteEmpty(ProductionPlanDO inspectionDO);

	/**
	 * 检查BOM
	 */
	boolean isBomEmpty(ProductionPlanDO inspectionDO);

//	/**
//	 * 检查检验方案
//	 */
//	boolean isInspectionEmpty(ProductionPlanDO inspectionDO);

	/**
	 * 检查生产计划是否为计划状态
	 */
	boolean isPlan(ProductionPlanDO planDO);

	/**
	 * 反下达生产计划
	 */
	R reverseIssuedPlan(Long id);

	/**
	 * 挂起生产计划
	 */
	R putUpPlan(Long id);

	/**
	 * 反挂起生产计划
	 */
	R reversePutUpPlan(Long id);

	/**
	 * 结案生产计划
	 */
	R closeCasePlan(Long id);

	/**
	 * 反结案生产计划
	 */
	R reverseCloseCasePlan(Long id);

	/**
	 * 变更生产计划
	 */
	R alterationPlan(ProductionPlanDO planDO);

	/**
	 * 跟踪生产计划
	 */
	R followPlan(Long id);

    List<WorkingProcedurePlanDO> getWorkingProcedurePlanList(Long id, Map<String, Object> param);

    List<Map<String, Object>> listDialogMap(Map<String, Object> params);

    BigDecimal getCountBySource(Map<String, Object> sourceParam);
}
