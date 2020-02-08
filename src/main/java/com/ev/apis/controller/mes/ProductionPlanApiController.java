package com.ev.apis.controller.mes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.WorkingProcedurePlanDO;
import com.ev.mes.service.WorkingProcedurePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.mes.domain.ProductionPlanDO;
import com.ev.mes.service.ProductionPlanAlterationService;
import com.ev.mes.service.ProductionPlanService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 生产计划管理
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-11-21 09:51:41
 */

@Api(value = "/", tags = "生产计划API")
@RestController
public class ProductionPlanApiController {
	@Autowired
	private ProductionPlanService productionPlanService;
	@Autowired
	private ProductionPlanAlterationService productionPlanAlterationService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private WorkingProcedurePlanService workingProcedurePlanService;
	/**
	 * 
	 * 获取生产计划列表
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/productionPlan/list", method = RequestMethod.POST, apiTitle = "生产计划列表")
	@ApiOperation("生产计划列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
			@ApiParam(value = "产品名称") @RequestParam(value = "proName", defaultValue = "", required = false) String proName,
			@ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
			@ApiParam(value = "状态ID") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
			@ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "标识") @RequestParam(value = "mark", defaultValue = "", required = false) String mark,
            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
//			@ApiParam(value = "生产计划名称", required = false) @RequestParam(value = "productionPlanName", defaultValue = "", required = false) String productionPlanName,
//			@ApiParam(value = "物料代码", required = false) @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
	) {
        boolean isDiaLog = StringUtils.isNoneEmpty(mark)&&"dialog".equals(mark);
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.productionPlanService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
		params.put("clientId", clientId);
        params.put("clientName", clientName);
		params.put("proName", proName);
		params.put("deptId", deptId);
		params.put("status", status);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
//		params.put("serialNo", serialNo);
		params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        List<Map<String, Object>> data;
        // 产品检验单关联生产计划选单时，生产计划必须是设定必须检验且状态是下达状态的单据
        if (isDiaLog){
            params.put("status",ConstantForMES.ISSUED);
            params.put("isCheck",1);
            data = productionPlanService.listDialogMap(params);
        }else{
            data = productionPlanService.listForMap(params);
        }
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        Map<String, Object> count = productionPlanService.countForMap(params);
		results.put("count", count);
		int total = count.containsKey("total") ? Integer.parseInt(count.get("total").toString()) : 0;
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
	 * 获取生产计划的变更记录
	 * @date 2019-11-27
	 * @author gumingjie
	 * @version 1 
	 */
	@EvApiByToken(value = "/apis/productionPlan/alterationList", method = RequestMethod.POST, apiTitle = "变更记录列表")
	@ApiOperation("变更记录列表")
	public R alterationList(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "生产计划ID") @RequestParam(value = "productionPlanId", defaultValue = "", required = false) Long productionPlanId,
			@ApiParam(value = "生产计划单号") @RequestParam(value = "planNo", defaultValue = "", required = false) String planNo,
			@ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
			@ApiParam(value = "变更人ID") @RequestParam(value = "updateUser", defaultValue = "", required = false) Long updateUser,
			@ApiParam(value = "变更人姓名") @RequestParam(value = "updateUserName", defaultValue = "", required = false) String updateUserName
			) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(8);

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("productionPlanId", productionPlanId);
		params.put("createBy", updateUser);
		params.put("updateUserName", updateUserName);
		params.put("planNo", planNo);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = productionPlanAlterationService.listForMap(params);
		int total = productionPlanAlterationService.countForMap(params);
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
	 * 保存生产计划
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/save", method = RequestMethod.POST, apiTitle = "保存生产计划信息")
	@ApiOperation("保存生产计划信息")
	public R save(ProductionPlanDO planDO) {
		if (productionPlanService.add(planDO) > 0) {
			Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
			result.put("id", planDO.getId());
			return R.ok(result);
		}
		return R.error();
	}

	/**
	 * 查看生产计划详情
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/productionPlan/detail", method = RequestMethod.POST, apiTitle = "查看生产计划信息")
	@ApiOperation("查看生产计划信息")
	public R detail(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(productionPlanService.getDetailInfo(id));
	}

	/**
	 * 修改生产计划
     * @see  #alterationPlan(ProductionPlanDO) 变更生产计划
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/update", method = RequestMethod.POST, apiTitle = "修改生产计划")
	@ApiOperation("修改生产计划")
	public R update(ProductionPlanDO planDO) {
		ProductionPlanDO productionPlanDO = productionPlanService.get(planDO.getId());
		if (Objects.equals(ConstantForMES.CLOSE_CASE, productionPlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("receipt.closeCase.update.disabled",null));
		}
		if (!Objects.equals(ConstantForMES.PLAN, productionPlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("receipt.plan.alteration",null));
		}
		if (productionPlanService.edit(planDO) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 下达生产计划
	 * 1、下达时检查是否有BOM存在，如果没有弹窗提示，如果有BOM，则调用BOM生成生产投料单，生产投料单上计划投料数量计算公式：生产计划单上的计划生产数量*BOM子项的标准用量/(1-损耗率)。
	 * 2、检查生产类型是否为工序跟踪，如果选择是，则工艺路线不能为空，如果为空则提示不能下达。如果有工艺路线，则根据配置的工艺路线自动生成对应的工序计划单。
	 * 3、将生产计划单状态更改为已下达
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/issuedPlan", method = RequestMethod.POST, apiTitle = "下达生产计划")
	@ApiOperation("下达生产计划")
	public R issuedPlan(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.issuedPlan(id);
	}

	/**
	 * 反下达生产计划
	 * 1、检查是否有对应的生产投料单和工序计划单存在，对应的生产投料单和工序计划单是否审核或下达，如果已审核或下达则生产计划单不能反下达；
	 * 2、对应的生产投料单和工序计划单存在，且为未审核和未下达状态则删除对应的生产投料单和工序计划单；
	 * 3、将单据状态修改为未下达
	 *
	 * @date 2019-11-27 
	 * @author gumingjie
	 * @version 1  
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/reverseIssuedPlan", method = RequestMethod.POST, apiTitle = "反下达生产计划")
	@ApiOperation("反下达生产计划")
	public R reverseIssuedPlan(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.reverseIssuedPlan(id);
	}

	/**
	 * 挂起生产计划
	 * 1.下达状态的单据才能挂起
	 * 2.若有工序计划单并且是下达状态将工序计划单的状态修改为挂起
	 * 3.将工序计划单的下属工单全部修改为挂起状态
	 * 4.将生产计划修改为挂起状态
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/putUpPlan", method = RequestMethod.POST, apiTitle = "挂起生产计划")
	@ApiOperation("挂起生产计划")
	public R putUpPlan(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.putUpPlan(id);
	}

	/**
	 * 反挂起生产计划
	 * 1、检查对应的工序计划单状态，如果是挂起状态则修改为下达；
	 * 2、检查对应的生产工单状态，如果是挂起状态则修改为开工； XXX（未修改为开工，只将派工单修改为派工状态）
	 * 3、生产计划单状态修改为下达
     *
	 * @date 2019-11-28
	 * @author gumingjie
	 * @version 1  
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/reversePutUpPlan", method = RequestMethod.POST, apiTitle = "反挂起生产计划")
	@ApiOperation("反挂起生产计划")
	public R reversePutUpPlan(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.reversePutUpPlan(id);
	}

	/**
	 * 结案生产计划
	 * 1、检查单据是否为计划、结案状态，如果是计划状态则无须变更可直接修改，如果是结案状态则不能变更。
	 * 2、如果是下达状态，对应的工序计划不允许有派工存在。如果没有派工记录存在，调出生产计划单编辑界面，同时开放计划生产数量、计划开工时间和计划完工时间允许修改。
	 * 3、如果是调整工艺路线，则无须变更生产计划，变更对应的工序计划单中的工序配置即可。
	 * 4、变更保存后调整对应的工序计划单中的计划生产数量、计划开工日期和计划完工日期。
	 * 5、生成变更记录
     *
	 * @date 2019-11-28
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/closeCase", method = RequestMethod.POST, apiTitle = "结案生产计划")
	@ApiOperation("结案生产计划")
	public R closeCase(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.closeCasePlan(id);
	}

	/**
	 * 反结案生产计划
	 * 1.若工序计划单已结案不能反结案
	 * 2.将结案的生产投料单改回已审核状态
	 * 3.将生产计划单修改为下达状态
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/reverseCloseCase", method = RequestMethod.POST, apiTitle = "反结案生产计划")
	@ApiOperation("反结案生产计划")
	public R reverseCloseCase(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.reverseCloseCasePlan(id);
	}

	/**
	 * 变更生产计划
	 * 1、检查单据是否为计划、结案状态，如果是计划状态则无须变更可直接修改，如果是结案状态则不能变更。
	 * 2、如果是下达状态，对应的工序计划不允许有派工存在。如果没有派工记录存在，调出生产计划单编辑界面，同时开放计划生产数量、计划开工时间和计划完工时间允许修改。
	 * 3、如果是调整工艺路线，则无须变更生产计划，变更对应的工序计划单中的工序配置即可。
	 * 4、变更保存后调整对应的工序计划单中的计划生产数量、计划开工日期和计划完工日期。
	 * 5、生成变更记录
     *
	 * @date 2019-11-28
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/alterationPlan", method = RequestMethod.POST, apiTitle = "变更生产计划")
	@ApiOperation("变更生产计划")
	public R alterationPlan(ProductionPlanDO planDO) {
		return productionPlanService.alterationPlan(planDO);
	}

    /**
     * 变更生产计划(获取是否存在派工单)
     *
     * @date 2019-12-12
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/productionPlan/isAlterationPlan", method = RequestMethod.POST, apiTitle = "变更生产计划(获取是否存在派工单)")
    @ApiOperation("变更生产计划(获取是否存在派工单)")
    public R isAlterationPlan(@ApiParam(value = "生产计划主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        List<WorkingProcedurePlanDO> planList = productionPlanService.getWorkingProcedurePlanList(id, Maps.newHashMap());
        if (planList.size()>0){
            Map<String,Object> params;
            for (WorkingProcedurePlanDO workingProcedurePlanDO: planList) {
                params = Maps.newHashMap();
                params.put("planId", workingProcedurePlanDO.getId());
                int count = workingProcedurePlanService.dispatchItemCount(params);
                if (count>0){
                    return R.error(messageSourceHandler.getMessage("receipt.plan.isAlterationPlan",null));
                }
            }
        }
        return  R.ok();
    }

	/**
	 * 删除单个生产计划单
	 * 删除：删除当前选中生产计划，已经下达或被关联引用提示不能删除
     *
	 * @date 2019-11-27
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/remove", method = RequestMethod.POST, apiTitle = "删除生产计划信息")
	@ApiOperation("删除生产计划信息")
	public R remove(
			@ApiParam(value = "生产计划主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionPlanService.delete(id);
	}

	/**
	 * 批量删除生产计划单 
	 * 删除：删除当前选中生产计划，已经下达或被关联引用提示不能删除
	 *
	 * @date 2019-11-27 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionPlan/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除生产计划信息")
	@ApiOperation("批量删除生产计划信息")
	public R remove(
			@ApiParam(value = "生产计划主键数组", required = true, example = "1,2,3,4") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		return productionPlanService.batchDelete(ids);
	}
	
	/**
	 * 跟踪工序计划
	 * 1、根据生产计划关联的下游单据列表显示工序计划、工序派工和报工的跟踪信息，显示方式为向下连查。
	 *
	 * @date 2019-11-30
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/productionPlan/followPlan", method = RequestMethod.POST, apiTitle = "跟踪")
	@ApiOperation("跟踪")
	public R followPlan(
			@ApiParam(value = "生产计划Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return productionPlanService.followPlan(id);
	}
}
