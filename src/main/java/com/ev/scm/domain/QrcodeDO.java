package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 库存二维码
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 15:12:03
 */
@Data
@NoArgsConstructor
@ApiModel(value = "库存二维码")
public class QrcodeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
    @ApiModelProperty(value = "序号")
	private Long id;
	//检验单主键
    @ApiModelProperty(value = "检验单主键")
	private Long inspectionId;
	//合同编号
    @ApiModelProperty(value = "合同编号")
	private String contractNo;
	//库存主键
    @ApiModelProperty(value = "库存主键")
	private Long stockId;
	//物料主键
    @ApiModelProperty(value = "物料主键")
	private Long materielId;
	//物料代码
    @ApiModelProperty(value = "物料代码")
	private String materielNo;
	//批号
    @ApiModelProperty(value = "批号")
	private String batch;
	//数量
    @ApiModelProperty(value = "数量")
	private BigDecimal count;
	//剩余数量
	@ApiModelProperty(value = "剩余数量")
	private BigDecimal remainCount;
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

	public QrcodeDO(Long inspectionId, String contractNo, Long materielId, String materielNo, String batch, BigDecimal count) {
		this.inspectionId = inspectionId;
		this.contractNo = contractNo;
		this.materielId = materielId;
		this.materielNo = materielNo;
		this.batch = batch;
		this.count = count;
		this.remainCount = count;
	}
}
