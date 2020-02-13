package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.custom.service.FacilityService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.StockDO;
import com.ev.scm.service.StockService;
import com.ev.scm.vo.StockEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by guMingJie on 2020-01-22.
 */


@Api(value = "/", tags = "库存")
@RestController
public class StockApiController {


    @Autowired
    private StockService stockService;
    @Autowired
    private FacilityLocationService facilityLocationService;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/stock", method = RequestMethod.POST, apiTitle = "期初库存导入")
    @ApiOperation("期初库存导入")
    @Transactional(rollbackFor = Exception.class)
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        List<StockEntity> stockEntityList = ExcelImportUtil.importExcel(file.getInputStream(), StockEntity.class, params);
        String facilityLocationName;
        String facilityName;
        if (stockEntityList.size() > 0) {
            for (StockEntity stockEntity : stockEntityList) {
                facilityLocationName = stockEntity.getFacilityLocationName();
                facilityName = stockEntity.getFacilityName();
                if (StringUtils.isEmpty(stockEntity.getSerialno())
                        || StringUtils.isEmpty(facilityLocationName)
                        || StringUtils.isEmpty(facilityName)
                        || !NumberUtils.isNumber(stockEntity.getTotalCount())
                        || !NumberUtils.isNumber(stockEntity.getUnitPrice())
                        || StringUtils.isEmpty(stockEntity.getBatch())) {
                    return R.error(messageSourceHandler.getMessage("basicInfo.correct.param", null));
                }

            }

            Map<String, Object> emptyMap = Maps.newHashMap();
            List<FacilityDO> facilityDOs = facilityService.list(emptyMap);
            List<FacilityLocationDO> locationDOs = facilityLocationService.list(emptyMap);
            List<MaterielDO> materielDOs = materielService.list(emptyMap);

            boolean isFacilityError = true;
            boolean isMaterielError = true;
            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            List<StockDO> stockDOs = Lists.newArrayList();
            StockDO stockDO;
            String serialNo;
            for (StockEntity stockEntity : stockEntityList) {
                stockDO = new StockDO();
                // 默认仓库
                facilityName = stockEntity.getFacilityName();
                facilityLocationName = stockEntity.getFacilityLocationName();
                for (FacilityDO facilityDO : facilityDOs) {
                    if (Objects.equals(facilityDO.getName(), facilityName)) {
                        Integer facilityDOId = facilityDO.getId();
                        // 默认库位
                        for (FacilityLocationDO locationDO : locationDOs) {
                            if (Objects.equals(locationDO.getName(), facilityLocationName)) {
                                // 若库位不在该仓库中
                                if (Objects.equals(locationDO.getFacilityId(), facilityDOId)) {
                                    isFacilityError = false;
                                    stockDO.setWarehLocation(locationDO.getId().longValue());
                                }
                                break;
                            }
                        }
                        if (isFacilityError) {
                            String[] args = {facilityName, facilityLocationName};
                            return R.error(messageSourceHandler.getMessage("basicInfo.facility.isFacilityError", args));
                        }
                        stockDO.setWarehouse(facilityDOId.longValue());
                        break;
                    }
                }

                // 验证物料是否存在
                serialNo = stockEntity.getSerialno();
                for (MaterielDO materielDO : materielDOs) {
                    if (Objects.equals(materielDO.getSerialNo(), serialNo)) {
                        isMaterielError = false;
                        stockDO.setMaterielId(materielDO.getId().longValue());
                        break;
                    }
                }
                if (isMaterielError) {
                    String[] args = {serialNo};
                    return R.error(messageSourceHandler.getMessage("basicInfo.materiel.isMaterielError", args));
                }
                stockDO.setBatch(stockEntity.getBatch());
                BigDecimal count = BigDecimal.valueOf(Double.parseDouble(stockEntity.getTotalCount()));
                stockDO.setEnteringTime(now);
                stockDO.setAvailableCount(count);
                stockDO.setCount(count);
                stockDO.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(stockEntity.getUnitPrice())));
                stockDO.setDelFlag(0);
                stockDO.setCreateBy(userId);
                stockDO.setCreateTime(now);
                stockDOs.add(stockDO);
            }
            stockService.batchSave(stockDOs);
        }
        return R.ok();
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/stock", method = RequestMethod.GET, apiTitle = "导出库存")
    @ApiOperation("导出库存")
    public void exportExcel(
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,

            @ApiParam(value = "产品Id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("productTypeId", productTypeId);
        param.put("fuzzySearch", fuzzySearch);
        param.put("facilityTypeId", facilityTypeId);

        param.put("materielId", materielId);
        param.put("batch", batch);
        List<Map<String, Object>> data = materielService.stockListForMap(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/stock.xlsx");
        Map<String, Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "库存");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApiByToken(value = "/apis/stock/list", method = RequestMethod.POST, apiTitle = "库存查询")
    @ApiOperation("库存查询")
    public R list(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,

            @ApiParam(value = "产品Id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch

    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("productTypeId", productTypeId);
        params.put("fuzzySearch", fuzzySearch);
        params.put("facilityTypeId", facilityTypeId);

        params.put("materielId", materielId);
        params.put("batch", batch);
        List<Map<String, Object>> data = materielService.stockListForMap(params);
        int total = materielService.stockCountForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }


}





