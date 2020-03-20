package com.ev.apis.controller.custom;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSONArray;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.custom.vo.DeviceEntity;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.ev.system.service.UserService;
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

//import com.ev.custom.service.*;


/**
 * @author yunian
 * @date 2018/6/17
 */
@Api(value = "/",tags = "设备管理API")
@RestController
public class DeviceApiController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceArgService deviceArgService;
    @Autowired
    private DeviceInsuranceService deviceInsuranceService;
    @Autowired
    private DeviceSuperviseService deviceSuperviseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DeviceSpareService deviceSpareService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;




    @EvApiByToken(value = "/apis/device/countOfSpectaculars", method = RequestMethod.POST, apiTitle = "运行效率看板")
    @ApiOperation("运行效率看板")
    public R deviceCountOfAll() {

        R r= deviceService.countOfAllDevice();

        return r;
    }

    /**
     * 提供未和其他设备绑定的设备
     */
    @EvApiByToken(value = "/apis/device/getFatherDevice", method = RequestMethod.POST, apiTitle = "主设备")
    @ApiOperation("主设备")
    public R fatherDevice() {
        List<Map<String, Object>> mapss = deviceService.deviceHaveNoChildDevice();
        Map<String, Object> results = Maps.newHashMap();
        if (mapss.size() > 0) {
            results.put("data", mapss);
            return R.ok(results);
        } else {
            return R.error();
        }
    }


    /**
     *
     */
    @EvApiByToken(value = "/apis/device/allDevice", method = RequestMethod.POST, apiTitle = "所有设备")
    @ApiOperation("所有设备")
    public R allDevices() {
        List<Map<String, Object>> mapss = deviceService.getAllDevices();
        Map<String, Object> results = Maps.newHashMap();
        if (mapss != null && mapss.size() > 0) {
            results.put("data", mapss);
            return R.ok(results);
        } else {
            results.put("data", "");
            return R.ok(results);
        }
    }


    @EvApiByToken(value = "/apis/device/advancedQuery", method = RequestMethod.POST, apiTitle = "设备台账-查询/高级查询")
    @ApiOperation("设备台账-查询/高级查询")
    public R advancedQueryList(@ApiParam(value = "设备名字或编码", required = false) @RequestParam(value = "nameAndType", defaultValue = "", required = false) String name,
                               @ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                               @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                               @ApiParam(value = "部门id", required = false) @RequestParam(value = "deptId", required = false) Long deptId,
                               @ApiParam(value = "设备类型id", required = false) @RequestParam(value = "type", required = false) Integer type,
                               @ApiParam(value = "使用状况id", required = false) @RequestParam(value = "using_status", required = false) Integer usingStatus,
                               @ApiParam(value = "安装地点", required = false) @RequestParam(value = "site", required = false) String site,
                               @ApiParam(value = "规格型号", required = false) @RequestParam(value = "model", required = false) String model,
                               @ApiParam(value = "生产厂商id", required = false) @RequestParam(value = "factoryId", required = false) Long factoryId,
                               @ApiParam(value = "负责人id", required = false) @RequestParam(value = "userId", required = false) Long userId,
                               @ApiParam(value = "供应商id", required = false) @RequestParam(value = "supplierId", required = false) Long supplierId,
                               @ApiParam(value = "购置时间Start", required = false) @RequestParam(value = "buyTimeStart", required = false) String buyTimeStart,
                               @ApiParam(value = "购置时间End", required = false) @RequestParam(value = "buyTimeEnd", required = false) String buyTimeEnd) {
        Map<String, Object> resuls = new HashMap<String, Object>();
        String idPath=null;
        if(Objects.nonNull(deptId)){
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.nonNull(deptDO)?deptDO.getIdPath():null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params. put("serialno", name);
        params. put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params. put("idPath",idPath);
        //params. put("deptId", deptId);
        params. put("type", type);
        params. put("usingStatus", usingStatus);
        params. put("site", site);
        params. put("model", model);
        params. put("factoryId", factoryId);
        params. put("userId", userId);
        params. put("supplierId", supplierId);
        params. put("buyTimeStart", buyTimeStart);
        params. put("buyTimeEnd", buyTimeEnd);

        int counts = deviceService.advancedCounts(params);

        List<Map<String, Object>> details = deviceService.advancedQueryLists(params);

        DsResultResponse resultResponse = new DsResultResponse();
        if (details.size() > 0) {
            resultResponse.setDatas(details);
            resultResponse.setPageno(pageno);
            resultResponse.setPagesize(pagesize);
            resultResponse.setTotalRows(counts);
            resultResponse.setTotalPages((Integer) ((counts + pagesize - 1) / pagesize));
            resuls.put("data", resultResponse);
            return R.ok(resuls);
        } else {
            resuls.put("data", details);
            return R.ok(resuls);
        }
    }


    @EvApiByToken(value = "/apis/device/getDeviceByCode",method = RequestMethod.POST,apiTitle = "根据设备编号获取设备信息")
    @ApiOperation("根据设备编号获取设备信息")
    public R getDevByCode(@ApiParam(value = "设备编号",required = true) @RequestParam(value = "serialno",defaultValue = "",required = true) String serialno){
        Map<String,Object> results = Maps.newHashMap();
        results.put("serialno",serialno);
        try{
            Map<String, Object> oneDeviceDetail = deviceService.oneDeviceDetail(results);
            if(oneDeviceDetail.size()>0){
                return R.ok(oneDeviceDetail);
            }
            results.remove("serialno");
            return R.ok(results);

        }catch(Exception e){
            //"编码存储重复请检查清理!"
            return  R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
        }
    }


    //=================================================================================================================

    @EvApiByToken(value = "/apis/device/add", method = RequestMethod.POST, apiTitle = "新增设备信息")
    @ApiOperation("新增设备信息")
    public R add(DeviceDO deviceDo) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> query = Maps.newHashMap();
        query.put("serialno",deviceDo.getSerialno());

        int count = deviceService.count(query);
        if(!(count>0)){
            if (deviceService.save(deviceDo) > 0) {
                results.put("data",deviceDo.getId());
                return R.ok(results);
            }
            //"新增失败！请检查参数"
            return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
        }else{
            return R.error("设备编码："+deviceDo.getSerialno()+"已存在！请设置新编码");
        }
    }

    @EvApiByToken(value = "/apis/device/edit", method = RequestMethod.POST, apiTitle = "修改设备信息")
    @ApiOperation("修改设备信息")
    public R edit(DeviceDO device, @ApiParam(value = "设备ID", required = false) @RequestParam(value = "deviceId", defaultValue = "", required = true) Long deviceId) {

        Map<String, Object> query = Maps.newHashMap();
        query.put("serialno", device.getSerialno());
        int countSer = deviceService.count(query);
        query.put("id", deviceId);
        int rowsSer = deviceService.count(query);
        if (countSer >0 && rowsSer == 0) {

            return R.error("设备编码："+device.getSerialno()+"已存在！请设置新编码");
        }else if (countSer==0&&rowsSer == 0){
            if (deviceService.update(device) > 0) {
                return R.ok();
            }
            return R.error();
        }else if (countSer==1&&rowsSer == 1){
            if (deviceService.update(device) > 0) {
                return R.ok();
            }
            return R.error();
        }else if (countSer>1&&rowsSer == 1){
            return R.error("设备编码："+device.getSerialno()+"已存在！请设置新编码");
        }
        return R.error();
    }

    /**
     *将物理删除改为逻辑删除
     */
    @EvApiByToken(value = "/apis/device/delete", method = RequestMethod.POST, apiTitle = "删除设备信息")
    @ApiOperation("删除设备信息")
    public R delete(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "", required = true) Long deviceId) {

            Long id[]=new Long[]{deviceId};
            R r = deviceService.apiDelete(id);
            return r;

    }


    @EvApiByToken(value = "/apis/device/allDelete", method = RequestMethod.POST, apiTitle = "批量删除设备信息")
    @ApiOperation("批量删除设备信息")
    @Transactional(rollbackFor = Exception.class)
    public R deleteAll(@ApiParam(value = "设备IDs:Long[]", required = true) @RequestParam(value = "deviceId", defaultValue = "", required = true) Long[] deviceId) {
        if (deviceId.length>0) {
            R r = deviceService.apiDelete(deviceId);

            return r;
        }
        return R.error("参数为空！");
    }

    @EvApiByToken(value = "/apis/device/getDeviceQRCode",method = RequestMethod.POST,apiTitle = "获取设备二维码页面信息")
    @ApiOperation("获取设备二维码页面信息")
    public R getDeviceQRCode(@ApiParam(value = "设备ID",required = true) @RequestParam(value = "deviceId",defaultValue = "",required = true) Long deviceId){
        Map<String,Object> results = Maps.newHashMap();
        results = this.deviceService.getDeviceQRCode(deviceId);
        return R.ok(results);
    }

    /**
     * 提供所有设备的信息
     */
    @EvApiByToken(value = "/apis/device/list",method = RequestMethod.POST,apiTitle = "获取设备列表信息")
    @ApiOperation("获取设备列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                  @ApiParam(value = "设备类型id", required = false) @RequestParam(value = "type", required = false) Integer type,
                  @ApiParam(value = "设备名称",required = false) @RequestParam(value = "name",defaultValue = "",required = true)  String name){
        return R.ok(this.deviceService.listApi(pageno,pagesize,name,type));
    }

    @EvApiByToken(value = "/apis/device/detail", method = RequestMethod.POST, apiTitle = "获取设备详情页信息")
    @ApiOperation("获取设备详情页信息")
    public R getDeviceDetail(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "", required = true) Long deviceId) {

        Map<String, Object> results = Maps.newHashMap();
        results.put("id", deviceId);
        Map<String, Object> oneDetail = this.deviceService.oneDeviceDetail(results);
        results.remove("id");
        if (oneDetail != null&&oneDetail.size()>0) {
            results.put("data", oneDetail);
            return R.ok(results);
        } else {
            results.put("data",oneDetail);
            return R.ok(results);
        }
    }

    @EvApiByToken(value = "/apis/device/getDevicesByUser",method = RequestMethod.POST)
    @ApiOperation("获取该人员权限的设备列表信息")
    public R getDevicesByUser(@ApiParam(value = "用户ID",required = true) @RequestParam(value = "userId",defaultValue = "",required = true) Long userId){
        Map<String,Object> results = Maps.newHashMap();
        results = this.deviceService.getDevicesByUser(userId);
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/deviceArg/addDeviceArg",method = RequestMethod.POST)
    @ApiOperation("新增设备参数信息")
    public R addDeviceArg(DeviceArgDO deviceArg){
        Map<String,Object> results = Maps.newHashMap();
        String code="SBCS"+DateFormatUtil.getWorkOrderno();
        deviceArg.setCode(code);
        results = this.deviceArgService.addDeviceArg(deviceArg);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/deviceArg/delDeviceArg",method = RequestMethod.POST)
    @ApiOperation("删除设备参数信息")
    @Transactional
    public R delDeviceArg(@ApiParam(value = "ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true) Long id){
        Map<String,Object> results = Maps.newHashMap();
        results = this.deviceArgService.delDeviceArg(id);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/deviceArg/deviceArgList",method = RequestMethod.POST)
    @ApiOperation("查询该设备参数列表信息")
    public R deviceArgList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                           @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                           @ApiParam(value = "设备ID",required = false) @RequestParam(value = "deviceId",defaultValue = "",required = true)  Long deviceId){
        Map<String,Object> results = Maps.newHashMap();
        results = this.deviceArgService.deviceArgList(pageno,pagesize,deviceId);
        return R.ok(results);
    }



    //============================安监管理=====================================

    @EvApiByToken(value = "/apis/deviceSupervise/list", method = RequestMethod.POST, apiTitle = "获取安监管理列表")
    @ApiOperation("获取安监管理列表")
    public R listSupervi(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "设备ID") @RequestParam(value = "deviceid", defaultValue = "", required = false) long deviceid) {
        //查询列表数据
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("deviceid", deviceid);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
        }};
        List<Map<String, Object>> maps = deviceSuperviseService.listPageMap(params);
        int total = deviceSuperviseService.count(params);
        Map<String, Object> results = Maps.newHashMap();

        if (maps != null && maps.size() > 0) {
            DsResultResponse resultResponse = new DsResultResponse() {{
                setDatas(maps);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(total);
                setTotalPages((Integer) ((total + pagesize - 1) / pagesize));
            }};
            results.put("data", resultResponse);
            return R.ok(results);
        }else{
            results.put("data", "");
            return R.ok(results);
        }

     }

    @EvApiByToken(value = "/apis/deviceSupervise/add",method = RequestMethod.POST,apiTitle = "新增安监管理")
    @ApiOperation("新增安监管理")
    public R addSupervi(DeviceSuperviseDO deviceSupervise){
        if(deviceSuperviseService.save(deviceSupervise)>0){
            return R.ok();
        }
        //"新增失败，检查参数是否正确"
        return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
    }

    @EvApiByToken(value = "/apis/deviceSupervise/remove",method = RequestMethod.POST,apiTitle = "删除/批量删除 安监管理记录")
    @ApiOperation("删除/批量删除 安监管理记录")
    @Transactional
    public R removeSupervi( @ApiParam(value = "安监信息主键IDs")@RequestParam(value = "deviceid",defaultValue = "",required = true)Long[] id){
        if(deviceSuperviseService.batchRemove(id)>0){
            return R.ok();
        }
        //"删除失败，检查参数是否正确"
        return R.error();
    }


    @EvApiByToken(value = "/apis/deviceSupervise/update",method = RequestMethod.POST,apiTitle = "修改安监管理记录")
    @ApiOperation("修改安监管理记录")
    public R update( DeviceSuperviseDO deviceSupervise){
       int updates = deviceSuperviseService.update(deviceSupervise);
       if(updates>0){
           return R.ok();
       }else{
           return R.error(messageSourceHandler.getMessage("common.device.saveChange",null));
       }

    }

