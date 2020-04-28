package com.ev.apis.controller.custom;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
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

@RestController
@Api(value = "/", tags = "库位管理")
public class FacilityLocationApiController {
    @Autowired
    private FacilityLocationService facilityLocationService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/facilityLocation/list",method = RequestMethod.GET,apiTitle = "获取库位列表信息")
    @ApiOperation("获取库位列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "库位编号、库位名称") @RequestParam(value = "name",defaultValue = "",required = false)  String name,
                  @ApiParam(value = "仓库Id") @RequestParam(value = "facilityId",defaultValue = "",required = false) Long facilityId,

                  @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",defaultValue = "",required = false)  String auditSign
                  ){
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("name",name);
        params.put("facilityId",facilityId);

        params.put("auditSign", auditSign);

        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.facilityLocationService.listForMap(params);
        int total = this.facilityLocationService.count(params);
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

    @EvApiByToken(value = "/apis/facilityLocation/detail",method = RequestMethod.GET,apiTitle = "获取库位详细信息")
    @ApiOperation("获取库位详细信息")
    public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        Map<String,Object> results = new HashMap<>();
        Map<String,Object> param = Maps.newHashMap();
        param.put("id",id);
        List<Map<String, Object>> facilityLocationList = facilityLocationService.listForMap(param);

        results.put("data",facilityLocationList.size()>0?facilityLocationList.get(0):"");
        return  R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/save",method = RequestMethod.POST,apiTitle = "保存库位信息")
    @ApiOperation("保存库位信息")
    public R save(@ApiParam(value = "库位信息",required = true) FacilityLocationDO facilityLocationDO){
		int save = facilityLocationService.save(facilityLocationDO);
		if (save==-1) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (save > 0) {
			return R.ok();
		}
		return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/update",method = RequestMethod.POST,apiTitle = "修改库位信息")
    @ApiOperation("修改库位信息")
    public R update(@ApiParam(value = "库位信息",required = true) FacilityLocationDO facilityLocationDO){
		int update = facilityLocationService.update(facilityLocationDO);
		if (update==-1) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (update > 0) {
			return R.ok();
		}
		return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/remove",method = RequestMethod.POST,apiTitle = "删除库位信息")
    @ApiOperation("删除库位信息")
    public R remove(@ApiParam(value = "库位主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
        FacilityLocationDO facilityLocationDO = facilityLocationService.get(id);
        if (facilityLocationDO != null) {
            if(Objects.equals(facilityLocationDO.getAuditSign(),ConstantForMES.OK_AUDITED)){
                return R.error(messageSourceHandler.getMessage("common.approved.delete.disabled", null));
            }
            if(facilityLocationService.logicRemove(id)>0){
                return R.ok();
            }
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除库位信息")
    @ApiOperation("批量删除库位信息")
    public R remove(@ApiParam(value = "库位主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        for (Long id : ids) {
            FacilityLocationDO facilityLocationDO = facilityLocationService.get(id);
            if(facilityLocationDO == null){
                return R.error();
            }
            if (Objects.equals(facilityLocationDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
                return R.error(messageSourceHandler.getMessage("common.approved.delete.disabled", null));
            }
        }
        facilityLocationService.logicBatchRemove(ids);
        return R.ok();
    }

    /**
     * 审核库位
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/audit", method = RequestMethod.POST, apiTitle = "审核库位")
    @ApiOperation("审核库位")
    public R audit(
            @ApiParam(value = "库位主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        FacilityLocationDO facilityLocationDO = facilityLocationService.get(id);
        if (Objects.equals(facilityLocationDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
        }
        if (facilityLocationService.audit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 反审核库位
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/facilityLocation/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核库位")
    @ApiOperation("反审核库位")
    public R reverseAudit(
            @ApiParam(value = "库位主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        FacilityLocationDO facilityLocationDO = facilityLocationService.get(id);
        if (Objects.equals(facilityLocationDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit",null));
        }
        if (facilityLocationService.reverseAudit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }
}
