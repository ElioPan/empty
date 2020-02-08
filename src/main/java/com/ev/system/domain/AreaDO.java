package com.ev.system.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 2018年最新行政区划数据库-旗舰版
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:08:25
 */
@ApiModel(value = "2018年最新行政区划数据库-旗舰版")
@JsonInclude(content = Include.NON_EMPTY) 
public class AreaDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Integer cityId;
	//
    @ApiModelProperty(value = "")
	private Integer parentId;
	//
    @ApiModelProperty(value = "")
	private Integer levelType;
	//
    @ApiModelProperty(value = "")
	private String cityName;
	//
    @ApiModelProperty(value = "")
	private String shortName;
	//
    @ApiModelProperty(value = "")
	private String parentPath;
	//
    @ApiModelProperty(value = "")
	private String province;
	//
    @ApiModelProperty(value = "")
	private String city;
	//
    @ApiModelProperty(value = "")
	private String district;
	//
    @ApiModelProperty(value = "")
	private String provinceShortName;
	//
    @ApiModelProperty(value = "")
	private String cityShortName;
	//
    @ApiModelProperty(value = "")
	private String districtShortName;
	//
    @ApiModelProperty(value = "")
	private String provincePinyin;
	//
    @ApiModelProperty(value = "")
	private String cityPinyin;
	//
    @ApiModelProperty(value = "")
	private String districtPinyin;
	//
    @ApiModelProperty(value = "")
	private String pinyin;
	//
    @ApiModelProperty(value = "")
	private String jianpin;
	//
    @ApiModelProperty(value = "")
	private String firstChar;
	//
    @ApiModelProperty(value = "")
	private String cityCode;
	//
    @ApiModelProperty(value = "")
	private String zipCode;
	//
    @ApiModelProperty(value = "")
	private Integer lng;
	//
    @ApiModelProperty(value = "")
	private Integer lat;
	//
    @ApiModelProperty(value = "")
	private String remark1;
	//
    @ApiModelProperty(value = "")
	private String remark2;

	/**
	 * 设置：
	 */
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	/**
	 * 获取：
	 */
	public Integer getCityId() {
		return cityId;
	}
	/**
	 * 设置：
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：
	 */
	public Integer getParentId() {
		return parentId;
	}
	/**
	 * 设置：
	 */
	public void setLevelType(Integer levelType) {
		this.levelType = levelType;
	}
	/**
	 * 获取：
	 */
	public Integer getLevelType() {
		return levelType;
	}
	/**
	 * 设置：
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * 获取：
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * 设置：
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * 获取：
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置：
	 */
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	/**
	 * 获取：
	 */
	public String getParentPath() {
		return parentPath;
	}
	/**
	 * 设置：
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * 获取：
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * 设置：
	 */
	public void setProvinceShortName(String provinceShortName) {
		this.provinceShortName = provinceShortName;
	}
	/**
	 * 获取：
	 */
	public String getProvinceShortName() {
		return provinceShortName;
	}
	/**
	 * 设置：
	 */
	public void setCityShortName(String cityShortName) {
		this.cityShortName = cityShortName;
	}
	/**
	 * 获取：
	 */
	public String getCityShortName() {
		return cityShortName;
	}
	/**
	 * 设置：
	 */
	public void setDistrictShortName(String districtShortName) {
		this.districtShortName = districtShortName;
	}
	/**
	 * 获取：
	 */
	public String getDistrictShortName() {
		return districtShortName;
	}
	/**
	 * 设置：
	 */
	public void setProvincePinyin(String provincePinyin) {
		this.provincePinyin = provincePinyin;
	}
	/**
	 * 获取：
	 */
	public String getProvincePinyin() {
		return provincePinyin;
	}
	/**
	 * 设置：
	 */
	public void setCityPinyin(String cityPinyin) {
		this.cityPinyin = cityPinyin;
	}
	/**
	 * 获取：
	 */
	public String getCityPinyin() {
		return cityPinyin;
	}
	/**
	 * 设置：
	 */
	public void setDistrictPinyin(String districtPinyin) {
		this.districtPinyin = districtPinyin;
	}
	/**
	 * 获取：
	 */
	public String getDistrictPinyin() {
		return districtPinyin;
	}
	/**
	 * 设置：
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	/**
	 * 获取：
	 */
	public String getPinyin() {
		return pinyin;
	}
	/**
	 * 设置：
	 */
	public void setJianpin(String jianpin) {
		this.jianpin = jianpin;
	}
	/**
	 * 获取：
	 */
	public String getJianpin() {
		return jianpin;
	}
	/**
	 * 设置：
	 */
	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}
	/**
	 * 获取：
	 */
	public String getFirstChar() {
		return firstChar;
	}
	/**
	 * 设置：
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * 获取：
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * 设置：
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * 获取：
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * 设置：
	 */
	public void setLng(Integer lng) {
		this.lng = lng;
	}
	/**
	 * 获取：
	 */
	public Integer getLng() {
		return lng;
	}
	/**
	 * 设置：
	 */
	public void setLat(Integer lat) {
		this.lat = lat;
	}
	/**
	 * 获取：
	 */
	public Integer getLat() {
		return lat;
	}
	/**
	 * 设置：
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	/**
	 * 获取：
	 */
	public String getRemark1() {
		return remark1;
	}
	/**
	 * 设置：
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	/**
	 * 获取：
	 */
	public String getRemark2() {
		return remark2;
	}
}
