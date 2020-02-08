package com.ev.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ev")
public class ConstantsConfig {
	//上传路径
	private String uploadPath;

	private String tmpPath;

	private String excelPath;

	private String picPath;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

	public String getExcelPath() {
		return excelPath;
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
