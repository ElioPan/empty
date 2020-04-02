package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.dao.UpkeepPlanDao;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@EnableTransactionManagement
@Service
public class UpkeepPlanServiceImpl implements UpkeepPlanService {
    @Autowired
    private UpkeepPlanDao upkeepPlanDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UpkeepProjectService upkeepProjectService;
    @Autowired
    private UpkeepPlanProjectService upkeepPlanProjectService;
    @Autowired
    private SparePartService sparePartService;
    @Autowired
    private UpkeepPlanPartService upkeepPlanPartService;
    @Autowired
    private UpkeepRecordService upkeepRecordService;
    @Autowired
    private UpkeepRecordPartService upkeepRecordPartService;
    @Autowired
    private UpkeepRecordProjectService upkeepRecordProjectService;
    @Autowired
    private UpkeepCheckService upkeepCheckService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public UpkeepPlanDO get(Long id) {
        return upkeepPlanDao.get(id);
    }

    @Override
    public List<UpkeepPlanDO> list(Map<String, Object> map) {
        return upkeepPlanDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return upkeepPlanDao.count(map);
    }

    @Override
    public int save(UpkeepPlanDO upkeepPlan) {
        return upkeepPlanDao.save(upkeepPlan);
    }

    @Override
    public int update(UpkeepPlanDO upkeepPlan) {
        return upkeepPlanDao.update(upkeepPlan);
    }

    @Override
    public int remove(Long id) {
        return upkeepPlanDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return upkeepPlanDao.batchRemove(ids);
    }

    @Override
    public Map<String, Object> planListForDevice(int pageno, int pagesize, Long deviceId) {
        Map<String, Object> userMap = this.userService.getUserMap();
        Map<String, Object> deviceMap = this.deviceService.getDeviceMap();
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceId", deviceId);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        List<UpkeepPlanDO> plans = upkeepPlanDao.list(params);
        Long total = (long) upkeepPlanDao.count(params);
        List<Map<String, Object>> datas = new ArrayList<>();
        if (plans != null && plans.size() > 0) {
            for (UpkeepPlanDO plan : plans) {
                Map map = new HashMap();
                map.put("id", plan.getId());
                map.put("workOrderno", plan.getWorkOrderno() == null ? "" : plan.getWorkOrderno());
                map.put("name", plan.getName() == null ? "" : plan.getName());
                map.put("deviceName", plan.getDeviceId() == null ? "" : deviceMap.get(plan.getDeviceId().toString()));
                map.put("engineer", plan.getEngineerId() == null ? "" : userMap.get(plan.getEngineerId().toString()));
                map.put("startTime", plan.getStartTime() == null ? "" : plan.getStartTime());
                map.put("endTime", plan.getEndTime() == null ? "" : plan.getEndTime());
                map.put("gmtTime", plan.getCreateTime() == null ? "" : plan.getCreateTime());
                map.put("status", plan.getStatus() == null ? "" : plan.getStatus());
                datas.add(map);
            }
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total.intValue());
            dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return results;
    }

