package com.ev.mes.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 工序采集项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:46:52
 */
@ApiModel(value = "工序采集项目")
public class ProcessCollectDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//
    @ApiModelProperty(value = "")
	private Long processId;
	//采集代码
    @ApiModelProperty(value = "采集代码")
	private String code;
	//采集项目
    @ApiModelProperty(value = "采集项目")
	private Long proId;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
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
	 * 设置：
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	/**
	 * 获取：
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * 设置：采集代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：采集代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：采集项目
	 */
	public void setProId(Long proId) {
		this.proId = proId;
	}
	/**
	 * 获取：采集项目
	 */
	public Long getProId() {
		return proId;
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
