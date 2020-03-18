package com.ev.apis.controller.report;

import com.ev.report.service.DeviceAccountingReportService;
import com.ev.report.vo.DeviceVO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return reportService.analysis(deviceVO);
    }
}
