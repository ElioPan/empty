package com.ev.system.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 2018年最新行政区划数据库-旗舰版
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:08:25
 */
@Data
@ApiModel(value = "2018年最新行政区划数据库-旗舰版")
@JsonInclude(content = Include.NON_EMPTY) 
public class AreaDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long cityId;
	//
    @ApiModelProperty()
	private Long parentId;
	//
    @ApiModelProperty()
	private Integer levelType;
	//
    @ApiModelProperty()
	private String cityName;
	//
    @ApiModelProperty()
	private String shortName;
	//
    @ApiModelProperty()
	private String parentPath;
	//
    @ApiModelProperty()
	private String province;
	//
    @ApiModelProperty()
	private String city;
	//
    @ApiModelProperty()
	private String district;
	//
    @ApiModelProperty()
	private String provinceShortName;
	//
    @ApiModelProperty()
	private String cityShortName;
	//
    @ApiModelProperty()
	private String districtShortName;
	//
    @ApiModelProperty()
	private String provincePinyin;
	//
    @ApiModelProperty()
	private String cityPinyin;
	//
    @ApiModelProperty()
	private String districtPinyin;
	//
    @ApiModelProperty()
	private String pinyin;
	//
    @ApiModelProperty()
	private String jianpin;
	//
    @ApiModelProperty()
	private String firstChar;
	//
    @ApiModelProperty()
	private String cityCode;
	//
    @ApiModelProperty()
	private String zipCode;
	//
    @ApiModelProperty()
	private Integer lng;
	//
    @ApiModelProperty()
	private Integer lat;
	//
    @ApiModelProperty()
	private String remark1;
	//
    @ApiModelProperty()
	private String remark2;

	
}