    @Override
    public Map<String, Object> getPlanDetail(Long id) {
        Map<String, Object> results = Maps.newHashMap();
        UpkeepPlanDO plan = this.upkeepPlanDao.get(id);
        if (plan != null) {
            results.put("plan", plan);
            UpkeepRecordDO record = this.upkeepRecordService.getByPlanId(plan.getId());
            if (record != null) {
                results.put("record", record);
                Map<String, Object> projectParams = new HashMap<String, Object>();
                projectParams.put("recordId", record.getId());
                List<UpkeepPlanProjectDO> projectList = this.upkeepPlanProjectService.list(projectParams);
                if (projectList != null && projectList.size() > 0) {
                    JSONArray projectArray = new JSONArray();
                    for (UpkeepPlanProjectDO recordProject : projectList) {
                        UpkeepProjectDO project = this.upkeepProjectService.get(recordProject.getProjectId());
                        JSONObject projectObject = new JSONObject();
                        projectObject.put("code", project.getCode());
                        projectObject.put("name", project.getName());
                        projectObject.put("function", project.getFunction());
                        projectObject.put("manHour", recordProject.getManhour());
                        projectObject.put("result", recordProject.getResult());
                        projectObject.put("remark", recordProject.getRemark());
//						projectArray.put(projectObject);
                    }
                    results.put("projects", projectArray);
                }
                Map<String, Object> partsParams = new HashMap<String, Object>();
                partsParams.put("recordId", record.getId());
                List<UpkeepPlanPartDO> partsList = this.upkeepPlanPartService.list(projectParams);
                if (partsList != null && partsList.size() > 0) {
                    JSONArray spartsArray = new JSONArray();
                    for (UpkeepPlanPartDO planPart : partsList) {
                        SparePartDO part = this.sparePartService.get(planPart.getPartId());
                        JSONObject partsObject = new JSONObject();
                        partsObject.put("code", part.getCode());
                        partsObject.put("name", part.getName());
                        partsObject.put("type", part.getType());
                        partsObject.put("amount", planPart.getAmount());
                        partsObject.put("unit", part.getUnit());
                        partsObject.put("unit", part.getUnit());
//						spartsArray.put(partsObject);
                    }
                    results.put("sparts", spartsArray);
                }
            }
        }
        return results;
    }


    @Override
    public Map<String, Object> savePlan(UpkeepPlanDO planDO, String projectIds, String partIds) {

        //1 将保养计划保存后并获取主键待用   (改造添加计划状态)
        Map<String, Object> results = Maps.newHashMap();

        String prefix = DateFormatUtil.getWorkOrderno(Constant.BYJH, new Date());
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("maxNo", prefix);
        params.put("offset", 0);
        params.put("limit", 1);
        List<UpkeepPlanDO> list = upkeepPlanDao.list(params);
        String suffix = null;
        if (list.size() > 0) {
            suffix = list.get(0).getWorkOrderno();
        }
        planDO.setWorkOrderno(DateFormatUtil.getWorkOrderno(prefix, suffix));
//        planDO.setWorkOrderno("BYJH"+DateFormatUtil.getWorkOrderno());

        int code = upkeepPlanDao.save(planDO);

        if (code > 0) {
            Long id = planDO.getId();
            //2.将计划中选择的保养项目主键和计划主键一起保存至  upkeep_plan_project
            if (projectIds != null && !"".equals(projectIds)) {
                String[] projectArray = projectIds.split(",");
                int saveProjects = 0;

                for (int i = 0; i < projectArray.length; i++) {
                    Long projectId = Long.valueOf(projectArray[i]);
                    UpkeepPlanProjectDO upkeepPlanProjectDao = new UpkeepPlanProjectDO();
                    upkeepPlanProjectDao.setPlanId(id);
                    upkeepPlanProjectDao.setProjectId(projectId);

                    saveProjects += upkeepPlanProjectService.save(upkeepPlanProjectDao);
                }
            }

            //3.将计划中选择的 备件主键和数量 和计划主键一起保存至 upkeep_plan_spart中

            // 备件传入数据的格式：{"dataList":[{"partId":3,"amount":5},{"partId":4,"amount":5}]}
//            {"dataList":[{"partId":3,"amount":5,"spartUnit":"个","spartPrice":20,"spartSum":1000,"remark":"备注"},{"partId":3,"amount":5,"spartUnit":"个","spartPrice":20,"spartSum":1000,"remark":"备注"}]}

            JSONObject argJSON = JSONObject.fromObject(partIds);
            net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(argJSON.get("dataList"));

            int saveParts = 0;

                for (int i = 0; i < jsonArray.size(); i++) {

                    Map<String, Object> mapss = (Map<String, Object>) jsonArray.get(i);
                    UpkeepPlanPartDO upkeepPlanPartDO = new UpkeepPlanPartDO();

                    Long partId = Long.valueOf(String.valueOf( mapss.containsKey("partId")?mapss.get("partId"):null));
//                    int amount =(int)mapss.get("amount");
                    int amount =(int)(mapss.containsKey("amount")?mapss.get("amount"):0);

                    upkeepPlanPartDO.setPlanId(id);
                    upkeepPlanPartDO.setPartId(partId);
                    upkeepPlanPartDO.setAmount(amount);
                    upkeepPlanPartDO.setSpartUnit(mapss.containsKey("spartUnit")?mapss.get("spartUnit").toString():null);
                    upkeepPlanPartDO.setSpartPrice(mapss.containsKey("spartPrice")?Double.parseDouble(mapss.get("spartPrice").toString()):null);
                    upkeepPlanPartDO.setSpartSum(mapss.containsKey("spartSum")?Double.parseDouble(mapss.get("spartSum").toString()):null);
                    upkeepPlanPartDO.setRemark(mapss.containsKey("remark")?mapss.get("remark").toString():null);

                    saveParts += upkeepPlanPartService.save(upkeepPlanPartDO);
                }

        }
        return results;
    }


