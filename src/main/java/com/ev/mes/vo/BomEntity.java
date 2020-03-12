package com.ev.mes.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * BOM导入
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-11 14:19:56
 */
@Data
public class BomEntity {

    @Excel(name = "BOM代码")
    private String serialno;
    @Excel(name = "BOM名称")
    private String name;
    @Excel(name = "产品代码")
    private String productCode;
    @Excel(name = "产品名称")
    private String productName;
    @Excel(name = "规格型号")
    private String productSpecification;
    @Excel(name = "单位")
    private String productUnitUom;
    @Excel(name = "数量")
    private String productCount;
    @Excel(name = "版本号")
    private String version;
    @Excel(name = "物料代码")
    private String materielCode;
    @Excel(name = "物料名称")
    private String materielName;
    @Excel(name = "型号")
    private String materielSpecification;
    @Excel(name = "计量单位")
    private String materielUnitUom;
    @Excel(name = "是否关键件")
    private String isKeyComponents;
    @Excel(name = "标准数量")
    private String materielCount;
    @Excel(name = "损耗率")
    private String wasteRate;
    @Excel(name = "发料仓库")
    private String defaultFacility;
    @Excel(name = "发料仓位")
    private String defaultLocation;


}
