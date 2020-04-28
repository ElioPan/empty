package com.ev.mes.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.MaterielService;
import com.ev.mes.dao.ProductionPlanDao;
import com.ev.mes.domain.BomDetailDO;
import com.ev.mes.domain.CraftItemDO;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.domain.ProductionPlanAlterationDO;
import com.ev.mes.domain.ProductionPlanDO;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.domain.WorkingProcedurePlanDO;
import com.ev.mes.service.BomDetailService;
import com.ev.mes.service.CraftItemService;
import com.ev.mes.service.DispatchItemService;
import com.ev.mes.service.ProcessCheckService;
import com.ev.mes.service.ProductionFeedingService;
import com.ev.mes.service.ProductionPlanAlterationService;
import com.ev.mes.service.ProductionPlanService;
import com.ev.mes.service.WorkingProcedureDetailService;
import com.ev.mes.service.WorkingProcedurePlanService;
import com.ev.system.service.DeptService;
import com.google.common.collect.Maps;

@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {
    @Autowired
    private ProductionPlanDao productionPlanDao;
    @Autowired
    private ProductionPlanAlterationService alterationService;
    @Autowired
    private DeptService sysDeptService;
    @Autowired
    private ProductionFeedingService feedingService;
    @Autowired
    private BomDetailService bomDetailService;
    @Autowired
    private WorkingProcedurePlanService workingProcedurePlanService;
    @Autowired
    private WorkingProcedureDetailService workingProcedureDetailService;
    @Autowired
    private CraftItemService craftItemService;
    @Autowired
    private ProcessCheckService checkService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private DispatchItemService dispatchItemService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public ProductionPlanDO get(Long id) {
        return productionPlanDao.get(id);
    }

    @Override
    public List<ProductionPlanDO> list(Map<String, Object> map) {
        return productionPlanDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return productionPlanDao.count(map);
    }

    @Override
    public int save(ProductionPlanDO productionPlan) {
        return productionPlanDao.save(productionPlan);
    }

    @Override
    public int update(ProductionPlanDO productionPlan) {
        return productionPlanDao.update(productionPlan);
    }

    @Override
    public int remove(Long id) {
        return productionPlanDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return productionPlanDao.batchRemove(ids);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> params) {
        return productionPlanDao.listForMap(params);
    }

    @Override
    public Map<String, Object> countForMap(Map<String, Object> params) {
        return productionPlanDao.countForMap(params);
    }

    @Override
    public int add(ProductionPlanDO planDO) {
        this.setPlanNo(planDO);
        planDO.setStatus(ConstantForMES.PLAN);
        return this.save(planDO);
    }

    @Override
    public Map<String, Object> getDetailInfo(Long id) {
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("id", id);
        params.put("offset", 0);
        params.put("limit", 1);
        List<Map<String, Object>> listForMap = this.listForMap(params);
        results.put("data", listForMap.size() > 0 ? listForMap.get(0) : null);
        return results;
    }

    @Override
    public int edit(ProductionPlanDO planDO) {
        return this.update(planDO);
    }

    @Override
    public R delete(Long id) {
        ProductionPlanDO productionPlanDO = this.get(id);
        if (!Objects.equals(productionPlanDO.getStatus(), ConstantForMES.PLAN)) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
        }
        alterationService.removeByPlanId(id);
        return this.remove(id) > 0 ? R.ok() : R.error();
    }

    @Override
    public R batchDelete(Long[] ids) {
        for (Long id : ids) {
            ProductionPlanDO productionPlanDO = this.get(id);
            if (!Objects.equals(productionPlanDO.getStatus(), ConstantForMES.PLAN)) {
                return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
            }
        }
        alterationService.batchRemoveByPlanId(ids);
        return this.batchRemove(ids) > 0 ? R.ok() : R.error();
    }

    @Override
    public void setPlanNo(ProductionPlanDO planDO) {
        String maxNo = DateFormatUtil.getWorkOrderno(ConstantForMES.SCJH_PREFIX);
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("maxNo", maxNo);
        param.put("offset", 0);
        param.put("limit", 1);
        List<ProductionPlanDO> list = this.list(param);
        String taskNo = null;
        if (list.size() > 0) {
            taskNo = list.get(0).getPlanNo();
        }
        planDO.setPlanNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
    }

    @Override
    public R issuedPlan(Long id) {
        ProductionPlanDO planDO = this.get(id);
        if (!this.isPlan(planDO)) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonPlan", null));
        }
        if (this.isBomEmpty(planDO)) {
            return R.error(messageSourceHandler.getMessage("plan.bom.isEmpty", null));
        }
        if (this.isTecRouteEmpty(planDO)) {
            return R.error(messageSourceHandler.getMessage("plan.tecRoute.isEmpty", null));
        }
