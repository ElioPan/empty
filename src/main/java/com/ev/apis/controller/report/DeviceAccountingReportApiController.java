package com.ev.apis.controller.report;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.report.service.DeviceAccountingReportService;
import com.ev.report.vo.DeviceVO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 设备管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-16 09:51:41
 */
@Api(value = "/", tags = "设备管理报表分析API")
@RestController
public class DeviceAccountingReportApiController {
    @Autowired
    private DeviceAccountingReportService reportService;

    @EvApiByToken(value = "/apis/device/accountingReport", method = RequestMethod.POST, apiTitle = "设备管理分析")
    @ApiOperation("设备管理分析")
    public R analysis(DeviceVO deviceVO) {
        // 查询列表数据
        DsResultResponse analysis = reportService.analysis(deviceVO);
        if (analysis == null) {
            return R.ok();
        }
        Map<String,Object> result = Maps.newHashMap();
        result.put("data",analysis);
        return R.ok(result);
    }
    @EvApiByToken(value = "/apis/exportExcel/device/accountingReport", method = RequestMethod.POST, apiTitle = "设备管理分析(导出)")
    @ApiOperation("设备管理分析(导出)")
    public void analysis(DeviceVO deviceVO,
    HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        // 查询列表数据
        DsResultResponse analysis = reportService.analysis(deviceVO);
        if (analysis == null) {
            return ;
        }
        ClassPathResource classPathResource = new ClassPathResource("poi/report_device_accountingReport.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", analysis.getDatas());
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "设备管理分析");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }
}
