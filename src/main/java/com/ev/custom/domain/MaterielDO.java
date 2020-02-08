package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 物料
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-08 14:19:56
 */
@ApiModel(value = "物料")
public class MaterielDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Integer id;
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
	private Integer type;
	//物料属性
    @ApiModelProperty(value = "物料属性38,39",required = true)
	private Integer attribute;
	//计量单位
    @ApiModelProperty(value = "计量单位117-125",required = true)
	private Integer unitUom;
	//默认仓库
    @ApiModelProperty(value = "默认仓库",required = true)
	private Integer defaultFacility;
	//默认库位
    @ApiModelProperty(value = "默认库位",required = true)
	private Integer defaultLocation;
	//辅助单位
    @ApiModelProperty(value = "辅助单位",hidden = true)
	private Integer supportUom;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Integer supplier;
	//工艺路线
    @ApiModelProperty(value = "工艺路线 141-142")
	private Integer tecRoute;
	//工艺版本号
    @ApiModelProperty(value = "工艺版本号")
	private String tecVersion;
	//保质期
    @ApiModelProperty(value = "保质期")
	private Integer expireDays;
	//计价方法
    @ApiModelProperty(value = "计价方法 143-144",required = true)
	private Integer valuationMethod;
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
	private Integer lengthUom;
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
	private Integer weightUom;
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
    private Integer auditSign;
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
    @ApiModelProperty(value = "",hidden = true)
	private Long providerId;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：编号
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	/**
	 * 获取：编号
	 */
	public String getSerialNo() {
		return serialNo;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：规格型号
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	/**
	 * 获取：规格型号
	 */
	public String getSpecification() {
		return specification;
	}
	/**
	 * 设置：图号
	 */
	public void setImageNo(String imageNo) {
		this.imageNo = imageNo;
	}
	/**
	 * 获取：图号
	 */
	public String getImageNo() {
		return imageNo;
	}
	/**
	 * 设置：物料类别
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：物料类别
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：物料属性
	 */
	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}
	/**
	 * 获取：物料属性
	 */
	public Integer getAttribute() {
		return attribute;
	}
	/**
	 * 设置：计量单位
	 */
	public void setUnitUom(Integer unitUom) {
		this.unitUom = unitUom;
	}
	/**
	 * 获取：计量单位
	 */
	public Integer getUnitUom() {
		return unitUom;
	}
	/**
	 * 设置：默认仓库
	 */
	public void setDefaultFacility(Integer defaultFacility) {
		this.defaultFacility = defaultFacility;
	}
	/**
	 * 获取：默认仓库
	 */
	public Integer getDefaultFacility() {
		return defaultFacility;
	}
	/**
	 * 设置：默认库位
	 */
	public void setDefaultLocation(Integer defaultLocation) {
		this.defaultLocation = defaultLocation;
	}
	/**
	 * 获取：默认库位
	 */
	public Integer getDefaultLocation() {
		return defaultLocation;
	}
	/**
	 * 设置：辅助单位
	 */
	public void setSupportUom(Integer supportUom) {
		this.supportUom = supportUom;
	}
	/**
	 * 获取：辅助单位
	 */
	public Integer getSupportUom() {
		return supportUom;
	}
	/**
	 * 设置：供应商
	 */
	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}
	/**
	 * 获取：供应商
	 */
	public Integer getSupplier() {
		return supplier;
	}
	/**
	 * 设置：工艺路线
	 */
	public void setTecRoute(Integer tecRoute) {
		this.tecRoute = tecRoute;
	}
	/**
	 * 获取：工艺路线
	 */
	public Integer getTecRoute() {
		return tecRoute;
	}
	/**
	 * 设置：工艺版本号
	 */
	public void setTecVersion(String tecVersion) {
		this.tecVersion = tecVersion;
	}
	/**
	 * 获取：工艺版本号
	 */
	public String getTecVersion() {
		return tecVersion;
	}
	/**
	 * 设置：保质期
	 */
	public void setExpireDays(Integer expireDays) {
		this.expireDays = expireDays;
	}
	/**
	 * 获取：保质期
	 */
	public Integer getExpireDays() {
		return expireDays;
	}
	/**
	 * 设置：计价方法
	 */
	public void setValuationMethod(Integer valuationMethod) {
		this.valuationMethod = valuationMethod;
	}
	/**
	 * 获取：计价方法
	 */
	public Integer getValuationMethod() {
		return valuationMethod;
	}
	/**
	 * 设置：是否生效
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	/**
	 * 获取：是否生效
	 */
	public Integer getIsValid() {
		return isValid;
	}
	/**
	 * 设置：是否进行序列号管理
	 */
	public void setIsSerial(Integer isSerial) {
		this.isSerial = isSerial;
	}
	/**
	 * 获取：是否进行序列号管理
	 */
	public Integer getIsSerial() {
		return isSerial;
	}
	/**
	 * 设置：是否进行保质期管理
	 */
	public void setIsExpire(Integer isExpire) {
		this.isExpire = isExpire;
	}
	/**
	 * 获取：是否进行保质期管理
	 */
	public Integer getIsExpire() {
		return isExpire;
	}
	/**
	 * 设置：是否进行批次管理
	 */
	public void setIsLot(Integer isLot) {
		this.isLot = isLot;
	}
	/**
	 * 获取：是否进行批次管理
	 */
	public Integer getIsLot() {
		return isLot;
	}
	/**
	 * 设置：是否关键件
	 */
	public void setIsKeyComponents(Integer isKeyComponents) {
		this.isKeyComponents = isKeyComponents;
	}
	/**
	 * 获取：是否关键件
	 */
	public Integer getIsKeyComponents() {
		return isKeyComponents;
	}
	/**
	 * 设置：是否检验
	 */
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	/**
	 * 获取：是否检验
	 */
	public Integer getIsCheck() {
		return isCheck;
	}

    public Integer getIsStockWarning() {
        return isStockWarning;
    }

    public void setIsStockWarning(Integer isStockWarning) {
        this.isStockWarning = isStockWarning;
    }

    /**
	 * 设置：长度单位
	 */
	public void setLengthUom(Integer lengthUom) {
		this.lengthUom = lengthUom;
	}
	/**
	 * 获取：长度单位
	 */
	public Integer getLengthUom() {
		return lengthUom;
	}
	/**
	 * 设置：长度
	 */
	public void setLength(BigDecimal length) {
		this.length = length;
	}
	/**
	 * 获取：长度
	 */
	public BigDecimal getLength() {
		return length;
	}
	/**
	 * 设置：宽度
	 */
	public void setWidth(BigDecimal width) {
		this.width = width;
	}
	/**
	 * 获取：宽度
	 */
	public BigDecimal getWidth() {
		return width;
	}
	/**
	 * 设置：高度
	 */
	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	/**
	 * 获取：高度
	 */
	public BigDecimal getHeight() {
		return height;
	}
	/**
	 * 设置：重量单位
	 */
	public void setWeightUom(Integer weightUom) {
		this.weightUom = weightUom;
	}
	/**
	 * 获取：重量单位
	 */
	public Integer getWeightUom() {
		return weightUom;
	}
	/**
	 * 设置：毛重
	 */
	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}
	/**
	 * 获取：毛重
	 */
	public BigDecimal getGrossWeight() {
		return grossWeight;
	}
	/**
	 * 设置：净重
	 */
	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}
	/**
	 * 获取：净重
	 */
	public BigDecimal getNetWeight() {
		return netWeight;
	}
	/**
	 * 设置：体积
	 */
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	/**
	 * 获取：体积
	 */
	public BigDecimal getVolume() {
		return volume;
	}
	/**
	 * 设置：安全库存
	 */
	public void setSecInvenCount(BigDecimal secInvenCount) {
		this.secInvenCount = secInvenCount;
	}
	/**
	 * 获取：安全库存
	 */
	public BigDecimal getSecInvenCount() {
		return secInvenCount;
	}
	/**
	 * 设置：最高库存
	 */
	public void setMaxInvenCount(BigDecimal maxInvenCount) {
		this.maxInvenCount = maxInvenCount;
	}
	/**
	 * 获取：最高库存
	 */
	public BigDecimal getMaxInvenCount() {
		return maxInvenCount;
	}
	/**
	 * 设置：最低库存
	 */
	public void setMinInvenCount(BigDecimal minInvenCount) {
		this.minInvenCount = minInvenCount;
	}
	/**
	 * 获取：最低库存
	 */
	public BigDecimal getMinInvenCount() {
		return minInvenCount;
	}
	/**
	 * 设置：税率
	 */
	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}
	/**
	 * 获取：税率
	 */
	public BigDecimal getTaxPercent() {
		return taxPercent;
	}
	/**
	 * 设置：采购成本
	 */
	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	/**
	 * 获取：采购成本
	 */
	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}
	/**
	 * 设置：销售价格
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	/**
	 * 获取：销售价格
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	/**
	 * 设置：数量精度
	 */
	public void setQuantityAccuracy(BigDecimal quantityAccuracy) {
		this.quantityAccuracy = quantityAccuracy;
	}
	/**
	 * 获取：数量精度
	 */
	public BigDecimal getQuantityAccuracy() {
		return quantityAccuracy;
	}
	/**
	 * 设置：单价精度
	 */
	public void setPriceAccuracy(BigDecimal priceAccuracy) {
		this.priceAccuracy = priceAccuracy;
	}
	/**
	 * 获取：单价精度
	 */
	public BigDecimal getPriceAccuracy() {
		return priceAccuracy;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：更新人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除状态
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：
	 */
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	/**
	 * 获取：
	 */
	public Long getProviderId() {
		return providerId;
	}
	/**
	 * 获取：检验方案
	 */
	public Long getInspectionScheme() {
		return inspectionScheme;
	}
	/**
	 * 获取：检验方案
	 */
	public void setInspectionScheme(Long inspectionScheme) {
		this.inspectionScheme = inspectionScheme;
	}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Integer getAuditSign() {
        return auditSign;
    }

    public void setAuditSign(Integer auditSign) {
        this.auditSign = auditSign;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }
}
