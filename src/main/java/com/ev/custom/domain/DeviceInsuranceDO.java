package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备保险管理
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 09:18:53
 */
@ApiModel(value = "设备保险管理")
public class DeviceInsuranceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//承保险别
	@ApiModelProperty(value = "承保险别")
	private String insurancesort;
	//险别id
	@ApiModelProperty(value = "险别id")
	private Long sortid;
	//保险总额
	@ApiModelProperty(value = "保险总额")
	private Double amount;
	//保险费用
	@ApiModelProperty(value = "保险费用")
	private Double charge;
	//投保开始时间
	@ApiModelProperty(value = "投保开始时间")
	private Date starttime;
	//投保到期时间
	@ApiModelProperty(value = "投保到期时间")
	private Date endtime;
	//保险公司名字
	@ApiModelProperty(value = "保险公司名字")
	private String insurcompany;
	//保险公司主键
	@ApiModelProperty(value = "保险公司主键")
	private Long companyid;
	//理赔日期
	@ApiModelProperty(value = "理赔日期")
	private Date settlementdate;
	//理赔金额
	@ApiModelProperty(value = "理赔金额")
	private Double settleamount;
	//保险业务员
	@ApiModelProperty(value = "保险业务员")
	private String insursalesman;
	//经办人
	@ApiModelProperty(value = "经办人")
	private String agent;
	//经办人id
	@ApiModelProperty(value = "经办人id")
	private String agentid;
	//设备id
	@ApiModelProperty(value = "设备id")
	private Long deviceid;
	//是否已出险  1是  0否
	@ApiModelProperty(value = "是否已出险  1是  0否")
	private String lossoccurred;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
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
	 * 设置：承保险别
	 */
	public void setInsurancesort(String insurancesort) {
		this.insurancesort = insurancesort;
	}
	/**
	 * 获取：承保险别
	 */
	public String getInsurancesort() {
		return insurancesort;
	}
	/**
	 * 设置：险别id
	 */
	public void setSortid(Long sortid) {
		this.sortid = sortid;
	}
	/**
	 * 获取：险别id
	 */
	public Long getSortid() {
		return sortid;
	}
	/**
	 * 设置：保险总额
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * 获取：保险总额
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * 设置：保险费用
	 */
	public void setCharge(Double charge) {
		this.charge = charge;
	}
	/**
	 * 获取：保险费用
	 */
	public Double getCharge() {
		return charge;
	}
	/**
	 * 设置：投保开始时间
	 */
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	/**
	 * 获取：投保开始时间
	 */
	public Date getStarttime() {
		return starttime;
	}
	/**
	 * 设置：投保到期时间
	 */
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	/**
	 * 获取：投保到期时间
	 */
	public Date getEndtime() {
		return endtime;
	}
	/**
	 * 设置：保险公司名字
	 */
	public void setInsurcompany(String insurcompany) {
		this.insurcompany = insurcompany;
	}
	/**
	 * 获取：保险公司名字
	 */
	public String getInsurcompany() {
		return insurcompany;
	}
	/**
	 * 设置：保险公司主键
	 */
	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}
	/**
	 * 获取：保险公司主键
	 */
	public Long getCompanyid() {
		return companyid;
	}
	/**
	 * 设置：理赔日期
	 */
	public void setSettlementdate(Date settlementdate) {
		this.settlementdate = settlementdate;
	}
	/**
	 * 获取：理赔日期
	 */
	public Date getSettlementdate() {
		return settlementdate;
	}
	/**
	 * 设置：理赔金额
	 */
	public void setSettleamount(Double settleamount) {
		this.settleamount = settleamount;
	}
	/**
	 * 获取：理赔金额
	 */
	public Double getSettleamount() {
		return settleamount;
	}
	/**
	 * 设置：保险业务员
	 */
	public void setInsursalesman(String insursalesman) {
		this.insursalesman = insursalesman;
	}
	/**
	 * 获取：保险业务员
	 */
	public String getInsursalesman() {
		return insursalesman;
	}
	/**
	 * 设置：经办人
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	/**
	 * 获取：经办人
	 */
	public String getAgent() {
		return agent;
	}
	/**
	 * 设置：经办人id
	 */
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	/**
	 * 获取：经办人id
	 */
	public String getAgentid() {
		return agentid;
	}
	/**
	 * 设置：设备id
	 */
	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}
	/**
	 * 获取：设备id
	 */
	public Long getDeviceid() {
		return deviceid;
	}
	/**
	 * 设置：是否已出险  1是  0否
	 */
	public void setLossoccurred(String lossoccurred) {
		this.lossoccurred = lossoccurred;
	}
	/**
	 * 获取：是否已出险  1是  0否
	 */
	public String getLossoccurred() {
		return lossoccurred;
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
	 * 设置：更改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
