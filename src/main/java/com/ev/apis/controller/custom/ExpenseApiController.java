package com.ev.apis.controller.custom;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.ExpenseDO;
import com.ev.custom.service.ExpenseService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
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
 * @Author Kuzi
 * @Date 2020-1-13 13:02
 * @Version 01
 **/
@Api(value="/",tags = "费用")
@RestController
public class ExpenseApiController {
    @Autowired
    private   ExpenseService expenseService;


    @EvApiByToken(value = "/apis/expense/addAndChange",method = RequestMethod.POST,apiTitle = "保存/修改 —费用")
    @ApiOperation("保存/修改 —费用")
    @Transactional(rollbackFor = Exception.class)
    public R addPurchaseProduct(ExpenseDO ExpenseDO){

        return  expenseService.add(ExpenseDO);
    }

    @EvApiByToken(value = "/apis/expense/audit",method = RequestMethod.POST,apiTitle = "审核—费用")
    @ApiOperation("审核—费用")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "费用id:", required = false) @RequestParam(value = "id",required = false) Long id){
        return expenseService.audit(id);
    }

    @EvApiByToken(value = "/apis/expense/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核—费用")
    @ApiOperation("反审核—费用")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "费用id:", required = false) @RequestParam(value = "id",required = false) Long id){
        return expenseService.rollBackAudit(id);
    }


    @EvApiByToken(value = "/apis/expense/delet",method = RequestMethod.POST,apiTitle = "删除—费用")
    @ApiOperation("删除—费用")
    @Transactional(rollbackFor = Exception.class)
    public R removePurchase(@ApiParam(value = "费用id:", required = false) @RequestParam(value = "ids",required = false) Long[] ids){
        return expenseService.removePurchase(ids);
    }

    @EvApiByToken(value = "/apis/expense/detailOfExpense",method = RequestMethod.POST,apiTitle = "详情—费用")
    @ApiOperation("详情—费用")
    public R detailOfExpense(@ApiParam(value = "费用id:", required = false) @RequestParam(value = "id",required = false) Long id){
        return expenseService.getDetail(id);
    }


    @EvApiByToken(value = "/apis/expense/listOfExpense", method = RequestMethod.POST, apiTitle = "列表—费用")
    @ApiOperation("列表—费用")
    public R purchaseOflist(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1", required = true) int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20", required = true) int pagesize,
                            @ApiParam(value = "状态", required = false) @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
                            @ApiParam(value = "名称", required = false) @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("name", name);
        params.put("auditSign", auditSign);

        List<Map<String, Object>> list = expenseService.listForMap(params);
        int count=expenseService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        if (!list.isEmpty()) {
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



}
