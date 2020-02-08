package com.ev.custom.rabbit.listener;

import com.ev.framework.utils.InfluxDbUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MqListener {
    private static final Logger log= LoggerFactory.getLogger(MqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "${data.device.queue.name}",containerFactory = "singleListenerContainer")
    public void consumeUserLogQueue(@Payload byte[] message){
        try {
            List<LinkedHashMap> deviceDatas=objectMapper.readValue(message, ArrayList.class);
            saveInfluxData(deviceDatas,"device_electricity");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保存数据到时序数据库
     */
    private void saveInfluxData(List<LinkedHashMap> deviceDatas, String measurement) {
        InfluxDbUtils influxDB = InfluxDbUtils.setUp();
        List<String> records = new ArrayList<String>();
        for(LinkedHashMap dataMap : deviceDatas){
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
