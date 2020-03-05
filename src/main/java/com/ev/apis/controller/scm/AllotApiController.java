package com.ev.apis.controller.scm;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.AllotDO;
import com.ev.scm.service.AllotItemService;
import com.ev.scm.service.AllotService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gumingjie
 * @date 2019/9/18
 */
@Api(value = "/", tags = "库存调拨Api")
@RestController
public class AllotApiController {
	@Autowired
	private AllotService allotService;

	@EvApiByToken(value = "/apis/allot/add", method = RequestMethod.POST, apiTitle = "增加调拨单")
	@ApiOperation("增加调拨单")
	@Transactional(rollbackFor = Exception.class)
	public R add(AllotDO allotDO,
                 @ApiParam(value = "调拨产品:" +
                    "[\n" +
                    "    {\n" +
                    "        \"stockId\":\"2,3\",\n" +
                    "        \"inFacility\":2,\n" +
                    "        \"inLocation\":3,\n" +
                    "        \"count\":15,\n" +
                    "        \"price\":1003000\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"stockId\":\"4,6\",\n" +
                    "        \"inFacility\":1,\n" +
                    "        \"inLocation\":3,\n" +
                    "        \"count\":88,\n" +
                    "        \"price\":5000\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "body", defaultValue = "") String body) throws IOException, ClassNotFoundException {
		return allotService.add(allotDO, body);
	}

	@EvApiByToken(value = "/apis/allot/audit", method = RequestMethod.POST, apiTitle = "审核调拨单")
	@ApiOperation("审核调拨单")
	public R audit(
			@ApiParam(value = "调拨单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
	) {
		return allotService.audit(id, ConstantForGYL.DB);

	}

    @EvApiByToken(value = "/apis/allot/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核调拨单")
    @ApiOperation("反审核调拨单")
    public R reverseAudit(
            @ApiParam(value = "调拨单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id
    ) {
        return allotService.reverseAudit(id, ConstantForGYL.DB);

    }

	@EvApiByToken(value = "/apis/allot/list", method = RequestMethod.POST, apiTitle = "获取调拨单列表")
	@ApiOperation("获取调拨单列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "单据编号、产品名称、产品编号模糊查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
			@ApiParam(value = "单据编号") @RequestParam(value = "allotCode", defaultValue = "", required = false) String allotCode,
			@ApiParam(value = "产品名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
			@ApiParam(value = "开始时间（单据日期）") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
			@ApiParam(value = "调出仓库") @RequestParam(value = "outFacility", defaultValue = "", required = false) Long outFacility,
			@ApiParam(value = "调入仓库") @RequestParam(value = "inFacility", defaultValue = "", required = false) Long inFacility) {
		Long userId = ShiroUtils.getUserId();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("fuzzySearch", fuzzySearch);
		params.put("outFacility", outFacility);
		params.put("inFacility", inFacility);
		params.put("code", allotCode);
		params.put("materielName", StringUtils.sqlLike(materielName));


		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = this.allotService.listForMap(params);
		int total = this.allotService.countForMap(params);
		if (data.size() > 0) {
			results.put("data", new DsResultResponse(pageno,pagesize,total,data));
		}
		return  R.ok(results);
    }

	@EvApiByToken(value = "/apis/allot/getDetail", method = RequestMethod.POST)
	@ApiOperation("获取调拨单详情")
	public R getDetail(
			@ApiParam(value = "调拨单ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(this.allotService.getDetail(id));
	}

	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/allot/edit", method = RequestMethod.POST, apiTitle = "修改调拨单")
	@ApiOperation("修改调拨单")
	public R edit(AllotDO allotDO,
			@ApiParam(value = "修改调拨产品:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":37,\n" +
                    "        \"stockId\":\"2,3\",\n" +
                    "        \"inFacility\":1,\n" +
                    "        \"in_location\":2,\n" +
                    "        \"count\":8,\n" +
                    "        \"price\":1003000\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":38,\n" +
                    "        \"stockId\":\"4,6\",\n" +
                    "        \"inFacility\":1,\n" +
                    "        \"in_location\":3,\n" +
                    "        \"count\":77,\n" +
                    "        \"price\":5000\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"stockId\":7,\n" +
                    "        \"inFacility\":1,\n" +
                    "        \"in_location\":2,\n" +
                    "        \"count\":9,\n" +
                    "        \"price\":5000\n" +
                    "    }\n" +
                    "]"
                    , required = true) @RequestParam(value = "body", defaultValue = "") String body,
                  @ApiParam(value = "调拨明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds
                  ) {
		return allotService.edit(allotDO, body, itemIds);

	}

	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/allot/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除调拨单")
	@ApiOperation("批量删除调拨单")
	public R batchRemove(
			@ApiParam(value = "调拨单ID数组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		return allotService.batchRemoveByIds(ids);
	}


}
