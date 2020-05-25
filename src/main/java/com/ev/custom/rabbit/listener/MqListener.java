package com.ev.custom.rabbit.listener;

import com.ev.custom.service.NoticeService;
import com.ev.custom.service.WeChatService;
import com.ev.custom.vo.NoticeEntity;
import com.ev.framework.socket.CusWebSocket;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Component
public class MqListener {
    private static final Logger log= LoggerFactory.getLogger(MqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private WeChatService weChatService;

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
            if(!StringUtils.isEmpty(dataMap.get("value").toString().trim())){
                try{
                    Map<String, String> tags = new HashMap<String, String>();
                    tags.put("point", dataMap.get("pointId").toString());
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put("value", Double.parseDouble(dataMap.get("value").toString().trim()));
                    values.put("valueRecord", Double.parseDouble(dataMap.get("valueRecord").toString().trim()));
                    Point point = influxDB.pointBuilder(measurement, tags, values,dataMap.get("time").toString());
                    BatchPoints batchPoint = BatchPoints.database(measurement).consistency(InfluxDB.ConsistencyLevel.ALL).build();
                    batchPoint.point(point);
                    records.add(batchPoint.lineProtocol());
                }catch (Exception e){
                    continue;
                }
            }

        }
        // 将两条数据批量插入到数据库中
        influxDB.batchInsert("sentinel_log", null, InfluxDB.ConsistencyLevel.ALL, records);
    }

    @RabbitListener(queues = "gyhl.data.notice.queue",containerFactory = "singleListenerContainer")
    public void consumeNoticeQueue(@Payload byte[] message){
        try {
            NoticeEntity notice=objectMapper.readValue(message, NoticeEntity.class);
            saveAndSendNotice(notice);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送微信卡片消息并发送websocket消息
     */
    private void saveAndSendNotice(NoticeEntity notice) throws IOException, ParseException {
        weChatService.sendTextCardMessage(notice.getNoticeDO(),notice.getToUserList());
        for(Long toUserId: notice.getToUserList()){
            CusWebSocket.sendInfo(notice.toString(),toUserId.toString());
        }
    }
}
