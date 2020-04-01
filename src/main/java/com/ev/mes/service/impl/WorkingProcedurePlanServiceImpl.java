package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.WorkingProcedurePlanDao;
import com.ev.mes.domain.ProcessCheckDO;
import com.ev.mes.domain.ReworkRepairMiddleDO;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.domain.WorkingProcedurePlanDO;
import com.ev.mes.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkingProcedurePlanServiceImpl implements WorkingProcedurePlanService {
	@Autowired
	private WorkingProcedurePlanDao workingProcedurePlanDao;
	@Autowired
	private WorkingProcedureDetailService detailService;
	@Autowired
	private ProcessCheckService checkService;
	@Autowired
	private DispatchItemService dispatchItemService;
	@Autowired
	private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private ReworkRepairMiddleService reworkRepairMiddleService;

	@Override
	public WorkingProcedurePlanDO get(Long id) {
		return workingProcedurePlanDao.get(id);
	}

	@Override
	public List<WorkingProcedurePlanDO> list(Map<String, Object> map) {
		return workingProcedurePlanDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return workingProcedurePlanDao.count(map);
	}

	@Override
	public int save(WorkingProcedurePlanDO workingProcedurePlan) {
		return workingProcedurePlanDao.save(workingProcedurePlan);
	}

	@Override
	public int update(WorkingProcedurePlanDO workingProcedurePlan) {
		return workingProcedurePlanDao.update(workingProcedurePlan);
	}

	@Override
	public int remove(Long id) {
		return workingProcedurePlanDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return workingProcedurePlanDao.batchRemove(ids);
	}

	@Override
	public R add(WorkingProcedurePlanDO planDO, String childArray,String signs) {
		if (StringUtils.isEmpty(childArray)) {
			return R.error();
		}
		planDO.setStatus(ConstantForMES.PLAN);
		this.setWorkOrderNo(planDO);
		int save = this.save(planDO);
		if (save > 0) {
            Long planId = planDO.getId();
            if (StringUtils.isNoneEmpty(signs)){
                ReworkRepairMiddleDO reworkRepairMiddleDO = JSON.parseObject(signs, ReworkRepairMiddleDO.class);
                reworkRepairMiddleDO.setPlanId(planId);
                reworkRepairMiddleService.save(reworkRepairMiddleDO);
            }
            JSONArray jsonArray = JSON.parseArray(childArray);
            List<WorkingProcedureDetailDO> detailArray = JSON.parseArray(childArray, WorkingProcedureDetailDO.class);
            ProcessCheckDO checkDO;
			for (int i = 0; i < detailArray.size(); i++) {
				WorkingProcedureDetailDO detailDO = detailArray.get(i);
				detailDO.setPlanId(planId);
				detailDO.setIsDispatching(ConstantForMES.PLAN);
				detailDO.setAlreadyCount(BigDecimal.ZERO);
				detailService.save(detailDO);
				Long detailId = detailDO.getId();
				// 是否需要检验
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (detailDO.getIsExamine() == 1) {
					JSONArray array = jsonObject.containsKey("pro")?jsonObject.getJSONArray("pro"):null;
					if (Objects.nonNull(array)) {
						for (int j = 0; j < array.size(); j++) {
							JSONObject jsonObject2 = array.getJSONObject(j);
							checkDO = new ProcessCheckDO();
							checkDO.setForeignId(detailId);
							checkDO.setType(ConstantForMES.GXJH_GYLX);
							checkDO.setRemark(jsonObject2.getOrDefault("remark", "").toString());
							checkDO.setProId(Long.parseLong(jsonObject2.getOrDefault("proId", 0L).toString()));
							checkDO.setWhetherCheck(Integer.parseInt(jsonObject2.getOrDefault("whetherCheck", 0).toString()));
							checkService.save(checkDO);
						}
					}
				}

				// sop上传
				JSONArray sopArray = jsonObject.containsKey("uploadAttachment")?jsonObject.getJSONArray("uploadAttachment"):null;
				if (Objects.nonNull(sopArray)) {
					contentAssocService.saveList(detailId, sopArray, ConstantForMES.SOP_FILE);
				}
			}
            Map<String,Object> result = Maps.newHashMap();
            result.put("id", planDO.getId());
            return R.ok(result);
		}
		return R.error();
	}

	@Override
	public void setWorkOrderNo(WorkingProcedurePlanDO planDO) {

		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForMES.GXJH_PREFIX);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		List<WorkingProcedurePlanDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
            List<WorkingProcedurePlanDO> collect = list.stream()
                    .filter(workingProcedurePlanDO -> StringUtils.isBlank(workingProcedurePlanDO.getOriginalPlanNo()))
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                taskNo = collect.get(0).getWorkOrderNo();
            }
		}
		planDO.setWorkOrderNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));

	}
	
	@Override
	public Map<String, Object> getDetailInfo(Long id) {
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);

		results.put("headInfo", workingProcedurePlanDao.getDetail(id));

        List<Map<String, Object>> workingDetailList = detailService.listByPlanId(id);
        Map<String, Object> param = Maps.newHashMap();
        param.put("planId",id);
        param.put("type",ConstantForMES.GXJH_GYLX);
        List<Map<String, Object>> checkProjectList = detailService.getDetailByPlanId(param);

        // 获取SOP文件
		if (workingDetailList.size() > 0) {
			List<Object> detailIds = workingDetailList.stream()
					.map(e -> e.get("id"))
					.collect(Collectors.toList());
			// 获取附件
			Map<String, Object> fileParam = Maps.newHashMapWithExpectedSize(2);
			fileParam.put("assocIds", detailIds);
			fileParam.put("assocType", ConstantForMES.SOP_FILE);
			List<ContentAssocDO> sopFileList = contentAssocService.list(fileParam);
			if (sopFileList.size() > 0) {
				for (Map<String, Object> map : workingDetailList) {
					map.put("uploadAttachment", sopFileList
							.stream()
							.filter(e->e.getAssocId().toString().equals(map.get("id").toString()))
							.collect(Collectors.toList()));
				}
			}
		}

