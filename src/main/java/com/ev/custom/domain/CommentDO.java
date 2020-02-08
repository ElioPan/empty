package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 回复信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-22 08:55:56
 */
@ApiModel(value = "回复信息表")
public class CommentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//关联主键
	@ApiModelProperty(value = "关联主键")
	private Long assocId;
	//关联类型
	@ApiModelProperty(value = "关联类型")
	private String assocType;
	//回复
	@ApiModelProperty(value = "回复")
	private String comment;
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
	 * 设置：关联主键
	 */
	public void setAssocId(Long assocId) {
		this.assocId = assocId;
	}
	/**
	 * 获取：关联主键
	 */
	public Long getAssocId() {
		return assocId;
	}
	/**
	 * 设置：关联类型
	 */
	public void setAssocType(String assocType) {
		this.assocType = assocType;
	}
	/**
	 * 获取：关联类型
	 */
	public String getAssocType() {
		return assocType;
	}
	/**
	 * 设置：回复
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * 获取：回复
	 */
	public String getComment() {
		return comment;
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

	public CommentDO(Long assocId, String assocType, String comment) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.comment = comment;
	}

	public CommentDO() {
	}
}
