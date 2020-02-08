package com.ev.custom.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
  设备导出
  @author ev-monitor
  @email 286600136@qq.com
  @date 2020-01-21 14:19:56
  */
@Data
public class DeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//设备编号
	@Excel(name = "设备编号")
	private String serialno;
	//设备名称
	@Excel(name = "设备名称")
	private String name;
	//设备类型
	@Excel(name = "设备类别")
	private String typeName;
	//品牌
	@Excel(name = "品牌")
	private String factory;
	//规格型号
	@Excel(name = "规格型号")
	private String model;
    //主设备
    @Excel(name = "主设备")
    private String parentDeviceName;
	//购买时间
	@Excel(name = "购置时间",format = "yyyy-MM-dd HH:mm:ss")
	private Date buyTime;
    //使用年限
    @Excel(name = "使用年限",type = 10)
    private Integer serviceYear;
    //设备来源
    @Excel(name = "设备来源")
    private String sourceName;
	//启用时间
	@Excel(name = "启用时间",format = "yyyy-MM-dd HH:mm:ss")
	private Date usingTime;
    // 使用部门
    @Excel(name = "使用部门")
    private String deptName;
	//负责人
	@Excel(name = "负责人")
	private String userName;
	//使用状况
	@Excel(name = "使用状况")
	private String usingStatusName;
	//设备用途
	@Excel(name = "设备用途")
	private String deviceUseName;
    //安装地点
    @Excel(name = "安装地点")
    private String site;
    //生产商
    @Excel(name = "生产商")
    private String factoryName;
    //供应商
    @Excel(name = "供应商")
    private String supplierName;
    //服务商
    @Excel(name = "服务商")
    private String serviceName;

}
