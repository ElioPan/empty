package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.mes.dao.DispatchItemDao;
import com.ev.mes.domain.DispatchItemDO;
import com.ev.mes.domain.DispatchWorkingHungDO;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.service.DispatchItemService;
import com.ev.mes.service.DispatchWorkingHungService;
import com.ev.mes.service.ProcessReportService;
import com.ev.mes.service.WorkingProcedureDetailService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DispatchItemServiceImpl implements DispatchItemService {
    @Autowired
    private DispatchItemDao dispatchItemDao;
    @Autowired
    private WorkingProcedureDetailService workingProcedureDetailService;
    @Autowired
    private ContentAssocService contentAssocService;
    @Autowired
    private DispatchWorkingHungService dispatchWorkingHungService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private  ProcessReportService processReportService;

    @Override
    public DispatchItemDO get(Long id) {
        return dispatchItemDao.get(id);
    }

    @Override
    public List<DispatchItemDO> list(Map<String, Object> map) {
        return dispatchItemDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dispatchItemDao.count(map);
    }

    @Override
    public int save(DispatchItemDO dispatchItem) {
        return dispatchItemDao.save(dispatchItem);
    }

    @Override
    public int update(DispatchItemDO dispatchItem) {
        return dispatchItemDao.update(dispatchItem);
    }

    @Override
    public int remove(Long id) {
        return dispatchItemDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dispatchItemDao.batchRemove(ids);
    }

    @Override
    public List<Map<String, Object>> listForMapOfPlan(Map<String, Object> map) {
        return dispatchItemDao.listForMapOfPlan(map);
    }

    @Override
    public int countForListMapOfPlan(Map<String, Object> map) {
        return dispatchItemDao.countForListMapOfPlan(map);
    }

    @Override
    public Map<String, Object> sendOfButton(Long planItemId) {
        return dispatchItemDao.sendOfButton(planItemId);
    }

    @Override
    public R disposeSbmint(Long id, String dispatchDetal) {
        List<DispatchItemDO> dispatchItemDOs = JSON.parseArray(dispatchDetal, DispatchItemDO.class);
        if (dispatchItemDOs.size() > 0) {

            BigDecimal countCheck = BigDecimal.ZERO;
            WorkingProcedureDetailDO workingProcedureDetailDO = workingProcedureDetailService.get(id);
            if(Objects.nonNull(workingProcedureDetailDO)) {
                for (DispatchItemDO dispatchItemDO : dispatchItemDOs) {
                    countCheck = countCheck.add(dispatchItemDO.getPlanCount());
                }
                    int result=countCheck.compareTo(workingProcedureDetailDO.getPlanCount().subtract(workingProcedureDetailDO.getAlreadyCount()));
                if(result==1){
                    return R.error(messageSourceHandler.getMessage("apis.mes.dispatchCount.overCount",null));
                }
            }else{
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatchCount.haveNothingOfDetail",null));
            }

//            BigDecimal dd = BigDecimal.ZERO;
            for (DispatchItemDO dispatchItemDO : dispatchItemDOs) {

//                dd = dd.add(dispatchItemDO.getPlanCount());

                String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.DISPATCH_JHGD, new Date());
                Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
                params.put("maxNo", prefix);
                params.put("offset", 0);
                params.put("limit", 1);
                List<DispatchItemDO> list = dispatchItemDao.list(params);
                String suffix = null;
                if (list.size() > 0) {
                    suffix = list.get(0).getCode();
                }
                dispatchItemDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
                dispatchItemDO.setForiegnId(id);
                dispatchItemDO.setCompletionCount(new BigDecimal(0));//完工数量
                dispatchItemDO.setStatus(ConstantForMES.AWAITING_DELIVERY); // 待产 249
                dispatchItemDao.save(dispatchItemDO);
            }

            this.addAndPlus(id,countCheck, 1);

            return R.ok();
        } else {
            //"所传工单明细为空！！"
            return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.isNonDetail",null));
        }
    }

    @Override
    public R deletByIds(Long[] ids) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("id", ids);
        //验证是否存在开工记录
        int lines = dispatchItemDao.canDeletOfDispatch(params);
        if (lines ==0) {
            for (int i = 0; i < ids.length; i++) {
                DispatchItemDO dispatchItemDO = dispatchItemDao.get(ids[i]);
                if(dispatchItemDO!=null){
                    this.addAndPlus(ids[i], dispatchItemDO.getPlanCount(), 0);
                    dispatchItemDao.remove(ids[i]);
                }else{
                    return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
                }
            }
            return R.ok();
        } else {
            //"存在开工记录删除失败！"
            return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.failsDelet",null));
        }
    }

    @Override
    public List<Map<String, Object>> listForMapOfDispatch(Map<String, Object> map) {
        return dispatchItemDao.listForMapOfDispatch(map);
    }

    @Override
    public int countForMapOfDispatch(Map<String, Object> map) {
        return dispatchItemDao.countForMapOfDispatch(map);
    }

    @Override
    public Map<String, Object> rollBackOfDetail(Map<String, Object> map) {
        return dispatchItemDao.rollBackOfDetail(map);
    }


    /*
    /将以分工数量反写至工序计划
     */
    public void addAndPlus(Long id, BigDecimal counts, int sign) {
        WorkingProcedureDetailDO workDetailDO = workingProcedureDetailService.get(id);

        WorkingProcedureDetailDO detalDO = new WorkingProcedureDetailDO();
        detalDO.setId(id);

        if (workDetailDO != null && sign == 0) {
            //已派数量做减法
            detalDO.setAlreadyCount(workDetailDO.getAlreadyCount().subtract(counts));
            workingProcedureDetailService.update(detalDO);
        } else if (workDetailDO != null && sign == 1) {

            detalDO.setAlreadyCount(workDetailDO.getAlreadyCount().add(counts));
            workingProcedureDetailService.update(detalDO);
        }
    }

    @Override
    public R saveRollBackOfCount(Long dispatchid, String countOfRollBack) {

        DispatchItemDO dispatchItemDO = dispatchItemDao.get(dispatchid);
        if (Objects.nonNull(dispatchItemDO)) {
            BigDecimal rollback = new BigDecimal(countOfRollBack);
            int result = rollback.compareTo(dispatchItemDO.getPlanCount().subtract(dispatchItemDO.getCompletionCount()));

            if (result == -1 || result == 0) {
                //允许退回
                DispatchItemDO disItemDO = new DispatchItemDO();
                disItemDO.setPlanCount(dispatchItemDO.getPlanCount().subtract(rollback));
                disItemDO.setId(dispatchid);
                dispatchItemDao.update(disItemDO);
                this.addAndPlus(dispatchItemDO.getForiegnId(), rollback, 0);
                return R.ok();
            } else {
                //不允许退回
               // "退回数量大于工单分配数量！"
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.erroCompare",null));
            }
        } else {
            //"此id下无数据！"
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }

    }

    //挂起接口
    @Override
    public int changeOfDiapatchStatus(Long[] foriegnIds) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("status", ConstantForMES.PUT_UP);
        params.put("id", foriegnIds);
        return dispatchItemDao.changeOfDiapatchStatus(params);

    }


    @Override
    public R closeDispatch(Long[] dispatchId) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("id", dispatchId);
        int i = dispatchItemDao.canDelet(params);
        if (dispatchId.length == i) {
            for (int ii = 0; ii < dispatchId.length; ii++) {
                DispatchItemDO dispatchItemDo = new DispatchItemDO();
                dispatchItemDo.setId(dispatchId[ii]);
                dispatchItemDo.setStatus(ConstantForMES.CLOSE_CASE);
                dispatchItemDao.update(dispatchItemDo);
            }
            return R.ok();
        } else {
            //"关闭失败！"
            return R.error(messageSourceHandler.getMessage("common.massge.errorColse",null));
        }
    }

    @Override
    public Map<String, Object> getPDStartEndTime(Long id) {
        return dispatchItemDao.getPDStartEndTime(id);
    }

    @Override
    public R workStartDispatchItem(Long dispatchId) {

        //根据操作工查询所有派工单，验证此人开工的工单数量
        int countOfStartWorking=dispatchItemDao.countOfStartWorking(dispatchId);
       if(countOfStartWorking!=0) {
           return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.workingNow",null));
       }

        Map<String, Object> statusOfPAndD = dispatchItemDao.getStatusOfPlanAndDisp(dispatchId);
        if (Objects.nonNull(statusOfPAndD)) {
            // dispatchStatus, planStatus
            //ISSUED = 232;下达  ++++  CLOSE_CASE = 234;结案   ++++  PUT_UP = 233;挂起    +++START_WORK 开工
            if (Objects.equals(ConstantForMES.ISSUED, Integer.parseInt(statusOfPAndD.get("planStatus").toString()))) {
                if (Objects.equals(ConstantForMES.PUT_UP, Integer.parseInt(statusOfPAndD.get("dispatchStatus").toString()))||Objects.equals(ConstantForMES.AWAITING_DELIVERY, Integer.parseInt(statusOfPAndD.get("dispatchStatus").toString()))) {
                    //允许开工
                    //搜寻是否有个同id的挂起记录，时间为空，标记为1的记录，
                    Map<String, Object> wHungTime = dispatchWorkingHungService.getWHungTime(0,dispatchId);//查询是否已有挂机记录

                    DispatchWorkingHungDO dWorkingHungDO=new DispatchWorkingHungDO();
                    dWorkingHungDO.setDispatchItemId(dispatchId);
                    dWorkingHungDO.setStartTime(new Date());
                    dWorkingHungDO.setSign(1);
                    dWorkingHungDO.setStartHangId(ShiroUtils.getUserId());

                    DispatchItemDO dispatchItemDO1=new DispatchItemDO();
                    dispatchItemDO1.setId(dispatchId);
                    dispatchItemDO1.setStatus(ConstantForMES.START_WORK);
                    if (wHungTime!=null){
                        //如果有挂机记录则算出开工到挂起的结束时间 更新   并添加挂起记录
                        Date startTime = DateFormatUtil.getDateByParttern(wHungTime.get("startTime").toString(), "yyyy-MM-dd HH:mm:ss");
                        BigDecimal dateHour = DatesUtil.dateHour( startTime,new Date());

                        DispatchWorkingHungDO dWorkingHungDO2=new DispatchWorkingHungDO();
                        dWorkingHungDO2.setId(Long.valueOf(wHungTime.get("id").toString()));
                        dWorkingHungDO2.setHangTime(new Date());
                        dWorkingHungDO2.setManHour(dateHour);

                        //更新原有挂机记录的 挂机结束时间（hung_time）和时长
                        dispatchWorkingHungService.update(dWorkingHungDO2);
                    }
                    //新建开工记录
                    dispatchWorkingHungService.save(dWorkingHungDO);
                    //更新工单状态
                    dispatchItemDao.update(dispatchItemDO1);


                    //将第一次开工时间反写计划子表、工单表
                    Map<String, Object> pdStartEndTime = dispatchItemDao.getPDStartEndTime(dispatchId);
                    if(pdStartEndTime!=null){
                        if(!pdStartEndTime.containsKey("realityStartTime")){
                            WorkingProcedureDetailDO wPDetailDO=new WorkingProcedureDetailDO();
                            wPDetailDO.setId(pdStartEndTime.containsKey("planItemid")?Long.valueOf(pdStartEndTime.get("planItemid").toString()):null);
                            wPDetailDO.setRealityStartTime(new Date());
                            workingProcedureDetailService.update(wPDetailDO);
                        }
                        if(!pdStartEndTime.containsKey("actualStartTime")){
                            DispatchItemDO dispatchItemDO=new DispatchItemDO();
                            dispatchItemDO.setId(dispatchId);
                            dispatchItemDO.setActualStartTime(new Date());
                            dispatchItemDao.update(dispatchItemDO);
                        }
                    }
                    return R.ok();
                } else if (Objects.equals(ConstantForMES.START_WORK, Integer.parseInt(statusOfPAndD.get("dispatchStatus").toString()))) {
                    //已开工 请勿重复操作
                    return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.workingOk",null));
                }

            } else if (Objects.equals(ConstantForMES.CLOSE_CASE, Integer.parseInt(statusOfPAndD.get("planStatus").toString())) || Objects.equals(ConstantForMES.CLOSE_CASE, Integer.parseInt(statusOfPAndD.get("dispatchStatus").toString()))) {
               //工序计划/工单已结案，禁止开工
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.planOrdisOver",null));

            } else if (Objects.equals(ConstantForMES.PUT_UP, Integer.parseInt(statusOfPAndD.get("planStatus").toString()))) {
                //工序计划挂起状态，禁止开工
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.hungNo",null));
            }
        }
            //"此参数下无数据！请检查参数！"
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));

    }

    @Override
    public R hangUpDispatchItem(Long dispatchId)  {

        DispatchItemDO dispatchItemDO = dispatchItemDao.get(dispatchId);
        if (Objects.nonNull(dispatchItemDO)) {
            //ISSUED = 232;下达  ++++  CLOSE_CASE = 234;结案   ++++  PUT_UP = 233;挂起    +++START_WORK 开工
            if(Objects.equals(dispatchItemDO.getStatus(),ConstantForMES.START_WORK)){
                //允许挂起----->检查是否有开工记录，有就更新结束时间和时长并新建挂起记录；无则新建挂起记录

                DispatchWorkingHungDO dWorkingHungDO=new DispatchWorkingHungDO();
                dWorkingHungDO.setDispatchItemId(dispatchId);
                dWorkingHungDO.setStartTime(new Date());
                dWorkingHungDO.setSign(0);
                dWorkingHungDO.setStartHangId(ShiroUtils.getUserId());

                DispatchItemDO dispatchItemDO1=new DispatchItemDO();
                dispatchItemDO1.setId(dispatchId);
                dispatchItemDO1.setStatus(ConstantForMES.PUT_UP);

                Map<String, Object> wHungTime = dispatchWorkingHungService.getWHungTime(1,dispatchId);//查询是否已有开工记录
                if(wHungTime!=null){
                    //更新 hung_time ,计算man_hour;+++新增挂起记录

                    Date startTime = DateFormatUtil.getDateByParttern(wHungTime.get("startTime").toString(), "yyyy-MM-dd HH:mm:ss");
                    BigDecimal dateHour = DatesUtil.dateHour( startTime,new Date());

                    DispatchWorkingHungDO dWorkingHungDO2=new DispatchWorkingHungDO();
                    dWorkingHungDO2.setId(Long.valueOf(wHungTime.get("id").toString()));
                    dWorkingHungDO2.setHangTime(new Date());
                    dWorkingHungDO2.setManHour(dateHour);
                    //更新原有挂机记录的 挂机结束时间（hung_time）和时长
                    dispatchWorkingHungService.update(dWorkingHungDO2);
                }
                    //新建开工记录
                    dispatchWorkingHungService.save(dWorkingHungDO);
                    //更新工单状态为挂起
                    dispatchItemDao.update(dispatchItemDO1);

                    return R.ok();

            }else if(Objects.equals(dispatchItemDO.getStatus(),ConstantForMES.PUT_UP)){
               //已为挂起状态，请勿重复操作！
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.hungNow",null));
            }else if(Objects.equals(dispatchItemDO.getStatus(),ConstantForMES.AWAITING_DELIVERY)){
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.hungError",null));
            }else if(Objects.equals(dispatchItemDO.getStatus(),ConstantForMES.CLOSE_CASE)){
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.overHungError",null));
            }else{
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.hungFalse",null));
            }

        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }


    @Override
    public R oneStartWorkDetail(){
        Long operatorId=ShiroUtils.getUserId();
        Map<String,Object>  map= new HashMap<>();
        List<Map<String, Object>> startWorkByOperator = dispatchItemDao.getStartWorkByOperator(operatorId);
        if (!startWorkByOperator.isEmpty()){
            Map<String, Object> data = startWorkByOperator.get(0);
            map.put("data",data);
            Map<String,Object> param = Maps.newHashMap();
            // 获取附件信息
            param.put("assocId",data.getOrDefault("processId",0));
            param.put("assocType", Constant.PROCESS_FILE);
            List<ContentAssocDO> checkResultList = contentAssocService.list(param);
            map.put("fileList", checkResultList);
        }
        return R.ok(map);
    }

    @Override
    public List<DispatchItemDO> countOfStatus(Map<String, Object> map) {
        return dispatchItemDao.countOfStatus(map);
    }

    @Override
    public R distributionDiagram()  {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        DatesUtil datesUtil=new DatesUtil();
        Date dateBefor = datesUtil.getDateBefor(new Date(), 6);
        Map<String,Object>  map= new HashMap<>();
        map.put("status",ConstantForMES.CLOSE_CASE);
        map.put("createTime",formatter.format(dateBefor));
        List<DispatchItemDO> dispatchItemDo = this.countOfStatus(map);
        BigDecimal awaitingDelivery =BigDecimal.ZERO;
        BigDecimal startWorking=BigDecimal.ZERO;
        BigDecimal unWorkingl=BigDecimal.ZERO;
        BigDecimal addOne=new BigDecimal("1");
        if(dispatchItemDo.size()>0){
            for(DispatchItemDO dispatchItem:dispatchItemDo){
               int statuss= dispatchItem.getStatus();
                if(Objects.equals(ConstantForMES.START_WORK,statuss)){
                    startWorking=startWorking.add(addOne);
                }else if(Objects.equals(ConstantForMES.AWAITING_DELIVERY,statuss)){
                    awaitingDelivery=awaitingDelivery.add(addOne);
                }else if(Objects.equals(ConstantForMES.PUT_UP,statuss)){
                    unWorkingl=unWorkingl.add(addOne);
                }
            }
        }
        BigDecimal totalOrder=awaitingDelivery.add(startWorking).add(unWorkingl);
        map.clear();
        map.put("totalOrder",totalOrder);
        map.put("awaitingDelivery",awaitingDelivery);
        map.put("startWorking",startWorking);
        map.put("unWorkingl",unWorkingl);
        return R.ok(map);
    }

    @Override
    public R productionSchedule(){
        Map<String,Object>  map= new HashMap<>();
        map.put("status",ConstantForMES.CLOSE_CASE);
        List<DispatchItemDO> dispatchItemDo = this.countOfStatus(map);
        List<DispatchItemDO> result=new ArrayList<>();
        if(dispatchItemDo.size()>0){
            List<DispatchItemDO> results=dispatchItemDo.stream().sorted(Comparator.comparing(DispatchItemDO::getPlanCount).reversed()).collect(Collectors.toList());
            int index=10;
            if(results.size()<=10){
                index=results.size();
            }
            result=results.subList(0,index);
        }
        map.clear();
        map.put("data",result);
        return  R.ok(map);
    }

    @Override
    public R getPieceRateWage(){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        DatesUtil datesUtil=new DatesUtil();
        Date dateBefor = datesUtil.getDateBefor(new Date(), 6);
        Map<String,Object>  map= new HashMap<>();
        map.put("status",Constant.APPLY_APPROED);
        map.put("createTimes",formatter.format(dateBefor));
        List<Map<String, Object>> mapList = processReportService.listForMap(map);
        map.clear();
        if(mapList.size()>0){
//            List<Map<String, Object>> results= mapList.stream().sorted((v1,v2)->Integer.parseInt(v1.get("conformityCount").toString())>Integer.parseInt(v2.get("conformityCount").toString())?-1:1).collect(Collectors.toList());
            Map<String, BigDecimal> operatorSalaryMap = mapList
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("operator").toString(), v -> MathUtils.getBigDecimal(v.get("salary")), BigDecimal::add));
            List<Map<String, Object>> finalList=new ArrayList<>();
            for(String operator:operatorSalaryMap.keySet()){
                for(Map<String, Object> mapOne:mapList){
                    if(Objects.equals(operator,mapOne.get("operator").toString())){
                        Map<String,Object>  finallMap= new HashMap<>();
                        finallMap.put("operatorName",mapOne.get("operatorName"));
                        finallMap.put("salary",operatorSalaryMap.get(mapOne.get("operator").toString()));
                        finalList.add(finallMap);
                        break;
                    }
                }
            }
            List<Map<String, Object>> results= finalList.stream()
                    .sorted((v1,v2)->new BigDecimal(v1.get("salary").toString()).compareTo(new BigDecimal(v2.get("salary").toString()))>0?-1:1)
                    .collect(Collectors.toList());
            int index=10;
           if(finalList.size()<=10){
               index=results.size();
           }
            List<Map<String, Object>>result =results.subList(0,index);
            map.put("data",result);
        }
        return  R.ok(map);
    }


    @Override
    public R deviceProduction(int sign){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        DatesUtil datesUtil=new DatesUtil();
        Date dateTime;
        if(Objects.equals(1,sign)){
            dateTime = datesUtil.getDateBefor(new Date(), 1);
        }else{
            dateTime=new Date();
        }
        Map<String,Object>  map= new HashMap<>();
        map.put("status",Constant.APPLY_APPROED);
        map.put("yieldTime",formatter.format(dateTime));
        List<Map<String, Object>> mapList = processReportService.listForMap(map);
        map.clear();
        if(mapList.size()>0) {
            Map<String, BigDecimal> operatorProductionMap = mapList
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("deviceId").toString(), v -> MathUtils.getBigDecimal(v.get("conformityCount")), BigDecimal::add));
            List<Map<String, Object>> finalList=new ArrayList<>();
            for(String operator:operatorProductionMap.keySet()){
                for(Map<String, Object> mapOne:mapList){
                    if(Objects.equals(operator,mapOne.get("deviceId").toString())){
                        Map<String,Object>  finallMap= new HashMap<>();
                        finallMap.put("deviceName",mapOne.get("deviceName"));
//                        finallMap.put("deviceId",mapOne.get("deviceId"));
//                        finallMap.put("id",mapOne.get("id"));
                        finallMap.put("conformityCount",operatorProductionMap.get(operator));
                        finalList.add(finallMap);
                        break;
                    }
                }
            }
            List<Map<String, Object>> results= finalList.stream()
                    .sorted((v1,v2)->new BigDecimal(v1.get("conformityCount").toString()).compareTo(new BigDecimal(v2.get("conformityCount").toString()))>0?-1:1)
                    .collect(Collectors.toList());
            int index=10;
            if(finalList.size()<=10){
                index=results.size();
            }
            List<Map<String, Object>>result =results.subList(0,index);
            map.put("data",result);
        }
        return  R.ok(map);
    }






}
