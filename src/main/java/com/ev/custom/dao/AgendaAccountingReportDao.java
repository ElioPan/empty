package com.ev.custom.dao;

import com.ev.custom.vo.AgendaVO;
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

    List<Map<String, Object>> executeForDailyReport(Map<String, Object> param);

    List<Map<String, Object>> executeForWeekReport(Map<String, Object> param);

    List<Map<String, Object>> executeForMonthReport(Map<String, Object> param);

    List<Map<String, Object>> overtime(AgendaVO agendaVO);

    List<Map<String, Object>> leave(AgendaVO agendaVO);

    List<Map<String, Object>> applyForReimbursement(AgendaVO agendaVO);

    List<Map<String, Object>> payment(AgendaVO agendaVO);
}
