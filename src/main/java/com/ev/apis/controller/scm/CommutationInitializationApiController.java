package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.service.CommutationInitializationService;
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
 * @Author Kuzi
 * @Date 2020-3-18 11:01
 **/
@Api(value="/",tags="往来初始化++客户、供应商往来对账")
@RestController
public class CommutationInitializationApiController {

    @Autowired
    private CommutationInitializationService commutationInitializationService;


    @EvApiByToken(value = "scm/apis/commutationInitialization/addAndChage", method = RequestMethod.POST, apiTitle = "增加/修改初始数据")
    @ApiOperation("增加/修改初始数据")
    @Transactional(rollbackFor = Exception.class)
    public R add(
            @ApiParam(value = "明细:[{\n" +
                    "\"id\":\"修改时必传\",\n" +
                    "\"period\":\"期间\",\n" +
                    "\"clientId\":\"客户\",\n" +
                    "\"supplierId\":\"供应商\",\n" +
                    "\"initialAmount\":\"期初金额\"\n" +
                    "}]", required = true) @RequestParam(value = "body", defaultValue = "") String body){
        return commutationInitializationService.disposeAddAndChage(body);
    }

    @EvApiByToken(value = "scm/apis/commutationInitialization/list", method = RequestMethod.POST, apiTitle = "列表")
    @ApiOperation("列表")
    @Transactional(rollbackFor = Exception.class)
    public R getList( ){

          Map<String,Object> map= new HashMap<>();
          map.put("clientId",1);
        List<Map<String, Object>> listForMapClient = commutationInitializationService.getListForMap(map);
        int sumAmoutClient=commutationInitializationService.countForMap(map);
        map.remove("clientId");
        map.put("supplierId",1);
        List<Map<String, Object>> listForMapSupplier = commutationInitializationService.getListForMap(map);
        int sumAmoutSupplier=commutationInitializationService.countForMap(map);
        map.clear();
        map.put("Client",listForMapClient);
        map.put("sumAmoutClient",sumAmoutClient);
        map.put("Supplier",listForMapSupplier);
        map.put("sumAmoutSupplier",sumAmoutSupplier);
        return R.ok(map);
    }

    @EvApiByToken(value = "/apis/commutationInitialization/customerReconciliation", method = RequestMethod.POST, apiTitle = "客户往来对账")
    @ApiOperation("客户往来对账")
    public R customerReconciliation(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "客户ID",required = true ) @RequestParam(value = "clientId") Long clientId,
            @ApiParam(value = "开始时间",required = true) @RequestParam(value = "startTime") String startTime,
            @ApiParam(value = "结束时间",required = true) @RequestParam(value = "endTime") String endTime) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("pageno", pageno);
        params.put("pagesize", pagesize);
        params.put("endTime", endTime);
        params.put("startTime", startTime);
        params.put("clientId", clientId);

        return commutationInitializationService.getClientAccountMessage(params);
    }















}
