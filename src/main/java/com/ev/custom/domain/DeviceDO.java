package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-09 11:30:12
 */
@ApiModel(value = "设备表")
public class DeviceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//设备编号
	@ApiModelProperty(value = "设备编号")
	private String serialno;
	//设备名称
	@ApiModelProperty(value = "设备名称")
	private String name;
	//设备类型
	@ApiModelProperty(value = "设备类型")
	private Integer type;
	//品牌
	@ApiModelProperty(value = "品牌")
	private String factory;
	//规格型号
	@ApiModelProperty(value = "规格型号")
	private String model;
	//购买时间
	@ApiModelProperty(value = "购买时间")
	private Date buyTime;
	//启用时间
	@ApiModelProperty(value = "启用时间")
	private Date usingTime;
	//使用年限
	@ApiModelProperty(value = "使用年限")
	private Integer serviceYear;
	//部门ID
	@ApiModelProperty(value = "部门ID")
	private Long deptId;
	//负责人ID
	@ApiModelProperty(value = "负责人ID")
	private Long userId;
	//使用状况
	@ApiModelProperty(value = "使用状况")
	private Integer usingStatus;
	//设备用途
	@ApiModelProperty(value = "设备用途")
	private Integer deviceUse;
	//保修到期时间
	@ApiModelProperty(value = "保修到期时间")
	private Date repairEnd;
	//安装地点
	@ApiModelProperty(value = "安装地点")
	private String site;
	//备注
	@ApiModelProperty(value = "备注")
	private String desc;
	//生产商
	@ApiModelProperty(value = "生产商")
	private Long factoryId;
	//供应商
	@ApiModelProperty(value = "供应商")
	private Long supplierId;
	//服务商
	@ApiModelProperty(value = "服务商")
	private Long serviceId;
	//父设备ID
	@ApiModelProperty(value = "父设备ID")
	private Long parentId;
	//设备来源
	@ApiModelProperty(value = "设备来源")
	private Long souceId;
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
	//删除标志
	@ApiModelProperty(value = "删除标志")
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
	 * 设置：设备编号
	 */
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	/**
	 * 获取：设备编号
	 */
	public String getSerialno() {
		return serialno;
	}
	/**
	 * 设置：设备名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：设备名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：设备类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：设备类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：品牌
	 */
	public void setFactory(String factory) {
		this.factory = factory;
	}
	/**
	 * 获取：品牌
	 */
	public String getFactory() {
		return factory;
	}
	/**
	 * 设置：规格型号
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * 获取：规格型号
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置：购买时间
	 */
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	/**
	 * 获取：购买时间
	 */
	public Date getBuyTime() {
		return buyTime;
	}
	/**
	 * 设置：启用时间
	 */
	public void setUsingTime(Date usingTime) {
		this.usingTime = usingTime;
	}
	/**
	 * 获取：启用时间
	 */
	public Date getUsingTime() {
		return usingTime;
	}
	/**
	 * 设置：使用年限
	 */
	public void setServiceYear(Integer serviceYear) {
		this.serviceYear = serviceYear;
	}
	/**
	 * 获取：使用年限
	 */
	public Integer getServiceYear() {
		return serviceYear;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：负责人ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：负责人ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：使用状况
	 */
	public void setUsingStatus(Integer usingStatus) {
		this.usingStatus = usingStatus;
	}
	/**
	 * 获取：使用状况
	 */
	public Integer getUsingStatus() {
		return usingStatus;
	}
	/**
	 * 设置：设备用途
	 */
	public void setDeviceUse(Integer deviceUse) {
		this.deviceUse = deviceUse;
	}
	/**
	 * 获取：设备用途
	 */
	public Integer getDeviceUse() {
		return deviceUse;
	}
	/**
	 * 设置：保修到期时间
	 */
	public void setRepairEnd(Date repairEnd) {
		this.repairEnd = repairEnd;
	}
	/**
	 * 获取：保修到期时间
	 */
	public Date getRepairEnd() {
		return repairEnd;
	}
	/**
	 * 设置：安装地点
	 */
	public void setSite(String site) {
		this.site = site;
	}
	/**
	 * 获取：安装地点
	 */
	public String getSite() {
		return site;
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
	 * 设置：生产商
	 */
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	/**
	 * 获取：生产商
	 */
	public Long getFactoryId() {
		return factoryId;
	}
	/**
	 * 设置：供应商
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * 获取：供应商
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * 设置：服务商
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * 获取：服务商
	 */
	public Long getServiceId() {
		return serviceId;
	}
	/**
	 * 设置：父设备ID
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父设备ID
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：设备来源
	 */
	public void setSouceId(Long souceId) {
		this.souceId = souceId;
	}
	/**
	 * 获取：设备来源
	 */
	public Long getSouceId() {
		return souceId;
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
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
