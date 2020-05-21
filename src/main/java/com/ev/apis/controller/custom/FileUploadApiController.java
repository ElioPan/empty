package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApi;
import com.ev.common.service.FtpService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "/", tags = "文件上传接口管理")
public class FileUploadApiController {
    @Autowired
    FtpService ftpService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @ResponseBody
    @EvApi(value = "/apis/file/upload",method = RequestMethod.POST,apiTitle = "单文件上传")
    @ApiOperation("单文件上传")
    public R upload(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect",null));
        }
        Map<String, Object> result = new HashMap<>();
        try {
            //String path=FileUtil.saveFile(file);
            String path = ftpService.uploadFiles(file);
            result.put("fileName",file.getOriginalFilename());
            result.put("filePath",path);
            return R.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(messageSourceHandler.getMessage("file.upload.error",null));
        }
    }

    @ResponseBody
    @EvApi(value = "/apis/file/uploads",method = RequestMethod.POST,apiTitle = "多文件上传")
    @ApiOperation("多文件上传")
    public R uploads(@ApiParam(value = "文件信息数组", required = true) @RequestParam("files") MultipartFile[] files){
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> pathList = new ArrayList<>();
        for(MultipartFile file: files){
            if (file.isEmpty()) {
                return R.error(messageSourceHandler.getMessage("file.nonSelect",null));
            }

            try {
                Map<String,Object> fileMap = new HashMap<>();
                String path= ftpService.uploadFiles(file);
                fileMap.put("fileName",file.getOriginalFilename());
                fileMap.put("filePath",path);
                pathList.add(fileMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.put("result",pathList);
        return R.ok(result);
    }

    @ResponseBody
    @EvApi(value = "/apis/file/uploadMulti",method = RequestMethod.POST,apiTitle = "多文件上传(2)")
    @ApiOperation("多文件上传")
    public R uploads(@ApiParam(value = "文件1", required = true) @RequestParam(value = "file1") MultipartFile file1,
                     @ApiParam(value = "文件2") @RequestParam(value="file2",required = false) MultipartFile file2,
                     @ApiParam(value = "文件3") @RequestParam(value="file3",required = false) MultipartFile file3,
                     @ApiParam(value = "文件4") @RequestParam(value="file4",required = false) MultipartFile file4,
                     @ApiParam(value = "文件5") @RequestParam(value="file5",required = false) MultipartFile file5,
                     @ApiParam(value = "文件6") @RequestParam(value="file6",required = false) MultipartFile file6,
                     @ApiParam(value = "文件7") @RequestParam(value="file7",required = false) MultipartFile file7,
                     @ApiParam(value = "文件8") @RequestParam(value="file8",required = false) MultipartFile file8,
                     @ApiParam(value = "文件9") @RequestParam(value="file9",required = false) MultipartFile file9){
        MultipartFile[] files = new MultipartFile[]{file1,file2,file3,file4,file5,file6,file7,file8,file9};
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> pathList = new ArrayList<>();
        for(MultipartFile file: files){
            if (file==null || file.isEmpty()) {
                break;
            }
            try {
                Map<String,Object> fileMap = new HashMap<>();
                String path= ftpService.uploadFiles(file);
                fileMap.put("fileName",file.getOriginalFilename());
                fileMap.put("filePath",path);
                pathList.add(fileMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.put("result",pathList);
        return R.ok(result);
    }

    @EvApi(value = "/apis/file/down",method = RequestMethod.GET,apiTitle = "单文件下载")
    @ApiOperation("单文件下载")
    public void download(@ApiParam(value = "路径", required = true) @RequestParam("filePath") String filePath,
                      @ApiParam(value = "文件名", required = true) @RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        response.addHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("UTF8"), "ISO8859-1"));
        InputStream is = ftpService.downFile(filePath,fileName);
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int i = -1;
        while ((i = is.read(buffer)) != -1) {
            os.write(buffer, 0, i);
        }
        os.flush();
        os.close();
    }
}
