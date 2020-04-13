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
public class StockInItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	//主表主键
	private Long inheadId;
	// 入库时间
	private Date inOutTime;
	// 入库单号
	private String stockInCode;
	// 单据类型
	private String documentType;
	//商品/产品id
	private Long materielId;
	// 产品编号
	private String serialNo;
	// 产品名称
	private String materielName;
	// 规格型号
	private String specification;
	//单位
	private String unitUomName;
	//数量
	private BigDecimal count;
	//单价
	private BigDecimal unitPrice;
	//金额
	private BigDecimal amount;
	//批次
	private String batch;
	//仓库
	private Long warehouse;
	//仓库名
	private String facilityName;
	//库位
	private Long warehLocation;
	//库位名
	private String locationName;
	//操作人员名
	private String operatorName;
	//源单类型
	private Long sourceType;
	//源单类型名
	private String sourceTypeName;
	//来源单号
	private String sourceCode;

	//退料原因
	private String returnReason;
	//来源单据明细id
	private Long sourceId;
	//费用
	private BigDecimal expense;
	// 成本
	private BigDecimal cost;
	//创建人
	private Long createBy;
	//创建时间
	private Date createTime;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//删除状态（）
	private Integer delFlag;
	// 未核销完毕的组件物料
	private String materialIdCount;
	// 二维码主键
	private Long qrcodeId;
	//记录已被核销的出库id及核销数量
	private String accountSource;

	// 排序号
	private int sortNo;
	// 标记颜色
	private String sign;


}
