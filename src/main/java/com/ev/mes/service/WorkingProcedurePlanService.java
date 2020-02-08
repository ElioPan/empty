package com.ev.mes.service;

import java.util.List;
import java.util.Map;

import com.ev.framework.utils.R;
import com.ev.mes.domain.WorkingProcedurePlanDO;

/**
 * 工序计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 15:44:50
 */
public interface WorkingProcedurePlanService {

	WorkingProcedurePlanDO get(Long id);

	List<WorkingProcedurePlanDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WorkingProcedurePlanDO workingProcedurePlan);

	int update(WorkingProcedurePlanDO workingProcedurePlan);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * 保存工序计划
	 */
	R add(WorkingProcedurePlanDO planDO, String childArray, String signs);

	/**
	 * 获取工序计划详情
	 */
	Map<String, Object> getDetailInfo(Long id);

	/**
	 * 修改工序计划
	 */
	int edit(WorkingProcedurePlanDO planDO, String childArray, Long[] projectIds, Long[] checkProjectIds);

    Map<String, Object> getDetailInfo(Long headId, Long bodyId);

    /**
	 * 下达工序计划
	 */
	R issuedPlan(Long id);

	/**
	 * 反下达工序计划
	 */
	R reverseIssuedPlan(Long id);

	/**
	 * 挂起工序计划
	 */
	R putUpPlan(Long id);

	/**
	 * 反挂起工序计划
	 */
	R reversePutUpPlan(Long id);

	/**
	 * 结案工序计划
	 */
	R closeCasePlan(Long id);

	/**
	 * 反结案工序计划
	 */
	R reverseCloseCasePlan(Long id);

	/**
	 * 拆分工序计划
	 */
	R splitPlan(Long id, String splitArray);

	/**
	 * 合并工序计划
	 */
	R concatPlan(Long[] ids);

	/**
	 * 删除工序计划
	 */
	R delete(Long id);

	/**
	 * 删除工序计划
	 */
	R batchDelete(Long[] ids);

	/**
	 * 设置单据编号
	 */
	void setWorkOrderNo(WorkingProcedurePlanDO planDO);

	/**
	 * 删除表头数据并且删除子表数据
	 */
	void removeHeadAndBody(Long id);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	/**
	  *  获取需要合并工序计划的列表
	 */
	R getConcatPlanList(Long id);

	List<Map<String, Object>> dispatchItemList(Map<String, Object> param);

	int dispatchItemCount(Map<String, Object> param);
}
