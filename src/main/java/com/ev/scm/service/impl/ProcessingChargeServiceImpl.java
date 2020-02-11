package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.scm.dao.ProcessingChargeDao;
import com.ev.scm.dao.ProcessingChargeItemDao;
import com.ev.scm.domain.ProcessingChargeDO;
import com.ev.scm.domain.ProcessingChargeItemDO;
import com.ev.scm.service.ProcessingChargeService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class ProcessingChargeServiceImpl implements ProcessingChargeService {
	@Autowired
	private ProcessingChargeDao processingChargeDao;
	@Autowired
	private ProcessingChargeItemDao processingChargeItemDao;
	
	@Override
	public ProcessingChargeDO get(Long id){
		return processingChargeDao.get(id);
	}
	
	@Override
	public List<ProcessingChargeDO> list(Map<String, Object> map){
		return processingChargeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processingChargeDao.count(map);
	}
	
	@Override
	public int save(ProcessingChargeDO processingCharge){
		return processingChargeDao.save(processingCharge);
	}
	
	@Override
	public int update(ProcessingChargeDO processingCharge){
		return processingChargeDao.update(processingCharge);
	}
	
	@Override
	public int remove(Long id){
		return processingChargeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processingChargeDao.batchRemove(ids);
	}

	@Override
	public R addOrUpdateProcessingCharge(ProcessingChargeDO processingChargeDO, String bodyItem, Long[] itemIds) {
		Long id = processingChargeDO.getId();
		// 新增
		List<ProcessingChargeItemDO> itemDOS = JSON.parseArray(bodyItem, ProcessingChargeItemDO.class);
		if (id == null) {
			Map<String,Object> result = Maps.newHashMap();

			processingChargeDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			processingChargeDO.setBillCode(this.BillCode());
			processingChargeDao.save(processingChargeDO);
			for(ProcessingChargeItemDO itemDO : itemDOS){
				itemDO.setChargeId(id);
				processingChargeItemDao.save(itemDO);
			}
			result.put("id",id);
			return R.ok(result);
		}

		// 修改操作
		if (itemIds.length > 0) {
			processingChargeItemDao.batchRemove(itemIds);
		}
		this.update(processingChargeDO);
		for (ProcessingChargeItemDO itemDO : itemDOS) {
			if (itemDO.getId() == null) {
				itemDO.setChargeId(id);
				processingChargeItemDao.save(itemDO);
				continue;
			}
			processingChargeItemDao.update(itemDO);
		}
		return R.ok();
	}

	private String BillCode() {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("offset", 0);
		param.put("limit", 1);
		List<ProcessingChargeDO> list = this.list(param);
		return DateFormatUtil.getWorkOrderNo(ConstantForGYL.PROCESSING_CHARGE, list.size() > 0 ? list.get(0).getBillCode() : null, 4);
	}

	@Override
	public R batchRemoveProcessingCharge(Long[] ids) {
		return null;
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return null;
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return 0;
	}

	@Override
	public R audit(Long id) {
		return null;
	}

	@Override
	public R reverseAudit(Long id) {
		return null;
	}

	@Override
	public R getDetail(Long id) {
		return null;
	}

}
