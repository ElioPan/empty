package com.ev.apis.controller.custom;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.custom.service.DeviceService;
import com.ev.custom.service.impl.ExcelDictHandlerImpl;
import com.ev.custom.vo.*;
import com.ev.framework.annotation.EvApi;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "/", tags = "导出Excel接口")
public class EasyExcelApiController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @ResponseBody
    @EvApi(value = "/apis/easyPoi/readSupplier", method = RequestMethod.POST, apiTitle = "供应商信息导入")
    @ApiOperation("供应商信息导入")
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect",null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        long start = System.currentTimeMillis();
        List<SupplierEntity> list = ExcelImportUtil.importExcel(file.getInputStream(), SupplierEntity.class, params);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println(System.currentTimeMillis() - start);
        return R.ok();
    }

    @EvApi(value = "/apis/easyPoi/downloads",method = RequestMethod.GET,apiTitle = "excelpoi做文件导出")
    @ApiOperation("excelpoi做文件导出")
    public void downloadEasyPoi(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        ClassPathResource classPathResource = new ClassPathResource("poi/supplier.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", getSupplierList());
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        params.setDictHandler(new ExcelDictHandlerImpl());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "测试文件信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApi(value = "/apis/easyPoi/downloadsDevice",method = RequestMethod.GET,apiTitle = "downloadsDevice做文件导出")
    @ApiOperation("downloadsDevice做文件导出")
    public void downloadsDevice(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("poi/device.xlsx");
        List<Map<String,Object>> datas = deviceService.advancedQueryLists(new HashMap<String,Object>(){{put("limit",10000);}});
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", datas);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "设备信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    public static List<SupplierEntity> getSupplierList() {
        List<SupplierEntity> list = Lists.newArrayList();
//        SupplierEntity supplierEntity = SupplierEntity.builder()
//                .code("GYS001").name("供应商1号").taxNumber("税号").bank("工商银行").account("110").linkName("张栋").address("110").build();
//        list.add(supplierEntity);

        return list;
    }

    public static List<DemandEntity> getList() {
        List<DemandEntity> list = new ArrayList<DemandEntity>();
        DemandEntity de = new DemandEntity();
        de.setDeptName("省建设厅");
        de.setCode("审核转报-00180-000");
        de.setName("建筑业企业资质变更示例");
        de.setHandleTotal(3500L);
        de.setCategory(1);

        SupMaterialEntity sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("建筑业企业资质变更申请表");
        DataitemEntity dae = new DataitemEntity();
        dae.setName("企业营业执照号（五证合一社会信用号码）");
        List<DeptEntity> sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);

        dae = new DataitemEntity();
        dae.setName("企业名称");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);

        dae = new DataitemEntity();
        dae.setName("资质证书编号");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        sdepts.add(new DeptEntity("省省建设厅"));
        dae.setSdepts(sdepts);
        dae.setRemark("这就是生活啊");
        sme.getDataitemList().add(dae);
        de.getSupMaterialList().add(sme);

        sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("居民身份证");
        de.getSupMaterialList().add(sme);

        list.add(de);

        de = new DemandEntity();
        de.setDeptName("省建设厅");
        de.setCode("审核转报-00190-000");
        de.setName("建筑业企业资质变更示例无子项");
        de.setHandleTotal(6500L);
        de.setCategory(1);
        sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("建筑业企业资质变更申请表");
        dae = new DataitemEntity();
        dae.setName("企业营业执照号（五证合一社会信用号码）");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("企业名称");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("资质证书编号");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        sdepts.add(new DeptEntity("省省建设厅"));
        dae.setSdepts(sdepts);
        dae.setRemark("这就是人生啊");
        sme.getDataitemList().add(dae);
        de.getSupMaterialList().add(sme);
        list.add(de);


        return list;
    }

}
