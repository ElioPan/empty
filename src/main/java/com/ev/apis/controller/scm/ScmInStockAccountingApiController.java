package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.service.InStockAccountingService;
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
 * @Date 2020-2-17 12:26
 **/

@Api(value="/",tags="入库核算")
@RestController
public class ScmInStockAccountingApiController {

    @Autowired
    private InStockAccountingService inStockAccountingService;


    /*
      生产入库核算首先调用生产入库列表接口，接下来保存。
     */

    @EvApiByToken(value = "/apis/scm/inStockAccounting/produceOrOtherInStock", method = RequestMethod.POST, apiTitle = "保存--产品入库核算/其他入库核算")
    @ApiOperation("保存--产品入库核算/其他入库核算")
    @Transactional(rollbackFor = Exception.class)
    public R saveAccountingInStock(
                               @ApiParam(value = "列表明细：[\n" +
                                       "{\n" +
                                       "\"id\":\"子表主键(列表中stockInItemId字段)（必传）\",\n" +
                                       "\"unitPrice\":\"单价(默认传0)\",\n" +
                                       "\"amount\":\"金额(默认传0)\"\n" +
                                       "}\n" +
                                       "]") @RequestParam(value = "detailAccounting") String detailAccounting) {
        return inStockAccountingService.saveAccounting(detailAccounting);
    }


    /*
     * 分配后改变采购入库单的费用字段值，前端刷新列表显示费用有值（初始化为0）
     */
    @EvApiByToken(value = "/apis/scm/inStockAccounting/purchaseInStockAccounting", method = RequestMethod.POST, apiTitle = "分配--采购入库核算")
    @ApiOperation("分配--采购入库核算")
    @Transactional(rollbackFor = Exception.class)
    public R allocation(
            @ApiParam(value = "采购费用分配标准") @RequestParam(value = "distributionType") Long distributionType,
            @ApiParam(value = "入库单主表主键（列表行中的id）（必传）") @RequestParam(value = "detailAccounting") Long[] detailAccounting) {
        return inStockAccountingService.allocationAmount(distributionType,detailAccounting);
    }












}
