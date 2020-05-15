package com.ev.framework.utils;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InfluxDbUtils {
    private static final Logger logger = LoggerFactory.getLogger(InfluxDbUtils.class);

    private static String openurl = "http://120.132.17.220:8086";//连接地址
    private static String username = "xinsoft";//用户名
    private static String password = "xinsoft";//密码
    private static String database = "sentinel_log";//数据库
    private InfluxDB influxDB;


    public InfluxDbUtils(String username, String password, String openurl, String database){
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
    }

    public static InfluxDbUtils setUp(){
        //创建 连接
        InfluxDbUtils influxDbUtil = new InfluxDbUtils(username, password, openurl, database);

        influxDbUtil.influxDbBuild();

        return influxDbUtil;
    }

    /**连接时序数据库；获得InfluxDB**/
    public InfluxDB influxDbBuild(){
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(openurl, username, password);
            influxDB.query(new org.influxdb.dto.Query("CREATE DATABASE " + database));
        }
        return influxDB;
    }

    /**
     * 查询
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command){
        return influxDB.query(new org.influxdb.dto.Query(command, database));
    }

    /**
     * 插入
     * @param tags 标签
     * @param fields 字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields){
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    /**
     * 批量写入测点
     *
     * @param batchPoints
     */
    public void batchInsert(BatchPoints batchPoints) {
        influxDB.write(batchPoints);
        // influxDB.enableGzip();
        // influxDB.enableBatch(2000,100,TimeUnit.MILLISECONDS);
        // influxDB.disableGzip();
        // influxDB.disableBatch();
    }

    /**
     * 批量写入数据
     *
     * @param database
     *            数据库
     * @param retentionPolicy
     *            保存策略
     * @param consistency
     *            一致性
     * @param records
     *            要保存的数据（调用BatchPoints.lineProtocol()可得到一条record）
     */
    public void batchInsert(final String database, final String retentionPolicy, final InfluxDB.ConsistencyLevel consistency,
                            final List<String> records) {
        influxDB.write(database, retentionPolicy, consistency, records);
    }

    /**
     * 构建Point(系统时间)
     * @param measurement
     * @param tags
     * @param fields
     * @param time
     * @return
     */
    public Point pointBuilder(String measurement, Map<String, String> tags, Map<String, Object> fields,String time) {
        Point point = Point.measurement(measurement).time(Long.parseLong(time),TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
        return point;
    }

    /**
     * 构建Point(指定时间)
     *
     * @param measurement
     * @param fields
     * @return
     */
    public Point pointBuilder(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point point = Point.measurement(measurement).time(System.currentTimeMillis(),TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
        return point;
    }
}
