package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 出库主表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
@Data
@ApiModel(value = "出库主表")
public class StockOutDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @ApiModelProperty(value = "")
    private Long id;
    //出库单据号/单据编号
    @ApiModelProperty(value = "出库单据号/单据编号", hidden = true)
    private String outCode;
    //出库类型
    @ApiModelProperty(value = "出库类型")
    private Long outboundType;
    //客户ID
    @ApiModelProperty(value = "客户ID")
    private Long clientId;
    //供应商ID
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    //销售员
    @ApiModelProperty(value = "销售员", required = true)
    private Long salesUser;
    //生产部门ID/销售部门ID
    @ApiModelProperty(value = "生产部门ID/销售部门ID", required = true)
    private Long deptId;
    //销售方式(0现销/1赊销)
    @ApiModelProperty(value = "销售方式(0现销/1赊销)")
    private Integer salesType;
    //出库时间/退货日期
    @ApiModelProperty(value = "出库时间/退货日期", required = true)
    private Date outTime;
    //出库操作员/采购员/销售员
    @ApiModelProperty(value = "出库操作员/采购员/销售员", required = true)
    private Long operator;
    //审核人员
    @ApiModelProperty(value = "审核人员", hidden = true)
    private Long auditor;
    //审核标志：（审核/反审）
    @ApiModelProperty(value = "审核标志：（审核/反审）", hidden = true)
    private Long auditSign;
    //审核时间
    @ApiModelProperty(value = "审核时间", hidden = true)
    private Date auditTime;
    //备注
    @ApiModelProperty(value = "备注")
    private String remarks;
    //创建人
    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createBy;
    //创建时间
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;
    //修改人
    @ApiModelProperty(value = "修改人", hidden = true)
    private Long updateBy;
    //修改时间
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateTime;
    //删除状态
    @ApiModelProperty(value = "删除状态", hidden = true)
    private Integer delFlag;

}
