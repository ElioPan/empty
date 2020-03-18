package com.ev.report.service;

import com.ev.report.vo.AgendaVO;
import com.ev.framework.utils.R;

/**
 * 日程管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface AgendaAccountingReportService {

    R execute(AgendaVO agendaVO);

    R overtime(AgendaVO agendaVO);

    R overtimeItem(AgendaVO agendaVO);

    R leaveGroup(AgendaVO agendaVO);

    R leaveTypeGroup(AgendaVO agendaVO);

    R leave(AgendaVO agendaVO, Long typeId);

    R applyForReimbursement(AgendaVO agendaVO, Long typeId);

    R applyForReimbursementGroup(AgendaVO agendaVO);

    R applyForReimbursementTypeGroup(AgendaVO agendaVO);

    R payment(AgendaVO agendaVO);

    R paymentItem(AgendaVO agendaVO);
}
