package com.ev.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @Author xy
 * @Date 2020/5/14 16:59
 * @Description
 */
public interface MqttService {
    public void receiveMqttMessage(String topic, String message) throws JsonProcessingException;
}
