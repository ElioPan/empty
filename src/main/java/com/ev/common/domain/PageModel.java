package com.ev.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件名称： com.socks.zhhc.core.common.page.PageModel.java</br>
 * 初始作者： Mark.Yao</br>
 * 创建日期： 2019年9月10日</br>
 * 功能说明： 分页公共实体 <br/>
 * <p>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2019-2020 .All rights reserved.<br/>
 */
@ApiModel
@Data
public class PageModel implements Serializable {

    /**
     * 字段描述: [序列化编号]
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer currentPage = 1;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;

}
