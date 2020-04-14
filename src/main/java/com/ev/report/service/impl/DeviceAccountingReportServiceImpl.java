package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.report.dao.DeviceAccountingReportDao;
import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.report.service.DeviceAccountingReportService;
import com.ev.report.vo.DeviceVO;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DeviceAccountingReportServiceImpl implements DeviceAccountingReportService {
    @Autowired
    private DeviceAccountingReportDao reportDao;

    @Override
    public DsResultResponse  analysis(DeviceVO deviceVO) {
        deviceVO.setNameAndCode(StringUtils.sqlLike(deviceVO.getNameAndCode()));
        List<Map<String, Object>> deviceDOList = reportDao.deviceList(deviceVO);
        if (deviceDOList.size() == 0) {
            return null;
        }
        int pageNo = deviceVO.getPageno();
        int pageSize = deviceVO.getPagesize();
        List<Map<String, Object>> deviceList = PageUtils.startPage(deviceDOList, pageNo, pageSize);
        List<Object> deviceIds = deviceList
                .stream()
                .map(stringObjectMap -> stringObjectMap.get("id"))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("deviceIds", deviceIds);
        // 维修所有记录
        List<RepairRecordDO> repairRecordList = reportDao.repairList(param);

        // 维修事件所有记录
        List<RepairEventDO> repairEventList = reportDao.repairEventList(param);

        // 保养所有记录
        List<UpkeepRecordDO> upkeepRecordList = reportDao.upkeepList(param);

        // 巡检所有记录(已完成填写的)
        param.put("status", Constant.STATE_STOP_OVER);
        List<PatrolRecordDO> patrolRecordList = reportDao.patrolList(param);

        // 计算停机时长
        // 维修
        Map<Long, Double> repairManHourMap = repairRecordList
                .stream()
                .collect(Collectors.toMap(RepairRecordDO::getDeviceId, RepairRecordDO::getManHour, Double::sum));
        // 保养
        Map<Long, Double> upkeepManHourMap = upkeepRecordList
                .stream()
                .collect(Collectors.toMap(UpkeepRecordDO::getDeviceId, UpkeepRecordDO::getManHour, Double::sum));
        Date now = new Date();

        // 计算巡检次数
        Map<Long, Long> patrolFrequencyMap = patrolRecordList
                .stream()
                .collect(Collectors.groupingBy(PatrolRecordDO::getDeviceId, Collectors.counting()));

        // 累计故障次数
        Map<Long, Long> faultFrequencyMap = repairEventList
                .stream()
                .collect(Collectors.groupingBy(RepairEventDO::getDeviceId, Collectors.counting()));

        // 故障未处理次数（若处理过则算作已处理）
        // 获取报修事件ID组
        List<Long> repairEventIds = repairEventList.stream()
                .map(RepairEventDO::getId)
                .collect(Collectors.toList());
        // 获取维修记录中关联报修事件的ID组(实际处理的维修事件)
        List<Long> repairRecordEventIds = repairRecordList.stream()
                .map(RepairRecordDO::getEventId)
                .distinct()
                .collect(Collectors.toList());
        repairEventIds.removeAll(repairRecordEventIds);
        Map<Long, Long> eventUnprocessedFrequencyMap = Maps.newHashMap();
        // 若存在为处理的维修事件
        if (repairEventIds.size() > 0) {
            eventUnprocessedFrequencyMap = repairEventList
                    .stream()
                    .filter(repairEventDO -> repairEventIds.contains(repairEventDO.getId()))
                    .collect(Collectors.groupingBy(RepairEventDO::getDeviceId, Collectors.counting()));
        }

        // 故障已处理验收次数（维修记录状态 不为待验收）
        Map<Long, Long> recordAlreadyCheckMap = repairRecordList
                .stream()
                .filter(recordDO -> !Constant.WAITING_CHECK.equals(recordDO.getStatus()))
                .collect(Collectors.groupingBy(RepairRecordDO::getDeviceId, Collectors.counting()));

        // 故障已处理未验收次数（维修记录状态 待验收）
        Map<Long, Long> recordWaitingCheckMap = repairRecordList
                .stream()
                .filter(recordDO -> Constant.WAITING_CHECK.equals(recordDO.getStatus()))
                .collect(Collectors.groupingBy(RepairRecordDO::getDeviceId, Collectors.counting()));

        // 累计维修费用(维修成本和)
        Map<Long, BigDecimal> repairCostMap = repairRecordList
                .stream()
                .collect(Collectors.toMap(RepairRecordDO::getDeviceId, RepairRecordDO::getCost, BigDecimal::add));

        // 累计保养费用(保养工时费+保养材料费合计)
        Map<Long, BigDecimal> upkeepCostMap = upkeepRecordList
                .stream()
                .collect(Collectors.toMap(UpkeepRecordDO::getDeviceId, upkeepRecordDO -> upkeepRecordDO.getCost().add(upkeepRecordDO.getManHourCost()), BigDecimal::add));


        for (Map<String, Object> map : deviceList) {
            Long id = Long.parseLong(map.get("id").toString());
            // 停机时长 计算公式：维修停机时长+保养停机时长
            double totalManHour = Double.sum(repairManHourMap.getOrDefault(id, 0.0d), upkeepManHourMap.getOrDefault(id, 0.0d));
            map.put("totalManHour", totalManHour);

            // 运行时长  计算公式：运行时长=当前日期-启用时间-停机时长
            Object usingTime = map.get("usingTime");
            double useTimeHour = 0.0d;
            if (usingTime != null) {
                useTimeHour = DatesUtil.dateHour(Objects.requireNonNull(DateFormatUtil.getDateByParttern(usingTime.toString())), now).doubleValue() - totalManHour;

            }
            map.put("useTimeHour", useTimeHour <= 0 ? 0 : String.format("%.2f", useTimeHour));

            // 累计故障次数
            Long faultFrequency = faultFrequencyMap.getOrDefault(id, 0L);
            map.put("faultFrequency", faultFrequency);

            // 故障已处理验收次数
            Long alreadyCheckFrequency = recordAlreadyCheckMap.getOrDefault(id, 0L);
            map.put("alreadyCheckFrequency", alreadyCheckFrequency);

            // 故障已处理未验收次数
            Long waitingCheckFrequency = recordWaitingCheckMap.getOrDefault(id, 0L);
            map.put("waitingCheckFrequency", waitingCheckFrequency);

            // 故障未处理次数
            Long unprocessedFrequency = eventUnprocessedFrequencyMap.getOrDefault(id, 0L);
            map.put("unprocessedFrequency", unprocessedFrequency);

            // 累计维修费用
            BigDecimal repairCost = repairCostMap.getOrDefault(id, BigDecimal.ZERO);
            map.put("repairCost", repairCost.stripTrailingZeros().toPlainString());

            // 累计保养费用
            BigDecimal upkeepCost = upkeepCostMap.getOrDefault(id, BigDecimal.ZERO);
            map.put("upkeepCost", upkeepCost.stripTrailingZeros().toPlainString());

            // 巡检次数
            Long patrolFrequency = patrolFrequencyMap.getOrDefault(id, 0L);
            map.put("patrolFrequency", patrolFrequency);

        }
        return new DsResultResponse(pageNo, pageSize, deviceDOList.size(), deviceList);
    }
}
