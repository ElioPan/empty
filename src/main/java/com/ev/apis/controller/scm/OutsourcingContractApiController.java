package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.ev.scm.domain.OutsourcingContractDO;
import com.ev.scm.domain.OutsourcingContractItemDO;
import com.ev.scm.domain.SalescontractItemDO;
import com.ev.scm.service.ContractAlterationService;
import com.ev.scm.service.OutsourcingContractService;
import com.ev.scm.service.SalescontractItemService;
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
 * 委外合同控制器层
 * @author gumingjie
 *
 */
@RestController
@Api(value = "/",tags = "委外合同API")
public class OutsourcingContractApiController {

	@Autowired
	private  OutsourcingContractService  outsourcingContractService;
    @Autowired
    private ContractAlterationService contractAlterationService;
    @Autowired
    private ProductionFeedingDetailService productionFeedingDetailService;
    @Autowired
    private ProductionFeedingService productionFeedingService;
    @Autowired
    private SalescontractItemService salescontractItemService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MaterielService materielService;
	
	@EvApiByToken(value = "/apis/outsourcingContract/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加委外合同")
    @ApiOperation("添加/修改委外合同（修改传入id）")
	@Transactional(rollbackFor = Exception.class)
	public R addOrUpdate(OutsourcingContractDO outsourcingContract,
                         @ApiParam(value = "添加/修改委外合同明细:" +
                                 "[\n" +
                                 "    {\n" +
                                 "        \"id\":1,\n" +
                                 "        \"materielId\":50,\n" +
                                 "        \"isCheck\":1,\n" +
                                 "        \"bomId\":15,\n" +
                                 "        \"isQuota\":15,\n" +
                                 "        \"count\":5,\n" +
                                 "        \"taxUnitPrice\":1500,\n" +
                                 "        \"taxRate\":90,\n" +
                                 "        \"taxAmount\":1300,\n" +
                                 "        \"unitPrice\":300,\n" +
                                 "        \"amount\":2000,\n" +
                                 "        \"taxes\":1300,\n" +
                                 "        \"sourceId\":2,\n" +
                                 "        \"sourceType\":89,\n" +
                                 "        \"sourceCode\":\"XSHT20190720001\",\n" +
                                 "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    },\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"materielId\":50,\n" +
                                 "        \"isCheck\":1,\n" +
                                 "        \"bomId\":15,\n" +
                                 "        \"isQuota\":15,\n" +
                                 "        \"count\":5,\n" +
                                 "        \"taxUnitPrice\":1500,\n" +
                                 "        \"taxRate\":90,\n" +
                                 "        \"taxAmount\":1300,\n" +
                                 "        \"unitPrice\":300,\n" +
                                 "        \"amount\":2000,\n" +
                                 "        \"taxes\":1300,\n" +
                                 "        \"sourceId\":2,\n" +
                                 "        \"sourceType\":89,\n" +
                                 "        \"sourceCode\":\"XSHT20190720001\",\n" +
                                 "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    }\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
                         @ApiParam(value = "添加/修改委外合同收款条件:" +
                                 "[\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"payableAmount\":500,\n" +
                                 "        \"paidAmount\":1300,\n" +
                                 "        \"unpaidAmount\":90,\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    },\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"payableAmount\":500,\n" +
                                 "        \"paidAmount\":1300,\n" +
                                 "        \"unpaidAmount\":90,\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    }\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
                              @ApiParam(value = "被删除的委外合同明细ID") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds,
                              @ApiParam(value = "被删除的委外合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds){
        // 与源单数量对比
        List<OutsourcingContractItemDO> itemDOs = JSON.parseArray(bodyItem, OutsourcingContractItemDO.class)
                .stream()
                .filter(outsourcingContractItemDO -> outsourcingContractItemDO.getSourceId()!=null)
                .collect(Collectors.toList());
        Map<Long, BigDecimal> count = Maps.newHashMap();
        Map<Long, Long> sourceIdAndItemId = Maps.newHashMap();
        for (OutsourcingContractItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            sourceIdAndItemId.put(sourceId,itemDO.getId());
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        SalescontractItemDO detailDO;
        BigDecimal contractCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = salescontractItemService.get(sourceId);
                contractCount = detailDO.getCount();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",sourceIdAndItemId.get(sourceId));
                map.put("sourceId",sourceId);
                map.put("sourceType",ConstantForGYL.XSHT);
                BigDecimal bySource = outsourcingContractService.getCountBySource(map);
                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource;
                if (contractCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    List<OutsourcingContractItemDO> collect = itemDOs.stream()
                            .filter(itemDO -> Objects.equals(itemDO.getSourceId(),sourceId))
                            .collect(Collectors.toList());
                    String [] args = {count.get(sourceId).toPlainString(),contractCount.subtract(countByOutSource).toPlainString(),collect.get(0).getSourceCode()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }

		return  outsourcingContractService.addOrUpdateOutsourcingContract(outsourcingContract, bodyItem, bodyPay,itemIds,payIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/batchRemove",method = RequestMethod.POST,apiTitle = "删除委外合同")
    @ApiOperation("批量删除委外合同")
	@Transactional(rollbackFor = Exception.class)
	public R removeOutsourcingContract(
			@ApiParam(value = "委外合同id数组",required = true) @RequestParam(value = "outsourcingContractIds",defaultValue = "") Long[] outsourcingContractIds) {
		return  outsourcingContractService.removeOutsourcingContract(outsourcingContractIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/edit",method = RequestMethod.POST,apiTitle = "变更委外合同")
    @ApiOperation("变更委外合同")
	@Transactional(rollbackFor = Exception.class)
	public R editOutsourcingContract(
            OutsourcingContractDO outsourcingContract,
            @ApiParam(value = "委外合同明细:详情回传过去的JSONArray",
                    required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "添加/修改委外合同付款条件:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"payableAmount\":500,\n" +
                    "        \"paidAmount\":1300,\n" +
                    "        \"unpaidAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"payableAmount\":500,\n" +
                    "        \"paidAmount\":1300,\n" +
                    "        \"unpaidAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    }\n" +
                    "]",
                    required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
            @ApiParam(value = "被删除的委外合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds
    ){
        List<OutsourcingContractItemDO> itemDOs = JSON.parseArray(bodyItem, OutsourcingContractItemDO.class)
                .stream()
                .filter(outsourcingContractItemDO -> outsourcingContractItemDO.getSourceId()!=null)
                .collect(Collectors.toList());
        Map<Long, BigDecimal> count = Maps.newHashMap();
        Map<Long, Long> sourceIdAndItemId = Maps.newHashMap();
        for (OutsourcingContractItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            sourceIdAndItemId.put(sourceId,itemDO.getId());
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        SalescontractItemDO detailDO;
        BigDecimal contractCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = salescontractItemService.get(sourceId);
                contractCount = detailDO.getCount();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",sourceIdAndItemId.get(sourceId));
                map.put("sourceId",sourceId);
                map.put("sourceType",ConstantForGYL.XSHT);
                BigDecimal bySource = outsourcingContractService.getCountBySource(map);
                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource;
                if (contractCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    List<OutsourcingContractItemDO> collect = itemDOs.stream()
                            .filter(itemDO -> Objects.equals(itemDO.getSourceId(),sourceId))
                            .collect(Collectors.toList());
                    String [] args = {count.get(sourceId).toPlainString(),contractCount.subtract(countByOutSource).toPlainString(),collect.get(0).getSourceCode()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }
		return  outsourcingContractService.editOutsourcingContract(outsourcingContract, bodyItem, bodyPay,payIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/outsourcingContractList",method = RequestMethod.GET,apiTitle = "获取委外合同列表")
    @ApiOperation("获取委外合同列表")
	public R outsourcingContractList(
	          @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
              @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			  @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
              @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
              @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
              // 高级查询
              @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
              @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
              @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
              @ApiParam(value = "制单人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,
            // 导入关联单据列表
              @ApiParam(value = "物料编号/物料规格型号/物料名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
              @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime",defaultValue = "",required = false)  String createStartTime,
              @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime",defaultValue = "",required = false)  String createEndTime,

              @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
			  @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("supplierName", StringUtils.sqlLike(supplierName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        // 高级查询
        map.put("contractType", contractType);
        map.put("specification",StringUtils.sqlLike(specification));
        map.put("auditSign", auditSign);
        map.put("createByName", StringUtils.sqlLike(createByName));
        // 导入关联单据列表
        map.put("fuzzyInquire",  StringUtils.sqlLike(fuzzyInquire));
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        map.put("closeStatus",closeStatus);
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data =  outsourcingContractService.listForMap(map);
        Map<String, Object> stringBigDecimalMap = outsourcingContractService.countForMap(map);
        int total = Integer.parseInt(stringBigDecimalMap.getOrDefault("total",0).toString());
        Map<String, Object> result = Maps.newHashMap();
        if (data.size() > 0) {
            DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.WWHT.intValue());
            String thisSourceTypeName = dictionaryDO.getName();
            for (Map<String, Object> datum : data) {
                datum.put("thisSourceType", ConstantForGYL.WWHT);
                datum.put("thisSourceTypeName", thisSourceTypeName);
            }
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
            result.put("total", stringBigDecimalMap);
        }
        return R.ok(result);
	}

    @EvApiByToken(value = "/apis/outsourcingContract/alterationList",method = RequestMethod.GET,apiTitle = "获取委外合同变更列表")
    @ApiOperation("获取委外合同变更列表")
    public R alterationList(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "变更人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,

            @ApiParam(value = "合同Id") @RequestParam(value = "contractId",required = false) Long contractId,
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("createByName", StringUtils.sqlLike(createByName));

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);

        map.put("contractId", contractId);
        map.put("contractType", ConstantForGYL.WWHT);
        List<Map<String, Object>> data = contractAlterationService.listForMap(map);
        Map<String, Object> result = Maps.newHashMap();
        int total = contractAlterationService.countForMap(map);
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/feedingList",method = RequestMethod.GET,apiTitle = "获取委外投料单列表（主）")
    @ApiOperation("获取委外投料单列表（主）")
    public R feedingList(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "委外合同号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "采购员") @RequestParam(value = "purchasePersonName",defaultValue = "",required = false)  String purchasePersonName,
            @ApiParam(value = "采购员ID") @RequestParam(value = "purchasePerson",defaultValue = "",required = false)  Long purchasePerson,

            @ApiParam(value = "合同Id") @RequestParam(value = "contractId",required = false) Long contractId,
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("purchasePersonName", StringUtils.sqlLike(purchasePersonName));
        map.put("purchasePerson", purchasePerson);

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);

        map.put("contractId", contractId);
        List<Map<String, Object>> data = productionFeedingService.listForMapToOutsourcingContract(map);
        Map<String, Object> result = Maps.newHashMap();
        int total = productionFeedingService.countForMapToOutsourcingContract(map);
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/childList", method = RequestMethod.POST, apiTitle = "生产投料列表")
    @ApiOperation("生产投料子项目列表")
    public R childList(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商名") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商Id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "投料单号") @RequestParam(value = "planNo", defaultValue = "", required = false) String planNo,
            @ApiParam(value = "物料名称") @RequestParam(value = "materialsName", defaultValue = "", required = false) String materialsName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,

            @ApiParam(value = "父项产品ID") @RequestParam(value = "headId", defaultValue = "", required = false) Long headId) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("planNo", planNo);
        params.put("materialsName", materialsName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("headId", headId);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("isPlan",0);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = productionFeedingDetailService.listForMap(params);

        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.WWTLD.intValue());
        String thisSourceTypeName = dictionaryDO.getName();
        Map<String, Object> param = Maps.newHashMap();
        param.put("isPc",1);
        // 获取实时库存
        List<Map<String, Object>> stockListForMap = materielService.stockListForMap(param);
        int total = productionFeedingDetailService.countForMap(params);
        if (data.size() > 0) {
            for (Map<String, Object> map : data) {
                map.put("thisSourceType", ConstantForGYL.WWTLD);
                map.put("thisSourceTypeName", thisSourceTypeName);
                double availableCount = 0.0d;
                if (stockListForMap.size() > 0) {
                    for (Map<String, Object> stockList : stockListForMap) {
                        if (Objects.equals(stockList.get("materielId").toString(), map.get("materielId").toString())) {
                            // 如果没有批次要求则查出所有该商品的可用数量累计
                            if (!map.containsKey("batch")) {
                                availableCount += Double.parseDouble(stockList.get("availableCount").toString());
                                continue;
                            }
                            // 若制定了批次则将这一批次的可用数量查出记为实时数量
                            if (Objects.equals(stockList.get("batch").toString(), map.get("batchNo").toString())) {
                                availableCount += Double.parseDouble(stockList.get("availableCount").toString());
                            }

                        }
                    }
                    map.put("availableCount", availableCount);
                }
            }
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
    }

    /**
     * 生产投料列表(生产计划待领列表）
     * 手机端
     *
     * @date 2020-02-26
     * @author gumingjie
     */
    @EvApiByToken(value = "/apis/outsourcingContract/phoneList", method = RequestMethod.POST, apiTitle = "委外投料列表(委外合同待领列表)")
    @ApiOperation("委外投料列表(委外合同待领列表)")
    public R phoneList(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产投料单号") @RequestParam(value = "planNo", defaultValue = "", required = false) String planNo,
            @ApiParam(value = "物料名称") @RequestParam(value = "materialsName", defaultValue = "", required = false) String materialsName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "是否为手机端") @RequestParam(value = "isPhone", defaultValue = "", required = false) Integer isPhone,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "父项产品ID") @RequestParam(value = "headId", defaultValue = "", required = false) Long headId) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("planNo", planNo);
        params.put("materialsName", materialsName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("headId", headId);
        params.put("auditSign", auditSign);
        params.put("isPhone", isPhone);

        params.put("isPlan",0);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = productionFeedingDetailService.phoneListForMap(params);
        int total = productionFeedingDetailService.phoneCountForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

	@EvApiByToken(value = "/apis/outsourcingContract/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return  outsourcingContractService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return  outsourcingContractService.reverseAudit(id);
	}

    @EvApiByToken(value = "/apis/outsourcingContract/close",method = RequestMethod.POST,apiTitle = "关闭接口")
    @ApiOperation("关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R close(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return  outsourcingContractService.close(id);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/reverseClose",method = RequestMethod.POST,apiTitle = "反关闭接口")
    @ApiOperation("反关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R reverseClose(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return  outsourcingContractService.reverseClose(id);
    }
	
	@EvApiByToken(value = "/apis/outsourcingContract/detail",method = RequestMethod.GET,apiTitle = "获取委外合同详细信息")
	@ApiOperation("获取委外合同详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "outsourcingContractId",defaultValue = "")  Long outsourcingContractId) {
	    return   outsourcingContractService.getDetail(outsourcingContractId);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/alterationDetail",method = RequestMethod.GET,apiTitle = "获取委外合同详细信息")
    @ApiOperation("获取委外合同变更详细信息")
    public R alterationDetail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return   outsourcingContractService.getAlterationDetail(id);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/outsourcingContract", method = RequestMethod.GET, apiTitle = "导出合同")
    @ApiOperation("导出合同")
    public void exportExcel(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
            // 高级查询
            @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,
            // 导入关联单据列表
            @ApiParam(value = "物料编号/物料规格型号/物料名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
            @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime",defaultValue = "",required = false)  String createStartTime,
            @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime",defaultValue = "",required = false)  String createEndTime,

            @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("supplierName", StringUtils.sqlLike(supplierName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        // 高级查询
        map.put("contractType", contractType);
        map.put("specification",StringUtils.sqlLike(specification));
        map.put("auditSign", auditSign);
        map.put("createByName", StringUtils.sqlLike(createByName));
        // 导入关联单据列表
        map.put("fuzzyInquire",  StringUtils.sqlLike(fuzzyInquire));
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        map.put("closeStatus",closeStatus);

        List<Map<String, Object>> data =  outsourcingContractService.listForMap(map);
        ClassPathResource classPathResource = new ClassPathResource("poi/outsourcing_contract.xlsx");
        Map<String,Object> param = Maps.newHashMap();
        param.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "委外合同");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, param);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
