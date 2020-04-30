package com.ev.framework.config;

/**
 * @Author ZhangDong
 * @Date 2020/4/15 15:50
 */
public class Constant {
    private Constant(){}

    /**
     * 企业微信配置
     */
    public static final String QIYE_WECHAT_SETTING = "QIYE_WECHAT_SETTING";
    //演示系统账户
    public static final String DEMO_ACCOUNT = "test";
    //自动去除表前缀
    public static final  String AUTO_REOMVE_PRE = "true";
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
     * 暂存
     */
    public static final Long TS = 146L;
    /**
     * 待审核
     */
    public static final Long WAIT_AUDIT = 178L;
    /**
     * 已审核
     */
    public static final Long OK_AUDITED = 179L;
}
