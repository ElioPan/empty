package com.ev.apis.model;

import io.swagger.annotations.ApiModelProperty;


public class DsSqlRequest {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数据源ID")
    private Long dsId;
    @ApiModelProperty("数据源名字")
    private String dsName;
    @ApiModelProperty("sql脚本")
    private String sqlScript;
    @ApiModelProperty("字段Mapping的JSON")
    private String fieldMapping;
    @ApiModelProperty("是否有时间查询字段  0 无  1 有")
    private int hasTimeQuery;//0 无  1 有时间范围查询
    @ApiModelProperty("时间查询的字段名")
    private String timeField;//时间查询的字段名字

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getSqlScript() {
        return sqlScript;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public String getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(String fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public int getHasTimeQuery() {
        return hasTimeQuery;
    }

    public void setHasTimeQuery(int hasTimeQuery) {
        this.hasTimeQuery = hasTimeQuery;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }
}
