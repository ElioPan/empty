package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.report.dao.AgendaAccountingReportDao;
import com.ev.report.service.AgendaAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.ev.report.vo.UserForReportVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgendaAccountingReportServiceImpl implements AgendaAccountingReportService {
    @Autowired
    private AgendaAccountingReportDao reportDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public List<UserForReportVO> getUserList(Map<String, Object> param) {
        List<UserForReportVO> userDOs = reportDao.userList(param);
        return userDOs.size() > 0 ? userDOs : null;
    }

    /**
     * @param pageNo   页数
     * @param pageSize 条数
     * @param userId   用户ID
     * @param deptId   部门ID
     * @return left:分页后额用户信息 middle:分页后额用户ID right:所有用户的ID
     */
    @Override
    public Triple<List<UserForReportVO>, List<Long>, List<Long>> getUserInfo(int pageNo, int pageSize, Long userId, Long deptId) {
        // 获取用户 用户的部门信息
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("deptId", deptId);
        List<UserForReportVO> userList = this.getUserList(param);
        if (userList == null) {
            return null;
        }
        // 用户分页
        List<UserForReportVO> userForReportVOS = PageUtils.startPage(userList, pageNo, pageSize);
        // 获取分页用户ID组
        List<Long> userIds = userForReportVOS
                .stream()
                .map(UserForReportVO::getUserId)
                .collect(Collectors.toList());
        // 获取所有用户ID组
        List<Long> userAllIds = userList
                .stream()
                .map(UserForReportVO::getUserId)
                .collect(Collectors.toList());
        return Triple.of(userForReportVOS, userIds, userAllIds);
    }

    /**
     * 获取 总计结果
     *
     * @param userForReportVOS 分页后的用户信息
     * @param itemList         查询出的单据信息
     * @param total            合计总数
     * @param size             总条数
     * @param pageNo           第几页
     * @param pageSize         一页几条
     * @return 结果集
     */
    @Override
    public Map<String, Object> getResult(List<UserForReportVO> userForReportVOS, List<Map<String, Object>> itemList, BigDecimal total, int size, int pageNo, int pageSize) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, BigDecimal> itemGroup = itemList
                .stream()
                .collect(Collectors.toMap(stringObjectMap -> stringObjectMap.get("createBy").toString(), stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("totalCount")), BigDecimal::add));
        List<Map<String, Object>> userDOsList = Lists.newArrayList();
        Map<String, Object> map;
        for (UserForReportVO userForReportVO : userForReportVOS) {
            map = Maps.newHashMap();
            String userId = userForReportVO.getUserId().toString();
            BigDecimal totalCount = itemGroup.get(userId);
            map.put("userId", userForReportVO.getUserId());
            map.put("userName", userForReportVO.getUserName() + ConstantForReport.TOTAL_SUFFIX);
            map.put("totalCount", totalCount == null ? 0 : totalCount);
            userDOsList.add(map);
        }
        results.put("data", new DsResultResponse(pageNo, pageSize, size, userDOsList));
        results.put("total", total == null ? 0 : total);
        return results;
    }

    /**
     * 用户Type统计列表
     *
     * @param dictionaryDOS                  字典列表
     * @param applyForReimbursementGroupType 查询出的业务单据
     * @param userId                         用户ID
     * @return 结果集
     */
    @Override
    public Map<String, Object> getResult(List<DictionaryDO> dictionaryDOS, List<Map<String, Object>> applyForReimbursementGroupType, Long userId) {
        Map<String, Object> result = Maps.newHashMap();
        List<String> typeNameList = dictionaryDOS
                .stream()
                .map(dictionaryDO -> dictionaryDO.getName() + ConstantForReport.TOTAL_SUFFIX)
                .collect(Collectors.toList());
        if (applyForReimbursementGroupType.size() != typeNameList.size()) {
            List<String> typeName = applyForReimbursementGroupType
                    .stream()
                    .map(stringObjectMap -> stringObjectMap.get("typeName").toString())
                    .collect(Collectors.toList());
            typeNameList.removeAll(typeName);
            Map<String, Object> map;
            for (String s : typeNameList) {
                map = Maps.newHashMap();
                map.put("userId", userId);
                map.put("typeName", s);
                map.put("typeTotalReiCount", 0);
                applyForReimbursementGroupType.add(map);
            }
        }
        result.put("data", applyForReimbursementGroupType);
        return result;
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

        int pageNo = commonVO.getPageno();
        int pageSize = commonVO.getPagesize();
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();
        Triple<List<UserForReportVO>, List<Long>, List<Long>> userInfo = this.getUserInfo(pageNo, pageSize, userIdInfo, deptIdInfo);
        if (userInfo == null) {
            return R.ok();
        }
        List<Long> userIds = userInfo.getMiddle();
        List<UserForReportVO> userForReportVOS = userInfo.getLeft();
        List<Long> userAllIds = userInfo.getRight();

        Calendar d1 = Calendar.getInstance();
        d1.setTime(dateStart);
        d1.set(Calendar.DAY_OF_MONTH, d1.get(Calendar.DAY_OF_MONTH) - 1);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(dateEnd);
        // 日志应填天数
        int workingNum = DateUtils.getWorkingDay(d1, d2);
        d1.set(Calendar.DAY_OF_MONTH, d1.get(Calendar.DAY_OF_MONTH) + 1);
        // 周报应填天数
        int weekNum = DateUtils.getWeekNum(d1, d2);
        // 月报应填天数
        int monthNum = DateUtils.getMonthNum(d1, d2);

        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userIds);
        param.put("status", ConstantForDevice.APPLY_APPROED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("dailyCount", workingNum);
        param.put("weekCount", weekNum);
        param.put("monthCount", monthNum);
        // 日报未填
        List<UserForReportVO> dailyReport = reportDao.executeForDailyReport(param);
        Map<Long, Integer> dailyReportCollect = Maps.newHashMap();
        if (dailyReport.size() > 0) {
            dailyReportCollect = dailyReport
                    .stream()
                    .collect(Collectors.toMap(UserForReportVO::getUserId, UserForReportVO::getCount));
        }
        // 周报未填
        List<UserForReportVO> weekReport = reportDao.executeForWeekReport(param);
        Map<Long, Integer> weekReportCollect = Maps.newHashMap();
        if (weekReport.size() > 0) {
            weekReportCollect = weekReport
                    .stream()
                    .collect(Collectors.toMap(UserForReportVO::getUserId, UserForReportVO::getCount));
        }
        // 月报未填
        List<UserForReportVO> monthReport = reportDao.executeForMonthReport(param);
        Map<Long, Integer> monthReportCollect = Maps.newHashMap();
        if (monthReport.size() > 0) {
            monthReportCollect = monthReport
                    .stream()
                    .collect(Collectors.toMap(UserForReportVO::getUserId, UserForReportVO::getCount));
        }
        String timeLimit = startTime.substring(0,10) + "至" + endTime.substring(0,10);
        List<Map<String, Object>> userDOsList = Lists.newArrayList();
        Map<String, Object> map;
        for (UserForReportVO userForReportVO : userForReportVOS) {
            map = Maps.newHashMap();
            Long userId = userForReportVO.getUserId();
            map.put("userName", userForReportVO.getUserName());
            map.put("deptName", userForReportVO.getDeptName());
            Integer dailyCount = dailyReportCollect.getOrDefault(userId,workingNum);
            Integer weekCount = weekReportCollect.getOrDefault(userId,weekNum);
            Integer monthCount = monthReportCollect.getOrDefault(userId,monthNum);
            map.put("dailyCount",dailyCount);
            map.put("weekCount", weekCount);
            map.put("monthCount", monthCount);
            map.put("timeLimit", timeLimit);
            userDOsList.add(map);
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", new DsResultResponse(pageNo, pageSize, userAllIds.size(), userDOsList));
        return R.ok(results);
    }

    @Override
    public R overtime(CommonVO commonVO) {
        boolean showItem = commonVO.getShowItem() == 1;
        boolean showUser = commonVO.getShowUser() == 1;
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();

        Map<String, Object> param = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        param.put("status", ConstantForDevice.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);

        List<Map<String, Object>> showList = Lists.newArrayList();

        List<Map<String, Object>> overtimeForItem = reportDao.overtimeForItem(param);

        Map<String,Object> result = Maps.newHashMap();
        if (overtimeForItem.size() > 0) {
            // 所有用户的加班总和
            BigDecimal total = reportDao.overtimeForItemTotal(param);
            if(showUser){
                Map<String, BigDecimal> itemGroup = overtimeForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> MathUtils.getBigDecimal(v.get("totalCount").toString()), BigDecimal::add));
                Map<String, Map<String, Object>> itemMap = overtimeForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> v, (v1,v2)->v1));

                Map<String, Object> map;
                for (String userId : itemGroup.keySet()) {
                    map = Maps.newHashMap();
                    BigDecimal totalCount = itemGroup.getOrDefault(userId,BigDecimal.ZERO);
                    Map<String, Object> item = itemMap.get(userId);
                    map.put("userId",userId);
                    // 颜色标记明细为0 类型合计1, 人员合计2
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", 1);
                    map.put("name", item.get("name") + ConstantForReport.TOTAL_SUFFIX);
                    map.put("deptName", item.get("deptName"));
                    map.put("deptId", item.get("deptId"));
                    map.put("totalCount", totalCount);
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(overtimeForItem);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("userId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());
            result.put("data",collect);
            result.put("total",total);
        }
        return R.ok(result);
    }


    @Override
    public R leaveGroup(CommonVO commonVO) {
        boolean showItem = commonVO.getShowItem() == 1;
        boolean showType = commonVO.getShowType() == 1;
        boolean showUser = commonVO.getShowUser() == 1;
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();

        Map<String, Object> param = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        param.put("status", ConstantForDevice.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);

        List<Map<String, Object>> showList = Lists.newArrayList();

        List<Map<String, Object>> leaveForItem = reportDao.leaveItem(param);

        List<Map<String, Object>> leaveForItemGroupType = reportDao.leaveForItemGroupType(param);

        Map<String,Object> result = Maps.newHashMap();
        if (leaveForItem.size() > 0 ) {

            // 所查询的全部用户
            BigDecimal total = reportDao.leaveItemTotal(param);

            List<DictionaryDO> dictionaryDOS = dictionaryService.listByType(ConstantForDevice.LEAVE_APPLY_TYPE);
            Long typeMax = dictionaryDOS
                    .stream()
                    .max(Comparator.comparing(DictionaryDO::getId))
                    .orElse(new DictionaryDO())
                    .getId();
            typeMax = typeMax == null ? 0 : typeMax + 1;

            if(showUser){
                Map<String, BigDecimal> itemGroup = leaveForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> MathUtils.getBigDecimal(v.get("totalCount").toString()), BigDecimal::add));
                Map<String, Map<String, Object>> itemMap = leaveForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> v, (v1,v2)->v1));

                Map<String, Object> map;
                for (String userId : itemGroup.keySet()) {
                    map = Maps.newHashMap();
                    BigDecimal totalCount = itemGroup.getOrDefault(userId,BigDecimal.ZERO);
                    Map<String, Object> item = itemMap.get(userId);
                    map.put("userId",userId);
                    // 颜色标记明细为0 类型合计1, 人员合计2
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", typeMax);
                    map.put("name", item.get("name") + ConstantForReport.TOTAL_SUFFIX);
                    map.put("deptName", item.get("deptName"));
                    map.put("deptId", item.get("deptId"));
                    map.put("type", typeMax);
                    map.put("totalCount", totalCount);
                    showList.add(map);
                }
            }

            if (leaveForItemGroupType.size() > 0 && showType) {
                showList.addAll(leaveForItemGroupType);
            }

            if (showItem) {
                showList.addAll(leaveForItem);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("type").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("userId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());
            result.put("data",collect);
            result.put("total",total);
        }
        return R.ok(result);
    }


    @Override
    public R applyForReimbursementGroup(CommonVO commonVO) {
        boolean showItem = commonVO.getShowItem() == 1;
        boolean showType = commonVO.getShowType() == 1;
        boolean showUser = commonVO.getShowUser() == 1;
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();

        Map<String, Object> param = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        param.put("status", ConstantForDevice.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);

        List<Map<String, Object>> applyForReimbursementItem = reportDao.applyForReimbursementItem(param);

        List<Map<String, Object>> applyForReimbursementGroupType = reportDao.applyForReimbursementGroupType(param);

        List<Map<String, Object>> showList = Lists.newArrayList();

        Map<String,Object> result = Maps.newHashMap();
        if (applyForReimbursementItem.size() > 0) {

            // 所查询的全部用户
            BigDecimal total = reportDao.applyForReimbursementItemTotal(param);

            List<DictionaryDO> dictionaryDOS = dictionaryService.listByType(ConstantForDevice.REIM_APPLY_TYPE);
            Long typeMax = dictionaryDOS
                    .stream()
                    .max(Comparator.comparing(DictionaryDO::getId))
                    .orElse(new DictionaryDO())
                    .getId();
            typeMax = typeMax == null ? 0 : typeMax + 1;

            if(showUser){
                Map<String, BigDecimal> itemGroup = applyForReimbursementItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> MathUtils.getBigDecimal(v.get("totalCount").toString()), BigDecimal::add));
                Map<String, Map<String, Object>> itemMap = applyForReimbursementItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> v, (v1,v2)->v1));

                Map<String, Object> map;
                for (String userId : itemGroup.keySet()) {
                    map = Maps.newHashMap();
                    BigDecimal totalCount = itemGroup.getOrDefault(userId,BigDecimal.ZERO);
                    Map<String, Object> item = itemMap.get(userId);
                    map.put("userId",userId);
                    // 颜色标记明细为0 类型合计1, 人员合计2
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", typeMax);
                    map.put("name", item.get("name") + ConstantForReport.TOTAL_SUFFIX);
                    map.put("deptName", item.get("deptName"));
                    map.put("deptId", item.get("deptId"));
                    map.put("type", typeMax);
                    map.put("totalCount", totalCount);
                    showList.add(map);
                }
            }

            if (applyForReimbursementGroupType.size() > 0 && showType) {
                showList.addAll(applyForReimbursementGroupType);
            }

            if (showItem) {
                showList.addAll(applyForReimbursementItem);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("type").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("userId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());
            result.put("data",collect);
            result.put("total",total);
        }

        return R.ok(result);
    }

    @Override
    public R payment(CommonVO commonVO) {
        boolean showItem = commonVO.getShowItem() == 1;
        boolean showUser = commonVO.getShowUser() == 1;
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();

        Map<String, Object> param = Maps.newHashMap();
        String startTime = commonVO.getStartTime();
        String endTime = commonVO.getEndTime();
        param.put("status", ConstantForDevice.APPLY_COMPLETED);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);

        List<Map<String, Object>> showList = Lists.newArrayList();

        List<Map<String, Object>> paymentForItem = reportDao.paymentForItem(param);

        Map<String,Object> result = Maps.newHashMap();
        if (paymentForItem.size() > 0) {
            // 获取所有的用户
            BigDecimal total = reportDao.paymentItemTotal(param);
            if(showUser){
                Map<String, BigDecimal> itemGroup = paymentForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> MathUtils.getBigDecimal(v.get("totalCount").toString()), BigDecimal::add));
                Map<String, Map<String, Object>> itemMap = paymentForItem
                        .stream()
                        .collect(Collectors.toMap(k -> k.get("userId").toString(), v -> v, (v1,v2)->v1));

                Map<String, Object> map;
                for (String userId : itemGroup.keySet()) {
                    map = Maps.newHashMap();
                    BigDecimal totalCount = itemGroup.getOrDefault(userId,BigDecimal.ZERO);
                    Map<String, Object> item = itemMap.get(userId);
                    map.put("userId",userId);
                    // 颜色标记明细为0 类型合计1, 人员合计2
                    map.put("sign",ConstantForReport.COLOUR_END);
                    // 详情内为1
                    map.put("sortNo", 1);
                    map.put("name", item.get("name") + ConstantForReport.TOTAL_SUFFIX);
                    map.put("deptName", item.get("deptName"));
                    map.put("deptId", item.get("deptId"));
                    map.put("totalCount", totalCount);
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(paymentForItem);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("userId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());
            result.put("data",collect);
            result.put("total",total);
        }
        return R.ok(result);
    }

}
