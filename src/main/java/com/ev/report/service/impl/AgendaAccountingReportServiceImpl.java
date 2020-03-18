package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DateUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.report.dao.AgendaAccountingReportDao;
import com.ev.report.service.AgendaAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Triple;
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
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    private Triple<List<Map<String, Object>>, List<Object>, Integer> getUserList(CommonVO commonVO) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", 1);
        params.put("userId", commonVO.getUserId());
        params.put("deptId", commonVO.getDeptId());
        List<Map<String, Object>> userDOs = reportDao.userList(params);
        if (userDOs.size() == 0) {
            return null;
        }
        List<Map<String, Object>> userDOsList = PageUtils.startPage(userDOs, commonVO.getPageno(), commonVO.getPagesize());
        List<Object> userIds = userDOsList.stream()
                .map(stringObjectMap -> stringObjectMap.get("userId"))
                .collect(Collectors.toList());
        return Triple.of(userDOsList, userIds, userDOs.size());
    }

    @Override
    public R execute(CommonVO commonVO) {
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
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
        Triple<List<Map<String, Object>>, List<Object>, Integer> userList = getUserList(commonVO);
        if (userList == null) {
            return R.ok();
        }
        // 日志未填
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userList.getMiddle());
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
        String timeLimit = startTime + "至" + endTime;
        List<Map<String, Object>> userDOsList = userList.getLeft();
        for (Map<String, Object> userDO : userDOsList) {
            String userId = userDO.get("userId").toString();
            Object dailyCount = dailyReportCollect.get(userId);
            Object weekCount = weekReportCollect.get(userId);
            Object monthCount = monthReportCollect.get(userId);
            userDO.put("dailyCount", dailyCount == null ? workingNum : dailyCount);
            userDO.put("weekCount", weekCount == null ? weekNum : weekCount);
            userDO.put("monthCount", monthCount == null ? monthNum : monthCount);
            userDO.put("timeLimit", timeLimit);
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), userList.getRight(), userDOsList));
        return R.ok(results);
    }

    @Override
    public R overtime(CommonVO commonVO) {
        Triple<List<Map<String, Object>>, List<Object>, Integer> userList = getUserList(commonVO);
        if (userList == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userList.getMiddle());
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> overtimeForItem = reportDao.overtimeForItem(param);
        Double total = reportDao.overtimeForItemTotal(param);
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Double> overTimeGroup =  overtimeForItem
                .stream()
                .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("overTimeUser").toString(), stringObjectMap -> Double.parseDouble(stringObjectMap.get("timeArea").toString()), Double::sum));
        List<Map<String, Object>> userDOsList = userList.getLeft();
        for (Map<String, Object> map : userDOsList) {
            String userId = map.get("userId").toString();
            Double timeArea = overTimeGroup.get(userId);
            map.put("userName", map.get("userName") + "小计");
            map.put("totalTimeArea", timeArea == null ? 0 : timeArea);
        }
        results.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), userList.getRight(), userDOsList));
