package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 产品入库明细表子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class StockOutItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	//生产领料单号
	private String stockOutCode;
	//领料日期
	private Date outTime;
	//物料代码
	private String serialNo;
	//物料名称
	private String materielName;
	//物料规格型号
	private String specification;
	//单位
	private String unitUomName;
	//批号
	private String batch;
	//领用数量
	private Double count;
	//领用金额
	private Double amount;
	// 源单ID
	private Long sourceId;
	// 源单单号
	private String sourceCode;

}
