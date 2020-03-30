package com.ev.apis.controller.custom;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.ClientDO;
import com.ev.custom.domain.ClientLinkmanDO;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.ClientLinkmanService;
import com.ev.custom.service.ClientService;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.vo.ClientEntity;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.ev.system.service.UserService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangyupeng on 2019-9-24.
 */

@Api(value = "/",tags = "客户管理")
@RestController
public class ClientApiController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLinkmanService clientLinkmanService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @EvApiByToken(value = "/apis/clients/returnClientCode", method = RequestMethod.POST, apiTitle = "增加")
    @ApiOperation("增加" )
    public R retrunCode() {

        Map<String, Object> result = new HashMap<String, Object>();

        String code = "C" + DateFormatUtil.getWorkOrderno();
        result.put("client_code", code);
        return R.ok(result);
    }


    @EvApiByToken(value = "/apis/clients/detalsOneClient", method = RequestMethod.POST, apiTitle = "客户详情")
    @ApiOperation("客户详情")
    public R clientDetals(@ApiParam(value = "主键id", required = false) @RequestParam(value = "Id", required = true) Long id) {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("id", id);
        List<Map<String, Object>> listDetals = clientService.checkClientsByparamete(query);
        if (listDetals.size() > 0) {
            Map<String, Object> mapOne = listDetals.get(0);

            query.put("data", mapOne);
        }else{
            query.put("data", listDetals);
        }
        return R.ok(query);
    }


    @EvApiByToken(value = "/apis/clients/saveChangeDetals", method = RequestMethod.POST, apiTitle = "修改保存")
    @ApiOperation("修改保存")
    @Transactional(rollbackFor = Exception.class)
    public R saveChangeClient(ClientDO clientDo,
                              @ApiParam(value = "联系人", required = false) @RequestParam(value = "linkerMan", required = false) String linkerMan) {

        Map<String,Object>  paramy= new HashMap<>();
        ClientDO clientDO = clientService.get(clientDo.getId());
        if(clientDO!=null){
            if(!Objects.equals(clientDo.getName().trim(),clientDO.getName())){
                paramy.put("name",clientDo.getName().trim());
                int lines = clientService.checkSave(paramy);
                if(lines>0){
                    return  R.error(messageSourceHandler.getMessage("apis.mes.clientSupplier.duplicationOfName",null));
                }
            }
        }else{
            return  R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }

        clientDo.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());
        clientDo.setName(clientDo.getName().trim());

        int rows = clientService.update(clientDo);
        ClientLinkmanDO clientLinkmanDO = new ClientLinkmanDO();
        if (rows > 0) {
            clientLinkmanDO.setClientId(clientDo.getId());
            clientLinkmanDO.setName(linkerMan);
            clientLinkmanDO.setPhone(clientDo.getPhone());
            clientLinkmanService.updateByClientId(clientLinkmanDO);
            return R.ok();
        } else {
            //修改失败
            return R.error("修改失败");
        }
    }


    @EvApiByToken(value = "/apis/clients/addServiceGroup", method = RequestMethod.POST, apiTitle = "新增保存")
    @ApiOperation("新增保存" )
    @Transactional(rollbackFor = Exception.class)
    public R addDetail(ClientDO clientDo,
                         @ApiParam(value = "联系人") @RequestParam(value = "linkerMan", required = false) String linkerMan) {

        Map<String,Object>  paramy= new HashMap<String,Object>();
        paramy.put("name",clientDo.getName().trim());
        int lines = clientService.checkSave(paramy);
        if(lines>0){
            return  R.error(messageSourceHandler.getMessage("apis.mes.clientSupplier.duplicationOfName",null));
        }

        if(StringUtils.isEmpty(clientDo.getCode()) || clientDo.getCode().startsWith(Constant.KH)){
            Map<String,Object> map = Maps.newHashMap();
            map.put("maxNo",Constant.KH);
            map.put("offset", 0);
            map.put("limit", 1);
            List<ClientDO> list = clientService.list(map);
            clientDo.setCode(DateFormatUtil.getWorkOrderno(Constant.KH,list.size()>0?list.get(0).getCode():null,4));
        }

        if(StringUtils.isNotEmpty(clientDo.getCode())&&!(clientDo.getCode().startsWith(Constant.KH))){
            paramy.clear();
            paramy.put("code",clientDo.getCode());
            if(clientService.checkSave(paramy)>0){
                return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> query = new HashMap<String, Object>();
        ClientLinkmanDO clientLinkmanDO = new ClientLinkmanDO();
        clientDo.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());
        clientDo.setName(clientDo.getName().trim());
        int rows = clientService.save(clientDo);
        if (rows > 0) {
            clientLinkmanDO.setClientId(clientDo.getId());
            clientLinkmanDO.setName(linkerMan);
            clientLinkmanDO.setPhone(clientDo.getPhone());
            clientLinkmanService.save(clientLinkmanDO);
            return R.ok();
        } else {
            return R.error();
        }

    }


    @EvApiByToken(value = "/apis/clients/clientSubmit", method = RequestMethod.POST, apiTitle = "审核客户")
    @ApiOperation("审核客户")
    @Transactional(rollbackFor = Exception.class)
    public R submitClient(@ApiParam(value = "客户id", required = true) @RequestParam(value = "id", defaultValue = "", required = true)Long id ){
        ClientDO clientDO = clientService.get(id);
        if(Objects.nonNull(clientDO)){
            if(Objects.equals(clientDO.getStatus(),ConstantForGYL.WAIT_AUDIT.intValue())){
                clientDO.setStatus(ConstantForGYL.OK_AUDITED.intValue());
                clientDO.setAuditId(ShiroUtils.getUserId());
                clientService.update(clientDO);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }

    @EvApiByToken(value = "/apis/clients/goBackSubmit", method = RequestMethod.POST, apiTitle = "反审核客户")
    @ApiOperation("反审核客户")
    @Transactional(rollbackFor = Exception.class)
    public R goBackSubmitClient(@ApiParam(value = "客户id", required = true) @RequestParam(value = "id", defaultValue = "", required = true)Long id ){
        ClientDO clientDO = clientService.get(id);
        if(Objects.nonNull(clientDO)){
            if(Objects.equals(clientDO.getStatus(),ConstantForGYL.OK_AUDITED.intValue())){
                clientDO.setStatus(ConstantForGYL.WAIT_AUDIT.intValue());
                clientDO.setAuditId(0L);
                clientService.update(clientDO);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }
    @EvApiByToken(value = "/apis/clients/listServiceGroup", method = RequestMethod.POST, apiTitle = "列表查询")
    @ApiOperation("列表查询")
    public R listServices(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1", required = true) int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20", required = true) int pagesize,
                          @ApiParam(value = "单位名称/联系人", required = false) @RequestParam(value = "name", defaultValue = "", required = false) String name,
                          @ApiParam(value = "部门id", required = false) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                          @ApiParam(value = "税号", required = false) @RequestParam(value = "taxNumber", defaultValue = "", required = false) String taxNumber,
                          @ApiParam(value = "状态", required = false) @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
                          @ApiParam(value = "审核状态", required = false) @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
                          @ApiParam(value = "单位类型id", required = false) @RequestParam(value = "type", defaultValue = "", required = false) Integer type) {


        String idPath=null;
        if(null!=deptId&&!"".equals(deptId)){
            DeptDO deptDO = deptService.get(deptId);
            idPath=deptDO.getIdPath();
        }
        Map<String, Object> query = Maps.newHashMap();
        Map<String, Object> results = Maps.newHashMap();
        query.put("type", type);
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query.put("name", name);
        query. put("idPath",idPath);
        query.put("linkname", name);
        query.put("name", name);
        query.put("taxNumber", taxNumber);

        query.put("useStatus", useStatus);
        query.put("status", status);

        clientService.count(query);
        List<Map<String, Object>> listDetals = clientService.checkClientsByparamete(query);
        int counts = clientService.countOfCheckClients(query);
        DsResultResponse resultResponse = new DsResultResponse();

        if (listDetals.size() > 0) {
            resultResponse.setDatas(listDetals);
            resultResponse.setPageno(pageno);
            resultResponse.setPagesize(pagesize);
            resultResponse.setTotalRows(counts);
            resultResponse.setTotalPages((Integer) ((counts + pagesize - 1) / pagesize));

            results.put("data", resultResponse);

            return R.ok(results);
        } else {
            results.put("data", resultResponse);
        }
        return R.ok();

    }

    @EvApiByToken(value = "/apis/clients/deletClient", method = RequestMethod.POST, apiTitle = "删除/批量删除")
    @ApiOperation("删除/批量删除")
    @Transactional(rollbackFor = Exception.class)
    public R deletDetail(@ApiParam(value = "客户主键id：Long[]", required = true) @RequestParam(value = "id", required = true) Long[] id) {
        return clientService.deletOfDevices(id);
    }

    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/client", method = RequestMethod.POST, apiTitle = "客户信息导入")
    @ApiOperation("客户信息导入")
    @Transactional(rollbackFor = Exception.class)
    public R readSupplier(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String[] importFields = {"客户编码"};
        params.setImportFields(importFields);
        List<ClientEntity> clientEntityList;
        try {
            clientEntityList  =  ExcelImportUtil.importExcel(file.getInputStream(), ClientEntity.class, params);
        }catch(Exception e) {
            return R.error(messageSourceHandler.getMessage("file.upload.error", null));
        }
        if (clientEntityList.size() > 0) {
            List<String> codeNoneEmptyList = clientEntityList.stream()
                    .filter(clientEntity -> StringUtils.isNoneEmpty(clientEntity.getCode()))
                    .map(ClientEntity::getCode)
                    .collect(Collectors.toList());
            List<String> allCode = clientService.getAllCode();
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
            param.put("maxNo", Constant.KH);
            param.put("offset", 0);
            param.put("limit", 1);
            List<ClientDO> list = clientService.list(param);
            String firstCode = DateFormatUtil.getWorkOrderno(Constant.KH, list.size() > 0 ? list.get(0).getCode() : null, 4);

            List<ClientEntity> codeEmptyList = clientEntityList.stream()
                    .filter(clientEntity -> StringUtils.isEmpty(clientEntity.getCode()) || clientEntity.getCode().startsWith(Constant.KH))
                    .collect(Collectors.toList());
            for (ClientEntity clientEntity : codeEmptyList) {
                clientEntity.setCode(firstCode);
                assert firstCode != null;
                firstCode = Constant.KH + StringUtils.autoGenericCode(firstCode.substring(Constant.KH.length()), 4);
            }

            Map<String,Object> emptyMap = Maps.newHashMap();
            List<DictionaryDO> bankDOs = dictionaryService.listByType(Constant.BANK);
            List<DictionaryDO> clientTypeDOs = dictionaryService.listByType(Constant.CLIENT_TYPE);
            List<DeptDO> deptDOs = deptService.list(emptyMap);
            List<UserDO> userDOs = userService.list(emptyMap);

            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            List<ClientDO> clientDOs = Lists.newArrayList();
            List<ClientLinkmanDO> clientLinkmanDOs = Lists.newArrayList();
            ClientDO clientDO;
            ClientLinkmanDO clientLinkmanDO;
            String bankName;
            String typeName;
            String belongDeptName;
            String belongSalesmanName;

            for (ClientEntity clientEntity : clientEntityList) {
                clientDO = new ClientDO();
                clientLinkmanDO = new ClientLinkmanDO();
                BeanUtils.copyProperties(clientEntity, clientDO);

                bankName = clientEntity.getBankName();
                for (DictionaryDO bankDO : bankDOs) {
                    if (Objects.equals(bankDO.getName(),bankName)) {
                        clientDO.setBank(bankDO.getId());
                        break;
                    }
                }

                typeName = clientEntity.getTypeName();
                for (DictionaryDO clientTypeDO : clientTypeDOs) {
                    if (Objects.equals(clientTypeDO.getName(),typeName)) {
                        clientDO.setType(clientTypeDO.getId());
                        break;
                    }
                }

                belongDeptName = clientEntity.getBelongDeptName();
                for (DeptDO deptDO : deptDOs) {
                    if (Objects.equals(deptDO.getName(),belongDeptName)) {
                        clientDO.setBelongDept(deptDO.getDeptId());
                        break;
                    }
                }

                belongSalesmanName = clientEntity.getBelongSalesmanName();
                for (UserDO userDO : userDOs) {
                    if (Objects.equals(userDO.getName(),belongSalesmanName)) {
                        clientDO.setBelongSalesman(userDO.getUserId());
                        break;
                    }
                }

                clientDO.setStatus(ConstantForMES.WAIT_AUDIT);
                // 使用状态(1是0否)
                clientDO.setDelFlag(0);
                clientDO.setUseStatus(1);
                clientDO.setCreateBy(userId);
                clientDO.setCreateTime(now);
                clientDOs.add(clientDO);

                clientLinkmanDO.setName(clientEntity.getLinkName());
                clientLinkmanDO.setPhone(clientEntity.getLinkPhone());
                clientLinkmanDO.setCreateBy(userId);
                clientLinkmanDO.setCreateTime(now);
                clientLinkmanDOs.add(clientLinkmanDO);
            }
            clientService.batchSave(clientDOs);
            for (int i = 0; i < clientDOs.size(); i++) {
                clientLinkmanDOs.get(i).setClientId(clientDOs.get(i).getId());
            }
            clientLinkmanService.batchSave(clientLinkmanDOs);
        }
        return R.ok();
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/client", method = RequestMethod.GET, apiTitle = "导出客户")
    @ApiOperation("导出客户")
    public void exportExcel(
            @ApiParam(value = "单位名称/联系人") @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @ApiParam(value = "部门id") @RequestParam(value = "deptId", defaultValue = "", required = false) String deptId,
            @ApiParam(value = "税号") @RequestParam(value = "taxNumber", defaultValue = "", required = false) String taxNumber,
            @ApiParam(value = "状态") @RequestParam(value = "useStatus", defaultValue = "", required = false) Integer useStatus,
            @ApiParam(value = "审核状态") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
            @ApiParam(value = "单位类型id") @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        String idPath = null;
        if (StringUtils.isNoneEmpty(deptId)) {
            DeptDO deptDO = deptService.get(Long.parseLong(deptId));
            idPath = deptDO.getIdPath();
        }
        Map<String, Object> query = Maps.newHashMap();
        query.put("idPath", idPath);
        query.put("type", type);
        query.put("name", StringUtils.sqlLike(name));
        query.put("taxNumber", taxNumber);
        query.put("useStatus", useStatus);
        query.put("status", status);

        List<Map<String, Object>> data = clientService.listForMap(query);
        ClassPathResource classPathResource = new ClassPathResource("poi/client.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "客户");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }



}
