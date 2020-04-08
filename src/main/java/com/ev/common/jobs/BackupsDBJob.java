package com.ev.common.jobs;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;

@Component
public class BackupsDBJob {


//    @Scheduled(cron="0/59 * * * * ? ")
//    private void executeDoJobs() {
//        try {
//            executeInternal();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    protected void executeInternal()throws Exception {

//        String filePath="D:\\db_backups";
//        String url="120.132.17.220";
//        String dbName="xinsoft-gyhl";
//        String username="xinsoft";
//        String password="xinsoft411";
        String filePath=disposeYamal("backups.db.filePath");
        String url=disposeYamal("backups.db.dbUrl");
        String dbName=disposeYamal("backups.db.dbName");
        String username=disposeYamal("backups.db.dbUsername");
        String password=disposeYamal("backups.db.dbPassword");

        File uploadDir = new File(filePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        StringBuilder cmds =new StringBuilder();
        cmds.append("cmd /c mysqldump").append(" -u").append(username).append(" -p").append(password).append(" -h").append(url).append(" ").append(dbName).append(" > ").append(filePath).append("\\").append(dbName+new java.util.Date().getTime()).append(".sql");
        try {
            Process process = Runtime.getRuntime().exec(cmds.toString());
//            System.out.println("备份数据库成功!!!"+new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String  disposeYamal(String str){
        Yaml yaml=new Yaml();
        InputStream in = BackupsDBJob.class.getResourceAsStream("/application-dev.yml");

        LinkedHashMap<String, Object> sourceMap = (LinkedHashMap<String, Object>) yaml.load(in);
//        System.out.println("读取到的内容："+sourceMap);
        String strResult = getString(sourceMap, str);
//        System.out.println("dbUrl："+strResult);
        return strResult;
    }

    public static String getString(LinkedHashMap<String, Object> sourceMap, String key) {
        String[] keys = key.split("[.]");
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) sourceMap.clone();
        int length = keys.length;
        Object resultValue = null;
        for (int i = 0; i < length; i++) {
            Object value = map.get(keys[i]);
            if (i < length - 1) {
                map = ((LinkedHashMap<String, Object>) value);
            } else if (value == null) {
                throw new RuntimeException("key is not exists!");
            } else {
                resultValue = value;
            }
        }
        return resultValue.toString();
    }






}