//		if (this.isInspectionEmpty(planDO)) {
//			return R.error("请选择检验方案");
//		}
        feedingService.add(this.getFeedingDO(planDO), this.getFeedingChildArray(planDO));
        planDO.setStatus(ConstantForMES.ISSUED);
        planDO.setGiveTime(new Date());
        int update = this.update(planDO);
        return update > 0 ? R.ok() : R.error();
    }

    @Override
    public boolean isTecRouteEmpty(ProductionPlanDO planDO) {
        // 检查生产类型是否为工序跟踪
        if (!Objects.equals(planDO.getType(), ConstantForMES.PROCESS_TRACKING)) {
            return false;
        }
        // 检查工艺路线是否为空
        if (Objects.isNull(planDO.getTecRouteId())) {
            return true;
        }
        workingProcedurePlanService.add(this.getWorkingProcedurePlanDO(planDO),
                this.gettWorkingProcedureChildArray(planDO),null);
        return false;
    }

    /**
     * 通过生产计划生成一个工序计划头单信息
     */
    private WorkingProcedurePlanDO getWorkingProcedurePlanDO(ProductionPlanDO planDO) {
        WorkingProcedurePlanDO procedurePlanDO = new WorkingProcedurePlanDO();
        BeanUtils.copyProperties(planDO, procedurePlanDO);
        procedurePlanDO.setCount(planDO.getPlanCount());
        procedurePlanDO.setProductionPlanId(planDO.getId());
        procedurePlanDO.setGiveTime(null);
        return procedurePlanDO;
    }

    /**
     * 通过生产计划生成一个工序详细列表信息
     */
    private String gettWorkingProcedureChildArray(ProductionPlanDO planDO) {
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
        param.put("craftId", planDO.getTecRouteId());
        List<CraftItemDO> list = craftItemService.list(param);
        List<Map<String, Object>> craftItemList = new ArrayList<>();
        Map<String, Object> craftItemMap;
        for (CraftItemDO craftItemDO : list) {
            craftItemMap = Maps.newHashMap();
            craftItemMap.put("processId", craftItemDO.getProcessId());
            craftItemMap.put("serialNumber", craftItemDO.getSerialNumber());
            craftItemMap.put("processType", craftItemDO.getType());
            craftItemMap.put("deptId", craftItemDO.getDeptId());
            craftItemMap.put("demand", craftItemDO.getDemand());
            craftItemMap.put("operator", craftItemDO.getDeptId());
            craftItemMap.put("planCount", planDO.getPlanCount());
            craftItemMap.put("isExamine", craftItemDO.getWhetherExamine());
            craftItemMap.put("isOutsource", craftItemDO.getWhetherOutsource());
            craftItemMap.put("isCollect", craftItemDO.getWhetherCollect());
            craftItemMap.put("deviceId", craftItemDO.getDeviceId());
            craftItemMap.put("isAuto", craftItemDO.getAutoDispatch());
            craftItemMap.put("totalHour", craftItemDO.getTotalHour());
            craftItemMap.put("manHour", craftItemDO.getManHour());
            craftItemMap.put("labourPrice", craftItemDO.getLabourPrice());
            if (craftItemDO.getWhetherExamine() == 1) {
                param.clear();
                param.put("foreignId", craftItemDO.getId());
                param.put("type", ConstantForMES.CRAFT_GYLX);
                craftItemMap.put("pro", checkService.list(param));
            }
            craftItemList.add(craftItemMap);
        }
        return JSON.toJSONString(craftItemList);
    }

    @Override
    public boolean isBomEmpty(ProductionPlanDO planDO) {
        return Objects.isNull(planDO.getBomId());
    }

    /**
     * 通过生产计划信息创建一个生产投料单子物料信息
     */
    private String getFeedingChildArray(ProductionPlanDO planDO) {
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
        Integer isCollect = planDO.getIsCollect();
        param.put("bomId", planDO.getBomId());
        List<BomDetailDO> list = bomDetailService.list(param);
        // 获取所有物料详情
        List<Long> materielIdList = list.stream()
                .map(BomDetailDO::getMaterielId)
                .distinct()
                .collect(Collectors.toList());
        param.remove("bomId");
        param.put("materielIdList",materielIdList);
        List<MaterielDO> materielDOList = materielService.list(param);
        Map<Long, Long> facilityMap = materielDOList
                .stream()
                .collect(Collectors.toMap(MaterielDO::getId, v->v.getDefaultFacility()==null?0:v.getDefaultFacility()));
        Map<Long, Long> locationMap = materielDOList
                .stream()
                .collect(Collectors.toMap(MaterielDO::getId, v->v.getDefaultFacility()==null?0:v.getDefaultLocation()));

        List<Map<String, Object>> feedingDetailList = new ArrayList<>();
        Map<String, Object> feedingDetail;
        for (BomDetailDO bomDetailDO : list) {
            feedingDetail = Maps.newHashMapWithExpectedSize(2);
            Long materielId = bomDetailDO.getMaterielId();
            feedingDetail.put("materielId", materielId);
            // 计划投料数量公式 (标准用量 /(1-损耗率/100))*计划生产数量
            BigDecimal wasteRate = bomDetailDO.getWasteRate();
            BigDecimal standardCount = bomDetailDO.getStandardCount();
            BigDecimal planCount = planDO.getPlanCount();
            BigDecimal planFeeding = standardCount.divide(BigDecimal.valueOf(1 - wasteRate.doubleValue() / 100),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP)
                    .multiply(planCount);
            feedingDetail.put("planFeeding", planFeeding);
            // 新增字段
            feedingDetail.put("processId", bomDetailDO.getProcessId());
            feedingDetail.put("stationId", bomDetailDO.getStationId());
            feedingDetail.put("facilityId", facilityMap.get(materielId));
            feedingDetail.put("locationId", locationMap.get(materielId));
            feedingDetail.put("isCollect", isCollect);
            feedingDetailList.add(feedingDetail);
        }
        return JSON.toJSONString(feedingDetailList);
    }

    /**
     * 通过生产计划信息创建一个生产投料单据头信息
     */
    private ProductionFeedingDO getFeedingDO(ProductionPlanDO planDO) {
        ProductionFeedingDO feedingDO = new ProductionFeedingDO();
        BeanUtils.copyProperties(planDO, feedingDO);
        feedingDO.setProductionPlanId(planDO.getId());
        return feedingDO;
    }

