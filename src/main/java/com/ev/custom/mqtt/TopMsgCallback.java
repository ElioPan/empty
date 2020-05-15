package com.ev.custom.mqtt;/**
 * @Author xy
 * @Date 2020/5/13 13:46
 * @Description
 */

import com.ev.common.service.MqttService;
import com.ev.common.service.SocketService;
import com.ev.framework.config.ApplicationContextRegister;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 文件名称： com.ev.mqtt.TopMsgCallback.java</br>
 * 初始作者： xy</br>
 * 创建日期： 2020/5/13 13:46</br>
 * 功能说明： TODO <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者        日期       修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2020-2021 .All rights reserved.<br/>
 */
public class TopMsgCallback implements MqttCallback {
    private static Logger logger= LoggerFactory.getLogger(TopMsgCallback.class);
    private MqttClient client;
    private MqttConnectOptions options;
    private String[] topic;
    private int[] qos;

    public TopMsgCallback() {}

    public TopMsgCallback(MqttClient client, MqttConnectOptions options,String[] topic,int[] qos) {
        this.client = client;
        this.options = options;
        this.topic=topic;
        this.qos=qos;
    }


    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("MQTT连接断开，发起重连");
        while(true) {
            try {
                Thread.sleep(30000);
                client.connect(options);
                //订阅消息
                client.subscribe(topic,qos);
                logger.info("MQTT重新连接成功:"+client);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @Override
    public void messageArrived(String topicStr, MqttMessage mqttMessage) throws Exception {
        System.out.println();
        //订阅消息字符
        String msg=new String(mqttMessage.getPayload());
        if(topicStr.equals("v10/devices/me/telemetry")){
            CachePool.getInstance().putCacheItem(String.valueOf(System.currentTimeMillis()),msg);
        }
        logger.info("topic:"+topicStr);
        logger.info("msg:"+msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    //对象转化为字节码
    public  byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }
}
