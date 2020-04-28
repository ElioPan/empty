package com.ev.framework.config;


public class Constant {


    private Constant() {
	}
    //演示系统账户
    public static final String DEMO_ACCOUNT = "test";
    //自动去除表前缀
    public static final  String AUTO_REOMVE_PRE = "true";
    //停止计划任务
    public static final String STATUS_RUNNING_STOP = "stop";
    //开启计划任务
    public static final String STATUS_RUNNING_START = "start";
    //部门根节点id
    public static final Long DEPT_ROOT_ID = 0L;
    //缓存方式
    public static final String CACHE_TYPE_REDIS ="redis";

    public static final String LOG_ERROR = "error";

    public static final String REDIS_USER_TOKEN_PREFIX = "shiro_redis_session:";

    public static final String REDIS_SHORT_CUT_PREFIX = "shot-cut:";

    public static final String SERVICE_ROLE = "服务专员";

    public static final String ADMIN_ROLE = "超级用户角色";

    public static final String APPID_WECHAT = "wx04bd47526bcc5d43";

    public static final String APPSECRET_WECHAT = "e0df40629f1138c436b8073488a97929";

	public static final String TABLE_COLUMN = "tableColumn";

	public static final String WECHAT_ACCESS_TOKEN = "weChat:accessToken";

	public static final String WECHAT_MOBILE_ACCESS_TOKEN = "weChat:mobileAccessToken";

	public static final String WECHAT_JSAPI_TICKET = "weChat:jsApiTicket";

	public static final Integer BIGDECIMAL_ZERO = 4;