//	@Override
//	public boolean isInspectionEmpty(ProductionPlanDO planDO) {
//		return Objects.isNull(planDO.getInspectionScheme());
//	}

    @Override
    public boolean isPlan(ProductionPlanDO planDO) {
        return Objects.equals(planDO.getStatus(), ConstantForMES.PLAN);
    }

    @Override
    public R reverseIssuedPlan(Long id) {
        ProductionPlanDO planDO = this.get(id);
        if (!Objects.equals(planDO.getStatus(), ConstantForMES.ISSUED)) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonIssuedPlan.reverseIssuedPlan", null));
        }

        Map<String, Object> param = Maps.newHashMap();
        List<ProductionFeedingDO> feedingList = this.getFeedingList(id, param);
        // 生产投料单存在&&生产投料单不为待审核
        if (feedingList.size() > 0 && Objects.equals(ConstantForMES.OK_AUDITED, feedingList.get(0).getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.feedingPlan.isAudit", null));
        }
        // 工序计划单存在&&工序计划单不为计划状态
        this.getWorkingProcedurePlanList(id, param);
        List<WorkingProcedurePlanDO> workingProcedurePlanList = this.getWorkingProcedurePlanList(id, param);
        if (workingProcedurePlanList.size() > 0
                && !Objects.equals(ConstantForMES.PLAN, workingProcedurePlanList.get(0).getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.workingPlan.issuedPlan", null));
        }

        // 看有无生成生产入库单
        int productionStockInCount  = productionPlanDao.productionStockInCount(id);
        if (productionStockInCount > 0) {
            return R.error(messageSourceHandler.getMessage("plan.child.nonIssuedPlan.reverseIssuedPlan", null));
        }

        // 若都未产生下级单据则将投料单删除
        if (feedingList.size() > 0) {
            feedingService.removeHeadAndBody(feedingList.get(0).getId());
        }
        // 若都未产生下级单据则将工序计划单删除
        if (workingProcedurePlanList.size() > 0) {
            workingProcedurePlanService.removeHeadAndBody(workingProcedurePlanList.get(0).getId());
        }

        ProductionPlanDO productionPlanDO = new ProductionPlanDO();
        productionPlanDO.setId(id);
        productionPlanDO.setStatus(ConstantForMES.PLAN);
        productionPlanDO.setGiveTime(new Date());
        return this.update(productionPlanDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public List<WorkingProcedurePlanDO> getWorkingProcedurePlanList(Long id, Map<String, Object> param) {
        param.put("productionPlanId", id);
        return workingProcedurePlanService.list(param);
    }

    @Override
    public List<Map<String, Object>> listDialogMap(Map<String, Object> params) {
        return productionPlanDao.listDialogMap(params);
    }

    @Override
    public BigDecimal getCountBySource(Map<String, Object> sourceParam) {
        return productionPlanDao.getCountBySource(sourceParam);
    }

    private List<ProductionFeedingDO> getFeedingList(Long id, Map<String, Object> param) {
        param.put("productionPlanId", id);
        return feedingService.list(param);
    }

    @Override
    public R putUpPlan(Long id) {
        if (!Objects.equals(this.get(id).getStatus(), ConstantForMES.ISSUED)) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonIssued.putUp", null));
        }
        // 查看工序计划单存在
        Map<String, Object> param = Maps.newHashMap();
        List<WorkingProcedurePlanDO> workingProcedurePlanList = this.getWorkingProcedurePlanList(id, param);
        if (workingProcedurePlanList.size() > 0) {
            List<Long> foriegnIds;
            // 存在即修改其状态为挂起
            for (WorkingProcedurePlanDO workingProcedurePlanDO : workingProcedurePlanList) {
                if (Objects.equals(workingProcedurePlanDO.getStatus(), ConstantForMES.ISSUED)) {
                    workingProcedurePlanDO.setStatus(ConstantForMES.PUT_UP);
                    workingProcedurePlanService.update(workingProcedurePlanDO);
                    foriegnIds = new ArrayList<>();
                    // 查询对应的工单，存在即全部修改为挂起
                    List<Map<String, Object>> listByPlanId = workingProcedureDetailService
                            .listByPlanId(workingProcedurePlanDO.getId());
                    if (listByPlanId.size() > 0) {
                        for (Map<String, Object> map : listByPlanId) {
                            foriegnIds.add(Long.parseLong(map.get("id").toString()));
                        }
                        dispatchItemService.changeOfDiapatchStatus(foriegnIds.toArray(new Long[0]));
                    }
                }
            }
        }

        // 修改生产计划为挂起状态
        ProductionPlanDO productionPlanDO = new ProductionPlanDO();
        productionPlanDO.setId(id);
        productionPlanDO.setStatus(ConstantForMES.PUT_UP);
        int update = this.update(productionPlanDO);
        return update > 0 ? R.ok() : R.error();
    }

    @Override
    public R reversePutUpPlan(Long id) {
        if (!Objects.equals(this.get(id).getStatus(), ConstantForMES.PUT_UP)) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonPutUp.reversePutUp", null));
        }
        // 查看工序计划单存在
        Map<String, Object> param = Maps.newHashMap();
        List<WorkingProcedurePlanDO> workingProcedurePlanList = this.getWorkingProcedurePlanList(id, param);
        if (workingProcedurePlanList.size() > 0) {
            WorkingProcedurePlanDO workingProcedurePlanDO = workingProcedurePlanList.get(0);
            if (Objects.equals(workingProcedurePlanDO.getStatus(), ConstantForMES.PUT_UP)) {
                // 存在即修改其状态为下达 ConstantForMES.ISSUED
                workingProcedurePlanDO.setStatus(ConstantForMES.ISSUED);
                workingProcedurePlanService.update(workingProcedurePlanDO);
            }
            // 修改工序子表的状态
            WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
            detailDO.setIsDispatching(ConstantForMES.SEND_ORDERS);
            detailDO.setPlanId(workingProcedurePlanDO.getId());
            // 将工序计划明细列表数据修改为派工状态
            workingProcedureDetailService.updateByPlanId(detailDO);
        }

        // 修改生产计划为下达状态
        ProductionPlanDO productionPlanDO = new ProductionPlanDO();
        productionPlanDO.setId(id);
        productionPlanDO.setStatus(ConstantForMES.ISSUED);
        int update = this.update(productionPlanDO);
        return update > 0 ? R.ok() : R.error();
    }

    @Override
    public R closeCasePlan(Long id) {
        if (!Objects.equals(ConstantForMES.ISSUED, this.get(id).getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonIssued.closeCase", null));
        }

        // 查出对应的工序计划信息
        Map<String, Object> param = Maps.newHashMap();
        List<WorkingProcedurePlanDO> workingProcedurePlanList = this.getWorkingProcedurePlanList(id, param);
        // 工序计划不是结案状态
        if (workingProcedurePlanList.size() > 0) {
            Map<String, Object> workingProcedurePlanDOMap;
            List<Map<String, Object>> workingProcedurePlanDOList = Lists.newArrayList();
            for (WorkingProcedurePlanDO workingProcedurePlanDO : workingProcedurePlanList) {
                // 若是结案状态则不返回列表数据 生产计划结案完成
                if (Objects.equals(workingProcedurePlanDO.getStatus(), ConstantForMES.CLOSE_CASE)) {
                    // 关闭投料单设置投料单状态为 ConstantForMES.CLOSE_CASE
                    continue;
                }
                // 若不是返回工序计划信息 显示内容：工序计划编号，产品代码、产品名称、计划开工日期、计划完工日期、完工数量
                workingProcedurePlanDOMap = Maps.newHashMap();
                workingProcedurePlanDOMap.put("workOrderNo", workingProcedurePlanDO.getWorkOrderNo());

                MaterielDO materielDO = materielService.get(workingProcedurePlanDO.getMaterielId());
                workingProcedurePlanDOMap.put("materialSerialNo", materielDO != null ? materielDO.getSerialNo() : null);
                workingProcedurePlanDOMap.put("materialName", materielDO != null ? materielDO.getName() : null);

                workingProcedurePlanDOMap.put("planStartTime", workingProcedurePlanDO.getPlanStartTime());
                workingProcedurePlanDOMap.put("planEndTime", workingProcedurePlanDO.getPlanEndTime());
                // 取末道工序的完工数量
                Long workingProcedurePlanId = workingProcedurePlanDO.getId();
                workingProcedurePlanDOMap.put("id", workingProcedurePlanId);
                List<Map<String, Object>> listByPlanId = workingProcedureDetailService.listByPlanId(workingProcedurePlanId);
                if (listByPlanId.size() > 0) {
                    Map<String, Object> map = listByPlanId.get(listByPlanId.size() - 1);
                    workingProcedurePlanDOMap.put("completionCount", map.getOrDefault("completionCount", null));
                }
                workingProcedurePlanDOList.add(workingProcedurePlanDOMap);
            }
            if (workingProcedurePlanDOList.size() > 0) {
                Map<String, Object> result = Maps.newHashMap();
                result.put("data", workingProcedurePlanDOList);
                return R.error(1, messageSourceHandler.getMessage("plan.workingPlan.unfinished", null), result);
            }
        }
        this.updateFeedingAndPlanStatus(id, param);
        return R.ok();
    }

    private void updateFeedingAndPlanStatus(Long id, Map<String, Object> param) {
        List<ProductionFeedingDO> feedingList = this.getFeedingList(id, param);
        if (feedingList.size() > 0) {
            ProductionFeedingDO feedingDO = feedingList.get(0);
            feedingDO.setStatus(ConstantForMES.CLOSE_CASE);
            feedingService.update(feedingDO);
        }
        // 修改生产计划为结案状态
        ProductionPlanDO productionPlanDO = new ProductionPlanDO();
        productionPlanDO.setId(id);
        productionPlanDO.setActualFinishTime(new Date());
        productionPlanDO.setStatus(ConstantForMES.CLOSE_CASE);
        this.update(productionPlanDO);
    }

    @Override
    public R reverseCloseCasePlan(Long id) {
        if (!Objects.equals(ConstantForMES.CLOSE_CASE, this.get(id).getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonCloseCase.reverseCloseCase", null));
        }

        // 查出对应的工序计划信息
        Map<String, Object> param = Maps.newHashMap();
        List<WorkingProcedurePlanDO> workingProcedurePlanList = this.getWorkingProcedurePlanList(id, param);
        // 工序计划是结案状态
        if (workingProcedurePlanList.size() > 0) {
            WorkingProcedurePlanDO procedurePlanDO = workingProcedurePlanList.get(0);
            if (Objects.equals(ConstantForMES.CLOSE_CASE, procedurePlanDO.getStatus())) {
                return R.error(messageSourceHandler.getMessage("plan.closeCase.reverseCloseCase", null));
            }
        }
        List<ProductionFeedingDO> feedingList = this.getFeedingList(id, param);
        // 开启投料单设置投料单状态为 ConstantForMES.OK_AUDITED
        if (feedingList.size() > 0) {
            ProductionFeedingDO feedingDO = feedingList.get(0);
            feedingDO.setStatus(ConstantForMES.OK_AUDITED);
            feedingService.update(feedingDO);
        }
        // 修改生产计划为下达状态
        ProductionPlanDO productionPlanDO = new ProductionPlanDO();
        productionPlanDO.setId(id);
        productionPlanDO.setStatus(ConstantForMES.ISSUED);
        int update = this.update(productionPlanDO);

        return update > 0 ? R.ok() : R.error();
    }

    @Override
    public R alterationPlan(ProductionPlanDO planDO) {
        Long planId = planDO.getId();
        ProductionPlanDO productionPlanDO = this.get(planId);
        Long status = productionPlanDO.getStatus();
        if (Objects.equals(status, ConstantForMES.PLAN)) {
            return R.error(messageSourceHandler.getMessage("plan.alteration.useUpdate", null));
        }
        if (Objects.equals(status, ConstantForMES.CLOSE_CASE)) {
            return R.error(messageSourceHandler.getMessage("plan.alteration.isCloseCase", null));
        }
        BigDecimal oldPlanCount = productionPlanDO.getPlanCount();
        BigDecimal newPlanCount = planDO.getPlanCount();

        Date oldPlanStartTime = productionPlanDO.getPlanStartTime();
        Date newPlanStartTime = planDO.getPlanStartTime();

        Date oldPlanEndTime = productionPlanDO.getPlanEndTime();
        Date newPlanEndTime = planDO.getPlanEndTime();

        Long oldProDept = productionPlanDO.getProDept();
        Long newProDept = planDO.getProDept();

        Map<String, Object> param = Maps.newHashMap();
        List<WorkingProcedurePlanDO> procedurePlanList = this.getWorkingProcedurePlanList(planId, param);
        if (procedurePlanList.size() > 0) {
            Long procedurePlanId = procedurePlanList.get(0).getId();
            List<Map<String, Object>> listByPlanId = workingProcedureDetailService.listByPlanId(procedurePlanId);
            Map<String, Object> params;
            for (Map<String, Object> map : listByPlanId) {
                // XXX 循环调用查询是否有已开工的的工单 (可以使用批量查出方法)
                params = Maps.newHashMap();
                params.put("foriegnId", map.get("id"));
                int count = dispatchItemService.count(params);
                if (count > 0) {
                    return R.error(messageSourceHandler.getMessage("plan.alteration.isSendOrders", null));
                }
            }
            // 修改工序主表的数量
            WorkingProcedurePlanDO procedurePlanDO = new WorkingProcedurePlanDO();
            procedurePlanDO.setId(procedurePlanId);
            procedurePlanDO.setCount(newPlanCount);
            procedurePlanDO.setPlanStartTime(newPlanStartTime);
            procedurePlanDO.setPlanEndTime(newPlanEndTime);
            workingProcedurePlanService.update(procedurePlanDO);
            // 修改工序子表的数量
            WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
            detailDO.setPlanCount(newPlanCount);
            detailDO.setPlanId(procedurePlanId);
            workingProcedureDetailService.updateByPlanId(detailDO);
        }

        ProductionPlanAlterationDO productionPlanAlteration;
        if (oldPlanCount.compareTo(newPlanCount) != 0) {
            productionPlanAlteration = new ProductionPlanAlterationDO();
            productionPlanAlteration.setProductionPlanId(planId);
            productionPlanAlteration.setAfterAlteration(newPlanCount.toPlainString());
            productionPlanAlteration.setBeforeAlteration(oldPlanCount.toPlainString());
            productionPlanAlteration.setAlterationProject("计划生产数量");
            alterationService.save(productionPlanAlteration);
        }
        if (!oldPlanStartTime.equals(newPlanStartTime)) {
            productionPlanAlteration = new ProductionPlanAlterationDO();
            productionPlanAlteration.setProductionPlanId(planId);
            productionPlanAlteration.setAfterAlteration(DateFormatUtil.getFormateDate(newPlanStartTime));
            productionPlanAlteration.setBeforeAlteration(DateFormatUtil.getFormateDate(oldPlanStartTime));
            productionPlanAlteration.setAlterationProject("计划开工时间");
            alterationService.save(productionPlanAlteration);
        }
        if (!oldPlanEndTime.equals(newPlanEndTime)) {
            productionPlanAlteration = new ProductionPlanAlterationDO();
            productionPlanAlteration.setProductionPlanId(planId);
            productionPlanAlteration.setAfterAlteration(DateFormatUtil.getFormateDate(newPlanEndTime));
            productionPlanAlteration.setBeforeAlteration(DateFormatUtil.getFormateDate(oldPlanEndTime));
            productionPlanAlteration.setAlterationProject("计划完工时间");
            alterationService.save(productionPlanAlteration);
        }
        if (!Objects.equals(oldProDept, newProDept)) {
            productionPlanAlteration = new ProductionPlanAlterationDO();
            productionPlanAlteration.setProductionPlanId(planId);
            productionPlanAlteration.setAfterAlteration(sysDeptService.get(newProDept).getName());
            productionPlanAlteration.setBeforeAlteration(sysDeptService.get(oldProDept).getName());
            productionPlanAlteration.setAlterationProject("生产部门");
            alterationService.save(productionPlanAlteration);
        }
        ProductionPlanDO productionPlan = new ProductionPlanDO();
        productionPlan.setId(planId);
        productionPlan.setPlanCount(newPlanCount);
        productionPlan.setPlanStartTime(newPlanStartTime);
        productionPlan.setPlanEndTime(newPlanEndTime);
        productionPlan.setProDept(newProDept);
        this.update(productionPlan);
        return R.ok();
    }

    @Override
    public R followPlan(Long id) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap;
        Map<String, Object> params = Maps.newHashMap();
        params.put("productionPlanId", id);
        List<Map<String, Object>> headList = workingProcedurePlanService.listForMap(params);
        if (headList.size() > 0) {
            for (Map<String, Object> headMap : headList) {
                List<Map<String, Object>> bodyList = workingProcedureDetailService
                        .listByPlanId(Long.parseLong(headMap.get("id").toString()));
                Map<String, Object> firstProcess = bodyList.get(0);
                String realityStartTime = firstProcess.containsKey("realityStartTime")
                        ? firstProcess.get("realityStartTime").toString()
                        : null;
                Map<String, Object> lastProcess = bodyList.get(bodyList.size() - 1);
                String realityEndTime = lastProcess.containsKey("realityEndTime")
                        ? lastProcess.get("realityEndTime").toString()
                        : null;
                headMap.put("realityStartTime", realityStartTime);
                headMap.put("realityEndTime", realityEndTime);
                resultMap = Maps.newHashMap();
                resultMap.put("inheadInfo", headMap);
                resultMap.put("bodyInfo", bodyList);
                resultList.add(resultMap);
            }
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("data", resultList);
        return R.ok(result);
    }

}
