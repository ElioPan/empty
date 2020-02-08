package com.ev.custom.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DeptEntity {
    public DeptEntity(){}

    public DeptEntity(String name){
        this.name = name;
    }

    @Excel(name = "需求部门标记数据来源部门")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
