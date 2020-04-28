package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 字典类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 08:57:07
 */
@Data
@ApiModel(value = "字典类型")
public class DictionaryTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//编号
    @ApiModelProperty(value = "编号")
	private String code;
	//名称
    @ApiModelProperty(value = "名称")
	private String name;
	//上级
    @ApiModelProperty(value = "上级")
	private Long parentId;
  //是否可修改
    @ApiModelProperty(value = "是否能修改（0能修改，1不能修改）")
	private Integer isUpdate;
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
