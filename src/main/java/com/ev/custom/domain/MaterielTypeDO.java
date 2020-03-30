package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


/**
 * 物料类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-08 14:33:15
 */
@Data
@ApiModel(value = "物料类型")
public class MaterielTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Integer id;
	//名称
    @ApiModelProperty(value = "名称",required = true)
	private String name;
	//代码
	@ApiModelProperty(value = "编号",required = true)
	private String code;
	//上级类型Id
    @ApiModelProperty(value = "上级类型Id",hidden = true)
	private Long parentId;
	//上级类型Id
	@ApiModelProperty(value = "是否系统自带(1:是；0:否)",hidden = true)
	@Builder.Default
	private Integer isSystem= 0;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;
}
