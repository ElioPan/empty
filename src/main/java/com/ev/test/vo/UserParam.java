package com.ev.test.vo;

import com.ev.common.domain.PageModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xy
 * @Date 2020/4/23 13:50
 * @Description
 */
@Data
public class UserParam extends PageModel {

    @ApiModelProperty(value = "用户主键", example = "1")
    private Long userId;

    @ApiModelProperty(value = "用户名", example = "张三")
    private String userName;

}
