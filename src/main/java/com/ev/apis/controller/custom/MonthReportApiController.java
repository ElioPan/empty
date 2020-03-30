package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.NoticeService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.MonthReportDO;
import com.ev.custom.service.MonthReportService;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@Api(value = "/", tags = "月报管理")
public class MonthReportApiController {
    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserAssocService userAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private NoticeService noticeService;


    @EvApiByToken(value = "/apis/monthReport/list", method = RequestMethod.GET, apiTitle = "获取月报列表信息")
    @ApiOperation("获取任月报列表信息--我发起/发给我的/我的团队")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "被发送人ID") @RequestParam(value = "beSentPeopleId", defaultValue = "", required = false) String beSentPeopleId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId) {
        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;
        Long lowderUserId = ShiroUtils.getUserId();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("userName", userName);
            put("idPath", idPath);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
            put("startTime", startTime);
            put("endTime", endTime);
        }};

        if(!"".equals(userId)&&Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
        }

        if(!"".equals(beSentPeopleId)) {
            params.put("beSentPeopleId", beSentPeopleId);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
//            params.put("sign",1);
        }

        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
        }

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.monthReportService.listForMap(params);
        int total = this.monthReportService.countForMap(params);
        if (data != null && data.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((Integer) ((total + pagesize - 1) / pagesize));
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/monthReport/detail", method = RequestMethod.POST, apiTitle = "获取月报详细信息")
    @ApiOperation("获取月报详细信息")
    public R detail(@ApiParam(value = "月报ID", required = true) @RequestParam(value = "id", defaultValue = "", required = false) Long id) {
        Map<String, Object> results = new HashMap<String, Object>();
        results = monthReportService.detail(id);

        //将已经查看到的标记为已读
        Long userId = ShiroUtils.getUserId();
        int rows = userAssocService.changeOfUserAssocSign(userId, id, "MONTH_REPORT_TARGET");

        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/monthReport/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除月报")
    @ApiOperation("删除月报/批量删除月报")
    public R remove(@ApiParam(value = "月报主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("id", ids);
        query.put("status", Constant.TS);//146暂存
        R r = monthReportService.listOfCanDelet(query,ids);
        return r;
    }

    @EvApiByToken(value = "/apis/monthReport/comment", method = RequestMethod.POST, apiTitle = "回复月报")
    @ApiOperation("回复月报")
    public R remove(@ApiParam(value = "月报ID", required = true, example = "1") @RequestParam(value = "monthReportId", defaultValue = "") Long monthReportId,
                    @ApiParam(value = "回复内容", required = true, example = "哈哈哈") @RequestParam(value = "comment", defaultValue = "") String comment) throws IOException, ParseException {
        monthReportService.commentMonthReport(monthReportId, comment);
        JSONObject contentDetail = new JSONObject();
        contentDetail.put("id",monthReportId);
        contentDetail.put("url","/month/monthDetail?id="+monthReportId);
        List<Long> toUsers = new ArrayList<>();
        toUsers.add(monthReportService.get(monthReportId).getCreateBy());
        noticeService.saveAndSendSocket("月报回复信息",comment, monthReportId, contentDetail.toString(),2L,ShiroUtils.getUserId(),toUsers);
        return R.ok();
    }


    @EvApiByToken(value = "/apis/monthReport/getCuntOfsenderToMe", method = RequestMethod.POST, apiTitle = "发给我的--未读取的数量")
    @ApiOperation("发给我的--未读取的数量")
    public R countOfSendToOther(@ApiParam(value = "被发送人ID", required = true) @RequestParam(value = "beSentPeopleId", defaultValue = "", required = false) Long beSentPeopleId) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("beSentPeopleId", beSentPeopleId);
        int cuntOfsenderToMe = monthReportService.countOfQuantyForward(results);
        results.remove("beSentPeopleId");
        results.put("data", cuntOfsenderToMe);
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/monthReport/saveAndChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 ——月报信息")
    @ApiOperation("暂存/修改暂存 ——月报信息")
    @Transactional(rollbackFor = Exception.class)
    public R saveAndChangeDetail(@ApiParam(value = "月报基础信息", required = true) MonthReportDO monthReport,
                  @ApiParam(value = "发给谁") @RequestParam(value = "newTargetList", defaultValue = "", required = false) Long[] newTargetList,
                  @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {

        if (!Objects.nonNull(monthReport.getId())) {
            if(monthReportService.duplicateDetectionOrNot()){
                monthReport.setStatus(Constant.TS);
                monthReportService.add(monthReport, newTargetList, taglocationappearanceImage);
                return R.ok();
            }else{
                //"本月月报已建立，请勿重复创建！！"
                return R.error(messageSourceHandler.getMessage("apis.monthReport.addAndSaveChange",null));
            }

        } else {
            MonthReportDO monthReportDO = monthReportService.get(monthReport.getId());
            if (monthReportDO != null) {

                if (!(Objects.equals(Constant.APPLY_APPROED, monthReportDO.getStatus()))) {  //148 已提交   允许修改
                    //更新
                    monthReportService.allPowerfulMelthod(monthReport,newTargetList,taglocationappearanceImage,0);
                    return R.ok();
                } else {
                    //已提交不允许修改！
                    return R.error(messageSourceHandler.getMessage("common.dailyReport.savChange",null));
                }
            }
            //提交的id不存在！
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/monthReport/submintApproveMonth", method = RequestMethod.POST, apiTitle = "提交月报（1.新增填写明细后直接提交；2.查看明细进行修改/未修改后直接提交）")
    @ApiOperation("提交月报（1.新增填写明细后直接提交；2.查看明细进行修改/未修改后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R submintApprove(@ApiParam(value = "月报基础信息", required = true) MonthReportDO monthReport,
                            @ApiParam(value = "新增的‘发送人ID") @RequestParam(value = "newTargetList", defaultValue = "", required = false) Long[] newTargetList,
                            @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "",  required = false) String[] taglocationappearanceImage) {

        if (!Objects.nonNull(monthReport.getId())) {//Objects.nonNull(dailyReport.getId())
            //1.新增填写明细后直接提交
               //验证本月是否新建，默认当月新建当月的月报。一个月只在本月建立一次。
            if (monthReportService.duplicateDetectionOrNot()) {
                monthReport.setStatus(Constant.APPLY_APPROED);//148状态 已提交
                monthReportService.add(monthReport, newTargetList, taglocationappearanceImage);
                return R.ok();
            } else {
                //"本月月报已建立，请勿重复创建！！"
                return R.error(messageSourceHandler.getMessage("apis.monthReport.addAndSaveChange",null));
            }

        } else {
            MonthReportDO monthReportDO = monthReportService.get(monthReport.getId());
            if (monthReportDO != null) {
                //2.查看明细进行修改/未修改后直接提交
                if (!(Objects.equals(Constant.APPLY_APPROED, monthReportDO.getStatus()))) {  //148 已提交    允许修改并提交
                    //更新
                    monthReportService.allPowerfulMelthod(monthReport, newTargetList, taglocationappearanceImage, 1);
                    return R.ok();
                } else {
                    //"已提交不允许再次提交！"
                    return R.error(messageSourceHandler.getMessage("common.dailyReport.submit",null));
                }
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }




}



