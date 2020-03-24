package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工艺路线 明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:17
 */
@ApiModel(value = "工艺路线 明细")
public class CraftItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	@ApiModelProperty(value = "序号")
	private Integer serialNumber;
	//工艺路线主键
    @ApiModelProperty(value = "工艺路线主键")
	private Long craftId;
	//工序主键
    @ApiModelProperty(value = "工序主键")
	private Long processId;
	//是否委外(1是0否) 
    @ApiModelProperty(value = "是否委外(1是0否) ")
	private Integer whetherOutsource;
	//是否自动采集   1是 0否
    @ApiModelProperty(value = "是否自动采集   1是 0否")
	private Integer whetherCollect;
	//是否自动派工(1是0否)
    @ApiModelProperty(value = "是否自动派工(1是0否)")
	private Integer autoDispatch;
	//是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
	private Integer whetherExamine;
	//工艺要求
    @ApiModelProperty(value = "工艺要求")
	private String demand;
	//工作时长
    @ApiModelProperty(value = "工作时长")
	private BigDecimal totalHour;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long deptId;
	//基准良率%
    @ApiModelProperty(value = "基准良率%")
	private BigDecimal standard;
	//使用设备id
    @ApiModelProperty(value = "使用设备id")
	private Long deviceId;
	//工序类型
    @ApiModelProperty(value = "工序类型")
	private Integer type;
	//单件工时
    @ApiModelProperty(value = "单件工时")
	private BigDecimal manHour;
	//单件工价
    @ApiModelProperty(value = "单件工价")
	private BigDecimal labourPrice;
	//操作工
    @ApiModelProperty(value = "操作工")
	private Long operator;
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
	 * 设置：工艺路线主键
	 */
	public void setCraftId(Long craftId) {
		this.craftId = craftId;
	}
	/**
	 * 获取：工艺路线主键
	 */
	public Long getCraftId() {
		return craftId;
	}
	/**
	 * 设置：工序主键
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	/**
	 * 获取：工序主键
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * 设置：是否委外(1是0否) 
	 */
	public void setWhetherOutsource(Integer whetherOutsource) {
		this.whetherOutsource = whetherOutsource;
	}
	/**
	 * 获取：是否委外(1是0否) 
	 */
	public Integer getWhetherOutsource() {
		return whetherOutsource;
	}
	/**
	 * 设置：是否自动采集   1是 0否
	 */
	public void setWhetherCollect(Integer whetherCollect) {
		this.whetherCollect = whetherCollect;
	}
	/**
	 * 获取：是否自动采集   1是 0否
	 */
	public Integer getWhetherCollect() {
		return whetherCollect;
	}
	/**
	 * 设置：是否自动派工(1是0否)
	 */
	public void setAutoDispatch(Integer autoDispatch) {
		this.autoDispatch = autoDispatch;
	}
	/**
	 * 获取：是否自动派工(1是0否)
	 */
	public Integer getAutoDispatch() {
		return autoDispatch;
	}
	/**
	 * 设置：是否检验   1是 0否
	 */
	public void setWhetherExamine(Integer whetherExamine) {
		this.whetherExamine = whetherExamine;
	}
	/**
	 * 获取：是否检验   1是 0否
	 */
	public Integer getWhetherExamine() {
		return whetherExamine;
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
	 * 设置：工作时长
	 */
	public void setTotalHour(BigDecimal totalHour) {
		this.totalHour = totalHour;
	}
	/**
	 * 获取：工作时长
	 */
	public BigDecimal getTotalHour() {
		return totalHour;
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
	 * 设置：工序类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：工序类型
	 */
	public Integer getType() {
		return type;
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
