package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 合同(变更历史)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-11 14:30:57
 */
@Data
@ApiModel(value = "合同(变更历史)")
public class ContractAlterationDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
    @ApiModelProperty(value = "")
	private Long id;
	//销售合同ID
    @ApiModelProperty(value = "销售合同ID")
	private Long contractId;
	//合同类型
    @ApiModelProperty(value = "合同类型")
	private Long contractType;
	//合同号
    @ApiModelProperty(value = "合同号")
	private String contractCode;
	//版本号
    @ApiModelProperty(value = "版本号")
	private String version;
	//变更内容
    @ApiModelProperty(value = "变更内容")
	private String alterationContent;
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
