package com.ev.apis.controller.scm;

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

/**
 * @Author Kuzi
 * @Date 2020-3-16 10:09
 **/
@Api(value = "/",tags="资金初始数据")
@RestController
public class FundInitializationApiController {


    @Autowired
    private FundInitializationService fundInitializationService;


    @EvApiByToken(value = "scm/apis/fundInitialization/addAndChage", method = RequestMethod.POST, apiTitle = "增加/修改初始数据")
    @ApiOperation("增加/修改初始数据")
    @Transactional(rollbackFor = Exception.class)
    public R add(
            @ApiParam(value = "启用0禁用1:") @RequestParam(value = "body", defaultValue = "") Integer usingStart,
                 @ApiParam(value = "明细:[{\n" +
                         "\"id\":\"修改时必传\",\n" +
                         "\"period\":\"期间\",\n" +
                         "\"bank\":\"开户银行\",\n" +
                         "\"accountNumber\":\"银行账号\",\n" +
                         "\"initialAmount\":\"期初金额\"\n" +
                         "}]", required = true) @RequestParam(value = "body", defaultValue = "") String body){
        return fundInitializationService.disposeAddAndChage(usingStart,body);
    }







}
