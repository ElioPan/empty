package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.domain.FrdbDO;
import com.ev.custom.service.FrdbService;
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

@RestController
@Api(value = "/", tags = "故障库管理接口")
public class FrdbApiController {
    @Autowired
    private FrdbService frdbService;

    @EvApiByToken(value = "/apisfrdblist",method = RequestMethod.POST,apiTitle = "获取故障库列表信息")
    @ApiOperation("获取故障库列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                  @ApiParam(value = "故障类型") @RequestParam(value = "frType",defaultValue = "",required = false)  Long frType,
                  @ApiParam(value = "上传人") @RequestParam(value = "upUserName",defaultValue = "",required = false)  String upUserName){
        Map<String, Object> params = new HashMap<>();
        params.put("frType",frType);
        params.put("upUser",upUserName);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.frdbService.listForMap(params);
        int total = this.frdbService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apisfrdbdetail",method = RequestMethod.POST,apiTitle = "获取故障库详细信息")
    @ApiOperation("获取故障库详细信息")
    public R detail(@ApiParam(value = "故障库ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<>();
        results = frdbService.detail(id);
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apisfrdbsave",method = RequestMethod.POST,apiTitle = "保存故障库信息")
    @ApiOperation("保存故障库信息")
    public R save(@ApiParam(value = "故障库信息",required = true) FrdbDO frdb,
                  @ApiParam(value = "上传图片",required = false) @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
        try {
            frdbService.add(frdb,taglocationappearanceImage);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apisfrdbupdate",method = RequestMethod.POST,apiTitle = "编辑故障库信息")
    @ApiOperation("编辑故障库信息")
    public R update(@ApiParam(value = "故障库信息",required = true) FrdbDO frdb,
                    @ApiParam(value = "添加图片服务器路径",required = false) @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage,
                    @ApiParam(value = "删除图片服务器路径",required = false) @RequestParam(value = "deletetag_appearanceImage",defaultValue = "",required = false) String[] deletetagAppearanceImage){
        try {
            frdbService.edit(frdb,taglocationappearanceImage,deletetagAppearanceImage);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apisfrdbremove",method = RequestMethod.POST,apiTitle = "删除故障库")
    @ApiOperation("删除故障库")
    public R remove(@ApiParam(value = "故障库主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
        if(frdbService.remove(id)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apisfrdbbatchRemove",method = RequestMethod.POST,apiTitle = "批量删除故障库")
    @ApiOperation("批量删除故障库")
    public R remove(@ApiParam(value = "故障库主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        frdbService.batchRemove(ids);
        return R.ok();
    }
}
