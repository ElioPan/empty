package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.NoticeService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.WeekReportDO;
import com.ev.custom.service.UserAssocService;
import com.ev.custom.service.WeekReportService;
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
@Api(value = "/",tags = "周报管理API")
public class WeekReportApiController {
    @Autowired
    WeekReportService weekReportService;

    @Autowired
    DeptService deptService;
    @Autowired
    private UserAssocService userAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private NoticeService noticeService;

    @EvApiByToken(value = "/apis/weekReport/list", method = RequestMethod.GET, apiTitle = "获取周报列表信息")
    @ApiOperation("获取列表信息")
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
        DeptDO deptDO= deptService.getIdpathByUserId(lowderUserId);
        String lowderIdpath =deptDO!=null?deptDO.getIdPath():null;

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("userName", userName);
            put("offset", (pageno - 1) * pagesize);
            put("limit", pagesize);
            put("startTime",startTime);
            put("endTime",endTime);
    }};

        if(!"".equals(userId)&&Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);

        }
        if(!"".equals(userId)&&!Objects.equals(userId,lowderUserId.toString())){
            params.put("createBy", userId);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
        }

        //发给我的
        if(!"".equals(beSentPeopleId)) {
            // params.put("createBy", userId);
            params.put("beSentPeopleId", beSentPeopleId);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
//            params.put("sign", 1);

        }

        //我的团队
        if(idPath!=null){
            params.put("idPath", idPath);
            params.put("status", Constant.APPLY_APPROED);//已提交：148
        }

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.weekReportService.listForMap(params);
        int total = this.weekReportService.countForMap(params);
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

    @EvApiByToken(value = "/apis/weekReport/detail",method = RequestMethod.GET,apiTitle = "获取周报详细信息")
    @ApiOperation("获取周报详细信息")
    public R detail(@ApiParam(value = "周报ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<String,Object>();
        results = weekReportService.detail(id);

        //将已经查看到的标记为已读
        Long userId= ShiroUtils.getUserId();
        int rows=userAssocService.changeOfUserAssocSign(userId,id,"WEEK_REPORT_TARGET");

        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/weekReport/comment",method = RequestMethod.POST,apiTitle = "回复周报")
    @ApiOperation("回复周报")
    public R comment(@ApiParam(value = "周报ID",required = true, example = "1") @RequestParam(value="weekReportId",defaultValue = "") Long weekReportId,
                     @ApiParam(value = "回复内容",required = true, example = "哈哈哈") @RequestParam(value="comment",defaultValue = "") String comment) throws IOException, ParseException {
        weekReportService.commentWeekReport(weekReportId,comment);
        JSONObject contentDetail = new JSONObject();
        contentDetail.put("id",weekReportId);
        List<Long> toUsers = new ArrayList<>();
        toUsers.add(weekReportService.get(weekReportId).getCreateBy());
        noticeService.saveAndSendSocket("周报回复信息",comment,contentDetail.toString(),284L,ShiroUtils.getUserId(),toUsers);
        return R.ok();
    }

    @EvApiByToken(value = "/apis/weekReport/addAndSaveChange", method = RequestMethod.POST, apiTitle = "新增暂存/修改暂存")
    @ApiOperation("新增暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R saveAddAndSaveChange(@ApiParam(value = "周报信息", required = true) WeekReportDO weekReport,
                                  @ApiParam(value = "所有的周报明细信息：[{\"reportDate\":\"2019-09-27\",\"week\":\"星期一\",\"mustContent\":\"必做事项\",\"waitContet\":\"待做事项\",\"needSource\":\"需求资源\"}]") @RequestParam(value = "newWeekReportItems", defaultValue = "", required = false) String newWeekReportItems,
                                  @ApiParam(value = "发给谁") @RequestParam(value = "targetList", defaultValue = "", required = false) Long[] targetList,
                                  @ApiParam(value = "上传附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage) {

        if (!Objects.nonNull(weekReport.getId())) {
            if(weekReportService.duplicateDetectionOrNot(newWeekReportItems)){

                weekReport.setStatus(Constant.TS);//146暂存
                weekReportService.add(weekReport, newWeekReportItems, targetList, taglocationappearanceImage);
                return R.ok();
            }else{
                //"本周周报已写！请勿重复新建！"
                return R.error(messageSourceHandler.getMessage("apis.weekReport.addAndSaveChange",null));
            }

        } else {

                WeekReportDO weekReportDoOne = weekReportService.get(weekReport.getId());
                if (weekReportDoOne!=null) {
                    if (!(Objects.equals(Constant.APPLY_APPROED, weekReportDoOne.getStatus()))) {  //148已提交不允许修改
                        //更新
                        weekReportService.saveWeekChangeAndSbmit(weekReport, newWeekReportItems, targetList,  taglocationappearanceImage,0);
                        return R.ok();
                    } else {
                        //"已提交不允许修改！"
                        return R.error(messageSourceHandler.getMessage("common.dailyReport.savChange",null));
                    }
                }
                //"提交的id不存在！"
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }


    @EvApiByToken(value = "/apis/weekReport/submintApproveDaliy", method = RequestMethod.POST, apiTitle = "提交周报")
    @ApiOperation("提交周报（1.新增填写明细后直接提交；2.查看明细进行修改/未修改后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R submintApprove(@ApiParam(value = "周报信息", required = true) WeekReportDO weekReport,
                            @ApiParam(value = "所有的周报明细信息：（更新加\"id\":\"6\",）[{\"reportDate\":\"2019-09-27\",\"week\":\"星期一\",\"mustContent\":\"必做事项\",\"waitContet\":\"待做事项\",\"needSource\":\"需求资源\"}]") @RequestParam(value = "newWeekReportItems", defaultValue = "", required = false) String newWeekReportItems,
                            @ApiParam(value = "发给谁") @RequestParam(value = "targetList", defaultValue = "", required = false) Long[] targetList,
                            @ApiParam(value = "附件") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] tagImage) {
        if (!Objects.nonNull(weekReport.getId())) {
            if(weekReportService.duplicateDetectionOrNot(newWeekReportItems)){
                weekReport.setStatus(Constant.APPLY_APPROED);//148已提交
                weekReportService.add(weekReport, newWeekReportItems, targetList, tagImage);
                return R.ok();
            }else{
                //"本周周报已写！请勿重复新建！"
                return R.error(messageSourceHandler.getMessage("apis.weekReport.addAndSaveChange",null));
            }

        } else {
                WeekReportDO weekReportDoOne = weekReportService.get(weekReport.getId());
                if (weekReportDoOne != null) {
                    if (!(Objects.equals(Constant.APPLY_APPROED, weekReportDoOne.getStatus()))) {  //允许修改
                        //更新
                        weekReportService.saveWeekChangeAndSbmit(weekReport, newWeekReportItems, targetList, tagImage, 1);
                        return R.ok();
                    } else {
                        //"已提交不允许修改！"
                        return R.error(messageSourceHandler.getMessage("common.dailyReport.savChange",null));
                    }
                }
                return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }

    @EvApiByToken(value = "/apis/weekReport/getCuntOfsenderToMe", method = RequestMethod.POST, apiTitle = "发给我的--未读取的数量")
    @ApiOperation("发给我的--未读取的数量")
    public R countOfSendToOther(@ApiParam(value = "被发送人ID", required = true) @RequestParam(value = "beSentPeopleId", defaultValue = "", required = true) Long beSentPeopleId) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("beSentPeopleId", beSentPeopleId);
        int cuntOfsenderToMe = weekReportService.countOfQuantyForward(results);
        results.remove("beSentPeopleId");
        results.put("data",cuntOfsenderToMe);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/weekReport/batchRemove",method = RequestMethod.POST,apiTitle = "删除/批量删除周报")
    @ApiOperation("删除/批量删除周报")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "周报主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "", required = true) Long[] ids){
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("id", ids);
            query.put("status", Constant.TS);//146暂存

                R r = weekReportService.listOfCanDelet(query,ids);
                return r;
    }



//    @EvApiByToken(value = "/apis/weekReport/getDateOfWeek", method = RequestMethod.POST, apiTitle = "获取周计划时间")
//    @ApiOperation("获取周计划时间")
//    public R getDateOfWeek() throws ParseException {
//        Map<String, Object> result = new HashMap<String, Object>();
//
//        Date date = new Date();
//        String weekOfThisDay = DatesUtil.getWeekOfDate(date);
//        int index = DatesUtil.getWeekOfDateIndex(date);
//        System.out.println("==========index====weekOfThisDay======"+index+"+++"+weekOfThisDay+"==================");
//
//
//        List<String> datesBetweenTwoDate = DatesUtil.getDatesBetweenTwoDate("2019-11-03 00:00:00", "2019-11-10 00:00:00");
//
//
//        System.out.println("==========datesBetweenTwoDate======"+datesBetweenTwoDate+"==================");
//        result.put("data",datesBetweenTwoDate);
//
//        switch (index) {
//            case 0:
//                break;
//            case 1:
//                System.out.println();
//                break;
//            case 2:
//                System.out.println();
//                break;
//            case 3:
//                System.out.println();
//                break;
//            case 4:
//                System.out.println();
//                break;
//            case 5:
//                System.out.println();
//                break;
//            case 6:
//                System.out.println();
//                break;
//        }
//
//        return R.ok(result);
//    }






}


