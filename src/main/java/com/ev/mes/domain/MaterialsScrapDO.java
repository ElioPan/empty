package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 用料报废单
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-09 19:58:36
 */
@ApiModel(value = "用料报废单")
public class MaterialsScrapDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//报废单编号
	@ApiModelProperty(value = "报废单编号")
	private String code;
	//生产投料单主键
	@ApiModelProperty(value = "生产投料单主键")
	private Long foreignId;
	//生产部门
	@ApiModelProperty(value = "生产部门")
	private Long deptId;
	//审核状态
	@ApiModelProperty(value = "审核状态")
	private Integer auditSign;
	//审核人
	@ApiModelProperty(value = "审核人")
	private Long auditId;
	//创建人(制单人)
	@ApiModelProperty(value = "创建人(制单人) ")
	private Long createBy;
	//制单日期
	@ApiModelProperty(value = "制单日期")
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
	 * 设置：报废单编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：报废单编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：生产投料单主键
	 */
	public void setForeignId(Long foreignId) {
		this.foreignId = foreignId;
	}
	/**
	 * 获取：生产投料单主键
	 */
	public Long getForeignId() {
		return foreignId;
	}
	/**
	 * 设置：生产部门
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：生产部门
	 */
	public Long getDeptId() {
		return deptId;
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
	 * 设置：审核人
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	/**
	 * 获取：审核人
	 */
	public Long getAuditId() {
		return auditId;
	}
	/**
	 * 设置：创建人(制单人) 
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人(制单人) 
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：制单日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：制单日期
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
