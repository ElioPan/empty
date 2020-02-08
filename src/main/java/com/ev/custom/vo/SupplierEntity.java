package com.ev.custom.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Builder;
import lombok.Data;
/**
 * 供应商导出
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:19:56
 */
@Data
public class SupplierEntity {

    @Excel(name = "供应商编码")
    private String code;
    @Excel(name = "供应商名称")
    private String name;
    @Excel(name = "联系人")
    private String linkName;
    @Excel(name = "联系电话")
    private String linkPhone;
    @Excel(name = "地址")
    private String address;
    @Excel(name = "税号")
    private String taxNumber;
    @Excel(name = "开户行")
    private String bank;
    @Excel(name = "账号")
    private String account;

}
