package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.WorkingProcedurePlanDO;
import com.ev.mes.service.WorkingProcedureDetailService;
import com.ev.mes.service.WorkingProcedurePlanService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 工序计划管理
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-11-21 09:51:41
 */

@Api(value = "/", tags = "工序计划API")
@RestController
public class WorkingProcedurePlanApiController {
	@Autowired
	private WorkingProcedurePlanService planService;
	@Autowired
	private WorkingProcedureDetailService detailService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
	
	/**
	 * 工序计划列表(上表)
	 * 说明:
	 * 上表选中下表动态显示工序明细数据，工序计划单号链接详情查看页面
	 * @see #childList(int, int, Long)
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/workingProcedurePlan/list", method = RequestMethod.POST, apiTitle = "工序计划列表")
	@ApiOperation("工序计划列表(上表)")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "产品名称") @RequestParam(value = "proName", defaultValue = "", required = false) String proName,
			@ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
			@ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
			@ApiParam(value = "状态ID") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
	) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.planService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }

		params.put("proName", proName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("deptId", deptId);
		params.put("status", status);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = planService.listForMap(params);
		int total = planService.countForMap(params);
		if (data.size() > 0) {
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
		}
		return R.ok(results);
	}
	
	/**
	 * 工序计划列表(下表，通过上表ID获取)
	 * 说明:
	 * 上表选中下表动态显示工序明细数据，工序计划单号链接详情查看页面
	 *
	 * @date 2019-11-29
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/workingProcedurePlan/childList", method = RequestMethod.POST, apiTitle = "工序计划列表")
	@ApiOperation("工序计划列表（明细）(下表)")
	public R childList(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "工序计划ID", required = true) @RequestParam(value = "planId", defaultValue = "") Long planId
			
	) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(10);
		
		params.put("planId", planId);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = detailService.listForMap(params);
		Map<String, Object> count = detailService.countForMap(params);
		results.put("count", count);

		int total = Integer.parseInt(count.getOrDefault("total",0).toString());
		if (data.size() > 0) {
			List<Map<String, Object>> datas= data.stream().sorted((v1,v2)->v1.get("serialNumber").toString().compareTo(v2.get("serialNumber").toString())).collect(Collectors.toList());
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(datas);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
		}
		return R.ok(results);
	}

	
	/**
	 * 保存工序计划信息(不通过生产计划)
	 * 注:如果需要检验，保存配置的检验项目
	 * 
	 * @param planDO 工序计划主表
	 * @param childArray 工序计划子表
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/save", method = RequestMethod.POST, apiTitle = "保存工序计划信息")
	@ApiOperation("保存工序计划信息(不通过生产计划)")
	public R save(WorkingProcedurePlanDO planDO, @ApiParam(value = "工序项目数组例："+
			"[\r\n" + 
			"    {\r\n" +
			"        \"processId\":1,\r\n" +
			"        \"serialNumber\":1,\r\n" +
            "        \"processType\":239,\r\n" +
            "        \"demand\":\"工艺要求\",\r\n" +
			"        \"deptId\":22,\r\n" + 
			"        \"operator\":10,\r\n" + 
			"        \"isExamine\":1,\r\n" + 
			"        \"isOutsource\":1,\r\n" + 
			"        \"isCollect\":1,\r\n" + 
			"        \"deviceId\":51,\r\n" + 
			"        \"isAuto\":1,\r\n" + 
			"        \"planCount\":1000,\r\n" + 
			"        \"planStartTime\":\"2018-12-27 00:00:00\",\r\n" + 
			"        \"planEndTime\":\"2018-12-27 00:00:00\",\r\n" + 
			"        \"totalHour\":22.5,\r\n" + 
			"        \"manHour\":1.2,\r\n" +
            "        \"standard\":1.2,\r\n" +
            "        \"labourPrice\":2.5,\r\n" +
			"        \"pro\":[\r\n" + 
			"            {\r\n" + 
			"                \"proId\":1,\r\n" +
            "                \"whetherCheck\":0,\r\n" +
            "                \"remark\":\"这里是备注\"\r\n" +
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"proId\":2,\r\n" +
            "                \"whetherCheck\":0,\r\n" +
            "                \"remark\":\"这里是备注\"\r\n" +
			"            }\r\n" + 
			"        ]\r\n" + 
			"    }\r\n" + 
			"]"
			, required = true) @RequestParam(value = "childArray", defaultValue = "") String childArray,
                  @ApiParam(value = "返工返修标记") @RequestParam(value = "signs", defaultValue = "",required = false) String signs) {
		return planService.add(planDO, childArray,signs);
	}

	/**
	 * 查看工序计划详情
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/workingProcedurePlan/detail", method = RequestMethod.POST, apiTitle = "查看工序计划信息")
	@ApiOperation("查看工序计划信息")
	public R detail(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(planService.getDetailInfo(id));
	}

	/**
	 * 修改工序计划
	 * 限制条件
	 * 1.非计划状态不允许修改
	 * 
	 * @param planDO 需要修改的工序计划表头
	 * @param childArray 需要修改的工序列表
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/update", method = RequestMethod.POST, apiTitle = "修改工序计划")
	@ApiOperation("修改工序计划")
	public R update(WorkingProcedurePlanDO planDO, @ApiParam(value = "工序项目数组例："+
			"[\r\n" + 
			"    {\r\n" + 
			"        \"id\":1,\r\n" +
			"        \"serialNumber\":1,\r\n" +
			"        \"processId\":1,\r\n" +
            "        \"processType\":239,\r\n" +
			"        \"demand\":\"工艺要求\",\r\n" + 
			"        \"deptId\":22,\r\n" + 
			"        \"operator\":10,\r\n" + 
			"        \"isExamine\":1,\r\n" + 
			"        \"planCount\":10000,\r\n" + 
			"        \"isOutsource\":1,\r\n" + 
			"        \"isCollect\":1,\r\n" + 
			"        \"deviceId\":51,\r\n" + 
			"        \"isAuto\":1,\r\n" + 
			"        \"planStartTime\":\"2018-12-27 00:00:00\",\r\n" + 
			"        \"planEndTime\":\"2018-12-27 00:00:00\",\r\n" + 
			"        \"totalHour\":22.5,\r\n" + 
			"        \"manHour\":1.2,\r\n" +
            "        \"standard\":1.2,\r\n" +
			"        \"labourPrice\":2.5,\r\n" + 
			"        \"pro\":[\r\n" + 
			"            {\r\n" + 
			"                \"id\":1,\r\n" + 
			"                \"proId\":1,\r\n" + 
			"                \"whetherCheck\":0,\r\n" +
            "                \"remark\":\"这里是备注\"\r\n" +
            "            },\r\n" +
			"            {\r\n" + 
			"                \"id\":2,\r\n" +
            "                \"whetherCheck\":0,\r\n" +
            "                \"remark\":\"这里是备注\"\r\n" +
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"proId\":3,\r\n" +
            "                \"whetherCheck\":0,\r\n" +
            "                \"remark\":\"这里是备注\"\r\n" +
			"            }\r\n" + 
			"        ]\r\n" + 
			"    }\r\n" + 
			"]\r\n" + 
			""
			, required = true) @RequestParam(value = "childArray", defaultValue = "") String childArray,
			@ApiParam(value = "删除的工序项目ID数组") @RequestParam(value = "projectIds", defaultValue = "", required = false) Long[] projectIds,
			@ApiParam(value = "删除的检验项目ID数组") @RequestParam(value = "checkProjectIds", defaultValue = "", required = false) Long[] checkProjectIds
			) {
		WorkingProcedurePlanDO procedurePlanDO = planService.get(planDO.getId());
		if (!Objects.equals(ConstantForMES.PLAN, procedurePlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("receipt.plan.update",null));
		}
		if (planService.edit(planDO, childArray,projectIds,checkProjectIds) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 下达工序计划
	 * 限制
	 * 1.只有计划状态才能够下达工序计划
	 * 
	 * TODO 自动生成派工单（没有配置需要派的人员信息） 暂无此功能 
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/issuedPlan", method = RequestMethod.POST, apiTitle = "下达工序计划")
	@ApiOperation("下达工序计划")
	public R issuedPlan(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.issuedPlan(id);
	}

	/**
	 * 反下达工序计划
	 * 限制
	 * 1.只有下达状态才能反下达
	 * 2.检查是否存在已开工或结案的派工单存在，如果有则不能反下达。如果没有开工或结案的派工单存在，则将工序计划单状态修改为计划
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 * @version 1  
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/reverseIssuedPlan", method = RequestMethod.POST, apiTitle = "反下达工序计划")
	@ApiOperation("反下达工序计划")
	public R reverseIssuedPlan(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.reverseIssuedPlan(id);
	}

	/**
	 * 挂起工序计划
	 * 1.检查对应的工单状态，将状态则修改为挂起
	 * 2.修改工序计划状态为挂起
	 * 3.修改工序状态为挂起
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 * @version 1  
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/putUpPlan", method = RequestMethod.POST, apiTitle = "挂起工序计划")
	@ApiOperation("挂起工序计划")
	public R putUpPlan(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.putUpPlan(id);
	}

	/**
	  * 反挂起工序计划
	 * 1.修改工序计划状态为下达
	 * 2.修改工序状态为派工
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/reversePutUpPlan", method = RequestMethod.POST, apiTitle = "反挂起工序计划")
	@ApiOperation("反挂起工序计划")
	public R reversePutUpPlan(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.reversePutUpPlan(id);
	}

	/**
	  * 结案工序计划
	 * 1.检查所有的派工单状态 若有工单未结案 显示相应的派工单列表，显示内容：派工单编号，工序代码，工序名称，开工时间，派工数量
	 * 2.工单全部为结案状态 则将工序计划明细列表数据修改为结案状态，将工序计划改为结案状态
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/closeCase", method = RequestMethod.POST, apiTitle = "结案工序计划")
	@ApiOperation("结案工序计划")
	public R closeCase(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.closeCasePlan(id);
	}

	/**
	  * 反结案工序计划
	  * 限制：结案下的单据才能反结案
	 * 1.将工序计划明细列表数据修改为派工状态，将工序计划改为下达状态
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/reverseCloseCase", method = RequestMethod.POST, apiTitle = "反结案工序计划")
	@ApiOperation("反结案工序计划")
	public R reverseCloseCase(
			@ApiParam(value = "工序计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.reverseCloseCasePlan(id);
	}

	/**
	  * 拆分工序计划
	 * 
	 * 1.结案状态不能拆分
	  * 每道工序的拆分数量不能大于源单计划生产数量，表头和表体信息默认锁定禁止修改，
	  * 选中工序行激活拆分数量列，允许录入拆分数量，其他属性修改只能在被拆分的子单上进行，
	  * 同时将源单对应行的计划生产数量修改为拆分前的计划生产数量－拆分数量，
	 *
	 * @date 2019-11-30 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/splitPlan", method = RequestMethod.POST, apiTitle = "拆分工序计划")
	@ApiOperation("拆分工序计划")
	public R splitPlan(@ApiParam(value = "拆分的母工序计划Id", required = true) @RequestParam(value = "oldId", defaultValue = "") Long oldId,
			@ApiParam(value = "工序项目数组例："+ 
					"[\r\n" + 
					"    {\r\n" + 
					"        \"id\":1,\r\n" + 
					"        \"splitCount\":1000\r\n" + 
					"    },\r\n" + 
					"    {\r\n" + 
					"        \"id\":2,\r\n" + 
					"        \"splitCount\":1000\r\n" + 
					"    },\r\n" + 
					"    {\r\n" + 
					"        \"id\":3,\r\n" + 
					"        \"splitCount\":1000\r\n" + 
					"    }\r\n" + 
					"]"
					, required = true) @RequestParam(value = "splitArray", defaultValue = "") String splitArray) {
		return planService.splitPlan(oldId, splitArray);
	}

	/**
	 * 
     * 获取需要合并工序计划的列表
     * 若工序计划已下达不能获取合并列表
	 *
	 * @date 2019-12-02 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/concatPlanList", method = RequestMethod.POST, apiTitle = "获取需要合并工序计划的列表")
	@ApiOperation("获取需要合并工序计划的列表")
	public R concatPlanList(@ApiParam(value = "id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.getConcatPlanList(id);
	}
	
	/**
	  * 合并工序计划
	  * 还原：只能对拆分过的单据进行还原操作，还原的提前是被拆分的母单和子单均没有下游关联的单据存在。 
	  * 对查出的可还原的计划进行还原
	 * @date 2019-12-02
	 * @author gumingjie
	 *
	 * 查询优化
	 * @date 2019-12-04 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/concatPlan", method = RequestMethod.POST, apiTitle = "合并工序计划")
	@ApiOperation("合并工序计划")
	public R concatPlan(@ApiParam(value = "需要合并的工序计划主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		return planService.concatPlan(ids);
	}

	/**
	 * 跟踪工序计划
	 * 关联的派工单列表
	 * 完工数量根据关联的报工单、工序检验单汇总计算
	 *
	 * @date 2019-12-02 
	 * @author gumingjie
	 * @version 1  
	 */
	@EvApiByToken(value = "/apis/workingProcedurePlan/followPlan", method = RequestMethod.POST, apiTitle = "跟踪工序计划")
	@ApiOperation("跟踪工序计划")
	public R followPlan(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "工序计划ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id
			) {
		// 查询列表数据
				Map<String, Object> params = Maps.newHashMapWithExpectedSize(10);
				params.put("planId", id);

				params.put("offset", (pageno - 1) * pagesize);
				params.put("limit", pagesize);
				Map<String, Object> results = Maps.newHashMap();
				List<Map<String, Object>> data = planService.dispatchItemList(params);
				int total = planService.dispatchItemCount(params);
				if (data.size() > 0) {
					DsResultResponse dsRet = new DsResultResponse();
					dsRet.setDatas(data);
					dsRet.setPageno(pageno);
					dsRet.setPagesize(pagesize);
					dsRet.setTotalRows(total);
					dsRet.setTotalPages((total + pagesize - 1) / pagesize);
					results.put("data", dsRet);
				}
				return R.ok(results);
	}

	/**
	 * 删除当前选中工序计划，已经下达或被关联引用提示不能删除，只能删除手工新建的无源单关联的工序计划
	 *1.有拆分操作的不允许删除
	 *2.开工的工序计划不允许删除
	 *
	 * @date 2019-12-02 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/remove", method = RequestMethod.POST, apiTitle = "删除工序计划信息")
	@ApiOperation("删除工序计划信息")
	public R remove(
			@ApiParam(value = "工序计划主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return planService.delete(id);
	}

	/**
	 * 批量删除
	 * 1.有拆分操作的不允许删除
	 * 2.开工的工序计划不允许删除
	 *
	 * @date 2019-12-02 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/workingProcedurePlan/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除工序计划信息")
	@ApiOperation("批量删除工序计划信息")
	public R remove(
			@ApiParam(value = "工序计划主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		return planService.batchDelete(ids);
	}

}
