package com.ev.apis.controller.custom;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.PaymentTypeDO;
import com.ev.custom.service.PaymentTypeService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Kuzi
 * @Date 2020-3-18 08:36
 **/
@RestController
@Api(value = "/" ,tags = "收支类型")
public class PaymentTypeApiController {
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private PaymentTypeService paymentTypeService;

    @EvApiByToken(value = "/apis/paymentType/addAndChage",method = RequestMethod.POST,apiTitle = "收支类型")
    @ApiOperation("收支类型")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChage(PaymentTypeDO paymentTypeDO){
        return paymentTypeService.disposeAddAndChage(paymentTypeDO);
    }


    @EvApiByToken(value = "/apis/paymentType/detail",method = RequestMethod.POST,apiTitle = "详情")
    @ApiOperation("详情")
    @Transactional(rollbackFor = Exception.class)
    public R detail(@ApiParam(value = "主键", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){
        PaymentTypeDO paymentTypeDO = paymentTypeService.get(id);
        Map<String,Object> map= new HashMap<>();
        map.put("data",paymentTypeDO);
        return R.ok(map);
    }

    @EvApiByToken(value = "/apis/paymentType/audit",method = RequestMethod.POST,apiTitle = "审核")
    @ApiOperation("审核")
    @Transactional(rollbackFor = Exception.class)
    public R audit(@ApiParam(value = "主键", required = true) @RequestParam(value = "ids", defaultValue = "", required = true) Long[] ids){

        for(Long id :ids){
            PaymentTypeDO paymentTypeDO= paymentTypeService.get(id);
            paymentTypeDO.setAuditor(ShiroUtils.getUserId());
            paymentTypeDO.setAuditSign(Constant.OK_AUDITED);
            paymentTypeDO.setAuditTime(new Date());
            paymentTypeService.updateAll(paymentTypeDO);
        }
        return R.ok();
    }

    @EvApiByToken(value = "/apis/paymentType/disAudit",method = RequestMethod.POST,apiTitle = "反审核")
    @ApiOperation("反审核")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "主键", required = true) @RequestParam(value = "ids", defaultValue = "", required = true) Long[] ids){

        for(Long id :ids){
            PaymentTypeDO paymentTypeDO= paymentTypeService.get(id);
            paymentTypeDO.setAuditor(null);
            paymentTypeDO.setAuditSign(Constant.WAIT_AUDIT);
            paymentTypeDO.setAuditTime(null);
            paymentTypeService.updateAll(paymentTypeDO);
        }
        return R.ok();
    }

    @EvApiByToken(value = "/apis/paymentType/delet",method = RequestMethod.POST,apiTitle = "删除")
    @ApiOperation("删除")
    @Transactional(rollbackFor = Exception.class)
    public R delet(@ApiParam(value = "主键", required = true) @RequestParam(value = "ids", defaultValue = "", required = true) Long[] ids){

        for(Long id :ids){
            PaymentTypeDO paymentTypeDO= paymentTypeService.get(id);
            if(paymentTypeDO!=null){
                if(paymentTypeDO.getAuditSign().equals(Constant.OK_AUDITED)){
                    return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk", null));
                }
            }
        }
        for(Long id :ids){
            PaymentTypeDO paymentTypeDO= paymentTypeService.get(id);
            paymentTypeDO.setDelFlag(1);
            paymentTypeService.update(paymentTypeDO);
        }
        return R.ok();
    }

    @EvApiByToken(value = "/apis/paymentType/list",method = RequestMethod.POST,apiTitle = "列表")
    @ApiOperation("列表")
    @Transactional(rollbackFor = Exception.class)
    public R list(
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
            @ApiParam(value = "收支类型代码") @RequestParam(value = "code", defaultValue = "", required = true)String code,
            @ApiParam(value = "收支类型名字") @RequestParam(value = "name", defaultValue = "", required = true)String name){
        Map<String,Object>  map= new HashMap<>();
        map.put("offset", (pageno - 1) * pagesize);
        map.put("limit", pagesize);
        map.put("code",code);
        map.put("name",name);
        List<Map<String, Object>> list = paymentTypeService.listOfMap(map);
        int count = paymentTypeService.count(map);

        Map<String,Object> results = Maps.newHashMap();
        if(list.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages((count  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);

    }



}
