package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.PurchaseInvoiceDao;
import com.ev.scm.domain.PurchaseInvoiceDO;
import com.ev.scm.domain.PurchaseInvoiceItemDO;
import com.ev.scm.domain.PurchasecontractDO;
import com.ev.scm.domain.PurchasecontractItemDO;
import com.ev.scm.service.PurchaseInvoiceItemService;
import com.ev.scm.service.PurchaseInvoiceService;
import com.ev.scm.service.PurchasecontractItemService;
import com.ev.scm.service.PurchasecontractService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private PurchaseInvoiceDao purchaseInvoiceDao;
	@Autowired
	private PurchaseInvoiceItemService purchaseInvoiceItemService;
	@Autowired
	private PurchasecontractService purchasecontractService;

	@Autowired
	private PurchasecontractItemService purchasecontractItemService;

	@Override
	public PurchaseInvoiceDO get(Long id){
		return purchaseInvoiceDao.get(id);
	}
	
	@Override
	public List<PurchaseInvoiceDO> list(Map<String, Object> map){
		return purchaseInvoiceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchaseInvoiceDao.count(map);
	}
	
	@Override
	public int save(PurchaseInvoiceDO purchaseInvoice){
		return purchaseInvoiceDao.save(purchaseInvoice);
	}
	
	@Override
	public int update(PurchaseInvoiceDO purchaseInvoice){
		return purchaseInvoiceDao.update(purchaseInvoice);
	}
	
	@Override
	public int remove(Long id){
		return purchaseInvoiceDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchaseInvoiceDao.batchRemove(ids);
	}

	@Override
	public R addAndChange(PurchaseInvoiceDO purchaseInvoiceDO, String bodyItem, Long[]itemIds){

		Map<String,Object>  map= new HashMap<String,Object>();
		if (Objects.isNull(purchaseInvoiceDO.getId())) {
			String prefix = DateFormatUtil.getWorkOrderno(ConstantForGYL.INVOICE, new Date());
			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
			params.put("maxNo", prefix);
			params.put("offset", 0);
			params.put("limit", 1);
			List<PurchaseInvoiceDO> list = purchaseInvoiceDao.list(params);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getBillCode();
			}
			purchaseInvoiceDO.setBillCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
			purchaseInvoiceDO.setAuditSign(Constant.WAIT_AUDIT);
			int row = purchaseInvoiceDao.save(purchaseInvoiceDO);

			if (row > 0) {
				List<PurchaseInvoiceItemDO> bodys = JSON.parseArray(bodyItem, PurchaseInvoiceItemDO.class);
				for (PurchaseInvoiceItemDO pdata : bodys) {
					pdata.setPurchasebillId(purchaseInvoiceDO.getId());
					purchaseInvoiceItemService.save(pdata);
				}


				map.put("id",purchaseInvoiceDO.getId());
				return R.ok(map);
			} else {
				return R.error();
			}
		} else {

			int rows = purchaseInvoiceDao.update(purchaseInvoiceDO);
			if (Objects.nonNull(itemIds)&&itemIds.length > 0){
				purchaseInvoiceItemService.batchRemove(itemIds);
			}
			if (rows > 0) {
				List<PurchaseInvoiceItemDO> bodys = JSON.parseArray(bodyItem, PurchaseInvoiceItemDO.class);
				for (PurchaseInvoiceItemDO pdata : bodys) {
					if (Objects.nonNull(pdata.getId())) {
						purchaseInvoiceItemService.update(pdata);
					} else {
						pdata.setPurchasebillId(purchaseInvoiceDO.getId());
						purchaseInvoiceItemService.save(pdata);
					}
				}
				map.put("id",purchaseInvoiceDO.getId());
				return R.ok(map);
			} else {
				return R.error();
			}
		}
	}

	@Override
	public R audit(Long id) {
		Map<String,Object>  map= new HashMap<>();
		map.put("purchasebillId",id);
		List<PurchaseInvoiceItemDO> list = purchaseInvoiceItemService.getSourceCode(map);
		String[] str=new String[list.size()];
			for(int i=0;i<list.size();i++){
				str[i]=list.get(i).getSourceCode();
			}
		map.clear();
		map.put("contractCode",str) ;
		int count = purchasecontractService.wetherChangeContract(map);
		if(!Objects.equals(count,str.length)){
			return R.error(messageSourceHandler.getMessage("scm.canDelet.contractChangeOrDelted",null));
		}

		PurchaseInvoiceDO purchaseInvoiceDO = purchaseInvoiceDao.get(id);
		if(Objects.nonNull(purchaseInvoiceDO)){
			if(Objects.equals(purchaseInvoiceDO.getAuditSign(),Constant.WAIT_AUDIT)){
				PurchaseInvoiceDO pDo=new PurchaseInvoiceDO();
				pDo.setAuditSign(Constant.OK_AUDITED);
				pDo.setAuditor(ShiroUtils.getUserId());
				pDo.setAuditTime(new Date());
				pDo.setId(id);
				purchaseInvoiceDao.update(pDo);
				changeContractorInvoicedAmount(id,list,true);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	public void changeContractorInvoicedAmount(Long id, List<PurchaseInvoiceItemDO> list, Boolean sign) {
		if (!list.isEmpty()) {
			for (PurchaseInvoiceItemDO purchaseInvoiceItemDO : list) {
				Map<String, Object> qurey = new HashMap<>();
				String sourceCode = purchaseInvoiceItemDO.getSourceCode();
				qurey.put("sourceCode", sourceCode);
				qurey.put("contractCode", sourceCode);
				Map<String, Object> totalTaxAmount = purchaseInvoiceItemService.getTotalTaxAmount(qurey);
				List<PurchasecontractDO> purchasecontractList = purchasecontractService.list(qurey);
				if (!purchasecontractList.isEmpty()) {
					BigDecimal invoicedAmount = (purchasecontractList.get(0).getInvoicedAmount() == null || "".equals(purchasecontractList.get(0).getInvoicedAmount())) ? new BigDecimal(0) : purchasecontractList.get(0).getInvoicedAmount();
					BigDecimal taxAmount = new BigDecimal(totalTaxAmount.get("totalTaxAmount").toString());

					PurchasecontractDO purchasecontractDO = purchasecontractList.get(0);
					if (sign) {
						purchasecontractDO.setInvoicedAmount(invoicedAmount.add(taxAmount));
					} else {
						purchasecontractDO.setInvoicedAmount(invoicedAmount.subtract(taxAmount));
					}
					purchasecontractService.update(purchasecontractDO);
				}
			}
		}
	}


	@Override
	public R rollBackAudit(Long id) {
		Map<String,Object>  map= new HashMap<>();
		map.put("purchasebillId",id);
		List<PurchaseInvoiceItemDO> list = purchaseInvoiceItemService.getSourceCode(map);

		PurchaseInvoiceDO purchaseInvoiceDO = purchaseInvoiceDao.get(id);
		if(Objects.nonNull(purchaseInvoiceDO)){
			if(Objects.equals(purchaseInvoiceDO.getAuditSign(), Constant.OK_AUDITED)){
				PurchaseInvoiceDO pDo=new PurchaseInvoiceDO();
				pDo.setAuditSign(Constant.WAIT_AUDIT);
				pDo.setAuditor(0L);
				pDo.setId(id);
				purchaseInvoiceDao.update(pDo);
				changeContractorInvoicedAmount(id,list,false);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public R removePurchase(Long[] ids) {

		Map<String,Object>  map= new HashMap<>();
		map.put("id",ids);
		int rows = purchaseInvoiceDao.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			purchaseInvoiceDao.batchRemove(ids);
			purchaseInvoiceItemService.batchRemoveByPurcahseId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return purchaseInvoiceDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return purchaseInvoiceDao.countForMap(map);
	}

	@Override
	public R getDetail(Long id ){
		Map<String,Object>  map= new HashMap<>();
		map.put("id",id);
		Map<String, Object> detailOfHead = purchaseInvoiceDao.getDetailOfHead(map);

		List<Map<String, Object>> detailOfBody = purchaseInvoiceItemService.getDetailOfBody(map);
		Map<String, Object> totalOfItem = purchaseInvoiceItemService.totalOfItem(map);

		map.clear();
		Map<String,Object>  result= new HashMap<>();
		if(Objects.nonNull(detailOfHead)){
			map.put("detailOfHead",detailOfHead);
			map.put("detailOfBody",detailOfBody);
			map.put("totalOfItem",totalOfItem);

			result.put("date",map);
		}
		return R.ok(map);
	}


	@Override
	public String checkSourceCounts(String bodyDetail) {

		List<PurchaseInvoiceItemDO> bodys = JSON.parseArray(bodyDetail, PurchaseInvoiceItemDO.class);
		List<PurchaseInvoiceItemDO> itemDos = new ArrayList<>();
		if (StringUtils.isNotEmpty(bodyDetail)) {
			itemDos = JSON.parseArray(bodyDetail, PurchaseInvoiceItemDO.class);
		} else {
			return messageSourceHandler.getMessage("common.massge.dateIsNon", null);
		}
		//验证采购合同
		for (PurchaseInvoiceItemDO itemDo : itemDos) {

			if (Objects.nonNull(itemDo.getSourceId())) {

				Long sourceId = itemDo.getSourceId();
				BigDecimal thisCount = itemDo.getCount();
				Long sourceType = itemDo.getSourceType();

				if (Objects.nonNull(sourceType)) {
					if (Objects.equals(sourceType, ConstantForGYL.CGHT)) {
						//采购合同的数量
						PurchasecontractItemDO contractItemDO = purchasecontractItemService.get(sourceId);
						if (contractItemDO != null) {

							Map<String, Object> map = new HashMap<>();
							map.put("sourceId", sourceId);
							map.put("sourceType", sourceType);
							if(itemDo.getId()!=null){map.put("id", itemDo.getId());}
							//获取采购发票已引入数量
							BigDecimal inCounts = purchaseInvoiceItemService.getInCountOfInvoiceItem(map);
							BigDecimal inCountOfContract = (inCounts == null) ? BigDecimal.ZERO : inCounts;

							int boo = (contractItemDO.getCount().subtract(inCountOfContract)).compareTo(thisCount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisCount.toPlainString(), contractItemDO.getCount().subtract(inCountOfContract).toPlainString(), itemDo.getSourceCode().toString()};
								messageSourceHandler.getMessage("stock.number.checkError", args);
							}
						} else {
							return messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null);
						}
					} else {
						//引入的源单类型非采购合同
						return messageSourceHandler.getMessage("scm.checkCount.EroorOfSourceType", null);
					}
				} else {
					return messageSourceHandler.getMessage("scm.purchase.haveNoMagOfSource", null);
				}
			}
		}
		return "ok";
	}

	@Override
	public R checkSourceCount(String bodyDetail,Long id ) {

		List<PurchaseInvoiceItemDO> itemDos;
		if (StringUtils.isNotEmpty(bodyDetail)) {
			itemDos = JSON.parseArray(bodyDetail, PurchaseInvoiceItemDO.class);
		} else {
			return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon", null));
		}

		//合并源单数量及sourseId
		Map<Long,BigDecimal>  sourseIdCounts= new HashMap<>();
		for (PurchaseInvoiceItemDO itemDo : itemDos) {
			Long sourseId=itemDo.getSourceId();
			if(sourseId==null){
				continue;
			}
			if(sourseIdCounts.containsKey(sourseId)){
				sourseIdCounts.put(sourseId,sourseIdCounts.get(sourseId).add(itemDo.getCount()));
				continue;
			}
			sourseIdCounts.put(sourseId,itemDo.getCount());
		}

		List<PurchaseInvoiceItemDO> invoiceItemDos=new ArrayList<>();
		for(Long sourseId:sourseIdCounts.keySet()){

			for(PurchaseInvoiceItemDO itemDo : itemDos){
				if(Objects.equals(itemDo.getSourceId(),sourseId)){
					itemDo.setCount(sourseIdCounts.get(sourseId));
                    invoiceItemDos.add(itemDo);
					break;
				}
			}
		}
		//验证采购合同
		for (PurchaseInvoiceItemDO itemDo : invoiceItemDos) {

			if (Objects.nonNull(itemDo.getSourceId())) {

				Long sourceId = itemDo.getSourceId();
				BigDecimal thisCount = itemDo.getCount();
				Long sourceType = itemDo.getSourceType();

				if (Objects.nonNull(sourceType)) {
					if (Objects.equals(sourceType, ConstantForGYL.CGHT)) {
						//采购合同的数量
						PurchasecontractItemDO contractItemDO = purchasecontractItemService.get(sourceId);
						if (contractItemDO != null) {

							Map<String, Object> map = new HashMap<>();
							map.put("sourceId", sourceId);
							map.put("sourceType", sourceType);
							if(id!=null){map.put("id",id);}
							//获取采购发票已引入数量
//							BigDecimal inCounts = purchaseInvoiceItemService.getInCountOfInvoiceItem(map);
							BigDecimal inCountOfContract = purchaseInvoiceItemService.getInCounteExcludeId(map);

							int boo = (contractItemDO.getCount().subtract(inCountOfContract)).compareTo(thisCount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisCount.toPlainString(), contractItemDO.getCount().subtract(inCountOfContract).toPlainString(), itemDo.getSourceCode().toString()};
								Map<String,Object>  maps= new HashMap<>();
								maps.put("sourceId",sourceId);
								maps.put("sourceCount",contractItemDO.getCount().subtract(inCountOfContract));
								return R.error(500,messageSourceHandler.getMessage("stock.number.checkError", args),maps);
							}
						} else {
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null)) ;
						}
					} else {
						//引入的源单类型非采购合同
						return R.error(messageSourceHandler.getMessage("scm.checkCount.EroorOfSourceType", null)) ;
					}
				} else {
					return R.error(messageSourceHandler.getMessage("scm.purchase.haveNoMagOfSource", null));
				}
			}
		}
		return null;
	}



}
