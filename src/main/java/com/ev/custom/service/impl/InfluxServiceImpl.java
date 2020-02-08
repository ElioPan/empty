package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.utils.DateUtils;
import com.ev.framework.utils.InfluxDbUtils;
import com.ev.custom.domain.MeasurePointDO;
import com.ev.custom.service.InfluxService;
import com.ev.custom.service.MeasurePointService;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InfluxServiceImpl implements InfluxService {
    @Autowired
    private MeasurePointService measurePointService;
    @Override
    public JSONArray list(String startTime, String endTime, String timeInterval, String convergenceMode, Long[] pointIds) throws ParseException {
        if(startTime.indexOf(":")<0){
            startTime = startTime+" 00:00:00";
        }
        if(endTime.indexOf(":")<0){
            endTime = endTime+" 23:59:59";
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray datasMap = new JSONArray();
        String selectSql = "SELECT time,point,value FROM device_electricity where time < '" + df.format(new Date())+"'" ;
        if(StringUtils.isNotEmpty(startTime)){
            selectSql+=" and time >= '" + startTime + "'";
        }
        if(StringUtils.isNotEmpty(endTime)){
            selectSql+=" and time <= '" + endTime + "'";
        }
        if(pointIds.length>0){
            for(int i=0; i<pointIds.length;i++){
                String selectSqlCommit = selectSql + " and point = '" + pointIds[i]+"'";
                selectSqlCommit+= " order by time asc tz('Asia/Shanghai')";
                JSONArray dataArray = this.getDataAray(selectSqlCommit);
                JSONObject resultObj = new JSONObject();
                resultObj.put("pointId",pointIds[i].toString());
                MeasurePointDO point = measurePointService.get(pointIds[i]);
                resultObj.put("pointName",point==null?"-":point.getName());
                resultObj.put("datas",converData(dataArray, startTime, timeInterval, convergenceMode));
                datasMap.add(resultObj);
            }

        }
        return datasMap;
    }

    /**
     *
     * @param selectSql
     * @return
     */
    private JSONArray getDataAray(String selectSql){
        InfluxDbUtils influxDB = InfluxDbUtils.setUp();
        System.out.println(DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss SSS"));
        QueryResult result = influxDB.query(selectSql);
        System.out.println(DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss SSS"));
        JSONArray dataArray = new JSONArray();
        if(result.getResults().get(0).getSeries() == null){
            return dataArray;
        }
        List<String> columns = result.getResults().get(0).getSeries().get(0).getColumns();
        List<List<Object>> datas = result.getResults().get(0).getSeries().get(0).getValues();
        for(List<Object> data : datas){
            JSONObject obj = new JSONObject();
            for(int i = 0; i< data.size();i++){
                if("time".equals(columns.get(i))){
                    String newTime = data.get(i).toString().substring(0,19).replace("T"," ");
                    obj.put(columns.get(i),newTime);
                }else{
                    obj.put(columns.get(i),data.get(i));
                }
            }
            dataArray.add(obj);
        }
        return dataArray;
    }

    /**
     * 根据规则对数据进行聚合
     * @param dataArray 源数据
     * @param timeInterval 秒
     * @param convergenceMode (0：首值；1：末值；2：平均值；3：最大值；4：最小值)
     * @return
     */
    private JSONArray converData(JSONArray dataArray, String startTime, String timeInterval, String convergenceMode) throws ParseException {
        /**
         * 1.按照时间长度进行分组
         */
        JSONArray converArray = new JSONArray();
        if(Integer.parseInt(timeInterval)>1){
            String initTime = startTime;
            String addedTime = addTime(startTime,Integer.parseInt(timeInterval)-1);
            JSONArray datas = new JSONArray();
            for(int i=0; i<dataArray.size();i++){
                JSONObject obj = (JSONObject)dataArray.get(i);
                String time = obj.get("time").toString();
                if(time.compareTo(addedTime) <= 0){
                    datas.add(obj);
                    if(i==dataArray.size()-1){
                        JSONObject converObj = new JSONObject();
                        converObj.put("time",initTime);
                        converObj.put("values",datas);
                        converArray.add(converObj);
                    }
                }else{
                    while(time.compareTo(addedTime)> 0){
                        JSONObject converObj = new JSONObject();
                        converObj.put("time",initTime);
                        converObj.put("values",datas);
                        converArray.add(converObj);
                        datas = new JSONArray();
                        initTime = addTime(initTime,Integer.parseInt(timeInterval));
                        addedTime = addTime(initTime,Integer.parseInt(timeInterval)-1);
                    }
                }
            }
        }else{
            for(int i=0; i<dataArray.size();i++){
                JSONArray datas = new JSONArray();
                JSONObject obj = (JSONObject)dataArray.get(i);
                String time = obj.get("time").toString();
                datas.add(obj);
                JSONObject converObj = new JSONObject();
                converObj.put("time",time);
                converObj.put("values",datas);
                converArray.add(converObj);
            }
        }

        /**
         * 2.对已经分组好的数据按照聚合规则聚合
         */
        JSONArray result = converData(converArray,convergenceMode);
        return result;
    }

    /**
     * 对已经分组好的数据按照聚合规则聚合
     * @param converArray
     * @param convergenceMode
     * @return
     */
    private JSONArray converData(JSONArray converArray, String convergenceMode ){
        JSONArray resultArray = new JSONArray();
        for(int i=0; i< converArray.size();i++){
            String time = ((JSONObject)converArray.get(i)).get("time").toString();
            JSONArray array = (JSONArray) ((JSONObject)converArray.get(i)).get("values");
             if("1".equals(convergenceMode)){
                JSONObject object = new JSONObject();
                object.put("time",time);
                object.put("value",array.size()==0?"0":((JSONObject)array.get(array.size()-1)).get("value"));
                resultArray.add(object);
            }else if ("2".equals(convergenceMode)){
                JSONObject object = new JSONObject();
                BigDecimal valueSum = BigDecimal.ZERO;
                for(int j = 0; j< array.size();j++){
                    valueSum = valueSum.add(BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value")));
                }
                object.put("time",time);
                object.put("value",array.size()==0?0:valueSum.divide(BigDecimal.valueOf(array.size()),2,BigDecimal.ROUND_HALF_UP));
                 resultArray.add(object);
            }else if ("3".equals(convergenceMode)){
                JSONObject object = new JSONObject();
                BigDecimal valueMax = BigDecimal.ZERO;
                for(int j = 0; j< array.size();j++){
                    if(j==0){
                        valueMax = BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value"));
                    }else{
                        if(valueMax.compareTo(BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value")))<0){
                            valueMax = BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value"));
                        }
                    }
                }
                object.put("time",time);
                object.put("value",valueMax);
                 resultArray.add(object);
            }else if("4".equals(convergenceMode)){
                JSONObject object = new JSONObject();
                BigDecimal valueMin = BigDecimal.ZERO;
                for(int j = 0; j< array.size();j++){
                    if(j==0){
                        valueMin = BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value"));
                    }else{
                        if(valueMin.compareTo(BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value")))>0){
                            valueMin = BigDecimal.valueOf(((JSONObject)array.get(j)).getDouble("value"));
                        }
                    }
                }
                object.put("time",time);
                object.put("value",valueMin);
                 resultArray.add(object);
            }else{
                JSONObject object = new JSONObject();
                object.put("time",time);
                object.put("value",array.size()==0?"0":((JSONObject)array.get(0)).get("value"));
                resultArray.add(object);
            }
        }
        return resultArray;
    }

    /**
     * 时间添加时间间隔，获取新的时间
     * @param time
     * @param addSeconds
     * @return
     * @throws ParseException
     */
    private String addTime(String time,int addSeconds) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =df.parse(time);
        date.setTime(date.getTime() + addSeconds*1000);
        return df.format(date);
    }

    public static void main(String[] args){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date =df.parse("2019-11-29T13:07:02.781Z");
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