    @Override
    public Map<String, Object> planListForUser(int pageno, int pagesize, Long userId) {
        Map<String, Object> userMap = this.userService.getUserMap();
        Map<String, Object> deviceMap = this.deviceService.getDeviceMap();
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userId);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        List<UpkeepPlanDO> plans = upkeepPlanDao.list(params);
        Long total = (long) upkeepPlanDao.count(params);
        List<Map<String, Object>> datas = new ArrayList<>();
        if (plans != null && plans.size() > 0) {
            for (UpkeepPlanDO plan : plans) {
                Map map = new HashMap();
                map.put("id", plan.getId());
                map.put("workOrderno", plan.getWorkOrderno() == null ? "" : plan.getWorkOrderno());
                map.put("name", plan.getName() == null ? "" : plan.getName());
                map.put("deviceName", plan.getDeviceId() == null ? "" : deviceMap.get(plan.getDeviceId().toString()));
                map.put("engineer", plan.getEngineerId() == null ? "" : userMap.get(plan.getEngineerId().toString()));
                map.put("startTime", plan.getStartTime() == null ? "" : plan.getStartTime());
                map.put("endTime", plan.getEndTime() == null ? "" : plan.getEndTime());
                map.put("gmtTime", plan.getCreateTime() == null ? "" : plan.getCreateTime());
                map.put("status", plan.getStatus() == null ? "" : plan.getStatus());
                datas.add(map);
            }
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total.intValue());
            dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> getNoticeValidPlanids(Map<String, Object> map) {
        return upkeepPlanDao.getNoticeValidPlanid(map);
    }

