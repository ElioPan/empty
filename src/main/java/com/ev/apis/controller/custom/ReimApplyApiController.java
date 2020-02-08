package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.ReimApplyDO;
import com.ev.custom.service.ReimApplyService;
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
@Api(value = "/", tags = "报销管理")
public class ReimApplyApiController {
    @Autowired
    ReimApplyService reimApplyService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    DeptService deptService;

    @EvApiByToken(value = "/apis/reimApply/list", method = RequestMethod.GET, apiTitle = "获取报销列表信息")
    @ApiOperation("获取列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "审批状态") @RequestParam(value = "statusId", defaultValue = "", required = false) Integer statusId,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
                  @ApiParam(value = "填报日期（开始）") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "填报日期（结束）") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {
        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;
        Long lowderUserId = ShiroUtils.getUserId();

        Map<String, Object> reponse = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("createByName", userName);
            put("beginTime", startTime);
            put("endTime", endTime);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
//            put("sort", "statusId");
//            put("order", "ASC");
        }};
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
        List<Map<String, Object>> data = this.reimApplyService.listForMap(params);
        Map<String, Object> stringObjectMap = this.reimApplyService.countForMap(params);


        if (data != null && data.size() > 0) {
            reponse.put("allReiCount",stringObjectMap.get("allReiCount"));  //所有的报销金额之和
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


    @EvApiByToken(value = "/apis/reimApply/listOfApprovingCount", method = RequestMethod.GET, apiTitle = "'我审批的'——待审批数量")
    @ApiOperation("'我审批的'——待审批数量")
    public R approvingOfCount(@ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("assignId", approveUserId);
            put("statusOfCount",Constant.APPLY_APPROVING);  //63审批中 为 待审核状态
        }};
        Map<String, Object> stringObjectMap = this.reimApplyService.countForMap(params);
        params.remove("assignId");
        params.remove("statusOfCount");

        params.put("data", Integer.parseInt(stringObjectMap.get("counts").toString()));

        return R.ok(params);
    }


    @EvApiByToken(value = "/apis/reimApply/detail",method = RequestMethod.GET,apiTitle = "获取报销详细信息")
    @ApiOperation("获取报销详细信息")
    public R detail(@ApiParam(value = "报销ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<String,Object>();
        results = reimApplyService.detail(id);
        return  R.ok(results);
    }


    @EvApiByToken(value = "/apis/reimApply/approve",method = RequestMethod.POST,apiTitle = "报销审批")
    @ApiOperation("报销审批")
    public R tempSave(@ApiParam(value = "报销信息",required = true) Long reimApplyId,
                      @ApiParam(value = "是否通过") @RequestParam(value = "isApproved",defaultValue = "",required = true) Integer isApproved,
                      @ApiParam(value = "原因") @RequestParam(value = "reason",defaultValue = "",required = false) String reason){
        try {
            reimApplyService.approve(reimApplyId,isApproved,reason);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/reimApply/comment",method = RequestMethod.POST,apiTitle = "回复")
    @ApiOperation("回复")
    public R comment(@ApiParam(value = "报销ID",required = true, example = "1") @RequestParam(value="reimApplyId",defaultValue = "") Long reimApplyId,
                     @ApiParam(value = "回复内容",required = true, example = "哈哈哈") @RequestParam(value="comment",defaultValue = "") String comment){
        reimApplyService.commentReimApply(reimApplyId,comment);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/reimApply/remove",method = RequestMethod.DELETE,apiTitle = "删除报销申请")
    @ApiOperation("删除报销申请")
    public R remove(@ApiParam(value = "报销申请主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){

        if(reimApplyService.remove(id)>0){
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/reimApply/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除报销申请")
    @ApiOperation("批量删除报销申请")
    public R remove(@ApiParam(value = "报销申请主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids[]", defaultValue = "") Long[] ids) {

        if (ids.length > 0) {

            R r = reimApplyService.removeBacth(ids);
            return r;
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/reimApply/saveAddAndChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存")
    @ApiOperation("暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndChange(@ApiParam(value = "报销信息", required = true) ReimApplyDO reimApply,
                              @ApiParam(value = "报销明细:（修改的加明细主键\"id\":\"3\",）[{\"reiCount\":\"88.8\",\"reiDate\":\"2019-09-09 12:00:00\",\"type\":\"52\",\"reiItemDescription\":\"马杀鸡费用88.8\"}]") @RequestParam(value = "newReimApplyItems", defaultValue = "",required = true) String newReimApplyItems,
                              @ApiParam(value = "审核人") @RequestParam(value = "approveList", defaultValue = "", required = true) Long[] approveList,
                              @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage,
                              @ApiParam(value = "删除的明细主键") @RequestParam(value = "deletDetailIds", defaultValue = "", required = false) Long[] deletDetailIds) {

        if ( Objects.isNull(reimApply.getId())) {
            //新增保存

                reimApply.setStatus(Constant.TS);//146暂存
            reimApplyService.tempSave(reimApply, newReimApplyItems, approveList, null,taglocationappearanceImage,null);
                return R.ok();

        } else {
            //更新

            ReimApplyDO reimApplyDoOne = reimApplyService.get(reimApply.getId());
            if (reimApplyDoOne != null) {
                if (Objects.equals(Constant.TS, reimApplyDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, reimApplyDoOne.getStatus())) {//146暂存 62退回
                    reimApplyService.saveChangeAndSbmit(reimApply, newReimApplyItems, approveList,  taglocationappearanceImage,deletDetailIds,0);
                    return R.ok();
                }
                //"此申请已提交/已完成不允许再次修改！"
                return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));

        }
    }

    @EvApiByToken(value = "/apis/reimApply/saveAndSubmit", method = RequestMethod.POST, apiTitle = "提交请假申请（1.从暂存态和退回态修改/未修改后直接提交；2:新增填写明细后直接提交）")
    @ApiOperation("提交报销申请（1.从暂存态和退回态修改/未修改后直接提交；2:新增填写明细后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R saveSbmit(@ApiParam(value = "报销信息", required = true) ReimApplyDO reimApply,
                       @ApiParam(value = "所有的报销明细:（修改的加明细主键\"id\":\"3\",）[{\"reiCount\":\"88.8\",\"reiDate\":\"2019-09-09 12:00:00\",\"type\":\"52\",\"reiItemDescription\":\"马杀鸡费用88.8\"}]") @RequestParam(value = "newReimApplyItems", defaultValue = "") String newReimApplyItems,
                       @ApiParam(value = "审核人") @RequestParam(value = "approveList", defaultValue = "", required = true) Long[] approveList,
                       @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage,
                       @ApiParam(value = "删除的明细主键") @RequestParam(value = "deletDetailIds", defaultValue = "", required = false) Long[] deletDetailIds) {

        if (Objects.isNull(reimApply.getId())) {
            //新增保存+提交

            reimApplyService.submit(reimApply, newReimApplyItems, approveList, null, taglocationappearanceImage, null);
            return R.ok();

        } else {
            //更新+提交
            ReimApplyDO reimApplyDoOne = reimApplyService.get(reimApply.getId());
            if (reimApplyDoOne != null) {
                if (Objects.equals(Constant.TS, reimApplyDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, reimApplyDoOne.getStatus())) {//146暂存 62退回
                    reimApplyService.saveChangeAndSbmit(reimApply, newReimApplyItems, approveList, taglocationappearanceImage, deletDetailIds, 1);
                    return R.ok();
                }
                //"此申请已提交/已完成不允许再次修改！"
                return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }


}
