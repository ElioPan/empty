package com.ev.apis.controller.custom;

import com.ev.custom.service.AgendaAccountingReportService;
import com.ev.custom.vo.AgendaVO;
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
    public R execute(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.execute(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/overtime", method = RequestMethod.POST, apiTitle = "加班统计分析")
    @ApiOperation("加班统计分析")
    public R overtime(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.overtime(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/leave", method = RequestMethod.POST, apiTitle = "请假统计分析")
    @ApiOperation("请假统计分析")
    public R leave(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.leave(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/applyForReimbursement", method = RequestMethod.POST, apiTitle = "报销统计分析")
    @ApiOperation("报销统计分析")
    public R applyForReimbursement(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.applyForReimbursement(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/payment", method = RequestMethod.POST, apiTitle = "付款统计分析")
    @ApiOperation("付款统计分析")
    public R payment(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.payment(agendaVO);
    }
}