    @Override
    public List<Map<String, Object>> getPlanListByUser(Map<String, Object> map) {
        return upkeepPlanDao.getPlanListByUserId(map);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int makeGeneratemMaintenance(List<Map<String, Object>> noticeValidPlanids) throws ParseException {

        Date date = new Date();
        SimpleDateFormat forMat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String upTime = forMat.format(date);
        Date upDates = forMat.parse(upTime);

        for (int i = 0; i < noticeValidPlanids.size(); i++) {
            Map<String, Object> planIds = noticeValidPlanids.get(i);
            Long planId = Long.valueOf(planIds.get("plan_id").toString());
            UpkeepPlanDO upkeepPlanDO = upkeepPlanDao.get(planId);
            //将工单保存record表
            UpkeepRecordDO upkeepRecordDO = new UpkeepRecordDO();
            upkeepRecordDO.setWorkOrderno(DateFormatUtil.getWorkOrderno());
            upkeepRecordDO.setStartTime(forMat.parse(planIds.get("begin_time").toString()));
            upkeepRecordDO.setEndTime(forMat.parse(planIds.get("end_time").toString()));
            upkeepRecordDO.setEngineerId(upkeepPlanDO.getEngineerId());
            upkeepRecordDO.setPlanId(planId);
            upkeepRecordDO.setDeviceId(upkeepPlanDO.getDeviceId());
            upkeepRecordDO.setCreateBy(upkeepPlanDO.getCreateBy());
            upkeepRecordDO.setCreateTime(new Date());
            upkeepRecordDO.setType(upkeepPlanDO.getType());
            upkeepRecordDO.setMessageId(Long.valueOf(planIds.get("id").toString()));
            upkeepRecordDO.setResult(Constant.WAITING_DEAL);//待处理状态

            upkeepRecordService.save(upkeepRecordDO);

            //将工单id保存至check验收表中，状态result 初始为133
            UpkeepCheckDO upkeepCheckDO = new UpkeepCheckDO() ;
                upkeepCheckDO.setResult(Constant.NO_EVALUATED);
                upkeepCheckDO.setRecordId(upkeepRecordDO.getId());

            upkeepCheckService.save(upkeepCheckDO);


            //将备件明细保存在recode_spart表中
            Map<String, Object> planPartD = new HashMap<String, Object>() {{
                put("planId", planId);
            }};
            List<UpkeepPlanPartDO> lists = upkeepPlanPartService.list(planPartD);

            for (UpkeepPlanPartDO upd : lists) {

                UpkeepRecordPartDO upkeepRecordPartDO = new UpkeepRecordPartDO();
                upkeepRecordPartDO.setPlanId(planId);
                upkeepRecordPartDO.setRecordId(upkeepRecordDO.getId());
                upkeepRecordPartDO.setPartId(upd.getPartId());
                upkeepRecordPartDO.setSpartAmount(upd.getAmount().toString());
                upkeepRecordPartDO.setSpartSum(upd.getSpartSum());
                upkeepRecordPartDO.setSpartUnit(upd.getSpartUnit());
                upkeepRecordPartDO.setSpartPrice(upd.getSpartPrice());
                upkeepRecordPartDO.setRemark(upd.getRemark());

                upkeepRecordPartService.save(upkeepRecordPartDO);
            }

            //将保养项目明细保存在recode_project表中

            Map<String, Object> planProD = new HashMap<String, Object>() {{
                put("planId", planId);
            }};
            List<UpkeepPlanProjectDO> listPlanPro = upkeepPlanProjectService.list(planProD);

            for (UpkeepPlanProjectDO updp : listPlanPro) {
                UpkeepRecordProjectDO upkeepRecordProjectDO = new UpkeepRecordProjectDO();
                upkeepRecordProjectDO.setPlanId(planId);
                upkeepRecordProjectDO.setRecordId(upkeepRecordDO.getId());
                upkeepRecordProjectDO.setProjectId(updp.getProjectId());

                int count = upkeepRecordProjectService.save(upkeepRecordProjectDO);
            }
        }
        return 1;
    }

    @Override
    public List<Long> getPlanstatusByEndTime() {
        return upkeepPlanDao.getPlanstatusByEndTime();
    }

    @Override
    public int updateStatus(Long[] ids) {
        return upkeepPlanDao.updateStatus(ids);
    }

    @Override
    public Map<String, Object> getPlanMsgById(Long id) {
        return upkeepPlanDao.getPlanMsgById(id);
    }

    @Override
    public List<Map<String, Object>> getProMsgById(Long id) {
        return upkeepPlanDao.getProMsgById(id);
    }

    @Override
    public List<Map<String, Object>> getPartMsgById(Long id) {
        return upkeepPlanDao.getPartMsgById(id);
    }

    @Override
    public Map<String, Object> predecessorById(Map<String, Object> map) {
        return upkeepPlanDao.predecessorById(map);
    }

    @Override
    public int checkTimeByNoticeId(Map<String, Object> map) {
        return upkeepPlanDao.checkTimeByNoticeId(map);
    }

    @Override
    public List<Map<String, Object>> getNoticeMsg(Map<String, Object> map) {
        return upkeepPlanDao.getNoticeMsg(map);
    }

    @Override
    public int countOfList(Map<String, Object> map) {
        return upkeepPlanDao.countOfList(map);
    }


    @Override
    public void savePlanChangeAndSbmit(UpkeepPlanDO planDO, String projectIds, String partIds, int sign) {

        //保养计划提交时状态为启用：129启用
        if (sign == 1) {
            planDO.setStatus(Constant.STATE_START);
        }

        upkeepPlanDao.update(planDO);

        Long id = planDO.getId();
        if (projectIds != null && !"".equals(projectIds)) {

            //删除修改前所有保养项目
            upkeepPlanProjectService.removeByPlanId(id);

            //2.将计划中选择的保养项目主键和计划主键一起保存至  upkeep_plan_project

            String[] projectArray = projectIds.split(",");

            for (int i = 0; i < projectArray.length; i++) {
                Long projectId = Long.valueOf(projectArray[i]);
                UpkeepPlanProjectDO upkeepPlanProjectDao = new UpkeepPlanProjectDO();
                upkeepPlanProjectDao.setPlanId(id);
                upkeepPlanProjectDao.setProjectId(projectId);

                upkeepPlanProjectService.save(upkeepPlanProjectDao);
            }
        }

        //3.将计划中选择的 备件主键和数量 和计划主键一起保存至 upkeep_plan_spart中
        if (partIds != null && !"".equals(partIds)) {
            //删除之前所有的备件信息
            upkeepPlanPartService.removePartByPlanId(id);
            // 备件传入数据的格式：{"dataList":[{"partId":3,"amount":5},{"partId":4,"amount":5}]}
            JSONObject argJSON = JSONObject.fromObject(partIds);
            net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(argJSON.get("dataList"));
            int saveParts = 0;

            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> mapss = (Map<String, Object>) jsonArray.get(i);

                UpkeepPlanPartDO upkeepPlanPartDO = new UpkeepPlanPartDO();

                Long partId = Long.valueOf(String.valueOf( mapss.containsKey("partId")?mapss.get("partId"):null));
//                    int amount =(int)mapss.get("amount");
                int amount =(int)(mapss.containsKey("amount")?mapss.get("amount"):0);

                upkeepPlanPartDO.setPlanId(id);
                upkeepPlanPartDO.setPartId(partId);
                upkeepPlanPartDO.setAmount(amount);
                upkeepPlanPartDO.setSpartUnit(mapss.containsKey("spartUnit")?mapss.get("spartUnit").toString():null);
                upkeepPlanPartDO.setSpartPrice(mapss.containsKey("spartPrice")?Double.parseDouble(mapss.get("spartPrice").toString()):null);
                upkeepPlanPartDO.setSpartSum(mapss.containsKey("spartSum")?Double.parseDouble(mapss.get("spartSum").toString()):null);
                upkeepPlanPartDO.setRemark(mapss.containsKey("remark")?mapss.get("remark").toString():null);

                saveParts += upkeepPlanPartService.save(upkeepPlanPartDO);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeWorkOrder(Long planId, Date startTime, Date endTime) {

        UpkeepPlanDO upkeepPlanDO = upkeepPlanDao.get(planId);
        //将工单保存record表
        UpkeepRecordDO upkeepRecordDO = new UpkeepRecordDO();

        String prefix = DateFormatUtil.getWorkOrderno(Constant.SBBY, startTime);
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("maxNo", prefix);
        params.put("offset", 0);
        params.put("limit", 1);
        List<UpkeepRecordDO> list = upkeepRecordService.list(params);
        String suffix = null;
        if (list.size() > 0) {
            suffix = list.get(0).getWorkOrderno();
        }
        upkeepRecordDO.setWorkOrderno(DateFormatUtil.getWorkOrderno(prefix, suffix));
//        upkeepRecordDO.setWorkOrderno(DateFormatUtil.getWorkOrderno());

        upkeepRecordDO.setStartTime(startTime);
        upkeepRecordDO.setEndTime(endTime);
        upkeepRecordDO.setManHour(0.00);  //工时初始为0.0
        upkeepRecordDO.setCost(new BigDecimal(0.00));
        upkeepRecordDO.setEngineerId(upkeepPlanDO.getEngineerId());
        upkeepRecordDO.setPlanId(planId);
        upkeepRecordDO.setDeviceId(upkeepPlanDO.getDeviceId());
        upkeepRecordDO.setName(upkeepPlanDO.getName());
        upkeepRecordDO.setCreateBy(upkeepPlanDO.getCreateBy());
        upkeepRecordDO.setCreateTime(new Date());
        upkeepRecordDO.setType(upkeepPlanDO.getType());
        upkeepRecordDO.setCost(new BigDecimal(0));
        upkeepRecordDO.setManHourCost(new BigDecimal(0));
//            upkeepRecordDO.setMessageId(Long.valueOf(planIds.get("id").toString()));

        upkeepRecordDO.setResult(Constant.WAITING_DEAL);//待处理状态

        upkeepRecordService.save(upkeepRecordDO);

        //将工单id保存至check验收表中，状态result 初始为133
        UpkeepCheckDO upkeepCheckDO = new UpkeepCheckDO();
        upkeepCheckDO.setResult(Constant.NO_EVALUATED);
        upkeepCheckDO.setRecordId(upkeepRecordDO.getId());

        upkeepCheckService.save(upkeepCheckDO);

        //将备件明细保存在recode_spart表中
        Map<String, Object> planPartD = new HashMap<String, Object>() {{
            put("planId", planId);
        }};
        List<UpkeepPlanPartDO> lists = upkeepPlanPartService.list(planPartD);

        for (UpkeepPlanPartDO upd : lists) {

            UpkeepRecordPartDO upkeepRecordPartDO = new UpkeepRecordPartDO();
            upkeepRecordPartDO.setPlanId(upd.getPlanId());

            upkeepRecordPartDO.setRecordId(upkeepRecordDO.getId());
            upkeepRecordPartDO.setPartId(upd.getPartId());
            upkeepRecordPartDO.setSpartAmount(upd.getAmount().toString());
            upkeepRecordPartDO.setSpartSum(upd.getSpartSum());
            upkeepRecordPartDO.setSpartUnit(upd.getSpartUnit());
            upkeepRecordPartDO.setSpartPrice(upd.getSpartPrice());
            upkeepRecordPartDO.setRemark(upd.getRemark());

            upkeepRecordPartService.save(upkeepRecordPartDO);
        }

        //将保养项目明细保存在recode_project表中

        Map<String, Object> planProD = new HashMap<String, Object>() {{
            put("planId", planId);
        }};
        List<UpkeepPlanProjectDO> listPlanPro = upkeepPlanProjectService.list(planProD);

        for (UpkeepPlanProjectDO updp : listPlanPro) {
            UpkeepRecordProjectDO upkeepRecordProjectDO = new UpkeepRecordProjectDO();
            upkeepRecordProjectDO.setPlanId(updp.getPlanId());
            upkeepRecordProjectDO.setRecordId(upkeepRecordDO.getId());
            upkeepRecordProjectDO.setManHourCost(new BigDecimal(0));
            upkeepRecordProjectDO.setManhour(0);
            upkeepRecordProjectDO.setProjectId(updp.getProjectId());

            int count = upkeepRecordProjectService.save(upkeepRecordProjectDO);
        }
    }

    @Override
    public int deletOfPlan(Map<String, Object> map) {
        return upkeepPlanDao.deletOfPlan( map);
    }

    @Override
    public int canChangeStatus(Map<String, Object> map) {
        return upkeepPlanDao.canChangeStatus(map);
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
            UpkeepPlanDO updatePlanDO=new UpkeepPlanDO();
            updatePlanDO.setStatus(Constant.STATE_START);
            updatePlanDO.setId(id);
            this.update(updatePlanDO);
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
            UpkeepPlanDO updatePlanDO=new UpkeepPlanDO();
            updatePlanDO.setStatus(Constant.FORBIDDEN);
            updatePlanDO.setId(id);
            this.update(updatePlanDO);
        }
        return  R.ok();
    }



}
