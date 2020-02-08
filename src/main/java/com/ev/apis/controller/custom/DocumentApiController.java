package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApi;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.common.service.FtpService;
import com.ev.framework.utils.R;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.DocumentDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.DocumentService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@Api(value = "/", tags = "文档管理接口")
public class DocumentApiController {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private ContentAssocService contentAssocService;

    @Autowired
    private FtpService ftpService;

    @ResponseBody
    @EvApiByToken(value = "/apis/document/list",method = RequestMethod.POST,apiTitle = "获取文档列表信息")
    @ApiOperation("获取文档列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                  @ApiParam(value = "文档名称") @RequestParam(value = "name",defaultValue = "",required = false)  String name,
                  @ApiParam(value = "文档类型") @RequestParam(value = "type",defaultValue = "",required = false)  Long type,
                  @ApiParam(value = "上传人") @RequestParam(value = "upUserName",defaultValue = "",required = false)  String upUserName){
        Map<String, Object> params = new HashMap<>();
        params.put("name",name);
        params.put("type",type);
        params.put("upUserName",upUserName);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.documentService.listForMap(params);
        int total = this.documentService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/document/detail",method = RequestMethod.POST,apiTitle = "获取文档详细信息")
    @ApiOperation("获取文档详细信息")
    public R detail(@ApiParam(value = "文档ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<>();
        results = documentService.detail(id);
        return  R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/document/save",method = RequestMethod.POST,apiTitle = "保存文档信息")
    @ApiOperation("保存文档信息")
    public R save(@ApiParam(value = "文档信息",required = true) DocumentDO document,
                  @ApiParam(value = "上传图片",required = false) @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
        try {
            documentService.add(document,taglocationappearanceImage);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/document/update",method = RequestMethod.POST,apiTitle = "编辑文档信息")
    @ApiOperation("编辑文档信息")
    public R update(@ApiParam(value = "文档信息",required = true) DocumentDO document,
                    @ApiParam(value = "添加图片服务器路径",required = false) @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage,
                    @ApiParam(value = "删除图片服务器路径",required = false) @RequestParam(value = "deletetag_appearanceImage",defaultValue = "",required = false) String[] deletetagAppearanceImage){
        try {
            documentService.edit(document,taglocationappearanceImage,deletetagAppearanceImage);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/document/remove",method = RequestMethod.POST,apiTitle = "删除文档")
    @ApiOperation("删除文档")
    public R remove(@ApiParam(value = "文档主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
        if(documentService.remove(id)>0){
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/document/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除文档")
    @ApiOperation("批量删除文档")
    public R remove(@ApiParam(value = "文档主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        documentService.batchRemove(ids);
        return R.ok();
    }

    @EvApi(value = "/apis/document/downloads",method = RequestMethod.GET,apiTitle = "多文件下载")
    @ApiOperation("单文件下载")
    public void download(@ApiParam(value = "主键", required = true) @RequestParam("id") Long id,
                         @ApiParam(value = "类型", required = true) @RequestParam("assocType") String assocType,HttpServletResponse response) throws IOException {
        //获取关联的文档资料
        DocumentDO documentDO = documentService.get(id);
        //获取附件
        Map<String, Object> contentMap = new HashMap<String, Object>() {{
            put("assocId", id);
            put("assocType", assocType);
            put("sort","id");
            put("order","ASC");
        }};
        List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
        File zipFile = new File(documentDO.getName());
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        byte[] buffer = new byte[1024];
        for(ContentAssocDO contentAssocDO: contentAssocDOS){
            ZipEntry entry = new ZipEntry(contentAssocDO.getFileName());
            zipOut.putNextEntry(entry);
/*            response.setContentType("text/xml");
            response.setHeader("Content-Disposition", "attachment;filename="+contentAssocDO.getFileName());*/
            InputStream is = ftpService.downFile(contentAssocDO.getFilePath(),contentAssocDO.getFileName());
            int i = -1;
            while ((i = is.read(buffer)) != -1) {
                zipOut.write(buffer, 0, i);
            }
            zipOut.closeEntry();
            is.close();
        }
        zipOut.close();

        int len;
        FileInputStream zipInput =new FileInputStream(zipFile);
        OutputStream out = response.getOutputStream();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename="
                + URLEncoder.encode(documentDO.getName(), "UTF-8") + ".zip");
        while ((len=zipInput.read(buffer))!= -1){
            out.write(buffer,0,len);
        }
        zipInput.close();
        out.flush();
        out.close();
        //删除压缩包
        zipFile.delete();
    }
}
