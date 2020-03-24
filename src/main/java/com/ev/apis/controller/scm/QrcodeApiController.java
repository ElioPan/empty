package com.ev.apis.controller.scm;

import com.alibaba.druid.util.StringUtils;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.mes.service.MaterialInspectionService;
import com.ev.scm.domain.PurchasecontractItemDO;
import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.domain.QrcodeItemDO;
import com.ev.scm.service.PurchasecontractItemService;
import com.ev.scm.service.PurchasecontractService;
import com.ev.scm.service.QrcodeItemService;
import com.ev.scm.service.QrcodeService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 库存二维码控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "库存二维码API")
public class QrcodeApiController {
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    QrcodeService qrcodeService;

    @Autowired
    QrcodeItemService qrcodeItemService;

    @Autowired
    MaterialInspectionService materialInspectionService;

    @Autowired
    PurchasecontractItemService purchasecontractItemService;



    @EvApiByToken(value = "/apis/scm/qrcode/list",method = RequestMethod.GET,apiTitle = "获取二维码列表")
    @ApiOperation("获取二维码列表")
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

    @EvApiByToken(value = "/apis/scm/qrcode/save",method = RequestMethod.POST,apiTitle = "保存库存二维码")
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
        List<QrcodeDO> qrcodeDOS = new ArrayList<>();
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
            qrcodeDOS.add(qrcodeDO);
        }while(totalCount.compareTo(BigDecimal.ZERO)>0);
        qrcodeService.batchInsert(qrcodeDOS);
        qrcodeDOList = qrcodeService.listForMap(new HashMap<String,Object>(){{put("inspectionId",inspectionId);}});
        /**
         * 修改检验单的状态
         */
        materialInspectionDO.setIsPrintedQrcode(1);
        materialInspectionDO.setUnitCodeCount(unitCount);
        materialInspectionService.update(materialInspectionDO);
        result.put("qrCodeList",qrcodeDOList);
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/scm/qrcode/inDetail",method = RequestMethod.GET,apiTitle = "获取入库时二维码信息")
    @ApiOperation("获取入库时二维码信息")
    public R inDetail(@ApiParam(value = "二维码主键") @RequestParam(value = "qrCodeId") Long qrCodeId,
                      @ApiParam(value = "供应商主键") @RequestParam(value = "supplierId",required = false) Long supplierId,
                      @ApiParam(value = "入库类型 0：采购入库，1：生产入库，2：委外入库，3：其他入库") @RequestParam(value = "inType") Integer inType,
                      @ApiParam(value = "是否多单入库 1：是，0：否") @RequestParam(value = "isMultiple") Integer isMultiple,
                      @ApiParam(value = "合同号") @RequestParam(value = "contractNo",required = false) String contractNo){
        Map<String,Object> result = new HashMap<>();
        /**
         * 先验证是否二维码已经有入库信息
         */
        QrcodeDO qrcodeDO = qrcodeService.get(qrCodeId);
        if(qrcodeDO == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.invalid",null));
        }
        if(qrcodeDO.getStockId() != null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.alreadyIn",null));
        }
        /**
         * 验证是否是一个供应商
         */
        MaterialInspectionDO materialInspectionDO = materialInspectionService.get(qrcodeDO.getInspectionId());
        if(supplierId != null && !Objects.equals(materialInspectionDO.getSupplierId(),supplierId)){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.in.oneSupplier",null));
        }
        /**
         * 判断单据类型是否一致
         */
        if(!qrcodeService.isMultipleType(inType, materialInspectionDO)){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.in.oneTypeIn",null));
        }
        /**
         * 判断合同号是否一致
         */
        if(!qrcodeService.isMultipleIn(isMultiple,contractNo, materialInspectionDO)){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.in.oneContract",null));
        }
        /**
         * 获取二维码关联合同单价
         */
        Map<String,Object> qrCodeInfo = qrcodeService.listForMap(new HashMap<String,Object>(){{put("id",qrCodeId);}}).get(0);
        Long inspectionId = Long.parseLong(qrCodeInfo.get("inspectionId").toString());
        MaterialInspectionDO inspectionDO = materialInspectionService.get(inspectionId);
        if(inspectionDO.getSourceId()!=null && Long.valueOf(inspectionDO.getSourceType().toString()).compareTo(ConstantForGYL.CGHT) ==0 ){
            PurchasecontractItemDO purchasecontractItemDO = purchasecontractItemService.get(inspectionDO.getSourceId());
            qrCodeInfo.put("unitPrice",purchasecontractItemDO.getUnitPrice());
            qrCodeInfo.put("amount",purchasecontractItemDO.getAmount());
        }else{
            qrCodeInfo.put("unitPrice",0);
            qrCodeInfo.put("amount",0);
        }
        result.put("qrCodeInfo",qrCodeInfo);
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/scm/qrcode/outDetail",method = RequestMethod.GET,apiTitle = "获取出库时二维码信息")
    @ApiOperation("获取出库时二维码信息")
    public R outDetail(@ApiParam(value = "二维码主键") @RequestParam(value = "qrCodeId") Long qrCodeId){
        Map<String,Object> result = new HashMap<>();
        /**
         * 验证是否包含此二维码信息
         */
        QrcodeDO qrcodeDO = qrcodeService.get(qrCodeId);
        if(qrcodeDO == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.invalid",null));
        }
        if(qrcodeDO.getStockId() == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.notIn",null));
        }
        List<Map<String,Object>> qrCodeList = qrcodeService.listForMap(new HashMap<String,Object>(){{put("id",qrCodeId);}});
        result.put("qrCodeInfo",qrCodeList.get(0));
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/scm/qrcode/backDetail",method = RequestMethod.GET,apiTitle = "退货（料）二维码信息")
    @ApiOperation("退货（料）二维码信息")
    public R outDetail(@ApiParam(value = "二维码主键") @RequestParam(value = "qrCodeId") Long qrCodeId,
                       @ApiParam(value = "退货（料）单单据编号") @RequestParam(value = "sourceCode") String sourceCode){
        Map<String,Object> result = new HashMap<>();
        /**
         * 验证是否包含此二维码信息
         */
        QrcodeDO qrcodeDO = qrcodeService.get(qrCodeId);
        if(qrcodeDO == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.invalid",null));
        }
        if(qrcodeDO.getStockId() == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.notIn",null));
        }
        /**
         * 验证扫描的二维码是否为源单关联的码
         */
        List<QrcodeItemDO> qrcodeItemDOList = qrcodeItemService.list(new HashMap<String,Object>(1){{put("inheadCode",sourceCode);}});
        if(qrcodeItemDOList.size()==0){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.invalidSource",new String[]{sourceCode}));
        }

        List<Map<String,Object>> qrCodeList = qrcodeService.listForMap(new HashMap<String,Object>(1){{put("id",qrCodeId);}});
        result.put("qrCodeInfo",qrCodeList.get(0));
        result.put("inOutCount",qrcodeItemDOList.get(0).getCount());
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/scm/qrcode/logDetail",method = RequestMethod.GET,apiTitle = "物料条码追溯查询")
    @ApiOperation("物料条码追溯查询")
    public R logDetail(@ApiParam(value = "二维码主键") @RequestParam(value = "qrCodeId") Long qrCodeId){
        Map<String,Object> result = new HashMap<>();
        /**
         * 验证是否包含此二维码信息
         */
        QrcodeDO qrcodeDO = qrcodeService.get(qrCodeId);
        if(qrcodeDO == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.invalid",null));
        }
        if(qrcodeDO.getStockId() == null){
            return R.error(messageSourceHandler.getMessage("scm.qrcode.notIn",null));
        }
        /**
         * 获取二维码历史信息
         */
        List<Map<String,Object>> qrCodeList = qrcodeService.listForMap(new HashMap<String,Object>(1){{put("id",qrCodeId);}});
        result.put("header",qrCodeList.get(0));
        result.put("body",qrcodeItemService.logDetail(qrCodeId));
        return R.ok(result);
    }
}
