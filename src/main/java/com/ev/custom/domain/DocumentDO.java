package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 文档
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 12:59:46
 */
@ApiModel(value = "文档")
public class DocumentDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//文档名称
    @ApiModelProperty(value = "文档名称")
	private String name;
	//文档类型
    @ApiModelProperty(value = "文档类型")
	private Long type;
	//上传人
    @ApiModelProperty(value = "上传人")
	private Long upUser;
	//所属设备
    @ApiModelProperty(value = "所属设备")
	private Long deviceId;
	//来源
    @ApiModelProperty(value = "来源")
	private Long source;
	//上传时间
    @ApiModelProperty(value = "上传时间")
	private Date upTime;
	//版本号
    @ApiModelProperty(value = "版本号")
	private String version;
	//备注说明
    @ApiModelProperty(value = "备注说明")
	private String description;
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
	 * 设置：文档名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：文档名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：文档类型
	 */
	public void setType(Long type) {
		this.type = type;
	}
	/**
	 * 获取：文档类型
	 */
	public Long getType() {
		return type;
	}
	/**
	 * 设置：上传人
	 */
	public void setUpUser(Long upUser) {
		this.upUser = upUser;
	}
	/**
	 * 获取：上传人
	 */
	public Long getUpUser() {
		return upUser;
	}
	/**
	 * 设置：所属设备
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：所属设备
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：来源
	 */
	public void setSource(Long source) {
		this.source = source;
	}
	/**
	 * 获取：来源
	 */
	public Long getSource() {
		return source;
	}
	/**
	 * 设置：上传时间
	 */
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getUpTime() {
		return upTime;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：版本号
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 设置：备注说明
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：备注说明
	 */
	public String getDescription() {
		return description;
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
