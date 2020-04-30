package com.ev.hr.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 员工信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:42
 */
@Data
@ApiModel(value = "员工信息表")
public class EmployeeInfoDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
    @ApiModelProperty(value = "ID")
	private Long id;
	//员工工号
    @ApiModelProperty(value = "员工工号")
	private String employeeCode;
	//员工姓名
    @ApiModelProperty(value = "员工姓名")
	private String employeeName;
	//部门id
    @ApiModelProperty(value = "部门id")
	private Long deptId;
	//岗 位
    @ApiModelProperty(value = "岗 位")
	private Long employeePost;
	//用工状态
    @ApiModelProperty(value = "用工状态")
	private Long employmentState;
	//用工类型
    @ApiModelProperty(value = "用工类型")
	private Long employmentType;
	//身份证号
    @ApiModelProperty(value = "身份证号")
	private String idNumber;
	//职称
    @ApiModelProperty(value = "职称")
	private String employeeTitle;
	//职级
    @ApiModelProperty(value = "职级")
	private String employeeLevel;
	//手机号码
    @ApiModelProperty(value = "手机号码")
	private String mobliePhone;
	//出生日期
    @ApiModelProperty(value = "出生日期")
	private Date employeeBirthday;
	//性别(1男0女 )
    @ApiModelProperty(value = "性别(1男0女 )")
	private Integer employeeGender;
	//最高学历
    @ApiModelProperty(value = "最高学历")
	private Long highestEducation;
	//毕业学校
    @ApiModelProperty(value = "毕业学校")
	private String graduateChool;
	//所学专业
    @ApiModelProperty(value = "所学专业")
	private String majorStudied;
	//民族
    @ApiModelProperty(value = "民族")
	private String employeeNation;
	//婚姻状况(1已婚0未婚)
    @ApiModelProperty(value = "婚姻状况(1已婚0未婚)")
	private Integer maritalStatus;
	//户口类型
    @ApiModelProperty(value = "户口类型")
	private Long registeredType;
	//户口所在地
    @ApiModelProperty(value = "户口所在地")
	private String registeredAddress;
	//籍贯
    @ApiModelProperty(value = "籍贯")
	private String nativPlace;
	//政治面貌
    @ApiModelProperty(value = "政治面貌")
	private Long politicalOutlook;
	//入党时间
    @ApiModelProperty(value = "入党时间")
	private Date joiningPartyDate;
	//个人社保号
    @ApiModelProperty(value = "个人社保号")
	private String socialNumber;
	//公积金帐号
    @ApiModelProperty(value = "公积金帐号")
	private String providentAccount;
	//工资卡号
    @ApiModelProperty(value = "工资卡号")
	private String salaryCard;
	//个人新职工住房补贴账号
    @ApiModelProperty(value = "个人新职工住房补贴账号")
	private String subsidyAccount;
	//合同签订单位
    @ApiModelProperty(value = "合同签订单位")
	private String signingUnit;
	//
    @ApiModelProperty(value = "")
	private Long headPortrait;
	//岗位说明书
    @ApiModelProperty(value = "岗位说明书")
	private Long positionDescription;
	//办公电话
    @ApiModelProperty(value = "办公电话")
	private String officePhone;
	//电子邮件
    @ApiModelProperty(value = "电子邮件")
	private String email;
	//邮政编码
    @ApiModelProperty(value = "邮政编码")
	private String postalCode;
	//通信/家庭地址
    @ApiModelProperty(value = "通信/家庭地址")
	private String address;
	//通信地址
    @ApiModelProperty(value = "通信地址")
	private String communicationAddress;
	//微信号
    @ApiModelProperty(value = "微信号")
	private String wechatNumber;
	//入职来源
    @ApiModelProperty(value = "入职来源")
	private String entrySourse;
	//入职日期
    @ApiModelProperty(value = "入职日期")
	private Date entryDate;
	//试用期(月)
    @ApiModelProperty(value = "试用期(月)")
	private Integer probationPeriod;
	//转正日期
    @ApiModelProperty(value = "转正日期")
	private Date formalDate;
	//离退休日期
    @ApiModelProperty(value = "离退休日期")
	private Date retireDate;
	//离职原因
    @ApiModelProperty(value = "离职原因")
	private String quitReason;
	//离职日期
    @ApiModelProperty(value = "离职日期")
	private Date quitDate;
	//公司服务年限(年)
    @ApiModelProperty(value = "公司服务年限(年)")
	private Integer companyServiceYear;
	//参加工作日期
    @ApiModelProperty(value = "参加工作日期")
	private Date joiningWorkDate;
	//社会工龄
    @ApiModelProperty(value = "社会工龄")
	private Integer serviceAge;
	//创建人
    @ApiModelProperty(value = "创建人")
	private String createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createDate;
	//修改人
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateDate;

}
