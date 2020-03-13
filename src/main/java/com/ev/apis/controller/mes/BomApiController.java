package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.service.ContentAssocService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.mes.domain.BomDO;
import com.ev.mes.service.BomDetailService;
import com.ev.mes.service.BomService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * BOM管理
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-11-19 09:51:41
 */

@Api(value = "/", tags = "BOM管理API")
@RestController
public class BomApiController {
	@Autowired
	private BomService bomService;
	@Autowired
	private BomDetailService bomDetailService;
	@Autowired
	private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
	
	/**
	 * BOM主列表
	 *
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/bom/list", method = RequestMethod.POST, apiTitle = "BOM列表")
	@ApiOperation("BOM主列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "BOM名称") @RequestParam(value = "bomName", defaultValue = "", required = false) String bomName,
			@ApiParam(value = "产品名称") @RequestParam(value = "proName", defaultValue = "", required = false) String proName,
			@ApiParam(value = "图号") @RequestParam(value = "imageNo", defaultValue = "", required = false) String imageNo,
			@ApiParam(value = "状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
			@ApiParam(value = "启用状态(0禁用，1启用)") @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
			@ApiParam(value = "产品名ID") @RequestParam(value = "proId", defaultValue = "", required = false) Long proId

			) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(9);

		params.put("bomName", bomName);
		params.put("proName", proName);
		params.put("imageNo", imageNo);
		params.put("auditSign", auditSign);
		params.put("useStatus", useStatus);

		params.put("proId", proId);

		params.put("delFlag", 0);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = bomService.listForMap(params);
        int total = bomService.countForMap(params);
        if (data.size() > 0) {
			results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
	}

	/**
	 * BOM子列表
	 *
	 * @date 2019-12-03
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/bom/childList", method = RequestMethod.POST, apiTitle = "BOM列表")
	@ApiOperation("BOM子列表")
	public R childList(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "BOM ID", required = true) @RequestParam(value = "bomId", defaultValue = "") Long bomId
			) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);

		params.put("bomId", bomId);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = bomDetailService.listForMap(params);
        int total = bomDetailService.countForMap(params);
        if (data.size() > 0) {
			results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
	}

	/**
	 * 审核BOM
	 * 
	 * @date 2019-12-03
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/audit", method = RequestMethod.POST, apiTitle = "审核BOM")
	@ApiOperation("审核BOM")
	public R audit(
			@ApiParam(value = "BOM主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (bomService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
		}
//		if (!Objects.equals(ShiroUtils.getUserId(), bomService.get(id).getAuditor())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user",null));
//		}
		if (bomService.audit(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 反审核BOM
	 * 
	 * @date 2019-12-03
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核BOM")
	@ApiOperation("反审核BOM")
	public R reverseAudit(
			@ApiParam(value = "BOM主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (!bomService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit",null));
		}
//		if (!Objects.equals(ShiroUtils.getUserId(), bomService.get(id).getAuditor())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user",null));
//		}
		if (bomService.reverseAudit(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 保存
	 * 
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/save", method = RequestMethod.POST, apiTitle = "保存BOM信息")
	@ApiOperation("保存BOM信息")
	public R save(BomDO bom,
			@ApiParam(value = "子物料数组例："+
					"[\r\n" + 
					"    {\r\n" + 
					"        \"materielId\":16,\r\n" +
					"        \"isKeyComponents\":1,\r\n" +
					"        \"standardCount\":10,\r\n" + 
					"        \"wasteRate\":10,\r\n" +
					"        \"processId\":1,\r\n" +
					"        \"stationId\":1,\r\n" +
					"        \"remarks\":\"这里是备注\"\r\n" + 
					"    },\r\n" + 
					"    {\r\n" + 
					"        \"materielId\":17,\r\n" +
					"        \"isKeyComponents\":1,\r\n" +
					"        \"standardCount\":1000,\r\n" + 
					"        \"wasteRate\":20,\r\n" +
					"        \"processId\":1,\r\n" +
					"        \"stationId\":1,\r\n" +
					"        \"remarks\":\"这里是备注\"\r\n" + 
					"    }\r\n" + 
					"]"
					, required = true) @RequestParam(value = "childBomArray", defaultValue = "") String childBomArray,
			      @ApiParam(value = "上传附件") @RequestParam(value = "uploadAttachment",defaultValue = "",required = false) String uploadAttachments) {
		return  bomService.add(bom, childBomArray, uploadAttachments);
	}

	/**
	 * 查看详情
	 * 
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/bom/detail", method = RequestMethod.POST, apiTitle = "查看BOM信息")
	@ApiOperation("查看BOM信息")
	public R detail(
			@ApiParam(value = "BOMId", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(bomService.getDetailInfo(id));
	}

	/**
	 * 修改BOM
	 * 
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/update", method = RequestMethod.POST, apiTitle = "修改BOM")
	@ApiOperation("修改BOM")
	public R update(BomDO bom,
			@ApiParam(value = "子物料数组例："+
					"[\r\n" + 
					"    {\r\n" + 
					"        \"id\":1,\r\n" +
					"        \"isKeyComponents\":1,\r\n" +
					"        \"materielId\":16,\r\n" + 
					"        \"standardCount\":10,\r\n" + 
					"        \"wasteRate\":10,\r\n" +
					"        \"processId\":1,\r\n" +
					"        \"stationId\":1,\r\n" +
					"        \"remarks\":\"这里是备注\"\r\n" + 
					"    },\r\n" + 
					"    {\r\n" + 
					"        \"id\":2,\r\n" +
					"        \"isKeyComponents\":1,\r\n" +
					"        \"materielId\":17,\r\n" + 
					"        \"standardCount\":10,\r\n" + 
					"        \"wasteRate\":10,\r\n" +
					"        \"processId\":1,\r\n" +
					"        \"stationId\":1,\r\n" +
					"        \"remarks\":\"这里是备注\"\r\n" + 
					"    }\r\n" + 
					"]"
			        )@RequestParam(value = "childBomArray", defaultValue = "", required = false) String childBomArray,
                    @ApiParam(value = "被删除的子项目ID") @RequestParam(value = "ids", defaultValue = "", required = false) Long[] ids,
                    @ApiParam(value = "上传附件") @RequestParam(value = "uploadAttachment",defaultValue = "",required = false) String uploadAttachments) {
        return bomService.edit(bom, childBomArray, ids, uploadAttachments);
	}

	/**
	 * 删除（逻辑删除）
	 * 
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/remove", method = RequestMethod.POST, apiTitle = "删除BOM信息")
	@ApiOperation("删除BOM信息")
	public R remove(
			@ApiParam(value = "BOM主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (bomService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("common.approved.delete.disabled",null));
		}
		if (bomService.removeHeadAndBody(id) > 0) {
		    Long[] ids = {id};
            contentAssocService.removeByAssocIdAndType(ids, ConstantForMES.BOM_FILE);
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 批量删除（逻辑删除）
	 * 
	 * @date 2019-12-03 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/bom/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除BOM信息")
	@ApiOperation("批量删除BOM信息")
	public R remove(
			@ApiParam(value = "BOM主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		for (Long id : ids) {
			if (bomService.isAudit(id)) {
                return R.error(messageSourceHandler.getMessage("common.approved.delete.disabled",null));
			}
		}
		bomService.batchRemoveHeadAndBody(ids);
        contentAssocService.removeByAssocIdAndType(ids, ConstantForMES.BOM_FILE);
		return R.ok();
	}


	/**
	 * 导入
	 */
	@ResponseBody
	@EvApiByToken(value = "/apis/importExcel/bom", method = RequestMethod.POST, apiTitle = "bom导入")
	@ApiOperation("bom导入")
	@Transactional(rollbackFor = Exception.class)
	public R readBomFile(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
		return bomService.importExcel(file);
	}

}
