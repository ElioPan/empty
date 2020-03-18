package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.scm.domain.OtherReceivablesDO;
import com.ev.scm.service.OtherReceivablesService;
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
 * @Date 2020-3-18 16:38
 **/
@Api(value="/",tags = "其他应付")
@RestController
public class OtherPayableApiController {

    @Autowired
    private OtherReceivablesService otherReceivablesService;

    @EvApiByToken(value = "/apis/scm/otherPayable/addAndChange", method = RequestMethod.POST, apiTitle = "增加/修改--其他应收")
    @ApiOperation("增加/修改--其他应收")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChange(
            OtherReceivablesDO otherReceivablesDO,
            @ApiParam(value = "明细行：[[{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"accrualDate\":\"应付日期\",\n" +
                    "\"receivablePayablesAmount\":\"应付金额\",\n" +
                    "\"paidReceivedAmount\":\"已付金额\",\n" +
                    "\"noReceiptPaymentAmount\":\"未付金额\",\n" +
                    "\"remarks\":\"备注\",\n" +
                    "}]") @RequestParam(value = "paymentBodys", defaultValue = "", required = false) String bodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", required = false) Long[] deleItemIds) {

        return otherReceivablesService.disposeAddAndChange(otherReceivablesDO, bodys, deleItemIds, ConstantForGYL.OTHER_PAYABLE);
    }




}
