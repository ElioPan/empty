package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.domain.BankTransferDO;
import com.ev.scm.service.BankTransferItemService;
import com.ev.scm.service.BankTransferService;
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
 * @Date 2020-2-7 17:19
 **/
@Api(value = "/",tags = "银行转账单")
@RestController
public class ScmBankTransferApiController {
    @Autowired
    private BankTransferService bankTransferService;

    @Autowired
    private BankTransferItemService bankTransferItemService;


    @EvApiByToken(value = "/apis/scm/bankTransfer/addAndChange", method = RequestMethod.POST, apiTitle = "增加银行转账单")
    @ApiOperation("增加银行转账单")
    @Transactional(rollbackFor=Exception.class)
    public R addAndChangeTransfer(
            BankTransferDO bankTransferDO,
            @ApiParam(value = "盘点产品明细行：[\n" +
                    "{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"transferOutAcc\":\"转出账号\",\n" +
                    "\"transferInAcc\":\"转入账号\",\n" +
                    "\"transferAmount\":\"转账金额\",\n" +
                    "\"settlementType\":\"结算方式\",\n" +
                    "\"statementNumber\":\"结算号\",\n" +
                    "\"remarks\":\"备注\"\n" +
                    "}\n" +
                    "]") @RequestParam(value = "transferBodys", defaultValue = "", required = false) String transferBodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", defaultValue = "", required = false) Long[] deleItemIds) {
        //jfdkgkk
        return R.ok();
    }








}
