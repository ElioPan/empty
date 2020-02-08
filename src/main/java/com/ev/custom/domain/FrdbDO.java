package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 故障库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 14:49:11
 */
@ApiModel(value = "故障库")
public class FrdbDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//设备类型
    @ApiModelProperty(value = "设备类型")
	private Long deviceType;
	//设备型号
    @ApiModelProperty(value = "设备型号")
	private String deviceModel;
	//故障代码
    @ApiModelProperty(value = "故障代码")
	private String frNo;
	//故障描述
    @ApiModelProperty(value = "故障描述")
	private String frAppearence;
	//故障类型
    @ApiModelProperty(value = "故障类型")
	private Long frType;
	//故障原因
    @ApiModelProperty(value = "故障原因")
	private String frReason;
	//处理方式
    @ApiModelProperty(value = "处理方式")
	private String dealMethod;
	//备注
    @ApiModelProperty(value = "备注")
	private String description;
	//上传人
    @ApiModelProperty(value = "上传人")
	private Long upUser;
	//上传时间
    @ApiModelProperty(value = "上传时间")
	private Date upTime;
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
	 * 设置：设备类型
	 */
	public void setDeviceType(Long deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * 获取：设备类型
	 */
	public Long getDeviceType() {
		return deviceType;
	}
	/**
	 * 设置：设备型号
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	/**
	 * 获取：设备型号
	 */
	public String getDeviceModel() {
		return deviceModel;
	}
	/**
	 * 设置：故障代码
	 */
	public void setFrNo(String frNo) {
		this.frNo = frNo;
	}
	/**
	 * 获取：故障代码
	 */
	public String getFrNo() {
		return frNo;
	}
	/**
	 * 设置：故障描述
	 */
	public void setFrAppearence(String frAppearence) {
		this.frAppearence = frAppearence;
	}
	/**
	 * 获取：故障描述
	 */
	public String getFrAppearence() {
		return frAppearence;
	}
	/**
	 * 设置：故障类型
	 */
	public void setFrType(Long frType) {
		this.frType = frType;
	}
	/**
	 * 获取：故障类型
	 */
	public Long getFrType() {
		return frType;
	}
	/**
	 * 设置：故障原因
	 */
	public void setFrReason(String frReason) {
		this.frReason = frReason;
	}
	/**
	 * 获取：故障原因
	 */
	public String getFrReason() {
		return frReason;
	}
	/**
	 * 设置：处理方式
	 */
	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}
	/**
	 * 获取：处理方式
	 */
	public String getDealMethod() {
		return dealMethod;
	}
	/**
	 * 设置：备注
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：备注
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：上传人
	 */
	public void setUpUser(Long upUser) {
		this.upUser = upUser;
	}
	/**
	 * 获取：上传人
	 */
	public Long getUpUser() {
		return upUser;
	}
	/**
	 * 设置：上传时间
	 */
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getUpTime() {
		return upTime;
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
