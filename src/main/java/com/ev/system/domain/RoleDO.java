package com.ev.system.domain;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class RoleDO {
	
	private Long roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Long createBy;
	private Date createTime;
	private Timestamp updateTime;
	private List<Long> menuIds;
	private List<Long> appMenuIds;
	private List<Long> padMenuIds;
	private Integer dataPermission;

    public Integer getDataPermission() {
        return dataPermission;
    }

    public void setDataPermission(Integer dataPermission) {
        this.dataPermission = dataPermission;
    }

    public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date createTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}

	public List<Long> getAppMenuIds() {
		return appMenuIds;
	}

	public void setAppMenuIds(List<Long> appMenuIds) {
		this.appMenuIds = appMenuIds;
	}

	public List<Long> getPadMenuIds() {
		return padMenuIds;
	}

	public void setPadMenuIds(List<Long> padMenuIds) {
		this.padMenuIds = padMenuIds;
	}

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
