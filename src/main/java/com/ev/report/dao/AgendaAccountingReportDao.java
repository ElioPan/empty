package com.ev.report.dao;

import com.ev.report.vo.AgendaAccountingReportVO;
import com.ev.report.vo.UserForReportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 日程管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
@Mapper
public interface AgendaAccountingReportDao {

    List<UserForReportVO> executeForDailyReport(Map<String, Object> param);

    List<UserForReportVO> executeForWeekReport(Map<String, Object> param);

    List<UserForReportVO> executeForMonthReport(Map<String, Object> param);

    Double overtimeForItemTotal(Map<String, Object> param);

    List<Map<String, Object>> overtimeForItem(Map<String, Object> param);

    List<Map<String, Object>> leaveItem(Map<String, Object> param);

    Double leaveItemTotal(Map<String, Object> param);

    List<Map<String, Object>> leaveForItemGroupType(Map<String, Object> param);

    List<Map<String, Object>> applyForReimbursementItem(Map<String, Object> param);

    Double applyForReimbursementItemTotal(Map<String, Object> param);

    List<Map<String, Object>> applyForReimbursementGroupType(Map<String, Object> param);

    List<Map<String, Object>> paymentForItem(Map<String, Object> param);

    Double paymentItemTotal(Map<String, Object> param);

    List<UserForReportVO> userList(Map<String, Object> params);

    List<AgendaAccountingReportVO> leaveItemList(Map<String, Object> param);
}
