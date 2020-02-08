package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.common.domain.Tree;
import com.ev.custom.domain.DictionaryTypeDO;
import com.ev.custom.service.DictionaryTypeService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/", tags = "字典类型管理")
public class DictionaryTypeApiController {
    @Autowired
    private DictionaryTypeService dictionaryTypeService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionaryType/add",method = RequestMethod.POST,apiTitle = "新增字典类型")
    @ApiOperation("新增字典类型")
    public R add(DictionaryTypeDO dictionaryTypeDO){
        // 0可修改1不可修改
    	dictionaryTypeDO.setIsUpdate(0);
        String typeName = dictionaryTypeDO.getName();
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",typeName);
        // 名称验重
        if (dictionaryTypeService.count(map)>0){
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
        }
        if(dictionaryTypeService.save(dictionaryTypeDO)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionaryType/edit",method = RequestMethod.POST,apiTitle = "修改字典类型")
    @ApiOperation("修改字典类型")
    public R edit(DictionaryTypeDO dictionaryTypeDO){
        if(dictionaryTypeService.update(dictionaryTypeDO)>0){
            return R.ok();
        }
        return R.ok(messageSourceHandler.getMessage("common.operating.success",null));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionaryType/delete",method = RequestMethod.POST,apiTitle = "删除字典类型")
    @ApiOperation("删除字典类型")
    public R delete(@ApiParam(value = "字典类型ID",required = true) @RequestParam(value = "dictionaryTypeId",defaultValue = "") Long dictionaryTypeId){
        if(dictionaryTypeService.remove(dictionaryTypeId)>0){
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/dictionaryType/list",method = RequestMethod.POST,apiTitle = "获取字典类型列表信息")
    @ApiOperation("获取字典类型列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "名称模糊搜索") @RequestParam(value = "fuzzySearch", defaultValue = "") String fuzzySearch,
                  @ApiParam(value = "是否可修改(1不可修改，0可修改)") @RequestParam(value = "isUpdate", defaultValue = "") Integer isUpdate){
        Map<String, Object> params = Maps.newHashMap();
        params.put("fuzzySearch", StringUtils.sqlLike(fuzzySearch));
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        params.put("isUpdate",isUpdate);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data = dictionaryTypeService.listForMap(params);
        int total = dictionaryTypeService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/dictionaryType/tree",method = RequestMethod.POST,apiTitle = "获取字典类型树")
    @ApiOperation("获取字典类型树")
    public R list(){
        Map<String,Object> results = Maps.newHashMap();
        Tree<DictionaryTypeDO> data = dictionaryTypeService.tree();
        results.put("data",data);
        return  R.ok(results);
    }
}
