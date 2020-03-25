package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.ContractAlterationDao;
import com.ev.scm.dao.SalescontractDao;
import com.ev.scm.dao.SalescontractItemDao;
import com.ev.scm.dao.SalescontractPayDao;
import com.ev.scm.domain.ContractAlterationDO;
import com.ev.scm.domain.SalescontractDO;
import com.ev.scm.domain.SalescontractItemDO;
import com.ev.scm.domain.SalescontractPayDO;
import com.ev.scm.service.SalescontractService;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;
import com.ev.scm.vo.ContractVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class SalescontractServiceImpl implements SalescontractService {
    @Autowired
    private SalescontractDao salesContractDao;

    @Autowired
    private SalescontractPayDao salesContractPayDao;

    @Autowired
    private SalescontractItemDao salesContractItemDao;

    @Autowired
    private ContractAlterationDao contractAlterationDao;

    @Autowired
    private MaterielService materielService;

    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @Override
    public R addOrUpdateSalesContract(SalescontractDO salesContract, String bodyItem, String bodyPay, Long[] itemIds, Long[] payIds) {
        Map<String, Object> result = Maps.newHashMap();
        Long id = salesContract.getId();
        // 新增
        if (id == null) {
            salesContract.setAuditSign(ConstantForGYL.WAIT_AUDIT);
            salesContract.setCloseStatus(0);
            salesContract.setContractCode(this.salesContractCode());
            salesContract.setUninvoicedAmount(salesContract.getPayAmount());
            salesContract.setInvoicedAmount(BigDecimal.ZERO);
            if (salesContract.getDiscountRate() == null) {
                salesContract.setDiscountRate(BigDecimal.ZERO);
            }
            if (salesContract.getDiscountAmount() == null) {
                salesContract.setDiscountAmount(BigDecimal.ZERO);
            }
            salesContractDao.save(salesContract);
            id = salesContract.getId();
        } else {
            // 修改
            // 验证是否能修改 CloseStatus 0 未关闭 1 关闭
            SalescontractDO salescontractDO = this.get(id);
            if (!Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
                return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
            }
            salesContractDao.update(salesContract);
        }

        // 删除子项目内的数据
        if (itemIds.length > 0) {
            salesContractItemDao.batchRemove(itemIds);
        }

        // 删除合同首款条件数据
        if (payIds.length > 0) {
            salesContractPayDao.batchRemove(payIds);
        }

        // 保存销售合同收款条件
        List<SalescontractPayDO> pay = JSON.parseArray(bodyPay, SalescontractPayDO.class);
        for (SalescontractPayDO payData : pay) {
            payData.setUnpayAmount(payData.getReceivableAmount());
            // 新增
            if (payData.getId() == null) {
                payData.setSalescontractId(id);
                salesContractPayDao.save(payData);
            } else {
                // 修改
                salesContractPayDao.update(payData);
            }
        }

        // 保存销售合同子项目表
        List<SalescontractItemDO> item = JSON.parseArray(bodyItem, SalescontractItemDO.class);
        for (SalescontractItemDO itemData : item) {
            // 新增
            if (itemData.getId() == null) {
                itemData.setSalescontractId(id);
                salesContractItemDao.save(itemData);
            } else {
                // 修改
                salesContractItemDao.update(itemData);
            }
        }
        result.put("id", id);
        return R.ok(result);
    }

    @Override
    public R removeSalesContract(Long[] salesContractIds) {
        SalescontractDO salescontractDO;
        if (salesContractIds.length > 0) {
            for (Long salesContractId : salesContractIds) {
                salescontractDO = this.get(salesContractId);
                if (!Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
                    return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled", null));
                }
            }
            this.batchRemove(salesContractIds);
            salesContractItemDao.batchRemoveBySalesContractIds(salesContractIds);
            salesContractPayDao.batchRemoveBySalesContractIds(salesContractIds);
            return R.ok();
        }
        return R.error();
    }


    @Override
    public R audit(Long id) {
        SalescontractDO salescontractDO = this.get(id);
        if (salescontractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }
        if (Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
        }
        salescontractDO.setAuditSign(ConstantForGYL.OK_AUDITED);
        salescontractDO.setAuditor(ShiroUtils.getUserId());
        salescontractDO.setAuditTime(new Date());
        return this.update(salescontractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R reverseAudit(Long id) {
        SalescontractDO salescontractDO = this.get(id);
        if (Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
        }
        if (salescontractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }
        if (this.childCount(id)>0) {
            return R.error(messageSourceHandler.getMessage("scm.childList.reverseAudit", null));
        }

//        if (!Objects.equals(salescontractDO.getAuditor(), ShiroUtils.getUserId())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user", null));
//        }
        salescontractDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
        salescontractDO.setAuditor(null);
        salescontractDO.setAuditTime(null);
        return this.updateAll(salescontractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R getDetail(Long salesContractId) {
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> salesContract = salesContractDao.getDetail(salesContractId);
        result.put("salesContract", salesContract);

        Map<String, Object> params = Maps.newHashMap();
        params.put("salescontractId", salesContractId);
        // 销售合同物料列表详情
        List<Map<String, Object>> salesContractItemList = salesContractItemDao.listByContract(params);
        Map<String, Object> salesContractItemCount = salesContractItemDao.countByContract(params);
        result.put("salesContractItemList", salesContractItemList);
        result.put("salesContractItemCount", salesContractItemCount);

        // 销售合同收款条件
        List<SalescontractPayDO> salesContractPayList = salesContractPayDao.list(params);
        Map<String, Object> salesContractPayCount = salesContractPayDao.countByContract(params);
        result.put("salesContractPayList", salesContractPayList);
        result.put("salesContractPayCount", salesContractPayCount);

        return R.ok(result);
    }

    @Override
    public R editSalesContract(SalescontractDO salesContract, String bodyItem, String bodyPay, Long[] payIds) {
        Long salesContractId = salesContract.getId();
        SalescontractDO salescontractDO = this.get(salesContractId);
        if (Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.contract.isUpdate.notAlteration", null));
        }
        if (salescontractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("salescontractId", salesContractId);
        // 销售合同物料列表详情
        List<SalescontractItemDO> salesContractItemList = salesContractItemDao.list(params);
        // 销售合同收款条件
        List<SalescontractPayDO> salesContractPayList = salesContractPayDao.list(params);
        JSONObject alterationContent = new JSONObject();

        List<ContractItemVO> itemList = this.getContractItemVOS(bodyItem, salesContractItemList);
        alterationContent.put("itemArray", itemList);

        for (SalescontractPayDO salescontractPayDO : salesContractPayList) {
            salescontractPayDO.setSalescontractId(salesContractId);
        }
        List<ContractPayVO> payList = this.getContractPayVOS(salesContractId,bodyPay, payIds, salesContractPayList);
        alterationContent.put("payArray", payList);

        ContractVO head = this.getContractHeadVOS(salesContract,salescontractDO);
        alterationContent.put("head", head);

        if (itemList.size() > 0 || payList.size() > 0) {
            ContractAlterationDO alterationDO = new ContractAlterationDO();
            alterationDO.setAlterationContent(alterationContent.toJSONString());
            alterationDO.setContractId(salesContractId);
            alterationDO.setContractCode(this.get(salesContractId).getContractCode());
            alterationDO.setContractType(ConstantForGYL.XSHT);
            contractAlterationDao.save(alterationDO);
        }
        this.update(salesContract);
        return R.ok();
    }

    @Override
    public List<ContractPayVO> getContractPayVOS(Long salesContractId, String bodyPay, Long[] payIds, List<SalescontractPayDO> salesContractPayList) {
        List<ContractPayVO> payList = Lists.newArrayList();
        ContractPayVO payVO;
        if (salesContractPayList.size() > 0) {
            // 若合同收款条件变了则执行修改操作
            // 若被删除保存删除前的信息
            if (payIds.length > 0) {
                List<SalescontractPayDO> collect = salesContractPayList.stream().filter(salesContractPayDO -> {
                    for (Long payId : payIds) {
                        return Objects.equals(salesContractPayDO.getId(), payId);
                    }
                    return false;
                }).collect(Collectors.toList());
                // 保存删除了的信息
                for (SalescontractPayDO salescontractPayDO : collect) {
                    payVO = new ContractPayVO();
                    payVO.setId(salescontractPayDO.getId());
                    payVO.setReceivableDateAfter(DateFormatUtil.getFormateDate(salescontractPayDO.getReceivableDate()));
                    payVO.setReceivableDateBefore("");
                    payVO.setReceivableAmountAfter(salescontractPayDO.getReceivableAmount().stripTrailingZeros().toPlainString());
                    payVO.setReceivableAmountBefore("");
                    payVO.setType("删除");
                    payList.add(payVO);
                }
                // 将选中的合同条件删除
                salesContractPayDao.batchRemove(payIds);
            }

            // 修改合同首款条件
            // 销售合同收款条件
            List<SalescontractPayDO> pay = JSON.parseArray(bodyPay, SalescontractPayDO.class);
            for (SalescontractPayDO afterPayDO : pay) {
                payVO = new ContractPayVO();
                String receivableDateAfter = DateFormatUtil.getFormateDate(afterPayDO.getReceivableDate());
                BigDecimal receivableAmountAfter = afterPayDO.getReceivableAmount().stripTrailingZeros();

                // 若是修改过的或是未变更的数据
                Long afterPayDOId = afterPayDO.getId();
                if (afterPayDOId != null) {
                    for (SalescontractPayDO beforePayDO : salesContractPayList) {
                        if (Objects.equals(afterPayDOId, beforePayDO.getId())) {
                            boolean isUpdate = false;
                            // 保存变更的时间
                            String receivableDateBefore = DateFormatUtil.getFormateDate(beforePayDO.getReceivableDate());
                            payVO.setReceivableDateBefore(receivableDateBefore);
                            if (!Objects.equals(receivableDateAfter, receivableDateBefore)) {
                                payVO.setType("修改");
                                payVO.setReceivableDateAfter(receivableDateAfter);
                                isUpdate = true;
                            }

                            // 保存变更的金额
                            BigDecimal receivableAmountBefore = beforePayDO.getReceivableAmount().stripTrailingZeros();
                            payVO.setReceivableAmountBefore(receivableAmountBefore.toPlainString());
                            if (receivableAmountAfter.compareTo(receivableAmountBefore) != 0) {
                                payVO.setReceivableAmountAfter(receivableAmountAfter.toPlainString());
                                // 差额
                                BigDecimal difference = receivableAmountAfter.subtract(receivableAmountBefore);
                                // 修改未付金额
                                afterPayDO.setUnpayAmount(afterPayDO.getUnpayAmount().add(difference));

                                if (!isUpdate){
                                    isUpdate = true;
                                    payVO.setType("修改");
                                }
                            }
                            if (!isUpdate) {
                                payVO.setType("未修改");
                            }
                            payList.add(payVO);
                            salesContractPayDao.update(afterPayDO);
                            break;
                        }
                    }
                    continue;
                }
                // 若是新增数据
                // 保存进合同收款条件子表
                afterPayDO.setSalescontractId(salesContractId);
                salesContractPayDao.save(afterPayDO);
                // 保存入变更记录表
                payVO.setId(afterPayDO.getId());
                payVO.setReceivableDateAfter("");
                payVO.setReceivableDateBefore(receivableDateAfter);
                payVO.setReceivableAmountAfter("");
                payVO.setReceivableAmountBefore(receivableAmountAfter.toPlainString());
                payVO.setType("新增");
                payList.add(payVO);
            }
        }
        return payList;
    }

    @Override
    public List<ContractItemVO> getContractItemVOS(String bodyItem, List<SalescontractItemDO> salesContractItemList) {
        List<ContractItemVO> itemList = Lists.newArrayList();
        if (salesContractItemList.size() > 0) {
            ContractItemVO contractItemVO;
            // 销售合同子项目表
            List<SalescontractItemDO> newSalesContractItemList = JSON.parseArray(bodyItem, SalescontractItemDO.class);
            // 找出变更的项目
            for (SalescontractItemDO newSalesContractItemDO : newSalesContractItemList) {
                Long itemId = newSalesContractItemDO.getId();
                for (SalescontractItemDO salescontractItemDO : salesContractItemList) {
                    if (Objects.equals(itemId, salescontractItemDO.getId())) {
                        boolean isUpdate = false;
                        // 变更数量
                        BigDecimal salesContractItemCount = salescontractItemDO.getCount();
                        BigDecimal newSalesContractItemCount = newSalesContractItemDO.getCount();
                        // 变更金额
                        BigDecimal salesContractItemTaxAmount = salescontractItemDO.getTaxAmount();
                        BigDecimal newSalesContractItemTaxAmount = newSalesContractItemDO.getTaxAmount();
                        // 保存修改后的数据变更记录表
                        contractItemVO = new ContractItemVO();
                        contractItemVO.setId(itemId);
                        if (salesContractItemCount.compareTo(newSalesContractItemCount) != 0) {
                            contractItemVO.setCountAfter(newSalesContractItemCount.toPlainString());
                            isUpdate = true;
                        }

                        if (salesContractItemTaxAmount.compareTo(newSalesContractItemTaxAmount) != 0) {
                            contractItemVO.setTaxAmountAfter(newSalesContractItemTaxAmount.toPlainString());
                            isUpdate = true;
                        }


                        if (isUpdate) {
                            contractItemVO.setCountBefore(salesContractItemCount.toPlainString());
                            contractItemVO.setTaxAmountBefore(salesContractItemTaxAmount.toPlainString());
                            contractItemVO.setType("已修改");
                            itemList.add(contractItemVO);
                            // 修改子表数据
                            salesContractItemDao.update(newSalesContractItemDO);
                        }
                        break;
                    }
                }
            }
        }
        return itemList;
    }

    @Override
    public int childCount(Long id) {
        return salesContractDao.childCount(id);
    }

    @Override
    public List<Map<String, Object>> payListForMap(Map<String, Object> map) {
        return salesContractPayDao.payListForMap(map);
    }

    @Override
    public Map<String, Object> payCountForMap(Map<String, Object> map) {
        return salesContractPayDao.payCountForMap(map);
    }

    @Override
    public ContractVO getContractHeadVOS(SalescontractDO newSalesContract,SalescontractDO oldSalesContract) {
        ContractVO contractVO = new ContractVO();
        BigDecimal afterDiscountRate = newSalesContract.getDiscountRate();
        BigDecimal beforeDiscountRate = oldSalesContract.getDiscountRate();
        contractVO.setDiscountRateBefore(beforeDiscountRate.stripTrailingZeros().toPlainString());
        if (beforeDiscountRate.compareTo(afterDiscountRate)!=0){
            contractVO.setType("已修改");
            contractVO.setDiscountRateAfter(afterDiscountRate.stripTrailingZeros().toPlainString());
        }else {
            contractVO.setType("未修改");
        }
        return contractVO;
    }



    @Override
    public R close(Long id) {
        SalescontractDO salescontractDO = this.get(id);
        if (Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
        }
        if (salescontractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }
//        if (!Objects.equals(salescontractDO.getAuditor(), ShiroUtils.getUserId())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user", null));
//        }
        salescontractDO.setCloseStatus(1);
        return this.update(salescontractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R reverseClose(Long id) {
        SalescontractDO salescontractDO = this.get(id);
        if (Objects.equals(salescontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
        }
        if (salescontractDO.getCloseStatus() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.contract.isClose", null));
        }
//        if (!Objects.equals(salescontractDO.getAuditor(), ShiroUtils.getUserId())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user", null));
//        }
        salescontractDO.setCloseStatus(0);
        return this.update(salescontractDO) > 0 ? R.ok() : R.error();
    }


    @Override
    public R getAlterationDetail(Long id) {
        Map<String, Object> result = Maps.newHashMap();
        ContractAlterationDO contractAlterationDO = contractAlterationDao.get(id);
        Map<String, Object> salesContract = salesContractDao.getDetail(contractAlterationDO.getContractId());
        result.put("contract", salesContract);
        String alterationContent = contractAlterationDO.getAlterationContent();
        if (StringUtils.isNoneEmpty(alterationContent)) {
            JSONObject alterationContentJSON = JSON.parseObject(alterationContent);
            JSONArray itemArray = alterationContentJSON.getJSONArray("itemArray");
            JSONArray payArray = alterationContentJSON.getJSONArray("payArray");
            JSONObject head = alterationContentJSON.getJSONObject("head");
            result.put("payArray", payArray);
            result.put("head",head);
            if (itemArray.size() > 0) {
                Map<String, Object> param;
                Map<String, Object> materiel;
                for (int i = 0; i < itemArray.size(); i++) {
                    JSONObject itemJSONObject = itemArray.getJSONObject(i);
                    param = Maps.newHashMap();
                    param.put("id", itemJSONObject.get("materielId"));
                    param.put("offset", 0);
                    param.put("limit", 1);
                    materiel = materielService.listForMap(param).get(0);
                    itemJSONObject.put("unitUomName", materiel.getOrDefault("unitUomName", ""));
                    itemJSONObject.put("name", materiel.getOrDefault("name", ""));
                    itemJSONObject.put("serialNo", materiel.getOrDefault("serialNo", ""));
                    itemJSONObject.put("specification", materiel.getOrDefault("specification", ""));
                }
                result.put("itemArray", itemArray);
                return R.ok(result);
            }
        }
        return R.ok(result);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return salesContractDao.listForMap(map);
    }

    @Override
    public Map<String, Object> countForMap(Map<String, Object> map) {
        return salesContractDao.countForMap(map);
    }

    @Override
    public SalescontractDO get(Long id) {
        return salesContractDao.get(id);
    }

    @Override
    public List<SalescontractDO> list(Map<String, Object> map) {
        return salesContractDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return salesContractDao.count(map);
    }

    @Override
    public int save(SalescontractDO salesContract) {
        return salesContractDao.save(salesContract);
    }

    @Override
    public int update(SalescontractDO salesContract) {
        return salesContractDao.update(salesContract);
    }

    @Override
    public int remove(Long id) {
        return salesContractDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return salesContractDao.batchRemove(ids);
    }

    private String salesContractCode() {
        String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.SALES_CONTRACT);
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("maxNo", maxNo);
        param.put("offset", 0);
        param.put("limit", 1);
        List<SalescontractDO> list = this.list(param);
        String taskNo = null;
        if (list.size() > 0) {
            taskNo = list.get(0).getContractCode();
        }
        return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
    }

    @Override
    public int updateAll(SalescontractDO salescontract){
        return salesContractDao.updateAll(salescontract);
    }

    @Override
    public List<Map<String, Object>> getDetailOfHead(Map<String, Object> map) {
        return salesContractDao.getDetailOfHead(map);
    }
}
