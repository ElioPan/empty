package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 报警规则
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-22 14:06:08
 */
@Data
@ApiModel(value = "报警规则")
public class AlarmRuleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
    //告警规则ID
    @ApiModelProperty(value = "告警规则ID",hidden = true)
	private Long groupId;
	//告警名称
    @ApiModelProperty(value = "告警名称",required = true)
	private String name;
	//告警类型
    @ApiModelProperty(value = "告警类型(157,158)",required = true)
	private Integer alarmType;
	//告警等级
    @ApiModelProperty(value = "告警等级(165,166)",required = true)
	private Integer alarmLevel;
	//触发方式
    @ApiModelProperty(value = "触发方式(0:大于 1:小于 2:等于)",required = true)
	private Integer triggerMode;
	//触发时长
    @ApiModelProperty(value = "触发时长",required = true)
	private Integer triggerTime;
	//持续时长
    @ApiModelProperty(value = "持续时长",required = true)
	private Integer continueTime;
	//告警方式
    @ApiModelProperty(value = "告警方式(0:APP推送 1:短信推送 2:电话推送)格式如: 0,1,2")
	private String alarmWay;
	//排序号
    @ApiModelProperty(value = "排序号",hidden = true)
	private Long sortNo;
	//测点ID
    @ApiModelProperty(value = "测点ID",hidden = true)
	private Long pointId;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
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
	 * 设置：告警名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：告警名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：告警类型
	 */
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	/**
	 * 获取：告警类型
	 */
	public Integer getAlarmType() {
		return alarmType;
	}
	/**
	 * 设置：告警等级
	 */
	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	/**
	 * 获取：告警等级
	 */
	public Integer getAlarmLevel() {
		return alarmLevel;
	}
	/**
	 * 设置：触发方式
	 */
	public void setTriggerMode(Integer triggerMode) {
		this.triggerMode = triggerMode;
	}
	/**
	 * 获取：触发方式
	 */
	public Integer getTriggerMode() {
		return triggerMode;
	}
	/**
	 * 设置：触发时长
	 */
	public void setTriggerTime(Integer triggerTime) {
		this.triggerTime = triggerTime;
	}
	/**
	 * 获取：触发时长
	 */
	public Integer getTriggerTime() {
		return triggerTime;
	}
	/**
	 * 设置：持续时长
	 */
	public void setContinueTime(Integer continueTime) {
		this.continueTime = continueTime;
	}
	/**
	 * 获取：持续时长
	 */
	public Integer getContinueTime() {
		return continueTime;
	}
	/**
	 * 设置：告警方式
	 */
	public void setAlarmWay(String alarmWay) {
		this.alarmWay = alarmWay;
	}
	/**
	 * 获取：告警方式
	 */
	public String getAlarmWay() {
		return alarmWay;
	}
	/**
	 * 设置：排序号
	 */
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}
	/**
	 * 获取：排序号
	 */
	public Long getSortNo() {
		return sortNo;
	}
	/**
	 * 设置：测点ID
	 */
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}
	/**
	 * 获取：测点ID
	 */
	public Long getPointId() {
		return pointId;
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
	 * 设置：告警规则ID
	 */
	public Long getGroupId() {
		return groupId;
	}
	/**
	 * 获取：告警规则ID
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
