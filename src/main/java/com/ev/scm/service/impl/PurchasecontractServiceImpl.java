package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.ContractAlterationDao;
import com.ev.scm.dao.PurchasecontractDao;
import com.ev.scm.domain.*;
import com.ev.scm.service.ContractAlterationService;
import com.ev.scm.service.PurchasecontractItemService;
import com.ev.scm.service.PurchasecontractPayService;
import com.ev.scm.service.PurchasecontractService;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class PurchasecontractServiceImpl implements PurchasecontractService {
	@Autowired
	private PurchasecontractDao purchasecontractDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Autowired
	private PurchasecontractPayService purchasecontractPayService;
	@Autowired
	private PurchasecontractItemService purchasecontractItemService;
	@Autowired
	private ContractAlterationService contractAlterationService;
	@Autowired
	private ContractAlterationDao contractAlterationDao;
	@Autowired
	private MaterielService materielService;

	@Override
	public PurchasecontractDO get(Long id) {
		return purchasecontractDao.get(id);
	}

	@Override
	public List<PurchasecontractDO> list(Map<String, Object> map) {
		return purchasecontractDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return purchasecontractDao.count(map);
	}

	@Override
	public int save(PurchasecontractDO purchasecontract) {
		return purchasecontractDao.save(purchasecontract);
	}

	@Override
	public int update(PurchasecontractDO purchasecontract) {
		return purchasecontractDao.update(purchasecontract);
	}

	@Override
	public int remove(Long id) {
		return purchasecontractDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return purchasecontractDao.batchRemove(ids);
	}

	@Override
	public R addOrChangePurchasecontract(PurchasecontractDO purchasecontractDO, String bodyItem, String bodyPay, Long[] itemIds, Long[] payIds) {
		Map<String, Object> result = Maps.newHashMap();
		Long id = purchasecontractDO.getId();
		// 新增
		if (id == null) {
			purchasecontractDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			purchasecontractDO.setCloseStatus(0);
			purchasecontractDO.setContractCode(this.purchaseContractCode());
			purchasecontractDao.save(purchasecontractDO);
			id = purchasecontractDO.getId();
		} else {
			// 修改
			// 验证是否能修改 CloseStatus 0 未关闭 1 关闭
			PurchasecontractDO purchasecontractDo = this.get(id);
			if (!Objects.equals(purchasecontractDo.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
				return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
			}
			purchasecontractDao.update(purchasecontractDO);
		}

		// 删除子项目内的数据
		if (Objects.nonNull(itemIds)&&itemIds.length > 0) {
			purchasecontractItemService.batchRemove(itemIds);
		}

		// 删除合同首款条件数据
		if (Objects.nonNull(payIds)&&payIds.length > 0) {
			purchasecontractPayService.batchRemove(payIds);
		}

		// 保存销售合同收款条件
		List<PurchasecontractPayDO> pay = JSON.parseArray(bodyPay, PurchasecontractPayDO.class);
		for (PurchasecontractPayDO payData : pay) {
			// 新增
			if (payData.getId() == null) {
				payData.setPurchaseContractId(id);
				purchasecontractPayService.save(payData);
			} else {
				// 修改
				purchasecontractPayService.update(payData);
			}
		}

		// 保存销售合同子项目表
		List<PurchasecontractItemDO> item = JSON.parseArray(bodyItem, PurchasecontractItemDO.class);
		for (PurchasecontractItemDO itemData : item) {
			// 新增
			if (itemData.getId() == null) {
				itemData.setPurchaseContractId(id);
				purchasecontractItemService.save(itemData);
			} else {
				// 修改
				purchasecontractItemService.update(itemData);
			}
		}
		result.put("id", id);
		return R.ok(result);
	}

	private String purchaseContractCode() {
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.PURCHASE_CONTRCAT);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<PurchasecontractDO> list = this.list(param);
		String taskNo = null;
		if (!list.isEmpty()) {
			taskNo = list.get(0).getContractCode();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}

	@Override
	public R audit(Long id) {
		PurchasecontractDO purchasecontractDO = this.get(id);
		if (purchasecontractDO.getCloseStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
		}
		if (Objects.equals(purchasecontractDO.getAuditSign(), ConstantForGYL.OK_AUDITED)) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
		}
		purchasecontractDO.setAuditSign(ConstantForGYL.OK_AUDITED);
		purchasecontractDO.setAuditor(ShiroUtils.getUserId());
		return this.update(purchasecontractDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R disAudit(Long id) {
		PurchasecontractDO purchasecontractDO = this.get(id);
		if (Objects.equals(purchasecontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
		}
		if (purchasecontractDO.getCloseStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
		}
		purchasecontractDO.setAuditor(0L);
		purchasecontractDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
		return this.update(purchasecontractDO) > 0 ? R.ok() : R.error();
	}


	@Override
	public R close(Long id) {
		PurchasecontractDO purchasecontractDO = this.get(id);
		if (Objects.equals(purchasecontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
		}
		if (purchasecontractDO.getCloseStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
		}

		purchasecontractDO.setCloseStatus(1);
		return this.update(purchasecontractDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R disClose(Long id) {
		PurchasecontractDO purchasecontractDO = this.get(id);
		if (Objects.equals(purchasecontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("scm.close.isWaitAudit", null));
		}
		if (purchasecontractDO.getCloseStatus() == 0) {
			return R.error(messageSourceHandler.getMessage("scm.contract.isClose", null));
		}

		purchasecontractDO.setCloseStatus(0);
		return this.update(purchasecontractDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R removeContract(Long[] contractIds) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", contractIds);
		int rows = purchasecontractDao.canDeletOfCount(map);

		if (rows == 0) {
			purchasecontractDao.batchRemove(contractIds);
			purchasecontractItemService.batchRemoveByContractId(contractIds);
			purchasecontractPayService.batchRemoveByContractId(contractIds);
			return R.ok();
		} else {
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk", null));
		}
	}

	@Override
	public R getDetailOfContract(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Map<String, Object> detailOfHead = purchasecontractDao.detailOfContract(map);
		List<Map<String, Object>> detailOfBody = purchasecontractItemService.detailOfItem(map);
		Map<String, Object> totalOfItem = purchasecontractItemService.totalOfItem(map);
		List<PurchasecontractPayDO> detailOfPay = purchasecontractPayService.detailOfPay(map);
		Map<String, Object> totalOfPay = purchasecontractPayService.totalOfPay(map);

		Map<String, Object> result = new HashMap<String, Object>();
		map.clear();
		if (Objects.nonNull(detailOfHead)) {
			map.put("detailOfHead", detailOfHead);
			map.put("detailOfBody", detailOfBody);
			map.put("detailOfTail", detailOfPay);
			map.put("totalOfItem", totalOfItem);
			map.put("totalOfPay", totalOfPay);
			result.put("data", map);
		}
		return R.ok(result);
	}


	@Override
	public R editPurchaseContract(Long purchaseContractId, String bodyItem, String bodyPay, Long[] payIds) {

		PurchasecontractDO purchasecontractDO = this.get(purchaseContractId);
		if (Objects.equals(purchasecontractDO.getAuditSign(), ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("scm.contract.isUpdate.notAlteration", null));
		}
		if (purchasecontractDO.getCloseStatus() == 1) {
			return R.error(messageSourceHandler.getMessage("common.contract.isCloseStatus", null));
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", purchaseContractId);
		//合同明细++付款明细
		List<PurchasecontractItemDO> oldDetailOfBody = purchasecontractItemService.list(params);
		List<PurchasecontractPayDO> oldDetailOfPay = purchasecontractPayService.detailOfPay(params);
		JSONObject alterationContent = new JSONObject();

		//处理明细
		List<ContractItemVO> itemList = this.getContractItemVOS(bodyItem, oldDetailOfBody);
		alterationContent.put("itemArray", itemList);
		//处理支付明细
		List<ContractPayVO> payList = this.getContractPayVOS(bodyPay, payIds, oldDetailOfPay);
		alterationContent.put("payArray", payList);

		if (itemList.size() > 0 || payList.size() > 0) {
			ContractAlterationDO alterationDO = new ContractAlterationDO();
			alterationDO.setAlterationContent(alterationContent.toJSONString());
			alterationDO.setContractId(purchaseContractId);
			alterationDO.setContractCode(this.get(purchaseContractId).getContractCode());
			alterationDO.setContractType(ConstantForGYL.CGHT);
			contractAlterationDao.save(alterationDO);
		}
		return R.ok();
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return purchasecontractDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return purchasecontractDao.countForMap(map);
	}

	@Override
	public R alterationList(Map<String, Object> map) {
		List<Map<String, Object>> data = contractAlterationDao.listForMap(map);
		Map<String, Object> result = Maps.newHashMap();
		int total = contractAlterationDao.countForMap(map);
		if (data.size() > 0) {
			DsResultResponse dsRet = new DsResultResponse();
			Integer pagesize = (Integer) map.get("limit");
			dsRet.setDatas(data);
			dsRet.setPageno((int) map.get("pageno"));
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			result.put("data", dsRet);
		}
		return R.ok(result);
	}

	@Override
	public R getAlterationDetail(Long id) {
		Map<String,Object> result = Maps.newHashMap();
		ContractAlterationDO contractAlterationDO = contractAlterationDao.get(id);

		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",id);
		Map<String, Object> detailOfHead = purchasecontractDao.detailOfContract(map);
		result.put("detailOfHead",detailOfHead);

		String alterationContent = contractAlterationDO.getAlterationContent();
		if (StringUtils.isNoneEmpty(alterationContent)){

			JSONObject alterationContentJSON = JSON.parseObject(alterationContent);
			JSONArray itemArray = alterationContentJSON.getJSONArray("itemArray");
			JSONArray payArray = alterationContentJSON.getJSONArray("payArray");
			if (itemArray.size() > 0) {
				Map<String,Object> param;
				Map<String, Object> materiel;
				for (int i = 0; i < itemArray.size(); i++) {
					JSONObject itemJSONObject = itemArray.getJSONObject(i);
					param = Maps.newHashMap();
					param.put("id",itemJSONObject.get("materielId"));
					param.put("offset", 0);
					param.put("limit", 1);
					materiel = materielService.listForMap(param).get(0);
					itemJSONObject.put("unitUomName",materiel.getOrDefault("unitUomName",""));
					itemJSONObject.put("name",materiel.getOrDefault("name",""));
					itemJSONObject.put("serialNo", materiel.getOrDefault("serialNo",""));
					itemJSONObject.put("specification",materiel.getOrDefault("specification",""));
				}
				result.put("itemArray",itemArray);
				result.put("payArray",payArray);
				return R.ok(result);
			}
		}
		return R.ok(result);
	}

	public List<ContractItemVO> getContractItemVOS(String newBodyItem, List<PurchasecontractItemDO> oldDetailOfBody) {

		List<ContractItemVO> itemList = Lists.newArrayList();
		if (oldDetailOfBody.size() > 0) {
			ContractItemVO contractItemVO;
			// 销售合同子项目表
			List<PurchasecontractItemDO> newPurchaseContractItemList = JSON.parseArray(newBodyItem, PurchasecontractItemDO.class);
			// 找出变更的项目
			for (PurchasecontractItemDO newPurchaseContractItemDO : newPurchaseContractItemList) {
				Long itemId = newPurchaseContractItemDO.getId();
				for (PurchasecontractItemDO oldContractItemDO : oldDetailOfBody) {
					if (Objects.equals(itemId, oldContractItemDO.getId())) {
						boolean isUpdate = false;
						// 变更数量
						BigDecimal oldContractItemCount = oldContractItemDO.getCount();
						BigDecimal newPurchaseContractItemCount = newPurchaseContractItemDO.getCount();
						// 变更金额
						BigDecimal oldContractItemTaxAmount = oldContractItemDO.getTaxAmount();
						BigDecimal newPurchaseContractItemTaxAmount = newPurchaseContractItemDO.getTaxAmount();
						// 保存修改后的数据变更记录表
						contractItemVO = new ContractItemVO();
						contractItemVO.setId(itemId);
						if (oldContractItemCount.compareTo(newPurchaseContractItemCount) != 0) {
							contractItemVO.setCountAfter(newPurchaseContractItemCount.toPlainString());
							isUpdate = true;
						}

						if (oldContractItemTaxAmount.compareTo(newPurchaseContractItemTaxAmount) != 0) {
							contractItemVO.setTaxAmountAfter(newPurchaseContractItemTaxAmount.toPlainString());
							isUpdate = true;
						}

						if (isUpdate) {
							contractItemVO.setCountBefore(oldContractItemCount.toPlainString());
							contractItemVO.setTaxAmountBefore(oldContractItemTaxAmount.toPlainString());
							contractItemVO.setType("已修改");
							itemList.add(contractItemVO);
							// 修改子表数据
							purchasecontractItemService.update(newPurchaseContractItemDO);
						}
						break;
					}
				}
			}
		}
		return itemList;
	}

	public List<ContractPayVO> getContractPayVOS(String bodyPay, Long[] payIds, List<PurchasecontractPayDO> oldDetailOfPay) {
		List<ContractPayVO> payList = Lists.newArrayList();
		ContractPayVO payVO;
		if (oldDetailOfPay.size() > 0) {
			// 若合同收款条件变了则执行修改操作
			// 若被删除保存删除前的信息
			if (payIds.length > 0) {
				List<PurchasecontractPayDO> collect = oldDetailOfPay.stream().filter(purchasecontractPayDO -> {
					for (Long payId : payIds) {
						return Objects.equals(purchasecontractPayDO.getId(), payId);
					}
					return false;
				}).collect(Collectors.toList());
				// 保存删除了的信息
				for (PurchasecontractPayDO purchasecontractPayDO : collect) {
					payVO = new ContractPayVO();
					payVO.setId(purchasecontractPayDO.getId());
					//应付日期
					payVO.setReceivableDateAfter(DateFormatUtil.getFormateDate(purchasecontractPayDO.getDueDate()));
					payVO.setReceivableDateBefore("");
					//应付金额
					payVO.setReceivableAmountAfter(purchasecontractPayDO.getPayAmount().stripTrailingZeros().toPlainString());
					payVO.setReceivableAmountBefore("");
					payVO.setType("删除");
					payList.add(payVO);
				}
				// 将选中的合同条件删除
				purchasecontractPayService.batchRemove(payIds);
			}

			// 修改合同首款条件
			// 合同收款条件
			List<PurchasecontractPayDO> pay = JSON.parseArray(bodyPay, PurchasecontractPayDO.class);
			for (PurchasecontractPayDO afterPayDO : pay) {
				payVO = new ContractPayVO();
				String receivableDateAfter = DateFormatUtil.getFormateDate(afterPayDO.getDueDate());
				BigDecimal receivableAmountAfter = afterPayDO.getPayAmount().stripTrailingZeros();

				// 若是修改过的或是未变更的数据
				Long afterPayDOId = afterPayDO.getId();
				if (afterPayDOId != null) {
					for (PurchasecontractPayDO beforePayDO : oldDetailOfPay) {
						if (Objects.equals(afterPayDOId, beforePayDO.getId())) {
							boolean isUpdate = false;
							// 保存变更的时间
							String receivableDateBefore = DateFormatUtil.getFormateDate(beforePayDO.getDueDate());
							payVO.setReceivableDateBefore(receivableDateBefore);
							if (!Objects.equals(receivableDateAfter, receivableDateBefore)) {
								payVO.setType("修改");
								payVO.setReceivableDateAfter(receivableDateAfter);
								isUpdate = true;
							}

							// 保存变更的金额
							BigDecimal receivableAmountBefore = beforePayDO.getPayAmount().stripTrailingZeros();
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
							purchasecontractPayService.update(afterPayDO);
							break;
						}
					}
				}
				// 若是新增数据
				// 保存进合同收款条件子表
				purchasecontractPayService.save(afterPayDO);
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



}
