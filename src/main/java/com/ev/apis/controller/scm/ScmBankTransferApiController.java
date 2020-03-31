package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.domain.BankTransferDO;
import com.ev.scm.service.BankTransferService;
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
 * @Author Kuzi
 * @Date 2020-2-7 17:19
 **/
@Api(value = "/",tags = "银行转账单(资金存取)")
@RestController
public class ScmBankTransferApiController {
    @Autowired
    private BankTransferService bankTransferService;


    @EvApiByToken(value = "/apis/scm/bankTransfer/addAndChange", method = RequestMethod.POST, apiTitle = "增加/修改银行转账单")
    @ApiOperation("增加/修改银行转账单")
    @Transactional(rollbackFor = Exception.class)
    public R addAndChangeTransfer(
            BankTransferDO bankTransferDO,
            @ApiParam(value = "明细行：[\n" +
                    "{\n" +
                    "\"id\":\"明细主键（修改时必传）\",\n" +
                    "\"businessType\":\"业务类型\",\n" +
                    "\"transferOutAcc\":\"转出账号(放入引用的id,没有不传)\",\n" +
                    "\"transferInAcc\":\"转入账号(放入引用的id,没有不传)\",\n" +
                    "\"transferAmount\":\"转账金额\",\n" +
                    "\"settlementType\":\"收支类型\",\n" +
                    "\"remarks\":\"备注\"\n" +
                    "}\n" +
                    "]") @RequestParam(value = "transferBodys", defaultValue = "", required = false) String transferBodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", required = false) Long[] deleItemIds) {

        return bankTransferService.addBankTransfer(bankTransferDO, transferBodys, deleItemIds);
    }

    @EvApiByToken(value = "/apis/scm/bankTransfer/detailOfTransfer", method = RequestMethod.POST, apiTitle = "详情—银行转账单")
    @ApiOperation("详情—银行转账单")
    public R detail(@ApiParam(value = "银行转账单id:", required = true) @RequestParam(value = "id") Long id) {
        return bankTransferService.getdetail(id);
    }

    @EvApiByToken(value = "/apis/scm/bankTransfer/audit", method = RequestMethod.POST, apiTitle = "审核—银行转账单")
    @ApiOperation("审核—银行转账单")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "银行转账单id:", required = true) @RequestParam(value = "id") Long id) {
        return bankTransferService.audit(id);
    }

    @EvApiByToken(value = "/apis/scm/bankTransfer/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核—银行转账单")
    @ApiOperation("反审核—银行转账单")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "银行转账单id:", required = true) @RequestParam(value = "id") Long id) {
        return bankTransferService.rollBackAudit(id);
    }

    @EvApiByToken(value = "/apis/scm/bankTransfer/delet", method = RequestMethod.POST, apiTitle = "删除—银行转账单")
    @ApiOperation("删除—银行转账单")
    @Transactional(rollbackFor = Exception.class)
    public R removeTransfers(@ApiParam(value = "银行转账单ids:", required = true) @RequestParam(value = "ids") Long[] ids) {
        return bankTransferService.removeTransfer(ids);
    }

    @EvApiByToken(value = "/apis/scm/bankTransfer/listOfTransfer", method = RequestMethod.POST, apiTitle = "列表—银行转账单")
    @ApiOperation("列表—银行转账单")
    public R transferOflist(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "单据编号") @RequestParam(value = "transferCode", defaultValue = "", required = false) String transferCode,
                            @ApiParam(value = "开始日期(转账时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(转账时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("transferCode", transferCode);
        params.put("auditSign", auditSign);

        List<Map<String, Object>> list = bankTransferService.listForMap(params);
        int count = bankTransferService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        if (!list.isEmpty()) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages((count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/bankTransferGetOut", method = RequestMethod.GET, apiTitle = "导出—银行转账单")
    @ApiOperation("导出—银行转账单")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "transferCode", defaultValue = "", required = false) String transferCode,
            @ApiParam(value = "开始日期(转账时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "截止日期(转账时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("transferCode", transferCode);
        params.put("auditSign", auditSign);
        List<Map<String, Object>> list = bankTransferService.listForMap(params);
        ClassPathResource classPathResource = new ClassPathResource("poi/scm_bank_transfer.xlsx");
        Map<String, Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "银行转账单");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response, TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }



}
