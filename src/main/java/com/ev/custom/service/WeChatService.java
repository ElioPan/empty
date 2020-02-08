package com.ev.custom.service;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public interface WeChatService {
    public Map<String,Object> getSignature(String url) throws IOException, ParseException;
}
