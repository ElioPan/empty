package com.ev.apis.controller.report;

import com.ev.report.service.AgendaAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日程管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-16 09:51:41
 */
@Api(value = "/", tags = "日程管理报表分析API")
@RestController
public class AgendaAccountingReportApiController {
    @Autowired
    private AgendaAccountingReportService reportService;

    @EvApiByToken(value = "/apis/agenda/execute", method = RequestMethod.POST, apiTitle = "日程执行分析")
    @ApiOperation("日程执行分析")
    public R execute(CommonVO commonVO) {
        // 查询列表数据
        return reportService.execute(commonVO);
    }

    @EvApiByToken(value = "/apis/agenda/overtimeGroup", method = RequestMethod.POST, apiTitle = "加班统计分析")
    @ApiOperation("加班统计分析")
    public R overtime(CommonVO commonVO) {
        // 查询列表数据
        return reportService.overtime(commonVO);
    }

    @EvApiByToken(value = "/apis/agenda/leaveGroup", method = RequestMethod.POST, apiTitle = "请假统计分析")
    @ApiOperation("请假统计分析")
    public R leaveGroup(CommonVO commonVO) {
        // 查询列表数据
        return reportService.leaveGroup(commonVO);
    }

    @EvApiByToken(value = "/apis/agenda/applyForReimbursement/group", method = RequestMethod.POST, apiTitle = "报销统计分析")
    @ApiOperation("报销统计分析")
    public R applyForReimbursementGroup(CommonVO commonVO) {
        // 查询列表数据
        return reportService.applyForReimbursementGroup(commonVO);
    }

    @EvApiByToken(value = "/apis/agenda/paymentGroup", method = RequestMethod.POST, apiTitle = "付款统计分析(汇总)")
    @ApiOperation("付款统计分析(汇总)")
    public R payment(CommonVO commonVO) {
        // 查询列表数据
        return reportService.payment(commonVO);
    }

}
