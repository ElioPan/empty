package com.ev.mes.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * BOM子物料表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-11 09:10:27
 */
@Data
@ApiModel(value = "BOM子物料表")
public class BomDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//
    @ApiModelProperty()
	private Long bomId;
	//子物料ID
    @ApiModelProperty(value = "子物料ID")
	private Long materielId;
	//标准数量
    @ApiModelProperty(value = "标准数量")
	private BigDecimal standardCount;
	//损耗率(单位%)
    @ApiModelProperty(value = "损耗率(单位%)")
	private BigDecimal wasteRate;
	//工序
    @ApiModelProperty(value = "工序")
	private Long processId;
	//工位名
    @ApiModelProperty(value = "工位名")
	private String stationName;
	//工位ID
    @ApiModelProperty(value = "工位ID")
	private Long stationId;
	//是否关键件
	@ApiModelProperty(value = "是否关键件")
	private Integer isKeyComponents;
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
