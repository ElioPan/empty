package com.ev.report.service;

import com.ev.apis.model.DsResultResponse;
import com.ev.report.vo.DeviceVO;
import com.ev.framework.utils.R;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * 设备管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface DeviceAccountingReportService {
    Pair<DsResultResponse, Map<String,Object>> analysis(DeviceVO deviceVO);
}
