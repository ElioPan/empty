package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.mes.domain.CraftDO;
import com.ev.mes.domain.ProcessDO;
import com.ev.mes.service.CraftService;
import com.ev.mes.service.ProcessCheckService;
import com.ev.mes.service.ProcessService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Kuzi on 2019-11-21.
 */
@Api(value="/", tags = "工序配置 + + 工艺路线")
@RestController
public class MesProcessAndCraftApiController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private CraftService craftService;
    @Autowired
    private ProcessCheckService processCheckService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @EvApiByToken(value = "/apis/mes/process/addAndChange", method = RequestMethod.POST, apiTitle = "添加/修改 工序配置")
    @ApiOperation("添加/修改 工序配置")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeProcess(ProcessDO processDO,
                                 @ApiParam(value = "检验项目明细:[{\"proId\":3,\"whetherCheck\":1(1是 0否),\"remark\":\"备注\"}]", required = true)  @RequestParam(value = "processCheck", defaultValue = "", required = false) String processCheck,
                                 @ApiParam(value = "上传附件") @RequestParam(value = "uploadAttachment",defaultValue = "",required = false) String  uploadAttachment) {
        //将审核状态变为待审核状态
        if(processDO.getAuditSign()==null){
            processDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
        }
        return processService.saveAndChange(processDO, processCheck, "",uploadAttachment);
    }

    @EvApiByToken(value = "/apis/mes/process/audit", method = RequestMethod.POST, apiTitle = "审核 工序配置")
    @ApiOperation("审核 工序配置")
    @Transactional(rollbackFor = Exception.class)
    public R auditProcess(@ApiParam(value = "工序配置id", required = true)  @RequestParam(value = "id", defaultValue = "", required = false) Long id) {

        ProcessDO processDO = processService.get(id);
        if(Objects.nonNull(processDO)){
            if(Objects.equals(ConstantForMES.WAIT_AUDIT,processDO.getAuditSign())){
                ProcessDO processDo1=new ProcessDO();
                processDo1.setAuditSign(ConstantForMES.OK_AUDITED);
                processDo1.setAuditId(ShiroUtils.getUserId());
                processDo1.setId(id);
                 processService.update(processDo1);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/mes/process/backToAudit", method = RequestMethod.POST, apiTitle = "反审核 工序配置")
    @ApiOperation("反审核 工序配置")
    @Transactional(rollbackFor = Exception.class)
    public R backToAuditProcess(@ApiParam(value = "工序配置id", required = true)  @RequestParam(value = "id", defaultValue = "", required = false) Long id) {

        ProcessDO processDO = processService.get(id);
        if(Objects.nonNull(processDO)){
            if(Objects.equals(ConstantForMES.OK_AUDITED,processDO.getAuditSign())){
                ProcessDO processDO1=new ProcessDO();
                processDO1.setAuditSign(ConstantForMES.WAIT_AUDIT);
                processDO1.setAuditId(0L);
                processDO1.setId(id);
                processService.update(processDO1);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }




    @EvApiByToken(value = "/apis/mes/process/processOfList", method = RequestMethod.POST, apiTitle = "工序配置列表")
    @ApiOperation("工序配置列表")
    @Transactional(rollbackFor = Exception.class)
    public R checkOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "状态") @RequestParam(value = "useStatus", required = false) Integer useStatus,
                         @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
                         @ApiParam(value = "工序名称") @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
//        params.put("useStatus", useStatus);
        params.put("auditSign", auditSign);

        List<Map<String, Object>> list = processService.listForMap(params);
        int count = processService.countListForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if (!list.isEmpty()) {
            for (Map<String, Object> listOfOne : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("foreignId", listOfOne.get("id"));
                map.put("type", ConstantForMES.PROCESS_GXPZ);
                List<Map<String, Object>> detailOfProcess = processCheckService.getDetailByProcessId(map);
                listOfOne.put("proDetail", detailOfProcess);
            }

            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages((count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }

        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/process/detail", method = RequestMethod.POST, apiTitle = "工序配置详情")
    @ApiOperation("工序配置详情")
    public R detail(@ApiParam(value = "工序配置Id", required = true) @RequestParam(value = "processId", defaultValue = "") Long processId) {

        return processService.getProcessDetail(processId);
    }


    @EvApiByToken(value = "/apis/mes/process/batchDelete", method = RequestMethod.POST, apiTitle = "删除工序配置")
    @ApiOperation("删除工序配置")
    @Transactional(rollbackFor = Exception.class)
    public R batchDelete(@ApiParam(value = "工序配置Id", required = true) @RequestParam(value = "ids") Long[] ids) {
        //若被工艺引用则不能被删除
        //逻辑删除
        return processService.deteBatchProcess(ids);
    }



/**
*===========================================工艺路线=============================================================================
 */


    @EvApiByToken(value = "/apis/mes/craft/addAndChange", method = RequestMethod.POST, apiTitle = "添加/修改 工艺路线")
    @ApiOperation("添加/修改 工艺路线")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeCraft(CraftDO craftDO,
                               @ApiParam(value = "[\n" +
                                       "{\n" +
                                       "\"id\":\"工艺明细行主键(修改时传且必传)\"," +
                                       "\"processId\":2," +
                                       "\"serialNumber\":序号," +
                                       "\"demand\":\"工艺要求\",\"deptId\":\"生产部门\",\"operator\":\"操作工\",\"autoDispatch\":\"是否自动派工\",\n" +
                                       "\"standard\":\"基准良率\",\"type\":\"工序类型\",\"whetherExamine\":\"是否检验\",\"whetherOutsource\":\"是否委外\",\"whetherCollect\":\"是否联网采集\",\n" +
                                       "\"deviceId\":\"使用设备id\",\"totalHour\":\"工作时长\",\"manHour\":\"单件工时\",\"labourPrice\":\"单件工价\",\n" +

                                       "\"pro\":[\n" +
                                       "{\n" +
                                       "\"id\":\"检验项目表主键(修改时传且必传)\",\n" +
                                       "\"foreignId\":\"工艺明细行主键(修改时传且必传)\",\n" +
                                       "\"proId\":\"检验项目主键\",\n" +
                                       "\"remark\":\"备注\",\n" +
                                       "\"whetherCheck\":\"是否必检1是0否\"\n" +
                                       "},\n" +
                                       "{\n" +
                                       "\"id\":\"检验项目表主键(修改时传且必传)\",\n" +
                                       "\"foreignId\":\"工艺明细行主键(修改时传且必传)\",\n" +
                                       "\"proId\":\"检验项目主键\",\n" +
                                       "\"remark\":\"备注\",\n" +
                                       "\"whetherCheck\":\"是否必检1是0否\"\n" +
                                       "}\n" +
                                       "]\n" +
                                       "}\n" +
                                       "]", required = true)  @RequestParam(value = "processAndProject", defaultValue = "", required = false) String processAndProject,
                               @ApiParam(value = "删除的工艺子表id") @RequestParam(value = "deletCraftItemIds",required = false) Long[] deletCraftItemIds,
                               @ApiParam(value = "删除的检查项目表id") @RequestParam(value = "deletProcessCheckIds",required = false) Long[] deletProcessCheckIds) {

        return craftService.saveAndChangeCraft(craftDO, processAndProject,deletCraftItemIds,deletProcessCheckIds);
    }

    @EvApiByToken(value = "/apis/mes/craft/auditCraft", method = RequestMethod.POST, apiTitle = "审核——工艺路线")
    @ApiOperation("审核——工艺路线")
    @Transactional(rollbackFor = Exception.class)
    public R auditOfCraft( @ApiParam(value = "工艺路线id", required = true) @RequestParam(value = "craftId") Long craftId,
                           @ApiParam(value = "审核人id") @RequestParam(value = "auditManIds",required = false) Long auditManIds){

        return craftService.auditAndUnOfCraft(craftId, auditManIds, 0);
    }

    @EvApiByToken(value = "/apis/mes/craft/reverseAuditCraft", method = RequestMethod.POST, apiTitle = "反审核——工艺路线")
    @ApiOperation("反审核——工艺路线")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAuditOfCraft( @ApiParam(value = "工艺路线id", required = true) @RequestParam(value = "craftId") Long craftId){
        Long auditManIds=0L;
        return craftService.auditAndUnOfCraft(craftId, auditManIds, 1);
    }

    @EvApiByToken(value = "/apis/mes/craft/listOfHeadCraft", method = RequestMethod.POST, apiTitle = "工艺路线列表")
    @ApiOperation("工艺路线列表--主表")
    @Transactional(rollbackFor = Exception.class)
    public R craftOfHeadList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                             @ApiParam(value = "状态") @RequestParam(value = "useStatus", required = false) Integer useStatus,
                             @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
                             @ApiParam(value = "工艺名称") @RequestParam(value = "name", defaultValue = "", required = false) String name) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("useStatus", useStatus);
        params.put("auditSign", auditSign);
        List<Map<String, Object>> list = craftService.listForMap(params);
        int count = craftService.countListForMap(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);

        if (list.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages( (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/mes/craft/listOfBadyCraft", method = RequestMethod.POST, apiTitle = "工艺路线列表")
    @ApiOperation("工艺路线列表--子表")
    @Transactional(rollbackFor = Exception.class)
    public R craftOfBodyList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "工艺路线主键", required = true) @RequestParam(value = "craftId") Long craftId) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("id", craftId);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        List<Map<String, Object>> list = craftService.listBodyForMap(params);
        int count = craftService.countListBodyForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if (list.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPagesize(pagesize);
            dsRet.setPageno(pageno);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages( (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/craft/craftOfDetail", method = RequestMethod.POST, apiTitle = "工艺路线详情")
    @ApiOperation("工艺路线详情")
    public R detailOfCraft( @ApiParam(value = "工艺路线id", required = true) @RequestParam(value = "craftId") Long craftId) {

        return craftService.getCraftOfDetail(craftId);
    }

    @EvApiByToken(value = "/apis/mes/craft/prcessDetalsOfCraft", method = RequestMethod.POST, apiTitle = "检验项目详情")
    @ApiOperation("检验项目详情")
    public R prcessDetalOfCraft( @ApiParam(value = "工艺明细行主键", required = true) @RequestParam(value = "itemId") Long itemId) {

        return craftService.getProcessOfDetail(itemId);
    }

    @EvApiByToken(value = "/apis/mes/craft/batchDeleteCraft", method = RequestMethod.POST, apiTitle = "删除工艺路线")
    @ApiOperation("删除工艺路线")
    @Transactional(rollbackFor = Exception.class)
    public R batchDeleteOfCraft( @ApiParam(value = "工艺路线id", required = true) @RequestParam(value = "craftId") Long[] craftId) {
        //  校验  ：工艺路线审核+工序计划是否引用
        return craftService.deletOfBatch(craftId);
    }


    /**
     *
     * @param file 工艺路线EXCEL 文件
     * Created by gumingjie on 2020-03-12.
     */
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/craft", method = RequestMethod.POST, apiTitle = "工艺路线导入")
    @ApiOperation("工艺路线导入")
    @Transactional(rollbackFor = Exception.class)
    public R readCraftFile(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
        return craftService.importExcel(file);
    }




}
