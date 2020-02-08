package com.ev.framework.utils;

import com.ev.common.domain.FtpBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FtpUtil {
    public HttpServletResponse _response;

    public static boolean uploadFile(FtpBean ftpBean) {

        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            ftp = connect(ftpBean);
            String tempPath = ftpBean.getBasePath();
            String fileName = ftpBean.getFileName();
            if (!ftp.changeWorkingDirectory(ftpBean.getBasePath())) {
                //判断目录是否存在，如果目录不存在创建目录，目录存在则跳转到此目录下
                String []tempPathList = tempPath.split("/");
                for (String dir : tempPathList) {
                    if(dir != null && dir != ""){
                        if (!ftp.changeWorkingDirectory(dir)) {
                            if (!ftp.makeDirectory(dir)) {
                                return result;
                            } else {
                                ftp.changeWorkingDirectory(dir);
                            }
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //设置模式很重要
            ftp.enterLocalPassiveMode();
            //上传文件
            result = ftp.storeFile(fileName, ftpBean.getInputStream());
            if(!result){
                return result;
            }

            ftpBean.getInputStream().close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 下载ftp文件
     * @param ftpBean
     * @param pathName
     * @param fileName
     * @return
     */
    public static InputStream downloadFile(FtpBean ftpBean, String pathName, String fileName){
        FTPClient ftp = new FTPClient();
        boolean flag = false;
        OutputStream os=null;
        InputStream in = null;
        try {
            ftp = connect(ftpBean);
            //切换FTP目录
            String path = pathName.substring(0,pathName.lastIndexOf('/'));
            String name = pathName.substring(pathName.lastIndexOf('/')+1,pathName.length());
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(path);
            FTPFile[] ftpFiles = ftp.listFiles();
            for(FTPFile file : ftpFiles){
                if(name.equalsIgnoreCase(file.getName())){
                    in = ftp.retrieveFileStream(name);
                }
            }
            ftp.logout();
            return in;
        } catch (Exception e) {
            System.out.println("下载文件失败");
            e.printStackTrace();
        } finally{
            if(ftp.isConnected()){
                try{
                    ftp.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return in;
    }

    private static FTPClient connect(FtpBean ftpBean){
        FTPClient ftp = new FTPClient();
        int reply;
        // 连接FTP服务器
        try {
            ftp.connect(ftpBean.getAddress(), Integer.valueOf(ftpBean.getPort()));
            // 登录FTP服务器
            ftp.login(ftpBean.getUsername(), ftpBean.getPassword());
            //获取应答Code
            reply = ftp.getReplyCode();
            //判断是否连接正常
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ftp;
    }
}
