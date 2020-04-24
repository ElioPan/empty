package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.LeaveApplyDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.LeaveApplyService;
import com.ev.custom.service.UserAssocService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "/", tags = "请假管理")
public class LeaveApplyApiController {
    @Autowired
    private LeaveApplyService leaveApplyService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserAssocService userAssocService;

    @Autowired
    private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/leaveApply/saveAddAndChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存")
    @ApiOperation("暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndChange(@ApiParam(value = "请假信息", required = true) LeaveApplyDO leaveApply,
                              @ApiParam(value = "审核人") @RequestParam(value = "approveList", defaultValue = "" ) Long[] approveList,
                              @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {
        if (Objects.isNull(leaveApply.getId())) {
            //新增保存

                leaveApply.setStatus(Constant.TS);//146：暂存  状态
                leaveApplyService.tempSave(leaveApply, approveList, null, taglocationappearanceImage, null);
                return R.ok();
        } else {
            //更新
                LeaveApplyDO leveDoOne = leaveApplyService.get(leaveApply.getId());
                if (leveDoOne != null) {
                    if (Objects.equals(Constant.TS, leveDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, leveDoOne.getStatus())) {  //146,62允许修改
                        leaveApplyService.saveChangeOfLeaveDetail(leaveApply,approveList,taglocationappearanceImage, 0);
                        return R.ok();
                    }
                    //"数据状态为审核中/已结束不允许修改！
                    return R.error(messageSourceHandler.getMessage("common.massge.notAllow",null));
                }
                //"要修改的请假信息不存在，请检查id是否正确"
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));

        }
    }

    @EvApiByToken(value = "/apis/leaveApply/sbmitAndChange", method = RequestMethod.POST, apiTitle = "提交")
    @ApiOperation("提交请假申请（1.从暂存态和退回态修改/未修改后直接提交；2:新增填写明细后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R saveSbmitAndChange(@ApiParam(value = "请假信息", required = true) LeaveApplyDO leaveApply,
                              @ApiParam(value = "审核人") @RequestParam(value = "approveList", defaultValue = "" ) Long[] approveList,
                              @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {
        if (Objects.isNull(leaveApply.getId())) {
            //新增保存
                leaveApplyService.submit(leaveApply, approveList, null, taglocationappearanceImage, null);
                return R.ok();

        } else {
            //更新
                LeaveApplyDO leveDoOne = leaveApplyService.get(leaveApply.getId());
                if (leveDoOne != null) {
                    if (Objects.equals(Constant.TS, leveDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, leveDoOne.getStatus())) {  //146,62允许修改
                        leaveApplyService.saveChangeOfLeaveDetail(leaveApply,approveList,taglocationappearanceImage, 1);
                        return R.ok();
                    }
                    //单据状态为审核中/已结束不允许再次提交！
                    return R.error(messageSourceHandler.getMessage("common.daily.submitNotAllow",null));
                }
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }


    @EvApiByToken(value = "/apis/leaveApply/list", method = RequestMethod.GET, apiTitle = "获取请假列表信息")
    @ApiOperation("获取列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
                  @ApiParam(value = "填报日期（开始）") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "填报日期（结束）") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "请假类型") @RequestParam(value = "type", defaultValue = "", required = false) Long type,
//                  @ApiParam(value = "审批状态") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
                  @ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {

        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;
        Long lowderUserId = ShiroUtils.getUserId();

        Map<String, Object> reponse = new HashMap<>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createByName", userName);
        params.put("beginTime", startTime);
        params.put("endTime", endTime);
        params.put("type", type);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
//            put("sort", "status");
//            put("order", "ASC");

        if(!"".equals(userId)&&Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
        }

        if(!"".equals(userId)&&!Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
            params.put("status", Constant.TS);//暂存：146
            params.put("statusOther", Constant.APPLY_REJECT);//退回：62
        }

        if(!"".equals(approveUserId)){
            params.put("assignId", approveUserId);
            params.put("status", Constant.TS);//暂存：146
            params.put("statusOther", Constant.APPLY_REJECT);//退回：62
        }

        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.TS);//暂存：146
            params.put("statusOther", Constant.APPLY_REJECT);//退回：62
        }

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.leaveApplyService.listForMap(params);
        Map<String, Object> stringObjectMap = this.leaveApplyService.countForMap(params);//allTimearea

        if (data.size() > 0) {
            //总时长
            reponse.put("allTimearea",stringObjectMap.get("allTimearea"));
            reponse.put("datas",data);
            reponse.put("pageno",pageno);
            reponse.put("totalRows",Integer.parseInt(stringObjectMap.get("counts").toString()));
            reponse.put("totalPages",(Integer.parseInt(stringObjectMap.get("counts").toString()) + pagesize - 1) / pagesize);
            reponse.put("pagesize",pagesize);
            reponse.put("keyword",null);
            reponse.put("searchKey",null);

            results.put("data", reponse);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/leaveApply/listOfApprovingCount", method = RequestMethod.GET, apiTitle = "'我审批的'——待审批数量")
    @ApiOperation("'我审批的'——待审批数量")
    public R approvingOfCount(@ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {

        Map<String, Object> params = new HashMap<>();
        params.put("assignId", approveUserId);
        params.put("statusOfCount",Constant.APPLY_APPROVING);
        Map<String, Object> stringObjectMap = leaveApplyService.countForMap(params);
        params.remove("assignId");
        params.remove("statusOfCount");

        params.put("data", Integer.parseInt(stringObjectMap.get("counts").toString()));

        return R.ok(params);
    }


    @EvApiByToken(value = "/apis/leaveApply/detail",method = RequestMethod.GET,apiTitle = "获取请假详细信息")
    @ApiOperation("获取请假详细信息")
    public R detail(@ApiParam(value = "请假ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results;
        results = leaveApplyService.detail(id);
        return  R.ok(results);
    }


    @EvApiByToken(value = "/apis/leaveApply/approve",method = RequestMethod.POST,apiTitle = "请假审批")
    @ApiOperation("请假审批")
    public R tempSave(@ApiParam(value = "请假信息",required = true) Long leaveApplyId,
                      @ApiParam(value = "是否通过(1:通过；0：拒绝)") @RequestParam(value = "isApproved",defaultValue = "" ) Integer isApproved,
                      @ApiParam(value = "原因") @RequestParam(value = "reason",defaultValue = "",required = false) String reason){
        try {
            leaveApplyService.approve(leaveApplyId,isApproved,reason);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/leaveApply/comment",method = RequestMethod.POST,apiTitle = "回复")
    @ApiOperation("回复")
    public R comment(@ApiParam(value = "请假ID",required = true, example = "1") @RequestParam(value="leaveApplyId",defaultValue = "") Long leaveApplyId,
                     @ApiParam(value = "回复内容",required = true, example = "哈哈哈") @RequestParam(value="comment",defaultValue = "") String comment){
        leaveApplyService.commentLeaveApply(leaveApplyId,comment);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/leaveApply/remove", method = RequestMethod.DELETE, apiTitle = "删除请假申请")
    @ApiOperation("删除请假申请")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "请假申请主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        LeaveApplyDO applyDO = leaveApplyService.get(id);
        if (applyDO != null) {
            if (Objects.equals(Constant.TS, applyDO.getStatus())||Objects.equals(Constant.APPLY_REJECT, applyDO.getStatus())) {
                if (leaveApplyService.remove(id) > 0) {
                    return R.ok();
                }
                return R.error();
            }
            //"单据状态非暂存/退回！不允许删除！"
            return R.error(messageSourceHandler.getMessage("common.massge.allowDelet",null));
        }
        return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
    }

    @EvApiByToken(value = "/apis/leaveApply/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除请假申请")
    @ApiOperation("批量请假申请")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "请假申请主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {

        if (ids.length>0) {
            return leaveApplyService.removeBacth(ids);

        }
        return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
    }


}
