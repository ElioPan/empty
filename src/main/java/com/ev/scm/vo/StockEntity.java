package com.ev.scm.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;


/**
 * 期初库存导入
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:19:56
 */
@Data
public class StockEntity {

    @Excel(name = "物料编码")
    private String serialno;
    @Excel(name = "物料名称")
    private String productName;
    @Excel(name = "规格型号")
    private String model;
    @Excel(name = "计量单位")
    private String unitName;
    @Excel(name = "批号")
    private String batch;
    @Excel(name = "仓库")
    private String facilityName;
    @Excel(name = "仓位")
    private String facilityLocationName;
    @Excel(name = "期初数量")
    private String totalCount;
    @Excel(name = "期初金额")
    private String amount;

}
