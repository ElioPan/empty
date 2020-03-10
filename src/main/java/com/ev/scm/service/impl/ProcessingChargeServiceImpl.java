package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.*;
import com.ev.scm.domain.*;
import com.ev.scm.service.ProcessingChargeService;
import com.ev.scm.service.StockInItemService;
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
public class ProcessingChargeServiceImpl implements ProcessingChargeService {
	@Autowired
	private ProcessingChargeDao processingChargeDao;
	@Autowired
	private ProcessingChargeItemDao processingChargeItemDao;
	@Autowired
	private OutsourcingContractDao outsourcingContractDao;
	@Autowired
	private StockInItemDao stockInItemDao;
	@Autowired
	private OutsourcingContractItemDao outsourcingContractItemDao;

	@Autowired
	private StockInItemService stockInItemService;

	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
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
		R r = this.checkSourceNumber(bodyItem,id);
		if (r != null) {
			return r;
		}

		// 新增
		List<ProcessingChargeItemDO> itemDOS = JSON.parseArray(bodyItem, ProcessingChargeItemDO.class);
		if (id == null) {
			Map<String,Object> result = Maps.newHashMap();

			processingChargeDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
//			processingChargeDO.setBillCode(this.BillCode());
			processingChargeDao.save(processingChargeDO);
			id = processingChargeDO.getId();
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

//	private String BillCode() {
//		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
//		param.put("offset", 0);
//		param.put("limit", 1);
//		List<ProcessingChargeDO> list = this.list(param);
//		return DateFormatUtil.getWorkOrderNo(ConstantForGYL.PROCESSING_CHARGE, list.size() > 0 ? list.get(0).getBillCode() : null, 4);
//	}

	@Override
	public R batchRemoveProcessingCharge(Long[] ids) {
		if (ids.length > 0) {
			ProcessingChargeDO processingChargeDO;
			for (Long id : ids) {
				processingChargeDO = this.get(id);
				if (Objects.equals(processingChargeDO.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
					return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled", null));
				}
			}
			this.batchRemove(ids);
			processingChargeItemDao.batchRemoveByChargeIds(ids);
			return R.ok();
		}
		return R.error();
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return processingChargeDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return processingChargeDao.countForMap(map);
	}

	@Override
	public R audit(Long id) {
		ProcessingChargeDO processingChargeDO = this.get(id);
		if (Objects.equals(processingChargeDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
		}
		// 反写委外合同开票金额
		List<Map<String, Object>> maps = processingChargeItemDao.countForMapGroupBySourceId(id);
		OutsourcingContractItemDO outsourcingContractItemDO;
		OutsourcingContractDO outsourcingContractDO;
		StockInItemDO stockInItemDO;
		BigDecimal totalTaxAmount;
		for (Map<String, Object> map : maps) {
			stockInItemDO = stockInItemDao.get(Long.parseLong(map.get("sourceId").toString()));
			outsourcingContractItemDO = outsourcingContractItemDao.get(stockInItemDO.getSourceId());

			outsourcingContractDO = outsourcingContractDao.get(outsourcingContractItemDO.getContractId());

			totalTaxAmount = MathUtils.getBigDecimal(map.get("totalTaxAmount"));
			outsourcingContractDO.setInvoicedAmount(outsourcingContractDO.getInvoicedAmount().add(totalTaxAmount));
			outsourcingContractDO.setUninvoicedAmount(outsourcingContractDO.getUninvoicedAmount().subtract(totalTaxAmount));
			outsourcingContractDao.update(outsourcingContractDO);
		}


		processingChargeDO.setAuditSign(ConstantForGYL.OK_AUDITED);
		processingChargeDO.setAuditTime(new Date());
		processingChargeDO.setAuditor(ShiroUtils.getUserId());
		return this.update(processingChargeDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseAudit(Long id) {
		ProcessingChargeDO processingChargeDO = this.get(id);
		if (Objects.equals(processingChargeDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
		}
//		int childCount = processingChargeDao.childCount(id);
//		if (childCount>0) {
//			return R.error(messageSourceHandler.getMessage("scm.childList.reverseAudit", null));
//		}
		// 撤回反写的开票金额
		List<Map<String, Object>> maps = processingChargeItemDao.countForMapGroupBySourceId(id);
		OutsourcingContractItemDO outsourcingContractItemDO;
		OutsourcingContractDO outsourcingContractDO;
		StockInItemDO stockInItemDO;
		BigDecimal totalTaxAmount;
		for (Map<String, Object> map : maps) {
			stockInItemDO = stockInItemDao.get(Long.parseLong(map.get("sourceId").toString()));
			outsourcingContractItemDO = outsourcingContractItemDao.get(stockInItemDO.getSourceId());

			outsourcingContractDO = outsourcingContractDao.get(outsourcingContractItemDO.getContractId());
			totalTaxAmount = MathUtils.getBigDecimal(map.get("totalTaxAmount"));

			outsourcingContractDO.setInvoicedAmount(outsourcingContractDO.getInvoicedAmount().subtract(totalTaxAmount));
			outsourcingContractDO.setUninvoicedAmount(outsourcingContractDO.getUninvoicedAmount().add(totalTaxAmount));
			outsourcingContractDao.update(outsourcingContractDO);
		}


		processingChargeDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
		processingChargeDO.setAuditor(0L);
		return this.update(processingChargeDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R getDetail(Long id) {
		Map<String,Object> result = Maps.newHashMap();
		// 表头数据
		result.put("processingCharge",processingChargeDao.getDetail(id));

		Map<String,Object> param = Maps.newHashMap();
		param.put("id",id);
		// 子项目列表
		List<Map<String, Object>> itemList = processingChargeItemDao.listForMap(param);
		// 子项目金额的合计
		Map<String, Object> itemTotalCount = processingChargeItemDao.countForMap(param);
		result.put("itemList",itemList);
		result.put("itemTotalCount",itemTotalCount);
		return R.ok(result);
	}

	@Override
	public R checkSourceNumber(String bodyItem,Long id) {
		// 与源单数量对比
		List<ProcessingChargeItemDO> itemDOs = JSON.parseArray(bodyItem, ProcessingChargeItemDO.class);
		Map<Long, BigDecimal> count = Maps.newHashMap();
		for (ProcessingChargeItemDO itemDO : itemDOs) {
			Long sourceId = itemDO.getSourceId();
			if (count.containsKey(sourceId)) {
				count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
				continue;
			}
			count.put(itemDO.getSourceId(), itemDO.getCount());
		}

		// 获取原先单据的数量
		Map<Long, BigDecimal> oldCounts = Maps.newHashMap();
		if (id != null) {
			Map<String,Object> map = Maps.newHashMap();
			map.put("outId",id);
			List<ProcessingChargeItemDO> list = processingChargeItemDao.list(map);
			if (list.size() > 0) {
				oldCounts = list.stream()
						.collect(Collectors.toMap(ProcessingChargeItemDO::getSourceId, ProcessingChargeItemDO::getCount, BigDecimal::add));
			}
		}

		StockInItemDO detailDO;
		BigDecimal contractCount;
		if (count.size() > 0) {
			for (Long sourceId : count.keySet()) {
				detailDO = stockInItemService.get(sourceId);
				contractCount = detailDO.getCount();
				// 查询源单已被选择数量
				Map<String,Object> map = Maps.newHashMap();
				map.put("sourceId",sourceId);
				map.put("sourceType", ConstantForGYL.OUTSOURCING_INSTOCK);
				BigDecimal bySource = processingChargeItemDao.getCountBySource(map);

				BigDecimal oldCount = oldCounts.getOrDefault(sourceId,BigDecimal.ZERO);

				BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource.subtract(oldCount);
				if (contractCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
					List<ProcessingChargeItemDO> collect = itemDOs.stream()
							.filter(itemDO -> Objects.equals(itemDO.getSourceId(),sourceId))
							.collect(Collectors.toList());
					String sourceCount = contractCount.subtract(countByOutSource).toPlainString();
					String [] args = {count.get(sourceId).toPlainString(),sourceCount,collect.get(0).getSourceCode()};
					Map<String,Object> result = Maps.newHashMap();
					result.put("sourceId",sourceId);
					result.put("sourceCount",sourceCount);
					return R.error(500,messageSourceHandler.getMessage("stock.number.error", args),result);
				}
			}
		}
		return null;
	}

}
