package com.ev.common.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;


/**
 * 人员导入（企业微信）
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:19:56
 */
@Data
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //姓名
    @Excel(name = "姓名")
    private String name;
    //帐号
    @Excel(name = "帐号")
    private String username;
//    //电话
//    @Excel(name = "别名")
//    private String linkPhone;
//    //传真
//    @Excel(name = "职务")
//    private String linkName;
    //部门
    @Excel(name = "部门")
    private String deptName;
    //性别
    @Excel(name = "性别")
    private String sex;
    //手机
    @Excel(name = "手机")
    private String mobile;
//    //开户行
//    @Excel(name = "座机")
//    private String bankName;
    //个人邮箱
    @Excel(name = "个人邮箱")
    private String email;
    //地址
    @Excel(name = "地址")
    private String liveAddress;
//    //所属业务员
//    @Excel(name = "企业简称")
//    private String belongSalesmanName;
//    //所属业务员
//    @Excel(name = "英文名")
//    private String belongSalesmanName;
//    //所属业务员
//    @Excel(name = "激活状态")
//    private String belongSalesmanName;
//    //所属业务员
//    @Excel(name = "禁用状态")
//    private String belongSalesmanName;
//    //所属业务员
//    @Excel(name = "微信插件")
//    private String belongSalesmanName;


}
