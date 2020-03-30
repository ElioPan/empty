package com.ev.apis.controller.custom;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.custom.vo.MaterielEntity;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.scm.vo.StockEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@RestController
@Api(value = "/", tags = "物料管理")
public class MaterielApiController {
    @Autowired
    private MaterielService materielService;
    @Autowired
    private MaterielTypeService materielTypeService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private FacilityLocationService facilityLocationService;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @EvApiByToken(value = "/apis/materiel/list", method = RequestMethod.GET, apiTitle = "获取物料列表信息")
    @ApiOperation("获取物料列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "物料编码") @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
                  @ApiParam(value = "物料名称") @RequestParam(value = "name", defaultValue = "", required = false) String name,
                  @ApiParam(value = "物料名称&&物料编码") @RequestParam(value = "query", defaultValue = "", required = false) String query,
                  @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
                  @ApiParam(value = "计量单位ID") @RequestParam(value = "unitUom", defaultValue = "", required = false) Integer unitUom,
                  @ApiParam(value = "物料类型ID") @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
                  @ApiParam(value = "物料属性ID") @RequestParam(value = "attribute", defaultValue = "", required = false) Integer attribute,
                  @ApiParam(value = "默认仓库") @RequestParam(value = "defaultFacilityName", defaultValue = "", required = false) String defaultFacilityName,
                  @ApiParam(value = "默认仓位") @RequestParam(value = "defaultLocationName", defaultValue = "", required = false) String defaultLocationName,
                  @ApiParam(value = "状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
                  @ApiParam(value = "启用状态(0禁用，1启用)") @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
                  @ApiParam(value = "默认仓库ID") @RequestParam(value = "defaultFacility", defaultValue = "", required = false) Integer defaultFacility,
                  @ApiParam(value = "默认仓位ID") @RequestParam(value = "defaultLocation", defaultValue = "", required = false) Integer defaultLocation) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(12);
        params.put("serialNo", serialNo);
        params.put("name", name);
        params.put("specification", specification);
        params.put("unitUom", unitUom);
        params.put("type", type);
        params.put("attribute", attribute);
        params.put("query", query);
        params.put("defaultFacilityName", defaultFacilityName);
        params.put("defaultLocationName", defaultLocationName);
        params.put("defaultFacility", defaultFacility);
        params.put("defaultLocation", defaultLocation);

        params.put("auditSign", auditSign);
        params.put("useStatus", useStatus);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = this.materielService.listForMap(params);
        int total = materielService.countForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);

    }

    @EvApiByToken(value = "/apis/materiel/typeList", method = RequestMethod.GET, apiTitle = "获取物料类型列表信息")
    @ApiOperation("获取物料类型列表信息")
    public R typeList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                      @ApiParam(value = "物料类型名称") @RequestParam(value = "name", defaultValue = "", required = false) String name) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("name", name);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> data = this.materielTypeService.listForMap(params);
        int total = this.materielTypeService.count(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);

    }

    @EvApiByToken(value = "/apis/materiel/outList", method = RequestMethod.POST, apiTitle = "库存查询")
    @ApiOperation("库存查询")
    public R list(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,

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

        params.put("materielId", materielId);
        params.put("batch", batch);
        params.put("isPc",1);
        // 获取实时库存
        List<Map<String, Object>> data = materielService.stockListForMap(params);
        int total = materielService.stockCountForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/materiel/add", method = RequestMethod.POST)
    @ApiOperation("添加物料")
    public R add(MaterielDO materiel) {
        // 若编号为空 则自动生成

        String serialNo = materiel.getSerialNo();
        if (StringUtils.isEmpty(serialNo) || serialNo.startsWith(Constant.WL)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("maxNo", Constant.WL);
            param.put("offset", 0);
            param.put("limit", 1);

            List<MaterielDO> list = materielService.list(param);
            materiel.setSerialNo(DateFormatUtil.getWorkOrderno(Constant.WL, list.size() > 0 ? list.get(0).getSerialNo() : null, 4));
        }else {
            materiel.setSerialNo(serialNo.trim());
        }


        //  编号不能重复
        if (materielService.checkSave(materiel) == 0) {
            if (materielService.save(materiel) > 0) {
                Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
                result.put("id", materiel.getId());
                return R.ok(result);
            }
            return R.error();
        }
        return R.error(messageSourceHandler.getMessage("common.duplicate.serialNoOrName", null));
    }

    /**
     * 审核物料
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/materiel/audit", method = RequestMethod.POST, apiTitle = "审核物料")
    @ApiOperation("审核物料")
    public R audit(
            @ApiParam(value = "物料主键", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        MaterielDO materielDO = materielService.get(id);
        if (Objects.equals(materielDO.getAuditSign(), ConstantForMES.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
        }
        if (materielService.audit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 反审核物料
     *
     * @date 2019-12-03
     * @author gumingjie
     */
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/materiel/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核物料")
    @ApiOperation("反审核物料")
    public R reverseAudit(
            @ApiParam(value = "物料主键", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        MaterielDO materielDO = materielService.get(id);
        if (Objects.equals(materielDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit", null));
        }
        if (materielService.reverseAudit(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/materiel/addType", method = RequestMethod.POST)
    @ApiOperation("添加物料类型")
    public R addType(MaterielTypeDO materielType) {
        //  类型不能重复
        if (materielTypeService.checkSave(materielType) == 0) {
            if (materielTypeService.save(materielType) > 0) {
                Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
                result.put("id", materielType.getId());
                return R.ok(result);
            }
            return R.error();
        }
        return R.error(messageSourceHandler.getMessage("common.duplicate.names", null));
    }

    @EvApiByToken(value = "/apis/materiel/detail", method = RequestMethod.POST)
    @ApiOperation("获取物料详情")
    public R detail(@ApiParam(value = "物料Id", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        Map<String, Object> results = this.materielService.getDetail(id);
        if (results == null) {
            return R.error();
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/materiel/update", method = RequestMethod.POST)
    @ApiOperation("修改物料")
    public R update(MaterielDO materiel) {
        //  编号不能重复和不能重复名称+规格型号的物料
        if (materielService.checkSave(materiel) == 0) {
            int update = materielService.update(materiel);
            if (update > 0) {
                return R.ok();
            }
            return R.error();
        }
        return R.error(messageSourceHandler.getMessage("common.duplicate.serialNoOrName", null));
    }

    @EvApiByToken(value = "/apis/materiel/updateType", method = RequestMethod.POST)
    @ApiOperation("修改物料类别")
    public R updateType(MaterielTypeDO materielType) {
        MaterielTypeDO materielTypeDO = materielTypeService.get(materielType.getId());
        if(materielTypeDO.getIsSystem()==1){
            return R.error(messageSourceHandler.getMessage("common.system.disable.operate", null));
        }
        if (materielType.getName().equals(materielTypeService.get(materielType.getId()).getName())) {
            int update = materielTypeService.update(materielType);
            if (update > 0) {
                return R.ok();
            }
            return R.error();
        }
        //  类型不能重复
        if (materielTypeService.checkSave(materielType) == 0) {
            int update = materielTypeService.update(materielType);
            if (update > 0) {
                return R.ok();
            }
            return R.error();
        }
        return R.error(messageSourceHandler.getMessage("common.duplicate.names", null));
    }

    @EvApiByToken(value = "/apis/materiel/remove", method = RequestMethod.POST)
    @ApiOperation("删除物料")
    public R remove(@ApiParam(value = "物料Id", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        return materielService.logicRemove(id);
    }

    @EvApiByToken(value = "/apis/materiel/batchRemove", method = RequestMethod.POST)
    @ApiOperation("批量删除物料")
    public R batchRemove(@ApiParam(value = "物料Id数组", required = true) @RequestParam(value = "id", defaultValue = "") Integer[] ids) {
        return materielService.logicBatchRemove(ids);
    }

    @EvApiByToken(value = "/apis/materiel/removeType", method = RequestMethod.POST)
    @ApiOperation("删除物料类型")
    public R removeType(@ApiParam(value = "物料类型Id", required = true) @RequestParam(value = "id", defaultValue = "") Integer id) {
        MaterielTypeDO materielTypeDO = materielTypeService.get(id);
        if(materielTypeDO.getIsSystem()==1){
            return R.error(messageSourceHandler.getMessage("common.system.disable.operate", null));
        }
        Integer[] ids = {id};
        // 有关联物料信息则不能删除
        if (materielTypeService.checkDelete(ids) > 0) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
        }
        if (materielTypeService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/materiel/batchRemoveType", method = RequestMethod.POST)
    @ApiOperation("批量删除物料类型")
    public R batchRemoveType(@ApiParam(value = "物料类型Id数组", required = true) @RequestParam(value = "id", defaultValue = "") Integer[] ids) {
        for(Integer id : ids){
            MaterielTypeDO materielTypeDO = materielTypeService.get(id);
            if(materielTypeDO.getIsSystem()==1){
                return R.error(messageSourceHandler.getMessage("common.system.disable.operate", null));
            }
        }
        // 有关联物料信息则不能删除
        if (materielTypeService.checkDelete(ids) > 0) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
        }
        if (materielTypeService.batchRemove(ids) == ids.length) {
            return R.ok();
        }
        return R.error();
    }

    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/materiel", method = RequestMethod.GET, apiTitle = "导出物料")
    @ApiOperation("导出物料")
    public void exportExcel(
            @ApiParam(value = "物料编码") @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
            @ApiParam(value = "物料名称") @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @ApiParam(value = "物料名称&&物料编码") @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "计量单位ID") @RequestParam(value = "unitUom", defaultValue = "", required = false) Integer unitUom,
            @ApiParam(value = "物料类型ID") @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
            @ApiParam(value = "物料属性ID") @RequestParam(value = "attribute", defaultValue = "", required = false) Integer attribute,
            @ApiParam(value = "默认仓库") @RequestParam(value = "defaultFacilityName", defaultValue = "", required = false) String defaultFacilityName,
            @ApiParam(value = "默认仓位") @RequestParam(value = "defaultLocationName", defaultValue = "", required = false) String defaultLocationName,
            @ApiParam(value = "状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
            @ApiParam(value = "启用状态(0禁用，1启用)") @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
            @ApiParam(value = "默认仓库ID") @RequestParam(value = "defaultFacility", defaultValue = "", required = false) Integer defaultFacility,
            @ApiParam(value = "默认仓位ID") @RequestParam(value = "defaultLocation", defaultValue = "", required = false) Integer defaultLocation,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("serialNo", serialNo);
        param.put("name", name);
        param.put("specification", specification);
        param.put("unitUom", unitUom);
        param.put("type", type);
        param.put("attribute", attribute);
        param.put("query", query);
        param.put("defaultFacilityName", defaultFacilityName);
        param.put("defaultLocationName", defaultLocationName);
        param.put("defaultFacility", defaultFacility);
        param.put("defaultLocation", defaultLocation);

        param.put("auditSign", auditSign);
        param.put("useStatus", useStatus);

        List<Map<String, Object>> data = this.materielService.listForMap(param);
        for (Map<String, Object> datum : data) {
            datum.put("isStockWarning", Integer.parseInt(datum.getOrDefault("isStockWarning", 0).toString()) == 0 ? "否" : "是");
            datum.put("isLot", Integer.parseInt(datum.getOrDefault("isLot", 0).toString()) == 0 ? "否" : "是");
            datum.put("isExpire", Integer.parseInt(datum.getOrDefault("isExpire", 0).toString()) == 0 ? "否" : "是");
        }
        ClassPathResource classPathResource = new ClassPathResource("poi/materiel.xlsx");
        Map<String, Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "物料");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);

        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }
    /*导入*/
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/materiel", method = RequestMethod.POST, apiTitle = "物料信息导入")
    @ApiOperation("物料信息导入")
    @Transactional(rollbackFor = Exception.class)
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String[] importFields = {"物料编码"};
        params.setImportFields(importFields);
        List<MaterielEntity> materielEntityList;
        try {
            materielEntityList = ExcelImportUtil.importExcel(file.getInputStream(), MaterielEntity.class, params);
        }catch(Exception e) {
            return R.error(messageSourceHandler.getMessage("file.upload.error", null));
        }
        if (materielEntityList.size() > 0) {
            List<String> codeNoneEmptyList = materielEntityList.stream()
                    .filter(materielEntity -> StringUtils.isNoneEmpty(materielEntity.getSerialNo()))
                    .map(MaterielEntity::getSerialNo)
                    .collect(Collectors.toList());
            List<String> allCode = materielService.getAllCode();
            if (allCode.size() > 0 && codeNoneEmptyList.size() > 0) {
                allCode.addAll(codeNoneEmptyList);
                List<String> duplicateElements = ListUtils.getDuplicateElements(allCode);
                // 若存在重复的元素提示用户
                if (duplicateElements.size() > 0) {
                    String[] arg = {StringUtils.join(duplicateElements.toArray(), ",")};
                    return R.error(messageSourceHandler.getMessage("basicInfo.code.isPresence", arg));
                }
            }

            Map<String, Object> param = Maps.newHashMap();
            param.put("maxNo", Constant.WL);
            param.put("offset", 0);
            param.put("limit", 1);
            List<MaterielDO> list = materielService.list(param);
            String firstCode = DateFormatUtil.getWorkOrderno(Constant.WL, list.size() > 0 ? list.get(0).getSerialNo() : null, 4);

            List<MaterielEntity> codeEmptyList = materielEntityList.stream()
                    .filter(materielEntity -> StringUtils.isEmpty(materielEntity.getSerialNo()) || materielEntity.getSerialNo().startsWith(Constant.WL)).collect(Collectors.toList());
            for (MaterielEntity materielEntity : codeEmptyList) {
                materielEntity.setSerialNo(firstCode);
                assert firstCode != null;
                firstCode = Constant.WL + StringUtils.autoGenericCode(firstCode.substring(Constant.WL.length()), 4);
            }

            Map<String, Object> emptyMap = Maps.newHashMap();
            List<MaterielTypeDO> typeDOs = materielTypeService.list(emptyMap);
            List<DictionaryDO> attributeDOs = dictionaryService.listByType(Constant.MATERIAL_TYPE);
            List<DictionaryDO> unitUomDOs = dictionaryService.listByType(Constant.UOM_TYPE);
            List<DictionaryDO> valuationMethodDOs = dictionaryService.listByType(Constant.VALUATION_METHOD);
            List<FacilityDO> facilityDOs = facilityService.list(emptyMap);
            List<FacilityLocationDO> locationDOs = facilityLocationService.list(emptyMap);
            List<SupplierDO> supplierDOs = supplierService.list(emptyMap);

            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            List<MaterielDO> materielDOs = Lists.newArrayList();
            MaterielDO materielDO;
            String typeName;
            String attributeName;
            String unitUomName;
            String valuationMethodName;
            String defaultFacilityName;
            String defaultLocationName;
            String supplierName;
            for (MaterielEntity materielEntity : materielEntityList) {
                materielDO = new MaterielDO();
                BeanUtils.copyProperties(materielEntity, materielDO);
                // 物料类别
                typeName = materielEntity.getTypeName();
                for (MaterielTypeDO materielTypeDO : typeDOs) {
                    if (Objects.equals(materielTypeDO.getName(),typeName)) {
                        materielDO.setType(materielTypeDO.getId());
                        break;
                    }
                }

                // 物料属性
                attributeName = materielEntity.getAttributeName();
                for (DictionaryDO attributeDO : attributeDOs) {
                    if (Objects.equals(attributeDO.getName(),attributeName)) {
                        materielDO.setAttribute(attributeDO.getId());
                        break;
                    }
                }

                // 计量单位
                unitUomName = materielEntity.getUnitUomName();
                for (DictionaryDO unitUomDO : unitUomDOs) {
                    if (Objects.equals(unitUomDO.getName(),unitUomName)) {
                        materielDO.setUnitUom(unitUomDO.getId());
                        break;
                    }
                }

                // 计价方法
                valuationMethodName = materielEntity.getValuationMethodName();
                for (DictionaryDO valuationMethodDO : valuationMethodDOs) {
                    if (Objects.equals(valuationMethodDO.getName(),valuationMethodName)) {
                        materielDO.setValuationMethod(valuationMethodDO.getId());
                        break;
                    }
                }

                // 默认仓库
                defaultFacilityName = materielEntity.getDefaultFacilityName();
                defaultLocationName = materielEntity.getDefaultLocationName();
                for (FacilityDO facilityDO : facilityDOs) {
                    if (Objects.equals(facilityDO.getName(),defaultFacilityName)) {
                        Integer facilityDOId = facilityDO.getId();
                        // 默认库位
                        for (FacilityLocationDO locationDO : locationDOs) {
                            if (Objects.equals(locationDO.getName(),defaultLocationName)) {
                                // 若库位不在该仓库中
                                if (Objects.equals(locationDO.getFacilityId(),facilityDOId)) {
                                    materielDO.setDefaultLocation(locationDO.getId());
                                }
                                break;
                            }
                        }
                        materielDO.setDefaultFacility(facilityDOId);
                        break;
                    }
                }

                // 供应商
                supplierName = materielEntity.getSupplierName();
                for (SupplierDO supplierDO: supplierDOs) {
                    if (Objects.equals(supplierDO.getName(),supplierName)) {
                        materielDO.setSupplier(supplierDO.getId().intValue());
                        break;
                    }
                }
                //是否库存预警
                if ("是".equals(materielEntity.getIsStockWarning())) {
                    materielDO.setIsStockWarning(1);
                } else {
                    materielDO.setIsStockWarning(0);
                }

                //是否进行批次管理
                if ("是".equals(materielEntity.getIsLot())) {
                    materielDO.setIsLot(1);
                } else {
                    materielDO.setIsLot(0);
                }

                //是否进行保质期管理
                if ("是".equals(materielEntity.getIsExpire())) {
                    materielDO.setIsExpire(1);
                } else {
                    materielDO.setIsExpire(0);
                }

                materielDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
                // 使用状态(1是0否)
                materielDO.setDelFlag(0);
                materielDO.setUseStatus(1);
                materielDO.setCreateBy(userId);
                materielDO.setCreateTime(now);
                materielDOs.add(materielDO);
            }
            materielService.batchSave(materielDOs);
        }
        return R.ok();
    }
}
