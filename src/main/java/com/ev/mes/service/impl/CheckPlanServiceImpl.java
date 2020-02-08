package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.mes.dao.CheckPlanDao;
import com.ev.mes.dao.CheckPlanItemDao;
import com.ev.mes.domain.CheckPlanDO;
import com.ev.mes.domain.CheckPlanItemDO;
import com.ev.mes.service.CheckPlanService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class CheckPlanServiceImpl implements CheckPlanService {
    @Autowired
    private CheckPlanDao checkPlanDao;

    @Autowired
    private CheckPlanItemDao checkPlanItemDao;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @Override
    public CheckPlanDO get(Long id) {
        return checkPlanDao.get(id);
    }

    @Override
    public List<CheckPlanDO> list(Map<String, Object> map) {
        return checkPlanDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return checkPlanDao.count(map);
    }

    @Override
    public int save(CheckPlanDO checkPlan) {
        return checkPlanDao.save(checkPlan);
    }

    @Override
    public int update(CheckPlanDO checkPlan) {
        return checkPlanDao.update(checkPlan);
    }

    @Override
    public int remove(Long id) {
        return checkPlanDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return checkPlanDao.batchRemove(ids);
    }

    @Override
    public R saveAndChangePlan(CheckPlanDO checkPlanDO, String projectDetail, Long[] ids) {

        if (Objects.nonNull(checkPlanDO.getId())) {

            CheckPlanDO planDO = checkPlanDao.get(checkPlanDO.getId());
            if(Objects.equals(ConstantForMES.OK_AUDITED,planDO.getAuditSign())){
                //"方案已审核不允许修改！"
                return R.error(messageSourceHandler.getMessage("common.approved.update.disabled",null));
            }
            //修改
            checkPlanDao.update(checkPlanDO);

            if (!"".equals(projectDetail)) {
                List<CheckPlanItemDO> bodys = JSON.parseArray(projectDetail, CheckPlanItemDO.class);
                for (CheckPlanItemDO itemDO : bodys) {
                    if (Objects.nonNull(itemDO.getId())) {
                        checkPlanItemDao.update(itemDO);
                    } else {
                        itemDO.setPlanId(checkPlanDO.getId());
                        checkPlanItemDao.save(itemDO);
                    }
                }
            }
            if (Objects.nonNull(ids)&&ids.length > 0) {
                checkPlanItemDao.batchRemove(ids);
            }
            return R.ok();
        } else {
            //新增
            //<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
            String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.CHECK_PLAN_JYFA, new Date());
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("maxNo", prefix);
            params.put("offset", 0);
            params.put("limit", 1);
            List<CheckPlanDO> list = checkPlanDao.list(params);
            String suffix = null;
            if (list.size() > 0) {
                suffix = list.get(0).getCode();
            }
            checkPlanDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
            checkPlanDO.setAuditSign(ConstantForMES.WAIT_AUDIT);

            checkPlanDao.save(checkPlanDO);

            if (!"".equals(projectDetail)) {
                List<CheckPlanItemDO> bodys = JSON.parseArray(projectDetail, CheckPlanItemDO.class);
                for (CheckPlanItemDO itemDO : bodys) {
                    itemDO.setPlanId(checkPlanDO.getId());
                    checkPlanItemDao.save(itemDO);
                }
            }
            return R.ok();
        }
    }

    @Override
    public R auditPlanOfCheck(Long id,Long auditId){
        CheckPlanDO planDO = checkPlanDao.get(id);
        if(Objects.equals(ConstantForMES.OK_AUDITED,planDO.getAuditSign())){
            //"已审核请勿重复操作！"
            return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
        }else{

            CheckPlanDO checkPlanDO =new CheckPlanDO();
            checkPlanDO.setAuditSign(ConstantForMES.OK_AUDITED);
            checkPlanDO.setId(id);
//            checkPlanDO.setAuditId(auditId);
            checkPlanDO.setAuditId(ShiroUtils.getUserId());
            checkPlanDao.update(checkPlanDO);
            return R.ok();
        }
    }

    @Override
    public R opposeAuditCheckPlan(Long id ){

        CheckPlanDO planDO = checkPlanDao.get(id);
        if(Objects.equals(ConstantForMES.WAIT_AUDIT,planDO.getAuditSign())){
            //"已为待审核请勿重复操作！"
            return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
        }else{
            CheckPlanDO checkPlanDO =new CheckPlanDO();
            checkPlanDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
            checkPlanDO.setId(id);
            checkPlanDO.setAuditId(0L);
            checkPlanDao.update(checkPlanDO);
            return R.ok();
        }
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return checkPlanDao.listForMap(map);
    }

    @Override
    public int countListForMap(Map<String, Object> map) {
        return checkPlanDao.countListForMap(map);
    }

    @Override
    public R getPlanOfDetail(Long id){
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("id",id);
        List<Map<String, Object>> planDetail = checkPlanDao.listForMap(params);
        List<Map<String, Object>> itemDetail = checkPlanItemDao.listOfDetail(params);
        params.clear();
        if(!planDetail.isEmpty()){
            params.put("planDetail",planDetail);
            params.put("itemDetail",itemDetail);
        }
        return R.ok(params);
    }

    @Override
    public R deletOfPlan(Long[] ids) {
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        results.put("id",ids);
        int canDelet = checkPlanDao.canDelet(results);
        if(canDelet==ids.length){
            checkPlanDao.deletOfPlan(results);
            return R.ok();
        }
        return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
    }



}
