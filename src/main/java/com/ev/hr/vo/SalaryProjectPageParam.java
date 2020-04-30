package com.ev.hr.vo;

import com.ev.common.domain.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件名称： com.ev.hr.vo.SalaryProjectPageParam.java</br>
 * 初始作者： 尹恒星</br>
 * 创建日期： 2019年11月16日</br>
 * 功能说明： 薪资项目参数 <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2019-2020 .All rights reserved.<br/>
 */
@ApiModel("薪资项目参数")
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryProjectPageParam extends PageModel {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资项目
     */
    @ApiModelProperty(value = "薪资项目")
    private String salaryItemName;
}
