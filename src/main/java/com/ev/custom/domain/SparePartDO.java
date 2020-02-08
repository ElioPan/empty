package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 备品备件分类表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-21 00:28:51
 */
@ApiModel(value = "备品备件分类表")
public class SparePartDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//备件编号
    @ApiModelProperty(value = "备件编号")
	private String code;
	//备件名称
    @ApiModelProperty(value = "备件名称")
	private String name;
	//备件型号
    @ApiModelProperty(value = "备件型号")
	private String type;
	//备件类型
    @ApiModelProperty(value = "备件类型")
	private Integer spartType;
	//单位
    @ApiModelProperty(value = "单位")
	private String unit;
	//参考价格
    @ApiModelProperty(value = "参考价格")
	private BigDecimal price;
	//仓库id
    @ApiModelProperty(value = "仓库id")
	private Integer warehouse;
	//库位
    @ApiModelProperty(value = "库位")
	private Integer locationId;
	//使用部门id
    @ApiModelProperty(value = "使用部门id")
	private Integer deptId;
	//备注
    @ApiModelProperty(value = "备注")
	private String desc;
	//数量
    @ApiModelProperty(value = "数量")
	private Integer amount;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：备件编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：备件编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：备件名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：备件名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：备件型号
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：备件型号
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：备件类型
	 */
	public void setSpartType(Integer spartType) {
		this.spartType = spartType;
	}
	/**
	 * 获取：备件类型
	 */
	public Integer getSpartType() {
		return spartType;
	}
	/**
	 * 设置：单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * 获取：单位
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * 设置：参考价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：参考价格
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：仓库id
	 */
	public void setWarehouse(Integer warehouse) {
		this.warehouse = warehouse;
	}
	/**
	 * 获取：仓库id
	 */
	public Integer getWarehouse() {
		return warehouse;
	}
	/**
	 * 设置：库位
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	/**
	 * 获取：库位
	 */
	public Integer getLocationId() {
		return locationId;
	}
	/**
	 * 设置：使用部门id
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：使用部门id
	 */
	public Integer getDeptId() {
		return deptId;
	}
	/**
	 * 设置：备注
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：备注
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置：数量
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：数量
	 */
	public Integer getAmount() {
		return amount;
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
	 * 设置：修改人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：修改人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
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
}
