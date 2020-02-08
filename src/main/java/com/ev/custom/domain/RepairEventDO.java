package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 维修事件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@ApiModel(value = "维修事件表")
public class RepairEventDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "事件主键")
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//故障类型
    @ApiModelProperty(value = "故障类型(设备故障：103 电路故障：104 油路故障：105)",hidden = true)
	private Integer type;
	//故障等级
    @ApiModelProperty(value = "故障等级(普通任务：50 紧急任务：49)",required = true)
	private Integer level;
	//使用情况
    @ApiModelProperty(value = "使用情况(正常运行:106 带病运行:107 停机待修:108 停用报废:109)",hidden = true)
	private Integer usage;
	//维修责任人
    @ApiModelProperty(value = "维修责任人",required = true)
	private Long engineerId;
	//要求完成时间
    @ApiModelProperty(value = "要求完成时间 (格式：2019-08-01 10:00:00)")
	private Date planTime;
	//工时
    @ApiModelProperty(value = "工时",hidden = true)
	private Double manHour;
	//成本
    @ApiModelProperty(value = "成本",hidden = true)
	private BigDecimal cost;
	//维修状态
    @ApiModelProperty(value = "维修状态",hidden = true)
	private Integer status;
	//内容
    @ApiModelProperty(value = "详细描述",required = true)
	private String content;
	//关联设备
    @ApiModelProperty(value = "关联设备",required = true)
	private Long deviceId;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//报修时间
    @ApiModelProperty(value = "报修时间",hidden = true)
	private Date createTime;
    //关联单据ID
    @ApiModelProperty(value = "关联单据ID")
    private Long parentId;
    //关联单据类型。1保养单据；2巡检单据；
    @ApiModelProperty(value = "关联单据类型")
    private Integer parentType;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
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
	 * 设置：工单号
	 */
	public void setWorkOrderno(String workOrderno) {
		this.workOrderno = workOrderno;
	}
	/**
	 * 获取：工单号
	 */
	public String getWorkOrderno() {
		return workOrderno;
	}
	/**
	 * 设置：故障类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：故障类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：故障等级
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：故障等级
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：使用情况
	 */
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	/**
	 * 获取：使用情况
	 */
	public Integer getUsage() {
		return usage;
	}
	/**
	 * 设置：维修责任人
	 */
	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}
	/**
	 * 获取：维修责任人
	 */
	public Long getEngineerId() {
		return engineerId;
	}
	/**
	 * 设置：要求完成时间
	 */
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	/**
	 * 获取：要求完成时间
	 */
	public Date getPlanTime() {
		return planTime;
	}
	/**
	 * 设置：工时
	 */
	public void setManHour(Double manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：工时
	 */
	public Double getManHour() {
		return manHour;
	}
	/**
	 * 设置：成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * 获取：成本
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * 设置：维修状态	
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：维修状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：关联设备
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：关联设备
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
	 * 设置：报修时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：报修时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
    /**
     * 设置：关联单据ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    /**
     * 获取：关联单据ID
     */
    public Long getParentId() {
        return parentId;
    }
    /**
     * 设置：关联单据类型
     */
    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }
    /**
     * 获取：关联单据类型
     */
    public Integer getParentType() {
        return parentType;
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
