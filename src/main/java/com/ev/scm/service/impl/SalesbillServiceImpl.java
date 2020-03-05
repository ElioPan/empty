package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.SalesbillDao;
import com.ev.scm.dao.SalesbillItemDao;
import com.ev.scm.dao.SalescontractDao;
import com.ev.scm.dao.SalescontractItemDao;
import com.ev.scm.domain.SalesbillDO;
import com.ev.scm.domain.SalesbillItemDO;
import com.ev.scm.domain.SalescontractDO;
import com.ev.scm.domain.SalescontractItemDO;
import com.ev.scm.service.SalesbillService;
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
public class SalesbillServiceImpl implements SalesbillService {
	@Autowired
	private SalesbillDao salesbillDao;

	@Autowired
	private SalesbillItemDao salesbillItemDao;

    @Autowired
    private SalescontractDao salescontractDao;

    @Autowired
    private SalescontractItemDao salescontractItemDao;

    @Autowired
    private MessageSourceHandler messageSourceHandler;

	
	@Override
	public R addOrUpdateSalesBill(SalesbillDO salesBillDO, String bodyItem, Long[] itemIds) {
        R r = this.checkSourceNumber(bodyItem);
        if (r != null) {
            return r;
        }

	    Long id = salesBillDO.getId();
        // 新增
        List<SalesbillItemDO> itemDOS = JSON.parseArray(bodyItem, SalesbillItemDO.class);
        if (id == null) {
            Map<String,Object> result = Maps.newHashMap();

            salesBillDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
//            salesBillDO.setBillCode(this.SalesBillCode());
            salesbillDao.save(salesBillDO);
            id = salesBillDO.getId();
            for(SalesbillItemDO itemDO : itemDOS){
                itemDO.setSalesbillId(id);
                salesbillItemDao.save(itemDO);
            }
            result.put("id",id);
            return R.ok(result);
        }

        // 修改操作
        if (itemIds.length > 0) {
            salesbillItemDao.batchRemove(itemIds);
        }
        this.update(salesBillDO);
        for (SalesbillItemDO itemDO : itemDOS) {
            if (itemDO.getId() == null) {
                itemDO.setSalesbillId(id);
                salesbillItemDao.save(itemDO);
                continue;
            }
            salesbillItemDao.update(itemDO);
        }
        return R.ok();
	}
	

