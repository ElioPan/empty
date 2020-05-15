package com.ev.common.service.impl;/**
 * @Author xy
 * @Date 2020/5/14 16:59
 * @Description
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.common.service.MqttService;
import com.ev.custom.service.InfluxService;
import com.ev.custom.service.MeasurePointService;
import com.ev.framework.utils.InfluxDbUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名称： com.ev.common.service.impl.MqttServiceImpl.java</br>
 * 初始作者： xy</br>
 * 创建日期： 2020/5/14 16:59</br>
 * 功能说明： TODO <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者        日期       修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2020-2021 .All rights reserved.<br/>
 */
@Service
public class MqttServiceImpl implements MqttService {
    @Autowired
    MeasurePointService measurePointService;
    @Autowired
    InfluxService influxService;

    @Override
    public void receiveMqttMessage(String topic, String message) throws JsonProcessingException {
        JSONArray dataList = new JSONArray();
        JSONObject data = JSON.parseObject(message);
        if("v10/devices/me/telemetry".equals(topic)){
            List<Map<String,Object>> measurePointDOList = measurePointService.listForMap(new HashMap<String,Object>(){{put("gatewayNo",data.get("gatew"));}});
            for(String str:data.keySet()){
                for(Map<String,Object> map : measurePointDOList){
                    if("gatew".equals(str)){
                        break;
                    }else{
                        if(str.equals(map.get("serialNo").toString())){
                            JSONObject dataObj = new JSONObject();
                            dataObj.put("pointId",map.get("id").toString());
                            dataObj.put("value",data.get(str).toString());
                            dataList.add(dataObj);
                            break;
                        }
                    }
                }
            }
            saveInfluxData(dataList,"device_electricity");
        }

    }

    /**
     * 保存数据到时序数据库
     */
    private void saveInfluxData(JSONArray deviceDatas, String measurement) {
        InfluxDbUtils influxDB = InfluxDbUtils.setUp();
        List<String> records = new ArrayList<String>();
        for(int i=0;i<deviceDatas.size(); i++){
            JSONObject dataMap = (JSONObject)deviceDatas.get(i);
            Map<String, String> tags = new HashMap<String, String>();
            tags.put("point", dataMap.get("pointId").toString());
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("value", Double.parseDouble(dataMap.get("value").toString()));
            Point point = influxDB.pointBuilder(measurement, tags, values);
            BatchPoints batchPoint = BatchPoints.database(measurement).consistency(InfluxDB.ConsistencyLevel.ALL).build();
            batchPoint.point(point);
            records.add(batchPoint.lineProtocol());
        }
        // 将两条数据批量插入到数据库中
        influxDB.batchInsert("sentinel_log", null, InfluxDB.ConsistencyLevel.ALL, records);
    }
}
