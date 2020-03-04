package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.scm.domain.PaymentReceivedDO;
import com.ev.scm.service.PaymentReceivedService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Kuzi
 * @Date 2020-2-12 13:20
 **/
@Api(value = "/", tags="付款单")
@RestController
public class ScmPaymentOrderApiController {

    @Autowired
    private PaymentReceivedService paymentReceivedService;

    @EvApiByToken(value = "/apis/scm/paymentOrder/addAndChange", method = RequestMethod.POST, apiTitle = "增加/修改--付款单")
    @ApiOperation("增加/修改--付款单")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeReceived(
            PaymentReceivedDO paymentReceivedDO,
            @ApiParam(value = "明细行：[\n" +
                    "{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"sourceType\":\"源单类型\",\n" +
                    "\"sourceCode\":\"源单编号\",\n" +
                    "\"sourceId\":\"源单主键\",\n" +
                    "\"sourcePayItemId\":\"源单付款条件明细主键\",\n" +
                    "\"thisAmount\":\"本次付款金额\",\n" +
                    "\"accountNumber\":\"付款账号\",\n" +
                    "\"settlementType\":\"结算方式\",\n" +
                    "\"statementNumber\":\"结算号\",\n" +
                    "\"remarks\":\"备注\"\n" +
                    "}\n" +
                    "]") @RequestParam(value = "paymentBodys", defaultValue = "", required = false) String paymentBodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", required = false) Long[] deleItemIds) {

        return paymentReceivedService.addReceived(paymentReceivedDO, paymentBodys, deleItemIds, ConstantForGYL.PAYMENT_ORDER);
    }

    @EvApiByToken(value = "/apis/scm/paymentOrder/detailOfReceived", method = RequestMethod.POST, apiTitle = "详情—付款单")
    @ApiOperation("详情—付款单")
    public R detail(@ApiParam(value = "付款单id:", required = true) @RequestParam(value = "id") Long id) {
        return paymentReceivedService.getdetail(id,ConstantForGYL.PAYMENT_ORDER);
    }

    @EvApiByToken(value = "/apis/scm/paymentOrder/audit", method = RequestMethod.POST, apiTitle = "审核—付款单")
    @ApiOperation("审核—付款单")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "付款单id:", required = true) @RequestParam(value = "id") Long id) {
        return paymentReceivedService.audit(id,ConstantForGYL.PAYMENT_ORDER);
    }

    @EvApiByToken(value = "/apis/scm/paymentOrder/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核—付款单")
    @ApiOperation("反审核—付款单")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "付款单id:", required = true) @RequestParam(value = "id") Long id) {
        return paymentReceivedService.rollBackAudit(id,ConstantForGYL.PAYMENT_ORDER);
    }

    @EvApiByToken(value = "/apis/scm/paymentOrder/delet", method = RequestMethod.POST, apiTitle = "删除—付款单")
    @ApiOperation("删除—付款单")
    @Transactional(rollbackFor = Exception.class)
    public R removeReceived(@ApiParam(value = "付款单ids:", required = true) @RequestParam(value = "ids") Long[] ids) {
        return paymentReceivedService.removeReceived(ids);
    }

    @EvApiByToken(value = "/apis/scm/paymentOrder/listOfReceived", method = RequestMethod.POST, apiTitle = "列表—付款单")
    @ApiOperation("列表—付款单")
    public R purchaseOflist(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "单据编号") @RequestParam(value = "prCode", defaultValue = "", required = false) String prCode,
                            @ApiParam(value = "供应商名字(模糊)") @RequestParam(value = "cusSupName", defaultValue = "", required = false) String cusSupName,
                            @ApiParam(value = "开始日期(单据日期)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(单据日期)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                            @ApiParam(value = "付款账号id") @RequestParam(value = "accountNumber", defaultValue = "", required = false) Long accountNumber,
                            @ApiParam(value = "结算方式id") @RequestParam(value = "settlementType", defaultValue = "", required = false) Long settlementType,
                            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("sign", ConstantForGYL.PAYMENT_ORDER);
        params.put("prCode", prCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("cusSupName", cusSupName);
        params.put("accountNumber", accountNumber);
        params.put("settlementType", settlementType);
        params.put("auditSign", auditSign);

        List<Map<String, Object>> list = paymentReceivedService.listForMap(params);

        Map<String, Object> totalMap = paymentReceivedService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        if (!list.isEmpty()) {
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",list);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",Integer.parseInt(totalMap.get("count").toString()));
            dsRet.put("totalPages",((Integer.parseInt(totalMap.get("count").toString()) + pagesize - 1) / pagesize));
            dsRet.put("totalThisAmount",totalMap.get("totalThisAmount"));
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/paymentOrderGetOut", method = RequestMethod.GET, apiTitle = "导出付款单")
    @ApiOperation("导出付款单")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "prCode", defaultValue = "", required = false) String prCode,
            @ApiParam(value = "客户名字(模糊)") @RequestParam(value = "cusSupName", defaultValue = "", required = false) String cusSupName,
            @ApiParam(value = "开始日期(单据日期)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "截止日期(单据日期)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "付款账号id") @RequestParam(value = "accountNumber", defaultValue = "", required = false) Long accountNumber,
            @ApiParam(value = "结算方式id") @RequestParam(value = "settlementType", defaultValue = "", required = false) Long settlementType,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("sign", ConstantForGYL.PAYMENT_ORDER);
        params.put("prCode", prCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("cusSupName", cusSupName);
        params.put("accountNumber", accountNumber);
        params.put("settlementType", settlementType);
        params.put("auditSign", auditSign);

        List<Map<String, Object>> list = paymentReceivedService.listForMap(params);
        ClassPathResource classPathResource = new ClassPathResource("poi/scm_payment_received_out.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "付款单");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }









}
