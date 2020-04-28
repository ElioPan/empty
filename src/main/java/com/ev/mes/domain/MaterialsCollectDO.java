package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用料采集
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:17
 */
@Data
@ApiModel(value = "用料采集")
public class MaterialsCollectDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//工单明细主键
	@ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//物料id
	@ApiModelProperty(value = "物料id")
	private Long materiaId;
	//批号
	@ApiModelProperty(value = "批号")
	private String batch;
	//数量
	@ApiModelProperty(value = "数量")
	private BigDecimal materiaCount;
	//状态
	@ApiModelProperty(value = "状态")
	private Long status;
	//创建人(采集人)
	@ApiModelProperty(value = "创建人(采集人) ")
	private Long createBy;
	//创建时间(采集时间)
	@ApiModelProperty(value = "创建时间(采集时间)")
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
