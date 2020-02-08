package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 租户表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-04 13:55:46
 */
public class ProviderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//名称
	private String name;
	//备注
	private String remark;
	//创建时间
	private Date createTime;

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
}
