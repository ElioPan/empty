package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用料采集
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:17
 */
@ApiModel(value = "用料采集")
public class MaterialsCollectDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//工单明细主键
	@ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//物料id
	@ApiModelProperty(value = "物料id")
	private Long materiaId;
	//批号
	@ApiModelProperty(value = "批号")
	private String batch;
	//数量
	@ApiModelProperty(value = "数量")
	private BigDecimal materiaCount;
	//状态
	@ApiModelProperty(value = "状态")
	private Integer status;
	//创建人(采集人)
	@ApiModelProperty(value = "创建人(采集人) ")
	private Long createBy;
	//创建时间(采集时间)
	@ApiModelProperty(value = "创建时间(采集时间)")
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
	 * 设置：工单明细主键
	 */
	public void setDispatchItemId(Long dispatchItemId) {
		this.dispatchItemId = dispatchItemId;
	}
	/**
	 * 获取：工单明细主键
	 */
	public Long getDispatchItemId() {
		return dispatchItemId;
	}
	/**
	 * 设置：物料id
	 */
	public void setMateriaId(Long materiaId) {
		this.materiaId = materiaId;
	}
	/**
	 * 获取：物料id
	 */
	public Long getMateriaId() {
		return materiaId;
	}
	/**
	 * 设置：批号
	 */
	public void setBatch(String batch) {
		this.batch = batch;
	}
	/**
	 * 获取：批号
	 */
	public String getBatch() {
		return batch;
	}
	/**
	 * 设置：数量
	 */
	public void setMateriaCount(BigDecimal materiaCount) {
		this.materiaCount = materiaCount;
	}
	/**
	 * 获取：数量
	 */
	public BigDecimal getMateriaCount() {
		return materiaCount;
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
	 * 设置：创建人(采集人) 
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人(采集人) 
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间(采集时间)
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间(采集时间)
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
