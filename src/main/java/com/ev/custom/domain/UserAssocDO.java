package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 用户关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 14:58:02
 */
@ApiModel(value = "用户关联表")
public class UserAssocDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//关联属性ID
    @ApiModelProperty(value = "关联属性ID")
	private Long assocId;
	//关联类型
    @ApiModelProperty(value = "关联类型")
	private String assocType;
	//用户
    @ApiModelProperty(value = "用户")
	private Long userId;
	//读取标记
	@ApiModelProperty(value = "读取标记")
	private Integer sign;
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
	 * 设置：关联属性ID
	 */
	public void setAssocId(Long assocId) {
		this.assocId = assocId;
	}
	/**
	 * 获取：关联属性ID
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
	 * 设置：用户
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户
	 */
	public Long getUserId() {
		return userId;
	}

	public UserAssocDO() {
	}

	public UserAssocDO(Long assocId, String assocType, Long userId) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.userId = userId;
	}

	/**
	 * 设置：读取标记
	 */
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	/**
	 * 获取：读取标记
	 */
	public Integer getSign() {
		return sign;
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
