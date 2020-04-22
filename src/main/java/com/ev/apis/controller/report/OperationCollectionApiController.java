package com.ev.apis.controller.report;

import com.ev.custom.service.DeviceService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.mes.service.DispatchItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kuzi
 * @Date 2020-4-20 9:25
 **/
@Api(value = "/",tags="看板—工单分布*生产进度*计件工资*设备状态*设备产量")
@RestController
public class OperationCollectionApiController {

    @Autowired
    private DispatchItemService dispatchItemService;
    @Autowired
    private DeviceService deviceService;


    @EvApiByToken(value = "/apis/board/dispachase/distributionDiagram", method = RequestMethod.POST, apiTitle = "工单分布")
    @ApiOperation("工单分布")
    public R distribution() {
        return dispatchItemService.distributionDiagram();
    }

    @EvApiByToken(value = "/apis/board/dispachase/productionSchedule", method = RequestMethod.POST, apiTitle = "生产进度")
    @ApiOperation("生产进度")
    public R schedule() {
        return dispatchItemService.productionSchedule();
    }

    @EvApiByToken(value = "/apis/board/dispachase/pieceRateWage", method = RequestMethod.POST, apiTitle = "计件工资")
    @ApiOperation("计件工资")
    public R pieceRateWage() {
        return dispatchItemService.getPieceRateWage();
    }


    @EvApiByToken(value = "/apis/board/dispachase/equipmentStatus", method = RequestMethod.POST, apiTitle = "设备状态")
    @ApiOperation("设备状态")
    public R equipmentStatus() {
        return deviceService.deviceStatus();
    }

    @EvApiByToken(value = "/apis/board/dispachase/equipmentProduction", method = RequestMethod.POST, apiTitle = "设备产量")
    @ApiOperation("设备产量")
    public R equipmentProduction() {
        return dispatchItemService.deviceProduction();
    }





}
