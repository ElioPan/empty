package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.service.MaterialsCollectService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kuzi on 2019-12-9.
 * @author
 */
@Api(value = "/",tags = "用料采集")
@RestController
public class MesMaterialsCollecApiController {

    @Autowired
    private MaterialsCollectService materialsCollectService;



    @EvApiByToken(value = "/apis/mes/materialCollect/addAndChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 用料采集")
    @ApiOperation("暂存/修改暂存 用料采集")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndChange(@ApiParam(value = "采集明细[{\"id\":\"采集单id(修改时传且必传)\",\"materiaId\":\"物料id\",\"batch\":\"批号\",\"materiaCount\":\"数量\"}]]", required = true) @RequestParam(value = "colletDetails") String colletDetails,
                              @ApiParam(value = "删除的采集明细主键") @RequestParam(value = "collectIds",defaultValue = "", required = false) Long[] colletIds,
                              @ApiParam(value = "工单主键id", required = true) @RequestParam(value = "dispatchItemId ") Long dispatchItemId) {

        return materialsCollectService.saveAndSubmit(colletDetails, colletIds, dispatchItemId, 0);
    }

    @EvApiByToken(value = "/apis/mes/materialCollect/submitCollect", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 用料采集")
    @ApiOperation("提交 用料采集(1.新增后直接提交；2.查看明细后修改/未修改直接提交)")
    @Transactional(rollbackFor = Exception.class)
    public R submitCollet(@ApiParam(value = "采集明细[{\"id\":\"采集单id(修改时传且必传)\",\"materiaId\":\"物料id\",\"batch\":\"批号\",\"materiaCount\":\"数量\"}]", required = true) @RequestParam(value = "colletDetails") String colletDetails,
                              @ApiParam(value = "删除的 采集明细主键") @RequestParam(value = "collectIds ",defaultValue = "", required = false) Long[] colletIds,
                              @ApiParam(value = "工单主键id", required = true) @RequestParam(value = "dispatchItemId") Long dispatchItemId) {

        return materialsCollectService.saveAndSubmit(colletDetails,colletIds,dispatchItemId,1);
    }


    @EvApiByToken(value = "/apis/mes/materialCollect/collectOfDetail", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 用料采集")
    @ApiOperation("采集明细详情")
    public R detailOfCollct(@ApiParam(value = "工单主键id", required = true) @RequestParam(value = "colletDetails") String dispatchItemId){
        Map<String,Object> result=new HashMap<>();
        result.put("dispatchItemId",dispatchItemId);

        List<Map<String, Object>> list = materialsCollectService.getListssDetail(result);
        result.clear();
        if(!(list.isEmpty())){
            result.put("data",list);
        }
        return  R.ok(result);
    }


    @EvApiByToken(value = "/apis/mes/materialCollect/listOfCollect", method = RequestMethod.POST, apiTitle = "采集列表")
    @ApiOperation("采集列表")
    public R collectOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                               @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                               @ApiParam(value = "操作工id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                               @ApiParam(value = "设备id") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
                               @ApiParam(value = "工序id") @RequestParam(value = "processId", defaultValue = "", required = false) Long  processId,
                               @ApiParam(value = "采集物料id") @RequestParam(value = "collectMateriaId", defaultValue = "", required = false) Long  collectMateriaId,
                               @ApiParam(value = "产品(物料)id") @RequestParam(value = "materialId", defaultValue = "", required = false) Long  materialId,
                               @ApiParam(value = "供应商id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long  supplierId,

                               @ApiParam(value = "操作工名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                               @ApiParam(value = "设备名字") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                               @ApiParam(value = "工序名字") @RequestParam(value = "processName", defaultValue = "", required = false) String processName,
                               @ApiParam(value = "采集物料名字") @RequestParam(value = "collectMateriaName", defaultValue = "", required = false) String  collectMateriaName,
                               @ApiParam(value = "产品名") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                               @ApiParam(value = "供应商名字") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,

                               @ApiParam(value = "开始日期") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                               @ApiParam(value = "截止日期") @RequestParam(value = "endTimes", defaultValue = "", required = false) String endTime,
                               @ApiParam(value = "采集时间") @RequestParam(value = "endTimes", defaultValue = "", required = false) String createTime,
                               @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                               @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {

        Map<String, Object> params = Maps.newHashMap();

        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps =  materialsCollectService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("processId", processId);
        params.put("materialId", materialId);
        params.put("collectMateriaId", collectMateriaId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("operatorName", operatorName);
        params.put("processName", processName);
        params.put("deviceName", deviceName);
        params.put("materielName", materielName);
        params.put("supplierName", supplierName);
        params.put("collectMateriaName", collectMateriaName);

        List<Map<String, Object>> list = materialsCollectService.listForMap(params);
        int count = materialsCollectService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);

        if (!(list.isEmpty())) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages((int) (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/materialCollect/batchRemove", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 用料采集")
    @ApiOperation("删除采集详情")
    public R detailOfCollct(@ApiParam(value = "采集详情主键", required = true) @RequestParam(value = "ids",defaultValue="") Long[] ids){

        return  materialsCollectService.removeCollect(ids);
    }





}