//        List<Map<String, Object>> checkProject;
//        if(workingDetailList.size()>0){
//            for (Map<String, Object> map : workingDetailList) {
//                if (checkProjectList.size() > 0) {
//                    checkProject = Lists.newArrayList();
//                    for (Map<String, Object> map2: checkProjectList) {
//                        if (map.get("id").equals(map2.get("foreignId"))){
//                            checkProject.add(map2);
//                        }
//                    }
//                    map.put("checkProject",checkProject);
//                }
//            }
//        }
        if (checkProjectList.size() > 0 && workingDetailList.size() > 0) {
            for (Map<String, Object> map : workingDetailList) {
                map.put("checkProject", checkProjectList
                        .stream()
                        .filter(stringObjectMap1 -> map.get("id").equals(stringObjectMap1.get("foreignId")))
                        .collect(Collectors.toList()));
            }
        }
		List<Map<String, Object>> workingDetailLists=workingDetailList
				.stream()
				.sorted(Comparator.comparing(e->Integer.parseInt(e.get("serialNumber").toString())))
				.collect(Collectors.toList());

        results.put("bodyInfo", workingDetailLists);
		return results;
	}

    @Override
    public Map<String, Object> getDetailInfo(Long planId, Long bodyId) {
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        results.put("headInfo", workingProcedurePlanDao.getDetail(planId));

//        List<Map<String, Object>> workingDetailList = detailService.listByPlanId(planId);
        Map<String, Object> map = detailService.listByDetailId(bodyId);
        Map<String, Object> param = Maps.newHashMap();
        param.put("bodyId",planId);
        param.put("type",ConstantForMES.GXJH_GYLX);
        List<Map<String, Object>> checkProject = detailService.getDetailByPlanId(param);
        map.put("checkProject", checkProject);
        results.put("bodyInfo",map);
//                .stream()
//                .filter(stringObjectMap -> stringObjectMap.get("id").toString().equals(bodyId.toString()))
//                .collect(Collectors.toList());
//        if (bodyInfo.size() > 0 && workingDetailList.size() > 0) {
//            Map<String, Object> map = workingDetailList.get(0);
//                    map.put("checkProject", bodyInfo
//                        .stream()
//                        .filter(stringObjectMap1 -> map.get("id").equals(stringObjectMap1.get("foreignId")))
//                        .collect(Collectors.toList()));
//            results.put("bodyInfo", map);
//        }
        return results;
    }

	@Override
	public R issuedPlan(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		if (!Objects.equals(ConstantForMES.PLAN, workingProcedurePlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonPlan.issuedPlan",null));
		}
		// 验证生产部门是否为空
		Map<String,Object> param = Maps.newHashMap();
		param.put("planId",id);
		List<WorkingProcedureDetailDO> itemList = detailService.list(param);
		long deptEmpty = itemList.stream()
				.filter(e -> e.getDeptId() == null)
				.count();
		if (deptEmpty > 0) {
			return R.error(messageSourceHandler.getMessage("plan.dept.isEmpty",null));
		}
		

		// 查看是否需要自动派工的工序
//		List<Map<String, Object>> plan = detailService.listByPlanId(id);
//		if (plan.size() > 0) {
//			for (Map<String, Object> map : plan) {
//				Integer isAuto = map.containsKey("isAuto") ? (Integer) map.get("isAuto") : null;
//				if (isAuto == 1) {
//					// TODO 自动生成派工单（没有配置需要派的人员信息） 暂无此功能
//				}
//			}
//		}

		// 修改工序计划为下达状态，并设置下达时间
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setGiveTime(new Date());
		planDO.setStatus(ConstantForMES.ISSUED);

		// 将工序计划明细列表（派工单）的状态修改为派工状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setPlanId(id);
		detailDO.setIsDispatching(ConstantForMES.SEND_ORDERS);
		detailService.updateByPlanId(detailDO);

		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseIssuedPlan(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		if (!Objects.equals(ConstantForMES.ISSUED, workingProcedurePlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonIssuedPlan.reverseIssuedPlan",null));
		}
		Map<String, Object> param = Maps.newHashMap();
		param.put("planId", id);
		int dispatchItemCount = this.dispatchItemCount(param);
		if (dispatchItemCount>0) {
            return R.error(messageSourceHandler.getMessage("plan.child.nonIssuedPlan.reverseIssuedPlan",null));
		}

		// 修改工序计划为计划状态
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setStatus(ConstantForMES.PLAN);

		// 将工序计划的明细列表状态修改为计划状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setPlanId(id);
		detailDO.setIsDispatching(ConstantForMES.PLAN);
		detailService.updateByPlanId(detailDO);

		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R putUpPlan(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		if (!Objects.equals(ConstantForMES.ISSUED, workingProcedurePlanDO.getStatus())) {
			return R.error("非下达状态不允许挂起");
		}
		// 获取工序的ID数组
		Map<String, Object> param = Maps.newHashMap();
		param.put("planId", id);
		List<WorkingProcedureDetailDO> detailList = detailService.list(param);
		if (detailList.size() > 0) {
			Long[] foriegnIds = new Long[detailList.size()];
			for (int i = 0; i < detailList.size(); i++) {
				foriegnIds[i] = detailList.get(i).getId();
			}
			// 根据工序 将对应的工单全部修改挂起状态
			dispatchItemService.changeOfDiapatchStatus(foriegnIds);
		}
		// 将工序计划修改为挂起状态
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setStatus(ConstantForMES.PUT_UP);

		// 将工序计划明细列表数据修改为挂起状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setIsDispatching(ConstantForMES.PUT_UP);
		detailDO.setPlanId(id);
		detailService.updateByPlanId(detailDO);

		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reversePutUpPlan(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		if (!Objects.equals(ConstantForMES.PUT_UP, workingProcedurePlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonPutUp.reversePutUp",null));
		}
		// 将工序计划明细列表数据修改为派工状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setIsDispatching(ConstantForMES.SEND_ORDERS);
		detailDO.setPlanId(id);
		detailService.updateByPlanId(detailDO);

		// 将工序计划修改为下达状态
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setStatus(ConstantForMES.ISSUED);

		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R closeCasePlan(Long id) {
		// 检查所有的派工单状态 若有工单未结案 显示相应的派工单列表，显示内容：派工单编号，工序代码，工序名称，开工时间，派工数量
		List<Map<String, Object>> dispatchItemlist = detailService.getDispatchItemlist(id);
		if (!Objects.equals(this.get(id).getStatus(), ConstantForMES.ISSUED)) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonIssued.closeCase",null));
		}
		if (dispatchItemlist.size() > 0) {
			Map<String, Object> result = Maps.newHashMap();
			result.put("data", dispatchItemlist);
			return R.error(-1,messageSourceHandler.getMessage("task.unfinished",null),result);
		}

		// 工单全部为结案状态
		// 将工序计划明细列表数据修改为结案状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setIsDispatching(ConstantForMES.CLOSE_CASE);
		detailDO.setPlanId(id);
		detailService.updateByPlanId(detailDO);

		// 将工序计划改为结案状态
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setActualFinishTime(new Date());
		planDO.setStatus(ConstantForMES.CLOSE_CASE);
		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseCloseCasePlan(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		if (!Objects.equals(ConstantForMES.CLOSE_CASE, workingProcedurePlanDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("plan.status.nonCloseCase.reverseCloseCase",null));
		}
		// 将工序计划改为下达状态
		WorkingProcedurePlanDO planDO = new WorkingProcedurePlanDO();
		planDO.setId(id);
		planDO.setStatus(ConstantForMES.ISSUED);

		// 将工序计划明细列表数据修改为派工状态
		WorkingProcedureDetailDO detailDO = new WorkingProcedureDetailDO();
		detailDO.setIsDispatching(ConstantForMES.SEND_ORDERS);
		detailDO.setPlanId(id);
		detailService.updateByPlanId(detailDO);

		return this.update(planDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R splitPlan(Long oldId, String splitArray) {
		// 结案状态不能拆分 判断是否为结案状态
		WorkingProcedurePlanDO procedurePlanDO = this.get(oldId);
		Integer status = procedurePlanDO.getStatus();
		if (Objects.equals(status, ConstantForMES.CLOSE_CASE)) {
            return R.error(messageSourceHandler.getMessage("plan.status.isCloseCase.split",null));
		}

		// 计划状态的工序计划单可以拆分和还原操作
		// 旧的工序列表容器
		List<WorkingProcedureDetailDO> workingProcedureDetailDOList = new ArrayList<>();
		// 新的原单工序列表的计划数量容器
		List<BigDecimal> newProcedurePlanCountList = new ArrayList<>();
		// 拆分后的数量容器
		List<BigDecimal> splitCountList = new ArrayList<>();

		// 首先验证是否每条工序都能被拆分
		com.alibaba.fastjson.JSONArray splitJsonArray = JSON.parseArray(splitArray);
		for (int i = 0; i < splitJsonArray.size(); i++) {
			com.alibaba.fastjson.JSONObject jsonObject = splitJsonArray.getJSONObject(i);
			Long detailId = jsonObject.getLong("id");
			BigDecimal splitCount = jsonObject.getBigDecimal("splitCount");
			WorkingProcedureDetailDO procedureDetailDO = detailService.get(detailId);
			BigDecimal alreadyCount = procedureDetailDO.getAlreadyCount();
			// 能拆分数量<计划数量-已派工数量
			if (splitCount.compareTo(procedureDetailDO.getPlanCount()
					.subtract(alreadyCount == null ? BigDecimal.ZERO : alreadyCount)) > 0) {
                return R.error(messageSourceHandler.getMessage("plan.count.nonEnough.split",null));
			}
			workingProcedureDetailDOList.add(procedureDetailDO);
			// 修改原工序计划数量 新的计划数量=旧的计划数量-拆走的数量
			BigDecimal newProcedurePlanCount = procedureDetailDO.getPlanCount().subtract(splitCount);
			newProcedurePlanCountList.add(newProcedurePlanCount);
			splitCountList.add(splitCount);
		}

		// 修改原单内的工序列表内的计划数量
		if (workingProcedureDetailDOList.size() > 0) {
			for (int i = 0; i < workingProcedureDetailDOList.size(); i++) {
				WorkingProcedureDetailDO procedureDetailDO = workingProcedureDetailDOList.get(i);
				procedureDetailDO.setPlanCount(newProcedurePlanCountList.get(i));
				detailService.update(procedureDetailDO);
			}
		}

		// 保存拆分后的工序计划表头信息
		Map<String, Object> param = Maps.newHashMap();
		String oldWorkOrderNo = procedurePlanDO.getWorkOrderNo();
		param.put("originalPlanNo", oldWorkOrderNo);
		int count = this.count(param);

		WorkingProcedurePlanDO newProcedurePlanDO = new WorkingProcedurePlanDO();
		BeanUtils.copyProperties(procedurePlanDO, newProcedurePlanDO);
		newProcedurePlanDO.setGiveTime(null);
		newProcedurePlanDO.setActualFinishTime(null);
		// 若是拆分的单据也是被拆过的单据，取最原始的单号为原单号
		if (StringUtils.isBlank(procedurePlanDO.getOriginalPlanNo())) {
			newProcedurePlanDO.setOriginalPlanNo(oldWorkOrderNo);
		}
		newProcedurePlanDO.setWorkOrderNo(procedurePlanDO.getWorkOrderNo() + "-" + (count + 1));
		newProcedurePlanDO.setStatus(ConstantForMES.PLAN);
		this.save(newProcedurePlanDO);

		// 保存拆分后的工序计划工序列表
		Map<String, Object> checkParam;
		Long newProcedurePlanId = newProcedurePlanDO.getId();
		for (int i = 0; i < workingProcedureDetailDOList.size(); i++) {
			WorkingProcedureDetailDO procedureDetailDO = workingProcedureDetailDOList.get(i);
			Long oldProcedureDetailId = procedureDetailDO.getId();
			procedureDetailDO.setPlanId(newProcedurePlanId);
			procedureDetailDO.setPlanCount(splitCountList.get(i));
			Long beforeSplitId = procedureDetailDO.getBeforeSplitId();
			if (Objects.isNull(beforeSplitId)) {
				procedureDetailDO.setBeforeSplitId(oldProcedureDetailId);
			}
			procedureDetailDO.setIsDispatching(ConstantForMES.PLAN);
			procedureDetailDO.setAlreadyCount(BigDecimal.ZERO);
			detailService.save(procedureDetailDO);

			// 查看该工序有无检验配置
			checkParam = Maps.newHashMap();
			checkParam.put("foreignId", Objects.isNull(beforeSplitId) ? oldProcedureDetailId : beforeSplitId);
			checkParam.put("type", ConstantForMES.GXJH_GYLX);
			List<ProcessCheckDO> checkList = checkService.list(checkParam);
			// 若有将原单的配置复制一份保存
			if (checkList.size() > 0) {
				for (ProcessCheckDO processCheckDO : checkList) {
					processCheckDO.setForeignId(procedureDetailDO.getId());
					checkService.save(processCheckDO);
				}
			}

		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("id", newProcedurePlanId);
		return R.ok(result);
	}

	@Override
	public R concatPlan(Long[] ids) {
		if (ids.length < 2) {
            return R.error(messageSourceHandler.getMessage("plan.count.nonEnough.concat",null));
		}
		List<Long> idList = Arrays.asList(ids);
		Collections.sort(idList);
        Long mainId = idList.get(0);
        // 获取原单据的工序ID
		Map<String, Object> params = Maps.newHashMap();
		params.put("planId", mainId);
		List<WorkingProcedureDetailDO> mainList = detailService.list(params);
		if (mainList.size() > 0) {
//			Map<String, Object> param = null;
			List<Long> arrayList = Lists.newArrayList();
			for (WorkingProcedureDetailDO mainDetailDO : mainList) {
				arrayList.add(mainDetailDO.getId());
			}
			
			params.clear();
			params.put("idList", arrayList);
			List<WorkingProcedureDetailDO> list = detailService.list(params);
			for (WorkingProcedureDetailDO mainDetailDO : mainList) {
				BigDecimal mainPlanCount = mainDetailDO.getPlanCount();
				for (WorkingProcedureDetailDO childDetailDO : list) {
					BigDecimal alreadyCount = Objects.isNull(childDetailDO.getAlreadyCount()) ? BigDecimal.ZERO
							: childDetailDO.getAlreadyCount();
					if (alreadyCount.compareTo(BigDecimal.ZERO) > 0) {
                        return R.error(messageSourceHandler.getMessage("plan.child.concat",null));
					}
					if (Objects.equals(mainDetailDO.getId(),childDetailDO.getBeforeSplitId() )) {
						mainPlanCount = mainPlanCount.add(childDetailDO.getPlanCount());
					}
				}
				mainDetailDO.setPlanCount(mainPlanCount);
			}
			
			// 将拆分后的数据还原到主单据中
			for (WorkingProcedureDetailDO workingProcedureDetailDO : mainList) {
				detailService.update(workingProcedureDetailDO);
			}

			// 删除子单据
            for (Long id : idList) {
                if (Objects.equals(id,mainId)){
                    continue;
                }
                this.removeHeadAndBody(id);
            }
		}
		return R.ok();
	}

	@Override
	public R getConcatPlanList(Long id) {
		WorkingProcedurePlanDO procedurePlanDO = this.get(id);
		Map<String, Object> result = Maps.newHashMap();
		String originalPlanNo = procedurePlanDO.getOriginalPlanNo();
		Map<String, Object> params = Maps.newHashMap();
		// 查看该单据是否为母单据
		if (StringUtils.isEmpty(originalPlanNo)) {
			// 为母单据
			params.put("id", id);
			params.put("offset", 0);
			params.put("limit", 1);
		}
		if (StringUtils.isNoneEmpty(originalPlanNo)) {
			// 不为母单据
			params.put("workOrderNo", originalPlanNo);
			params.put("offset", 0);
			params.put("limit", 1);
		}
		List<Map<String, Object>> planList = this.listForMap(params);

		// 母单据查询下列的子单据
		params.clear();
		params.put("originalPlanNo",
				StringUtils.isEmpty(originalPlanNo) ? procedurePlanDO.getWorkOrderNo() : originalPlanNo);
		List<Map<String, Object>> childList = this.listForMap(params);
		if (planList.size() > 0 && childList.size() > 0) {
			for (Map<String, Object> map : childList) {
                if (!Objects.equals(map.get("planStatusId").toString(), ConstantForMES.PLAN.toString())) {
                    return R.error(messageSourceHandler.getMessage("plan.child.concat",null));
                }
            }
            planList.addAll(childList);
            result.put("data", planList);
		}
		return R.ok(result);
	}

	@Override
	public int dispatchItemCount(Map<String, Object> param) {
		return workingProcedurePlanDao.dispatchItemCount(param);
	}

	@Override
	public List<Map<String, Object>> dispatchItemList(Map<String, Object> param) {
		return workingProcedurePlanDao.dispatchItemList(param);
	}

	@Override
	public R delete(Long id) {
		WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
		// 查看是否为拆分出的单据
		if (StringUtils.isNoneEmpty(workingProcedurePlanDO.getOriginalPlanNo())) {
            return R.error(messageSourceHandler.getMessage("plan.isChild.delete.disabled",null));
		}

		// 查询是否具有子单据
		Map<String, Object> params = Maps.newHashMap();
		params.put("originalPlanNo", workingProcedurePlanDO.getWorkOrderNo());
		List<WorkingProcedurePlanDO> planList = this.list(params);
		if (planList.size() > 0) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
		}

		// 验证工序下是否有对应的工单
		params.clear();
		params.put("planId", id);
		List<WorkingProcedureDetailDO> detailList = detailService.list(params);
		if (detailList.size() > 0) {
			for (WorkingProcedureDetailDO detailDO : detailList) {
				if (Objects.nonNull(detailDO.getRealityStartTime())) {
                    return R.error(messageSourceHandler.getMessage("plan.startWork.delete.disabled",null));
				}
			}
		}

		this.removeHeadAndBody(id);
		return R.ok();
	}

	@Override
	public R batchDelete(Long[] ids) {
		for (Long id : ids) {
			WorkingProcedurePlanDO workingProcedurePlanDO = this.get(id);
			// 查看是否为拆分出的单据 是否有生产计划关联
			if (StringUtils.isNoneEmpty(workingProcedurePlanDO.getOriginalPlanNo())||Objects.nonNull(workingProcedurePlanDO.getProductionPlanId())) {
                return R.error(messageSourceHandler.getMessage("plan.isChild.delete.disabled",null));
			}

			// 查询是否具有子单据
			Map<String, Object> params = Maps.newHashMap();
			params.put("originalPlanNo", workingProcedurePlanDO.getWorkOrderNo());
			List<WorkingProcedurePlanDO> planList = this.list(params);
			if (planList.size() > 0) {
                return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
			}

			// 验证工序下是否有对应的工单
			params.clear();
			params.put("planId", id);
			List<WorkingProcedureDetailDO> detailList = detailService.list(params);
			if (detailList.size() > 0) {
				for (WorkingProcedureDetailDO detailDO : detailList) {
					if (Objects.nonNull(detailDO.getRealityStartTime())) {
                        return R.error(messageSourceHandler.getMessage("plan.startWork.delete.disabled",null));
					}
				}
			}
		}
		for (Long id : ids) {
			this.removeHeadAndBody(id);
		}
		return R.ok();
	}

	@Override
	public int edit(WorkingProcedurePlanDO planDO, String childArray, Long[] projectIds, Long[] checkProjectIds) {
		int update = this.update(planDO);
		if (StringUtils.isEmpty(childArray)) {
			return update;
		}
		if (projectIds.length > 0) {
			detailService.batchRemove(projectIds);
		}
		if (checkProjectIds.length > 0) {
			checkService.batchRemove(checkProjectIds);
		}
		if (update > 0) {
            JSONArray jsonArray = JSON.parseArray(childArray);
			List<WorkingProcedureDetailDO> parseArray = JSON.parseArray(childArray, WorkingProcedureDetailDO.class);

			// SOP 文件先删后增
			Long[] detailIds = parseArray
					.stream()
					.map(WorkingProcedureDetailDO::getId).toArray(Long[]::new);
			if (detailIds.length > 0) {
				contentAssocService.removeByAssocIdAndType(detailIds, ConstantForMES.SOP_FILE);
			}

			ProcessCheckDO checkDO;
			Long planId = planDO.getId();
			for (int i = 0; i < parseArray.size(); i++) {
				WorkingProcedureDetailDO detailDO = parseArray.get(i);
				Long id = detailDO.getId();
				if (id == null) {
					detailDO.setPlanId(planId);
					detailService.save(detailDO);
				}
				if (id != null) {
					detailService.update(detailDO);
				}
				Long detailId = detailDO.getId();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (detailDO.getIsExamine() == 1) {
					JSONArray array = jsonObject.containsKey("pro")?jsonObject.getJSONArray("pro"):null;
					if (Objects.isNull(array)) {
						continue;
					}
					for (int j = 0; j < array.size(); j++) {
						JSONObject jsonObject2 = array.getJSONObject(j);
						checkDO = new ProcessCheckDO();
						checkDO.setForeignId(detailId);
						checkDO.setType(ConstantForMES.GXJH_GYLX);
						checkDO.setProId(jsonObject2.getLong("proId"));
						checkDO.setRemark(jsonObject2.getOrDefault("remark","").toString());
						checkDO.setWhetherCheck(Integer.parseInt(jsonObject2.getOrDefault("whetherCheck",0) .toString()));
						long checkId = Long.parseLong(jsonObject2.getOrDefault("id",0L).toString());
						if (checkId == 0L) {
							checkService.save(checkDO);
							continue;
						}
						checkDO.setId(checkId);
						checkService.update(checkDO);
					}
				}

				// sop上传
				JSONArray sopArray = jsonObject.containsKey("uploadAttachment")?jsonObject.getJSONArray("uploadAttachment"):null;
				if (Objects.nonNull(sopArray)) {
					contentAssocService.saveList(detailId, sopArray, ConstantForMES.SOP_FILE);
				}
			}
		}
		return update;
	}

	@Override
	public void removeHeadAndBody(Long id) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(2);
		List<Map<String, Object>> listByPlanId = detailService.listByPlanId(id);
		List<Long> ids = new ArrayList<>();
		if (listByPlanId.size() > 0) {
			for (Map<String, Object> map : listByPlanId) {
				ids.add(Long.parseLong(map.get("id").toString()));
			}
			Long[] detailIds = ids.toArray(new Long[0]);
			param.put("foreignId", detailIds);
			param.put("type", ConstantForMES.GXJH_GYLX);
			checkService.removeBacthByforeignId(param);

			// 删除SOP文件
			contentAssocService.removeByAssocIdAndType(detailIds, ConstantForMES.SOP_FILE);

		}
		this.remove(id);
		detailService.removeByHeadId(id);
		reworkRepairMiddleService.removeByPlanId(id);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return workingProcedurePlanDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return workingProcedurePlanDao.countForMap(params);
	}

}
