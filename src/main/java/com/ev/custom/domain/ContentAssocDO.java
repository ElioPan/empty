package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 附件关联表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-02 17:17:17
 */
@ApiModel(value = "附件关联表")
public class ContentAssocDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Integer id;
	//关联属性ID
	@ApiModelProperty(value = "关联属性ID")
	private Integer assocId;
	//关联类型
	@ApiModelProperty(value = "关联类型")
	private String assocType;
	//名称
	@ApiModelProperty(value = "名称")
	private String fileName;
	//路径
	@ApiModelProperty(value = "路径")
	private String filePath;
	//租户ID
	@ApiModelProperty(value = "租户ID")
	private Integer providerId;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//创建人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//创建时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：关联属性ID
	 */
	public void setAssocId(Integer assocId) {
		this.assocId = assocId;
	}
	/**
	 * 获取：关联属性ID
	 */
	public Integer getAssocId() {
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
	 * 设置：名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取：路径
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置：路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * 获取：路径
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * 设置：租户ID
	 */
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	/**
	 * 获取：租户ID
	 */
	public Integer getProviderId() {
		return providerId;
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
	 * 设置：创建人
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
	 * 设置：创建时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：创建时间
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

	public ContentAssocDO(Integer assocId, String assocType, String filePath) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.filePath = filePath;
	}

	public ContentAssocDO(Integer assocId, String assocType, String fileName, String filePath) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public ContentAssocDO() {
	}
}
