package com.ev.custom.domain;

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
 * @date 2020-03-27 15:28:16
 */
@Data
@ApiModel(value = "")
public class NoticeTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//标识
    @ApiModelProperty(value = "标识")
	private String code;
	//名称
    @ApiModelProperty(value = "名称")
	private String name;
	//路由
    @ApiModelProperty(value = "路由")
	private String route;
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
