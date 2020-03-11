package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.*;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.ev.scm.domain.StockOutDO;
import com.ev.scm.service.OutsourcingStockOutService;
import com.ev.scm.service.StockInItemService;
import com.ev.scm.service.StockService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 * @author guMingJie
 *
 */
@RestController
@Api(value = "/",tags = "委外出库单API")
public class OutsourcingStockOutApiController {

    @Autowired
    private OutsourcingStockOutService outsourcingStockOutService;

    @Autowired
    private StockInItemService stockInItemService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductionFeedingDetailService feedingDetailService;

    @Autowired
    private ProductionFeedingService productionFeedingService;

	@Autowired
	private DictionaryService dictionaryService;

	@EvApiByToken(value = "/apis/outsourcingStockOut/add", method = RequestMethod.POST, apiTitle = "增加委外出库单")
	@ApiOperation("增加委外出库单")
	@Transactional(rollbackFor = Exception.class)
	public R add(StockOutDO stockOutDO,
                 @ApiParam(value = "出库明细:" +
                    "[\n" +
                         "    {\n" +
                         "        \"stockId\":\"28,30\",\n" +
                         "        \"materielId\":25,\n" +
                         "        \"batch\":\"wh_ycl_001\",\n" +
                         "        \"count\":201,\n" +
                         "        \"unitPrice\":1200,\n" +
                         "        \"amount\":24000,\n" +
                         "        \"chargeOffCount\":201,\n" +
                         "        \"purpose\":\"发料用途\"\n" +
                         "        \"sourceId\":11,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"WWTL0001\"\n" +
                         "    },\n" +
                         "    {\n" +
                         "        \"stockId\":\"27,29\",\n" +
                         "        \"materielId\":24,\n" +
                         "        \"batch\":\"wh_001\",\n" +
                         "        \"count\":101,\n" +
                         "        \"unitPrice\":1200,\n" +
                         "        \"amount\":12000,\n" +
                         "        \"chargeOffCount\":201,\n" +
                         "        \"purpose\":\"发料用途\"\n" +
                         "        \"sourceId\":25,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"WWTL0001\"\n" +
                         "    }\n" +
                         "]"
                    , required = true)@RequestParam(value = "item",defaultValue = "") String item) {
        R r = outsourcingStockOutService.checkSourceNumber(item,stockOutDO.getId());
        DictionaryDO storageType = dictionaryService.get(ConstantForGYL.WWCK.intValue());
        return r==null?outsourcingStockOutService.add(stockOutDO, item, storageType):r;
	}
	
	@EvApiByToken(value = "/apis/outsourcingStockOut/audit", method = RequestMethod.POST, apiTitle = "审核委外出库单")
	@ApiOperation("审核委外出库单")
	@Transactional(rollbackFor = Exception.class)
	public R audit(
			@ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        return outsourcingStockOutService.auditOutsourcingStockOut(id);
	}

    @EvApiByToken(value = "/apis/outsourcingStockOut/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核委外出库单")
    @ApiOperation("反审核委外出库单")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(
            @ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        return outsourcingStockOutService.reverseAuditOutsourcingStockOut(id);
    }
	
