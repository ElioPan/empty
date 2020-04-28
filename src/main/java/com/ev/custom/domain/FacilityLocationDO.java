package com.ev.custom.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 库位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Data
public class FacilityLocationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//编号
	private String serialNo;
	//名称
	private String name;
	//备注
	private String description;
	//所属仓库
	private Long facilityId;
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
    // 审核状态
    private Long auditSign;
    // 审核状态
    private Long auditor;
    // 审核状态
    private String remarks;



}
