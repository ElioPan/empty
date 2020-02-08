package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序配置
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:45:29
 */
@ApiModel(value = "工序配置")
public class ProcessDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//代码
    @ApiModelProperty(value = "代码")
	private String code;
	//名字
    @ApiModelProperty(value = "名字")
	private String name;
	//基准良率%
    @ApiModelProperty(value = "基准良率%")
	private BigDecimal standard;
	//工序类型
    @ApiModelProperty(value = "工序类型")
	private Integer type;
	//单件工时
    @ApiModelProperty(value = "单件工时")
	private BigDecimal manHour;
	//单件工价
    @ApiModelProperty(value = "单件工价")
	private BigDecimal labourPrice;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long deptId;
	//操作工
    @ApiModelProperty(value = "操作工")
	private Long operator;
	//工艺要求
    @ApiModelProperty(value = "工艺要求")
	private String demand;
	//是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
	private Integer whetherExamine;
	//是否自动采集   1是 0否
    @ApiModelProperty(value = "是否自动采集   1是 0否")
	private Integer whetherCollect;
	//生产设备
    @ApiModelProperty(value = "生产设备")
	private Long deviceId;
	//检验方案id
    @ApiModelProperty(value = "检验方案id")
	private Long checkPlanId;

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


	@ApiModelProperty(value = "审核人")
	private Long auditId;

	@ApiModelProperty(value = "审核状态")
	private Integer auditSign;

	@ApiModelProperty(value = "使用状态   1是 0否")
	private Integer useStatus;



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
	 * 设置：代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名字
	 */
	public String getName() {
		return name;
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
	 * 设置：生产设备
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：生产设备
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：检验方案id
	 */
	public void setCheckPlanId(Long checkPlanId) {
		this.checkPlanId = checkPlanId;
	}
	/**
	 * 获取：检验方案id
	 */
	public Long getCheckPlanId() {
		return checkPlanId;
	}

	/**
	 * 设置：检验方案id
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：检验方案id
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


	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public Long getAuditId() {
		return auditId;
	}


	public void setAuditSign(Integer auditSign) {
		this.auditSign = auditSign;
	}
	public Integer getAuditSign() {
		return auditSign;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}
	public Integer getUseStatus() {
		return useStatus;
	}
}
