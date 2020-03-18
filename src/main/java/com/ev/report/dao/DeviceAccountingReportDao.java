package com.ev.report.dao;

import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.report.vo.DeviceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设备管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-17 17:22:16
 */
@Mapper
public interface DeviceAccountingReportDao {

    List<Map<String,Object>> deviceList(DeviceVO deviceVO);

    List<RepairRecordDO> repairList(Map<String, Object> map);

    List<RepairEventDO> repairEventList(Map<String, Object> map);

    List<UpkeepRecordDO> upkeepList(Map<String, Object> map);

    List<PatrolRecordDO> patrolList(Map<String, Object> map);

}
