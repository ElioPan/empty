package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.BomDetailDO;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.service.BomDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.ev.scm.dao.ContractAlterationDao;
import com.ev.scm.dao.OutsourcingContractDao;
import com.ev.scm.dao.OutsourcingContractItemDao;
import com.ev.scm.dao.OutsourcingContractPayDao;
import com.ev.scm.domain.*;
import com.ev.scm.service.OutsourcingContractService;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class OutsourcingContractServiceImpl implements OutsourcingContractService {
    @Autowired
    private OutsourcingContractDao outsourcingContractDao;
    @Autowired
    private OutsourcingContractItemDao outsourcingContractItemDao;
    @Autowired
    private OutsourcingContractPayDao outsourcingContractPayDao;
    @Autowired
    private ContractAlterationDao contractAlterationDao;
    @Autowired
    private ProductionFeedingService feedingService;
    @Autowired
    private BomDetailService bomDetailService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Override
    public OutsourcingContractDO get(Long id) {
        return outsourcingContractDao.get(id);
    }

    @Override
    public List<OutsourcingContractDO> list(Map<String, Object> map) {
        return outsourcingContractDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return outsourcingContractDao.count(map);
    }

    @Override
    public int save(OutsourcingContractDO outsourcingContract) {
        return outsourcingContractDao.save(outsourcingContract);
    }

    @Override
    public int update(OutsourcingContractDO outsourcingContract) {
        return outsourcingContractDao.update(outsourcingContract);
    }

    @Override
    public int remove(Long id) {
        return outsourcingContractDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return outsourcingContractDao.batchRemove(ids);
    }

    @Override
    public R addOrUpdateOutsourcingContract(OutsourcingContractDO outsourcingContract, String bodyItem, String bodyPay, Long[] itemIds, Long[] payIds) {
        Map<String, Object> result = Maps.newHashMap();
        Long id = outsourcingContract.getId();
        // 新增
        if (id == null) {
            outsourcingContract.setAuditSign(ConstantForGYL.WAIT_AUDIT);
            outsourcingContract.setCloseStatus(0);
            outsourcingContract.setContractCode(this.outsourcingContractCode());
            outsourcingContractDao.save(outsourcingContract);
            id = outsourcingContract.getId();
        } else {
            // 修改
            // 验证是否能修改 CloseStatus 0 未关闭 1 关闭
            OutsourcingContractDO outsourcingContractDO = this.get(id);
            if (!Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
                return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
            }
            outsourcingContractDao.update(outsourcingContract);
        }

        // 删除子项目内的数据
        if (itemIds.length > 0) {
            outsourcingContractItemDao.batchRemove(itemIds);
        }

        // 删除合同首款条件数据
        if (payIds.length > 0) {
            outsourcingContractPayDao.batchRemove(payIds);
        }

        // 保存委外合同收款条件
        List<OutsourcingContractPayDO> pay = JSON.parseArray(bodyPay, OutsourcingContractPayDO.class);
        for (OutsourcingContractPayDO payData : pay) {
            // 新增
            if (payData.getId() == null) {
                payData.setContractId(id);
                outsourcingContractPayDao.save(payData);
            } else {
                // 修改
                outsourcingContractPayDao.update(payData);
            }
        }

        // 保存委外合同子项目表
        List<OutsourcingContractItemDO> item = JSON.parseArray(bodyItem, OutsourcingContractItemDO.class);
        for (OutsourcingContractItemDO itemData : item) {
            // 新增
            if (itemData.getId() == null) {
                itemData.setContractId(id);
                outsourcingContractItemDao.save(itemData);
            } else {
                // 修改
                outsourcingContractItemDao.update(itemData);
            }
        }
        result.put("id", id);
        return R.ok(result);
    }

    private String outsourcingContractCode() {
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("offset", 0);
        param.put("limit", 1);
        List<OutsourcingContractDO> list = this.list(param);
        return DateFormatUtil.getWorkOrderNo(ConstantForGYL.OUTSOURCING_CONTRACT, list.size() > 0 ? list.get(0).getContractCode() : null, 4);
    }

    @Override
    public R removeOutsourcingContract(Long[] outsourcingContractIds) {
        OutsourcingContractDO outsourcingContractDO;
        if (outsourcingContractIds.length > 0) {
            for (Long outsourcingContractId : outsourcingContractIds) {
                outsourcingContractDO = this.get(outsourcingContractId);
                if (!Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
                    return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled", null));
                }
            }
            this.batchRemove(outsourcingContractIds);
            outsourcingContractItemDao.batchRemoveByOutsourcingContractIds(outsourcingContractIds);
            outsourcingContractPayDao.batchRemoveByOutsourcingContractIds(outsourcingContractIds);
            return R.ok();
        }
        return R.error();
    }

    @Override
    public R editOutsourcingContract(Long outsourcingContractId, String bodyItem, String bodyPay, Long[] payIds) {
        OutsourcingContractDO outsourcingContractDO = this.get(outsourcingContractId);
        if (Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.contract.isUpdate.notAlteration", null));
        }
        if (outsourcingContractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("contractId", outsourcingContractId);
        // 委外合同物料列表详情
        List<OutsourcingContractItemDO> outsourcingContractItemList = outsourcingContractItemDao.list(params);
        // 委外合同收款条件
        List<OutsourcingContractPayDO> outsourcingContractPayList = outsourcingContractPayDao.list(params);
        JSONObject alterationContent = new JSONObject();

        List<ContractItemVO> itemList = this.getContractItemVOS(bodyItem, outsourcingContractItemList);
        alterationContent.put("itemArray", itemList);

        List<ContractPayVO> payList = this.getContractPayVOS(bodyPay, payIds, outsourcingContractPayList);
        alterationContent.put("payArray", payList);

        if (itemList.size() > 0 || payList.size() > 0) {
            ContractAlterationDO alterationDO = new ContractAlterationDO();
            alterationDO.setAlterationContent(alterationContent.toJSONString());
            alterationDO.setContractId(outsourcingContractId);
            alterationDO.setContractCode(this.get(outsourcingContractId).getContractCode());
            alterationDO.setContractType(ConstantForGYL.WWHT);
            contractAlterationDao.save(alterationDO);
        }
        return R.ok();
    }

    @Override
    public List<ContractPayVO> getContractPayVOS(String bodyPay, Long[] payIds, List<OutsourcingContractPayDO> outsourcingContractPayList) {
        List<ContractPayVO> payList = Lists.newArrayList();
        ContractPayVO payVO;
        if (outsourcingContractPayList.size() > 0) {
            // 若合同收款条件变了则执行修改操作
            // 若被删除保存删除前的信息
            if (payIds.length > 0) {
                List<OutsourcingContractPayDO> collect = outsourcingContractPayList.stream().filter(outsourcingContractPayDO -> {
                    for (Long payId : payIds) {
                        return Objects.equals(outsourcingContractPayDO.getId(), payId);
                    }
                    return false;
                }).collect(Collectors.toList());
                // 保存删除了的信息
                for (OutsourcingContractPayDO outsourcingcontractPayDO : collect) {
                    payVO = new ContractPayVO();
                    payVO.setId(outsourcingcontractPayDO.getId());
                    payVO.setReceivableDateAfter(DateFormatUtil.getFormateDate(outsourcingcontractPayDO.getPayableDate()));
                    payVO.setReceivableDateBefore("");
                    payVO.setReceivableAmountAfter(outsourcingcontractPayDO.getPayableAmount().stripTrailingZeros().toPlainString());
                    payVO.setReceivableAmountBefore("");
                    payVO.setType("删除");
                    payList.add(payVO);
                }
                // 将选中的合同条件删除
                outsourcingContractPayDao.batchRemove(payIds);
            }

            // 修改合同收款条件
            List<OutsourcingContractPayDO> pay = JSON.parseArray(bodyPay, OutsourcingContractPayDO.class);
            for (OutsourcingContractPayDO afterPayDO : pay) {
                payVO = new ContractPayVO();
                String receivableDateAfter = DateFormatUtil.getFormateDate(afterPayDO.getPayableDate());
                BigDecimal receivableAmountAfter = afterPayDO.getPayableAmount().stripTrailingZeros();

                // 若是修改过的或是未变更的数据
                Long afterPayDOId = afterPayDO.getId();
                if (afterPayDOId != null) {
                    for (OutsourcingContractPayDO beforePayDO : outsourcingContractPayList) {
                        if (Objects.equals(afterPayDOId, beforePayDO.getId())) {
                            boolean isUpdate = false;
                            // 保存变更的时间
                            String receivableDateBefore = DateFormatUtil.getFormateDate(beforePayDO.getPayableDate());
                            payVO.setReceivableDateBefore(receivableDateBefore);
                            if (!Objects.equals(receivableDateAfter, receivableDateBefore)) {
                                payVO.setType("修改");
                                payVO.setReceivableDateAfter(receivableDateAfter);
                                isUpdate = true;
                            }

                            // 保存变更的金额
                            BigDecimal receivableAmountBefore = beforePayDO.getPayableAmount().stripTrailingZeros();
                            payVO.setReceivableAmountBefore(receivableAmountBefore.toPlainString());
                            if (receivableAmountAfter.compareTo(receivableAmountBefore) != 0) {
                                payVO.setReceivableAmountAfter(receivableAmountAfter.toPlainString());
                                if (!isUpdate){
                                    isUpdate = true;
                                    payVO.setType("修改");
                                }
                            }
                            if (!isUpdate) {
                                payVO.setType("未修改");
                            }
                            payList.add(payVO);
                            outsourcingContractPayDao.update(afterPayDO);
                            break;
                        }
                    }
                }
                // 若是新增数据
                // 保存进合同收款条件子表
                outsourcingContractPayDao.save(afterPayDO);
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
    public List<ContractItemVO> getContractItemVOS(String bodyItem, List<OutsourcingContractItemDO> outsourcingContractItemList) {
        List<ContractItemVO> itemList = Lists.newArrayList();
        if (outsourcingContractItemList.size() > 0) {
            ContractItemVO contractItemVO;
            // 委外合同子项目表
            List<OutsourcingContractItemDO> newOutsourcingContractItemList = JSON.parseArray(bodyItem, OutsourcingContractItemDO.class);
            // 找出变更的项目
            for (OutsourcingContractItemDO newOutsourcingContractItemDO : newOutsourcingContractItemList) {
                Long itemId = newOutsourcingContractItemDO.getId();
                for (OutsourcingContractItemDO outsourcingcontractItemDO : outsourcingContractItemList) {
                    if (Objects.equals(itemId, outsourcingcontractItemDO.getId())) {
                        boolean isUpdate = false;
                        // 变更数量
                        BigDecimal outsourcingContractItemCount = outsourcingcontractItemDO.getCount();
                        BigDecimal newOutsourcingContractItemCount = newOutsourcingContractItemDO.getCount();
                        // 变更金额
                        BigDecimal outsourcingContractItemTaxAmount = outsourcingcontractItemDO.getTaxAmount();
                        BigDecimal newOutsourcingContractItemTaxAmount = newOutsourcingContractItemDO.getTaxAmount();
                        // 保存修改后的数据变更记录表
                        contractItemVO = new ContractItemVO();
                        contractItemVO.setId(itemId);
                        if (outsourcingContractItemCount.compareTo(newOutsourcingContractItemCount) != 0) {
                            contractItemVO.setCountAfter(newOutsourcingContractItemCount.toPlainString());
                            isUpdate = true;
                        }

                        if (outsourcingContractItemTaxAmount.compareTo(newOutsourcingContractItemTaxAmount) != 0) {
                            contractItemVO.setTaxAmountAfter(newOutsourcingContractItemCount.toPlainString());
                            isUpdate = true;
                        }


                        if (isUpdate) {
                            contractItemVO.setCountBefore(outsourcingContractItemCount.toPlainString());
                            contractItemVO.setTaxAmountBefore(outsourcingContractItemTaxAmount.toPlainString());
                            contractItemVO.setType("已修改");
                            itemList.add(contractItemVO);
                            // 修改子表数据
                            outsourcingContractItemDao.update(newOutsourcingContractItemDO);
                        }
                        break;
                    }
                }
            }
        }
        return itemList;
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return outsourcingContractDao.listForMap(map);
    }

    @Override
    public Map<String, BigDecimal> countForMap(Map<String, Object> map) {
        return outsourcingContractDao.countForMap(map);
    }

    @Override
    public R audit(Long id) {
        OutsourcingContractDO outsourcingContractDO = this.get(id);
        if (outsourcingContractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }
        if (Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
        }
        // 表体中物料有BOM编号审核时调用BOM数据同步生成委外投料单，具体算法同生产投料单。
        Map<String,Object> param = Maps.newHashMap();
        param.put("contractId",id);
        List<OutsourcingContractItemDO> itemDOList = outsourcingContractItemDao.list(param)
                .stream()
                .filter(outsourcingContractItemDO -> outsourcingContractItemDO.getBomId() != null)
                .collect(Collectors.toList());
        if (itemDOList.size() > 0) {
            for (OutsourcingContractItemDO outsourcingContractItemDO : itemDOList) {
                feedingService.add(this.getFeedingDO(outsourcingContractItemDO), this.getFeedingChildArray(outsourcingContractItemDO));
            }
        }
        // 修改单据状态
        outsourcingContractDO.setAuditSign(ConstantForGYL.OK_AUDITED);
        outsourcingContractDO.setAuditor(ShiroUtils.getUserId());
        return this.update(outsourcingContractDO) > 0 ? R.ok() : R.error();
    }


    /**
     * 通过委外合同子表信息创建一个生产投料单头部信息
     */
    private ProductionFeedingDO getFeedingDO(OutsourcingContractItemDO outsourcingContractItemDO) {
        ProductionFeedingDO feedingDO = new ProductionFeedingDO();
        feedingDO.setOutsourceContractItemId(outsourcingContractItemDO.getId());
        feedingDO.setMaterielId(outsourcingContractItemDO.getMaterielId());
        feedingDO.setPlanCount(outsourcingContractItemDO.getCount());
        feedingDO.setIsQuota(outsourcingContractItemDO.getIsQuota());
        return feedingDO;
    }

    /**
     * 通过委外合同子表信息创建一个生产投料单子物料信息
     */
    private String getFeedingChildArray(OutsourcingContractItemDO itemDO) {
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
        param.put("bomId", itemDO.getBomId());
        List<BomDetailDO> list = bomDetailService.list(param);
        List<Map<String, Object>> feedingDetailList = new ArrayList<>();
        Map<String, Object> feedingDetail;
        for (BomDetailDO bomDetailDO : list) {
            feedingDetail = Maps.newHashMapWithExpectedSize(2);
            feedingDetail.put("materielId", bomDetailDO.getMaterielId());
            // 计划投料数量公式 (标准用量 /(1-损耗率/100))*计划生产数量
            BigDecimal wasteRate = bomDetailDO.getWasteRate();
            BigDecimal standardCount = bomDetailDO.getStandardCount();
            BigDecimal planCount = itemDO.getCount();
            BigDecimal planFeeding = standardCount.divide(BigDecimal.valueOf(1 - wasteRate.doubleValue() / 100), 2)
                    .multiply(planCount);
            feedingDetail.put("planFeeding", planFeeding);
            feedingDetailList.add(feedingDetail);
        }
        return JSON.toJSONString(feedingDetailList);
    }

    @Override
    public R reverseAudit(Long id) {
        OutsourcingContractDO outsourcingContractDO = this.get(id);
        if (outsourcingContractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }
        if (!Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
        }

        Map<String,Object> param = Maps.newHashMap();
        param.put("contractId",id);
        List<Long> itemIdList = outsourcingContractItemDao.list(param)
                .stream()
                .filter(outsourcingContractItemDO -> outsourcingContractItemDO.getBomId() != null)
                .map(OutsourcingContractItemDO::getId)
                .collect(Collectors.toList());

        // 删除对应委外投料单，如果委外投料单已审核则不能反审核。
        if (itemIdList.size() > 0) {
            for (Long itemId : itemIdList) {
                ProductionFeedingDO feedingDO = feedingService.getByOutsourcingContractItemId(itemId);
                if (Objects.equals(feedingDO.getStatus(), ConstantForMES.OK_AUDITED)) {
                    return R.error(messageSourceHandler.getMessage("plan.feedingPlan.isAudit", null));
                }
            }
            feedingService.batchRemoveHeadAndBody(itemIdList.toArray(new Long[0]));
        }

        // 修改单据状态
        outsourcingContractDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
        outsourcingContractDO.setAuditor(0L);
        return this.update(outsourcingContractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R close(Long id) {
        OutsourcingContractDO outsourcingContractDO = this.get(id);
        if (Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
        }
        if (outsourcingContractDO.getCloseStatus() == 1) {
            return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
        }

        outsourcingContractDO.setCloseStatus(1);
        return this.update(outsourcingContractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R reverseClose(Long id) {
        OutsourcingContractDO outsourcingContractDO = this.get(id);
        if (Objects.equals(outsourcingContractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
        }
        if (outsourcingContractDO.getCloseStatus() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.contract.isClose", null));
        }

        outsourcingContractDO.setCloseStatus(0);
        return this.update(outsourcingContractDO) > 0 ? R.ok() : R.error();
    }

    @Override
    public R getDetail(Long outsourcingContractId) {
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> outsourcingContract = outsourcingContractDao.getDetail(outsourcingContractId);
        if (outsourcingContract.isEmpty()){
            return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD", null));
        }
        result.put("outsourcingContract", outsourcingContract);
        Map<String, Object> params = Maps.newHashMap();
        params.put("contractId",outsourcingContractId);
        // 委外合同物料列表详情
        List<Map<String, Object>> outsourcingContractItemList = outsourcingContractItemDao.listByContract(params);
        Map<String, Object> outsourcingContractItemCount = outsourcingContractItemDao.countByContract(params);
        result.put("outsourcingContractItemList", outsourcingContractItemList);
        result.put("outsourcingContractItemCount", outsourcingContractItemCount);

        // 委外合同收款条件
        List<OutsourcingContractPayDO> outsourcingContractPayList = outsourcingContractPayDao.list(params);
        Map<String, Object> outsourcingContractPayCount = outsourcingContractPayDao.countByContract(params);
        result.put("outsourcingContractPayList", outsourcingContractPayList);
        result.put("outsourcingContractPayCount", outsourcingContractPayCount);

        return R.ok(result);
    }

    @Override
    public R getAlterationDetail(Long id) {
        Map<String, Object> result = Maps.newHashMap();
        ContractAlterationDO contractAlterationDO = contractAlterationDao.get(id);
        Map<String, Object> outsourcingContract = outsourcingContractDao.getDetail(id);
        result.put("contract", outsourcingContract);
        String alterationContent = contractAlterationDO.getAlterationContent();
        if (StringUtils.isNoneEmpty(alterationContent)) {
            JSONObject alterationContentJSON = JSON.parseObject(alterationContent);
            JSONArray itemArray = alterationContentJSON.getJSONArray("itemArray");
            JSONArray payArray = alterationContentJSON.getJSONArray("payArray");
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
                result.put("payArray", payArray);
                return R.ok(result);
            }
        }
        return R.ok(result);
    }


}
