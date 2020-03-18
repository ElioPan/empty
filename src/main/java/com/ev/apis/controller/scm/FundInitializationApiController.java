package com.ev.apis.controller.scm;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.service.FundInitializationService;
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
 * @Date 2020-3-16 10:09
 **/
@Api(value = "/",tags="资金初始数据+资金余额+资金明细")
@RestController
public class FundInitializationApiController {


    @Autowired
    private FundInitializationService fundInitializationService;


    @EvApiByToken(value = "scm/apis/fundInitialization/addAndChage", method = RequestMethod.POST, apiTitle = "增加/修改初始数据")
    @ApiOperation("增加/修改初始数据")
    @Transactional(rollbackFor = Exception.class)
    public R add(
                 @ApiParam(value = "明细:[{\n" +
                         "\"id\":\"修改时必传\",\n" +
                         "\"period\":\"期间\",\n" +
                         "\"bank\":\"开户银行\",\n" +
                         "\"accountNumber\":\"银行账号\",\n" +
                         "\"initialAmount\":\"期初金额\"\n" +
                         "}]", required = true) @RequestParam(value = "body", defaultValue = "") String body){
        return fundInitializationService.disposeAddAndChage(body);
    }

    @EvApiByToken(value = "scm/apis/fundInitialization/startUsing", method = RequestMethod.POST, apiTitle = "启用")
    @ApiOperation("启用")
    @Transactional(rollbackFor = Exception.class)
    public R startUsing(
            @ApiParam(value = "明细行主键:", required = true) @RequestParam(value = "ids") Long[] ids){
        return fundInitializationService.disposeStartUsing(ids);
    }

    @EvApiByToken(value = "scm/apis/fundInitialization/forbidden", method = RequestMethod.POST, apiTitle = "禁用")
    @ApiOperation("禁用")
    @Transactional(rollbackFor = Exception.class)
    public R  forbidden(
            @ApiParam(value = "明细行主键:", required = true) @RequestParam(value = "ids") Long[] ids){
        return fundInitializationService.disposeForbidden(ids);
    }

    @EvApiByToken(value = "scm/apis/fundInitialization/list", method = RequestMethod.POST, apiTitle = "列表")
    @ApiOperation("列表")
    @Transactional(rollbackFor = Exception.class)
    public R  list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                   @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                   @ApiParam(value = "启用0，禁用1") @RequestParam(value = "usingStart",defaultValue = "") Integer  usingStart){
        Map<String,Object> params= new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("usingStart", usingStart);
        List<Map<String, Object>> getlist = fundInitializationService.getlist(params);
        Map<String, Object> countOfList = fundInitializationService.countOfList(params);

        if ( getlist.size() > 0) {
            params.clear();
            int total= Integer.parseInt(countOfList.get("totailCount").toString());
            params.put("total",total);
            params.put("totailInitialAmount",countOfList.get("totailInitialAmount"));
            params.put("data", new DsResultResponse(pageno,pagesize,total,getlist));
        }
        return R.ok(params);

    }



    @EvApiByToken(value = "scm/apis/fundInitialization/fundBalance", method = RequestMethod.POST, apiTitle = "资金余额")
    @ApiOperation("资金余额")
    @Transactional(rollbackFor = Exception.class)
    public R  fundBalance(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                   @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                   @ApiParam(value = "启用0，禁用1") @RequestParam(value = "usingStart",defaultValue = "") Integer  usingStart,
                   @ApiParam(value = "截止时间") @RequestParam(value = "endTime",defaultValue = "") String  endTime,
                   @ApiParam(value = "组织") @RequestParam(value = "deptId",defaultValue = "") Long  deptId,
                   @ApiParam(value = "银行账号") @RequestParam(value = "accountNumber",defaultValue = "") String  accountNumber){
        Map<String,Object> params= new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("usingStart", usingStart);
        params.put("endTime", endTime);
        params.put("deptId", deptId);
        params.put("accountNumber", accountNumber);

        return fundInitializationService.disposeFundBalance(pageno,pagesize,params,endTime);

    }


    @EvApiByToken(value = "scm/apis/fundInitialization/financialDetails", method = RequestMethod.POST, apiTitle = "资金明细")
    @ApiOperation("资金明细")
    @Transactional(rollbackFor = Exception.class)
    public R  financialDetails(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                          @ApiParam(value = "启用0，禁用1") @RequestParam(value = "usingStart",defaultValue = "") Integer  usingStart,
                               @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "") String  startTime,
                               @ApiParam(value = "截止时间") @RequestParam(value = "endTime",defaultValue = "") String  endTime,
                          @ApiParam(value = "组织") @RequestParam(value = "deptId",defaultValue = "") Long  deptId,
                          @ApiParam(value = "银行账号(初始化表中的id)",required = true) @RequestParam(value = "id",defaultValue = "") Long  id){
        Map<String,Object> params= new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("usingStart", usingStart);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("deptId", deptId);

        return fundInitializationService.disposeFinancialDetails(pageno,pagesize,params,id);
    }













}
