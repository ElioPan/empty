package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 巡检标准表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 15:22:51
 */
@ApiModel(value = "巡检标准表")
public class PatrolProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "",hidden = true)
	private Long id;
	//编号
    @ApiModelProperty(value = "编号")
	private String code;
	//巡检类型
//    @ApiModelProperty(value = "巡检类型")
//	private Integer type;
	//巡检项目名称
    @ApiModelProperty(value = "巡检项目名称")
	private String name;
	//检巡标准
    @ApiModelProperty(value = "检巡标准")
	private String function;
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
	 * 设置：编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：编号
	 */
	public String getCode() {
		return code;
	}
//	/**
//	 * 设置：巡检类型
//	 */
//	public void setType(Integer type) {
//		this.type = type;
//	}
//	/**
//	 * 获取：巡检类型
//	 */
//	public Integer getType() {
//		return type;
//	}
	/**
	 * 设置：巡检项目名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：巡检项目名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：检巡标准
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * 获取：检巡标准
	 */
	public String getFunction() {
		return function;
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
