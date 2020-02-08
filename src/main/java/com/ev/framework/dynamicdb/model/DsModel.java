package com.ev.framework.dynamicdb.model;

public class DsModel {
    private String sqlType;
    private String dsName;
    private String url;
    private String user;
    private String password;
    private String driver;

    public DsModel(String sqlType, String dsName, String url, String user, String password, String driver) {
        this.sqlType = sqlType;
        this.dsName = dsName;
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
}
