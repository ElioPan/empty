package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 用料报废单
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-09 19:58:36
 */
@Data
@ApiModel(value = "用料报废单")
public class MaterialsScrapDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//报废单编号
	@ApiModelProperty(value = "报废单编号")
	private String code;
	//生产投料单主键
	@ApiModelProperty(value = "生产投料单主键")
	private Long foreignId;
	//生产部门
	@ApiModelProperty(value = "生产部门")
	private Long deptId;
	//审核状态
	@ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//审核人
	@ApiModelProperty(value = "审核人")
	private Long auditId;
	//创建人(制单人)
	@ApiModelProperty(value = "创建人(制单人) ")
	private Long createBy;
	//制单日期
	@ApiModelProperty(value = "制单日期")
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
