package com.ev.mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.domain.DeviceDO;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DeviceService;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.CraftDao;
import com.ev.mes.dao.CraftItemDao;
import com.ev.mes.dao.ProcessCheckDao;
import com.ev.mes.domain.CraftDO;
import com.ev.mes.domain.CraftItemDO;
import com.ev.mes.domain.ProcessCheckDO;
import com.ev.mes.domain.ProcessDO;
import com.ev.mes.service.CraftService;
import com.ev.mes.service.ProcessCheckService;
import com.ev.mes.service.ProcessService;
import com.ev.mes.vo.CraftEntity;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CraftServiceImpl implements CraftService {
    @Autowired
    private CraftDao craftDao;
    @Autowired
    private ProcessService processService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProcessCheckDao processCheckDao;
    @Autowired
    private CraftItemDao craftItemDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private ProcessCheckService processCheckService;

    @Override
    public CraftDO get(Long id) {
        return craftDao.get(id);
    }

    @Override
    public List<CraftDO> list(Map<String, Object> map) {
        return craftDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return craftDao.count(map);
    }

    @Override
    public int save(CraftDO craft) {
        return craftDao.save(craft);
    }

    @Override
    public int update(CraftDO craft) {
        return craftDao.update(craft);
    }

    @Override
    public int remove(Long id) {
        return craftDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return craftDao.batchRemove(ids);
    }


    @Override
    public R saveAndChangeCraft(CraftDO craftDO, String processAndProject, Long[] deletCraftItemIds, Long[] deletProcessCheckIds) {

        if (Objects.nonNull(craftDO.getId())) {
            //更新
            craftDao.update(craftDO);

            if (!"".equals(processAndProject)) {

                List<CraftItemDO> craftItemDos = JSON.parseArray(processAndProject, CraftItemDO.class);
                JSONArray processAndPro = JSONArray.parseArray(processAndProject);

                //处理工艺明细表
                for (int i = 0; i < craftItemDos.size(); i++) {

                    CraftItemDO craftItemDo = craftItemDos.get(i);

                    if (Objects.nonNull(deletCraftItemIds)&&deletCraftItemIds.length > 0) {
                        craftItemDao.batchRemove(deletCraftItemIds);
                    }
                    if (Objects.nonNull(craftItemDo.getId())) {
                        craftItemDao.update(craftItemDo);

                    } else {
                        craftItemDo.setCraftId(craftDO.getId());
                        craftItemDao.save(craftItemDo);
                    }

                    //处理明细保养项目表
                    JSONObject proPros = processAndPro.getJSONObject(i);
                    //此处需要判断  projects非空  ()(如果子表中是否检验为1)

//                    if(craftItemDo.getWhetherExamine()==0){
//                        Long itemIds[]={craftItemDo.getId()};
//                        Map<String, Object> query = Maps.newHashMap();
//                        query.put("type", ConstantForMES.CRAFT_GYLX);
//                        query.put("id", itemIds);
//                        processCheckDao.removeBacthById(query);
//                    }

                    if(craftItemDo.getWhetherExamine()==1){

                        JSONArray project = proPros.getJSONArray("pro");
                        List<ProcessCheckDO> processCheckDos = JSON.parseArray(project.toString(), ProcessCheckDO.class);

                        if (Objects.nonNull(deletProcessCheckIds)&&deletProcessCheckIds.length > 0){
                            Map<String, Object> query = Maps.newHashMap();
                            query.put("type", ConstantForMES.CRAFT_GYLX);
                            query.put("id", deletProcessCheckIds);
                            processCheckDao.removeBacthById(query);
                        }
                        for(int k=0;k<processCheckDos.size();k++){
                            ProcessCheckDO processCheckDo = processCheckDos.get(k);
                            if (Objects.nonNull(processCheckDo.getId())) {
                                processCheckDao.update(processCheckDo);

                            } else {
                                processCheckDo.setType(ConstantForMES.CRAFT_GYLX);
                                processCheckDo.setForeignId(craftItemDo.getId());
                                processCheckDao.save(processCheckDo);
                            }
                        }
                    }
                }
                return R.ok();
            }
            //"明细行数据为空！"
            return R.error(messageSourceHandler.getMessage("apis.mes.process.detaiNoull",null));

        } else {
            //新增

            //<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
            String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.CRAFT_GYLX, new Date());
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("maxNo", prefix);
            params.put("offset", 0);
            params.put("limit", 1);
            List<CraftDO> list = craftDao.list(params);
            String suffix = null;
            if (list.size() > 0) {
                suffix = list.get(0).getCode();
            }
            craftDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
            craftDO.setAuditSign(ConstantForMES.WAIT_AUDIT);//设置为 待审核

            craftDao.save(craftDO);

            if (!"".equals(processAndProject)) {

                List<CraftItemDO> craftItemDos = JSON.parseArray(processAndProject, CraftItemDO.class);

                JSONArray processAndPro = JSONArray.parseArray(processAndProject);

                for (int i = 0; i < craftItemDos.size(); i++) {
                    //工艺明细表
                    CraftItemDO craftItemDo = craftItemDos.get(i);
                    craftItemDo.setCraftId(craftDO.getId());
                    craftItemDao.save(craftItemDo);

                    if(craftItemDo.getWhetherExamine()==1){
                        //处理明保养项目表
                        JSONObject proPros = processAndPro.getJSONObject(i);
                        JSONArray project = proPros.getJSONArray("pro");
                        List<ProcessCheckDO> processCheckDos = JSON.parseArray(project.toString(), ProcessCheckDO.class);

                        for (int j=0;j<processCheckDos.size();j++){

                            ProcessCheckDO processCheckDo = processCheckDos.get(j);
                            processCheckDo.setType(ConstantForMES.CRAFT_GYLX);
                            processCheckDo.setForeignId(craftItemDo.getId());
                            processCheckDao.save(processCheckDo);
                        }
                    }
                }
                return R.ok();
            }
            //"明细行数据为空！"
            return R.error(messageSourceHandler.getMessage("apis.mes.process.detaiNoull",null));
        }
    }


    @Override
    public R auditAndUnOfCraft(Long craftId, Long auditManIds, int sign) {

        CraftDO getCraftDO = craftDao.get(craftId);
        if (getCraftDO != null) {
            CraftDO craftDo = new CraftDO();
            if (sign == 0 && Objects.equals(getCraftDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)) {
                craftDo.setAuditSign(ConstantForMES.OK_AUDITED);
                craftDo.setAuditId(ShiroUtils.getUserId());
            } else if (sign == 0 && Objects.equals(getCraftDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
                //已审核请勿重复操作！
                return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
            } else if (sign == 1 && Objects.equals(getCraftDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
                craftDo.setAuditSign(ConstantForMES.WAIT_AUDIT);
                craftDo.setAuditId(0L);
            } else if(sign == 1 && Objects.equals(getCraftDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)) {
                //单据为待审状态请勿重复操作！
                return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
            }

                craftDo.setId(craftId);
            craftDao.update(craftDo);

            return R.ok();
        }
        //"此参数无数据！"
        return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return craftDao.listForMap(map);
    }

    @Override
    public int countListForMap(Map<String, Object> map) {
        return craftDao.countListForMap(map);
    }


    @Override
    public  R getCraftOfDetail(Long craftId){
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(4);
        results.put("id", craftId);
        Map<String, Object> listForMap = craftDao.getOneCraftDetail(results);
        results.put("craftId", craftId);
        List<Map<String, Object>> itemDetal = craftItemDao.itemDetalByCraftId(results);
        if(!itemDetal.isEmpty()){
            for (int i = 0; i < itemDetal.size(); i++) {
                Map<String, Object> listOfOne = itemDetal.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("foreignId", listOfOne.get("itemId"));
                map.put("type", ConstantForMES.CRAFT_GYLX);
                List<Map<String, Object>> detailOfProcess = processCheckService.getDetailByProcessId(map);
                listOfOne.put("proDetail",detailOfProcess);
            }
        }
        results.clear();
        results.put("craftDetal",listForMap);
        results.put("itemDetal",itemDetal);
        return R.ok(results);
    }


    @Override
    public  R getProcessOfDetail(Long itemId){
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(4);
        results.put("foreignId",itemId);//foreignId
        results.put("type",ConstantForMES.CRAFT_GYLX);
        List<Map<String, Object>> processCheckDetals = processCheckDao.getDetailByProcessId(results);
        results.clear();
        results.put("processCheckDetals",processCheckDetals);
        return R.ok(results);
    }

    @Override
    public R  deletOfBatch(Long[] craftId){
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(4);
        results.put("id",craftId);
        //验证
        int lines = craftDao.canDelet(results);//178待审核状态下数量
        int rows = craftDao.isQuote(results);//等于0 说明未被工序计划关联，可删
        if(craftId.length==lines && rows==0){
            //物理删除
            craftDao.batchRemove(craftId);

            //获取子表主键
            List<Map<String, Object>> itemIds = craftItemDao.getItemIds(results);
            Long[] ids =new Long[itemIds.size()];
            if(itemIds.size()>0){
                for (int i=0;i<itemIds.size();i++){
                    ids[i]=Long.valueOf(String.valueOf(itemIds.get(i).get("id")));
                }
            }

            craftItemDao.removeByCraftIds(craftId);

            Map<String, Object> query = Maps.newHashMap();
            query.put("type", ConstantForMES.CRAFT_GYLX);
            query.put("foreignId", ids);
           processCheckDao.removeBacthByforeignId(query);

            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
        }
    }

    @Override
    public List<Map<String, Object>> listBodyForMap(Map<String, Object> map) {
        return craftDao.listBodyForMap(map);
    }

    @Override
    public int countListBodyForMap(Map<String, Object> map) {
        return craftDao.countListBodyForMap(map);
    }

    @Override
    public R importExcel(MultipartFile file) {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<CraftEntity> craftEntityList;
        try {
            craftEntityList = ExcelImportUtil.importExcel(file.getInputStream(), CraftEntity.class, params);
            craftEntityList = craftEntityList.stream().filter(craftEntity -> craftEntity.getCode() != null).collect(Collectors.toList());
        } catch (Exception e) {
            return R.error(messageSourceHandler.getMessage("file.upload.error", null));
        }
        if (craftEntityList.size() > 0) {
            for (CraftEntity craftEntity : craftEntityList) {
                if (
                        StringUtils.isEmpty(craftEntity.getProcessCode())
                ) {
                    return R.error(messageSourceHandler.getMessage("basicInfo.correct.param", null));
                }
            }
            Map<String, Object> param = Maps.newHashMap();
            List<String> codeList = craftEntityList
                    .stream()
                    .map(CraftEntity::getCode)
                    .distinct()
                    .collect(Collectors.toList());
            param.put("codes", codeList);
            List<CraftDO> craftDOList = this.list(param);
            if (craftDOList.size() > 0) {
                List<String> collect = craftDOList
                        .stream()
                        .map(CraftDO::getCode)
                        .collect(Collectors.toList());
                List<String> craftList = codeList
                        .stream()
                        .filter(collect::contains)
                        .collect(Collectors.toList());
                String[] notExist = {craftList.toString()};
                return R.error(messageSourceHandler.getMessage("basicInfo.craft.isExist", notExist));
            }

            List<String> processCodeList = craftEntityList
                    .stream()
                    .map(CraftEntity::getProcessCode)
                    .distinct()
                    .collect(Collectors.toList());

            param.put("codes", processCodeList);
            List<ProcessDO> processDOList = processService.list(param);
            if (processDOList.size() != processCodeList.size()) {
                List<String> collect = processDOList
                        .stream()
                        .map(ProcessDO::getCode)
                        .collect(Collectors.toList());
                processCodeList.removeAll(collect);
                String[] notExist = {processCodeList.toString()};
                return R.error(messageSourceHandler.getMessage("basicInfo.process.notExist", notExist));
            }

            Map<String, List<CraftEntity>> groupCraftEntity = craftEntityList
                    .stream()
                    .collect(Collectors.groupingBy(CraftEntity::getCode));
            List<String> deptList = craftEntityList
                    .stream()
                    .filter(craftEntity -> craftEntity.getDeptName() != null)
                    .map(CraftEntity::getDeptName)
                    .distinct()
                    .collect(Collectors.toList());
            List<String> userList = craftEntityList
                    .stream()
                    .filter(craftEntity -> craftEntity.getOperatorName() != null)
                    .map(CraftEntity::getOperatorName)
                    .distinct()
                    .collect(Collectors.toList());
            List<String> deviceList = craftEntityList
                    .stream()
                    .filter(craftEntity -> craftEntity.getDeviceName() != null)
                    .map(CraftEntity::getDeviceName)
                    .distinct()
                    .collect(Collectors.toList());
            List<DictionaryDO> typeList = dictionaryService.listByType(ConstantForMES.PROCESS_TYPE);

            Map<String, Long> deptMap = Maps.newHashMap();
            if (deptList.size() > 0) {
                param.put("deptNames", deptList);
                List<DeptDO> list = deptService.list(param);
                if (list.size() > 0) {
                    deptMap = list.stream().collect(Collectors.toMap(DeptDO::getName, DeptDO::getDeptId));
                }
            }
            Map<String, Long> userMap = Maps.newHashMap();
            if (userList.size() > 0) {
                param.put("userNames", userList);
                List<UserDO> list = userService.list(param);
                if (list.size() > 0) {
                    userMap = list.stream().collect(Collectors.toMap(UserDO::getName, UserDO::getUserId));
                }
            }
            Map<String, Long> deviceMap = Maps.newHashMap();
            if (deviceList.size() > 0) {
                param.put("deviceNames", deviceList);
                List<DeviceDO> list = deviceService.list(param);
                if (list.size() > 0) {
                    deviceMap = list.stream().collect(Collectors.toMap(DeviceDO::getName, DeviceDO::getId));
                }
            }
            Map<String, Long> typeMap = Maps.newHashMap();
            if (typeList.size() > 0) {
                typeMap = typeList.stream().collect(Collectors.toMap(DictionaryDO::getName, m -> m.getId().longValue()));
            }

            Map<String, Long> idForCode = processDOList
                    .stream()
                    .collect(Collectors.toMap(ProcessDO::getCode, ProcessDO::getId));
            CraftDO craft;
            CraftItemDO craftItem;
            CraftEntity craftEntity;
            Long craftId;
            String isBoolean;
            String number;
            for (String craftCode : groupCraftEntity.keySet()) {
                List<CraftEntity> craftEntities = groupCraftEntity.get(craftCode);
                // 取出BOM主表
                craftEntity = craftEntities.get(0);
                craft = new CraftDO();
                craft.setCode(craftEntity.getCode());
                craft.setName(craftEntity.getName());
                craft.setVersion(craftEntity.getVersion());
                craft.setAuditSign(ConstantForMES.WAIT_AUDIT);
                craft.setUseStatus(1);
                this.save(craft);
                craftId = craft.getId();
                // 取出BOM子表
                for (CraftEntity entity : craftEntities) {
                    craftItem = new CraftItemDO();
                    craftItem.setCraftId(craftId);
                    craftItem.setDemand(entity.getDemand());

                    craftItem.setProcessId(idForCode.get(entity.getProcessCode()));
                    craftItem.setDeptId(typeMap.getOrDefault(entity.getTypeName(), null));
                    craftItem.setDeptId(deptMap.getOrDefault(entity.getDeptName(), null));
                    craftItem.setOperator(userMap.getOrDefault(entity.getOperatorName(), null));
                    craftItem.setDeviceId(deviceMap.getOrDefault(entity.getDeviceName(), null));

                    isBoolean = entity.getAutoDispatch();
                    isBoolean = isBoolean == null ? "否" : isBoolean;
                    craftItem.setAutoDispatch("否".equals(isBoolean) ? 0 : 1);
                    isBoolean = entity.getWhetherOutsource();
                    isBoolean = isBoolean == null ? "否" : isBoolean;
                    craftItem.setWhetherOutsource("否".equals(isBoolean) ? 0 : 1);
                    isBoolean = entity.getWhetherExamine();
                    isBoolean = isBoolean == null ? "否" : isBoolean;
                    craftItem.setWhetherExamine("否".equals(isBoolean) ? 0 : 1);
                    isBoolean = entity.getWhetherCollect();
                    isBoolean = isBoolean == null ? "否" : isBoolean;
                    craftItem.setWhetherCollect("否".equals(isBoolean) ? 0 : 1);


                    number = entity.getStandard();
                    if (NumberUtils.isNumber(number)) {
                        craftItem.setStandard(new BigDecimal(number));
                    } else {
                        craftItem.setStandard(BigDecimal.ZERO);
                    }
                    number = entity.getTotalHour();
                    if (NumberUtils.isNumber(number)) {
                        craftItem.setTotalHour(new BigDecimal(number));
                    } else {
                        craftItem.setTotalHour(BigDecimal.ZERO);
                    }
                    number = entity.getManHour();
                    if (NumberUtils.isNumber(number)) {
                        craftItem.setManHour(new BigDecimal(number));
                    } else {
                        craftItem.setManHour(BigDecimal.ZERO);
                    }
                    number = entity.getLabourPrice();
                    if (NumberUtils.isNumber(number)) {
                        craftItem.setLabourPrice(new BigDecimal(number));
                    } else {
                        craftItem.setLabourPrice(BigDecimal.ZERO);
                    }

                    craftItemDao.save(craftItem);
                }

            }
            return R.ok();
        }
        return R.error();
    }


}
