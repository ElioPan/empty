package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.SalesbillDao;
import com.ev.scm.dao.SalesbillItemDao;
import com.ev.scm.domain.SalesbillDO;
import com.ev.scm.domain.SalesbillItemDO;
import com.ev.scm.service.SalesbillService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SalesbillServiceImpl implements SalesbillService {
	@Autowired
	private SalesbillDao salesbillDao;

	@Autowired
	private SalesbillItemDao salesbillItemDao;

    @Autowired
    private MessageSourceHandler messageSourceHandler;

	
	@Override
	public R addOrUpdateSalesBill(SalesbillDO salesBillDO, String bodyItem, Long[] itemIds) {
        Long id = salesBillDO.getId();
        // 新增
        List<SalesbillItemDO> itemDOS = JSON.parseArray(bodyItem, SalesbillItemDO.class);
        if (id == null) {
            Map<String,Object> result = Maps.newHashMap();

            salesBillDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
//            salesBillDO.setBillCode(this.SalesBillCode());
            salesbillDao.save(salesBillDO);
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
        salesbillDO.setAuditSign(ConstantForGYL.OK_AUDITED);
        salesbillDO.setAuditor(ShiroUtils.getUserId());
        return this.update(salesbillDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseAudit(Long id) {
        SalesbillDO salesbillDO = this.get(id);
        if (Objects.equals(salesbillDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
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
}
