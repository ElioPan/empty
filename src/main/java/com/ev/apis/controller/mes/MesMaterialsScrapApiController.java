package com.ev.apis.controller.mes;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.service.MaterialsScrapItemService;
import com.ev.mes.service.MaterialsScrapService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kuzi on 2019-12-9.
 * @author
 */
@Api(value="/",tags = "用料报废")
@RestController
public class MesMaterialsScrapApiController {

    @Autowired
    private MaterialsScrapService materialsScrapService;
    @Autowired
    private MaterialsScrapItemService materialsScrapItemService;


    @EvApiByToken(value = "/apis/mes/materialsScrap/addAndChange", method = RequestMethod.POST, apiTitle = "保存/修改保存")
    @ApiOperation("保存/修改保存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndChange(@ApiParam(value = "报废单头部[{\"id\":\"主单主键(更新时传且必传)\",\"foreignId\":\"生产投料单主键\",\"deptId\":\"生产部门\",\"createBy\":\"制单人\"}]", required = true) @RequestParam(value = "scrapDetail", required = true) String  scrapDetail,
                              @ApiParam(value = "报废单明细[{\"id\":\"明细主键(更新时传且必传)\",\"materialId\":\"物料id\",\"batch\":\"批号\",\"scrapReason\":\"报废原因\",\"scrapCount\":\"报废数量\",\"remark\":\"备注\"}]", required = true) @RequestParam(value = "scrapItemDetail", required = true) String  scrapItemDetail,
                              @ApiParam(value = "删除的明细Id", required = false) @RequestParam(value = "itemIds", required = false) Long[]  itemIds) {

        //返回报废单主键供审核使用
        return materialsScrapService.saveChangeScrap(scrapDetail, scrapItemDetail,itemIds);
    }


    @EvApiByToken(value = "/apis/mes/materialsScrap/submitOfScrap", method = RequestMethod.POST, apiTitle = "保存/修改保存")
    @ApiOperation("审核")
    @Transactional(rollbackFor = Exception.class)
    public R submitScrap(@ApiParam(value = "用料报废单Id", required = true) @RequestParam(value = "scrapId", required = true) Long scrapId,
                              @ApiParam(value = "审核人", required = true) @RequestParam(value = "auditId", required = true) Long  auditId) {
        //先保存后审核
        return materialsScrapService.submit(scrapId,auditId);
    }

    @EvApiByToken(value = "/apis/mes/materialsScrap/detail", method = RequestMethod.POST, apiTitle = "用料报废单详情")
    @ApiOperation("用料报废单详情")
    public R detailOfScrap(@ApiParam(value = "用料报废单Id", required = true) @RequestParam(value = "scrapId", required = true) Long  scrapId) {

        return materialsScrapService.getDetailOfScrap(scrapId);
    }


    @EvApiByToken(value = "/apis/mes/materialsScrap/reversAudit", method = RequestMethod.POST, apiTitle = "反审核")
    @ApiOperation("反审核")
    @Transactional(rollbackFor = Exception.class)
    public R rollbackOfAudit(@ApiParam(value = "用料报废单Id", required = true) @RequestParam(value = "scrapId", required = true) Long scrapId) {

        return  materialsScrapService.reversAuditScrap(scrapId);
    }


    @EvApiByToken(value = "/apis/mes/materialsScrap/listOfScrap", method = RequestMethod.POST, apiTitle = "用料报废列表")
    @ApiOperation("用料报废列表")
    public R scrapOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1", required = true) int pageno,
                           @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20", required = true) int pagesize,
                           @ApiParam(value = "用料报废单号", required = false) @RequestParam(value = "scrapCode", defaultValue = "", required = false) String scrapCode,
                         @ApiParam(value = "物料id", required = false) @RequestParam(value = "materialId", defaultValue = "", required = false) Long  materialId,
                         @ApiParam(value = "物料名字", required = false) @RequestParam(value = "materialName", defaultValue = "", required = false) String  materialName,
                         @ApiParam(value = "物料代码", required = false) @RequestParam(value = "serialNo", defaultValue = "", required = false) String  serialNo,
                         @ApiParam(value = "制单开始日期", required = false) @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                         @ApiParam(value = "制单截止日期", required = false) @RequestParam(value = "endTimes", defaultValue = "", required = false) String endTime,
                         @ApiParam(value = "制单日期", required = false) @RequestParam(value = "endTimes", defaultValue = "", required = false) String createTime,
                         @ApiParam(value = "需排序字段（制单日期createTime）") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                         @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {

        Map<String, Object> params = Maps.newHashMap();

        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps =  materialsScrapItemService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("materialId", materialId);
        params.put("code", scrapCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materialName", materialName);
        params.put("serialNo", serialNo);


        List<Map<String, Object>> list = materialsScrapItemService.listForMap(params);
        int count = materialsScrapItemService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);

        if (!(list.isEmpty())) {
            BigDecimal totalScrapCount=list.stream().map(v-> MathUtils.getBigDecimal(v.get("scrapCount"))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",list);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",count);
            dsRet.put("totalPages",(count + pagesize - 1) / pagesize);
            dsRet.put("totalScrapCount", totalScrapCount);
            results.put("data", dsRet);
        }

        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/mes/materialsScrap/deletScraps", method = RequestMethod.POST, apiTitle = "删除用料报废")
    @ApiOperation("删除用料报废")
    @Transactional(rollbackFor = Exception.class)
    public R removeBatch(@ApiParam(value = "用料报废单Id", required = true) @RequestParam(value = "scrapIds", required = true) Long[]  scrapIds) {

        return materialsScrapService.deletScrap(scrapIds);
    }


}
