package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户报表显示类
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 12:54:56
 */
@Data
public class AgendaAccountingReportVO implements Serializable {
	private static final long serialVersionUID = 1L;
	// 部门名
	private String deptName;
	// 用户名
	private String name;
	// 用户ID
	private Long createBy;
	// 类型
	private Long type;
	// 类型名
	private String typeName;
	// 开始时间
	private Date beginTime;
	// 原因
	private String reason;
	// 总数
	private Double totalCount;

}
