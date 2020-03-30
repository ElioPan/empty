package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.NoticeService;
import com.ev.custom.service.WeChatService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.DailyReportDO;
import com.ev.custom.service.DailyReportService;
import com.ev.custom.service.UserAssocService;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@Api(value = "/", tags = "日报管理API")
public class DailyReportApiController {
    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    DeptService deptService;
    @Autowired
    private UserAssocService userAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private WeChatService weChatService;

    @EvApiByToken(value = "/apis/dailyReport/list", method = RequestMethod.POST, apiTitle = "获取日志列表信息")
    @ApiOperation("获取日志列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = "", required = false) String userId,
                  @ApiParam(value = "被发送人的ID") @RequestParam(value = "beSentPeopleId", defaultValue = "", required = false) String beSentPeopleId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                  @ApiParam(value = "截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId) {

        String idPath = StringUtils.isNotEmpty(deptId) ? deptService.get(Long.parseLong(deptId)).getIdPath() : null;

        Long lowderUserId = ShiroUtils.getUserId();
        DeptDO deptDO= deptService.getIdpathByUserId(lowderUserId);
        String lowderIdpath =deptDO!=null?deptDO.getIdPath():null;

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("userName", userName);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
            put("startTime", startTime);
            put("endTime", endTime);
//            put("sort", "status");
//            put("order", "ASC");
        }};
        if(!"".equals(userId)&&Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);

        }

        if(!"".equals(beSentPeopleId)) {

            params.put("beSentPeopleId", beSentPeopleId);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
//            params.put("sign", 1);
        }

        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
        }

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.dailyReportService.listForMap(params);
        int total = this.dailyReportService.countForMap(params);
        if (data != null && data.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse() {{
                setDatas(data);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(total);
                setTotalPages((Integer) ((total + pagesize - 1) / pagesize));
            }};
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/dailyReport/getCuntOfsenderToMe", method = RequestMethod.POST, apiTitle = "发给我的--未读取的数量")
    @ApiOperation("发给我的--未读取的数量")
    public R countOfSendToOther(@ApiParam(value = "被发送人ID", required = true) @RequestParam(value = "beSentPeopleId", defaultValue = "", required = false) Long beSentPeopleId) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("beSentPeopleId",beSentPeopleId);

        int cuntOfsenderToMe = dailyReportService.countOfQuantyForward(results);

        results.remove("beSentPeopleId");
        results.put("data",cuntOfsenderToMe);
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/dailyReport/detail", method = RequestMethod.POST, apiTitle = "获取日志详细信息")
    @ApiOperation("获取日志详细信息")
    public R detail(@ApiParam(value = "日志ID", required = true) @RequestParam(value = "id", defaultValue = "", required = false) Long id) {
        Map<String, Object> results = new HashMap<String, Object>();
        results = dailyReportService.detail(id);

        //将已经查看到的日志标记为已读
        Long userId=ShiroUtils.getUserId();
        int rows=userAssocService.changeOfUserAssocSign(userId,id,"DAILY_REPORT_TARGET");

        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/dailyReport/addAndSaveChange", method = RequestMethod.POST, apiTitle = "新增暂存/修改暂存")
    @ApiOperation("新增暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndSaveChange(@ApiParam(value = "日志信息", required = true) DailyReportDO dailyReport,
                                  @ApiParam(value = "所有的‘发送人ID’") @RequestParam(value = "targetList", defaultValue = "", required = false) Long[] targetList,
                                  @ApiParam(value = "所有的上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {

        if (!Objects.nonNull(dailyReport.getId())) {
            if (dailyReportService.duplicateDetectionOrNot()) {
                dailyReport.setStatus(Constant.TS);//146暂存
                dailyReportService.add(dailyReport, targetList, taglocationappearanceImage);
                return R.ok();
            } else {
                //今天日报已写！请勿重复！
                return R.error(messageSourceHandler.getMessage("apis.dailyReport.addAndSaveChange",null));
            }
        } else {
            DailyReportDO dailyReportDoOne = dailyReportService.get(dailyReport.getId());
            if (dailyReportDoOne != null) {

                if (!(Objects.equals(Constant.APPLY_APPROED, dailyReportDoOne.getStatus()))) {  //148 已提交   允许修改
                    //更新
                    dailyReportService.allPowerfulMelthod(dailyReport, targetList, taglocationappearanceImage, 0);
                    return R.ok();
                } else {
                    //已提交不允许修改
                    return R.error(messageSourceHandler.getMessage("common.dailyReport.savChange",null));
                }
            }
            //提交的id不存在
            return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
        }


    }


    @EvApiByToken(value = "/apis/dailyReport/submintApproveDaliy", method = RequestMethod.POST, apiTitle = "提交日志")
    @ApiOperation("提交日志（1.新增填写明细后直接提交；2.查看明细进行修改/未修改后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R submintApprove(@ApiParam(value = "日志信息", required = true) DailyReportDO dailyReport,
                            @ApiParam(value = "新增的‘发送人ID") @RequestParam(value = "targetList", defaultValue = "", required = false) Long[] targetList,
                            @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {

        if (!Objects.nonNull(dailyReport.getId())) {//Objects.nonNull(dailyReport.getId())
            //1.新增填写明细后直接提交
            if(dailyReportService.duplicateDetectionOrNot()){
                dailyReport.setStatus(Constant.APPLY_APPROED);//148状态 已提交
                dailyReportService.add(dailyReport, targetList, taglocationappearanceImage);
                return R.ok();
            }else{
                //今天日报已写！请勿重复
                return R.error(messageSourceHandler.getMessage("apis.dailyReport.addAndSaveChange",null));
            }
        } else {
            DailyReportDO dailyReportDO = dailyReportService.get(dailyReport.getId());
            if (dailyReportDO != null) {
                //2.查看明细进行修改/未修改后直接提交
                if (!(Objects.equals(Constant.APPLY_APPROED, dailyReportDO.getStatus()))) {  //148 已提交    允许修改并提交
                    //更新
                    dailyReportService.allPowerfulMelthod(dailyReport, targetList, taglocationappearanceImage, 1);
                    return R.ok();
                } else {
                    //请勿重复提交
                    return R.error(messageSourceHandler.getMessage("common.dailyReport.submit",null));
                }
            }
            //提交的id不存在
            return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
        }
    }


    @EvApiByToken(value = "/apis/dailyReport/comment", method = RequestMethod.POST, apiTitle = "回复日志")
    @ApiOperation("回复日志")
    public R remove(@ApiParam(value = "日志ID", required = true, example = "1") @RequestParam(value = "dailyReportId", defaultValue = "") Long dailyReportId,
                    @ApiParam(value = "回复内容", required = true, example = "哈哈哈") @RequestParam(value = "comment", defaultValue = "") String
                            comment) throws IOException, ParseException {
        dailyReportService.commentDailyReport(dailyReportId, comment);
        JSONObject contentDetail = new JSONObject();
        contentDetail.put("id",dailyReportId);
        contentDetail.put("url","/daily/dailyDetail?id="+dailyReportId);
        List<Long> toUsers = new ArrayList<>();
        toUsers.add(dailyReportService.get(dailyReportId).getCreateBy());
        noticeService.saveAndSendSocket("日志回复信息",comment,dailyReportId,contentDetail.toString(),1L,ShiroUtils.getUserId(),toUsers);
        return R.ok();
    }


    @EvApiByToken(value = "/apis/dailyReport/batchRemove", method = RequestMethod.POST, apiTitle = "删除/批量删除日志")
    @ApiOperation("删除/批量删除日志")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "日志主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("id", ids);
        query.put("status", Constant.TS);//146暂存

            R r = dailyReportService.listOfCanDelet(query,ids);
            return r;
    }



}
