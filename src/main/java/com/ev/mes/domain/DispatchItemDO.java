package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工单明细
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-03 09:41:21
 */
@ApiModel(value = "工单明细")
public class DispatchItemDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//工单代码
	@ApiModelProperty(value = "工单代码")
	private String code;
	//工序计划子表主键
	@ApiModelProperty(value = "工序计划子表主键")
	private Long foriegnId;
	//操作工
	@ApiModelProperty(value = "操作工")
	private Long operator;
	//生产设备id
	@ApiModelProperty(value = "生产设备id")
	private Long deviceId;
	//委外供应商
	@ApiModelProperty(value = "委外供应商")
	private Long supplierId;
	//开始时间（派工单计划时间）
	@ApiModelProperty(value = "开始时间（派工单计划时间）")
	private Date startTime;
	//结束时间（派工单计划时间）
	@ApiModelProperty(value = "结束时间（派工单计划时间）")
	private Date endTime;
	//实际结束时间（工单结案时间）
	@ApiModelProperty(value = "实际结束时间（工单结案时间）")
	private Date actualEndTime;
	//实际开工时间(第一次开工时间）
	@ApiModelProperty(value = "实际开工时间(第一次开工时间）")
	private Date actualStartTime;
	//计划生产数量（派给的数量）
	@ApiModelProperty(value = "计划生产数量（派给的数量）")
	private BigDecimal planCount;
	//状态
	@ApiModelProperty(value = "状态")
	private Integer status;
	//完工数量
	@ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
	//创建人(派单人)
	@ApiModelProperty(value = "创建人(派单人)")
	private Long createBy;
	//派单时间
	@ApiModelProperty(value = "派单时间")
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
	 * 设置：工单代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：工单代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：工序计划子表主键
	 */
	public void setForiegnId(Long foriegnId) {
		this.foriegnId = foriegnId;
	}
	/**
	 * 获取：工序计划子表主键
	 */
	public Long getForiegnId() {
		return foriegnId;
	}
	/**
	 * 设置：操作工
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}
	/**
	 * 获取：操作工
	 */
	public Long getOperator() {
		return operator;
	}
	/**
	 * 设置：生产设备id
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：生产设备id
	 */
	public Long getDeviceId() {
		return deviceId;
	}

	/**
	 * 设置：生产设备id
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * 获取：生产设备id
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * 设置：开始时间（派工单计划时间）
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开始时间（派工单计划时间）
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间（派工单计划时间）
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间（派工单计划时间）
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：实际结束时间（工单结案时间）
	 */
	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	/**
	 * 获取：实际结束时间（工单结案时间）
	 */
	public Date getActualEndTime() {
		return actualEndTime;
	}
	/**
	 * 设置：实际开工时间(第一次开工时间）
	 */
	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	/**
	 * 获取：实际开工时间(第一次开工时间）
	 */
	public Date getActualStartTime() {
		return actualStartTime;
	}
	/**
	 * 设置：计划生产数量（派给的数量）
	 */
	public void setPlanCount(BigDecimal planCount) {
		this.planCount = planCount;
	}
	/**
	 * 获取：计划生产数量（派给的数量）
	 */
	public BigDecimal getPlanCount() {
		return planCount;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：完工数量
	 */
	public void setCompletionCount(BigDecimal completionCount) {
		this.completionCount = completionCount;
	}
	/**
	 * 获取：完工数量
	 */
	public BigDecimal getCompletionCount() {
		return completionCount;
	}
	/**
	 * 设置：创建人(派单人)
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人(派单人)
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
}
