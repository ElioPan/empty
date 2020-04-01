package com.ev.framework.config;

/**
 * @author AirOrangeWorkSpace
 *
 */
public final class ConstantForMES{

    private ConstantForMES() {
	}
	/**
	 * 待审核
	 */
	public static final Integer WAIT_AUDIT = ConstantForGYL.WAIT_AUDIT.intValue();
	/**
	 * 已审核
	 */
	public static final Integer OK_AUDITED = ConstantForGYL.OK_AUDITED.intValue();
	/**
	 * BOM文件
	 */
	public static final String BOM_FILE = "BOM_FILE";
	/**
	 * SOP文件
	 */
	public static final String SOP_FILE = "SOP_FILE";
	/**
	 * 检验项目前缀
	 */
	public static final String CHECK_JYXM = "JYXM";

	/**
	 * 检验方案前缀
	 */
	public static final String CHECK_PLAN_JYFA = "JYFA";


	/**
	 * 工序配置前缀
	 */
	public static final String PROCESS_GXPZ = "GXPZ";


	/**
	 * 工艺路线前缀
	 */
	public static final String CRAFT_GYLX = "GYLX";
	
	/**
	 * 生产计划工单前缀
	 */
	public static final String SCJH_PREFIX = "SCJH";
	/**
	 * 生产计划
	 */
	public static final Integer SCJH = 278;
	/**
	 * 计划
	 */
	public static final Integer PLAN = 231;
	/**
	 * 下达
	 */
	public static final Integer ISSUED = 232;
	/**
	 * 挂起
	 */
	public static final Integer PUT_UP = 233;
	/**
	 * 结案
	 */
	public static final Integer CLOSE_CASE = 234;
	/**
	 * 工序跟踪
	 */
	public static final Integer PROCESS_TRACKING = 235;
	/**
	 * 派工
	 */
	public static final Integer SEND_ORDERS = 236;
	/**
	 * 生产投料工单前缀
	 */
	public static final String SCTL_PREFIX = "TLJH";
	/**
	 * 工序计划单前缀
	 */
	public static final String GXJH_PREFIX = "GXJH";
	/**
	 * 工序计划工艺路线
	 */
	public static final String GXJH_GYLX = "GXJH_GYLX";

	/**
	 * 班制配置
	 */
	public static final String SHIFT_BZPZ = "BZPZ";

	/**
	 * 工单前缀
	 */
	public static final String DISPATCH_JHGD = "GXGD";
	/**
	 * 开工
	 */
	public static final Integer START_WORK = 237;

	/**
	 * 暂存
	 */
	public static final Integer MES_TS= Constant.TS;
	/**
	 * 已提交
	 */
	public static final Integer MES_APPLY_APPROED = Constant.APPLY_APPROED;

	/**
	 * 工序报工单号前缀
	 */
	public static final String DISPATCH_GXBG = "GXBG";

	/**
	 * 工序检验单号前缀
	 */
	public static final String REPORT_CHECK_GXJY = "GXJY";

	/**
	 * 用料报废单号前缀
	 */
	public static final String SCRAP_YLBF = "YLBF";


	/**
	 * 返工返修
	 */
	public static final Integer REWPRKER = 212;

	/**
	 * 报工 返工返修类型标记
	 */
	public static final String REWORK_REPORT_YPE ="REWORK_REPORT";

	/**
	 * 报检 返工返修类型标记
	 */
	public static final String REWORK_CHECK_TYPE ="REWORK_CHECK";

	/**
	 * 待产
	 */
	public static final Integer AWAITING_DELIVERY = 249;

	/**
	 * 投料单Type
	 */
	public static final String FEEDING = "feeding";

	/**
	 * 工序类型Type
	 */
	public static final String PROCESS_TYPE = "process_type";

	/**
	 * 产品检验
	 */
	public static final Integer CPJY = 217;

	/**
	 * 生产投料
	 */
	public static final Integer SCTL = 277;

	/**
	 * 来料检验
	 */
	public static final Integer LLJY = 216;

}
