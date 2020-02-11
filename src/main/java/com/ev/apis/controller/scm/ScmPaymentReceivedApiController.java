package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.scm.domain.PaymentReceivedDO;
import com.ev.scm.service.PaymentReceivedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kuzi
 * @Date 2020-2-11 13:19
 **/
@Api(value = "/", tags="收款单")
@RestController
public class ScmPaymentReceivedApiController {
    @Autowired
    private PaymentReceivedService paymentReceivedService;


    @EvApiByToken(value = "/apis/scm/paymentReceived/addAndChange", method = RequestMethod.POST, apiTitle = "增加/修改--收款单")
    @ApiOperation("增加/修改--收款单")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeReceived(
            PaymentReceivedDO paymentReceivedDO,
            @ApiParam(value = "明细行：[\n" +
                    "{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"sourceType\":\"源单类型\",\n" +
                    "\"sourceCode\":\"源单编号\",\n" +
                    "\"sourceId\":\"源单主键\",\n" +
                    "\"sourcePayItemId\":\"源单明细主键\",\n" +
                    "\"accrualDate\":\"应收日期\",\n" +
                    "\"receivablePayablesAmount\":\"应收金额\",\n" +
                    "\"paidReceivedAmount\":\"已收金额\",\n" +
                    "\"thisAmount\":\"本次收款金额\",\n" +
                    "\"noReceiptPaymentAmount\":\"未收金额\t\",\n" +
                    "\"accountNumber\":\"收款账号\",\n" +
                    "\"settlementType\":\"结算方式\",\n" +
                    "\"statementNumber\":\"结算号\",\n" +
                    "\"remarks\":\"备注\"\n" +
                    "}\n" +
                    "]") @RequestParam(value = "paymentBodys", defaultValue = "", required = false) String paymentBodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", required = false) Long[] deleItemIds) {

        return paymentReceivedService.addReceived(paymentReceivedDO, paymentBodys, deleItemIds, ConstantForGYL.ALL_BILL);
    }

    @EvApiByToken(value = "/apis/scm/paymentReceived/audit", method = RequestMethod.POST, apiTitle = "审核—收款单")
    @ApiOperation("审核—收款单")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "收款单id:", required = true) @RequestParam(value = "id") Long id) {
        return paymentReceivedService.audit(id,ConstantForGYL.ALL_BILL);
    }

    @EvApiByToken(value = "/apis/scm/paymentReceived/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核—收款单")
    @ApiOperation("反审核—收款单")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "收款单id:", required = true) @RequestParam(value = "id") Long id) {
        return paymentReceivedService.rollBackAudit(id,ConstantForGYL.ALL_BILL);
    }

    @EvApiByToken(value = "/apis/scm/paymentReceived/delet", method = RequestMethod.POST, apiTitle = "删除—收款单")
    @ApiOperation("删除—收款单")
    @Transactional(rollbackFor = Exception.class)
    public R removeReceived(@ApiParam(value = "收款单ids:", required = true) @RequestParam(value = "ids") Long[] ids) {
        return paymentReceivedService.removeReceived(ids);
    }












}
