package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 检验项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:31
 */
@ApiModel(value = "检验项目")
public class CheckProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//检验项目代码
    @ApiModelProperty(value = "检验项目代码")
	private String code;
	//检验项目名称
    @ApiModelProperty(value = "检验项目名称")
	private String name;
	//检验标准
    @ApiModelProperty(value = "检验标准")
	private String norm;
	//检验方法
    @ApiModelProperty(value = "检验方法")
	private String method;

	//检验仪器名字
	@ApiModelProperty(value = "检验仪器名字")
	private String instrumentName;

	//检验仪器
    @ApiModelProperty(value = "检验仪器")
	private Long instrument;
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
	 * 设置：检验项目代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：检验项目代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：检验项目名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：检验项目名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：检验标准
	 */
	public void setNorm(String norm) {
		this.norm = norm;
	}
	/**
	 * 获取：检验标准
	 */
	public String getNorm() {
		return norm;
	}
	/**
	 * 设置：检验方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：检验方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置：检验仪器名字
	 */
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	/**
	 * 获取：检验仪器名字
	 */
	public String getInstrumentName() {
		return instrumentName;
	}
	/**
	 * 设置：检验仪器
	 */
	public void setInstrument(Long instrument) {
		this.instrument = instrument;
	}
	/**
	 * 获取：检验仪器
	 */
	public Long getInstrument() {
		return instrument;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
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
}
