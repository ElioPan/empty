package com.ev.apis.controller.custom;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.domain.SupplierDO;
import com.ev.custom.domain.SupplierLinkmanDO;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.SupplierLinkmanService;
import com.ev.custom.service.SupplierService;
import com.ev.custom.vo.SupplierEntity;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.system.domain.DeptDO;
import com.ev.system.service.DeptService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created  on 2019-8-20.
 */
@Api(value = "/",tags = "供应商")
@RestController
public class SupplierApiController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierLinkmanService supplierLinkmanService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/supplier/addServiceGroup", method = RequestMethod.POST, apiTitle = "增加供应商")
    @ApiOperation("增加供应商")
    @Transactional(rollbackFor = Exception.class)
    public R addServiceOne(SupplierDO supplierDO,
                                 @ApiParam(value = "联系人", required = true) @RequestParam(value = "linkname", defaultValue = "", required = true) String linkname) {


        Map<String,Object>  paramy= new HashMap<String,Object>();
        paramy.put("name",supplierDO.getName().trim());
        int lines = supplierService.checkSave(paramy);
        if(lines>0){
            return  R.error(messageSourceHandler.getMessage("apis.mes.clientSupplier.duplicationOfName",null));
        }

        if(StringUtils.isEmpty(supplierDO.getCode()) || supplierDO.getCode().startsWith(Constant.GYS)){
            Map<String,Object> param = Maps.newHashMap();
            param.put("maxNo",Constant.GYS);
            param.put("offset", 0);
            param.put("limit", 1);
            List<SupplierDO> list = supplierService.list(param);
            supplierDO.setCode(DateFormatUtil.getWorkOrderno(Constant.GYS,list.size()>0?list.get(0).getCode():null,4));
        }

        if(StringUtils.isNotEmpty(supplierDO.getCode())&&!(supplierDO.getCode().startsWith(Constant.GYS))){
            paramy.clear();
            paramy.put("code",supplierDO.getCode());
            if(supplierService.checkSave(paramy)>0){
                return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
            }
        }
            supplierDO.setName(supplierDO.getName().trim());
            supplierDO.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());

        //按照之前表设计将联系人保存在supplierLinkman表中
        SupplierLinkmanDO supplierLinkmanDO = new SupplierLinkmanDO();
            supplierLinkmanDO.setName(linkname);
            supplierLinkmanDO.setPhone(supplierDO.getPhone());

        supplierService.addSupplierOne(supplierDO,supplierLinkmanDO);
        return R.ok();

    }

    @EvApiByToken(value = "/apis/supplier/editdetail", method = RequestMethod.POST, apiTitle = "修改供应商")
    @ApiOperation("修改供应商")
    @Transactional(rollbackFor = Exception.class)
    public R changedetail(SupplierDO supplierDO,
                          @ApiParam(value = "联系人", required = false) @RequestParam(value = "linkname", defaultValue = "", required = false) String linkname) {

        Map<String,Object>  paramy= new HashMap<String,Object>();
        SupplierDO getSupplierDo = supplierService.get(supplierDO.getId());
        if(getSupplierDo!=null){
            if(!Objects.equals(getSupplierDo.getName().trim(),supplierDO.getName())){
                paramy.put("name",supplierDO.getName().trim());
                int lines = supplierService.checkSave(paramy);
                if(lines>0){
                    return  R.error(messageSourceHandler.getMessage("apis.mes.clientSupplier.duplicationOfName",null));
                }
            }
        }else{
            return  R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }

        supplierDO.setName(supplierDO.getName().trim());
        supplierDO.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());

        int count = supplierService.update(supplierDO);

        SupplierLinkmanDO supplierLinkmanDO = new SupplierLinkmanDO();
        supplierLinkmanDO.setName(( null==linkname||"".equals(linkname))?null:linkname);
        supplierLinkmanDO.setPhone(supplierDO.getPhone());
        supplierLinkmanDO.setSupplierId(supplierDO.getId());
        int linkUpdates = supplierLinkmanService.updateDetail(supplierLinkmanDO);

        if (count > 0 ) {
            return R.ok();
        } else {
            //"修改错误，请检查参数！"
            return R.error(messageSourceHandler.getMessage("common.device.saveChange",null));
        }
    }



    @EvApiByToken(value = "/apis/supplier/supplierSubmit", method = RequestMethod.POST, apiTitle = "审核供应商")
    @ApiOperation("审核供应商")
    @Transactional(rollbackFor = Exception.class)
    public R submitSupplier(@ApiParam(value = "供应商id", required = true) @RequestParam(value = "id", defaultValue = "", required = true)Long id ){
        SupplierDO supplierDO= supplierService.get(id);
        if(Objects.nonNull(supplierDO)){
            if(Objects.equals(supplierDO.getStatus(),ConstantForGYL.WAIT_AUDIT.intValue())){
                supplierDO.setStatus(ConstantForGYL.OK_AUDITED.intValue());
                supplierDO.setAuditId(ShiroUtils.getUserId());
                supplierService.update(supplierDO);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/supplier/goBackSubmit", method = RequestMethod.POST, apiTitle = "反审核供应商")
    @ApiOperation("反审核供应商")
    @Transactional(rollbackFor = Exception.class)
    public R goBackSubmitSupplier(@ApiParam(value = "供应商id", required = true) @RequestParam(value = "id", defaultValue = "", required = true)Long id ){
        SupplierDO supplierDO= supplierService.get(id);
        if(Objects.nonNull(supplierDO)){
            if(Objects.equals(supplierDO.getStatus(),ConstantForGYL.OK_AUDITED.intValue())){
                supplierDO.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());
                supplierDO.setAuditId(0L);
                supplierService.update(supplierDO);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }


    @EvApiByToken(value = "/apis/supplier/listServiceGroup", method = RequestMethod.POST, apiTitle = "供应商列表查询")
    @ApiOperation("供应商列表查询")
    public R listServices(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1", required = true) int pageno,
                           @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20", required = true) int pagesize,
                           @ApiParam(value = "单位名称/联系人", required = false) @RequestParam(value = "name", defaultValue = "", required = false) String name,
                          @ApiParam(value = "部门id", required = false) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                          @ApiParam(value = "状态", required = false) @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
                          @ApiParam(value = "审核状态", required = false) @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
                          @ApiParam(value = "税号", required = false) @RequestParam(value = "taxNumber", defaultValue = "", required = false) String taxNumber,
                          @ApiParam(value = "单位类型id", required = false) @RequestParam(value = "type", defaultValue = "", required = false) Integer type) {

        String idPath=null;
        if(null!=deptId&&!"".equals(deptId)){
            DeptDO deptDO = deptService.get(deptId);
            idPath=deptDO.getIdPath();
        }

        Map<String, Object> query = Maps.newHashMap();
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query. put("idPath",idPath);
        query.put("type", type);
        query.put("name", name);
        query.put("linkname", name);
        query.put("taxNumber", taxNumber);
        query.put("useStatus", useStatus);
        query.put("status", status);

        List<Map<String, Object>> checkLists = supplierService.checkListByparamete(query);
        int counts = supplierService.countCheckListByparam(query);
        Map<String, Object> resultList = new HashMap<String, Object>();
        if (checkLists.size() > 0 || checkLists != null) {
            DsResultResponse resultPonse = new DsResultResponse() {{
                setDatas(checkLists);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(counts);
                setTotalPages((Integer) ((counts + pagesize - 1) / pagesize));
            }};
            resultList.put("data", resultPonse);
            return R.ok(resultList);

        } else {
            resultList.put("data", "");
            return R.ok(resultList);
        }
    }

    @EvApiByToken(value = "/apis/supplier/deletedetail", method = RequestMethod.POST, apiTitle = "删除往来单位")
    @ApiOperation("删除供应商")
    public R deleDetail( @ApiParam(value = "往来单位id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id) {

        Long ids[] =new Long[]{id};
        supplierService.deletOfDevices(ids);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/supplier/deletedetailMore", method = RequestMethod.POST, apiTitle = "批量删除供应商")
    @ApiOperation("批量删除供应商")
    @Transactional(rollbackFor = Exception.class)
    public R deleMuchDetail( @ApiParam(value = "往来单位ids:格式Long[]", required = true) @RequestParam(value = "ids", defaultValue = "", required = true) Long[] ids) {
        R r = supplierService.deletOfDevices(ids);
        return  r;
    }

    @EvApiByToken(value = "/apis/supplier/groupDetail", method = RequestMethod.POST, apiTitle = "供应商详细信息")
    @ApiOperation("供应商详细信息")
    public R gropDetails( @ApiParam(value = "往来单位id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id) {

        Map<String,Object> query =new HashMap<String,Object>();
        query.put("id",id);
        List<Map<String, Object>> checkListByparamete = supplierService.checkListByparamete(query);
        query.remove("id");
        if (!checkListByparamete.isEmpty() ) {
            query.put("data",checkListByparamete);
            return R.ok(query);
        } else {
            query.put("data","");
            return R.ok(query);
        }
    }

    @EvApiByToken(value = "/apis/supplier/groupSupplier", method = RequestMethod.POST, apiTitle = "供应商")
    @ApiOperation("供应商")
    public R supplierList(
            @ApiParam(value = "供应商名称", required = false) @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String,Object> query =new HashMap<String,Object>();
        query.put("name",name);

        List<Map<String, Object>> proList = supplierService.oneTypeGroupList(query);

        if (proList!=null&&proList.size()>0){
            query.put("data",proList);
            return R.ok(query);
        }else{
            query.put("data","");
            return R.ok(query);
        }
    }
//
//    @EvApiByToken(value = "/apis/supplier/groupProductor", method = RequestMethod.POST, apiTitle = "生产商")
//    @ApiOperation("生产商")
//    public R productorList(){
//        Map<String,Object> query =new HashMap<String,Object>();
////        query.put("type",Constant.SEVICE_MANUFACTURER);
//        List<Map<String, Object>> proList = supplierService.oneTypeGroupList(query);
////        query.remove("type");
//        if (proList!=null&&proList.size()>0){
//            query.put("data",proList);
//            return R.ok(query);
//        }else{
//            query.put("data","");
//            return R.ok(query);
//        }
//    }
//
//    @EvApiByToken(value = "/apis/supplier/groupService", method = RequestMethod.POST, apiTitle = "服务商")
//    @ApiOperation("服务商")
//    public R serviceList(){
//        Map<String,Object> query =new HashMap<String,Object>();
////        query.put("type",Constant.SEVICE_FACILITATOR);
//        List<Map<String, Object>> proList = supplierService.oneTypeGroupList(query);
////        query.remove("type");
//        if (proList!=null&&proList.size()>0){
//            query.put("data",proList);
//            return R.ok(query);
//        }else{
//            query.put("data","");
//            return R.ok(query);
//        }
//    }



    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/supplier/importExcel", method = RequestMethod.POST, apiTitle = "供应商信息导入")
    @ApiOperation("供应商信息导入")
    @Transactional(rollbackFor = Exception.class)
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        List<SupplierEntity> supplierEntityList = ExcelImportUtil.importExcel(file.getInputStream(), SupplierEntity.class, params);
        if (supplierEntityList.size() > 0) {
            List<String> codeNoneEmptyList = supplierEntityList.stream()
                    .filter(supplierEntity -> StringUtils.isNoneEmpty(supplierEntity.getCode()))
                    .map(SupplierEntity::getCode)
                    .collect(Collectors.toList());
            List<String> allCode = supplierService.getAllCode();
            if (allCode.size() > 0 && codeNoneEmptyList.size() > 0) {
                allCode.addAll(codeNoneEmptyList);
                List<String> duplicateElements = ListUtils.getDuplicateElements(allCode);
                // 若存在重复的元素提示用户
                if (duplicateElements.size() > 0) {
                    String[] arg = {StringUtils.join(duplicateElements.toArray(), ",")};
                    return R.error(messageSourceHandler.getMessage("basicInfo.code.isPresence", arg));
                }
            }

            Map<String, Object> param = Maps.newHashMap();
            param.put("maxNo", Constant.GYS);
            param.put("offset", 0);
            param.put("limit", 1);
            List<SupplierDO> list = supplierService.list(param);
            String firstCode = DateFormatUtil.getWorkOrderno(Constant.GYS, list.size() > 0 ? list.get(0).getCode() : null, 4);

            List<SupplierEntity> codeEmptyList = supplierEntityList.stream()
                    .filter(supplierEntity -> StringUtils.isEmpty(supplierEntity.getCode()) || supplierEntity.getCode().startsWith(Constant.GYS)).collect(Collectors.toList());
            for (SupplierEntity supplierEntity : codeEmptyList) {
                supplierEntity.setCode(firstCode);
                assert firstCode != null;
                firstCode = Constant.GYS + StringUtils.autoGenericCode(firstCode.substring(Constant.GYS.length()), 4);
            }

            List<DictionaryDO> dictionaryDOS = dictionaryService.listByType(Constant.BANK);

            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            List<SupplierDO> supplierDOs = Lists.newArrayList();
            List<SupplierLinkmanDO> supplierLinkmanDOs = Lists.newArrayList();
            SupplierDO supplierDO;
            SupplierLinkmanDO supplierLinkmanDO;
            for (SupplierEntity supplierEntity : supplierEntityList) {
                supplierDO = new SupplierDO();
                supplierLinkmanDO = new SupplierLinkmanDO();
                BeanUtils.copyProperties(supplierEntity, supplierDO);
                String bank;
                for (DictionaryDO dictionaryDO : dictionaryDOS) {
                    bank = supplierEntity.getBank();
                    if (Objects.equals(dictionaryDO.getName(),bank)) {
                        supplierDO.setBank(dictionaryDO.getId());
                        break;
                    }
                }
                supplierDO.setStatus(ConstantForMES.WAIT_AUDIT);
                // 使用状态(1是0否)
                supplierDO.setDelFlag(0);
                supplierDO.setUseStatus(1);
                supplierDO.setCreateBy(userId);
                supplierDO.setCreateTime(now);
                supplierDOs.add(supplierDO);

                supplierLinkmanDO.setName(supplierEntity.getLinkName());
                supplierLinkmanDO.setPhone(supplierEntity.getLinkPhone());
                supplierLinkmanDO.setCreateBy(userId);
                supplierLinkmanDO.setCreateTime(now);
                supplierLinkmanDOs.add(supplierLinkmanDO);
            }
            supplierService.batchSave(supplierDOs);
            for (int i = 0; i < supplierDOs.size(); i++) {
                supplierLinkmanDOs.get(i).setSupplierId(supplierDOs.get(i).getId());
            }
            supplierLinkmanService.batchSave(supplierLinkmanDOs);
        }
        return R.ok();
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/supplier/exportExcel", method = RequestMethod.GET, apiTitle = "导出供应商")
    @ApiOperation("导出供应商")
    public void exportExcel(
            @ApiParam(value = "单位名称/联系人") @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @ApiParam(value = "部门id") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
            @ApiParam(value = "状态") @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
            @ApiParam(value = "审核状态") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
            @ApiParam(value = "税号") @RequestParam(value = "taxNumber", defaultValue = "", required = false) String taxNumber,
            @ApiParam(value = "单位类型id") @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        String idPath = null;
        if (StringUtils.isNoneEmpty(deptId)) {
            DeptDO deptDO = deptService.get(Long.parseLong(deptId));
            idPath = deptDO.getIdPath();
        }
        Map<String, Object> query = Maps.newHashMap();
        query.put("idPath", idPath);
        query.put("type", type);
        query.put("name", name);
        query.put("linkname", name);
        query.put("taxNumber", taxNumber);
        query.put("useStatus", useStatus);
        query.put("status", status);

        List<Map<String, Object>> data = supplierService.listForMap(query);
        ClassPathResource classPathResource = new ClassPathResource("poi/supplier.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "供应商");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
