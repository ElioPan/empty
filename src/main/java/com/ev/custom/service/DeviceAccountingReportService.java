package com.ev.custom.service;

import com.ev.custom.vo.DeviceVO;
import com.ev.framework.utils.R;

/**
 * 设备管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface DeviceAccountingReportService {
    R analysis(DeviceVO deviceVO);
}
