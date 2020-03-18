package com.ev.apis.controller.scm;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.service.CommutationInitializationService;
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
 * @Date 2020-3-18 11:01
 **/
@Api(value="/",tags="往来初始化+客户往来对账+供应商往来对账")
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









}