    /**
     * 供应商
     */
    public static final Long SEVICE_SUPPLIER = 42L;
    /**
     * 生产商
     */
    public static final Long SEVICE_MANUFACTURER = 43L;
    /**
     * 服务商
     */
    public static final Long SEVICE_FACILITATOR = 44L;
    /**
     * 任务责任人
     */
    public static final Long HELD_PERSON = 51L;
    /**
     * 任务抄送人
     */
    public static final Long CC_PERSON = 52L;
    /**
     * 待处理
     */
    public static final Long WAITING_DEAL = 56L;
    /**
     * 待验收
     */
    public static final Long WAITING_CHECK = 57L;
    /**
     * 已验收
     */
    public static final Long ALREADY_CHECK = 58L;
    /**
     * 任务验收人
     */
    public static final Long CHECK_PERSON = 59L;
    /**
     * 任务回复处理
     */
    public static final Long REPLY_DEAL = 60L;
    /**
     * 验收
     */
    public static final Long REPLY_CHECK = 61L;
    /**
     * 退回（审批退回）
     */
    public static final Long APPLY_REJECT = 62L;
    /**
     * 审批中
     */
    public static final Long APPLY_APPROVING = 63L;
    /**
     * 已结束
     */
    public static final Long APPLY_COMPLETED = 64L;
    /**
     * 8D报告编制中
     */
    public static final Long EDITING = 91L;
    /**
     * 8D报告已完成
     */
    public static final Long COMPLETED = 92L;
    /**
     * 通过
     */
    public static final Long RESULT_PASS = 115L;
    /**
     * 不通过
     */
    public static final Long RESULT_UNPASS = 116L;
    /**
     * 计划启用
     */
    public static final Long STATE_START = 129L;
    /**
     * 计划完成
     */
    public static final Long STATE_STOP_OVER = 131L;
    /**
     * 待评价
     */
    public static final Long NO_EVALUATED= 133L;
    /**
     * 维修事件抄送人
     */
    public static final Long REPAIR_EVENT_CC_PERSON = 134L;
    /**
     * 回复
     */
    public static final Long REPLY = 145L;
    /**
     * 暂存
     */
    public static final Long TS = 146L;
    /**
     * 维修责任人
     */
    public static final Long REPAIR_EVENT_HELD_PERSON = 147L;
    /**
     * 已提交
     */
    public static final Long APPLY_APPROED = 148L;
    /**
     * 关闭
     */
    public static final Long CLOSE = 167L;
    /**
     * 全部
     */
    public static final Long ALL_DATA = 243L;
    /**
     * 本部门及下属部门
     */
    public static final Long SUBORDINATE_DEPT_DATA = 244L;
    /**
     * 本部门
     */
    public static final Long THIS_DEPT_DATA = 245L;
    /**
     * 个人
     */
    public static final Long PERSONAL_DATA = 246L;
    /**
     * 自定义数据权限
     */
    public static final Long CUSTOM_DATA = 247L;
    /**
     * 巡检完成图片
     */
    public static final String PATROL_RECORD_APPEARANCE_IMAGE = "PATROL_RECORD_APPEARANCE_IMAGE";
    /**
     * 巡检签到图片
     */
    public static final String PATROL_RECORD_SIGNIN_IMAGE = "PATROL_RECORD_SIGNIN_IMAGE";
    /**
     * 8D报告分析主要原因附件
     */
    public static final String BD_MAINREASON_FILE = "8D_MAINREASON_FILE";
    /**
     * 8D报告分析主要原因附件
     */
    public static final String BD_CHECKRESULT_FILE = "8D_CHECKRESULT_FILE";
	/**
	 * 工序配置附件
	 */
	public static final String PROCESS_FILE = "PROCESS_FILE";
    /**
     * 报修图片
     */
    public static final String REPAIR_EVENT_IMAGE = "REPAIR_EVENT_IMAGE";
    /**
     * 维修记录图片
     */
    public static final String REPAIR_EVENT_RECORD_IMAGE = "REPAIR_EVENT_RECORD_IMAGE";
    /**
     * 任务照片
     */
    public static final String TASK_APPEARANCE_IMAGE = "TASK_APPEARANCE_IMAGE";
	/**
     * 巡检计划
     */
    public static final String PATRAL_PLAN = "PATRAL_PLAN";
	/**
	 * 保养计划
	 */
	public static final String UPKEEP_PLAN = "UPKEEP_PLAN";
    /**
     * 故障报修单据前缀
     */
    public static final String GZBX = "GZBX";
    /**
     * 8D报告单据前缀
     */
    public static final String  BDBG =  "BDBG";
    /**
     * 新闻发布
     */
    public static final String NEWS_PRESS_RELEASE = "NEWS_PRESS_RELEASE";
	/**
     * 文档外观附件
     */
	public static final String DOCUMENT_APPEARANCE_ATTACHMENT = "DOCUMENT_APPEARANCE_ATTACHMENT";
	/**
     * FRDB外观附件
     */
	public static final String FRDB_APPEARANCE_ATTACHMENT = "FRDB_APPEARANCE_ATTACHMENT";
	/**
     * 设备外观形象
     */
	public static final String DEVICE_ARTECLE_IMAGE = "DEVICE_ARTECLE_IMAGE";
	/**
     * 每周报告目标
     */
	public static final String WEEK_REPORT_TARGET = "WEEK_REPORT_TARGET";
	/**
     * 周报附件
     */
	public static final String WEEK_REPORT_APPEARANCE_ATTACHMENT = "WEEK_REPORT_APPEARANCE_ATTACHMENT";
	/**
     * 周报评论
     */
	public static final String WEEK_REPORT_COMMENT = "WEEK_REPORT_COMMENT";
	/**
     * 每月报告目标
     */
	public static final String MONTH_REPORT_TARGET = "MONTH_REPORT_TARGET";
	/**
	 * 每日报告的评论
	 */
	public static final String DAILY_REPORT_COMMENT = "DAILY_REPORT_COMMENT";
	/**
	 * 每日报告外观附件
	 */
	public static final String DAILY_REPORT_APPEARANCE_ATTACHMENT = "DAILY_REPORT_APPEARANCE_ATTACHMENT";
	/**
     * 每日报告目标
     */
	public static final String DAILY_REPORT_TARGET = "DAILY_REPORT_TARGET";

