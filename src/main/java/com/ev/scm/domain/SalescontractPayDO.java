package com.ev.scm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 销售合同收款条件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:14
 */
@Data
@ApiModel(value = "销售合同收款条件表")
public class SalescontractPayDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long salescontractId;
	//应收日期
    @ApiModelProperty(value = "应收日期")
	private Date receivableDate;
	//应收金额
    @ApiModelProperty(value = "应收金额")
	private BigDecimal receivableAmount;
	//已收金额
    @ApiModelProperty(value = "已收金额")
	private BigDecimal receivedAmount;
	//未收金额
    @ApiModelProperty(value = "未收金额")
	private BigDecimal unpayAmount;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
