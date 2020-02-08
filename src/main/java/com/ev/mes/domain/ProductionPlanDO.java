package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:26:39
 */
@ApiModel(value = "生产计划单")
public class ProductionPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// 源单类型
	@ApiModelProperty(value = "源单类型")
	private Integer sourceType;
	// 来源单ID
	@ApiModelProperty(value = "来源单ID")
	private Long sourceId;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	// 计划单号
	@ApiModelProperty(value = "计划单号", hidden = true)
	private String planNo;
	// 产品ID
	@ApiModelProperty(value = "产品ID", required = true)
	private Integer materielId;
	// 生产部门
	@ApiModelProperty(value = "生产部门", required = true)
	private Long proDept;
	// 生产类型
	@ApiModelProperty(value = "生产类型", required = true)
	private Integer type;
	// 是否限额(0不限/1限)
	@ApiModelProperty(value = "是否限额(0不限/1限)")
	private Integer isQuota;
	// 计划生产数量
	@ApiModelProperty(value = "计划生产数量", required = true)
	private BigDecimal planCount;
	// 批号
	@ApiModelProperty(value = "批号")
	private String batchNo;
	// 计划开始时间
	@ApiModelProperty(value = "计划开始时间", required = true)
	private Date planStartTime;
	// 计划完工时间
	@ApiModelProperty(value = "计划完工时间", required = true)
	private Date planEndTime;
	// BOMID
	@ApiModelProperty(value = "BOMID", required = true)
	private Long bomId;
	// 工艺路线ID
	@ApiModelProperty(value = "工艺路线ID")
	private Long tecRouteId;
	// 完工上限
	@ApiModelProperty(value = "完工上限")
	private BigDecimal completionMax;
	// 完工下限
	@ApiModelProperty(value = "完工下限")
	private BigDecimal completionMin;
	// 是否检验(0不检验/1检验)
	@ApiModelProperty(value = "是否检验(0不检验/1检验)")
	private Integer isCheck;
	// 检验方案
	@ApiModelProperty(value = "检验方案")
	private Long inspectionScheme;
	// 客户ID
	@ApiModelProperty(value = "客户ID")
	private Long clientId;
    // 客户ID
    @ApiModelProperty(value = "客户名称")
    private String clientName;
	// 交货期
	@ApiModelProperty(value = "交货期")
	private Date deliveryDate;
	// 客户商品名称
	@ApiModelProperty(value = "客户商品名称")
	private String clientProductName;
	// 客户料号
	@ApiModelProperty(value = "客户料号")
	private String clientProductNo;
	// 下达时间
	@ApiModelProperty(value = "下达时间", hidden = true)
	private Date giveTime;
	// 结案时间（实际完成时间）
	@ApiModelProperty(value = "结案时间（实际完成时间）", hidden = true)
	private Date actualFinishTime;
	// 单据状态
	@ApiModelProperty(value = "单据状态", hidden = true)
	private Integer status;
	// 审核人员
	@ApiModelProperty(value = "审核人员", hidden = true)
	private Long auditor;
	// 创建人
	@ApiModelProperty(value = "创建人", hidden = true)
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人", hidden = true)
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间", hidden = true)
	private Date updateTime;
	// 删除状态
	@ApiModelProperty(value = "删除状态", hidden = true)
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
	 * 设置：源单类型
	 */
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 获取：源单类型
	 */
	public Integer getSourceType() {
		return sourceType;
	}

	/**
	 * 设置：来源单ID
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * 获取：来源单ID
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * 设置：计划单号
	 */
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	/**
	 * 获取：计划单号
	 */
	public String getPlanNo() {
		return planNo;
	}

	/**
	 * 设置：产品ID
	 */
	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
	}

	/**
	 * 获取：产品ID
	 */
	public Integer getMaterielId() {
		return materielId;
	}

	/**
	 * 设置：生产部门
	 */
	public void setProDept(Long proDept) {
		this.proDept = proDept;
	}

	/**
	 * 获取：生产部门
	 */
	public Long getProDept() {
		return proDept;
	}

	/**
	 * 设置：生产类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取：生产类型
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置：是否限额(0不限/1限)
	 */
	public void setIsQuota(Integer isQuota) {
		this.isQuota = isQuota;
	}

	/**
	 * 获取：是否限额(0不限/1限)
	 */
	public Integer getIsQuota() {
		return isQuota;
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
	 * 设置：批号
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 获取：批号
	 */
	public String getBatchNo() {
		return batchNo;
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
	 * 设置：BOMID
	 */
	public void setBomId(Long bomId) {
		this.bomId = bomId;
	}

	/**
	 * 获取：BOMID
	 */
	public Long getBomId() {
		return bomId;
	}

	/**
	 * 设置：工艺路线ID
	 */
	public void setTecRouteId(Long tecRouteId) {
		this.tecRouteId = tecRouteId;
	}

	/**
	 * 获取：工艺路线ID
	 */
	public Long getTecRouteId() {
		return tecRouteId;
	}

	/**
	 * 设置：完工上限
	 */
	public void setCompletionMax(BigDecimal completionMax) {
		this.completionMax = completionMax;
	}

	/**
	 * 获取：完工上限
	 */
	public BigDecimal getCompletionMax() {
		return completionMax;
	}

	/**
	 * 设置：完工下限
	 */
	public void setCompletionMin(BigDecimal completionMin) {
		this.completionMin = completionMin;
	}

	/**
	 * 获取：完工下限
	 */
	public BigDecimal getCompletionMin() {
		return completionMin;
	}

	/**
	 * 设置：是否检验(0不检验/1检验)
	 */
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	/**
	 * 获取：是否检验(0不检验/1检验)
	 */
	public Integer getIsCheck() {
		return isCheck;
	}

	/**
	 * 设置：检验方案
	 */
	public void setInspectionScheme(Long inspectionScheme) {
		this.inspectionScheme = inspectionScheme;
	}

	/**
	 * 获取：检验方案
	 */
	public Long getInspectionScheme() {
		return inspectionScheme;
	}

	/**
	 * 设置：客户ID
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	/**
	 * 获取：客户ID
	 */
	public Long getClientId() {
		return clientId;
	}
    /**
     * 设置：客户名称
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 获取：客户名称
     */
    public String  getClientName() {
        return clientName;
    }

	/**
	 * 设置：交货期
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * 获取：交货期
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * 设置：客户商品名称
	 */
	public void setClientProductName(String clientProductName) {
		this.clientProductName = clientProductName;
	}

	/**
	 * 获取：客户商品名称
	 */
	public String getClientProductName() {
		return clientProductName;
	}

	/**
	 * 设置：客户料号
	 */
	public void setClientProductNo(String clientProductNo) {
		this.clientProductNo = clientProductNo;
	}

	/**
	 * 获取：客户料号
	 */
	public String getClientProductNo() {
		return clientProductNo;
	}

	/**
	 * 设置：下达时间
	 */
	public void setGiveTime(Date giveTime) {
		this.giveTime = giveTime;
	}

	/**
	 * 获取：下达时间
	 */
	public Date getGiveTime() {
		return giveTime;
	}

	/**
	 * 设置：结案时间（实际完成时间）
	 */
	public void setActualFinishTime(Date actualFinishTime) {
		this.actualFinishTime = actualFinishTime;
	}

	/**
	 * 获取：结案时间（实际完成时间）
	 */
	public Date getActualFinishTime() {
		return actualFinishTime;
	}

	/**
	 * 设置：单据状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：单据状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置：审核人员
	 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/**
	 * 获取：审核人员
	 */
	public Long getAuditor() {
		return auditor;
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
	 * 设置：来源单号
	 */
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceNo() {
		return sourceNo;
	}
}
