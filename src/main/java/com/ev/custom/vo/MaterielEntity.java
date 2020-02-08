package com.ev.custom.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 物料导出
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:19:56
 */
@Data
public class MaterielEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//编号
    @Excel(name = "物料编码")
	private String serialNo;
	//名称
    @Excel(name = "物料名称")
	private String name;
	//规格型号
    @Excel(name = "规格型号")
	private String specification;
    //物料类别
    @Excel(name = "物料类别")
    private String typeName;
    //物料属性
    @Excel(name = "物料属性")
    private String attributeName;
    //计量单位
    @Excel(name = "计量单位")
    private String unitUomName;
    //计价方法
    @Excel(name = "计价方法")
    private String valuationMethodName;
    //默认仓库
    @Excel(name = "默认仓库")
    private String defaultFacilityName;
    //默认库位
    @Excel(name = "默认仓位")
    private String defaultLocationName;
    //供应商
    @Excel(name = "供应商")
    private String supplierName;
    //安全库存
    @Excel(name = "安全库存",type = 10)
    private BigDecimal secInvenCount;
    //最高库存
    @Excel(name = "最高库存",type = 10)
    private BigDecimal maxInvenCount;
    //最低库存
    @Excel(name = "最低库存",type = 10)
    private BigDecimal minInvenCount;
    //税率
    @Excel(name = "税率",type = 10)
    private BigDecimal taxPercent;
	//图号
    @Excel(name = "图号")
	private String imageNo;
    //是否库存预警
    @Excel(name = "是否库存预警")
    private String isStockWarning;
    //是否进行批次管理
    @Excel(name = "是否批次管理")
    private String isLot;
    //是否进行保质期管理
    @Excel(name = "是否进行保质期管理")
    private String isExpire;
    //保质期
    @Excel(name = "保质期",type = 10)
    private Integer expireDays;
}
