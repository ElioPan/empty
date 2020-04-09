package com.ev.common.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BackupsDBJob {
    @Autowired
    Environment env;


//    @Scheduled(cron="0/59 * * * * ? ")
//    private void executeDoJobs() {
//        try {
//            executeInternal();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    protected void executeInternal()throws Exception {

        String filePath=env.getProperty("backups.db.filePath");
        String url=env.getProperty("backups.db.dbUrl");
        String dbName=env.getProperty("backups.db.dbName");
        String username=env.getProperty("backups.db.dbUsername");
        String password=env.getProperty("backups.db.dbPassword");

        File uploadDir = new File(filePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        StringBuilder cmds =new StringBuilder();
        cmds.append("cmd /c mysqldump")
            .append(" -u")
            .append(username)
            .append(" -p")
            .append(password)
            .append(" -h")
            .append(url)
            .append(" ")
            .append(dbName)
            .append(" > ")
            .append(filePath)
            .append("\\")
            .append(dbName+new java.util.Date().getTime())
            .append(".sql");
        try {
            Process process = Runtime.getRuntime().exec(cmds.toString());
//            System.out.println("备份数据库成功!!!"+new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
