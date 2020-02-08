package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 工序检验项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 14:47:11
 */
@ApiModel(value = "工序检验项目")
public class ProcessCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//外键id
    @ApiModelProperty(value = "外键id")
	private Long foreignId;
	//检验项目id
    @ApiModelProperty(value = "检验项目id")
	private Long proId;
	//区分标记
    @ApiModelProperty(value = "区分标记")
	private String type;
	//是否必检   1是 0否
    @ApiModelProperty(value = "是否必检   1是 0否")
	private Integer whetherCheck;

	//备注
	@ApiModelProperty(value = "是否必检   1是 0否")
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
	 * 设置：外键id
	 */
	public void setForeignId(Long foreignId) {
		this.foreignId = foreignId;
	}
	/**
	 * 获取：外键id
	 */
	public Long getForeignId() {
		return foreignId;
	}
	/**
	 * 设置：检验项目id
	 */
	public void setProId(Long proId) {
		this.proId = proId;
	}
	/**
	 * 获取：检验项目id
	 */
	public Long getProId() {
		return proId;
	}
	/**
	 * 设置：区分标记
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：区分标记
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：是否必检   1是 0否
	 */
	public void setWhetherCheck(Integer whetherCheck) {
		this.whetherCheck = whetherCheck;
	}
	/**
	 * 获取：是否必检   1是 0否
	 */
	public Integer getWhetherCheck() {
		return whetherCheck;
	}


	/**
	 * 设置：是否必检   1是 0否
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：是否必检   1是 0否
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
