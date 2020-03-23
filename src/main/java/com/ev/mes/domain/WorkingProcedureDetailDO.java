package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工序计划单的工序详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-25 11:26:07
 */
@Data
@ApiModel(value = "工序计划单的工序详情")
public class WorkingProcedureDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工序计划ID
    @ApiModelProperty(value = "工序计划ID")
	private Long planId;

	@ApiModelProperty(value = "序号")
	private Integer serialNumber;
	//工序ID
    @ApiModelProperty(value = "工序ID")
	private Long processId;
    //工序类型
    @ApiModelProperty(value = "工序类型")
    private Integer processType;
	//工艺要求
    @ApiModelProperty(value = "工艺要求")
	private String demand;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long deptId;
	//操作工
    @ApiModelProperty(value = "操作工")
	private Long operator;
	//是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
	private Integer isExamine;
	//是否委外(1是0否) 
    @ApiModelProperty(value = "是否委外(1是0否) ")
	private Integer isOutsource;
	//是否联网采集（1是/0不是）
    @ApiModelProperty(value = "是否联网采集（1是/0不是）")
	private Integer isCollect;
	//使用设备id
    @ApiModelProperty(value = "使用设备id")
	private Long deviceId;
	//设备名称
    @ApiModelProperty(value = "设备名称")
	private String deviceName;
	//是否自动派工(0非自动/1自动)
    @ApiModelProperty(value = "是否自动派工(0非自动/1自动)")
	private Integer isAuto;
	//是否已被派工(0未被派工/1被派工）
    @ApiModelProperty(value = "是否已被派工(0未被派工/1被派工）")
	private Integer isDispatching;
	//计划生产数量
    @ApiModelProperty(value = "计划生产数量")
	private BigDecimal planCount;
	//计划开始时间
    @ApiModelProperty(value = "计划开始时间")
	private Date planStartTime;
	//计划完工时间
    @ApiModelProperty(value = "计划完工时间")
	private Date planEndTime;
	//基准良率%
    @ApiModelProperty(value = "基准良率%")
	private BigDecimal standard;
	//机台工作时长
    @ApiModelProperty(value = "机台工作时长")
	private BigDecimal totalHour;
	//单件工时
    @ApiModelProperty(value = "单件工时")
	private BigDecimal manHour;
	//单件工价
    @ApiModelProperty(value = "单件工价")
	private BigDecimal labourPrice;
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
  //已派工数量
    @ApiModelProperty(value = "已派工数量")
	private BigDecimal alreadyCount;
	//完工数量
    @ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
    // 实际开工时间
    @ApiModelProperty(value = "实际开工时间",hidden = true)
	private Date realityEndTime;
    // 实际完工时间
    @ApiModelProperty(value = "实际完工时间",hidden = true)
	private Date realityStartTime;
    // 拆分前的工序ID
    @ApiModelProperty(value = "拆分前的工序ID")
	private Long beforeSplitId;

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
	 * 设置：工序计划ID
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：工序计划ID
	 */
	public Long getPlanId() {
		return planId;
	}
	/**
	 * 设置：工序ID
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	/**
	 * 获取：工序ID
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * 设置：工艺要求
	 */
	public void setDemand(String demand) {
		this.demand = demand;
	}
	/**
	 * 获取：工艺要求
	 */
	public String getDemand() {
		return demand;
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
	 * 设置：是否检验   1是 0否
	 */
	public void setIsExamine(Integer isExamine) {
		this.isExamine = isExamine;
	}
	/**
	 * 获取：是否检验   1是 0否
	 */
	public Integer getIsExamine() {
		return isExamine;
	}
	/**
	 * 设置：是否委外(1是0否) 
	 */
	public void setIsOutsource(Integer isOutsource) {
		this.isOutsource = isOutsource;
	}
	/**
	 * 获取：是否委外(1是0否) 
	 */
	public Integer getIsOutsource() {
		return isOutsource;
	}
	/**
	 * 设置：是否联网采集（1是/0不是）
	 */
	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}
	/**
	 * 获取：是否联网采集（1是/0不是）
	 */
	public Integer getIsCollect() {
		return isCollect;
	}
	/**
	 * 设置：使用设备id
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：使用设备id
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：设备名称
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	/**
	 * 获取：设备名称
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * 设置：是否自动派工(0非自动/1自动)
	 */
	public void setIsAuto(Integer isAuto) {
		this.isAuto = isAuto;
	}
	/**
	 * 获取：是否自动派工(0非自动/1自动)
	 */
	public Integer getIsAuto() {
		return isAuto;
	}
	/**
	 * 设置：是否已被派工(0未被派工/1被派工）
	 */
	public void setIsDispatching(Integer isDispatching) {
		this.isDispatching = isDispatching;
	}
	/**
	 * 获取：是否已被派工(0未被派工/1被派工）
	 */
	public Integer getIsDispatching() {
		return isDispatching;
	}
	/**
	 * 设置：计划生产数量
	 */
	public void setPlanCount(BigDecimal planCount) {
		this.planCount = planCount;
	}
	/**
	 * 获取：计划生产数量
	 */
	public BigDecimal getPlanCount() {
		return planCount;
	}
	/**
	 * 设置：计划开始时间
	 */
	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	/**
	 * 获取：计划开始时间
	 */
	public Date getPlanStartTime() {
		return planStartTime;
	}
	/**
	 * 设置：计划完工时间
	 */
	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	/**
	 * 获取：计划完工时间
	 */
	public Date getPlanEndTime() {
		return planEndTime;
	}
	/**
	 * 设置：基准良率%
	 */
	public void setStandard(BigDecimal standard) {
		this.standard = standard;
	}
	/**
	 * 获取：基准良率%
	 */
	public BigDecimal getStandard() {
		return standard;
	}
	/**
	 * 设置：机台工作时长
	 */
	public void setTotalHour(BigDecimal totalHour) {
		this.totalHour = totalHour;
	}
	/**
	 * 获取：机台工作时长
	 */
	public BigDecimal getTotalHour() {
		return totalHour;
	}
	/**
	 * 设置：单件工时
	 */
	public void setManHour(BigDecimal manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：单件工时
	 */
	public BigDecimal getManHour() {
		return manHour;
	}
	/**
	 * 设置：单件工价
	 */
	public void setLabourPrice(BigDecimal labourPrice) {
		this.labourPrice = labourPrice;
	}
	/**
	 * 获取：单件工价
	 */
	public BigDecimal getLabourPrice() {
		return labourPrice;
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
	/**
	 * 设置：已派工数量
	 */
	public void setAlreadyCount(BigDecimal alreadyCount) {
		this.alreadyCount = alreadyCount;
	}
	/**
	 * 获取：已派工数量
	 */
	public BigDecimal getAlreadyCount() {
		return alreadyCount;
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
	 * 获取：实际完工时间
	 */
	public Date getRealityEndTime() {
		return realityEndTime;
	}
	/**
	 * 设置：实际完工时间
	 */
	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	/**
	 * 获取：实际开始时间
	 */
	public Date getRealityStartTime() {
		return realityStartTime;
	}
	/**
	 * 设置：实际开始时间
	 */
	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}
	/**
	 * 获取：源工序ID
	 */
	public Long getBeforeSplitId() {
		return beforeSplitId;
	}
	/**
	 * 设置：源工序ID
	 */
	public void setBeforeSplitId(Long beforeSplitId) {
		this.beforeSplitId = beforeSplitId;
	}
	
}
