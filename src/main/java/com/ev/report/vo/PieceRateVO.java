package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 12:54:56
 */
@Data
public class PieceRateVO implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	// 生产部门ID
	private Long deptId;
	// 生产部门
	private String deptName;
	// 操作工ID
	private Long operator;
	// 操作工
	private String operatorName;
	// 产品代码
	private String serialNo;
	// 产品名称
	private String materielName;
	// 规格型号
	private String specification;
	// 计量单位
	private String unitUomName;
	// 工序报工单号
	private String code;
	// 日期
	private Date createTime;
	// 工序代码
	private String processCode;
	// 工序名称
	private String processName;
	// 完工数量
	private Double conformityCount;
	// 单件工资
	private Double labourPrice;
	// 计件工资合计
	private Double totalPrice;

	// 标记 部门总计标2 用户总计标1 详情标0
	private int sign;
	// 排序号
	private long sortNo;

}
