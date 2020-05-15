package com.ev.common.jobs;/**
 * @Author xy
 * @Date 2020/5/15 12:31
 * @Description
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.mqtt.CacheItem;
import com.ev.custom.mqtt.CachePool;
import com.ev.custom.service.MeasurePointService;
import com.ev.framework.config.ApplicationContextRegister;
import com.ev.framework.utils.InfluxDbUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名称： com.ev.common.jobs.InfluxJob.java</br>
 * 初始作者： xy</br>
 * 创建日期： 2020/5/15 12:31</br>
 * 功能说明： TODO <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者        日期       修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2020-2021 .All rights reserved.<br/>
 */
@Component
public class InfluxJob {
    @Scheduled(fixedRate = 10000)
    private void executeInflux() {
        List<String> keyList = new ArrayList<>();
        JSONArray dataList = new JSONArray();
        MeasurePointService measurePointService = ApplicationContextRegister.getBean(MeasurePointService.class);
        Map<String,Object> dataMap = CachePool.getInstance().getAllCacheItems();
        List<Map<String,Object>> measurePointDOList = measurePointService.listForMap(null);
        for(Map.Entry<String, Object> entry : dataMap.entrySet()){
            String time = entry.getKey();
            CacheItem cacheItem = (CacheItem)entry.getValue();
            JSONObject value = JSONObject.parseObject(cacheItem.getEntity().toString());
            String gatew = value.get("gatew").toString();
            for(String str:value.keySet()){
                for(Map<String,Object> map : measurePointDOList){
                    if("gatew".equals(str)){
                        break;
                    }else{
                        if(gatew.equals(map.get("gatewayNo").toString()) && str.equals(map.get("serialNo").toString())){
                            JSONObject dataObj = new JSONObject();
                            dataObj.put("time",time);
                            dataObj.put("pointId",map.get("id").toString());
                            dataObj.put("value",value.get(str).toString());
                            dataList.add(dataObj);
                            break;
                        }
                    }
                }
            }
            keyList.add(time);
        }
        saveInfluxData(dataList,"device_electricity");
        for(String timeStr : keyList){
            CachePool.getInstance().removeCacheItem(timeStr);
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
            Point point = influxDB.pointBuilder(measurement, tags, values,dataMap.get("time").toString());
            BatchPoints batchPoint = BatchPoints.database(measurement).consistency(InfluxDB.ConsistencyLevel.ALL).build();
            batchPoint.point(point);
            records.add(batchPoint.lineProtocol());
        }
        // 将两条数据批量插入到数据库中
        influxDB.batchInsert("sentinel_log", null, InfluxDB.ConsistencyLevel.ALL, records);
    }
}
