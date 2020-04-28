package com.ev.system.domain;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleDO {
	
	private Long roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Long createBy;
	private Date createTime;
	private Date updateTime;
	private List<Long> menuIds;
	private List<Long> appMenuIds;
	private List<Long> padMenuIds;
	private Long dataPermission;

	@Override
	public String toString() {
		return "RoleDO{" +
				"roleId=" + roleId +
				", roleName='" + roleName + '\'' +
				", roleSign='" + roleSign + '\'' +
				", remark='" + remark + '\'' +
				", createBy=" + createBy +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", menuIds=" + menuIds +
				", appMenuIds=" + appMenuIds +
				", padMenuIds=" + padMenuIds +
				'}';
	}
}
