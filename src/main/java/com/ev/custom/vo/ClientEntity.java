package com.ev.custom.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;


/**
 * 客户导出
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:19:56
 */
@Data
public class ClientEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //客户编号
    @Excel(name = "客户编码")
    private String code;
    //客户名称
    @Excel(name = "客户名称")
    private String name;
    //电话
    @Excel(name = "联系电话")
    private String linkPhone;
    //联系人
    @Excel(name = "联系人")
    private String linkName;
    //地址
    @Excel(name = "地址")
    private String address;
    //客户类型
    @Excel(name = "客户类型")
    private String typeName;
    //税号
    @Excel(name = "税号")
    private String taxNumber;
    //开户行
    @Excel(name = "开户行")
    private String bankName;
    //账号
    @Excel(name = "账号")
    private String account;
    //所属部门
    @Excel(name = "所属部门")
    private String belongDeptName;
    //所属业务员
    @Excel(name = "所属业务员")
    private String belongSalesmanName;


}
