package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.scm.dao.ContractAlterationDao;
import com.ev.scm.dao.OutsourcingContractDao;
import com.ev.scm.dao.OutsourcingContractItemDao;
import com.ev.scm.dao.OutsourcingContractPayDao;
import com.ev.scm.domain.*;
import com.ev.scm.domain.OutsourcingContractDO;
import com.ev.scm.service.OutsourcingContractService;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        // 销售合同物料列表详情
        List<OutsourcingContractItemDO> outsourcingContractItemList = outsourcingContractItemDao.list(params);
        // 销售合同收款条件
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
            // 销售合同子项目表
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
        return R.ok();
    }

    @Override
    public R reverseAudit(Long id) {
        return null;
    }

    @Override
    public R close(Long id) {
        return null;
    }

    @Override
    public R reverseClose(Long id) {
        return null;
    }

    @Override
    public R getDetail(Long outsourcingContractId) {
        return null;
    }

    @Override
    public R getAlterationDetail(Long id) {
        return null;
    }


}
