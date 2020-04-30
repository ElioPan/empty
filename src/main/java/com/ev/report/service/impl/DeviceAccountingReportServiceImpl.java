package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.report.dao.DeviceAccountingReportDao;
import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.report.service.DeviceAccountingReportService;
import com.ev.report.vo.DeviceVO;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
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
    public Pair<DsResultResponse,Map<String,Object>> analysis(DeviceVO deviceVO) {
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
        param.put("status", ConstantForDevice.STATE_STOP_OVER);
        List<PatrolRecordDO> patrolRecordList = reportDao.patrolList(param);

        // 计算停机时长
        // 维修
        Map<Long, BigDecimal> repairOffHourMap = repairRecordList
                .stream()
                .collect(Collectors.toMap(RepairRecordDO::getDeviceId, e->BigDecimal.valueOf(e.getOffHour()), BigDecimal::add));
        // 保养
        Map<Long, BigDecimal> upkeepOffHourMap = upkeepRecordList
                .stream()
                .collect(Collectors.toMap(UpkeepRecordDO::getDeviceId, e->BigDecimal.valueOf(e.getDownHour()), BigDecimal::add));
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
                .filter(recordDO -> !ConstantForDevice.WAITING_CHECK.equals(recordDO.getStatus()))
                .collect(Collectors.groupingBy(RepairRecordDO::getDeviceId, Collectors.counting()));

        // 故障已处理未验收次数（维修记录状态 待验收）
        Map<Long, Long> recordWaitingCheckMap = repairRecordList
                .stream()
                .filter(recordDO -> ConstantForDevice.WAITING_CHECK.equals(recordDO.getStatus()))
                .collect(Collectors.groupingBy(RepairRecordDO::getDeviceId, Collectors.counting()));

        // 累计维修费用(维修成本和)
        Map<Long, BigDecimal> repairCostMap = repairRecordList
                .stream()
                .collect(Collectors.toMap(RepairRecordDO::getDeviceId, RepairRecordDO::getCost, BigDecimal::add));

        // 累计保养费用(保养工时费+保养材料费合计)
        Map<Long, BigDecimal> upkeepCostMap = upkeepRecordList
                .stream()
                .collect(Collectors.toMap(UpkeepRecordDO::getDeviceId, upkeepRecordDO -> upkeepRecordDO.getCost().add(upkeepRecordDO.getManHourCost()), BigDecimal::add));

        BigDecimal offHourForTotal = BigDecimal.ZERO;
        BigDecimal useTimeHourForTotal = BigDecimal.ZERO;
        long faultFrequencyForTotal = 0L;
        long alreadyCheckFrequencyForTotal = 0L;
        long waitingCheckFrequencyTotal = 0L;
        long unprocessedFrequencyForTotal = 0L;
        BigDecimal repairCostForTotal = BigDecimal.ZERO;
        BigDecimal upkeepCostForTotal = BigDecimal.ZERO;
        long patrolFrequencyForTotal = 0L;

        for (Map<String, Object> map : deviceList) {
            Long id = Long.parseLong(map.get("id").toString());
            // 停机时长 计算公式：维修停机时长+保养停机时长
            BigDecimal totalOffHour = repairOffHourMap.getOrDefault(id, BigDecimal.ZERO).add(upkeepOffHourMap.getOrDefault(id, BigDecimal.ZERO));
            map.put("totalOffHour", totalOffHour);
            offHourForTotal = offHourForTotal.add(totalOffHour);

            // 运行时长  计算公式：运行时长=当前日期-启用时间-停机时长
            Object usingTime = map.get("usingTime");
            BigDecimal useTimeHour = BigDecimal.ZERO;
            if (usingTime != null) {
                useTimeHour = DatesUtil.dateHour(Objects.requireNonNull(DateFormatUtil.getDateByParttern(usingTime.toString())),now).subtract(totalOffHour) ;
            }
            map.put("useTimeHour", useTimeHour.compareTo(BigDecimal.ZERO)<=0? 0 : StringUtils.formatDouble(useTimeHour.doubleValue()));
            useTimeHourForTotal = useTimeHourForTotal.add(useTimeHour);

            // 累计故障次数
            Long faultFrequency = faultFrequencyMap.getOrDefault(id, 0L);
            map.put("faultFrequency", faultFrequency);
            faultFrequencyForTotal = faultFrequencyForTotal+faultFrequency;

            // 故障已处理验收次数
            Long alreadyCheckFrequency = recordAlreadyCheckMap.getOrDefault(id, 0L);
            map.put("alreadyCheckFrequency", alreadyCheckFrequency);
            alreadyCheckFrequencyForTotal = alreadyCheckFrequencyForTotal+alreadyCheckFrequency;

            // 故障已处理未验收次数
            Long waitingCheckFrequency = recordWaitingCheckMap.getOrDefault(id, 0L);
            map.put("waitingCheckFrequency", waitingCheckFrequency);
            waitingCheckFrequencyTotal = waitingCheckFrequencyTotal+waitingCheckFrequency;

            // 故障未处理次数
            Long unprocessedFrequency = eventUnprocessedFrequencyMap.getOrDefault(id, 0L);
            map.put("unprocessedFrequency", unprocessedFrequency);
            unprocessedFrequencyForTotal = unprocessedFrequencyForTotal+unprocessedFrequency;

            // 累计维修费用
            BigDecimal repairCost = repairCostMap.getOrDefault(id, BigDecimal.ZERO);
            map.put("repairCost", repairCost.stripTrailingZeros().toPlainString());
            repairCostForTotal = repairCostForTotal.add(repairCost);

            // 累计保养费用
            BigDecimal upkeepCost = upkeepCostMap.getOrDefault(id, BigDecimal.ZERO);
            map.put("upkeepCost", upkeepCost.stripTrailingZeros().toPlainString());
            upkeepCostForTotal = upkeepCostForTotal.add(upkeepCost);

            // 巡检次数
            Long patrolFrequency = patrolFrequencyMap.getOrDefault(id, 0L);
            map.put("patrolFrequency", patrolFrequency);
            patrolFrequencyForTotal = patrolFrequencyForTotal + patrolFrequency;

        }
        Map<String,Object> total = Maps.newHashMap();
        total.put("totalOffHour",offHourForTotal);
        total.put("useTimeHour",useTimeHourForTotal);
        total.put("faultFrequency",faultFrequencyForTotal);
        total.put("alreadyCheckFrequency",alreadyCheckFrequencyForTotal);
        total.put("waitingCheckFrequency",waitingCheckFrequencyTotal);
        total.put("unprocessedFrequency",unprocessedFrequencyForTotal);
        total.put("repairCost",repairCostForTotal);
        total.put("upkeepCost",upkeepCostForTotal);
        total.put("patrolFrequency",patrolFrequencyForTotal);

        return Pair.of(new DsResultResponse(pageNo, pageSize, deviceDOList.size(), deviceList),total);
    }
}
