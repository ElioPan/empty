package com.ev.common.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.ApplicationContextRegister;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.config.RabbitmqConfig;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.common.vo.PlanVo;
import com.ev.custom.domain.PatrolPlanDO;
import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.custom.service.PatrolPlanService;
import com.ev.custom.service.PatrolRecordService;
import com.ev.custom.service.UpkeepPlanService;
import com.ev.custom.service.UpkeepRecordService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excuteNotice:根据计划配置的规则定时生成第二天的巡检单和保养单。
 * updatePatrolPlan:根据计划的结束时间更新计划单的状态为已完成状态
 */
@Component
public class NoticeJob{
    private static final Logger log= LoggerFactory.getLogger(NoticeJob.class);

    @Scheduled(cron="0 50 23 * * ? ")
    private void executeInternal() {
        try {
            //根据计划配置的规则定时生成第二天的巡检单和保养单。
            excuteNotice();
            //根据计划的结束时间更新计划单的状态为已完成状态
            updatePatrolPlan();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //随机返回字符串数组中的字符串
    public static String RandomStr(String[] strs){
        int random_index = (int) (Math.random()*strs.length);
        return strs[random_index];
    }


    private void  excuteNotice() throws ParseException {
        //巡检计划单
        PatrolPlanService patrolPlanService = ApplicationContextRegister.getBean(PatrolPlanService.class);
        PatrolRecordService patrolRecordService = ApplicationContextRegister.getBean(PatrolRecordService.class);
        UpkeepRecordService upkeepRecordService = ApplicationContextRegister.getBean(UpkeepRecordService.class);
        Date now = new Date();
        String tomrrowStr = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(now,1));
        Map<String,Object> map = new HashMap<String,Object>(){{put("beginTime",tomrrowStr);put("endTime",tomrrowStr);put("status","state_start");}};
        List<PlanVo> patrolPlanList = patrolPlanService.getPlanView(map);
        for(PlanVo planVo : patrolPlanList){
            if(Objects.equals(planVo.getFrequency(),0)){
                log.error(ConstantForDevice.PATRAL_PLAN.equals(planVo.getPlanType())?"巡检计划"+planVo.getPlanNo()+"时间间隔不能为0":"保养计划"+planVo.getPlanNo()+"时间间隔不能为0");
                continue;
            }
            JSONArray array = new JSONArray();
            Map<String,Object> conditionMap = new HashMap<String, Object>();
            conditionMap.put("planId",planVo.getId());
            if(ConstantForDevice.PATRAL_PLAN.equals(planVo.getPlanType())){
                //TODO 巡检计划生成
                List<PatrolRecordDO> patrolRecordList = patrolRecordService.list(conditionMap);
                if(patrolRecordList.size()>0){
                    //获取最新一条
                    PatrolRecordDO patrolRecord = patrolRecordList.get(0);
                    array = getDoTimes(now,patrolRecord.getStartTime(),planVo.getFrequency(),planVo.getExpireTime(),planVo.getPlanType());
                }else{
                    array = getDoTimes(now,null,planVo.getFrequency(),planVo.getExpireTime(),planVo.getPlanType());
                }
                try{
                    saveRecordByPlan(array,planVo);
                }catch(Exception e){
                    continue;
                }
            }else if(ConstantForDevice.UPKEEP_PLAN.equals(planVo.getPlanType())){
                //TODO 保养计划生成
                List<UpkeepRecordDO> upkeepRecordList = upkeepRecordService.list(conditionMap);
                if(upkeepRecordList.size()>0){
                    //获取最新一条
                    UpkeepRecordDO upkeepRecordDO = upkeepRecordList.get(0);
                    array = getDoTimes(now,upkeepRecordDO.getStartTime(),planVo.getFrequency(),planVo.getExpireTime(),planVo.getPlanType());
                }else{
                    array = getDoTimes(now,null,planVo.getFrequency(),planVo.getExpireTime(),planVo.getPlanType());
                }
                try{
                    saveRecordByPlan(array,planVo);
                }catch(Exception e){
                    continue;
                }
            }
        }
    }

    private JSONArray getDoTimes(Date nowDate, Date lastTime, Integer frequncy,Integer expireTime, String planType) throws ParseException {
        Integer timeInterval = frequncy-expireTime;
        JSONArray array = new JSONArray();
        Long tomrrowDate = DateUtils.addDays(nowDate,1).getTime();
        //明天零点零分零秒的毫秒数
        long zeroT=tomrrowDate/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();
        String zeroString  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zeroT);
        Date zero = DateUtils.parseDate(zeroString,"yyyy-MM-dd HH:mm:ss");
        //明天23点59分59秒的毫秒数
        long endT=zeroT+24*60*60*1000-1;
        String endString  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endT);
        Date end = DateUtils.parseDate(endString,"yyyy-MM-dd HH:mm:ss");
        //获取下一次计划的时间（中间算上了提前消息提醒的时间，所以这个下次计划时间实际上是下次提醒的时间）
        Date nextNoticeDate = lastTime==null?zero:(DateUtils.addHours(lastTime,timeInterval).compareTo(zero)>0?DateUtils.addHours(lastTime,timeInterval):zero);
        Date nextDate = lastTime==null?zero:(DateUtils.addHours(lastTime,frequncy).compareTo(zero)>0?DateUtils.addHours(lastTime,frequncy):zero);
        if(nextNoticeDate.compareTo(nowDate)>0 && end.compareTo(nextNoticeDate)>0){
            //循环叠加，指导超过第二天的时间
            Date doTime = nextDate;
            while(end.compareTo(doTime)>=0) {
                JSONObject nextPlanObj = new JSONObject();
                nextPlanObj.put("beginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(doTime));
                nextPlanObj.put("endTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.addHours(doTime,frequncy)));
                nextPlanObj.put("planType",planType);
                array.add(nextPlanObj);
                doTime = DateUtils.addHours(doTime,frequncy);
            }
        }
        return array;
    }

    private void saveRecordByPlan(JSONArray array,PlanVo planVo)throws ParseException{
        PatrolRecordService patrolRecordService = ApplicationContextRegister.getBean(PatrolRecordService.class);
        UpkeepPlanService upkeepPlanService = ApplicationContextRegister.getBean(UpkeepPlanService.class);
        if(ConstantForDevice.PATRAL_PLAN.equals(planVo.getPlanType())){
            for (int i=0;i<array.size();i++){
                JSONObject object = array.getJSONObject(i);
                patrolRecordService.addRecordByPlan(planVo.getId(),DateUtils.parseDate(object.get("beginTime").toString(),"yyyy-MM-dd HH:mm:ss"),DateUtils.parseDate(object.get("endTime").toString(),"yyyy-MM-dd HH:mm:ss"));
            }
        }else if(ConstantForDevice.UPKEEP_PLAN.equals(planVo.getPlanType())){
            for (int i=0;i<array.size();i++){
                JSONObject object = array.getJSONObject(i);
                upkeepPlanService.makeWorkOrder(planVo.getId(),DateUtils.parseDate(object.get("beginTime").toString(),"yyyy-MM-dd HH:mm:ss"),DateUtils.parseDate(object.get("endTime").toString(),"yyyy-MM-dd HH:mm:ss"));
            }
        }
    }
    
    private void updatePatrolPlan(){
    	 //巡检计划单
        PatrolPlanService patrolPlanService = ApplicationContextRegister.getBean(PatrolPlanService.class);
    	Map<String,Object> params = Maps.newHashMapWithExpectedSize(1);
    	params.put("planEndTime", DateFormatUtil.getFormateDate(new Date()));
    	// 129 为启用状态
    	params.put("status", ConstantForDevice.STATE_START);
    	List<Map<String,Object>> planList = patrolPlanService.planList(params);
    	List<Long> planIds = new ArrayList<>();
    	if (planList.size()>0) {
    		PatrolPlanDO patrolPlan = null;
    		for (Map<String, Object> map : planList) {
    			planIds.add(Long.parseLong(map.get("id").toString()));
    		}
    		if (planIds.size()>0) {
    			for (Long planId : planIds) {
    				patrolPlan = new PatrolPlanDO();
    				patrolPlan.setId(planId);
    				// 131 为已完成
    				patrolPlan.setStatus(ConstantForDevice.STATE_STOP_OVER);
    				patrolPlanService.update(patrolPlan);
    			}
			}
		}
    }
}
