package com.ev.framework.utils;

import com.ev.framework.fastdfs.FastDFSClient;
import com.ev.framework.fastdfs.FastDFSFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtil {

	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String renameToUUID(String fileName) {
		return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	public static String saveFile(MultipartFile multipartFile) throws IOException {
		String[] fileAbsolutePath={};
		String fileName=multipartFile.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
		byte[] file_buff = null;
		InputStream inputStream=multipartFile.getInputStream();
		if(inputStream!=null){
			int len1 = inputStream.available();
			file_buff = new byte[len1];
			inputStream.read(file_buff);
			inputStream.close();
		}
		FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
		try {
			fileAbsolutePath = FastDFSClient.upload(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileAbsolutePath==null) {
			System.out.println("upload file failed,please upload again!!!!!!");
		}
		String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
		return path;
	}
}
