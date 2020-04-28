package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-09 11:30:12
 */
@Data
@ApiModel(value = "设备表")
public class DeviceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	//设备编号
	@ApiModelProperty(value = "设备编号")
	private String serialno;
	//设备名称
	@ApiModelProperty(value = "设备名称")
	private String name;
	//设备类型
	@ApiModelProperty(value = "设备类型")
	private Long type;
	//品牌
	@ApiModelProperty(value = "品牌")
	private String factory;
	//规格型号
	@ApiModelProperty(value = "规格型号")
	private String model;
	//购买时间
	@ApiModelProperty(value = "购买时间")
	private Date buyTime;
	//启用时间
	@ApiModelProperty(value = "启用时间")
	private Date usingTime;
	//使用年限
	@ApiModelProperty(value = "使用年限")
	private Integer serviceYear;
	//部门ID
	@ApiModelProperty(value = "部门ID")
	private Long deptId;
	//负责人ID
	@ApiModelProperty(value = "负责人ID")
	private Long userId;
	//使用状况
	@ApiModelProperty(value = "使用状况")
	private Long usingStatus;
	//设备用途
	@ApiModelProperty(value = "设备用途")
	private Long deviceUse;
	//保修到期时间
	@ApiModelProperty(value = "保修到期时间")
	private Date repairEnd;
	//安装地点
	@ApiModelProperty(value = "安装地点")
	private String site;
	//备注
	@ApiModelProperty(value = "备注")
	private String desc;
	//生产商
	@ApiModelProperty(value = "生产商")
	private Long factoryId;
	//供应商
	@ApiModelProperty(value = "供应商")
	private Long supplierId;
	//服务商
	@ApiModelProperty(value = "服务商")
	private Long serviceId;
	//父设备ID
	@ApiModelProperty(value = "父设备ID")
	private Long parentId;
	//设备来源
	@ApiModelProperty(value = "设备来源")
	private Long souceId;
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
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;
	//维修责任人
	@ApiModelProperty(value = "维修责任人")
	private Long engineerId;

}
