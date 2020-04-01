package com.ev.custom.service.impl;

import com.ev.common.vo.PlanVo;
import com.ev.custom.dao.PatrolPlanDao;
import com.ev.custom.domain.PatrolPlanDO;
import com.ev.custom.domain.PatrolPlanDetailDO;
import com.ev.custom.domain.PatrolProjectDO;
import com.ev.custom.service.PatrolPlanDetailService;
import com.ev.custom.service.PatrolPlanService;
import com.ev.custom.service.PatrolProjectService;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatrolPlanServiceImpl implements PatrolPlanService {
	@Autowired
	private PatrolPlanDao patrolPlanDao;
	@Autowired
	private PatrolPlanDetailService patrolPlanDetailService;
	@Autowired
	private PatrolProjectService patrolProjectService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Override
	public PatrolPlanDO get(Long id) {
		return patrolPlanDao.get(id);
	}

	@Override
	public List<PatrolPlanDO> list(Map<String, Object> map) {
		return patrolPlanDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return patrolPlanDao.count(map);
	}

	@Override
	public int save(PatrolPlanDO patrolPlan) {
		return patrolPlanDao.save(patrolPlan);
	}

	@Override
	public int update(PatrolPlanDO patrolPlan) {
		return patrolPlanDao.update(patrolPlan);
	}

	@Override
	public int remove(Long id) {
		return patrolPlanDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return patrolPlanDao.batchRemove(ids);
	}

	@Override
	public int canChangeStatus(Map<String, Object> map) {
		return patrolPlanDao.canChangeStatus(map);
	}

	@Override
	public Map<String, Object> addPlan(PatrolPlanDO plan, String detailArray) {
		Map<String, Object> results = Maps.newHashMap();
		JSONArray jsonArray = new JSONArray(detailArray);
		for (int j = 0; j < jsonArray.length(); j++) {
			JSONObject jsonObject = jsonArray.getJSONObject(j);
			JSONArray projectId = jsonObject.getJSONArray("projectId");
			if ("[null]".equals(projectId.toString())||"[]".equals(projectId.toString())) {
				return results; 
			}
		}
		Long planId = plan.getId();
		int code = 0;
		if(Objects.isNull(planId)) {
			//获取编号
			String maxNo = DateFormatUtil.getWorkOrderno(Constant.XJJH);
			Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("maxNo", maxNo);
			param.put("offset", 0);
			param.put("limit", 1);
			List<PatrolPlanDO> list = this.list(param);
			String taskNo = null;
			if (list.size()>0) {
				taskNo = list.get(0).getWorkOrderno();
			}
			plan.setWorkOrderno(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
			// 设置创建人
			code = patrolPlanDao.save(plan);
			results .put("id", plan.getId());
		}
		if (Objects.nonNull(planId)) {
			patrolPlanDetailService.remove(planId);
			results .put("id", planId);
			code = patrolPlanDao.update(plan);
		}
		if (code > 0) {
			// 关联巡检明细项
			for (int j = 0; j < jsonArray.length(); j++) {
				PatrolPlanDetailDO detail = new PatrolPlanDetailDO();
				JSONObject jsonObject = jsonArray.getJSONObject(j);
				Long deviceId = jsonObject.optLong("deviceId");
				JSONArray projectId = jsonObject.getJSONArray("projectId");
				detail.setLinkDeviceId(deviceId);
				detail.setLinkPatrolPlanId(plan.getId());
				detail.setLinkProjectId(projectId.toString());
				this.patrolPlanDetailService.save(detail);
			}
		}
		return results;
	}

	@Override
	public List<Map<String, Object>> planList(Map<String, Object> params) {
		return this.patrolPlanDao.listForMap(params);
	}

	@Override
	public Map<String, Object> planDetail(Long id) {
		Map<String, Object> results = Maps.newHashMap();
		Map<String, Object> patrolplan = this.patrolPlanDao.getDetailInfo(id);
		if (patrolplan != null) {
			results.put("patrolplan", patrolplan);
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
			param.put("linkPatrolPlanId", id);
			List<Map<String, Object>> patrolPlanDetail = this.patrolPlanDetailService.listForMap(param);
			for (Map<String, Object> map : patrolPlanDetail) {
				List<PatrolProjectDO> patrolProject = new ArrayList<>();
				String linkProjectId = map.get("linkProjectId").toString();
				String first = "[";
				String last = "]";
				linkProjectId = linkProjectId.substring(linkProjectId.indexOf(first) + 1, linkProjectId.indexOf(last));
				if (linkProjectId.contains(",")) {
					String[] split = linkProjectId.split(",");
                    for (String s : split) {
                        PatrolProjectDO patrolProjectDO = patrolProjectService.get(Long.parseLong(s));
                        patrolProject.add(patrolProjectDO);
                    }
					map.put("patrolProject", patrolProject);
					continue;
				}
				patrolProject.add(patrolProjectService.get(Long.parseLong(linkProjectId)));
				map.put("patrolProject", patrolProject);
			}
			results.put("patrolPlanDetail", patrolPlanDetail);
		}
		return results;
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return this.patrolPlanDao.countForMap(params);
	}

	@Override
	public List<Map<String, Object>> getNoticeList(Long deptId, Long userId, Integer checkType) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("deptId", deptId);
		params.put("userId", userId);
		if (checkType != null) {
			if (checkType == 1) {
				params.put("createBy", userId);// 我发起的
			} else {
				params.put("manage", userId);// 我办理的
			}
		}
		params.put("planType", Constant.PATRAL_PLAN);
		params.put("nowTime", DateFormatUtil.getFormateDate(new Date()));
		return this.patrolPlanDao.getNoticeList(params);
	}

	@Override
	public List<PlanVo> getPlanView(Map<String, Object> map) {
		return patrolPlanDao.getPlanView(map);
	}

	@Override
	public Map<String, Object> params(Map<String, Object> params) {
		if (params.get("checkType") != null) {
			Long userId = params.get("userId") == null ? null : Long.parseLong(params.get("userId").toString());
			if (Integer.parseInt(params.get("checkType").toString()) == 1) {
				params.put("createBy", userId);// 我发起的
			} else {
				params.put("manage", userId);// 我办理的
			}
		}
		return params;
	}

	@Override
	public int getNoticeListCount(Map<String, Object> params) {
		params.put("planType", Constant.PATRAL_PLAN);
		params.put("nowTime", DateFormatUtil.getFormateDate(new Date()));
		return patrolPlanDao.getNoticeListCount(params);
	}


	@Override
	public R disposeStartUsing(Long[]ids){
		Map<String,Object>  map= new HashMap<>();
		map.put("ids",ids);
		map.put("status",Constant.STATE_STOP_OVER);
		int counts= this.canChangeStatus(map);
		if(counts>0){
			return R.error(messageSourceHandler.getMessage("scm.plan.statusIsOver.prohibitToEnable",null));
		}
		map.put("status",Constant.FORBIDDEN);
		map.put("endTime",1);
		int rows= this.canChangeStatus(map);
		if(rows>0){
			return R.error(messageSourceHandler.getMessage("scm.plan.statusIsOver.timeISOver",null));
		}
		for(Long id :ids){
			PatrolPlanDO patrolPlanDO=new PatrolPlanDO();
			patrolPlanDO.setStatus(Constant.STATE_START);
			this.update(patrolPlanDO);
		}
		return  R.ok();
	}

	@Override
	public R disposeForbidden(Long[]ids){

		Map<String,Object>  map= new HashMap<>();
		map.put("ids",ids);
		map.put("status",Constant.STATE_STOP_OVER);
		int counts= this.canChangeStatus(map);
		if(counts>0){
			return R.error(messageSourceHandler.getMessage("scm.plan.statusIsOver.prohibitToDisable",null));
		}
		for(Long id :ids){
			PatrolPlanDO patrolPlanDO=new PatrolPlanDO();
			patrolPlanDO.setStatus(Constant.FORBIDDEN);
			this.update(patrolPlanDO);
		}
		return  R.ok();
	}





}
