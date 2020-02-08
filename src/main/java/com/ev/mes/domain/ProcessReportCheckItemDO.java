package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序检验明细（子表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:25
 */
@ApiModel(value = "工序检验明细（子表）")
public class ProcessReportCheckItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工序检验单id
    @ApiModelProperty(value = "工序检验单id")
	private Long reportCheckId;
	//不良原因
    @ApiModelProperty(value = "不良原因")
	private String rejectsReason;
	//不良数量
    @ApiModelProperty(value = "不良数量")
	private BigDecimal rejectsCount;
	//处理方式
    @ApiModelProperty(value = "处理方式")
	private Integer dispose;
	//检验项目id
    @ApiModelProperty(value = "检验项目id")
	private Long projectId;

	@ApiModelProperty(value = "目标检验值")
	private String normn;

	//实际检验值
    @ApiModelProperty(value = "实际检验值")
	private String actualValue;
	//创建人 
    @ApiModelProperty(value = "创建人 ")
	private Long createBy;
	//派单时间 
    @ApiModelProperty(value = "派单时间 ")
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

	@ApiModelProperty(value = "单位")
	private String unit;
	@ApiModelProperty(value = "是否必检")
	private Integer whetherCheck;
	@ApiModelProperty(value = "检验结果")
	private Long result;

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
	 * 设置：工序检验单id
	 */
	public void setReportCheckId(Long reportCheckId) {
		this.reportCheckId = reportCheckId;
	}
	/**
	 * 获取：工序检验单id
	 */
	public Long getReportCheckId() {
		return reportCheckId;
	}
	/**
	 * 设置：不良原因
	 */
	public void setRejectsReason(String rejectsReason) {
		this.rejectsReason = rejectsReason;
	}
	/**
	 * 获取：不良原因
	 */
	public String getRejectsReason() {
		return rejectsReason;
	}
	/**
	 * 设置：不良数量
	 */
	public void setRejectsCount(BigDecimal rejectsCount) {
		this.rejectsCount = rejectsCount;
	}
	/**
	 * 获取：不良数量
	 */
	public BigDecimal getRejectsCount() {
		return rejectsCount;
	}
	/**
	 * 设置：处理方式
	 */
	public void setDispose(Integer dispose) {
		this.dispose = dispose;
	}
	/**
	 * 获取：处理方式
	 */
	public Integer getDispose() {
		return dispose;
	}
	/**
	 * 设置：检验项目id
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：检验项目id
	 */
	public Long getProjectId() {
		return projectId;
	}


	/**
	 * 设置：目标检验值
	 */
	public void setNormn(String normn) {
		this.normn = normn;
	}
	/**
	 * 获取：目标检验值
	 */
	public String getNormn() {
		return normn;
	}


	/**
	 * 设置：实际检验值
	 */
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	/**
	 * 获取：实际检验值
	 */
	public String getActualValue() {
		return actualValue;
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
	 * 设置：派单时间 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：派单时间 
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

	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setWhetherCheck(Integer whetherCheck) {
		this.whetherCheck = whetherCheck;
	}
	public Integer getWhetherCheck() {
		return whetherCheck;
	}
	public void setResult(Long result) {
		this.result = result;
	}
	public Long getResult() {
		return result;
	}


}
