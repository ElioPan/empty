package com.ev.report.service;

import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    List<Map<String, Object>> stockList(Map<String, Object> params);

    List<Map<String, Object>> pickingSummary(Map<String, Object> params);

    Pair<List<Map<String, Object>>, Map<String, Object>> inOutSummaryResult(int showPeriodTotalInt, int showMaterielTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId);

    Pair<List<InOutStockItemVO>, Map<String, Object>> inOutStockItemResult(String startTime, String endTime, Long materielType, Long materielId);

    Pair<List<StockInItemVO>, Map<String, Object>> inStockItemResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId);

    Pair<List<StockOutItemVO>, Map<String, Object>> outStockItemResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId);

    Pair<List<Map<String,Object>>, Map<String, Object>> pickingSummaryResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long deptId);

    void  processingExport(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String fileName, List<Map<String, Object>> dateList, String chineseName);

}
