package com.ev.apis.controller.custom;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.MaintenanceRepositoryDO;
import com.ev.custom.service.MaintenanceRepositoryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gumingjie
 * @date 2020/3/10
 */
@Api(value = "/", tags = "维保知识库API")
@RestController
public class MaintenanceRepositoryApiController {
    @Autowired
    private MaintenanceRepositoryService maintenanceRepositoryService;

	@EvApiByToken(value = "/apis/maintenanceRepository/addOrUpdate", method = RequestMethod.POST, apiTitle = "增加/修改维保知识库")
	@ApiOperation("增加/修改维保知识库")
	public R addOrUpdate(MaintenanceRepositoryDO maintenanceRepositoryDO) {
		return maintenanceRepositoryService.addOrUpdate(maintenanceRepositoryDO);
	}

    @EvApiByToken(value = "/apis/maintenanceRepository/list", method = RequestMethod.POST, apiTitle = "获取维保知识库")
    @ApiOperation("获取维保知识库")
    public R list(
            @ApiParam(value = "故障代码") @RequestParam(value = "faultCode", defaultValue = "", required = false) String faultCode,
			@ApiParam(value = "故障类型") @RequestParam(value = "faultType", defaultValue = "", required = false) Long faultType,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize) {
		Map<String, Object> params = Maps.newHashMap();
		Map<String,Object> result = Maps.newHashMap();
		// 列表查询
		params.put("offset",(pageno-1)*pagesize);
		params.put("limit",pagesize);

		params.put("faultCodeSearch", StringUtils.sqlLike(faultCode));
		params.put("faultType", faultType);
		List<Map<String, Object>> data =	maintenanceRepositoryService.listForMap(params);
		int total = maintenanceRepositoryService.count(params);
		if (data.size() > 0) {
			result.put("data", new DsResultResponse(pageno,pagesize,total,data));
		}
		return R.ok(result);
    }

    @EvApiByToken(value = "/apis/maintenanceRepository/detail", method = RequestMethod.POST, apiTitle = "获取维保知识库详情")
    @ApiOperation("获取维保知识库详情")
    public R detail(@ApiParam(value = "维保知识库Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
        return maintenanceRepositoryService.detail(id);
    }

	@EvApiByToken(value = "/apis/maintenanceRepository/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除维保知识库")
	@ApiOperation("批量删除维保知识库")
	public R batchRemove(
			@ApiParam(value = "维保知识库Id组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		return maintenanceRepositoryService.batchDelete(ids);
	}

}
