package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 单位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public class UomDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//编号
	private String serialNo;
	//名称
	private String name;
	//缩写
	private String shortName;
	//单位类型
	private Integer uomType;
	//创建人
	private Long createBy;
	//创建时间
	private Date createTime;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//删除状态
	private Integer delFlag;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：编号
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	/**
	 * 获取：编号
	 */
	public String getSerialNo() {
		return serialNo;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：缩写
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * 获取：缩写
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置：单位类型
	 */
	public void setUomType(Integer uomType) {
		this.uomType = uomType;
	}
	/**
	 * 获取：单位类型
	 */
	public Integer getUomType() {
		return uomType;
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