//        results.put("dataItem", overtimeForItem);
        results.put("total", total==null?0:total);
        return R.ok(results);
    }

    @Override
    public R overtimeItem(CommonVO commonVO) {
        Long userId = commonVO.getUserId();
        if (userId == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> overtimeForItem = reportDao.overtimeForItem(param);
        Map<String,Object> result = Maps.newHashMap();
        if (overtimeForItem.size() > 0) {
            List<Map<String, Object>> overtimeForItemList = PageUtils.startPage(overtimeForItem, commonVO.getPageno(), commonVO.getPagesize());
            result.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), overtimeForItem.size(), overtimeForItemList));

        }
        return R.ok(result);
    }

    @Override
    public R leaveGroup(CommonVO commonVO) {
        Triple<List<Map<String, Object>>, List<Object>, Integer> userList = getUserList(commonVO);
        if (userList == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userList.getMiddle());
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> leaveForItem = reportDao.leaveItem(param);
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Double> leaveGroup = Maps.newHashMap();
        if (leaveForItem.size() > 0) {
            leaveGroup = leaveForItem
                    .stream()
                    .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> Double.parseDouble(stringObjectMap.get("timeArea").toString()), Double::sum));
        }
        List<Map<String, Object>> userDOsList = userList.getLeft();
        Double total = reportDao.leaveItemTotal(param);
        for (Map<String, Object> map : userDOsList) {
            String userId = map.get("userId").toString();
            Double timeArea = leaveGroup.get(userId);
            map.put("userName", map.get("userName") + "小计");
            map.put("totalTimeArea", timeArea == null ? 0 : timeArea);
        }
        results.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), userList.getRight(), userDOsList));
        results.put("total", total == null ? 0 : total);
        return R.ok(results);
    }

    @Override
    public R leaveTypeGroup(CommonVO commonVO) {
        Long userId = commonVO.getUserId();
        if (userId == null) {
            return R.ok();
        }
        Map<String,Object> result = Maps.newHashMap();
        List<DictionaryDO> dictionaryDOS = dictionaryService.listByType(Constant.LEAVE_APPLY_TYPE);
        Map<String, Object> param = Maps.newHashMap();
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", commonVO.getStartTime());
        param.put("endTime", commonVO.getEndTime());
        param.put("createBy", userId);
        List<Map<String, Object>> leaveForItemGroupType = reportDao.leaveForItemGroupType(param);
        List<String> collect = dictionaryDOS
                .stream()
                .map(dictionaryDO -> dictionaryDO.getName() + "小计")
                .collect(Collectors.toList());
        if (leaveForItemGroupType.size() != collect.size()) {
            List<String> typeName = leaveForItemGroupType
                    .stream()
                    .map(stringObjectMap -> stringObjectMap.get("typeName").toString())
                    .collect(Collectors.toList());
            collect.removeAll(typeName);
            Map<String, Object> map;
            for (String s : collect) {
                map = Maps.newHashMap();
                map.put("userId", userId);
                map.put("typeName", s);
                map.put("typeTotalTimeArea", 0);
                leaveForItemGroupType.add(map);
            }
        }
        result.put("data",leaveForItemGroupType);
        return R.ok(result);
    }

    @Override
    public R leave(CommonVO commonVO, Long typeId) {
        Long userId = commonVO.getUserId();
        if (userId == null || typeId == null) {
            return R.ok();
        }
        Map<String,Object> result = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("createBy", userId);
        param.put("type", typeId);
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> leaveForItem = reportDao.leaveItem(param);
        if (leaveForItem.size() > 0) {
            List<Map<String, Object>> leaveForItemList = PageUtils.startPage(leaveForItem, commonVO.getPageno(), commonVO.getPagesize());
            result.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), leaveForItem.size(), leaveForItemList));
        }
        return R.ok(result);
    }

    @Override
    public R applyForReimbursement(CommonVO commonVO, Long typeId) {
        Long userId = commonVO.getUserId();
        if (userId == null || typeId == null) {
            return R.ok();
        }
        Map<String,Object> result = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("createBy", userId);
        param.put("type", typeId);
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> applyForReimbursementItem = reportDao.applyForReimbursementItem(param);
        if (applyForReimbursementItem.size() > 0) {
            List<Map<String, Object>> applyForReimbursementItemList = PageUtils.startPage(applyForReimbursementItem, commonVO.getPageno(), commonVO.getPagesize());
            result.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), applyForReimbursementItem.size(), applyForReimbursementItemList));
        }
        return R.ok(result);
    }

    @Override
    public R applyForReimbursementGroup(CommonVO commonVO) {
        Triple<List<Map<String, Object>>, List<Object>, Integer> userList = getUserList(commonVO);
        if (userList == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userList.getMiddle());
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> applyForReimbursementItem = reportDao.applyForReimbursementItem(param);
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Double> leaveGroup = Maps.newHashMap();
        if (applyForReimbursementItem.size() > 0) {
            leaveGroup = applyForReimbursementItem
                    .stream()
                    .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> Double.parseDouble(stringObjectMap.get("reiCount").toString()), Double::sum));
        }
        List<Map<String, Object>> userDOsList = userList.getLeft();
        Double total = reportDao.leaveItemTotal(param);
        for (Map<String, Object> map : userDOsList) {
            String userId = map.get("userId").toString();
            Double totalReiCount = leaveGroup.get(userId);
            map.put("userName", map.get("userName") + "小计");
            map.put("totalReiCount", totalReiCount == null ? 0 : totalReiCount);
        }
        results.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), userList.getRight(), userDOsList));
        results.put("total", total == null ? 0 : total);
        return R.ok(results);
    }

    @Override
    public R applyForReimbursementTypeGroup(CommonVO commonVO) {
        Long userId = commonVO.getUserId();
        if (userId == null) {
            return R.ok();
        }
        Map<String,Object> result = Maps.newHashMap();
        List<DictionaryDO> dictionaryDOS = dictionaryService.listByType(Constant.REIM_APPLY_TYPE);
        Map<String, Object> param = Maps.newHashMap();
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", commonVO.getStartTime());
        param.put("endTime", commonVO.getEndTime());
        param.put("createBy", userId);
        List<Map<String, Object>> applyForReimbursementGroupType = reportDao.applyForReimbursementGroupType(param);
        List<String> collect = dictionaryDOS
                .stream()
                .map(dictionaryDO -> dictionaryDO.getName() + "小计")
                .collect(Collectors.toList());
        if (applyForReimbursementGroupType.size() != collect.size()) {
            List<String> typeName = applyForReimbursementGroupType
                    .stream()
                    .map(stringObjectMap -> stringObjectMap.get("typeName").toString())
                    .collect(Collectors.toList());
            collect.removeAll(typeName);
            Map<String, Object> map;
            for (String s : collect) {
                map = Maps.newHashMap();
                map.put("userId", userId);
                map.put("typeName", s);
                map.put("typeTotalReiCount", 0);
                applyForReimbursementGroupType.add(map);
            }
        }
        result.put("data",applyForReimbursementGroupType);
        return R.ok(result);
    }

    @Override
    public R payment(CommonVO commonVO) {
        Triple<List<Map<String, Object>>, List<Object>, Integer> userList = getUserList(commonVO);
        if (userList == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userList.getMiddle());
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> paymentForItem = reportDao.paymentForItem(param);
        Double total = reportDao.overtimeForItemTotal(param);
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Double> paymentGroup = paymentForItem
                .stream()
                .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> Double.parseDouble(stringObjectMap.get("totalNumber").toString()), Double::sum));
        List<Map<String, Object>> userDOsList = userList.getLeft();
        for (Map<String, Object> map : userDOsList) {
            String userId = map.get("userId").toString();
            Double totalNumber = paymentGroup.get(userId);
            map.put("userName", map.get("userName") + "小计");
            map.put("totalNumber", totalNumber == null ? 0 : totalNumber);
        }
        results.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), userList.getRight(), userDOsList));
//        results.put("dataItem", overtimeForItem);
        results.put("total", total == null ? 0 : total);
        return R.ok(results);
    }

    @Override
    public R paymentItem(CommonVO commonVO) {
        Long userId = commonVO.getUserId();
        if (userId == null) {
            return R.ok();
        }
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("status", Constant.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        List<Map<String, Object>> overtimeForItem = reportDao.paymentForItem(param);
        Map<String,Object> result = Maps.newHashMap();
        if (overtimeForItem.size() > 0) {
            List<Map<String, Object>> overtimeForItemList = PageUtils.startPage(overtimeForItem, commonVO.getPageno(), commonVO.getPagesize());
            result.put("data", new DsResultResponse(commonVO.getPageno(), commonVO.getPagesize(), overtimeForItem.size(), overtimeForItemList));

        }
        return R.ok(result);
    }


}
