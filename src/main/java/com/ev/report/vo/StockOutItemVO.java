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
	//领料日期
	private Date outTime;
	//生产领料单号
	private String stockOutCode;
	// 单据类型
	private String documentType;
	//商品/产品id
	private Long materielId;
	//物料代码
	private String serialNo;
	//物料名称
	private String materielName;
	//物料规格型号
	private String specification;
	//单位
	private String unitUomName;
	//领用数量
	private BigDecimal count;
	//单价
	private BigDecimal unitPrice;
	//领用金额
	private BigDecimal amount;
	//批号
	private String batch;
	//仓库名
	private String facilityName;
	//库位名
	private String locationName;
	//操作人员名
	private String operatorName;
	//源单类型名
	private String sourceTypeName;
	// 源单单号
	private String sourceCode;
	// 源单ID
	private Long sourceId;

	// 排序号
	private int sortNo;
	// 标记颜色
	private String sign;

}
