package com.ev.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.common.service.SocketService;
import com.ev.custom.domain.DeviceDataDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketServiceImpl implements SocketService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void receiveMessage(String message) throws JsonProcessingException {
        JSONArray datas = (JSONArray) JSON.parseObject(message).get("datas");
        List<DeviceDataDO> dDoList = JSON.parseArray(datas.toJSONString(),DeviceDataDO.class);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(env.getProperty("data.device.exchange.name"));
        rabbitTemplate.setRoutingKey(env.getProperty("data.device.routing.key.name"));
        Message messageObj= MessageBuilder.withBody(objectMapper.writeValueAsBytes(dDoList)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        messageObj.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);
        rabbitTemplate.convertAndSend(messageObj);
    }
}
