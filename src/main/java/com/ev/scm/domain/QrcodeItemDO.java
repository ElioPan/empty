package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;

import com.ev.framework.utils.ShiroUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 14:28:20
 */
@Data
@NoArgsConstructor
@ApiModel(value = "")
public class QrcodeItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//二维码主键ID
    @ApiModelProperty(value = "二维码主键ID")
	private Long qrcodeId;
	//单据类型
    @ApiModelProperty(value = "单据类型")
	private Long inOutType;
	//单据主键(主表主键)
    @ApiModelProperty(value = "单据主键(主表主键)")
	private Long inheadId;
	//单据主键（子表主键)
    @ApiModelProperty(value = "单据主键（子表主键)")
	private Long inbodyId;
	//
    @ApiModelProperty(value = "")
	private BigDecimal count;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy = ShiroUtils.getUserId();
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime = new Date();
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy = ShiroUtils.getUserId();
	//修改时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime = new Date();
	//删除状态
	@ApiModelProperty(value = "删除状态")
	private Integer delFlag = 0;

	public QrcodeItemDO(Long qrcodeId, Long inOutType, Long inheadId, Long inbodyId, BigDecimal count) {
		this.qrcodeId = qrcodeId;
		this.inOutType = inOutType;
		this.inheadId = inheadId;
		this.inbodyId = inbodyId;
		this.count = count;
	}
}
