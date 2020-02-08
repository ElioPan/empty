package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-22 14:08:43
 */
@ApiModel(value = "")
public class MeasurePointDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	@ApiModelProperty(value = "主键")
	private Long id;
	// 测点ID
	@ApiModelProperty(value = "测点ID", required = true)
	private String serialNo;
	// 测点名称
	@ApiModelProperty(value = "测点名称", required = true)
	private String name;
	// 读写类型
	@ApiModelProperty(value = "读写类型", required = true)
	private Integer rwType;
	// 子类型
	@ApiModelProperty(value = "子类型", hidden = true)
	private Integer childType;
	// 单位
	@ApiModelProperty(value = "单位", hidden = true)
	private Integer uom;
	// 使用类别
	@ApiModelProperty(value = "使用类别", required = true)
	private Integer useType;
	// 是否手动输入
	@ApiModelProperty(value = "是否手动输入(0为否，1为是)", required = true)
	private Integer isManualInput;
	// 是否虚拟点
	@ApiModelProperty(value = "是否虚拟点", hidden = true)
	private Integer isVirtualPoint;
	// 表达式
	@ApiModelProperty(value = "表达式", hidden = true)
	private String expression;
	// 授权方式
	@ApiModelProperty(value = "授权方式", required = true)
	private Integer impower;
	// 格式
	@ApiModelProperty(value = "格式", required = true)
	private Integer format;
	// 排序号
	@ApiModelProperty(value = "排序号", hidden = true)
	private Long sortNo;
	//
	@ApiModelProperty(value = "设备ID", required = true)
	private Long deviceId;
	// 创建人
	@ApiModelProperty(value = "创建人", hidden = true)
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人", hidden = true)
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间", hidden = true)
	private Date updateTime;
	// 删除状态
	@ApiModelProperty(value = "删除状态", hidden = true)
	private Integer delFlag;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：测点ID
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * 获取：测点ID
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * 设置：测点名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：测点名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：读写类型
	 */
	public void setRwType(Integer rwType) {
		this.rwType = rwType;
	}

	/**
	 * 获取：读写类型
	 */
	public Integer getRwType() {
		return rwType;
	}

	/**
	 * 设置：
	 */
	public void setChildType(Integer childType) {
		this.childType = childType;
	}

	/**
	 * 获取：
	 */
	public Integer getChildType() {
		return childType;
	}

	/**
	 * 设置：
	 */
	public void setUom(Integer uom) {
		this.uom = uom;
	}

	/**
	 * 获取：
	 */
	public Integer getUom() {
		return uom;
	}

	/**
	 * 设置：使用类别
	 */
	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	/**
	 * 获取：使用类别
	 */
	public Integer getUseType() {
		return useType;
	}

	/**
	 * 设置：是否手动输入
	 */
	public void setIsManualInput(Integer isManualInput) {
		this.isManualInput = isManualInput;
	}

	/**
	 * 获取：是否手动输入
	 */
	public Integer getIsManualInput() {
		return isManualInput;
	}

	/**
	 * 设置：是否虚拟点
	 */
	public void setIsVirtualPoint(Integer isVirtualPoint) {
		this.isVirtualPoint = isVirtualPoint;
	}

	/**
	 * 获取：是否虚拟点
	 */
	public Integer getIsVirtualPoint() {
		return isVirtualPoint;
	}

	/**
	 * 设置：表达式
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * 获取：表达式
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * 设置：授权方式
	 */
	public void setImpower(Integer impower) {
		this.impower = impower;
	}

	/**
	 * 获取：授权方式
	 */
	public Integer getImpower() {
		return impower;
	}

	/**
	 * 设置：格式
	 */
	public void setFormat(Integer format) {
		this.format = format;
	}

	/**
	 * 获取：格式
	 */
	public Integer getFormat() {
		return format;
	}

	/**
	 * 设置：排序号
	 */
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * 获取：排序号
	 */
	public Long getSortNo() {
		return sortNo;
	}

	/**
	 * 设置：设备ID
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 获取：设备ID
	 */
	public Long getDeviceId() {
		return deviceId;
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
