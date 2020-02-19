package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.scm.domain.StockOutDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.StockOutItemService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author guMingJie
 *
 */
@RestController
@Api(value = "/",tags = "生产领用API")
public class ConsumingStockOutApiController {

	@Autowired
    private StockOutService stockOutService;

    @Autowired
    private StockOutItemService stockOutItemService;

    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private ProductionFeedingDetailService productionFeedingDetailService;

    @Autowired
	private DictionaryService dictionaryService;

	@EvApiByToken(value = "/apis/consumingStockOut/add", method = RequestMethod.POST, apiTitle = "增加生产领用")
	@ApiOperation("增加生产领用")
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
                         "        \"purpose\":\"领料用途\"\n" +
                         "        \"sourceId\":11,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"SCTL20190720001\"\n" +
                         "    },\n" +
                         "    {\n" +
                         "        \"stockId\":\"27,29\",\n" +
                         "        \"materielId\":24,\n" +
                         "        \"batch\":\"wh_001\",\n" +
                         "        \"count\":101,\n" +
                         "        \"unitPrice\":1200,\n" +
                         "        \"amount\":12000,\n" +
                         "        \"purpose\":\"领料用途\"\n" +
                         "        \"sourceId\":25,\n" +
                         "        \"sourceType\":11,\n" +
                         "        \"sourceCode\":\"SCTL20190720001\"\n" +
                         "    }\n" +
                         "]"
                    , required = true)@RequestParam(value = "item",defaultValue = "") String item) {
        // 与源单数量对比
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        for (StockOutItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
            }
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        ProductionFeedingDetailDO detailDO;
        BigDecimal feedingCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                feedingCount = detailDO.getPlanFeeding();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("sourceId",sourceId);
                map.put("sourceType",ConstantForGYL.SCTLD);
                BigDecimal bySource = stockOutItemService.getCountBySource(map);
                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource;
                if (feedingCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    String [] args = {count.get(sourceId).toPlainString(),feedingCount.subtract(countByOutSource).toPlainString()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }


		DictionaryDO storageType = dictionaryService.get(ConstantForGYL.LYCK.intValue());
        return stockOutService.add(stockOutDO, item, storageType);
	}
	
	@EvApiByToken(value = "/apis/consumingStockOut/audit", method = RequestMethod.POST, apiTitle = "审核(反审核)生产领用")
	@ApiOperation("审核(反审核)生产领用")
	@Transactional(rollbackFor = Exception.class)
	public R audit(
			@ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        R audit = stockOutService.audit(id, ConstantForGYL.LYCK);
        // 反写生产投料单数量
        if (Integer.parseInt(audit.get("code").toString())==0) {
            Map<Long, BigDecimal> count = this.getFeedingCountMap(id);
            ProductionFeedingDetailDO detailDO;
            BigDecimal outCount;
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                outCount = detailDO.getOutCount()==null?BigDecimal.ZERO:detailDO.getOutCount();
                detailDO.setOutCount(outCount.add(count.get(sourceId)));
                productionFeedingDetailService.update(detailDO);
            }
        }

        return audit;
	}

    private Map<Long, BigDecimal> getFeedingCountMap(Long id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("outId", id);
        List<StockOutItemDO> list = stockOutItemService.list(map);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        for (StockOutItemDO itemDO : list) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
            }
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        return count;
    }

    @EvApiByToken(value = "/apis/consumingStockOut/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核生产领用")
    @ApiOperation("反审核生产领用")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(
            @ApiParam(value = "出库单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        R reverseAuditForR = stockOutService.reverseAuditForR(id, ConstantForGYL.LYCK);
        // 反写委外投料单数量
        if (Integer.parseInt(reverseAuditForR.get("code").toString())==0) {
            Map<Long, BigDecimal> count = this.getFeedingCountMap(id);
            ProductionFeedingDetailDO detailDO;
            BigDecimal outCount;
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                outCount = detailDO.getOutCount();
                detailDO.setOutCount(outCount.subtract(count.get(sourceId)));
                productionFeedingDetailService.update(detailDO);
            }
        }

        return reverseAuditForR;
    }
	
	@EvApiByToken(value = "/apis/consumingStockOut/batchRemove", method = RequestMethod.POST, apiTitle = "删除生产领用")
	@ApiOperation("批量删除生产领用")
	@Transactional(rollbackFor = Exception.class)
	public R delete(
			@ApiParam(value = "生产领用ID组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids
    ) {
		return stockOutService.batchDelete(ids,ConstantForGYL.LYCK);
	}
	
	@EvApiByToken(value = "/apis/consumingStockOut/edit", method = RequestMethod.POST, apiTitle = "修改生产领用")
	@ApiOperation("修改生产领用(头部ID与明细ID均传)")
	@Transactional(rollbackFor = Exception.class)
	public R edit(StockOutDO stockOutDO,
			@ApiParam(value = "修改生产领用明细" +
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
                    "        \"sourceCode\":\"SCTL20190720001\"\n" +
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
                    "        \"sourceCode\":\"SCTL20190720001\"\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "item", defaultValue = "") String item,
                  @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds) {
        // 与源单数量对比
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        for (StockOutItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
            }
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        ProductionFeedingDetailDO detailDO;
        BigDecimal feedingCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                feedingCount = detailDO.getPlanFeeding();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("sourceId",sourceId);
                map.put("sourceType",ConstantForGYL.SCTLD);
                BigDecimal bySource = stockOutItemService.getCountBySource(map);
                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource;
                if (feedingCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    String [] args = {count.get(sourceId).toPlainString(),feedingCount.subtract(countByOutSource).toPlainString()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }


		return stockOutService.edit(stockOutDO, item, ConstantForGYL.LYCK , itemIds);
	}
	
	@EvApiByToken(value = "/apis/consumingStockOut/advancedQuery", method = RequestMethod.POST, apiTitle = "获取生产领用列表/高级查询")
	@ApiOperation("获取生产领用列表/高级查询")
	public R advancedQuery(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "单据编号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "部门ID" ) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            /*高级查询*/
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "批次") @RequestParam(value = "materielName", defaultValue = "", required = false) String batch,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "领料人") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
            @ApiParam(value = "领料人Id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            @ApiParam(value = "制单人Id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime
			) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("outCode", outCode);
        params.put("deptId", deptId);
        params.put("materielName", StringUtils.sqlLike(materielName));
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        /*高级查询*/
        params.put("specification", StringUtils.sqlLike(specification));
        params.put("batch", batch);
        params.put("auditSign", auditSign);
        params.put("operatorName", StringUtils.sqlLike(operatorName));
        params.put("createByName", StringUtils.sqlLike(createByName));
        params.put("operator", operator);
        params.put("createBy", createBy);
        params.put("createTime", createTime);

        params.put("outboundType", ConstantForGYL.LYCK);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = this.stockOutService.listApi(params);
		int total = this.stockOutService.countApi(params);
		if ( data.size() > 0) {
            DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.LYCK.intValue());
            String thisSourceTypeName = dictionaryDO.getName();
            for (Map<String, Object> datum : data) {
                datum.put("thisSourceType", ConstantForGYL.LYCK);
                datum.put("thisSourceTypeName", thisSourceTypeName);
            }
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
		}
		return R.ok(results);
	}

    @EvApiByToken(value = "/apis/consumingStockOut/getDetail", method = RequestMethod.POST)
    @ApiOperation("获取生产领用单详情")
    public R getDetail(
            @ApiParam(value = "生产领用ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return R.ok(this.stockOutService.getDetail(id));
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/consumingStockOut", method = RequestMethod.GET, apiTitle = "导出生产领用")
    @ApiOperation("导出生产领用")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "outCode", defaultValue = "", required = false) String outCode,
            @ApiParam(value = "部门ID" ) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();

        param.put("outCode", outCode);
        param.put("deptId", deptId);
        param.put("materielName", StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime", endTime);

        param.put("outboundType", ConstantForGYL.LYCK);
        List<Map<String, Object>> data = this.stockOutService.listApi(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/consuming_out_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "生产领用单");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }
}
