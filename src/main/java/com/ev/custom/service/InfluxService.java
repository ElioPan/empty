package com.ev.custom.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public interface InfluxService {
    public JSONArray list(String startTime,String endTime,String timeInterval,String convergenceMode,Long[] pointIds) throws ParseException;
}
