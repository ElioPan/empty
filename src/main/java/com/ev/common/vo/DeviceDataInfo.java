package com.ev.common.vo;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "deviceDataInfo")
public class DeviceDataInfo {
    @Column(name = "time")
    private String time;
    // 注解中添加tag = true,表示当前字段内容为tag内容
    @Column(name = "deviceId", tag = true)
    private String deviceId;
    @Column(name = "measurePoint", tag = true)
    private String measurePoint;
    @Column(name = "value")
    private String value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMeasurePoint() {
        return measurePoint;
    }

    public void setMeasurePoint(String measurePoint) {
        this.measurePoint = measurePoint;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DeviceDataInfo(String time, String deviceId, String measurePoint, String value) {
        this.time = time;
        this.deviceId = deviceId;
        this.measurePoint = measurePoint;
        this.value = value;
    }

    public DeviceDataInfo() {
    }
}
