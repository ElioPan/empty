package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-04-15 15:23:52
 */
@Data
@ApiModel(value = "")
public class SettingDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//配置类型
    @ApiModelProperty(value = "配置类型")
	private String key;
	//配置内容
    @ApiModelProperty(value = "配置内容")
	private String value;
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
