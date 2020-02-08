package com.ev.custom.service.impl;

import com.ev.framework.utils.DateUtils;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.service.DingdingService;
import com.ev.system.domain.UserDO;
import com.ev.system.service.UserService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DingdingServiceImpl implements DingdingService {
    @Autowired
    UserService userService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    RepositoryService repositoryService;

    /**
     * 提交申请
     * @param targetList
     * @param processInstanceId
     * @return
     */
    @Override
    public String submitApply(Long[] targetList,String processInstanceId) {
        List<String> userList = new ArrayList<String>();
        for(Long userId:targetList){
            UserDO user = userService.get(userId);
            userList.add(user.getUserId().toString());
        }
        if(StringUtils.isEmpty(processInstanceId) || "undefined".equals(processInstanceId)){
            Map<String,Object> variables = new HashMap<String,Object>(){{put("huiqian",userList);put("assign",ShiroUtils.getUser().getUserId());}};
            ProcessInstance process = ProcessEngines.getDefaultProcessEngine().getRuntimeService().startProcessInstanceByKey("dingding_common", variables);
            processInstanceId = process.getId();
        }else{
            Map<String,Object> variables = new HashMap<String,Object>(){{put("huiqian",userList);put("assign",ShiroUtils.getUser().getUserId());}};
            ProcessEngines.getDefaultProcessEngine().getRuntimeService().setVariables(processInstanceId,variables);
        }

        String taskId = getTasksByProcessId(processInstanceId,ShiroUtils.getUserId().toString());
        ProcessEngines.getDefaultProcessEngine().getTaskService().complete(taskId);
        return processInstanceId;
    }

    /**
     * 审核
     * @param processInstanceId
     * @param isApproved
     * @param reason
     * @return
     */
    @Override
    public String completeApprove(String processInstanceId, Boolean isApproved, String reason) {
        String taskId = getTasksByProcessId(processInstanceId,ShiroUtils.getUserId().toString());
        Object downObj = ProcessEngines.getDefaultProcessEngine().getTaskService().getVariable(taskId,"down");
        Integer down = downObj==null?0:Integer.parseInt(downObj.toString());
        Object upObj = ProcessEngines.getDefaultProcessEngine().getTaskService().getVariable(taskId,"up");
        Integer up = upObj==null?0:Integer.parseInt(upObj.toString());
        Integer nrOfInstances = Integer.parseInt(ProcessEngines.getDefaultProcessEngine().getTaskService().getVariable(taskId,"nrOfInstances").toString());
        if(isApproved != null){
            if(isApproved){
                up++;
                ProcessEngines.getDefaultProcessEngine().getTaskService().setVariable(taskId,"up",up);
                Map<String,Object> variables = new HashMap<String,Object>();
                variables.put("isApproved",true);
                ProcessEngines.getDefaultProcessEngine().getTaskService().setVariablesLocal(taskId,variables);
            }else{
                down++;
                ProcessEngines.getDefaultProcessEngine().getTaskService().setVariable(taskId,"down",down);
                Map<String,Object> variables = new HashMap<String,Object>();
                variables.put("isApproved",false);
                variables.put("reason",reason);
                ProcessEngines.getDefaultProcessEngine().getTaskService().setVariablesLocal(taskId,variables);
            }
        }
        ProcessEngines.getDefaultProcessEngine().getTaskService()
                .complete(taskId);
        if(down > 0){
            return "down";
        }else if(up/nrOfInstances == 1){
            return "up";
        }else{
            return null;
        }
    }

    @Override
    public List getHistoryByProcessId(String processId) {
        List<HistoricTaskInstance> list=ProcessEngines.getDefaultProcessEngine().
                getHistoryService().createHistoricTaskInstanceQuery().
                processInstanceId(processId).
                orderByTaskCreateTime().asc().list();

        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for(HistoricTaskInstance historicTaskInstance : list){
            Map<String,Object> map = new HashMap<String,Object>();
            //获取reason参数和isApproved参数
            if("approve".equals(historicTaskInstance.getName())){
                List<HistoricVariableInstance> aaList = ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).list();
                for(HistoricVariableInstance entity:aaList){
                    if("isApproved".equals(entity.getVariableName())){
                        map.put("isApproved",entity.getValue());
                    }
                    if("reason".equals(entity.getVariableName())){
                        map.put("reason",entity.getValue());
                    }
                }
                map.put("dealName","审批");
            }else{
                map.put("dealName","发起申请");
            }
            if(historicTaskInstance.getEndTime()==null){
                map.put("isDealed",false);
            }else{
                map.put("isDealed",true);
                map.put("dealTime", DateUtils.format(historicTaskInstance.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
            }

            //获取用户信息
            UserDO user = userService.get(Long.parseLong(historicTaskInstance.getAssignee()));
            map.put("userName",user.getName());
            map.put("userId",user.getUserId());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 根据流程实例ID和用户Id获得代办的任务ID
     * @param processId
     * @param userId
     * @return
     */
    public String getTasksByProcessId(String processId,String userId){
        Task task = null;
        TaskQuery query = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery().taskCandidateOrAssigned(userId).active();
        List<Task> todoList = query.list();//获取申请人的待办任务列表
        for (Task tmp : todoList) {
            if(tmp.getProcessInstanceId().equals(processId)){
                task = tmp;//获取当前流程实例，当前申请人的待办任务
                break;
            }
        }
        return task==null?null:task.getId();
    }


}
