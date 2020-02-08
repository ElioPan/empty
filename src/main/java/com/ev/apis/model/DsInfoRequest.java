package com.ev.apis.model;

import io.swagger.annotations.ApiModelProperty;


public class DsInfoRequest {
    @ApiModelProperty("数据源连接URL")
    private String url;
    @ApiModelProperty("数据源用户名")
    private String username;
    @ApiModelProperty("数据源密码")
    private String password;
    @ApiModelProperty("数据源驱动ID")
    private Long driverId;
    @ApiModelProperty("数据源驱动")
    private String driver;
    @ApiModelProperty("数据源类型ID")
    private Long typeId;
    @ApiModelProperty("数据源类型")
    private String type;
    @ApiModelProperty("数据源名字")
    private String name;
    @ApiModelProperty("数据源描述")
    private String descMsg;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescMsg() {
        return descMsg;
    }

    public void setDescMsg(String descMsg) {
        this.descMsg = descMsg;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
