package com.ev.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocketService {
    public void receiveMessage(String message) throws JsonProcessingException;
}
