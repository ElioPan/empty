package com.ev.apis.controller.custom;

import com.ev.custom.domain.MaterielDO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.service.FacilityService;
import com.ev.framework.config.ConstantForMES;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "/", tags = "仓库管理")
public class FacilityApiController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/facility/list",method = RequestMethod.GET,apiTitle = "获取仓库列表信息")
    @ApiOperation("获取仓库列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "仓库名称") @RequestParam(value = "name",defaultValue = "",required = false)  String name,

                  @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",defaultValue = "",required = false)  String auditSign){
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("name", StringUtils.sqlLike(name));

        params.put("auditSign", auditSign);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.facilityService.listForMap(params);
        int total = this.facilityService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages(((total  +  pagesize  - 1) / pagesize));
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/facility/detail",method = RequestMethod.GET,apiTitle = "获取仓库详细信息")
    @ApiOperation("获取仓库详细信息")
    public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Integer id) {
        Map<String,Object> results = new HashMap<>();
        Map<String,Object> param = Maps.newHashMap();
        param.put("id",id);
        List<Map<String, Object>> list = facilityService.listForMap(param);
        results.put("data",list.size()>0?list.get(0):null);
        return  R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/save",method = RequestMethod.POST,apiTitle = "保存仓库信息")
    @ApiOperation("保存仓库信息")
    public R save(@ApiParam(value = "仓库信息",required = true) FacilityDO facilityDO){
		int save = facilityService.save(facilityDO);
		if (save==-1) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (save > 0) {
			return R.ok();
		}
		return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/update",method = RequestMethod.POST,apiTitle = "修改仓库信息")
    @ApiOperation("修改仓库信息")
    public R update(@ApiParam(value = "仓库信息",required = true) FacilityDO facilityDO){
    	int update = facilityService.update(facilityDO);
    	if (update == -1) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
    	if (update > 0) {
			return R.ok();
		}
		return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/remove",method = RequestMethod.POST,apiTitle = "删除仓库信息")
    @ApiOperation("删除仓库信息")
    public R remove(@ApiParam(value = "仓库主键",required = true) @RequestParam(value="id",defaultValue = "") Integer id){
        if(facilityService.logicRemove(id)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除仓库信息")
    @ApiOperation("批量删除仓库信息")
    public R remove(@ApiParam(value = "仓库主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Integer[] ids){
        facilityService.logicBatchRemove(ids);
        return R.ok();
    }

    /**
     * 审核仓库
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/audit", method = RequestMethod.POST, apiTitle = "审核仓库")
    @ApiOperation("审核仓库")
    public R audit(
            @ApiParam(value = "仓库主键", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        FacilityDO facilityDO = facilityService.get(id);
        if (Objects.equals(facilityDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
        }
        if (facilityService.audit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 反审核仓库
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facility/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核仓库")
    @ApiOperation("反审核仓库")
    public R reverseAudit(
            @ApiParam(value = "仓库主键", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        FacilityDO facilityDO = facilityService.get(id);
        if (Objects.equals(facilityDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit",null));
        }
        if (facilityService.reverseAudit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }
}
