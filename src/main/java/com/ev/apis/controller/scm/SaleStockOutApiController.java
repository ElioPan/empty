package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.*;
import com.ev.scm.domain.StockOutDO;
import com.ev.scm.service.SaleStockOutService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author guMingJie
 *
 */
@RestController
@Api(value = "/",tags = "销售出库API")
public class SaleStockOutApiController {

    @Autowired
    private SaleStockOutService saleStockOutService;

    @Autowired
    private StockInItemService stockInItemService;

    @Autowired
    private StockService stockService;

	@Autowired
	private DictionaryService dictionaryService;

	@EvApiByToken(value = "/apis/salesOutStock/add", method = RequestMethod.POST, apiTitle = "增加销售出库")
	@ApiOperation("增加销售出库")
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
                         "        \"sellUnitPrice\":1200,\n" +
                         "        \"amount\":24000,\n" +
                         "        \"sellAmount\":24000,\n" +
                         "        \"sourceId\":11,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"LYDH20190720001\"\n" +
                         "    },\n" +
                         "    {\n" +
                         "        \"stockId\":\"27,29\",\n" +
                         "        \"materielId\":24,\n" +
                         "        \"batch\":\"wh_001\",\n" +
                         "        \"count\":101,\n" +
                         "        \"unitPrice\":1200,\n" +
                         "        \"sellUnitPrice\":1200,\n" +
                         "        \"amount\":12000,\n" +
                         "        \"sellAmount\":12000,\n" +
                         "        \"sourceId\":25,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"LYDH20190720001\"\n" +
                         "    }\n" +
                         "]"
                    , required = true)@RequestParam(value = "item",defaultValue = "") String item) {
        R r = saleStockOutService.checkSourceNumber(item,stockOutDO.getId());
        DictionaryDO storageType = dictionaryService.get(ConstantForGYL.XSCK);
        return r==null?saleStockOutService.add(stockOutDO, item, storageType):r;
	}
	
	@EvApiByToken(value = "/apis/salesOutStock/audit", method = RequestMethod.POST, apiTitle = "审核销售出库")
	@ApiOperation("审核销售出库")
	@Transactional(rollbackFor = Exception.class)
	public R audit(
			@ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        R audit = saleStockOutService.audit(id, ConstantForGYL.XSCK);
        return audit==null?R.ok():audit;
	}

    @EvApiByToken(value = "/apis/salesOutStock/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核销售出库")
    @ApiOperation("反审核销售出库")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(
            @ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        return saleStockOutService.reverseAuditSaleStockOut(id);
    }
	
	@EvApiByToken(value = "/apis/salesOutStock/batchRemove", method = RequestMethod.POST, apiTitle = "删除销售出库")
	@ApiOperation("批量删除销售出库")
	@Transactional(rollbackFor = Exception.class)
	public R delete(
			@ApiParam(value = "销售出库ID组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids
    ) {
		return saleStockOutService.batchDelete(ids,ConstantForGYL.XSCK);
	}
	
	@EvApiByToken(value = "/apis/salesOutStock/edit", method = RequestMethod.POST, apiTitle = "修改销售出库")
	@ApiOperation("修改销售出库(头部ID与明细ID均传)")
	@Transactional(rollbackFor = Exception.class)
	public R edit(StockOutDO stockOutDO,
			@ApiParam(value = "修改销售出库明细" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"stockId\":\"28,30\",\n" +
                    "        \"materielId\":25,\n" +
                    "        \"batch\":\"wh_ycl_001\",\n" +
                    "        \"count\":201,\n" +
                    "        \"unitPrice\":1200,\n" +
                    "        \"sellUnitPrice\":1200,\n" +
                    "        \"amount\":24000,\n" +
                    "        \"sellAmount\":24000,\n" +
                    "        \"sourceId\":11,\n" +
                    "        \"sourceType\":11,\n" +
                    "        \"sourceCode\":\"LYDH20190720001\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":3,\n" +
                    "        \"stockId\":\"27,29\",\n" +
                    "        \"materielId\":24,\n" +
                    "        \"batch\":\"wh_001\",\n" +
                    "        \"count\":101,\n" +
                    "        \"unitPrice\":1200,\n" +
                    "        \"sellUnitPrice\":1200,\n" +
                    "        \"amount\":12000,\n" +
                    "        \"sellAmount\":12000,\n" +
                    "        \"sourceId\":25,\n" +
                    "        \"sourceType\":11,\n" +
                    "        \"sourceCode\":\"LYDH20190720001\"\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "item", defaultValue = "") String item,
                  @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds) {
        R r = saleStockOutService.checkSourceNumber(item,stockOutDO.getId());
		return r==null?saleStockOutService.edit(stockOutDO, item, ConstantForGYL.XSCK , itemIds):r;
	}
	
	@EvApiByToken(value = "/apis/salesOutStock/advancedQuery", method = RequestMethod.POST, apiTitle = "获取销售出库列表/高级查询")
	@ApiOperation("获取销售出库列表/高级查询")
	public R advancedQuery(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "出库单号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售方式(0现销/1赊销)") @RequestParam(value = "salesType", defaultValue = "", required = false) Integer salesType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "销售员") @RequestParam(value = "salesUserName", defaultValue = "", required = false) String salesUserName,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            // 销售出库导入关联单据
            @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String createStartTime,
            @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String createEndTime
			) {
		Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("outCode", outCode);
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
        params.put("materielName", StringUtils.sqlLike(materielName));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        // 高级查询
        params.put("deptId", deptId);
        params.put("auditSign", auditSign);
        params.put("salesType", salesType);
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("salesUserName", StringUtils.sqlLike(salesUserName));
        params.put("createByName", StringUtils.sqlLike(createByName));
        // 销售出库导入关联单据
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);
        // 出库类型
        params.put("outboundType", ConstantForGYL.XSCK);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = this.saleStockOutService.listApi(params);
        Map<String, Object> maps = this.saleStockOutService.countTotal(params);
        int total = Integer.parseInt(maps.getOrDefault("total",0).toString());
		if ( data.size() > 0) {
            results.put("total",maps);
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
		}
		return R.ok(results);
	}

    @EvApiByToken(value = "/apis/salesOutStock/dialog", method = RequestMethod.POST, apiTitle = "获取销售出库dialog列表")
    @ApiOperation("获取销售出库dialog列表")
    public R dialog(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "5") int pagesize,
            @ApiParam(value = "出库单号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "销售方式(0现销/1赊销)") @RequestParam(value = "salesType", defaultValue = "", required = false) Integer salesType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "销售部门") @RequestParam(value = "materielName", defaultValue = "", required = false) String deptName,
            @ApiParam(value = "销售员") @RequestParam(value = "salesUserName", defaultValue = "", required = false) String salesUserName,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            // 销售出库导入关联单据
            @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String createStartTime,
            @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String createEndTime
    ) {
        Map<String, Object> params = Maps.newHashMap();

        params.put("outCode", outCode);
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
        params.put("materielName", StringUtils.sqlLike(materielName));
        Date periodTime = stockService.getPeriodTime();
        if (periodTime != null) {
            params.put("startTime", DateFormatUtil.getFormateDate(periodTime));
        }
        params.put("endTime", endTime);

        // 高级查询
        params.put("salesType", salesType);
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("deptName", StringUtils.sqlLike(deptName));
        params.put("salesUserName", StringUtils.sqlLike(salesUserName));
        params.put("createByName", StringUtils.sqlLike(createByName));
        // 销售出库导入关联单据
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);


        // 出库类型
        params.put("auditSign", Constant.OK_AUDITED);
        params.put("outboundType", ConstantForGYL.XSCK);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.saleStockOutService.listApi(params);
        if (data.size() > 0) {
            Map<String, Object> sourceParam;
            BigDecimal bySource;
            for (Map<String, Object> datum : data) {
                sourceParam = Maps.newHashMap();
                sourceParam.put("sourceId", datum.get("itemId"));
                sourceParam.put("sourceType", ConstantForGYL.XSCK);
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
                    .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("quoteCount")).compareTo(BigDecimal.ZERO)>0)
                    .collect(Collectors.toList());
            if (quoteLists.size() > 0) {
                DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.XSCK);
                String thisSourceTypeName = dictionaryDO.getName();
                List<Map<String, Object>> quoteList = PageUtils.startPage(quoteLists, pageno, pagesize);
                for (Map<String, Object> stringObjectMap : quoteList) {
                    stringObjectMap.put("thisSourceType", ConstantForGYL.XSCK);
                    stringObjectMap.put("thisSourceTypeName", thisSourceTypeName);
                }
                results.put("data", new DsResultResponse(pageno, pagesize, quoteLists.size(), quoteList));
            }
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/salesOutStock/getDetail", method = RequestMethod.POST)
    @ApiOperation("获取销售出库单详情")
    public R getDetail(
            @ApiParam(value = "销售出库ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return R.ok(this.saleStockOutService.getDetail(id));
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesOutStock", method = RequestMethod.GET, apiTitle = "导出销售出库")
    @ApiOperation("导出销售出库")
    public void exportExcel(
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "出库单号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 高级查询
            @ApiParam(value = "销售方式(0现销/1赊销)") @RequestParam(value = "salesType", defaultValue = "", required = false) Integer salesType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "销售部门") @RequestParam(value = "materielName", defaultValue = "", required = false) String deptName,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "销售员") @RequestParam(value = "salesUserName", defaultValue = "", required = false) String salesUserName,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();

        param.put("deptId", deptId);
        param.put("outCode", outCode);
        param.put("clientName", StringUtils.sqlLike(clientName));
        param.put("materielName", StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        // 高级查询
        param.put("auditSign", auditSign);
        param.put("salesType", salesType);
        param.put("specification", StringUtils.sqlLike(specification));
        param.put("deptName", StringUtils.sqlLike(deptName));
        param.put("salesUserName", StringUtils.sqlLike(salesUserName));
        param.put("createByName", StringUtils.sqlLike(createByName));

        param.put("outboundType", ConstantForGYL.XSCK);
        List<Map<String, Object>> data = this.saleStockOutService.listApi(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/sales_out_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售出库单");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }
}
