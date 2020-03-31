package com.ev.report.dao;

import com.ev.report.vo.InOutStockItemVO;
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
public interface WarehouseAccountingReportDao {

    List<InOutStockItemVO> inOutStockItem(Map<String, Object> params);
}
