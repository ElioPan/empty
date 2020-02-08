package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.mes.domain.CheckPlanDO;
import com.ev.mes.domain.CheckProjectDO;
import com.ev.mes.service.CheckPlanService;
import com.ev.mes.service.CheckProjectService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Kuzi on 2019-11-20.
 *  @author Kuzi
 */
@Api(value = "/", tags = "检验项目++检验方案")
@RestController
public class MesCheckAndPlanApiController {
    @Autowired
    private CheckProjectService checkProjectService;
    @Autowired
    private CheckPlanService checkPlanService;



    @EvApiByToken(value = "/apis/mes/checkProject/add", method = RequestMethod.POST, apiTitle = "添加/修改 檢驗项目")
    @ApiOperation("添加/修改 檢驗项目")
    @Transactional(rollbackFor = Exception.class)
    public R add(CheckProjectDO checkProjectDO) {

        checkProjectService.saveAndChangePro(checkProjectDO);

        return R.ok();
    }

    @EvApiByToken(value = "/apis/mes/checkProject/checkOfList", method = RequestMethod.POST, apiTitle = "檢驗项目列表")
    @ApiOperation("檢驗项目列表")
    @Transactional(rollbackFor = Exception.class)
    public R checkOfList(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "檢驗项目") @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        List<Map<String, Object>> list = checkProjectService.listForMap(params);
        int count = checkProjectService.countListForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if (!list.isEmpty()) {
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

    @EvApiByToken(value = "/apis/mes/checkProject/detail", method = RequestMethod.POST, apiTitle = "查看檢驗项目详情")
    @ApiOperation("查看檢驗项目详情")
    public R detail(@ApiParam(value = "檢驗项目Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        results.put("id", id);
        List<Map<String, Object>> list = checkProjectService.listForMap(results);
        results.remove("id");
        if ( !list.isEmpty()) {
            results.put("data", list);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/mes/checkProject/batchDelete", method = RequestMethod.POST, apiTitle = "查看檢驗项目详情")
    @ApiOperation("删除檢驗项目")
    @Transactional(rollbackFor = Exception.class)
    public R batchDelete(@ApiParam(value = "檢驗项目Id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {

        //删除前添加验证:是否被检验方案引用，++逻辑删除
        return checkProjectService.deletOfPro(ids);
    }


    /*
    ======================================================检验方案===================================================================
     */


    @EvApiByToken(value = "/apis/mes/checkPlan/addPlan", method = RequestMethod.POST, apiTitle = "新增/修改 檢驗方案")
    @ApiOperation("新增/修改 檢驗方案")
    @Transactional(rollbackFor = Exception.class)
    public R addCheckPlan(CheckPlanDO checkPlanDO,
                          @ApiParam(value = "檢驗项目(修改时id必传)[{ \"id\":2,\"projectId\":2,\"remark\":\"备注\"}]", required = true) @RequestParam(value = "projectDetail", defaultValue = "") String projectDetail,
                          @ApiParam(value = "删除的檢驗项目ids") @RequestParam(value = "ids", defaultValue = "",required = false) Long[] ids) {

        return checkPlanService.saveAndChangePlan(checkPlanDO, projectDetail, ids);
    }


    @EvApiByToken(value = "/apis/mes/checkPlan/auditPlan", method = RequestMethod.POST, apiTitle = "审核 檢驗方案")
    @ApiOperation("审核 檢驗方案")
    @Transactional(rollbackFor = Exception.class)
    public R auditCheckPlan(@ApiParam(value = "檢驗方案id", required = true) @RequestParam(value = "id", defaultValue = "") Long id,
                            @ApiParam(value = "审核人id") @RequestParam(value = "auditId", defaultValue = "",required = false) Long auditId) {

        return checkPlanService.auditPlanOfCheck(id, auditId);
    }


    @EvApiByToken(value = "/apis/mes/checkPlan/opposeAuditPlan", method = RequestMethod.POST, apiTitle = "反审核 檢驗方案")
    @ApiOperation("反审核 檢驗方案")
    @Transactional(rollbackFor = Exception.class)
    public R opposeAuditCheckPlan(@ApiParam(value = "檢驗方案id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {

        return checkPlanService.opposeAuditCheckPlan(id);
    }

    @EvApiByToken(value = "/apis/mes/checkPlan/list", method = RequestMethod.POST, apiTitle = "檢驗方案列表")
    @ApiOperation("檢驗方案列表")
    public R checkOfPlanList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "檢驗方案名字") @RequestParam(value = "name", defaultValue = "", required = false) String name) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        List<Map<String, Object>> list = checkPlanService.listForMap(params);
        int count = checkPlanService.countListForMap(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);

        if (!list.isEmpty()) {
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

    @EvApiByToken(value = "/apis/mes/checkPlan/detail", method = RequestMethod.POST, apiTitle = "查看檢驗方案详情")
    @ApiOperation("查看檢驗方案详情")
    public R detailOfPlan(@ApiParam(value = "檢驗方案id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {

        return checkPlanService.getPlanOfDetail(id);
    }


    @EvApiByToken(value = "/apis/mes/checkPlan/batchDelete", method = RequestMethod.POST, apiTitle = "删除檢驗方案")
    @ApiOperation("删除檢驗方案")
    @Transactional(rollbackFor = Exception.class)
    public R batchDeleteOfPlan(@ApiParam(value = "檢驗方案Id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        //后期删除前添加被引用验证(已添状态验证)
        //逻辑删除
        return checkPlanService.deletOfPlan(ids);
    }








}
