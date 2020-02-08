package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONArray;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.QualityReportDO;
import com.ev.custom.service.*;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.DeptService;
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

/**
 * @author gumingjie
 * @date 2019/9/04
 */
@Api(value = "/",tags = "8D报告管理API")
@RestController
public class QualityReportApiController {

	@Autowired
	private QualityReportService qualityReportService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/qualityReport/list",method = RequestMethod.POST,apiTitle = "获取8D报告列表信息")
    @ApiOperation("获取8D报告列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "报告名称") @RequestParam(value = "reportName",defaultValue = "",required = false)  String reportName,
                  @ApiParam(value = "报告人") @RequestParam(value = "userName",defaultValue = "",required = false)  String userName,
                  @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                  @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                  @ApiParam(value = "报告状态（待验收：57；已验收：58）") @RequestParam(value = "checkStatus",defaultValue = "",required = false)  String checkStatus,
                  @ApiParam(value = "类型（我发起的事件1；我办理的事件2；）") @RequestParam(value = "checkType",defaultValue = "",required = false) Integer checkType,
                  @ApiParam(value = "用户部门ID") @RequestParam(value = "deptId",defaultValue = "",required = false) Long deptId,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId",defaultValue = "",required = false)Long userId){
        
    	Map<String, Object> params = Maps.newHashMapWithExpectedSize(10);
        params.put("reportName",reportName);
        params.put("userName",userName);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("checkStatus",checkStatus);
        params.put("checkType",checkType);
    	if (deptId!=null&&deptService.get(deptId)!=null) {
    		String idPath = deptService.get(deptId).getIdPath();
    		params.put("deptId",idPath);
		}
        params.put("userId",userId);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.qualityReportService.listForMap(params);
        int total = this.qualityReportService.countForMap(params);
        return getR(pageno, pagesize, results, data, total);
    }

    @EvApiByToken(value = "/apis/qualityReport/detail",method = RequestMethod.POST,apiTitle = "获取8D报告表详细信息")
    @ApiOperation("获取8D报告表详细信息")
    public R detail(@ApiParam(value = "8D报告表ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        return  R.ok(qualityReportService.detail(id));
    }
    
    @EvApiByToken(value = "/apis/qualityReport/getDetailPlan",method = RequestMethod.POST,apiTitle = "获取8D报告表阶段计划详情")
    @ApiOperation("获取8D报告表阶段计划类型")
    public R getDetailPlan(@ApiParam(value = "8D报告表ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id,
    					   @ApiParam(value = "8D报告表阶段类型（临时改善对策：126；永久改善计划：127；预防再发生：128; 措施验证：132）",required = true) @RequestParam(value = "planTypeId",defaultValue = "",required = false)  Integer planTypeId) {
        return  R.ok(qualityReportService.getDetailPlan(id, planTypeId));
    }
 
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/save",method = RequestMethod.POST,apiTitle = "保存8D报告表信息")
    @ApiOperation("保存8D报告表信息")
    public R save(@ApiParam(value = "8D报告表信息",required = true) QualityReportDO qualityReport,
    			  @ApiParam(value = "增加改善小组[{\"userId\":25,\"deptId\":23,\"responsibleContent\":\"负责事项XXX\"},{\"userId\":26,\"deptId\":24,\"responsibleContent\":\"负责事项XXX\"}]",required = true) @RequestParam(value = "groupList",defaultValue = "") String groupList) {
    	Map<String, Object> add = qualityReportService.add(qualityReport,groupList);
    	if (add.size()>0) {
        	return R.ok(add);
		}
       return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/update",method = RequestMethod.POST,apiTitle = "编辑8D报告表信息")
    @ApiOperation("编辑8D报告表信息")
    public R update(@ApiParam(value = "8D报告表信息",required = true) QualityReportDO qualityReport,
    				@ApiParam(value = "增加修改改善小组[{\"userId\":25,\"deptId\":23,\"responsibleContent\":\"负责事项XXX\"},{\"userId\":26,\"deptId\":24,\"responsibleContent\":\"负责事项XXX\"}]") @RequestParam(value = "groupList",defaultValue = "",required = false) String groupList,
    				@ApiParam(value = "删除改善小组") @RequestParam(value = "groupIds",defaultValue = "",required = false) Long[]groupIds){
            int edit = qualityReportService.edit(qualityReport,groupList,groupIds);
            if (edit>0) {
            	return R.ok();
			}
            return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/analyzeReason",method = RequestMethod.POST,apiTitle = "8D报告分析主要原因")
    @ApiOperation("分析8D报告主要原因")
    public R editReason(
    					@ApiParam(value = "8D报告ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id,
    					@ApiParam(value = "分析主要原因",required = true) @RequestParam(value = "resultContent",defaultValue = "")  String analyzeReason,
    					@ApiParam(value = "上传附件") @RequestParam(value = "uploadAttachment",defaultValue = "",required = false) String uploadAttachment){
        	int editReason = qualityReportService.editReason(id,analyzeReason);
        	if (editReason>0) {
        		if (StringUtils.isNoneBlank(uploadAttachment)) {
        			contentAssocService.saveList(id,JSONArray.parseArray(uploadAttachment),Constant.BD_MAINREASON_FILE);
				}
        		return R.ok();
			}
        	return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/checkSave",method = RequestMethod.POST,apiTitle = "8D报告效果确认验收")
    @ApiOperation("验收8D报告单")
    public R checkSave(	@ApiParam(value = "8D报告ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id,
//    		@ApiParam(value = "验收人ID",required = true) @RequestParam(value = "userId",defaultValue = "",required = true)  Long userId,
    					@ApiParam(value = "效果确认验收",required = true) @RequestParam(value = "resultContent",defaultValue = "")  String resultContent,
    					@ApiParam(value = "上传附件") @RequestParam(value = "uploadAttachment",defaultValue = "",required = false) String  uploadAttachment){
    		if (!qualityReportService.checkBDTask(id)) {
                return R.error(messageSourceHandler.getMessage("BD.task.unfinished",null));
			}
    		int checkSave = qualityReportService.checkSave(id,ShiroUtils.getUserId(),resultContent);
        	if (checkSave>0) {
        		if (StringUtils.isNoneBlank(uploadAttachment)) {
        			contentAssocService.saveList(id,JSONArray.parseArray(uploadAttachment),Constant.BD_CHECKRESULT_FILE);
        		}
        		return R.ok();
			}
            return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/remove",method = RequestMethod.POST,apiTitle = "删除8D报告表")
    @ApiOperation("删除8D报告表")
    public R remove(@ApiParam(value = "8D报告表主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
        if (qualityReportService.isDelete(id)) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
        }
    	if(qualityReportService.remove(id)>0){
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/qualityReport/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除8D报告表")
    @ApiOperation("批量删除8D报告表")
    public R remove(@ApiParam(value = "8D报告表主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        for (Long id : ids) {
        	 if (qualityReportService.isDelete(id)) {
                 return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
     		}
		}
    	int batchRemove = qualityReportService.batchRemove(ids);
        if (ids.length==batchRemove) {
        	return R.ok();
		}
        return R.error();
    }

    private R getR( int pageno, int pagesize, Map<String, Object> results, List<Map<String, Object>> data, int total) {
        if( data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }
}
