package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 库存系统设置
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-14 14:03:26
 */
@Data
@ApiModel(value = "库存系统设置")
public class StockStartDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//启用时间
    @ApiModelProperty(value = "启用时间")
	private Date startTime;
	//启用状态
    @ApiModelProperty(value = "启用状态")
	private Integer status;
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
