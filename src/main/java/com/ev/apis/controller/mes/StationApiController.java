package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.StationDO;
import com.ev.mes.service.StationService;
import com.google.common.collect.Maps;
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
 * 工位管理
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-11 09:51:41
 */

@Api(value = "/", tags = "工位管理API")
@RestController
public class StationApiController {
    @Autowired
    private StationService stationService;

    @EvApiByToken(value = "/apis/station/list",method = RequestMethod.POST,apiTitle = "获取工位列表信息")
    @ApiOperation("获取工位列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "工位名称") @RequestParam(value = "nameSearch",defaultValue = "",required = false)  String nameSearch,
                  @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",defaultValue = "",required = false)  String auditSign){
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("nameSearch", StringUtils.sqlLike(nameSearch));
        params.put("auditSign", auditSign);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.stationService.listForMap(params);
        int total = this.stationService.count(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/station/detail",method = RequestMethod.POST,apiTitle = "获取工位详细信息")
    @ApiOperation("获取工位详细信息")
    public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        Map<String,Object> results = new HashMap<>();
        Map<String,Object> param = Maps.newHashMap();
        param.put("id",id);
        List<Map<String, Object>> list = stationService.listForMap(param);
        results.put("data",list.size()>0?list.get(0):null);
        return  R.ok(results);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/station/addOrUpdate",method = RequestMethod.POST,apiTitle = "保存/修改工位信息")
    @ApiOperation("保存/修改工位信息")
    public R addOrUpdate(@ApiParam(value = "工位信息",required = true) StationDO stationDO){
        return stationService.addOrUpdate(stationDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/station/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除工位信息")
    @ApiOperation("批量删除工位信息")
    public R remove(@ApiParam(value = "工位主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        return stationService.batchRemoveByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/station/audit", method = RequestMethod.POST, apiTitle = "审核工位")
    @ApiOperation("审核工位")
    public R audit(
            @ApiParam(value = "工位主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return stationService.audit(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/station/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核工位")
    @ApiOperation("反审核工位")
    public R reverseAudit(
            @ApiParam(value = "工位主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return stationService.reverseAudit(id);
    }
}
