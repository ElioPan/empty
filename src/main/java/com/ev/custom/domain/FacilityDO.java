package com.ev.custom.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 仓库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Data
public class FacilityDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//编号
	private String serialNo;
	//名称
	private String name;
	//备注
	private String description;
	//创建人
	private Long createBy;
	//创建时间
	private Date createTime;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//删除状态
	private Integer delFlag;
	//仓库类型
	private Integer facilityType;
    // 审核状态
    private Integer auditSign;
    // 审核状态
    private Long auditor;
}
