package com.ev.custom.service;

import com.ev.custom.vo.AgendaVO;
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

    R leave(AgendaVO agendaVO);

    R applyForReimbursement(AgendaVO agendaVO);

    R payment(AgendaVO agendaVO);
}
