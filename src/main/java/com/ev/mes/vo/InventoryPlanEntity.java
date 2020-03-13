package com.ev.mes.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @Author Kuzi
 * @Date 2020-3-13 11:09
 **/
@Data
public class InventoryPlanEntity {

    private String serialno;
    @Excel(name = "物料名称")
    private String materielName;
    @Excel(name = "规格型号")
    private String specification;
    @Excel(name = "单位")
    private String unitNomName;
    @Excel(name = "批号")
    private String batch;
    @Excel(name = "仓库")
    private String warehouseName;
    @Excel(name = "仓位")
    private String warehLocationName;
    @Excel(name = "系统数量")
    private String systemCount;
    @Excel(name = "实际盘点数量")
    private String checkCount;
    @Excel(name = "盘盈盘亏数量")
    private String profitLos;
    @Excel(name = "方案子id")
    private String id;
}
