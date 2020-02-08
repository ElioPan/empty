package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 销售合同(变更历史)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:13
 */
@Data
@ApiModel(value = "销售合同(变更历史)")
public class SalescontractAlterationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//销售合同ID
    @ApiModelProperty(value = "销售合同ID")
	private Long salescontractId;
	//销售合同号
    @ApiModelProperty(value = "销售合同号")
	private String salescontractCode;
	//变更项目(1.销售数量2.销售金额3.应收日期4.应收金额)
    @ApiModelProperty(value = "变更项目(1.销售数量2.销售金额3.应收日期4.应收金额)")
	private String alterationProject;
	//变更前
    @ApiModelProperty(value = "变更前")
	private String afterAlteration;
	//变更后
    @ApiModelProperty(value = "变更后")
	private String beforeAlteration;
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
