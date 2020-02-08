package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 工艺路线
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:00
 */
@ApiModel(value = "工艺路线")
public class CraftDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工艺代码
    @ApiModelProperty(value = "工艺代码")
	private String code;
	//工艺名称
    @ApiModelProperty(value = "工艺名称")
	private String name;
	//版本号
    @ApiModelProperty(value = "版本号")
	private String version;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
	//审核人id
    @ApiModelProperty(value = "审核人id")
	private Long auditId;
	//启用禁用
    @ApiModelProperty(value = "启用禁用")
	private Integer useStatus;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Integer auditSign;
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
	 * 设置：工艺代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：工艺代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：工艺名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：工艺名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：版本号
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：审核人id
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	/**
	 * 获取：审核人id
	 */
	public Long getAuditId() {
		return auditId;
	}
	/**
	 * 设置：启用禁用
	 */
	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}
	/**
	 * 获取：启用禁用
	 */
	public Integer getUseStatus() {
		return useStatus;
	}
	/**
	 * 设置：审核状态
	 */
	public void setAuditSign(Integer auditSign) {
		this.auditSign = auditSign;
	}
	/**
	 * 获取：审核状态
	 */
	public Integer getAuditSign() {
		return auditSign;
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
