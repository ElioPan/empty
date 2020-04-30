package com.ev.report.service.impl;

import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.R;
import com.ev.report.dao.DeviceAccountingReportDao;
import com.ev.report.dao.ManageKanbanServiceDao;
import com.ev.report.service.ManageKanbanService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ManageKanbanServiceImpl implements ManageKanbanService {
    @Autowired
    private ManageKanbanServiceDao kanbanService;
    @Autowired
    private DeviceAccountingReportDao reportDao;

    @Override
    public List<Map<String, Object>> feedingList() {
        return kanbanService.feedingList();
    }

    @Override
    public List<Map<String, Object>> getProductionStatistics(Map<String, Object> param) {
        return kanbanService.getProductionStatistics(param);
    }

    @Override
    public List<Map<String, Object>> badProductList(Map<String, Object> param) {
        return kanbanService.badProductList(param);
    }

    @Override
    public R deviceTask() {
        Map<String, Object> param = Maps.newHashMap();
        // 维修事件
        List<RepairEventDO> repairEventDOS = reportDao.repairEventList(param);
        // 巡检记录
        List<PatrolRecordDO> patrolRecordDOS = reportDao.patrolList(param);
        // 保养记录
        List<UpkeepRecordDO> upkeepRecordDOS = reportDao.upkeepList(param);

        // 维修完成数量 已验收
        long eventCompleteCount = repairEventDOS
                .stream()
                .filter(e -> ConstantForDevice.ALREADY_CHECK.equals(e.getStatus()))
                .count();

        // 巡检完成数量 已填写或者关闭的
        long patrolCompleteCount = patrolRecordDOS
                .stream()
                .filter(e -> ConstantForDevice.STATE_STOP_OVER.equals(e.getStatus()) || ConstantForDevice.CLOSE.equals(e.getStatus()))
                .count();

        // 保养完成数量 已验收或者关闭的
        long upkeepCompleteCount = upkeepRecordDOS
                .stream()
                .filter(e -> ConstantForDevice.ALREADY_CHECK.equals(e.getStatus()) || ConstantForDevice.CLOSE.equals(e.getStatus()))
                .count();

        Map<String, Object> result = Maps.newHashMap();
        result.put("eventCompleteCount", eventCompleteCount);
        result.put("patrolCompleteCount", patrolCompleteCount);
        result.put("upkeepCompleteCount", upkeepCompleteCount);
        int eventCount = repairEventDOS.size();
        result.put("eventCount", eventCount);
        int patrolCount = patrolRecordDOS.size();
        result.put("patrolCount", patrolCount);
        int upkeepCount = upkeepRecordDOS.size();
        result.put("upkeepCount", upkeepCount);
        result.put("eventRate", eventCount == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(eventCompleteCount).divide(BigDecimal.valueOf(eventCount), BigDecimal.ROUND_HALF_UP, Constant.BIGDECIMAL_ZERO));
        result.put("patrolRate", patrolCount == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(patrolCompleteCount).divide(BigDecimal.valueOf(patrolCount), BigDecimal.ROUND_HALF_UP, Constant.BIGDECIMAL_ZERO));
        result.put("upkeepRate", upkeepCount == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(upkeepCompleteCount).divide(BigDecimal.valueOf(upkeepCount), BigDecimal.ROUND_HALF_UP, Constant.BIGDECIMAL_ZERO));

        return R.ok(result);
    }
}
