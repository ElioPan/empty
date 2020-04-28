package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工序配置
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:45:29
 */
@Data
@ApiModel(value = "工序配置")
public class ProcessDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @ApiModelProperty()
    private Long id;
    //代码
    @ApiModelProperty(value = "代码")
    private String code;
    //名字
    @ApiModelProperty(value = "名字")
    private String name;
    //基准良率%
    @ApiModelProperty(value = "基准良率%")
    private BigDecimal standard;
    //工序类型
    @ApiModelProperty(value = "工序类型")
    private Long type;
	//工序类型
	@ApiModelProperty(value = "设备类型")
	private Long deviceType;
    //单件工时
    @ApiModelProperty(value = "单件工时")
    private BigDecimal manHour;
    //单件工价
    @ApiModelProperty(value = "单件工价")
    private BigDecimal labourPrice;
    //生产部门
    @ApiModelProperty(value = "生产部门")
    private Long deptId;
    //操作工
    @ApiModelProperty(value = "操作工")
    private Long operator;
    //工艺要求
    @ApiModelProperty(value = "工艺要求")
    private String demand;
    //是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
    private Integer whetherExamine;
    //是否自动采集   1是 0否
    @ApiModelProperty(value = "是否自动采集   1是 0否")
    private Integer whetherCollect;
    //生产设备
    @ApiModelProperty(value = "生产设备")
    private Long deviceId;
    //检验方案id
    @ApiModelProperty(value = "检验方案id")
    private Long checkPlanId;
	//备注
	@ApiModelProperty(value = "备注")
	private String remark;
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
    @ApiModelProperty(value = "审核人")
    private Long auditId;
    @ApiModelProperty(value = "审核状态")
    private Long auditSign;
    @ApiModelProperty(value = "使用状态   1是 0否")
    private Integer useStatus;


}
