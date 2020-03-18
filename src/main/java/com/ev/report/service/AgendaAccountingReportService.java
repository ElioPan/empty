package com.ev.report.service;

import com.ev.report.vo.CommonVO;
import com.ev.framework.utils.R;

/**
 * 日程管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface AgendaAccountingReportService {

    R execute(CommonVO commonVO);

    R overtime(CommonVO commonVO);

    R overtimeItem(CommonVO commonVO);

    R leaveGroup(CommonVO commonVO);

    R leaveTypeGroup(CommonVO commonVO);

    R leave(CommonVO commonVO, Long typeId);

    R applyForReimbursement(CommonVO commonVO, Long typeId);

    R applyForReimbursementGroup(CommonVO commonVO);

    R applyForReimbursementTypeGroup(CommonVO commonVO);

    R payment(CommonVO commonVO);

    R paymentItem(CommonVO commonVO);
}
