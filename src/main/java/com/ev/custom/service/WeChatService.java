package com.ev.custom.service;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public interface WeChatService {
    public JSONObject getAccessToken(String corpid, String corpsecre, Date now) throws IOException, ParseException;

    public JSONObject getJsapiTicket(String accessToken,Date now) throws IOException, ParseException;

    public JSONObject getSignature(String corpid, String corpsecre,String url,Date now) throws IOException, ParseException;
}
