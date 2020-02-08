package com.ev.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FtpService {
    public String uploadFiles(MultipartFile file);

    public InputStream downFile(String ftpPath, String fileName);
}
