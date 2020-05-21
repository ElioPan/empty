package com.ev.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.xml.internal.rngom.parse.host.Base;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class SoftWareDeleteEntity extends BaseEntity {
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "删除标志 默认0")
    @TableLogic
    private Integer delFlag = 0;
}
