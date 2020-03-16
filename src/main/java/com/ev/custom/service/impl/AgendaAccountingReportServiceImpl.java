package com.ev.custom.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.dao.AgendaAccountingReportDao;
import com.ev.custom.service.AgendaAccountingReportService;
import com.ev.custom.vo.AgendaVO;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DateUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgendaAccountingReportServiceImpl implements AgendaAccountingReportService {
    @Autowired
    private AgendaAccountingReportDao reportDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public R execute(AgendaVO agendaVO) {
        String startTime = agendaVO.getStartTime();
        String endTime = agendaVO.getEndTime();
        int pageNo = agendaVO.getPageno();
        int pageSize = agendaVO.getPagesize();
        if (startTime == null || endTime == null) {
            return R.error(messageSourceHandler.getMessage("param.time.isEmpty", null));
        }
        Date dateStart = DateFormatUtil.getDateByParttern(startTime, "yyyy-MM-dd");
        Date dateEnd = DateFormatUtil.getDateByParttern(endTime, "yyyy-MM-dd");
        if (dateEnd == null || dateStart == null) {
            return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
        }
        Calendar d1 = Calendar.getInstance();
        d1.setTime(dateStart);
        d1.set(Calendar.DAY_OF_MONTH, d1.get(Calendar.DAY_OF_MONTH) - 1);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(dateEnd);
        // 日志应填天数
        int workingNum = DateUtils.getWorkingDay(d1, d2);
        // 周报应填天数
        int weekNum = DateUtils.getWeekNum(d1, d2);
        d1.set(Calendar.DAY_OF_MONTH, d1.get(Calendar.DAY_OF_MONTH) + 1);
        // 月报应填天数
        int monthNum = DateUtils.getMonthNum(d1, d2);
        // 获取用户 用户的部门信息
        Long userIdParam = agendaVO.getUserId();
        Long deptIdParam = agendaVO.getDeptId();
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", 1);
        params.put("userId", userIdParam);
        params.put("deptId", deptIdParam);
        List<Map<String, Object>> userDOs = userService.listForMap(params);
        if (userDOs.size() == 0) {
            return R.ok();
        }
        List<Map<String, Object>> userDOsList = PageUtils.startPage(userDOs, pageNo, pageSize);

        List<Object> userIds = userDOsList.stream()
                .map(stringObjectMap -> stringObjectMap.get("userId"))
                .collect(Collectors.toList());
        // 日志未填
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userIds);
        param.put("status", Constant.APPLY_APPROED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("dailyCount", workingNum);
        param.put("weekCount", weekNum);
        param.put("monthCount", monthNum);
        // 日报未填
        List<Map<String, Object>> dailyReport = reportDao.executeForDailyReport(param);
        Map<String, Object> dailyReportCollect = Maps.newHashMap();
        if (dailyReport.size() > 0) {
            dailyReportCollect = dailyReport.stream()
                    .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> stringObjectMap.get("dailyCount")));
        }
        // 周报未填
        List<Map<String, Object>> weekReport = reportDao.executeForWeekReport(param);
        Map<String, Object> weekReportCollect = Maps.newHashMap();
        if (weekReport.size() > 0) {
            weekReportCollect = weekReport.stream()
                    .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> stringObjectMap.get("weekCount")));
        }
        // 月报未填
        List<Map<String, Object>> monthReport = reportDao.executeForMonthReport(param);
        Map<String, Object> monthReportCollect = Maps.newHashMap();
        if (monthReport.size() > 0) {
            monthReportCollect = monthReport.stream()
                    .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> stringObjectMap.get("monthCount")));
        }
        for (Map<String, Object> userDO : userDOsList) {
            String userId = userDO.get("userId").toString();
            Object dailyCount = dailyReportCollect.get(userId);
            Object weekCount = weekReportCollect.get(userId);
            Object monthCount = monthReportCollect.get(userId);
            userDO.put("dailyCount", dailyCount == null ? workingNum : dailyCount);
            userDO.put("weekCount", weekCount == null ? weekNum : weekCount);
            userDO.put("monthCount", monthCount == null ? monthNum : monthCount);
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", new DsResultResponse(pageNo, pageSize, userDOs.size(), userDOsList));
        return R.ok(results);
    }

    @Override
    public R overtime(AgendaVO agendaVO) {
        return null;
    }

    @Override
    public R leave(AgendaVO agendaVO) {
        return null;
    }

    @Override
    public R applyForReimbursement(AgendaVO agendaVO) {
        return null;
    }

    @Override
    public R payment(AgendaVO agendaVO) {
        return null;
    }
}
