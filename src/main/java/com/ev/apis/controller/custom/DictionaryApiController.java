package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.domain.DictionaryTypeDO;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.DictionaryTypeService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
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
import java.util.Objects;

/**
 * @author yunian
 * @date 2018/6/17
 */
@Api(value = "/",tags = "数据字典管理API")
@RestController
public class DictionaryApiController {
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private DictionaryTypeService dictionaryTypeService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    
    @EvApiByToken(value = "/apis/dict/getDictsByType",method = RequestMethod.POST,apiTitle = "根据类型获取数据字典下拉选项")
    @ApiOperation("根据类型获取数据字典下拉选项，不填时默认所有")
    public R getDictsByType(@ApiParam(value = "数据字典type值") @RequestParam(value = "type",defaultValue = "",required = false) String type){
        Map<String,Object> results = Maps.newHashMap();
        results = this.dictionaryService.getDictsByType(type);
        return R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionary/add",method = RequestMethod.POST,apiTitle = "新增字典")
    @ApiOperation("新增字典")
    public R add(DictionaryDO dictionaryDO){
    	DictionaryTypeDO dictionaryTypeDO = dictionaryTypeService.get(dictionaryDO.getTypeId());
    	if (dictionaryTypeDO!=null&&dictionaryTypeDO.getIsUpdate()==1) {
            return R.error(messageSourceHandler.getMessage("basicInfo.dictionary.insert",null));
		}
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",dictionaryDO.getName());
        map.put("typeId",dictionaryTypeDO.getId());
        // 名称验重
        if (dictionaryService.count(map)>0){
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
        }
        if(dictionaryService.save(dictionaryDO)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionary/edit",method = RequestMethod.POST,apiTitle = "修改字典")
    @ApiOperation("修改字典")
    public R edit(DictionaryDO dictionaryDO){
    	DictionaryTypeDO dictionaryTypeDO = dictionaryTypeService.get(dictionaryDO.getTypeId());
    	if (dictionaryTypeDO!=null&&dictionaryTypeDO.getIsUpdate()==1) {
            return R.error(messageSourceHandler.getMessage("basicInfo.dictionary.insert",null));
		}
        DictionaryDO dictionary = dictionaryService.get(dictionaryDO.getId());
        if (Objects.equals(dictionary.getName(),dictionaryDO.getName())) {
            return R.ok();
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",dictionaryDO.getName());
        map.put("typeId",dictionaryTypeDO.getId());
        // 名称验重
        if (dictionaryService.count(map)>0){
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
        }
        if(dictionaryService.update(dictionaryDO)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/dictionary/delete",method = RequestMethod.POST,apiTitle = "删除字典")
    @ApiOperation("删除字典")
    public R delete(@ApiParam(value = "字典ID",required = true) @RequestParam(value = "dictionaryId",defaultValue = "",required = true) Integer dictionaryId){
        if(dictionaryService.remove(dictionaryId)>0){
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/dictionary/list",method = RequestMethod.POST,apiTitle = "获取字典列表信息")
    @ApiOperation("获取字典列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                  @ApiParam(value = "内容",required = false) @RequestParam(value = "content",defaultValue = "",required = false) String content,
                  @ApiParam(value = "字典类型ID") @RequestParam(value = "typeId",required = false) Long typeId){
        Map<String, Object> params = new HashMap<>();
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        params.put("typeId",typeId);
        params.put("content",content);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data = dictionaryService.listForMap(params);
        int total = dictionaryService.countForMap(params);
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





}
