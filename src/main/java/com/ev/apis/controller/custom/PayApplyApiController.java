package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.PayApplyDO;
import com.ev.custom.service.PayApplyService;
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
@Api(value = "/", tags = "付款管理")
public class PayApplyApiController {
    @Autowired
    PayApplyService payApplyService;

    @Autowired
    DeptService deptService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/payApply/list", method = RequestMethod.GET, apiTitle = "获取付款列表信息")
    @ApiOperation("获取列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
//                  @ApiParam(value = "审批状态") @RequestParam(value = "statusId", defaultValue = "", required = false) Integer statusId,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
                  @ApiParam(value = "填报日期（开始）") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "填报日期（结束）") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {
        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;
        Long lowderUserId = ShiroUtils.getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("createByName", userName);
        params.put("beginTimes", startTime);
        params.put("endTimes", endTime);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        if(!"".equals(userId)&&Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
        }

        if(!"".equals(userId)&&!Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
            params.put("status", Constant.TS);
            params.put("statusOther", Constant.APPLY_REJECT);
        }

        if(!"".equals(approveUserId)){
            params.put("assignId", approveUserId);
            params.put("status", Constant.TS);
            params.put("statusOther", Constant.APPLY_REJECT);
        }

        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.TS);
//            params.put("statusOther", Constant.APPLY_REJECT);
        }

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.payApplyService.listForMap(params);
        Map<String, Object> stringObjectMap = this.payApplyService.countForMap(params);

        if (data.size() > 0) {
            Map<String, Object> reponse = new HashMap<>();
            reponse.put("allTotalNumber",stringObjectMap.get("allTotalNumber"));
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

    @EvApiByToken(value = "/apis/payApply/listOfApprovingCount", method = RequestMethod.GET, apiTitle = "'我审批的'——待审批数量")
    @ApiOperation("'我审批的'——待审批数量")
    public R approvingOfCount(@ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {

        Map<String, Object> params = new HashMap<>();
        params.put("assignId", approveUserId);
        params.put("statusOfCount",Constant.APPLY_APPROVING);


        Map<String, Object> stringObjectMap = this.payApplyService.countForMap(params);
        params.remove("assignId");
        params.remove("statusOfCount");

        params.put("data", Integer.parseInt(stringObjectMap.get("counts").toString()));

        return R.ok(params);
    }


    @EvApiByToken(value = "/apis/payApply/detail",method = RequestMethod.GET,apiTitle = "获取付款详细信息")
    @ApiOperation("获取付款详细信息")
    public R detail(@ApiParam(value = "付款ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results  ;
        results = payApplyService.detail(id);
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/payApply/approve",method = RequestMethod.POST,apiTitle = "付款审批")
    @ApiOperation("付款审批")
    public R tempSave(@ApiParam(value = "付款信息",required = true) Long payApplyId,
                      @ApiParam(value = "是否通过") @RequestParam(value = "isApproved",defaultValue = "") Integer isApproved,
                      @ApiParam(value = "原因") @RequestParam(value = "reason",defaultValue = "",required = false) String reason){
        try {
            payApplyService.approve(payApplyId,isApproved,reason);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/payApply/comment",method = RequestMethod.POST,apiTitle = "回复")
    @ApiOperation("回复")
    public R comment(@ApiParam(value = "付款ID",required = true, example = "1") @RequestParam(value="payApplyId",defaultValue = "") Long payApplyId,
                     @ApiParam(value = "回复内容",required = true, example = "哈哈哈") @RequestParam(value="comment",defaultValue = "") String comment){
        payApplyService.commentPayApply(payApplyId,comment);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/payApply/remove",method = RequestMethod.DELETE,apiTitle = "删除付款申请")
    @ApiOperation("删除付款申请")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "付款申请主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){

        PayApplyDO payApplyDO = payApplyService.get(id);
        if (payApplyDO != null) {
            if (Objects.equals(Constant.TS, payApplyDO.getStatus())||Objects.equals(Constant.APPLY_REJECT, payApplyDO.getStatus())) {
                if(payApplyService.remove(id)>0){
                    return R.ok();
                }
                return R.error();
            }
            //"单据状态非暂存/退回！不允许删除！"
            return R.error(messageSourceHandler.getMessage("common.massge.allowDelet",null));
        }
        return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
    }

    @EvApiByToken(value = "/apis/payApply/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除付款申请")
    @ApiOperation("批量删除付款申请")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "付款申请主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids[]") Long[] ids){
        if (ids.length>0) {
            return payApplyService.removeBacth(ids);
        }
        return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
    }

    @EvApiByToken(value = "/apis/payApply/addAndSaveChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存")
    @ApiOperation("暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndSaveChange(@ApiParam(value = "付款信息", required = true) PayApplyDO payApply,
                                  @ApiParam(value = "审核人") @RequestParam(value = "newApproveList", defaultValue = "") Long[] newApproveList,
                                  @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {

        if ( Objects.isNull(payApply.getId())) {//payApply.getId() == null
            //暂存

                payApply.setStatus(Constant.TS);//46暂存
                payApplyService.tempSave(payApply, newApproveList, null, taglocationappearanceImage, null);
                return R.ok();

        } else {
            //更新
                PayApplyDO payApplyDoOne = payApplyService.get(payApply.getId());
                if (payApplyDoOne != null) {

                    if (Objects.equals(Constant.TS, payApplyDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, payApplyDoOne.getStatus())) {//146+62
                        payApplyService.saveChangeAndSbmit(payApply, newApproveList, taglocationappearanceImage,0);
                        return R.ok();
                    }
                    //"此申请已提交/已完成不允许修改！"
                    return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
                }
                //提交的id不存在
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }

    @EvApiByToken(value = "/apis/payApply/saveSbmitChange", method = RequestMethod.POST)
    @ApiOperation("提交付款申请（1.从暂存态和退回态修改/未修改后直接提交；2:新增填写明细后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R saveSbmit(@ApiParam(value = "付款信息", required = true) PayApplyDO payApply,
                       @ApiParam(value = "审核人") @RequestParam(value = "newApproveList", defaultValue = "" ) Long[] newApproveList,
                       @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {
        if (!Objects.nonNull(payApply.getId())) {
                //保存+提交
                payApplyService.submit(payApply, newApproveList, null, taglocationappearanceImage,null);
                return R.ok();
        } else {
            //更新+提交
            //验证是否重复提交
                PayApplyDO payApplyDoOne = payApplyService.get(payApply.getId());
                if (payApplyDoOne != null) {
                if (Objects.equals(Constant.TS, payApplyDoOne.getStatus())||Objects.equals(Constant.APPLY_REJECT, payApplyDoOne.getStatus())) {//146+62
                    payApplyService.saveChangeAndSbmit(payApply, newApproveList, taglocationappearanceImage,1);
                    return R.ok();
                }
                return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
                }
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }

}
