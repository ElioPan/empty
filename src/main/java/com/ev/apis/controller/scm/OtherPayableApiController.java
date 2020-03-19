package com.ev.apis.controller.scm;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.scm.domain.OtherReceivablesDO;
import com.ev.scm.service.OtherReceivablesService;
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
 * @Date 2020-3-18 16:38
 **/
@Api(value="/",tags = "其他应付")
@RestController
public class OtherPayableApiController {

    @Autowired
    private OtherReceivablesService otherReceivablesService;

    @EvApiByToken(value = "/apis/scm/otherPayable/addAndChange", method = RequestMethod.POST, apiTitle = "增加/修改--其他应付")
    @ApiOperation("增加/修改--其他应付")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChange(
            OtherReceivablesDO otherReceivablesDO,
            @ApiParam(value = "明细行：[{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"accrualDate\":\"应付日期\",\n" +
                    "\"receivablePayablesAmount\":\"应付金额\",\n" +
                    "\"paidReceivedAmount\":\"已付金额\",\n" +
                    "\"noReceiptPaymentAmount\":\"未付金额\",\n" +
                    "\"remarks\":\"备注\",\n" +
                    "}]") @RequestParam(value = "paymentBodys", defaultValue = "", required = false) String bodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "itemIds", required = false) Long[] itemIds) {

        return otherReceivablesService.disposeAddAndChange(otherReceivablesDO, bodys, itemIds, ConstantForGYL.OTHER_PAYABLE);
    }


    @EvApiByToken(value = "/apis/scm/otherPayable/audit", method = RequestMethod.POST, apiTitle = "审核--其他应付")
    @ApiOperation("审核--其他应付")
    @Transactional(rollbackFor = Exception.class)
    public R audit(
            @ApiParam(value = "主键id") @RequestParam(value = "id", required = false) Long id) {
        return otherReceivablesService.disposeAudit(id);
    }


    @EvApiByToken(value = "/apis/scm/otherPayable/reverseAudit", method = RequestMethod.POST, apiTitle = "审核--其他应付")
    @ApiOperation("反审核--其他应付")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(
            @ApiParam(value = "主键id") @RequestParam(value = "id", required = false) Long id) {
        return otherReceivablesService.disposeReverseAudit(id);
    }

    @EvApiByToken(value = "/apis/scm/otherPayable/batchDelete", method = RequestMethod.POST, apiTitle = "删除--其他应付")
    @ApiOperation("删除--其他应付")
    @Transactional(rollbackFor = Exception.class)
    public R delete(
            @ApiParam(value = "主键id") @RequestParam(value = "ids", required = false) Long[] ids) {
        return otherReceivablesService.dispoRemove(ids);
    }

    @EvApiByToken(value = "/apis/scm/otherPayable/detail", method = RequestMethod.POST, apiTitle = "详情--其他应付")
    @ApiOperation("详情--其他应付")
    public R detail(
            @ApiParam(value = "主键id") @RequestParam(value = "id", required = false) Long id) {
        return otherReceivablesService.getDetail(id);
    }

    @EvApiByToken(value = "/apis/scm/otherPayable/list", method = RequestMethod.POST, apiTitle = "列表--其他应付")
    @ApiOperation("列表--其他应付")
    public R list(
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商名字") @RequestParam(value = "supplierName",defaultValue = "") String supplierName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "") String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "") String endTime,
            @ApiParam(value = "审核") @RequestParam(value = "auditSign",defaultValue = "") Long auditSign,
            @ApiParam(value = "单据号") @RequestParam(value = "code",defaultValue = "") String code ) {

        Map<String,Object> map= new HashMap<>();
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        map.put("supplierName",supplierName);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("auditSign",auditSign);
        map.put("code",code);
        map.put("sign",ConstantForGYL.OTHER_PAYABLE);

        List<Map<String, Object>> list = otherReceivablesService.listForMap(map);
        Map<String, Object> countForMap = otherReceivablesService.countForMap(map);

        Map<String, Object> result = Maps.newHashMap();
        if (list.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,Integer.parseInt(countForMap.get("count").toString()),list));
            result.put("totalAmount", countForMap.get("totailAmount"));
        }
        return R.ok(result);

    }





}
