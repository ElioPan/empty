package com.ev.framework.utils;

import java.io.File;

public class UploadUtils {

    // 判断文件是否存在
    public static boolean judeFileExists(File file) {
        boolean flag;
        if (file.exists()) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
