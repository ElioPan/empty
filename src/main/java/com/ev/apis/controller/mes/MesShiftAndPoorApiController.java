package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.mes.domain.PoorDO;
import com.ev.mes.domain.ShiftDO;
import com.ev.mes.service.PoorService;
import com.ev.mes.service.ShiftService;
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
 * Created by Kuzi on 2019-11-26.
 * @author
 */

@Api(value="/", tags = "班制管理 + + 不良原因")
@RestController
public class MesShiftAndPoorApiController {

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private PoorService poorService;



    @EvApiByToken(value = "/apis/mes/shift/addAndChange", method = RequestMethod.POST, apiTitle = "添加/修改 班制配置")
    @ApiOperation("添加/修改 班制配置")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeShift(ShiftDO  shiftDO) {
        return shiftService.saveAddChangeShift(shiftDO);
    }


    @EvApiByToken(value = "/apis/mes/shift/detail", method = RequestMethod.POST, apiTitle = "班制配置详情")
    @ApiOperation("班制配置详情")
    @Transactional(rollbackFor = Exception.class)
    public R shiftDetail(@ApiParam(value = "班制配置Id", required = true) @RequestParam(value = "shiftId", defaultValue = "") Long shiftId) {

        Map<String,Object> query=new HashMap<>();
        ShiftDO shiftDO = shiftService.get(shiftId);
        query.put("data",shiftDO);
        return R.ok(query);
    }

    @EvApiByToken(value = "/apis/mes/shift/shiftOfList", method = RequestMethod.POST, apiTitle = "班制配置列表")
    @ApiOperation("班制配置列表")
    @Transactional(rollbackFor = Exception.class)
    public R shiftOfLists(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "班制配置名称") @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        List<Map<String, Object>> list = shiftService.listForMap(params);
        int count = shiftService.count(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if ( !list.isEmpty()) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages( (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/mes/shift/batchDelete", method = RequestMethod.POST, apiTitle = "删除班制配置")
    @ApiOperation("删除班制配置")
    @Transactional(rollbackFor = Exception.class)
    public R batchDelete(@ApiParam(value = "班制配置Id", required = true) @RequestParam(value = "ids") Long[] ids) {

        //若被引用则不能被删除(暂不知被谁引用)
        //TODO   逻辑删除
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        results.put("id",ids);
        shiftService.deletOfShift(results);
        return R.ok();
    }




    /**
     * =====================================================不良原因=============================================================================
     */


    @EvApiByToken(value = "/apis/mes/poor/addPoor", method = RequestMethod.POST, apiTitle = "增加不良原因")
    @ApiOperation("增加不良原因")
    @Transactional(rollbackFor = Exception.class)
    public R addPoor(PoorDO poorDO) {

        return poorService.savePoor(poorDO);
    }



    @EvApiByToken(value = "/apis/mes/poor/poorOfList", method = RequestMethod.POST, apiTitle = "不良原因列表")
    @ApiOperation("不良原因列表")
    public R poorOfLists(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                          @ApiParam(value = "不良原因名称") @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        List<Map<String, Object>> list = poorService.listForMap(params);
        int count = poorService.count(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if ( !(list.isEmpty())) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages((int) (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/poor/batchRemove", method = RequestMethod.POST, apiTitle = "删除不良原因")
    @ApiOperation("删除不良原因")
    @Transactional(rollbackFor = Exception.class)
    public R removePoors(@ApiParam(value = "不良原因id", required = true) @RequestParam(value = "id") Long[] id ) {
        //逻辑删除
        poorService.batchPoorDetail(id);
        return R.ok();
    }












}
