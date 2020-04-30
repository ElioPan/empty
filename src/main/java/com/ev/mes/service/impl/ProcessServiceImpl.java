package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.ProcessCheckDao;
import com.ev.mes.dao.ProcessDao;
import com.ev.mes.dao.ProcessDeviceDao;
import com.ev.mes.domain.ProcessCheckDO;
import com.ev.mes.domain.ProcessDO;
import com.ev.mes.domain.ProcessDeviceDO;
import com.ev.mes.service.CraftItemService;
import com.ev.mes.service.ProcessService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessDao processDao;
    @Autowired
    private ProcessCheckDao processCheckDao;
    @Autowired
    private ProcessDeviceDao processDeviceDao;
    @Autowired
    private CraftItemService craftItemService;
    @Autowired
    private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public ProcessDO get(Long id) {
        return processDao.get(id);
    }

    @Override
    public List<ProcessDO> list(Map<String, Object> map) {
        return processDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return processDao.count(map);
    }

    @Override
    public int save(ProcessDO process) {
        return processDao.save(process);
    }

    @Override
    public int update(ProcessDO process) {
        return processDao.update(process);
    }

    @Override
    public int remove(Long id) {
        return processDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return processDao.batchRemove(ids);
    }

    @Override
    public R saveAndChange(ProcessDO processDO, String processCheck, String processDevice,String uploadAttachment) {

        if (Objects.nonNull(processDO.getId())) {
            //更新
            processDO.setAuditSign(Constant.WAIT_AUDIT);
            processDao.update(processDO);

            //检验项目+设备  --先全删后新增
            if (!"".equals(processCheck)) {
                //删除全部再新增
                Map<String,Object> param=Maps.newHashMap();
                param.put("foreignId",processDO.getId());
                param.put("type",ConstantForMES.PROCESS_GXPZ);
                processCheckDao.removeByProcessId(param);

                List<ProcessCheckDO> bodys = JSON.parseArray(processCheck, ProcessCheckDO.class);

                for (ProcessCheckDO checkDO : bodys) {
                    checkDO.setForeignId(processDO.getId());
                    checkDO.setType(ConstantForMES.PROCESS_GXPZ);//标记区分
                    processCheckDao.save(checkDO);
                }
            }

            if (!"".equals(processDevice)) {
                //删除全部再新增
                processDeviceDao.removeByProcessId(processDO.getId());

                List<ProcessDeviceDO> bodys = JSON.parseArray(processDevice, ProcessDeviceDO.class);
                for (ProcessDeviceDO deviceDO : bodys) {
                    deviceDO.setProcessId(processDO.getId());
                    processDeviceDao.save(deviceDO);
                }
            }
            //删除路径
            Long[] ids = {processDO.getId()};
            contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.PROCESS_FILE);

            // 上传附件
            if (StringUtils.isNoneBlank(uploadAttachment)) {
                contentAssocService.saveList(processDO.getId(), JSONArray.parseArray(uploadAttachment), ConstantForDevice.PROCESS_FILE);
            }

            return R.ok();
        } else {
            //新增
            if(StringUtils.isNotEmpty(processDO.getCode())&&!(processDO.getCode().startsWith(ConstantForDevice.GYS))){
                Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
                params.put("code",processDO.getCode());
                if(processDao.checkSave(params)>0){
                    return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
                }
            }
            if(StringUtils.isEmpty(processDO.getCode()) || processDO.getCode().startsWith(ConstantForMES.PROCESS_GXPZ)){
                String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.PROCESS_GXPZ, new Date());
                Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
                params.put("maxNo", prefix);
                params.put("offset", 0);
                params.put("limit", 1);
                List<ProcessDO> list = processDao.list(params);
                String suffix = null;
                if (list.size() > 0) {
                    suffix = list.get(0).getCode();
                }
                processDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
            }

            processDO.setAuditSign(Constant.WAIT_AUDIT);
            processDao.save(processDO);

            if (!"".equals(processCheck)) {
                List<ProcessCheckDO> bodys = JSON.parseArray(processCheck, ProcessCheckDO.class);
                for (ProcessCheckDO checkDO : bodys) {
                    checkDO.setForeignId(processDO.getId());
                    checkDO.setType(ConstantForMES.PROCESS_GXPZ);
                    processCheckDao.save(checkDO);
                }
            }
            if (!"".equals(processDevice)) {
                List<ProcessDeviceDO> bodys = JSON.parseArray(processDevice, ProcessDeviceDO.class);
                for (ProcessDeviceDO deviceDO : bodys) {
                    deviceDO.setProcessId(processDO.getId());
                    processDeviceDao.save(deviceDO);
                }
            }

            // 上传附件
            if (StringUtils.isNoneBlank(uploadAttachment)) {
                contentAssocService.saveList(processDO.getId(), JSONArray.parseArray(uploadAttachment), ConstantForDevice.PROCESS_FILE);
            }
            return R.ok();
        }
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return processDao.listForMap(map);
    }

    @Override
    public int countListForMap(Map<String, Object> map) {
        return processDao.countListForMap(map);
    }

    @Override
    public  R getProcessDetail(Long id ){
        Map<String, Object> results = Maps.newHashMap();
        results.put("id", id);
        List<Map<String, Object>> processlist = processDao.listForMap(results);

        Map<String,Object> param=Maps.newHashMap();
        param.put("foreignId",id);
        param.put("type",ConstantForMES.PROCESS_GXPZ);
        List<Map<String, Object>> processCheckDetail = processCheckDao.getDetailByProcessId(param);

        results.remove("id");
        if(!processlist.isEmpty()){
            results.put("processlist",processlist);
            results.put("processCheckDetail",processCheckDetail);
            param.clear();
            // 获取附件信息
            param.put("assocId",id);
            param.put("assocType", ConstantForDevice.PROCESS_FILE);
            List<ContentAssocDO> checkResultList = contentAssocService.list(param);
            results.put("fileList", checkResultList);
        }
        return R.ok(results);
    }

    @Override
    public R deteBatchProcess(Long[] ids) {
        //验证是否已审核
        for(Long id:ids){
             ProcessDO processDO = processDao.get(id);
            if(Objects.equals(processDO.getAuditSign(), Constant.OK_AUDITED)){
                return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
            }
        }
        //去工艺路线的子表中查询是否已经有了此工序id，
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", ids);
        int i= craftItemService.canDeletaByProcessId(param);

        if (i==0) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", ids);
            processDao.deletOfProcess(map);
            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
        }
    }

    @Override
    public List<Map<String, Object>> getDetailByProcessId(Map<String, Object> map) {
        return processCheckDao.getDetailByProcessId(map);
    }


}
