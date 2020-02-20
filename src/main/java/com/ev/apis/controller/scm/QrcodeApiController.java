package com.ev.apis.controller.scm;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.mes.service.MaterialInspectionService;
import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.service.QrcodeService;
import com.google.common.collect.Maps;
import com.google.zxing.qrcode.encoder.QRCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存二维码控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "库存二维码API")
public class QrcodeApiController {
    @Autowired
    QrcodeService qrcodeService;

    @Autowired
    MaterialInspectionService materialInspectionService;

    @EvApiByToken(value = "/apis/scm/qrcode/list",method = RequestMethod.GET,apiTitle = "获取二维码列表")
    @ApiOperation("获取销售票据列表/高级搜索")
    public R list(@ApiParam(value = "检验单据ID") @RequestParam(value = "inspectionId",required = false) String inspectionId,
                          @ApiParam(value = "库存ID") @RequestParam(value = "stockId",defaultValue = "",required = false)  String stockId,
                          @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String,Object> map = Maps.newHashMap();
        map.put("inspectionId", inspectionId);
        map.put("stockId",stockId);

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data = qrcodeService.listForMap(map);
        int total = qrcodeService.countForMap(map);
        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/scm/qrcode/save",method = RequestMethod.GET,apiTitle = "保存库存二维码")
    @ApiOperation("保存库存二维码")
    public R save(@ApiParam(value = "检验单据ID") @RequestParam(value = "inspectionId",required = false) Long inspectionId,
                  @ApiParam(value = "物料编号") @RequestParam(value = "materielNo",required = false) String materielNo,
                  @ApiParam(value = "单个包装数量") @RequestParam(value = "unitCount",defaultValue = "",required = false) BigDecimal unitCount){
        Map<String,Object> result = new HashMap<>();
        /**
         * 先验证是否二维码已经保存过
         */
        List<Map<String,Object>> qrcodeDOList = qrcodeService.listForMap(new HashMap<String,Object>(){{put("inspectionId",inspectionId);}});
        /**
         * 返回值封装
         */
        if(qrcodeDOList.size()>0){
            result.put("qrCodeList",qrcodeDOList);
            return R.ok(result);
        }
        MaterialInspectionDO materialInspectionDO = materialInspectionService.get(inspectionId);
        BigDecimal totalCount = materialInspectionDO.getQualifiedCount();
        do{
            BigDecimal thisCount = BigDecimal.ZERO;
            if(totalCount.compareTo(unitCount)>=0){
                thisCount = unitCount;
            }else{
                thisCount = totalCount;
            }
            totalCount = totalCount.subtract(unitCount);
            QrcodeDO qrcodeDO = new QrcodeDO(inspectionId, materialInspectionDO.getSourceNo(), Long.parseLong(materialInspectionDO.getMaterielId().toString()), materielNo, materialInspectionDO.getBatchNo(), thisCount);
            qrcodeService.save(qrcodeDO);
        }while(totalCount.compareTo(BigDecimal.ZERO)>0);
        qrcodeDOList = qrcodeService.listForMap(new HashMap<String,Object>(){{put("inspectionId",inspectionId);}});
        result.put("qrCodeList",qrcodeDOList);
        return R.ok(result);
    }
}
