package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户报表基础信息(未禁用的用户)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 12:54:56
 */
@Data
public class UserForReportVO implements Serializable {
	private static final long serialVersionUID = 1L;
	// 用户ID
	private Long userId;
	// 用户名
	private String userName;
	// 部门ID
	private Long deptId;
	// 部门名
	private String deptName;
	// 统计数量
	private Integer count;

}
