package com.ev.apis.controller.custom;

import com.ev.custom.service.AgendaAccountingReportService;
import com.ev.custom.vo.AgendaVO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @EvApiByToken(value = "/apis/agenda/overtimeGroup", method = RequestMethod.POST, apiTitle = "加班统计分析(汇总)")
    @ApiOperation("加班统计分析(汇总)")
    public R overtime(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.overtime(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/overtime", method = RequestMethod.POST, apiTitle = "加班统计分析（详细）")
    @ApiOperation("加班统计分析（详细）")
    public R overtimeItem(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.overtimeItem(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/leaveGroup", method = RequestMethod.POST, apiTitle = "请假统计分析（总汇总）")
    @ApiOperation("请假统计分析（总汇总）")
    public R leaveGroup(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.leaveGroup(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/leaveTypeGroup", method = RequestMethod.POST, apiTitle = "请假统计分析（类型汇总）")
    @ApiOperation("请假统计分析（类型汇总）")
    public R leaveTypeGroup(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.leaveTypeGroup(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/leave", method = RequestMethod.POST, apiTitle = "请假统计分析（详细）")
    @ApiOperation("请假统计分析（详细）")
    public R leaveItem(AgendaVO agendaVO,
                       @ApiParam(value = "请假类型") @RequestParam(value = "typeId", defaultValue = "", required = false) Long typeId) {
        // 查询列表数据
        return reportService.leave(agendaVO,typeId);
    }

    @EvApiByToken(value = "/apis/agenda/applyForReimbursement/group", method = RequestMethod.POST, apiTitle = "报销统计分析(汇总)")
    @ApiOperation("报销统计分析(汇总)")
    public R applyForReimbursementGroup(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.applyForReimbursementGroup(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/applyForReimbursementTypeGroup", method = RequestMethod.POST, apiTitle = "报销统计分析(类型汇总)")
    @ApiOperation("报销统计分析(类型汇总)")
    public R applyForReimbursementTypeGroup(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.applyForReimbursementTypeGroup(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/applyForReimbursement", method = RequestMethod.POST, apiTitle = "报销统计分析(详细)")
    @ApiOperation("报销统计分析(详细)")
    public R applyForReimbursement(AgendaVO agendaVO,
                                   @ApiParam(value = "报销类型") @RequestParam(value = "typeId", defaultValue = "", required = false) Long typeId) {
        // 查询列表数据
        return reportService.applyForReimbursement(agendaVO,typeId);
    }

    @EvApiByToken(value = "/apis/agenda/paymentGroup", method = RequestMethod.POST, apiTitle = "付款统计分析(汇总)")
    @ApiOperation("付款统计分析(汇总)")
    public R payment(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.payment(agendaVO);
    }

    @EvApiByToken(value = "/apis/agenda/payment", method = RequestMethod.POST, apiTitle = "付款统计分析(详细)")
    @ApiOperation("付款统计分析(详细)")
    public R paymentItem(AgendaVO agendaVO) {
        // 查询列表数据
        return reportService.paymentItem(agendaVO);
    }
}
