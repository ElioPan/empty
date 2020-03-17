package com.ev.framework.config;

/**
 * @author AirOrangeWorkSpace
 *
 */
public final class ConstantForGYL {

    private ConstantForGYL() {
	}

	/**
	 * 失败
	 */
	public static final String ERROR = "error";
	/**
	 * 调拨前缀
	 */
	public static final String DB_PREFIX = "DB";
	/**
	 * 调拨单据前缀
	 */
	public static final String DBDJ_PREFIX = "DBDJ";
	/**
	 * 其他出库前缀
	 */
	public static final String QTCK_PREFIX = "QTCK";
	/**
	 * 生产退料单据前缀
	 */
	public static final String TLDJ_PREFIX = "TLDJ";
	/**
	 * 待审核
	 */
	public static final Long WAIT_AUDIT = 178L;
	/**
	 * 已审核
	 */
	public static final Long OK_AUDITED = 179L;
	/**
	 * 调拨
	 */
	public static final Long DB = 186L;
	/**
	 * 其他出库
	 */
	public static final Long QTCK = 187L;
	/**
	 * 调拨单据
	 */
	public static final Long DBDJ = 189L;
	/**
	 * 领用出库
	 */
	public static final Long LYCK = 193L;
	/**
	 * 退料入库
	 */
	public static final Long TLRK = 199L;
	/**
	 * 生产领用
	 */
	public static final Long SCLY = 200L;
	/**
	 * 销售出库
	 */
	public static final Long XSCK = 188L;
	/**
	 * 委外出库
	 */
	public static final Long WWCK = 274L;
	//==========================================================
	/**
	 * 生产入库
	 */
	public static final Long YDGOODS_WAREHOUSE = 182L;


	/**
	 * 其他入库
	 */
	public static final Long OTHER_WAREHOUSE = 183L;


	/**
	 * 生产入库前缀
	 */
	public static final String SCRK_PREFIX = "SCRK";

	/**
	 * 其他入库前缀
	 */
	public static final String QTRK_PREFIX = "QTRK";

	/**
	 * 未执行  (23-->190未执行)
	 */
	public static final Long EXECUTE_NON = 190L;

	/**
	 * 执行中  (24-->191未执行)
	 */
	public static final Long EXECUTE_NOW = 191L;

	/**
	 * 执行结束  (25--->192未执行)
	 */
	public static final Long EXECUTE_OVER = 192L;

	/**
	 * 盘盈单据    32--->198
	 */
	public static final Long PYDJ = 198L;

	/**
	 * 盘亏单据    28--->195);//PKDJ
	 */
	public static final Long PKDJ = 195L;
	/**
	 * 销售合同
	 */
	public static final Long XSHT = 204L;

	/**
	 * 采购申请单前缀
	 */
	public static final String PURCHAER = "CGSQ";

	/**
	 * 销售合同前缀
	 */
	public static final String SALES_CONTRACT = "XSHT";

	/**
	 * 采购合同前缀
	 */
	public static final String PURCHASE_CONTRCAT = "CGHT";

	/**
	 * 采购/销售共用票据前缀
	 */
	public static final String BILL_CODE = "PJDH";

	/**
	 * 收款单/付款单/银行转账当共用前缀
	 */
	public static final String ALL_BILL = "SK";

	/**
	 * 单据类型：采购申请单
	 */
	public static final Long PURCHASE = 203L;

	/**
	 * 采购费用前缀
	 */
	public static final String EXPENCE = "CGFY";

	/**
	 * 采购合同
	 */
	public static final Long CGHT = 205L;

	/**
	 * 采购发票前缀
	 */
	public static final String INVOICE = "CGFP";

	/**
	 * 采购入库前缀
	 */
	public static final String CGRU_INSTOCK = "CGRK";


	/**
	 * 入库类型：采购入库
	 */
	public static final Long PURCHASE_INSTOCK = 260L;

	/**
	 * 销售退货前缀
	 */
	public static final String XSTH_INSTOCK = "XSTH";

	/**
	 * 销售退货
	 */
	public static final Long SALES_RETURN = 261L;
	/**
	 * 采购退货
	 */
	public static final Integer CGTH = 259;

	/**
	 * 盘点方案前缀
	 */
	public static final String PLAN_PDFA = "PDFA";
	/**
	 * 委外合同前缀
	 */
	public static final String OUTSOURCING_CONTRACT = "WWTH";
	/**
	 * 委外合同
	 */
	public static final Long WWHT = 271L;

	/**
	 * 银行转账单前缀
	 */
	public static final String BANK_TRANSFER = "ZJCQ";
	/**
	 * 加工费用前缀
	 */
	public static final String PROCESSING_CHARGE = "JGFY";

	/**
	 * 付款单前缀
	 */
	public static final String PAYMENT_ORDER = "FK";

	/**
	 * 委外入库类  型
	 */
	public static final Long OUTSOURCING_INSTOCK = 276L;

	/**
	 * 委外入库  单前缀
	 */
	public static final String OUTSOURCING_INSTOCK_CODE = "WWRK";

	/**
	 * 生产投料单
	 */
	public static final Long SCTLD = 277L;
	/**
	 * 委外投料单
	 */
	public static final Long WWTLD = 272L;


	/**
	 * 采购付款  付款单类型
	 */
	public static final Long PR_TYPE = 229L;

	/**
	 * 加权平均  计价方法
	 */
	public static final Integer WEIGHTED_AVERAGE = 144;

	/**
	 * 按数量分配
	 */
	public static final Long COUNT_DISTRIBUTION = 279L;
	/**
	 * 按金额分配
	 */
	public static final Long AMOUNT_DISTRIBUTION = 280L;

	/**
	 * 分批认定 计价方法
	 */
	public static final Integer BATCH_FINDS = 143;


	/**
	 * 支出
	 */
	public static final Long EXPENDITURE = 294L;


}