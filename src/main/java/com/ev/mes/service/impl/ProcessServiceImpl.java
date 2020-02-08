package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
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

import java.util.*;


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
    public R saveAndChange(ProcessDO processDO, String processCheck, String processDevice) {

        if (Objects.nonNull(processDO.getId())) {
            //更新

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
            return R.ok();
        } else {
            //新增
            //<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
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

            processDao.save(processDO);

            if (!"".equals(processCheck)) {
                List<ProcessCheckDO> bodys = JSON.parseArray(processCheck, ProcessCheckDO.class);
                for (ProcessCheckDO checkDO : bodys) {
                    checkDO.setForeignId(processDO.getId());
                    checkDO.setType(ConstantForMES.PROCESS_GXPZ);//标记区分
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
        }
        return R.ok(results);
    }

    @Override
    public R deteBatchProcess(Long[] ids) {

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