	@EvApiByToken(value = "/apis/outsourcingStockOut/batchRemove", method = RequestMethod.POST, apiTitle = "删除委外出库单")
	@ApiOperation("批量删除委外出库单")
	@Transactional(rollbackFor = Exception.class)
	public R delete(
			@ApiParam(value = "委外出库单ID组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids
    ) {
		return outsourcingStockOutService.batchDelete(ids,ConstantForGYL.WWCK);
	}
	
	@EvApiByToken(value = "/apis/outsourcingStockOut/edit", method = RequestMethod.POST, apiTitle = "修改委外出库单")
	@ApiOperation("修改委外出库单(头部ID与明细ID均传)")
	@Transactional(rollbackFor = Exception.class)
	public R edit(StockOutDO stockOutDO,
			@ApiParam(value = "修改委外出库单明细" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"stockId\":\"28,30\",\n" +
                    "        \"materielId\":25,\n" +
                    "        \"batch\":\"wh_ycl_001\",\n" +
                    "        \"count\":201,\n" +
                    "        \"unitPrice\":1200,\n" +
                    "        \"amount\":24000,\n" +
                    "        \"sourceId\":11,\n" +
                    "        \"sourceType\":11,\n" +
                    "        \"sourceCode\":\"CJHT20190720001\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":3,\n" +
                    "        \"stockId\":\"27,29\",\n" +
                    "        \"materielId\":24,\n" +
                    "        \"batch\":\"wh_001\",\n" +
                    "        \"count\":101,\n" +
                    "        \"unitPrice\":1200,\n" +
                    "        \"amount\":12000,\n" +
                    "        \"sourceId\":25,\n" +
                    "        \"sourceType\":11,\n" +
                    "        \"sourceCode\":\"CJHT20190720001\"\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "item", defaultValue = "") String item,
                  @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds
    ) {
        R r = outsourcingStockOutService.checkSourceNumber(item,stockOutDO.getId());
		return r==null?outsourcingStockOutService.edit(stockOutDO, item, ConstantForGYL.WWCK , itemIds):r;
	}
	
	@EvApiByToken(value = "/apis/outsourcingStockOut/dialog/writeOff", method = RequestMethod.POST, apiTitle = "获取委外出库核销列表")
	@ApiOperation("获取委外出库核销列表")
	public R writeOffList(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "委外入库单明细ID", required = true) @RequestParam(value = "stockInItemId", defaultValue = "") Long stockInItemId
			) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("outboundType", ConstantForGYL.WWCK);
        // 核销导入列表
        Long outsourcingContractItemId = stockInItemService.get(stockInItemId).getSourceId();
        if (outsourcingContractItemId != null) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("outsourceContractItemId", outsourcingContractItemId);
            List<ProductionFeedingDO> list = productionFeedingService.list(map);
            if (list.size() > 0) {
                Long feedingId = list.get(0).getId();
                map.put("headId", feedingId);
                List<Long> feedingDetailIds = feedingDetailService.list(map).stream()
                        .map(ProductionFeedingDetailDO::getId)
                        .collect(Collectors.toList());
                if (feedingDetailIds.size() > 0) {
                    params.put("sourceIds", feedingDetailIds);
                    List<Map<String, Object>> data = this.outsourcingStockOutService.listApi(params);
                    Map<String, Object> maps = this.outsourcingStockOutService.countTotal(params);
                    int total = Integer.parseInt(maps.getOrDefault("total", 0).toString());
                    if (data.size() > 0) {
                        results.put("data", new DsResultResponse(pageno, pagesize, total, data));
                        results.put("total", maps);
                    }
                }
            }
        }
        return R.ok(results);
	}

    @EvApiByToken(value = "/apis/outsourcingStockOut/advancedQuery", method = RequestMethod.POST, apiTitle = "获取委外出库单列表/高级查询")
    @ApiOperation("获取委外出库单列表/高级查询")
    public R advancedQuery(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "单据编号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "发料部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务类型") @RequestParam(value = "outboundType", defaultValue = "", required = false) Integer outboundType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "出库员") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
            @ApiParam(value = "出库员Id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            @ApiParam(value = "制单人Id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime

    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("outCode", outCode);
        params.put("clientId", clientId);
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("materielName", StringUtils.sqlLike(materielName));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        /*高级查询*/
        params.put("deptId", deptId);
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("batch", batch);
        params.put("auditSign", auditSign);
        params.put("operatorName", StringUtils.sqlLike(operatorName));
        params.put("createByName", StringUtils.sqlLike(createByName));
        params.put("operator", operator);
        params.put("createBy", createBy);
        params.put("createTime", createTime);
        params.put("outboundType", Objects.isNull(outboundType)?ConstantForGYL.WWCK:outboundType);

        List<Map<String, Object>> data = this.outsourcingStockOutService.listApi(params);
        Map<String, Object> map = this.outsourcingStockOutService.countTotal(params);
        int total = Integer.parseInt(map.getOrDefault("total",0).toString());
        if ( data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
            results.put("total",map);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcingStockOut/dialog", method = RequestMethod.POST, apiTitle = "获取委外出库单dialog列表")
    @ApiOperation("获取委外出库单dialog列表")
    public R dialogList(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "单据编号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "发料部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "出库员") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
            @ApiParam(value = "出库员Id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            @ApiParam(value = "制单人Id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime

    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("outCode", outCode);
        params.put("clientId", clientId);
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("materielName", StringUtils.sqlLike(materielName));
        params.put("startTime", DateFormatUtil.getFormateDate(stockService.getPeriodTime()));
        params.put("endTime", endTime);
        /*高级查询*/
        params.put("deptId", deptId);
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("batch", batch);
        params.put("operatorName", StringUtils.sqlLike(operatorName));
        params.put("createByName", StringUtils.sqlLike(createByName));
        params.put("operator", operator);
        params.put("createBy", createBy);
        params.put("createTime", createTime);

        params.put("auditSign", ConstantForGYL.OK_AUDITED);
        params.put("outboundType", ConstantForGYL.WWCK);

        List<Map<String, Object>> data = this.outsourcingStockOutService.listApi(params);
        if (data.size() > 0) {
            Map<String, Object> sourceParam;
            BigDecimal bySource;
            for (Map<String, Object> datum : data) {
                sourceParam = Maps.newHashMap();
                sourceParam.put("sourceId", datum.get("id"));
                sourceParam.put("sourceType", ConstantForGYL.WWCK);
                bySource = stockInItemService.getInCountOfContract(sourceParam);

                BigDecimal countByOutSource = bySource == null ? BigDecimal.ZERO : bySource;
                BigDecimal count = MathUtils.getBigDecimal(datum.get("count")).subtract(countByOutSource);
                if (count.compareTo(BigDecimal.ZERO) <= 0) {
                    datum.put("quoteCount", 0);
                } else {
                    datum.put("quoteCount", count);
                }
            }
            List<Map<String, Object>> quoteLists = data
                    .stream()
                    .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("quoteCount")).compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
            if (quoteLists.size() > 0) {
                DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.WWCK.intValue());
                String thisSourceTypeName = dictionaryDO.getName();
                List<Map<String, Object>> quoteList = PageUtils.startPage(quoteLists, pageno, pagesize);
                for (Map<String, Object> stringObjectMap : quoteList) {
                    stringObjectMap.put("thisSourceType", ConstantForGYL.WWCK);
                    stringObjectMap.put("thisSourceTypeName", thisSourceTypeName);
                }
                results.put("data", new DsResultResponse(pageno, pagesize, quoteLists.size(), quoteList));
            }
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcingStockOut/getDetail", method = RequestMethod.POST)
    @ApiOperation("获取委外出库单单详情")
    public R getDetail(
            @ApiParam(value = "委外出库单ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return R.ok(this.outsourcingStockOutService.getDetail(id));
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/outsourcingStockOut", method = RequestMethod.GET, apiTitle = "导出委外出库单")
    @ApiOperation("导出委外出库单")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "业务类型") @RequestParam(value = "outboundType", defaultValue = "", required = false) Integer outboundType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "出库员") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
            @ApiParam(value = "出库员Id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            @ApiParam(value = "制单人Id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();

        param.put("outCode", outCode);
        param.put("supplierName", StringUtils.sqlLike(clientName));
        param.put("materielName", StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime", endTime);

        /*高级查询*/
        param.put("specification", StringUtils.sqlLike(specification));
        param.put("batch", batch);
        param.put("auditSign", auditSign);
        param.put("operatorName", StringUtils.sqlLike(operatorName));
        param.put("createByName", StringUtils.sqlLike(createByName));
        param.put("operator", operator);
        param.put("createBy", createBy);
        param.put("createTime", createTime);

        param.put("outboundType", Objects.isNull(outboundType)?ConstantForGYL.WWCK:outboundType);
        List<Map<String, Object>> data = this.outsourcingStockOutService.listApi(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/other_out_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "委外出库单");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }


}