//    @EvApiByToken(value = "/apis/deviceSupervise/getOne",method = RequestMethod.POST,apiTitle = "获取单条安监记录")
//    @ApiOperation("获取单条安监记录")
//    public R editSuperOne(@ApiParam(value = "安监信息主键ID")@RequestParam(value = "deviceid",defaultValue = "",required = true)long id){
//        Map<String,Object> result =Maps.newHashMap();
//        DeviceSuperviseDO deviceSupervise = deviceSuperviseService.get(id);
//        if(deviceSupervise!=null){
//            result.put("data",deviceSupervise);
//        }
//        return R.ok(result);
//    }



    //============================保险记录=====================================


    @EvApiByToken(value = "/apis/deviceInsurance/add",method = RequestMethod.POST,apiTitle = "新增保险记录")
    @ApiOperation("新增保险记录")
    public R saveInturan( DeviceInsuranceDO deviceInsurance){
        if(deviceInsuranceService.save(deviceInsurance)>0){
            return R.ok();
        }
        return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
    }

    @EvApiByToken(value = "/apis/deviceInsurance/update",method = RequestMethod.POST,apiTitle = "修改保险记录")
    @ApiOperation("修改保险记录")
    public R updateInturan( DeviceInsuranceDO deviceInsurance){
        deviceInsuranceService.update(deviceInsurance);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/deviceInsurance/deleteone",method = RequestMethod.POST,apiTitle = "删除单条保险记录")
    @ApiOperation("删除单条保险记录")
    @Transactional
    public R removeOneInturan( @ApiParam(value = "保险记录主键ID")@RequestParam(value = "id",defaultValue = "",required = true)long id){
        if(deviceInsuranceService.remove(id)>0){
            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
        }
    }


    @EvApiByToken(value = "/apis/deviceInsurance/deleteMore",method = RequestMethod.POST,apiTitle = "批量删除保险记录")
    @ApiOperation("批量删除保险记录")
    @Transactional
    public R addBanchDelet( @ApiParam(value = "保险记录主键:Long[]")@RequestParam(value = "id",defaultValue = "",required = true)Long[] id){

        int rows = deviceInsuranceService.batchRemove(id);
        if(rows==id.length){
            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
        }
    }


    @EvApiByToken(value = "/apis/deviceInsurance/listPage", method = RequestMethod.POST, apiTitle = "获取保险记录列表")
    @ApiOperation("获取保险记录列表")
    public R list(@ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1",required = false) int pageno,
                  @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20",required = false) int pagesize,
                  @ApiParam(value = "设备ID") @RequestParam(value = "deviceid", defaultValue = "", required = false) Long deviceid) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("deviceid", deviceid);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
        }};

        List<Map<String, Object>> insturanceMaps = deviceInsuranceService.listPageMap(params);
        int count = deviceInsuranceService.count(params);

        Map<String, Object> result = Maps.newHashMap();
        if (insturanceMaps != null ) {
            DsResultResponse resultPonse = new DsResultResponse() {{
                setDatas(insturanceMaps);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(count);
                setTotalPages((Integer) ((count + pagesize - 1) / pagesize));
            }};
            result.put("data", resultPonse);
            return R.ok(result);
        }else{
            result.put("data", "");
            return R.ok(result);
        }

    }


    //=========================================设备文档资料==============================================

    @EvApiByToken(value = "/apis/deviceDocument/listDocPage",method = RequestMethod.POST,apiTitle = "获取设备文档资料列表")
    @ApiOperation("获取设备文档资料列表")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                  @ApiParam(value = "设备id") @RequestParam(value = "device_id",defaultValue = "",required = true)  String device_id,
                  @ApiParam(value = "文档类型") @RequestParam(value = "type",defaultValue = "",required = false)  Long type){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceId",device_id);
        params.put("type",type);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.documentService.listForMap(params);
        int total = this.documentService.countForMap(params);
        if(data!=null ){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/deviceDocument/addDoc", method = RequestMethod.POST, apiTitle = "增加文档资料")
    @ApiOperation("增加文档资料")
    public R save(//@ApiParam(value = "文档信息",required = true) DocumentDO document,
                  @ApiParam(value = "文档名称 ", required = true) @RequestParam(value = "name", defaultValue = "", required = true) String name,
                  @ApiParam(value = "文档类型 ", required = true) @RequestParam(value = "type", defaultValue = "", required = true) long type,
                  @ApiParam(value = "上传人", required = true) @RequestParam(value = "up_user", defaultValue = "", required = true) long upUser,
                  @ApiParam(value = "所属设备id", required = true) @RequestParam(value = "device_id", defaultValue = "", required = true) long deviceId,
                  @ApiParam(value = "所属部门id", required = true) @RequestParam(value = "deptid", defaultValue = "", required = true) String deptid,
                  @ApiParam(value = "来源id", required = true) @RequestParam(value = "source", defaultValue = "", required = true) long source,
                  @ApiParam(value = "上传时间", required = false) @RequestParam(value = "up_time", defaultValue = "", required = false) String  upTime,
                  @ApiParam(value = "版本号", required = false) @RequestParam(value = "version", defaultValue = "", required = false) String version,
                  @ApiParam(value = "备注说明", required = false) @RequestParam(value = "description", defaultValue = "", required = false) String description,
                  @ApiParam(value = "上传文档[{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"},{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"}]", required = false) @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String taglocationappearanceDoc) {

        DocumentDO document = new DocumentDO();
        document.setName(name);
        document.setType(type);
        document.setUpUser(upUser);
        document.setDeviceId(deviceId);
        document.setSource(source);
        document.setUpTime(new Date());
        document.setVersion(version);
        document.setDescription(description);
        document.setCreateTime(new Date());

        try {
            JSONArray nameAndPath = JSONArray.parseArray(taglocationappearanceDoc);

            documentService.save(document);

            contentAssocService.saveList(document.getId(),nameAndPath,Constant.DEVICE_ARTECLE_IMAGE);

            return R.ok();
        } catch (Exception ex) {
            return R.error(ex.getMessage());
        }

    }



    //=========================================附属子设备==============================================


    @EvApiByToken(value = "/apis/deviceChilden/listChild", method = RequestMethod.POST, apiTitle = "当前主设备下子设备列表")
    @ApiOperation("当前主设备下子设备列表")
    public R listChildDevice(@ApiParam(value = "主设备id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id,
                             @ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                             @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize) {
        Map<String, Object> query = new HashMap<String, Object>() {{
            put("parentId", id);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
        }};
        int count = deviceService.count(query);
        Integer totalPages = (Integer) ((count + pagesize - 1) / pagesize);
        List<Map<String, Object>> childList = deviceService.childrenDevice(query);
        Map<String, Object> resultList = new HashMap<String, Object>();
        DsResultResponse resultPonse = new DsResultResponse();
        if (!childList.isEmpty()) {

            resultPonse.setDatas(childList);
            resultPonse.setPageno(pageno);
            resultPonse.setPagesize(pagesize);
            resultPonse.setTotalRows(count);
            resultPonse.setTotalPages((Integer) ((count + pagesize - 1) / pagesize));
        }
        resultList.put("data", resultPonse);
        return R.ok(resultList);
    }


    @EvApiByToken(value = "/apis/deviceChilden/childDevice", method = RequestMethod.POST, apiTitle = "选择子设备列表/子设备查询")
    @ApiOperation("选择子设备列表/子设备查询")
    public R ChildenList(@ApiParam(value = "设备名字或编码", required = false) @RequestParam(value = "name", defaultValue = "", required = false) String name,
                         @ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                         @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                         @ApiParam(value = "部门id", required = false) @RequestParam(value = "deptId", required = false) Integer dept_id,
                         @ApiParam(value = "当前设备id", required = false) @RequestParam(value = "deviceId", required = false) Long deviceId,
                         @ApiParam(value = "设备类型id", required = false) @RequestParam(value = "type", required = false) Integer type) {
        Map<String, Object> query = new HashMap<String, Object>() {{
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
            put("name", name);
            put("serialno", name);  //设备编码
            put("deptId", dept_id);
            put("type", type);      //设备类型
            put("parentId", null);
            put("deviceId",deviceId);
        }};
        int count = deviceService.countOfChildList(query);
        List<Map<String, Object>> childList = this.deviceService.childListDev(query);
        Map<String, Object> resultList = new HashMap<String, Object>();
        if (childList != null) {
            DsResultResponse resultPonse = new DsResultResponse() {{
                setDatas(childList);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(count);
                setTotalPages((Integer) ((count + pagesize - 1) / pagesize));
            }};
            resultList.put("data", resultPonse);
        } else {
            resultList.put("data", "");
        }
        return R.ok(resultList);
    }




    /**
     *  2.主子设备绑定  接口   将选中的单个或多个子设备的parent_id 列下记录主设备的id。
      */
    @EvApiByToken(value = "/apis/devicechild/fatherAddChilds", method = RequestMethod.POST, apiTitle = "添加子设备")
    @ApiOperation("添加子设备")
    public R addChilds(@ApiParam(value = "子设备IDs", required = true) @RequestParam(value = "ids", required = true) Long[] ids,
                       @ApiParam(value = "主设备id", required = true) @RequestParam(value = "id", required = true) long id) {
        int counts = deviceService.updateParentId(id, ids);
        if (counts==(ids.length)) {
            return R.ok();
        } else {
            //"添加失败，检查参数是否正确"
            return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
        }
    }

    /**
     * 3.删除子设备与主设备的绑定关系     将选中的设备主键id在当前主设备的parent_id 列下删除
     */
    @EvApiByToken(value = "/apis/deviceAndchild/unparent", method = RequestMethod.POST, apiTitle = "删除/批量删除子设备")
    @ApiOperation("删除/批量删除子设备")
    @Transactional(rollbackFor = Exception.class)
    public R removeContact(@ApiParam(value = "子设备IDs:格式Long[]", required = true) @RequestParam(value = "ids", required = true) Long[] ids) {

        int counts = deviceService.desvinculao(ids);
        if (counts == (ids.length)) {
            return R.ok();
        } else {
            return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
        }
    }


    //=========================================关联备件=================================================

    /**
     * 1.获取主设备下备件列表
     *
     */
    @EvApiByToken(value = "/apis/sparePart:sparePartList", method = RequestMethod.POST, apiTitle = "当前主设备下备件列表")
    @ApiOperation("当前主设备下备件列表")
    public R relevanceSpareList(@ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                                @ApiParam(value = "主设备ID", required = true) @RequestParam(value = "id", required = true) Long id) {

        Map<String, Object> query = new HashMap<String, Object>() {{
            put("offset",(pageno - 1) * pagesize);
            put("limit",pagesize);
            put("deviceId", id);
        }};
        List<Map<String, Object>> idNameListType = deviceSpareService.RelatedSpareParts(query);
        int count = deviceSpareService.countRelatedSpareParts(query);
        Map<String, Object> resultListMaps = Maps.newHashMap();

        if (idNameListType.size()>0) {
                DsResultResponse resultPonse = new DsResultResponse() {{
                    setDatas(idNameListType);
                    setPageno(pageno);
                    setPagesize(pagesize);
                    setTotalRows(count);
                    setTotalPages((Integer) ((count + pagesize - 1) / pagesize));
                }};
            resultListMaps.put("data", resultPonse);
            return R.ok(resultListMaps);
        }
            return R.ok(resultListMaps);
    }



    /**
     * 添加备件（单条/多条）
     */
    @EvApiByToken(value = "/apis/sparePart/fatherAddchilds", method = RequestMethod.POST, apiTitle = "添加子备件")
    @ApiOperation("添加子备件")
    public R addSpareChilds(@ApiParam(value = "子备件IDs", required = true) @RequestParam(value = "ids", required = true) Long[] ids,
                            @ApiParam(value = "主设备id", required = true) @RequestParam(value = "id", required = true) long id,
                            @ApiParam(value = "仓库ID", required = false) @RequestParam(value = "waerhouse", required = false) Integer  waerhouse) {

        int counts= deviceSpareService.saveSparesPart(ids,id,null);
        if (counts == (ids.length)) {
            return R.ok();
        } else {
            return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
        }
    }

    /**
     * 删除备件（单条/多条）
     */
    @EvApiByToken(value = "/apis/sparePart/deletchilds", method = RequestMethod.POST, apiTitle = "删除/批量删除 子备件")
    @ApiOperation("删除/批量删除 子备件")
    @Transactional
    public R deletSpareChilds(@ApiParam(value = "子备件IDs", required = true) @RequestParam(value = "ids", required = true) Long[] ids,
                            @ApiParam(value = "主设备id", required = true) @RequestParam(value = "id", required = true) long id) {

        int counts= deviceSpareService.removeSpareChilden(ids,id);
        if (counts == (ids.length)) {
            return R.ok();
        } else {
            return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
        }
    }
    
    /**
     * 导入导出
     */

    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/device", method = RequestMethod.GET, apiTitle = "导出物料")
    @ApiOperation("导出物料")
    public void exportExcel(
            @ApiParam(value = "设备名字或编码") @RequestParam(value = "nameAndType", defaultValue = "", required = false) String name,
            @ApiParam(value = "部门id") @RequestParam(value = "deptId", required = false) Long deptId,
            @ApiParam(value = "设备类型id") @RequestParam(value = "type", required = false) Integer type,
            @ApiParam(value = "使用状况id") @RequestParam(value = "using_status", required = false) Integer usingStatus,
            @ApiParam(value = "安装地点") @RequestParam(value = "site", required = false) String site,
            @ApiParam(value = "规格型号") @RequestParam(value = "model", required = false) String model,
            @ApiParam(value = "生产厂商id") @RequestParam(value = "factoryId", required = false) Long factoryId,
            @ApiParam(value = "负责人id") @RequestParam(value = "userId", required = false) Long userId,
            @ApiParam(value = "供应商id") @RequestParam(value = "supplierId", required = false) Long supplierId,
            @ApiParam(value = "购置时间Start") @RequestParam(value = "buyTimeStart", required = false) String buyTimeStart,
            @ApiParam(value = "购置时间End") @RequestParam(value = "buyTimeEnd", required = false) String buyTimeEnd,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();
        String idPath=null;
        if(Objects.nonNull(deptId)){
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.nonNull(deptDO)?deptDO.getIdPath():null;
        }
        param.put("name", StringUtils.sqlLike(name));
        param. put("idPath",idPath);
        param. put("type", type);
        param. put("usingStatus", usingStatus);
        param. put("site", StringUtils.sqlLike(site));
        param. put("model", StringUtils.sqlLike(model));
        param. put("factoryId", factoryId);
        param. put("userId", userId);
        param. put("supplierId", supplierId);
        param. put("buyTimeStart", buyTimeStart);
        param. put("buyTimeEnd", buyTimeEnd);

        List<Map<String, Object>> data = this.deviceService.listForMap(param);

        ClassPathResource classPathResource = new ClassPathResource("poi/device.xlsx");
        Map<String, Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "设备");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);

        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }
    /*导入*/
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/device", method = RequestMethod.POST, apiTitle = "设备信息导入")
    @ApiOperation("设备信息导入")
    @Transactional(rollbackFor = Exception.class)
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        List<DeviceEntity> deviceEntityList = ExcelImportUtil.importExcel(file.getInputStream(), DeviceEntity.class, params);
        if (deviceEntityList.size() > 0) {
            List<String> codeNoneEmptyList = deviceEntityList.stream()
                    .filter(deviceEntity -> StringUtils.isNoneEmpty(deviceEntity.getSerialno()))
                    .map(DeviceEntity::getSerialno)
                    .collect(Collectors.toList());
            List<String> allCode = deviceService.getAllCode();
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
            param.put("maxNo", Constant.SB);
            param.put("offset", 0);
            param.put("limit", 1);
            List<DeviceDO> list = deviceService.list(param);
            String firstCode = DateFormatUtil.getWorkOrderno(Constant.SB, list.size() > 0 ? list.get(0).getSerialno() : null, 4);

            List<DeviceEntity> codeEmptyList = deviceEntityList.stream()
                    .filter(deviceEntity -> StringUtils.isEmpty(deviceEntity.getSerialno()) || deviceEntity.getSerialno().startsWith(Constant.SB)).collect(Collectors.toList());
            for (DeviceEntity deviceEntity : codeEmptyList) {
                deviceEntity.setSerialno(firstCode);
                assert firstCode != null;
                firstCode = Constant.SB + StringUtils.autoGenericCode(firstCode.substring(Constant.SB.length()), 4);
            }

            Map<String, Object> emptyMap = Maps.newHashMap();
            List<DictionaryDO> typeDOs = dictionaryService.listByType(Constant.DEVICE_TYPE);
            List<DictionaryDO> sourceDOs = dictionaryService.listByType(Constant.DEVICE_SOURCE);
            List<DictionaryDO> statusDOs = dictionaryService.listByType(Constant.DEVICE_STATUS);
            List<DictionaryDO> purposeDOs = dictionaryService.listByType(Constant.DEVICE_PURPOSE);
            List<UserDO> userDOs = userService.list(emptyMap);
            List<DeptDO> deptDOs = deptService.list(emptyMap);
            List<SupplierDO> supplierDOs = supplierService.list(emptyMap);
            List<DeviceDO> parentDeviceDOs = deviceService.list(emptyMap);


            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            List<DeviceDO> deviceDOs = Lists.newArrayList();
            DeviceDO deviceDO;
            String typeName;
            String sourceName;
            String usingStatusName;
            String deviceUseName;
            String factoryName;
            String supplierName;
            String serviceName;
            String parentDeviceName;
            String userName;
            String deptName;
            for (DeviceEntity deviceEntity : deviceEntityList) {
                deviceDO = new DeviceDO();
                BeanUtils.copyProperties(deviceEntity, deviceDO);
                // 设备类别
                typeName = deviceEntity.getTypeName();
                for (DictionaryDO deviceTypeDO : typeDOs) {
                    if (Objects.equals(deviceTypeDO.getName(),typeName)) {
                        deviceDO.setType(deviceTypeDO.getId());
                        break;
                    }
                }

                // 设备来源
                sourceName = deviceEntity.getSourceName();
                for (DictionaryDO sourceDO : sourceDOs) {
                    if (Objects.equals(sourceDO.getName(),sourceName)) {
                        deviceDO.setSouceId(sourceDO.getId().longValue());
                        break;
                    }
                }

                // 使用状况
                usingStatusName = deviceEntity.getUsingStatusName();
                for (DictionaryDO statusDO : statusDOs) {
                    if (Objects.equals(statusDO.getName(),usingStatusName)) {
                        deviceDO.setUsingStatus(statusDO.getId());
                        break;
                    }
                }

                // 设备用途
                deviceUseName = deviceEntity.getDeviceUseName();
                for (DictionaryDO purposeDO : purposeDOs) {
                    if (Objects.equals(purposeDO.getName(),deviceUseName)) {
                        deviceDO.setDeviceUse(purposeDO.getId());
                        break;
                    }
                }

                // 供应商 服务商 生产商
                factoryName = deviceEntity.getFactoryName();
                supplierName = deviceEntity.getSupplierName();
                serviceName = deviceEntity.getServiceName();
                for (SupplierDO supplierDO: supplierDOs) {
                    if (Objects.equals(supplierDO.getName(),factoryName)) {
                        deviceDO.setFactoryId(supplierDO.getId());
                    }
                    if (Objects.equals(supplierDO.getName(),supplierName)) {
                        deviceDO.setSupplierId(supplierDO.getId());
                    }
                    if (Objects.equals(supplierDO.getName(),serviceName)) {
                        deviceDO.setServiceId(supplierDO.getId());
                    }
                }

                // 主设备
                parentDeviceName = deviceEntity.getParentDeviceName();
                for (DeviceDO parentDeviceDO : parentDeviceDOs) {
                    if (Objects.equals(parentDeviceDO.getName(),parentDeviceName)) {
                        deviceDO.setParentId(parentDeviceDO.getId());
                        break;
                    }
                }

                // 负责人
                userName = deviceEntity.getUserName();
                for (UserDO userDO : userDOs) {
                    if (Objects.equals(userDO.getName(),userName)) {
                        deviceDO.setUserId(userDO.getUserId());
                        break;
                    }
                }

                // 使用部门
                deptName = deviceEntity.getDeptName();
                for (DeptDO deptDO : deptDOs) {
                    if (Objects.equals(deptDO.getName(),deptName)) {
                        deviceDO.setDeptId(deptDO.getDeptId());
                        break;
                    }
                }

                // 使用状态(1是0否)
                deviceDO.setDelFlag(0);
                deviceDO.setCreateBy(userId);
                deviceDO.setCreateTime(now);
                deviceDOs.add(deviceDO);
            }
            deviceService.batchSave(deviceDOs);
        }
        return R.ok();
    }
}