	@Override
	public R audit(Long id) {
        SalesbillDO salesbillDO = this.get(id);
        if (Objects.equals(salesbillDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
        }

        // 子项目金额的合计
        // 反写销售合同开票金额
        List<Map<String, Object>> maps = salesbillItemDao.countForMapGroupBySourceId(id);
        SalescontractDO salescontractDO;
        BigDecimal totalTaxAmount;
        for (Map<String, Object> map : maps) {
            salescontractDO = salescontractDao.get(salescontractItemDao.get(Long.parseLong(map.get("sourceId").toString())).getSalescontractId());
            totalTaxAmount = MathUtils.getBigDecimal(map.get("totalTaxAmount"));

            salescontractDO.setInvoicedAmount(salescontractDO.getInvoicedAmount().add(totalTaxAmount));
            salescontractDO.setUninvoicedAmount(salescontractDO.getUninvoicedAmount().subtract(totalTaxAmount));
            salescontractDao.update(salescontractDO);
        }

        salesbillDO.setAuditSign(ConstantForGYL.OK_AUDITED);
        salesbillDO.setAuditor(ShiroUtils.getUserId());
        salesbillDO.setAuditTime(new Date());

        return this.update(salesbillDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseAudit(Long id) {
        SalesbillDO salesbillDO = this.get(id);
        if (Objects.equals(salesbillDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
        }

        // 撤回反写的开票金额
        List<Map<String, Object>> maps = salesbillItemDao.countForMapGroupBySourceId(id);
        SalescontractDO salescontractDO;
        BigDecimal totalTaxAmount;
        for (Map<String, Object> map : maps) {
            salescontractDO = salescontractDao.get(salescontractItemDao.get(Long.parseLong(map.get("sourceId").toString())).getSalescontractId());
            totalTaxAmount = MathUtils.getBigDecimal(map.get("totalTaxAmount"));
            salescontractDO.setInvoicedAmount(salescontractDO.getInvoicedAmount().subtract(totalTaxAmount));
            salescontractDO.setUninvoicedAmount(salescontractDO.getUninvoicedAmount().add(totalTaxAmount));
            salescontractDao.update(salescontractDO);
        }

        salesbillDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
        salesbillDO.setAuditor(0L);
        return this.update(salesbillDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R getDetail(Long id) {
	    Map<String,Object> result = Maps.newHashMap();
	    // 表头数据
        result.put("salesBill",salesbillDao.getDetail(id));

        Map<String,Object> param = Maps.newHashMap();
        param.put("id",id);
        // 子项目列表
        List<Map<String, Object>> itemList = salesbillItemDao.listForMap(param);
        // 子项目金额的合计
        Map<String, Object> itemTotalCount = salesbillItemDao.countForMap(param);
        result.put("itemList",itemList);
        result.put("itemTotalCount",itemTotalCount);
        return R.ok(result);
	}

    @Override
    public R batchRemoveSalesBill(Long[] ids) {
        if (ids.length > 0) {
            SalesbillDO salesbillDO;
            for (Long id : ids) {
                salesbillDO = this.get(id);
                if (Objects.equals(salesbillDO.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
                    return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled", null));
                }
            }
            this.batchRemove(ids);
            salesbillItemDao.batchRemoveByBillId(ids);
            return R.ok();
        }
        return R.error();
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return salesbillDao.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return salesbillDao.countForMap(map);
    }

    @Override
    public BigDecimal getCountBySource(Map<String, Object> map) {
        return salesbillItemDao.getCountBySource(map);
    }

    @Override
    public Map<String, Object> countTotal(Map<String, Object> map) {
        return salesbillDao.countTotal(map);
    }

    @Override
	public SalesbillDO get(Long id){
		return salesbillDao.get(id);
	}
	
	@Override
	public List<SalesbillDO> list(Map<String, Object> map){
		return salesbillDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salesbillDao.count(map);
	}
	
	@Override
	public int save(SalesbillDO salesbill){
		return salesbillDao.save(salesbill);
	}
	
	@Override
	public int update(SalesbillDO salesbill){
		return salesbillDao.update(salesbill);
	}
	
	@Override
	public int remove(Long id){
		return salesbillDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return salesbillDao.batchRemove(ids);
	}
	
//	public String SalesBillCode(){
//		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.BILL_CODE);
//		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
//		param.put("maxNo", maxNo);
//		param.put("offset", 0);
//		param.put("limit", 1);
//		List<SalesbillDO> list = this.list(param);
//		String taskNo = null;
//		if (list.size() > 0) {
//			taskNo = list.get(0).getBillCode();
//		}
//		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
//	}
    @Override
    public R checkSourceNumber(String bodyItem) {
        // 与源单数量对比
        List<SalesbillItemDO> itemDOs = JSON.parseArray(bodyItem, SalesbillItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        Map<Long, Long> sourceIdAndItemId = Maps.newHashMap();
        for (SalesbillItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            sourceIdAndItemId.put(sourceId, itemDO.getId());
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        SalescontractItemDO detailDO;
        BigDecimal contractCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = salescontractItemDao.get(sourceId);
                contractCount = detailDO.getCount();
                // 查询源单已被选择数量
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", sourceIdAndItemId.get(sourceId));
                map.put("sourceId", sourceId);
                map.put("sourceType", ConstantForGYL.XSHT);
                BigDecimal bySource = this.getCountBySource(map);
                BigDecimal countByOutSource = bySource == null ? BigDecimal.ZERO : bySource;
                if (contractCount.compareTo(count.get(sourceId).add(countByOutSource)) < 0) {
                    List<SalesbillItemDO> collect = itemDOs.stream()
                            .filter(itemDO -> Objects.equals(itemDO.getSourceId(), sourceId))
                            .collect(Collectors.toList());
                    String[] args = {count.get(sourceId).toPlainString(), contractCount.subtract(countByOutSource).toPlainString(), collect.get(0).getSourceCode()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }
        return null;
    }
}
