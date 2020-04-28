package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 物料
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-08 14:19:56
 */
@Data
@ApiModel(value = "物料")
public class MaterielDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//编号
    @ApiModelProperty(value = "编号")
	private String serialNo;
	//名称
    @ApiModelProperty(value = "名称",required = true)
	private String name;
	//规格型号
    @ApiModelProperty(value = "规格型号")
	private String specification;
	//图号
    @ApiModelProperty(value = "图号")
	private String imageNo;
	//物料类别
    @ApiModelProperty(value = "物料类别",required = true)
	private Long type;
	//物料属性
    @ApiModelProperty(value = "物料属性38,39",required = true)
	private Long attribute;
	//计量单位
    @ApiModelProperty(value = "计量单位117-125",required = true)
	private Long unitUom;
	//默认仓库
    @ApiModelProperty(value = "默认仓库",required = true)
	private Long defaultFacility;
	//默认库位
    @ApiModelProperty(value = "默认库位",required = true)
	private Long defaultLocation;
	//辅助单位
    @ApiModelProperty(value = "辅助单位",hidden = true)
	private Long supportUom;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Long supplier;
	//工艺路线
    @ApiModelProperty(value = "工艺路线 141-142")
	private Long tecRoute;
	//工艺版本号
    @ApiModelProperty(value = "工艺版本号")
	private String tecVersion;
	//保质期
    @ApiModelProperty(value = "保质期")
	private Integer expireDays;
	//计价方法
    @ApiModelProperty(value = "计价方法 143-144",required = true)
	private Long valuationMethod;
	//是否生效
    @ApiModelProperty(value = "是否生效")
	private Integer isValid;
	//是否进行序列号管理
    @ApiModelProperty(value = "是否进行序列号管理")
	private Integer isSerial;
	//是否进行保质期管理
    @ApiModelProperty(value = "是否进行保质期管理")
	private Integer isExpire;
	//是否进行批次管理
    @ApiModelProperty(value = "是否进行批次管理")
	private Integer isLot;
	//是否关键件
    @ApiModelProperty(value = "是否关键件",hidden = true)
	private Integer isKeyComponents;
	//是否检验
    @ApiModelProperty(value = "是否检验")
    private Integer isCheck;
    //是否库存预警
    @ApiModelProperty(value = "是否库存预警")
    private Integer isStockWarning;
    //检验方案
    @ApiModelProperty(value = "检验方案")
	private Long  inspectionScheme;
	//长度单位
    @ApiModelProperty(value = "长度单位",hidden = true)
	private Long lengthUom;
	//长度
    @ApiModelProperty(value = "长度",hidden = true)
	private BigDecimal length;
	//宽度
    @ApiModelProperty(value = "宽度",hidden = true)
	private BigDecimal width;
	//高度
    @ApiModelProperty(value = "高度",hidden = true)
	private BigDecimal height;
	//重量单位
    @ApiModelProperty(value = "重量单位",hidden = true)
	private Long weightUom;
	//毛重
    @ApiModelProperty(value = "毛重",hidden = true)
	private BigDecimal grossWeight;
	//净重
    @ApiModelProperty(value = "净重",hidden = true)
	private BigDecimal netWeight;
	//体积
    @ApiModelProperty(value = "体积",hidden = true)
	private BigDecimal volume;
	//安全库存
    @ApiModelProperty(value = "安全库存")
	private BigDecimal secInvenCount;
	//最高库存
    @ApiModelProperty(value = "最高库存")
	private BigDecimal maxInvenCount;
	//最低库存
    @ApiModelProperty(value = "最低库存")
	private BigDecimal minInvenCount;
	//税率
    @ApiModelProperty(value = "税率")
	private BigDecimal taxPercent;
	//采购成本
    @ApiModelProperty(value = "采购成本",hidden = true)
	private BigDecimal purchaseCost;
	//销售价格
    @ApiModelProperty(value = "销售价格",hidden = true)
	private BigDecimal salePrice;
	//数量精度
    @ApiModelProperty(value = "数量精度",hidden = true)
	private BigDecimal quantityAccuracy;
	//单价精度
    @ApiModelProperty(value = "单价精度",hidden = true)
	private BigDecimal priceAccuracy;
    // 条形码
    @ApiModelProperty(value = "条形码")
    private String barcode;
    // 审核人员
    @ApiModelProperty(value = "审核人员")
    private Long auditor;
    // 审核标志：（待审核/已审核）
    @ApiModelProperty(value = "审核标志：（待审核/已审核）", hidden = true)
    private Long auditSign;
    // 启用状态
    @ApiModelProperty(value = "启用状态(是否启用 1启用，0禁用)")
    private Integer useStatus;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//更新人
    @ApiModelProperty(value = "更新人",hidden = true)
	private Long updateBy;
	//更新时间
    @ApiModelProperty(value = "更新时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;
	//
    @ApiModelProperty(hidden = true)
	private Long providerId;

}
