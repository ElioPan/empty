package com.ev.custom.domain;

import java.io.Serializable;

public class DeviceDataDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String time;

    private String pointId;

    private String value;

    private String valueRecord;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueRecord() {
        return valueRecord;
    }

    public void setValueRecord(String valueRecord) {
        this.valueRecord = valueRecord;
    }
}
