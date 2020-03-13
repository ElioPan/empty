package com.ev.mes.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 工艺路线导入
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-12 14:19:56
 */
@Data
public class CraftEntity {

    @Excel(name = "工艺路线代码")
    private String code;
    @Excel(name = "工艺路线名称")
    private String name;
    @Excel(name = "版本号")
    private String version;
    @Excel(name = "工序代码")
    private String processCode;
    @Excel(name = "工序名称")
    private String processName;
    @Excel(name = "工艺要求")
    private String demand;
    @Excel(name = "工序类型")
    private String typeName;
    @Excel(name = "基准良率")
    private String standard;
    @Excel(name = "生产部门")
    private String deptName;
    @Excel(name = "操作工")
    private String operatorName;
    @Excel(name = "是否自动派工")
    private String autoDispatch;
    @Excel(name = "是否委外")
    private String whetherOutsource;
    @Excel(name = "是否检验")
    private String whetherExamine;
    @Excel(name = "是否联网采集")
    private String whetherCollect;
    @Excel(name = "设备名称")
    private String deviceName;
    @Excel(name = "工作时长")
    private String totalHour;
    @Excel(name = "单件工时")
    private String manHour;
    @Excel(name = "单件工件")
    private String labourPrice;


}
