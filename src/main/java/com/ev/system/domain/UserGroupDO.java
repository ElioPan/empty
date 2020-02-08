package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 用户与用户组对应关系
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2018-06-14 14:51:48
 */
public class UserGroupDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用户ID
	private Long userId;
	//角色ID
	private Long groupId;

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
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：角色ID
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	/**
	 * 获取：角色ID
	 */
	public Long getGroupId() {
		return groupId;
	}
}
