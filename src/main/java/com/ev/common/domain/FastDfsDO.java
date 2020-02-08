package com.ev.common.domain;

public class FastDfsDO {
    private static String uploadConfigPath;

    public static String getUploadConfigPath() {
        return uploadConfigPath;
    }

    public static void setUploadConfigPath(String uploadConfigPath) {
        FastDfsDO.uploadConfigPath = uploadConfigPath;
    }
}
