package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 网关信息
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:23:38
 */
@ApiModel(value = "网关信息")
public class GatewayDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//网关序列号
    @ApiModelProperty(value = "网关序列号",required = true)
	private String serialNo;
	//网关名称
    @ApiModelProperty(value = "网关名称",required = true)
	private String name;
	//网关型号
    @ApiModelProperty(value = "网关型号",required = true)
	private String model;
	//厂家
    @ApiModelProperty(value = "厂家",required = true)
	private String manufacturers;
	//安装时间
    @ApiModelProperty(value = "安装时间",required = true)
	private Date setupTime;
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
	 * 设置：网关序列号
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	/**
	 * 获取：网关序列号
	 */
	public String getSerialNo() {
		return serialNo;
	}
	/**
	 * 设置：网关名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：网关名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：网关型号
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * 获取：网关型号
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置：厂家
	 */
	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}
	/**
	 * 获取：厂家
	 */
	public String getManufacturers() {
		return manufacturers;
	}
	/**
	 * 设置：安装时间
	 */
	public void setSetupTime(Date setupTime) {
		this.setupTime = setupTime;
	}
	/**
	 * 获取：安装时间
	 */
	public Date getSetupTime() {
		return setupTime;
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