	/**
	 * 休假申请外观附件
	 */
	public static final String LEAVE_APPLY_APPEARANCE_ATTACHMENT = "LEAVE_APPLY_APPEARANCE_ATTACHMENT";
	/**
	 * 休假申请外观附件
	 */
	public static final String LEAVE_APPLY_TARGET = "LEAVE_APPLY_TARGET";
	/**
	 * 休假
	 */
	public static final String LEAVE_APPROVE_TARGET = "LEAVE_APPROVE_TARGET";
	/**
	 * 请假评论
	 */
	public static final String LEAVE_APPLY_COMMENT = "LEAVE_APPLY_COMMENT";
	/**
	 * 请假类型
	 */
	public static final String LEAVE_APPLY_TYPE = "leave_apply_type";
	/**
	 * 月报附件
	 */
	public static final String MONTH_REPORT_APPEARANCE_ATTACHMENT = "MONTH_REPORT_APPEARANCE_ATTACHMENT";
	/**
	 * 月报回复
	 */
	public static final String MONTH_REPORT_COMMENT = "MONTH_REPORT_COMMENT";
	/**
	 * 加班附件
	 */
	public static final String OVER_TIME_APPLY_APPEARANCE_ATTACHMENT = "OVER_TIME_APPLY_APPEARANCE_ATTACHMENT";
	/**
	 * 加班回复
	 */
	public static final String OVER_TIME_APPLY_COMMENT = "OVER_TIME_APPLY_COMMENT";
	/**
	 * 加班申请
	 */
	public static final String OVER_TIME_APPLY_TARGET = "OVER_TIME_APPLY_TARGET ";
	/**
	 * 加班批准
	 */
	public static final String OVER_TIME_APPROVE_TARGET = "OVER_TIME_APPROVE_TARGET";
	/**
	 * 支付申请附件
	 */
	public static final String PAY_APPLY_APPEARANCE_ATTACHMENT = "PAY_APPLY_APPEARANCE_ATTACHMENT";
	/**
	 * 付款批准的目标
	 */
	public static final String PAY_APPROVE_TARGET = "PAY_APPROVE_TARGET";
	/**
	 * 支付申请目标
	 */
	public static final String PAY_APPLY_TARGET = "PAY_APPLY_TARGET";
	/**
	 * 支付申请评论
	 */
	public static final String PAY_APPLY_COMMENT = "PAY_APPLY_COMMENT";
	/**
	 * 报销附件
	 */
	public static final String REIM_APPLY_APPEARANCE_ATTACHMENT = "REIM_APPLY_APPEARANCE_ATTACHMENT";
	/**
	 * 报销批准
	 */
	public static final String REIM_APPROVE_TARGET = "REIM_APPROVE_TARGET";
	/**
	 * 报销申请
	 */
	public static final String REIM_APPLY_TARGET = "REIM_APPLY_TARGET";
	/**
	 * 报销类型
	 */
	public static final String REIM_APPLY_TYPE = "reim_apply_type";
	/**
	 * 报销申请评论
	 */
	public static final String REIM_APPLY_COMMENT = "REIM_APPLY_COMMENT";
	/**
	 * 巡检计划前缀
	 */
	public static final String XJJH = "XJJH";
	/**
	 * 巡检记录前缀
	 */
	public static final String XJJL = "XJJL";
	/**
	 * 保养工单前缀
	 */
	public static final String SBBY = "SBBY";
	/**
	 * 保养计划前缀
	 */
	public static final String BYJH = "BYJH";

	/**
	 * 客户编码前缀
	 */
	public static final String KH = "KH";
	/**
	 * 供应商编码前缀
	 */
	public static final String GYS = "GYS";
/**
	 * 待审核
	 */
	public static final Long WAIT_AUDIT = 178L;
	/**
	 * 已审核
	 */
	public static final Long OK_AUDITED = 179L;

	/**
	 * 费用编码前缀
	 */
	public static final String FY = "FY";/**
     * 库位编码前缀
     */
    public static final String KW = "KW";

    /**
     * 仓库编码前缀
     */
    public static final String CK = "CK";

    /**
     * 物料编码前缀
     */
    public static final String WL = "WL";

    /**
     * BOM编码前缀
     */
    public static final String BOM = "BOM";

    /**
     * 银行type
     */
    public static final String BANK = "bank";

    /**
     * 物料属性type
     */
    public static final String MATERIAL_TYPE = "material_type";
    /**
     * 单位type
     */
    public static final String UOM_TYPE = "uom_type";
    /**
     * 计价方法type
     */
    public static final String VALUATION_METHOD = "valuation_method";
    /**
     * 客户类型type
     */
    public static final String CLIENT_TYPE = "client_type";

    /**
     * 设备前缀
     */
    public static final String SB = "SB";
    /**
     * 设备类别type
     */
    public static final String DEVICE_TYPE = "device_type";
    /**
     * 设备来源type
     */
    public static final String DEVICE_SOURCE = "device_source";
    /**
     * 使用状况type
     */
    public static final String DEVICE_STATUS = "device_status";
    /**
     * 设备用途type
     */
    public static final String DEVICE_PURPOSE = "device_purpose";
	/**
	 * 工位
	 */
	public static final String GW = "GW";

	/**
	 * 收支类型
	 */
	public static final String SZRX = "SZRX";

	/**
	 * 计划禁用
	 */
	public static final Long FORBIDDEN = 130L;

	/**
	 * 巡检标准
	 */
	public static final String XJBZ = "XJBZ";

}
