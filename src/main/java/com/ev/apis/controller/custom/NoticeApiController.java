package com.ev.apis.controller.custom;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.NoticeAssocDO;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.NoticeAssocService;
import com.ev.custom.service.NoticeService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
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

@Api(value = "/",tags = "通知管理API")
@RestController
public class NoticeApiController {
    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeAssocService noticeAssocService;

    @EvApiByToken(value = "/apis/notice/list", method = RequestMethod.GET, apiTitle = "获取消息列表")
    @ApiOperation("获取消息列表")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "标题") @RequestParam(value = "title", defaultValue = "", required = false) String title,
                  @ApiParam(value = "类型") @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
                  @ApiParam(value = "接收人") @RequestParam(value = "toUserId", defaultValue = "", required = false) Long toUserId,
                  @ApiParam(value = "状态(0:未读;1:已读") @RequestParam(value = "signStatus", defaultValue = "", required = false) String signStatus) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
        params.put("title", title);
        params.put("type", type);
        params.put("toUserId", toUserId);
        params.put("signStatus", signStatus);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.noticeService.listForMap(params);
        int total = this.noticeService.countForMap(params);
        if (data != null && data.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/notice/countNotRead", method = RequestMethod.GET, apiTitle = "获取未读消息数量")
    @ApiOperation("获取未读消息数量")
    public R countNotRead() {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
        params.put("signStatus", "0");
        params.put("toUserId", ShiroUtils.getUserId());
        int count = noticeService.countForMap(params);
        Map<String, Object> results = Maps.newHashMap();
        results.put("notReadCount",count);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/notice/batchRemove", method = RequestMethod.POST, apiTitle = "删除/批量删除消息")
    @ApiOperation("删除/批量删除消息")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "消息主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {

        noticeAssocService.batchRemove(ids);
        return R.ok();

    }

    @EvApiByToken(value = "/apis/notice/auditSign", method = RequestMethod.POST, apiTitle = "标记已读")
    @ApiOperation("标记已读")
    @Transactional(rollbackFor = Exception.class)
    public R audit(@ApiParam(value = "消息id数组", required = true)  @RequestParam(value = "ids", defaultValue = "", required = false) Long[] ids) {
        for(int i=0; i< ids.length;i++){
            NoticeAssocDO noticeAssocDO = noticeAssocService.get(ids[i]);
            if(noticeAssocDO.getSignStatus()==null || noticeAssocDO.getSignStatus()==0){
                noticeAssocDO.setSignStatus(1);
                noticeAssocService.update(noticeAssocDO);
            }
        }
        return R.ok();
    }
}
