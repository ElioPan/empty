package com.ev.report.service;

import com.ev.framework.utils.R;
import com.ev.report.vo.DeviceVO;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;

import java.util.List;
import java.util.Map;

/**
 * 设备管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface WarehouseAccountingReportService {

    List<StockOutItemVO> stockOutItem(Map<String, Object> params);

    List<StockInItemVO> stockInItem(Map<String, Object> params);

    List<InOutStockItemVO> inOutStockItem(Map<String, Object> params);
}
