package com.ev.common.service;

import com.ev.common.domain.FileDO;
import com.ev.framework.utils.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
public interface ImportUserService {
    R importExcelForUser(MultipartFile file);
}
