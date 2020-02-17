package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.StockOutDO;
import com.ev.scm.service.StockOutService;
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
import java.util.List;
import java.util.Map;

/**
 * 
 * @author guMingJie
 *
 */
@RestController
@Api(value = "/",tags = "采购退货API")
public class PurchaseStockOutApiController {

	@Autowired
    private StockOutService stockOutService;

	@Autowired
	private DictionaryService dictionaryService;

	@EvApiByToken(value = "/apis/purchaseStockOut/add", method = RequestMethod.POST, apiTitle = "增加采购退货")
	@ApiOperation("增加采购退货")
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
                         "        \"purpose\":\"退货原因\",\n" +
                         "        \"sourceId\":11,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"CJHT20190720001\"\n" +
                         "    },\n" +
                         "    {\n" +
                         "        \"stockId\":\"27,29\",\n" +
                         "        \"materielId\":24,\n" +
                         "        \"batch\":\"wh_001\",\n" +
                         "        \"count\":101,\n" +
                         "        \"unitPrice\":1200,\n" +
                         "        \"amount\":12000,\n" +
                         "        \"purpose\":\"退货原因\",\n" +
                         "        \"sourceId\":25,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"CJHT20190720001\"\n" +
                         "    }\n" +
                         "]"
                    , required = true)@RequestParam(value = "item",defaultValue = "") String item) {
		DictionaryDO storageType = dictionaryService.get(ConstantForGYL.CGTH);
        return stockOutService.add(stockOutDO, item, storageType);
	}
	
	@EvApiByToken(value = "/apis/purchaseStockOut/audit", method = RequestMethod.POST, apiTitle = "审核采购退货")
	@ApiOperation("审核采购退货")
	@Transactional(rollbackFor = Exception.class)
	public R audit(
			@ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
		return stockOutService.audit(id, ConstantForGYL.CGTH.longValue());
	}

    @EvApiByToken(value = "/apis/purchaseStockOut/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核采购退货")
    @ApiOperation("反审核采购退货")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(
            @ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        return stockOutService.reverseAuditForR(id, ConstantForGYL.CGTH.longValue());
    }
	
	@EvApiByToken(value = "/apis/purchaseStockOut/batchRemove", method = RequestMethod.POST, apiTitle = "删除采购退货")
	@ApiOperation("批量删除采购退货")
	@Transactional(rollbackFor = Exception.class)
	public R delete(
			@ApiParam(value = "采购退货ID组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids
    ) {
		return stockOutService.batchDelete(ids,ConstantForGYL.CGTH.longValue());
	}
	
	@EvApiByToken(value = "/apis/purchaseStockOut/edit", method = RequestMethod.POST, apiTitle = "修改采购退货")
	@ApiOperation("修改采购退货(头部ID与明细ID均传)")
	@Transactional(rollbackFor = Exception.class)
	public R edit(StockOutDO stockOutDO,
			@ApiParam(value = "修改采购退货明细" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"stockId\":\"28,30\",\n" +
                    "        \"materielId\":25,\n" +
                    "        \"batch\":\"wh_ycl_001\",\n" +
                    "        \"count\":201,\n" +
                    "        \"unitPrice\":1200,\n" +
                    "        \"amount\":24000,\n" +
                    "        \"purpose\":\"退货原因\",\n" +
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
                    "        \"purpose\":\"退货原因\",\n" +
                    "        \"sourceId\":25,\n" +
                    "        \"sourceType\":11,\n" +
                    "        \"sourceCode\":\"CJHT20190720001\"\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "item", defaultValue = "") String item,
                  @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds) {
		return stockOutService.edit(stockOutDO, item, ConstantForGYL.CGTH.longValue() , itemIds);
	}
	
	@EvApiByToken(value = "/apis/purchaseStockOut/advancedQuery", method = RequestMethod.POST, apiTitle = "获取采购退货列表/高级查询")
	@ApiOperation("获取采购退货列表/高级查询")
	public R advancedQuery(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "退货单号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 采购高级查询
            @ApiParam(value = "采购方式(0现购/1赊购)") @RequestParam(value = "salesType", defaultValue = "", required = false) Integer salesType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName

			) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("outCode", outCode);
        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("materielName", StringUtils.sqlLike(materielName));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        // 高级查询
        params.put("salesType", salesType);
        params.put("auditSign", auditSign);
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("createByName", StringUtils.sqlLike(createByName));

        params.put("outboundType", ConstantForGYL.CGTH);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = this.stockOutService.listApi(params);
		int total = this.stockOutService.countApi(params);
		if ( data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
		}
		return R.ok(results);
	}

    @EvApiByToken(value = "/apis/purchaseStockOut/getDetail", method = RequestMethod.POST)
    @ApiOperation("获取采购退货单详情")
    public R getDetail(
            @ApiParam(value = "采购退货ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return R.ok(this.stockOutService.getDetail(id));
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/purchaseStockOut", method = RequestMethod.GET, apiTitle = "导出采购退货")
    @ApiOperation("导出采购退货")
    public void exportExcel(
            @ApiParam(value = "退货单号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            // 采购高级查询
            @ApiParam(value = "采购方式(0现购/1赊购)") @RequestParam(value = "salesType", defaultValue = "", required = false) Integer salesType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();

        param.put("outCode", outCode);
        param.put("supplierName", StringUtils.sqlLike(supplierName));
        param.put("materielName", StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        // 高级查询
        param.put("salesType", salesType);
        param.put("auditSign", auditSign);
        param.put("specification", StringUtils.sqlLike(specification));
        param.put("createByName", StringUtils.sqlLike(createByName));

        param.put("outboundType", ConstantForGYL.CGTH);
        List<Map<String, Object>> data = this.stockOutService.listApi(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/purchase_out_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购退货单");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }
}
