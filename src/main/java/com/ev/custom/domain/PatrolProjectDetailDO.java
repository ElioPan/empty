package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 巡检标准明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 15:22:51
 */
@ApiModel(value = "巡检标准明细表")
public class PatrolProjectDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键",hidden = true)
	private Long id;
	//巡检明细名称
    @ApiModelProperty(value = "巡检明细名称")
	private String name;
	//参考值
    @ApiModelProperty(value = "参考值")
	private String refvalue;
	//描述
    @ApiModelProperty(value = "描述")
	private String desc;
	//对应项目ID
    @ApiModelProperty(value = "对应项目ID",hidden = true)
	private Long projectId;
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
	 * 设置：巡检明细名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：巡检明细名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：参考值
	 */
	public void setRefvalue(String refvalue) {
		this.refvalue = refvalue;
	}
	/**
	 * 获取：参考值
	 */
	public String getRefvalue() {
		return refvalue;
	}
	/**
	 * 设置：描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：描述
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置：对应项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：对应项目ID
	 */
	public Long getProjectId() {
		return projectId;
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
