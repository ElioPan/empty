package com.ev.common.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FtpBean {
    //获取ip地址
    private String address;
    //端口号
    private String port;
    //用户名
    private String username;
    //密码
    private String password;
    //基本路
    private String basePath;
    //本地路径
    private String localPath;
    //文件名称
    private String fileName;
    //文件输入流
    private InputStream inputStream;
    public String getAddress() {
        return address == null ? "127.0.0.1":address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPort() {
        return port == null ? "21":port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getUsername() {
        return username == null ? "maple":username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password == null ?"xxxxxxx":password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getLocalPath() {
        return localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    public String getBasePath() {
        return basePath;
    }
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
