package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.custom.service.FacilityService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.scm.domain.StockAnalysisDO;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.domain.StockStartDO;
import com.ev.scm.service.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by guMingJie on 2020-01-22.
 */


@Api(value = "/", tags = "库存API")
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
    private StockStartService stockStartService;
    @Autowired
    private StockInService stockInService;
    @Autowired
    private StockOutItemService stockOutItemService;
    @Autowired
    private StockAnalysisService stockAnalysisService;
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
        List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
        if (list.size() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
        }
        if (list.get(0).getStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
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
                        || !NumberUtils.isNumber(stockEntity.getAmount())
//                        || StringUtils.isEmpty(stockEntity.getBatch()
                        ) {
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
            String batch;
            Integer isLot;
            for (StockEntity stockEntity : stockEntityList) {
                stockDO = new StockDO();
                // 默认仓库
                facilityName = stockEntity.getFacilityName();
                facilityLocationName = stockEntity.getFacilityLocationName();
                for (FacilityDO facilityDO : facilityDOs) {
                    isFacilityError =true;
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
                        stockDO.setWarehouse(facilityDOId.longValue());
                        break;
                    }
                }
                if (isFacilityError) {
                    String[] args = {facilityName, facilityLocationName};
                    return R.error(messageSourceHandler.getMessage("basicInfo.facility.isFacilityError", args));
                }
                // 验证物料是否存在
                serialNo = stockEntity.getSerialno();
                batch = stockEntity.getBatch();
                for (MaterielDO materielDO : materielDOs) {
                    if (Objects.equals(materielDO.getSerialNo(), serialNo)) {
                        isLot = materielDO.getIsLot();
                        if (isLot == 1 && StringUtils.isEmpty(batch)) {
                            String[] args = {materielDO.getName()};
                            return R.error(messageSourceHandler.getMessage("basicInfo.materiel.isLotError", args));
                        }
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
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(stockEntity.getAmount()));
                stockDO.setAmount(amount);
                stockDO.setUnitPrice(amount.divide(count,Constant.BIGDECIMAL_ZERO));
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
            @ApiParam(value = "库位ID") @RequestParam(value = "locationId", defaultValue = "", required = false) String locationId,
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

        params.put("locationId", locationId);
        params.put("materielId", materielId);
        params.put("batch", batch);
        List<Map<String, Object>> data = materielService.stockListForMap(params);
        int total = materielService.stockCountForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/start", method = RequestMethod.POST, apiTitle = "设置库存初始时间")
    @ApiOperation("设置库存初始时间")
    public R save(@ApiParam(value = "启用年月") @RequestParam(value = "yearAndMonth", defaultValue = "", required = false) String yearAndMonth) {
        Calendar input = Calendar.getInstance();
        Date inputDate = DateFormatUtil.getDateByParttern(yearAndMonth);
        if (inputDate == null) {
            return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
        }
        input.setTime(inputDate);
        int year = input.get(Calendar.YEAR);
        int month = input.get(Calendar.MONTH);

        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) == year && month == now.get(Calendar.MONTH) + 1) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.YEAR, year);

            start.set(Calendar.MONTH, month - 1);
            start.set(Calendar.DAY_OF_MONTH, 1);
            // 是否为修改
            List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
            if (list.size() > 0) {
                StockStartDO stockStartDO = list.get(0);
                if (stockStartDO.getStatus() == 1) {
                    return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
                }
                stockStartDO.setStartTime(start.getTime());
                stockStartService.update(stockStartDO);
                return R.ok();
            }
            // 为新增
            StockStartDO stockStartDO = new StockStartDO();
            stockStartDO.setStartTime(start.getTime());
            stockStartDO.setStatus(0);
            stockStartService.save(stockStartDO);
            return R.ok();
        }
        return R.error(messageSourceHandler.getMessage("scm.stock.timeError", null));
    }

    @EvApiByToken(value = "/apis/stock/startDetail", method = RequestMethod.POST, apiTitle = "查看库存初始时间")
    @ApiOperation("查看库存初始时间")
    public R detail() {
        // 是否为修改
        List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
        if (list.size() > 0) {
            StockStartDO stockStartDO = list.get(0);
            Map<String, Object> map = Maps.newHashMap();
            map.put("yearAndMonth", stockStartDO.getStartTime());
            return R.ok(map);
        }
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/save", method = RequestMethod.POST, apiTitle = "初始库存录入")
    @ApiOperation("初始库存录入")
    public R saveStock(@ApiParam(value = "出库明细:" +
            "[\n" +
            "    {\n" +
            "        \"materielId\":25,\n" +
            "        \"batch\":\"wh_ycl_0045\",\n" +
            "        \"warehouse\":3,\n" +
            "        \"warehLocation\":7,\n" +
            "        \"count\":2011,\n" +
            "        \"amount\":24000\n" +
            "    },\n" +
            "    {\n" +
            "        \"materielId\":25,\n" +
            "        \"batch\":\"wh_ycl_0034\",\n" +
            "        \"warehouse\":4,\n" +
            "        \"warehLocation\":8,\n" +
            "        \"count\":241,\n" +
            "        \"amount\":24000\n" +
            "    }\n" +
            "]"
            , required = true) @RequestParam(value = "item", defaultValue = "") String stockList,
                       @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds) {
        Map<String, Object> emptyMap = Maps.newHashMap();
        List<StockStartDO> list = stockStartService.list(emptyMap);
        if (list.size() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
        }
        if (list.get(0).getStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
        }

        if (itemIds.length > 0) {
            stockService.batchRemove(itemIds);
        }
        Date now = new Date();
        List<StockDO> itemDOs = JSON.parseArray(stockList, StockDO.class);
        List<MaterielDO> materielDOs = materielService.list(emptyMap);
        String batch;
        Integer isLot;
        if (itemDOs.size() > 0 && materielDOs.size() > 0) {
            for (StockDO itemDO : itemDOs) {
                batch = itemDO.getBatch();
                for (MaterielDO materielDO : materielDOs) {
                    if (Objects.equals(materielDO.getId().longValue(), itemDO.getMaterielId())) {
                        isLot = materielDO.getIsLot();
                        if (isLot == 1 && StringUtils.isEmpty(batch)) {
                            String[] args = {materielDO.getName()};
                            return R.error(messageSourceHandler.getMessage("basicInfo.materiel.isLotError", args));
                        }
                        BigDecimal count = itemDO.getCount();
                        BigDecimal amount = itemDO.getAmount();
                        itemDO.setAvailableCount(count);
                        itemDO.setUnitPrice(amount.divide(count,Constant.BIGDECIMAL_ZERO));
                        itemDO.setEnteringTime(now);
                        itemDO.setDelFlag(0);
                        break;
                    }
                }

            }
            List<StockDO> batchSave = itemDOs.stream().filter(itemDO -> itemDO.getId() == null).collect(Collectors.toList());
            if (batchSave.size() > 0) {
                stockService.batchSave(batchSave);
            }
            List<StockDO> batchUpdate = itemDOs.stream().filter(itemDO -> itemDO.getId() != null).collect(Collectors.toList());
            if (batchUpdate.size() > 0) {
                stockService.batchUpdate(batchUpdate);
            }
            return R.ok();
        }
        return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
    }

    @EvApiByToken(value = "/apis/stock/startList", method = RequestMethod.POST, apiTitle = "期初库存列表")
    @ApiOperation("期初库存列表")
    public R startList() {

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = stockService.listForMap(results);
        Map<String, Object> countForMap = stockService.countForMap(results);
        results.put("data",data);
        results.put("total",countForMap);
        int startStatus=0;
        if (data.size() > 0) {
            List<StockStartDO> list = stockStartService.list(Maps.newHashMap());
            startStatus=list.get(0).getStatus();
        }
        results.put("startStatus",startStatus);
        return R.ok(results);
    }



    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endInitial", method = RequestMethod.POST, apiTitle = "结束初始化")
    @ApiOperation("结束初始化")
    public R endInitial() {
        Map<String, Object> emptyMap = Maps.newHashMap();
        List<StockStartDO> list = stockStartService.list(emptyMap);
        if (list.size() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.stock.startTimeError", null));
        }
        if (list.get(0).getStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("scm.stock.error", null));
        }

        List<StockDO> stockDOList = stockService.list(emptyMap);
        StockAnalysisDO stockAnalysisDO;
        StockStartDO stockStartDO = list.get(0);
        Date period = stockStartDO.getStartTime();
        List<StockAnalysisDO> stockAnalysisDOS = Lists.newArrayList();
        for (StockDO stockDO : stockDOList) {
            stockAnalysisDO = new StockAnalysisDO();
            stockAnalysisDO.setMaterielId(stockDO.getMaterielId().intValue());
            stockAnalysisDO.setBatch(stockDO.getBatch());
            stockAnalysisDO.setInitialCount(stockDO.getCount());
            stockAnalysisDO.setInitialAmount(stockDO.getAmount());
            stockAnalysisDO.setPeriod(period);
            stockAnalysisDO.setIsClose(0);
            stockAnalysisDO.setDelFlag(0);
            stockAnalysisDOS.add(stockAnalysisDO);
        }
        stockAnalysisService.batchInsert(stockAnalysisDOS);

        stockStartDO.setStatus(1);
        stockStartService.update(stockStartDO);

        return R.ok();
    }

    @EvApiByToken(value = "/apis/stock/stockAnalysisTime", method = RequestMethod.POST, apiTitle = "获取本次期间")
    @ApiOperation("获取本次期间")
    public R stockOutAccountingTime() {
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            result.put("period", stockAnalysisDO.getPeriod());
        }
        return R.ok(result);
    }


    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/stockOutAccounting", method = RequestMethod.POST, apiTitle = "出库核算")
    @ApiOperation("出库核算")
    public R stockOutAccounting(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            Calendar instance = Calendar.getInstance();
            Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
            Date oldPeriod = stockAnalysisDO.getPeriod();
            if (periodTime == null) {
                return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
            }
            instance.setTime(periodTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long newMillis = instance.getTimeInMillis();
            instance.setTime(oldPeriod);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long oldMillis = instance.getTimeInMillis();

            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
            long upOldInMillis = instance.getTimeInMillis();

            if (newMillis == upOldInMillis && stockAnalysisDO.getIsClose() == 0) {
                return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
            }
            if (newMillis == oldMillis) {
                params.clear();
                // 入库单
                params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
                params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
                params.put("auditSign", ConstantForGYL.OK_AUDITED);
                List<Map<String, Object>> stockInList = stockInService.listForMap(params);
                List<StockOutItemDO> stockOutList = stockOutItemService.list(params);
                params.put("period", period);
                List<StockAnalysisDO> stockAnalysisList = stockAnalysisService.list(params);

                // 加权平均法出库成本的算法：(月初结存金额+本月入库金额)/（月初结存数量+本月入库数量），算出当月加权平均
                // 分批认定法：物料属性中需要设置批次管理，入库时入库单需要录入批号，出库时出库单的单价以入库时相同批号的单价作为出库单价。

                // 入库的物料
                List<Object> materielInIdList = stockInList.stream()
                        .map(stringObjectMap -> stringObjectMap.get("materielId"))
                        .collect(Collectors.toList());
                params.put("materielIdList", materielInIdList);
                List<MaterielDO> materielDOInList = materielService.list(params);

                // 出库的物料
                List<Integer> materielOutIdList = stockOutList.stream()
                        .map(StockOutItemDO::getMaterielId)
                        .collect(Collectors.toList());
                params.put("materielIdList", materielOutIdList);
                List<MaterielDO> materielDOOutList = materielService.list(params);

                // 加权平均的入库物料
                List<Integer> weightedAverageIdInList = materielDOInList.stream()
                        .filter(materielDO -> Objects.equals(ConstantForGYL.WEIGHTED_AVERAGE, materielDO.getValuationMethod()))
                        .map(MaterielDO::getId)
                        .collect(Collectors.toList());

                // 加权平均的出库物料
                List<Integer> weightedAverageIdOutList = materielDOOutList.stream()
                        .filter(materielDO -> Objects.equals(ConstantForGYL.WEIGHTED_AVERAGE, materielDO.getValuationMethod()))
                        .map(MaterielDO::getId)
                        .collect(Collectors.toList());

                // 分批认定的入库物料
                List<Integer> idAndBatchInList = materielDOInList.stream()
                        .filter(materielDO -> Objects.equals(ConstantForGYL.BATCH_FINDS, materielDO.getValuationMethod()))
                        .map(MaterielDO::getId)
                        .collect(Collectors.toList());

                // 分批认定的出库物料
                List<Integer> idAndBatchOutList = materielDOOutList.stream()
                        .filter(materielDO -> Objects.equals(ConstantForGYL.BATCH_FINDS, materielDO.getValuationMethod()))
                        .map(MaterielDO::getId)
                        .collect(Collectors.toList());


                // 获取本期要计算入库的加权平均物料
                List<Map<String, Object>> stockInBatchEmpty = stockInList.stream()
                        .filter(stringObjectMap -> weightedAverageIdInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
                        .collect(Collectors.toList());

                // 获取本期要计算出库的加权平均物料
                List<StockOutItemDO> stockOutBatchEmpty = stockOutList.stream()
                        .filter(stockOutItemDO -> weightedAverageIdOutList.contains(stockOutItemDO.getMaterielId()))
                        .collect(Collectors.toList());

                // 获取本期要计算入库的分批认定物料 id+&+batch
                List<Map<String, Object>> stockInBatchNonEmpty = stockInList.stream()
                        .filter(stringObjectMap -> idAndBatchInList.contains(Integer.parseInt(stringObjectMap.get("materielId").toString())))
                        .collect(Collectors.toList());

                // 获取本期要计算出库的分批认定物料 id+&+batch
                List<StockOutItemDO> stockOutBatchNonEmpty = stockOutList.stream()
                        .filter(stockOutItemDO -> idAndBatchOutList.contains(stockOutItemDO.getMaterielId()))
                        .collect(Collectors.toList());

                Map<String, BigDecimal> materielOutCountMap = Maps.newHashMap();
                Map<String, BigDecimal> materielOutAmountMap = Maps.newHashMap();
                Map<String, BigDecimal> materielInCountMap = Maps.newHashMap();
                Map<String, BigDecimal> materielInAmountMap = Maps.newHashMap();
                Map<String, BigDecimal> materielInUnitPriceMap = Maps.newHashMap();

                // 加权平均入库
                if (stockInBatchEmpty.size() > 0) {
                    // 获取入库列表的总数量
                    for (Map<String, Object> stockInBatch : stockInBatchEmpty) {
                        String materielId = stockInBatch.get("materielId").toString();
                        if (materielInCountMap.containsKey(materielId)) {
                            materielInCountMap.put(materielId, materielInCountMap.get(materielId).add(MathUtils.getBigDecimal(stockInBatch.get("count"))));
                            materielInAmountMap.put(materielId, materielInAmountMap.get(materielId).add(MathUtils.getBigDecimal(stockInBatch.get("amount"))));
                            continue;
                        }
                        materielInCountMap.put(materielId, MathUtils.getBigDecimal(stockInBatch.get("count")));
                        materielInAmountMap.put(materielId, MathUtils.getBigDecimal(stockInBatch.get("amount")));
                    }
                }

                // 计算出库成本单价
                for (StockAnalysisDO analysisDO : stockAnalysisList) {
                    String materielId = analysisDO.getMaterielId().toString();
                    if (materielInCountMap.containsKey(materielId)) {
                        // (月初结存金额+本月入库金额)/（月初结存数量+本月入库数量）
                        BigDecimal inAmount = materielInAmountMap.get(materielId);
                        BigDecimal inCount = materielInCountMap.get(materielId);
                        analysisDO.setInCount(inCount);
                        analysisDO.setInAmount(inAmount);
                        materielInUnitPriceMap.put(materielId
                                , (analysisDO.getInitialAmount().add(inAmount)).divide(analysisDO.getInitialCount().add(inCount), Constant.BIGDECIMAL_ZERO));
                        continue;
                    }
                    analysisDO.setInAmount(BigDecimal.ZERO);
                    analysisDO.setInCount(BigDecimal.ZERO);
                }


                // 分批认定入库
                if (stockInBatchNonEmpty.size() > 0) {
                    // 获取入库列表的总数量
                    for (Map<String, Object> stockInBatch : stockInBatchNonEmpty) {
                        String materielIdAndBatch = stockInBatch.get("materielId").toString() + "&" + stockInBatch.get("batch").toString();
                        if (materielInCountMap.containsKey(materielIdAndBatch)) {
                            materielInCountMap.put(materielIdAndBatch, materielInCountMap.get(materielIdAndBatch).add(MathUtils.getBigDecimal(stockInBatch.get("count"))));
                            materielInAmountMap.put(materielIdAndBatch, materielInAmountMap.get(materielIdAndBatch).add(MathUtils.getBigDecimal(stockInBatch.get("amount"))));
                            continue;
                        }
                        materielInCountMap.put(materielIdAndBatch, MathUtils.getBigDecimal(stockInBatch.get("count")));
                        materielInAmountMap.put(materielIdAndBatch, MathUtils.getBigDecimal(stockInBatch.get("amount")));
                    }
                }

                // 计算出库成本单价
                for (StockAnalysisDO analysisDO : stockAnalysisList) {
                    String materielIdAndBatch = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();

                    if (materielInCountMap.containsKey(materielIdAndBatch)) {
                        // (月初结存金额+本月入库金额)/（月初结存数量+本月入库数量）
                        BigDecimal inAmount = materielInAmountMap.get(materielIdAndBatch);
                        BigDecimal inCount = materielInCountMap.get(materielIdAndBatch);
                        analysisDO.setInCount(inCount);
                        analysisDO.setInAmount(inAmount);
                        materielInUnitPriceMap.put(materielIdAndBatch
                                , (analysisDO.getInitialAmount().add(inAmount)).divide(analysisDO.getInitialCount().add(inCount), Constant.BIGDECIMAL_ZERO));
                        continue;
                    }
                    analysisDO.setInAmount(BigDecimal.ZERO);
                    analysisDO.setInCount(BigDecimal.ZERO);
                }

                // 加权平均出库
                if (stockOutBatchEmpty.size() > 0) {
                    for (StockOutItemDO itemDO : stockOutBatchEmpty) {
                        String materielId = itemDO.getMaterielId().toString();
                        if (materielInUnitPriceMap.containsKey(materielId)) {
                            // 写入出库成本单价成本金额
                            BigDecimal unitPrice = materielInUnitPriceMap.get(materielId);
                            itemDO.setUnitPrice(unitPrice);
                            BigDecimal count = itemDO.getCount();
                            BigDecimal amount = count.multiply(unitPrice);
                            itemDO.setAmount(amount);

                            // 保存出库的总数量
                            if (materielOutCountMap.containsKey(materielId)) {
                                materielOutCountMap.put(materielId, materielOutCountMap.get(materielId).add(count));
                                materielOutAmountMap.put(materielId, materielOutAmountMap.get(materielId).add(amount));
                                continue;
                            }
                            materielOutCountMap.put(materielId, count);
                            materielOutAmountMap.put(materielId, amount);
                        }
                    }
                    stockOutItemService.batchUpdate(stockOutBatchEmpty);
                }

                // 分批认定出库
                if (stockOutBatchNonEmpty.size() > 0) {
                    for (StockOutItemDO itemDO : stockOutBatchNonEmpty) {
                        String materielIdAndBatch = itemDO.getMaterielId().toString() + "&" + itemDO.getBatch();
                        if (materielInUnitPriceMap.containsKey(materielIdAndBatch)) {
                            // 写入出库成本单价成本金额
                            BigDecimal unitPrice = materielInUnitPriceMap.get(materielIdAndBatch);
                            itemDO.setUnitPrice(unitPrice);
                            BigDecimal count = itemDO.getCount();
                            BigDecimal amount = count.multiply(unitPrice);
                            itemDO.setAmount(amount);

                            // 保存出库的总数量
                            if (materielOutCountMap.containsKey(materielIdAndBatch)) {
                                materielOutCountMap.put(materielIdAndBatch, materielOutCountMap.get(materielIdAndBatch).add(count));
                                materielOutAmountMap.put(materielIdAndBatch, materielOutAmountMap.get(materielIdAndBatch).add(amount));
                                continue;
                            }
                            materielOutCountMap.put(materielIdAndBatch, count);
                            materielOutAmountMap.put(materielIdAndBatch, amount);
                        }
                    }
                    stockOutItemService.batchUpdate(stockOutBatchNonEmpty);
                }

                // 分析所有物料
                for (StockAnalysisDO analysisDO : stockAnalysisList) {
                    String materielId = analysisDO.getMaterielId().toString();
                    String materielIdAndBatch = analysisDO.getMaterielId().toString() + "&" + analysisDO.getBatch();

                    BigDecimal inCount = analysisDO.getInCount() == null ? BigDecimal.ZERO : analysisDO.getInCount();
                    BigDecimal inAmount = analysisDO.getInAmount() == null ? BigDecimal.ZERO : analysisDO.getInAmount();
                    // 加权平均
                    if (materielOutCountMap.containsKey(materielId)) {
                        BigDecimal outCount = materielOutCountMap.get(materielId);
                        BigDecimal outAmount = materielOutAmountMap.get(materielId);
                        analysisDO.setOutCount(outCount);
                        analysisDO.setOutAmount(outAmount);
                        // 期末结存数量=期初数量+本月入库数量-本月发出数量
                        BigDecimal finalCount = analysisDO.getInitialCount()
                                .add(inCount)
                                .subtract(outCount);
                        analysisDO.setFinalCount(finalCount);
                        // 期末结存金额=期初金额+本月入库金额-本月发出金额
                        BigDecimal finalAmount = analysisDO.getInitialAmount()
                                .add(inAmount)
                                .subtract(outAmount);
                        analysisDO.setFinalAmount(finalAmount);
                        continue;
                    }

                    // 分批认定
                    if (materielOutCountMap.containsKey(materielIdAndBatch)) {
                        BigDecimal outCount = materielOutCountMap.get(materielIdAndBatch);
                        BigDecimal outAmount = materielOutAmountMap.get(materielIdAndBatch);
                        analysisDO.setOutCount(outCount);
                        analysisDO.setOutAmount(outAmount);
                        // 期末结存数量=期初数量+本月入库数量-本月发出数量
                        BigDecimal finalCount = analysisDO.getInitialCount()
                                .add(inCount)
                                .subtract(outCount);
                        analysisDO.setFinalCount(finalCount);
                        // 期末结存金额=期初金额+本月入库金额-本月发出金额
                        BigDecimal finalAmount = analysisDO.getInitialAmount()
                                .add(inAmount)
                                .subtract(outAmount);
                        analysisDO.setFinalAmount(finalAmount);
                        continue;
                    }
                    analysisDO.setOutCount(BigDecimal.ZERO);
                    analysisDO.setOutAmount(BigDecimal.ZERO);

                    analysisDO.setFinalCount(
                            analysisDO.getInitialCount()
                                    .add(inCount));
                    analysisDO.setFinalAmount(
                            analysisDO.getInitialAmount()
                                    .add(inAmount));
                }
                stockAnalysisService.batchUpdate(stockAnalysisList);

//                List<Map<String, Object>> stockInBatchNonEmpty = stockInList.stream()
//                        .filter(stringObjectMap -> stringObjectMap.containsKey("batch"))
//                        .collect(Collectors.toList());
//                List<StockOutItemDO> stockOutBatchNonEmpty = stockOutList.stream()
//                        .filter(stockOutItemDO -> stockOutItemDO.getBatch() != null)
//                        .collect(Collectors.toList());
//                if (stockOutBatchNonEmpty.size() > 0 && stockInBatchNonEmpty.size() > 0) {
//                    Map<String, BigDecimal> materielInUnitPriceMap = Maps.newHashMap();
//                    Map<String, BigDecimal> materielOutCountMap = Maps.newHashMap();
//                    Map<String, BigDecimal> materielOutAmountMap = Maps.newHashMap();
//                    for (Map<String, Object> map : stockInBatchNonEmpty) {
//                        String materielIdAndBatch = map.get("materielId").toString() + "&" + map.get("batch").toString();
//                        if (materielInUnitPriceMap.containsKey(materielIdAndBatch)) {
//                            continue;
//                        }
//                        materielInUnitPriceMap.put(materielIdAndBatch, MathUtils.getBigDecimal(map.get("unitPrice")));
//
//                    }
//
//                    Map<String, Object> map = Maps.newHashMap();
//                    map.put("period", period);
//                    List<StockAnalysisDO> stockAnalysisBatchNonEmptyDOS = stockAnalysisService.list(map).stream()
//                            .filter(stockAnalysis -> stockAnalysis.getBatch() != null)
//                            .collect(Collectors.toList());
//
//                    for (StockOutItemDO itemDO : stockOutBatchNonEmpty) {
//                        String materielIdAndBatch = itemDO.getMaterielId().toString() + "&" + itemDO.getBatch();
//                        if (materielInUnitPriceMap.containsKey(materielIdAndBatch)) {
//                            BigDecimal unitPrice = materielInUnitPriceMap.get(materielIdAndBatch);
//                            BigDecimal count = itemDO.getCount();
//                            BigDecimal amount = itemDO.getCount().multiply(unitPrice);
//                            itemDO.setUnitPrice(unitPrice);
//                            itemDO.setAmount(amount);
//
//                            if (materielOutCountMap.containsKey(materielIdAndBatch)) {
//                                materielOutCountMap.put(materielIdAndBatch, materielOutCountMap.get(materielIdAndBatch).add(count));
//                                materielOutAmountMap.put(materielIdAndBatch, materielOutAmountMap.get(materielIdAndBatch).add(amount));
//                                continue;
//                            }
//                            materielOutCountMap.put(materielIdAndBatch, count);
//                            materielOutAmountMap.put(materielIdAndBatch, amount);
//                        }
//                    }
//
//                    for (StockAnalysisDO stockAnalysisBatchNonEmptyDO : stockAnalysisBatchNonEmptyDOS) {
//                        String materielIdAndBatch = stockAnalysisBatchNonEmptyDO.getMaterielId().toString() + "&" + stockAnalysisBatchNonEmptyDO.getBatch();
//                        if (materielOutCountMap.containsKey(materielIdAndBatch)) {
//                            BigDecimal outCount = materielOutCountMap.get(materielIdAndBatch);
//                            BigDecimal outAmount = materielOutAmountMap.get(materielIdAndBatch);
//                            stockAnalysisBatchNonEmptyDO.setOutCount(outCount);
//                            stockAnalysisBatchNonEmptyDO.setOutAmount(outAmount);
//                            // 期末结存数量=期初数量+本月入库数量-本月发出数量
//                            BigDecimal finalCount = stockAnalysisBatchNonEmptyDO.getInitialCount()
//                                    .add(stockAnalysisBatchNonEmptyDO.getInCount() == null ? BigDecimal.ZERO : stockAnalysisBatchNonEmptyDO.getInCount())
//                                    .subtract(outCount);
//                            stockAnalysisBatchNonEmptyDO.setFinalCount(finalCount);
//                            // 期末结存金额=期初金额+本月入库金额-本月发出金额
//                            BigDecimal finalAmount = stockAnalysisBatchNonEmptyDO.getFinalAmount()
//                                    .add(stockAnalysisBatchNonEmptyDO.getInAmount() == null ? BigDecimal.ZERO : stockAnalysisBatchNonEmptyDO.getInAmount())
//                                    .subtract(outAmount);
//                            stockAnalysisBatchNonEmptyDO.setFinalAmount(finalAmount);
//                        }
//                    }
//                    stockAnalysisService.batchUpdate(stockAnalysisBatchNonEmptyDOS);
//                    stockOutItemService.batchInsert(stockOutBatchEmpty);
//                }
//
                return R.ok();
            }
            return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));

        }
        return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
    }

    @EvApiByToken(value = "/apis/stock/stockOutAccountingCheck", method = RequestMethod.POST, apiTitle = "检验出库核算（检查是否有单价为0的入库单存在）")
    @ApiOperation("检验出库核算（检查是否有单价为0的入库单存在）")
    public R stockOutAccountingCheck(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            Calendar instance = Calendar.getInstance();
            Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
            Date oldPeriod = stockAnalysisDO.getPeriod();
            if (periodTime == null) {
                return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
            }
            instance.setTime(periodTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long newMillis = instance.getTimeInMillis();
            instance.setTime(oldPeriod);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long oldMillis = instance.getTimeInMillis();
            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
            long upOldInMillis = instance.getTimeInMillis();
            if (newMillis == upOldInMillis && stockAnalysisDO.getIsClose() == 0) {
                return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
            }

            if (newMillis == oldMillis) {
                params.clear();
                // 入库单
                params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
                params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
                params.put("auditSign", ConstantForGYL.OK_AUDITED);
                List<Map<String, Object>> stockInList = stockInService.listForMap(params);
                List<String> unitPriceEmpty = stockInList.stream()
                        .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("unitPrice")).compareTo(BigDecimal.ZERO)==0)
                        .map(stringObjectMap -> stringObjectMap.get("inheadCode").toString())
                        .collect(Collectors.toList());

                if (unitPriceEmpty.size() > 0) {
                    String[] args = {unitPriceEmpty.toString()};
                    // -1码为查询出有空的单价的错误码
                    return R.error(-1, messageSourceHandler.getMessage("scm.stockIn.unitPriceEmpty", args));
                }
                return R.ok();
            }
            return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));

        }
        return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
    }

    @EvApiByToken(value = "/apis/stock/checkEndingCarryOver", method = RequestMethod.POST, apiTitle = "检验期末结转（检查是否有单价为0的入库单/出库单存在）")
    @ApiOperation("检验期末结转（检查是否有单价为0的入库单/出库单存在）")
    public R checkEndingCarryOver(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        // 期末结账
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            Calendar instance = Calendar.getInstance();
            Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
            Date oldPeriod = stockAnalysisDO.getPeriod();
            if (periodTime == null) {
                return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
            }
            instance.setTime(periodTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long newMillis = instance.getTimeInMillis();
            instance.setTime(oldPeriod);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long oldMillis = instance.getTimeInMillis();
            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
            long upOldInMillis = instance.getTimeInMillis();
            if (newMillis == oldMillis
                    || (newMillis == upOldInMillis && stockAnalysisDO.getIsClose() == 0)) {
                params.clear();
                // 入库单
                params.put("createStartTime", DatesUtil.getSupportBeginDayOfMonth(periodTime));
                params.put("createEndTime", DatesUtil.getSupportEndDayOfMonth(periodTime));
                params.put("auditSign", ConstantForGYL.OK_AUDITED);
                List<Map<String, Object>> stockInList = stockInService.listForMap(params);
                List<String> stockInUnitPriceEmpty = stockInList.stream()
                        .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("unitPrice")).compareTo(BigDecimal.ZERO)==0)
                        .map(stringObjectMap -> stringObjectMap.get("inheadCode").toString())
                        .collect(Collectors.toList());

                if (stockInUnitPriceEmpty.size() > 0) {
                    String[] args = {stockInUnitPriceEmpty.toString()};
                    // -1码为查询出有空的单价的错误码
                    return R.error(-1, messageSourceHandler.getMessage("scm.stockIn.unitPriceEmpty", args));
                }
                List<StockOutItemDO> stockOutUnitPriceEmpty = stockOutItemService.list(params).stream()
                        .filter(stockOutItemDO -> stockOutItemDO.getUnitPrice().compareTo(BigDecimal.ZERO) == 0)
                        .collect(Collectors.toList());
                if (stockOutUnitPriceEmpty.size() > 0) {
                    // -1码为查询出有空的单价的错误码
                    return R.error(-1, messageSourceHandler.getMessage("scm.stockOut.unitPriceEmpty", null));
                }
                return R.ok();
            }

            return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
        }
        return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endingCarryOver", method = RequestMethod.POST, apiTitle = "期末结转")
    @ApiOperation("期末结转")
    public R endingCarryOver(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        // 期末结账
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            Calendar instance = Calendar.getInstance();
            Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
            Date oldPeriod = stockAnalysisDO.getPeriod();
            if (periodTime == null) {
                return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
            }
            instance.setTime(periodTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long newMillis = instance.getTimeInMillis();
            instance.setTime(oldPeriod);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long oldMillis = instance.getTimeInMillis();
            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
            long upOldInMillis = instance.getTimeInMillis();
            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) + 2);
            if (newMillis == oldMillis
                    || (newMillis == upOldInMillis && stockAnalysisDO.getIsClose() == 0)) {
                params.clear();
                params.put("period", period);
                // 上一期的列表
                List<StockAnalysisDO> lastTerm = stockAnalysisService.list(params);
                List<StockAnalysisDO> stockAnalysisInsertDOS = Lists.newArrayList();
                List<StockAnalysisDO> stockAnalysisUpdateDOS = Lists.newArrayList();
                if (lastTerm.get(0).getIsClose()==1){
                    return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
                }
                if (lastTerm.get(0).getFinalCount()==null){
                    return R.error(messageSourceHandler.getMessage("scm.stock.outError", null));
                }

                for (StockAnalysisDO stockAnalysis : lastTerm) {

                    stockAnalysisDO = new StockAnalysisDO();
                    stockAnalysisDO.setMaterielId(stockAnalysis.getMaterielId());
                    stockAnalysisDO.setBatch(stockAnalysis.getBatch());
                    stockAnalysisDO.setInitialCount(stockAnalysis.getFinalCount());
                    stockAnalysisDO.setInitialAmount(stockAnalysis.getFinalAmount());
                    if (newMillis == upOldInMillis) {
                        stockAnalysisUpdateDOS.add(stockAnalysisDO);
                        continue;
                    }
                    stockAnalysisDO.setIsClose(0);
                    stockAnalysisDO.setDelFlag(0);
                    stockAnalysisDO.setPeriod(instance.getTime());
                    stockAnalysisInsertDOS.add(stockAnalysisDO);
                }

                if (stockAnalysisInsertDOS.size() > 0) {
                    stockAnalysisService.batchInsert(stockAnalysisInsertDOS);
                }
                if (stockAnalysisUpdateDOS.size() > 0) {
                    stockAnalysisService.batchUpdate(stockAnalysisUpdateDOS);
                }
                return R.ok();
            }
            return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
        }
        return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endingClose", method = RequestMethod.POST, apiTitle = "期末关账")
    @ApiOperation("期末关账")
    public R endingClose(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        // 期末结账
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 1);
        List<StockAnalysisDO> list = stockAnalysisService.list(params);
        if (list.size() > 0) {
            StockAnalysisDO stockAnalysisDO = list.get(0);
            Calendar instance = Calendar.getInstance();
            Date periodTime = DateFormatUtil.getDateByParttern(period, "yyyy-MM-dd");
            Date oldPeriod = stockAnalysisDO.getPeriod();
            if (periodTime == null) {
                return R.error(messageSourceHandler.getMessage("scm.stock.timeIsStart", null));
            }
            instance.setTime(periodTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            long newMillis = instance.getTimeInMillis();
            instance.setTime(oldPeriod);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            instance.set(Calendar.MONTH, instance.get(Calendar.MONTH) - 1);
            long upOldInMillis = instance.getTimeInMillis();

            if (newMillis == upOldInMillis && stockAnalysisDO.getIsClose() == 0) {
                params.clear();
                params.put("period", period);
                // 上一期的列表
                List<StockAnalysisDO> lastTerm = stockAnalysisService.list(params);
                if (lastTerm.get(0).getIsClose()==1){
                    return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
                }

                for (StockAnalysisDO stockAnalysis : lastTerm) {
                    stockAnalysis.setIsClose(1);
                }
                stockAnalysisService.batchUpdate(lastTerm);
                return R.ok();
            }
            return R.error(messageSourceHandler.getMessage("scm.stock.carryOver", null));
        }
        return R.error(messageSourceHandler.getMessage("scm.stock.nonUse", null));
    }

    @EvApiByToken(value = "/apis/stock/analysis", method = RequestMethod.POST, apiTitle = "报表分析")
    @ApiOperation("报表分析")
    public R analysis(@ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                      @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                      @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                      @ApiParam(value = "显示已关账期间（1是/0否）") @RequestParam(value = "isClose", defaultValue = "", required = false) Integer isClose
    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("isClose",isClose);
        params.put("materielName", StringUtils.sqlLike(materielName));
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        int total = stockAnalysisService.countForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

}





