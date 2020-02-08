package com.ev.common.service.impl;

import com.ev.common.domain.FtpBean;
import com.ev.common.service.FtpService;
import com.ev.framework.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FtpServiceImpl implements FtpService {

    //获取ip地址
    @Value("${org.ftp.address}")
    private String address;
    //端口号
    @Value("${org.ftp.port}")
    private String port;
    //用户名
    @Value("${org.ftp.userName}")
    private String userName;
    //密码
    @Value("${org.ftp.password}")
    private String password;
    //基本路
    @Value("${org.ftp.basePath}")
    private String basePath;
    //本地路径
    @Value("${org.ftp.localPath}")
    private String localPath;

    @Override
    public String uploadFiles(MultipartFile file) {
        FtpBean ftp = new FtpBean();
        ftp.setAddress(this.address);
        ftp.setPort(this.port);
        ftp.setUsername(this.userName);
        ftp.setPassword(this.password);
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
        String today = ymd.format(new Date());
        ftp.setBasePath(this.basePath+ "/"+today);

        String uuid = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = uuid +"."+suffix;
        ftp.setFileName(newFileName);

        try{
            //将文件转换成一个输入流
            InputStream in = file.getInputStream();
            ftp.setInputStream(in);
            //传入文件名称，和文件输入流，上传至FTP服务器
            boolean isOk = FtpUtil.uploadFile(ftp);
            if(isOk){
                System.out.println("文件上传成功");
            }else{
                System.out.println("文件上传失败");
            }
            in.close();
            return ftp.getBasePath()+ "/"+newFileName;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream downFile(String ftpPath, String fileName) {
        FtpBean ftp = new FtpBean();
        ftp.setAddress(this.address);
        ftp.setPort(this.port);
        ftp.setUsername(this.userName);
        ftp.setPassword(this.password);
        ftp.setPassword(this.password);
        ftp.setLocalPath(this.localPath);
        try{
            return FtpUtil.downloadFile(ftp,ftpPath,fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
