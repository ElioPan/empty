package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-13 15:02:53
 */
@Data
@ApiModel(value = "")
public class RoleDataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Integer id;
	//角色ID
    @ApiModelProperty(value = "角色ID")
	private Long roleId;
	//所能查看的部门
    @ApiModelProperty(value = "所能查看的部门")
	private Long deptId;

}
