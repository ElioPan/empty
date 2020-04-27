package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 数据字典
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 08:57:06
 */
@Data
@ApiModel(value = "数据字典")
public class DictionaryDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Integer id;
	//名称
	@ApiModelProperty(value = "名称")
	private String name;
	//值
	@ApiModelProperty(value = "值")
	private String value;
	//
	@ApiModelProperty(value = "上级ID")
	private Long parentId;
	//类型
	@ApiModelProperty(value = "类型")
	private Long typeId;
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
