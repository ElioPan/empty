package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.OverTimeApplyDO;
import com.ev.custom.service.OverTimeApplyService;
import com.ev.system.domain.DeptDO;
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
@Api(value = "加班申请管理", tags = "加班申请管理")
public class OverTimeApplyApiController {
    @Autowired
    OverTimeApplyService overTimeApplyService;

    @Autowired
    DeptService deptService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/overTimeApply/list", method = RequestMethod.GET, apiTitle = "获取加班列表信息")
    @ApiOperation("获取列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "审批状态") @RequestParam(value = "statusId", defaultValue = "", required = false) Integer statusId,
                  @ApiParam(value = "填报日期（开始）") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "填报日期（结束）") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
                  @ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {
        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;

        Long lowderUserId = ShiroUtils.getUserId();
        DeptDO deptDO= deptService.getIdpathByUserId(lowderUserId);
        String lowderIdpath =deptDO!=null?deptDO.getIdPath():null;

        Map<String, Object> reponse = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("createByName", userName);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
            put("beginTimes", startTime);
            put("endTimes", endTime);
//            put("sort", "statusId");
//            put("order", "ASC");
        }};

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

        //所有的中显示退回
        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.TS);
//            params.put("statusOther", Constant.APPLY_REJECT);
        }


        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.overTimeApplyService.listForMap(params);
        Map<String, Object> stringObjectMap = this.overTimeApplyService.countForMap(params);

        if (data != null && data.size() > 0) {
            reponse.put("allTimearea", stringObjectMap.get("allTimearea"));  //所有的加班时长之和
            reponse.put("datas", data);
            reponse.put("pageno", pageno);
            reponse.put("totalRows", Integer.parseInt(stringObjectMap.get("counts").toString()));
            reponse.put("totalPages", (Integer.parseInt(stringObjectMap.get("counts").toString()) + pagesize - 1) / pagesize);
            reponse.put("pagesize", pagesize);
            reponse.put("keyword", null);
            reponse.put("searchKey", null);

            results.put("data", reponse);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/overTimeApply/listOfApprovingCount", method = RequestMethod.GET, apiTitle = "'我审批的'——待审批数量")
    @ApiOperation("'我审批的'——待审批数量")
    public R approvingOfCount(@ApiParam(value = "审批人ID") @RequestParam(value = "approveUserId", defaultValue = "", required = false) String approveUserId) {

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("assignId", approveUserId);
            put("statusOfCount",Constant.APPLY_APPROVING);  //63审批中 为 待审核状态

        }};
        Map<String, Object> stringObjectMap = this.overTimeApplyService.countForMap(params);
        params.remove("assignId");
        params.remove("statusOfCount");

        params.put("data", Integer.parseInt(stringObjectMap.get("counts").toString()));

        return R.ok(params);
    }


    @EvApiByToken(value = "/apis/overTimeApply/detail",method = RequestMethod.GET,apiTitle = "获取加班详细信息")
    @ApiOperation("获取加班详细信息")
    public R detail(@ApiParam(value = "加班ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<String,Object>();
        results = overTimeApplyService.detail(id);
        return  R.ok(results);
    }


    @EvApiByToken(value = "/apis/overTimeApply/approve",method = RequestMethod.POST,apiTitle = "加班审批")
    @ApiOperation("加班审批")
    public R tempSave(@ApiParam(value = "加班信息",required = true) Long overTimeApplyId,
                      @ApiParam(value = "是否通过") @RequestParam(value = "isApproved",defaultValue = "",required = true) Integer isApproved,
                      @ApiParam(value = "原因") @RequestParam(value = "reason",defaultValue = "",required = false) String reason){
        try {
            overTimeApplyService.approve(overTimeApplyId,isApproved,reason);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/overTimeApply/comment",method = RequestMethod.POST,apiTitle = "回复")
    @ApiOperation("回复")
    public R comment(@ApiParam(value = "加班ID",required = true, example = "1") @RequestParam(value="overTimeApplyId",defaultValue = "") Long overTimeApplyId,
                     @ApiParam(value = "回复内容",required = true, example = "哈哈哈") @RequestParam(value="comment",defaultValue = "") String comment){
        overTimeApplyService.commentOverTimeApply(overTimeApplyId,comment);
        return R.ok();
    }


    @EvApiByToken(value = "/apis/overTimeApply/tempSaveAndSaveChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存")
    @ApiOperation("暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndChange(@ApiParam(value = "加班信息", required = true) OverTimeApplyDO overTimeApply,
                              @ApiParam(value = "审核人") @RequestParam(value = "newApproveList", defaultValue = "", required = true) Long[] newApproveList,
                              @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] newImage) {

        if (Objects.isNull(overTimeApply.getId())) {

                overTimeApply.setStatus(Constant.TS);//146 暂存
                overTimeApplyService.tempSave(overTimeApply, newApproveList, null, newImage, null);
                return R.ok();

        } else {
            //验证并更新

                OverTimeApplyDO overTimeApplyDoOne = overTimeApplyService.get(overTimeApply.getId());
                if (overTimeApplyDoOne!=null) {
                    if (Objects.equals(Constant.TS, overTimeApplyDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, overTimeApplyDoOne.getStatus())) {//146+62
                        overTimeApplyService.saveChangeAndSbmit(overTimeApply, newApproveList, newImage, 0);
                        return R.ok();
                    }
                    //此申请已提交/已完成不允许修改！
                    return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
                }
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/overTimeApply/saveSbmitAndChange", method = RequestMethod.POST)
    @ApiOperation("提交加班申请（1.从暂存态和退回态修改/未修改后直接提交；2:新增填写明细后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R saveSbmit(@ApiParam(value = "加班信息", required = true) OverTimeApplyDO overTimeApply,
                       @ApiParam(value = "审核人") @RequestParam(value = "newApproveList", defaultValue = "", required = true) Long[] newApproveList,
                       @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] newImage) {

        if (Objects.isNull(overTimeApply.getId())) {
                overTimeApplyService.submit(overTimeApply, newApproveList, null, newImage, null);
                return R.ok();
        } else {
            //验证并更新
                OverTimeApplyDO overTimeApplyDoOne = overTimeApplyService.get(overTimeApply.getId());
                if (overTimeApplyDoOne != null) {
                    if (Objects.equals(Constant.TS, overTimeApplyDoOne.getStatus()) || Objects.equals(Constant.APPLY_REJECT, overTimeApplyDoOne.getStatus())) {//146+62

                        overTimeApplyService.saveChangeAndSbmit(overTimeApply, newApproveList,newImage,1);
                        return R.ok();
                    }
                    //"此申请已提交/已完成不允许再次提交！"
                    return R.error(messageSourceHandler.getMessage("common.massge.noAllowChanfe",null));
                }
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/overTimeApply/batchRemove", method = RequestMethod.POST, apiTitle = "删除/批量加班申请")
    @ApiOperation("删除/批量加班申请")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "加班申请主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids[]", defaultValue = "") Long[] ids) {

        //146L,//62退回 {Constant.TS,Constant.APPLY_APPROED};
        long[] status = {Constant.TS, Constant.APPLY_REJECT};

        Map<String, Object> query = new HashMap<String, Object>();
        query.put("id", ids);
        query.put("status", status);//146暂存

            R r = overTimeApplyService.listOfCanDelet(query,ids);
            return r;
    }



}
