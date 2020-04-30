package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.PaymentReceivedDao;
import com.ev.scm.domain.*;
import com.ev.scm.service.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class PaymentReceivedServiceImpl implements PaymentReceivedService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private PaymentReceivedDao paymentReceivedDao;
	@Autowired
	private PaymentReceivedItemService paymentReceivedItemService;

	@Autowired
	private PurchasecontractPayService purchasecontractPayService;
	@Autowired
	private OutsourcingContractPayService outsourcingContractPayService;
	@Autowired
    private SalescontractPayService salescontractPayService;
	@Autowired
	private OtherReceivablesItemService otherReceivablesItemService;


	@Override
	public Map<String, Object> detailOfPurchaseContrat(Long[] ids) {
		return paymentReceivedDao.detailOfPurchaseContrat(ids);
	}



	@Override
	public List<Map<String, Object>> detailOfReceivedAmount(Map<String, Object> map) {
		return paymentReceivedDao.detailOfReceivedAmount(map);
	}
	@Override
	public Map<String, Object> detailOfSaleContrat(Long[] ids) {
		return paymentReceivedDao.detailOfSaleContrat(ids);
	}

	@Override
	public PaymentReceivedDO get(Long id){
		return paymentReceivedDao.get(id);
	}
	
	@Override
	public List<PaymentReceivedDO> list(Map<String, Object> map){
		return paymentReceivedDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentReceivedDao.count(map);
	}
	
	@Override
	public int save(PaymentReceivedDO paymentReceived){
		return paymentReceivedDao.save(paymentReceived);
	}
	
	@Override
	public int update(PaymentReceivedDO paymentReceived){
		return paymentReceivedDao.update(paymentReceived);
	}
	
	@Override
	public int remove(Long id){
		return paymentReceivedDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paymentReceivedDao.batchRemove(ids);
	}

	@Override
	public int updateAuditSign(PaymentReceivedDO paymentReceived) {
		return paymentReceivedDao.updateAuditSign(paymentReceived);
	}

	@Override
	public int canDeletOfCount(Map<String, Object> map) {
		return paymentReceivedDao.canDeletOfCount(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return paymentReceivedDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return paymentReceivedDao.countForMap(map);
	}

	@Override
	public Map<String, Object> detailOfReceived(Map<String, Object> map) {
		return paymentReceivedDao.detailOfReceived(map);
	}

	@Override
	public List<SalescontractPayDO> detailOfSalePayById(Map<String, Object> map) {
		return paymentReceivedDao.detailOfSalePayById(map);
	}


	@Override
	public R addReceived(PaymentReceivedDO paymentReceivedDO, String paymentBodys, Long[] deleItemIds,String sign){

		if (Objects.isNull(paymentReceivedDO.getId())) {

			String maxNo = DateFormatUtil.getWorkOrderno(sign);
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("maxNo", maxNo);
			param.put("offset", 0);
			param.put("limit", 1);
			List<PaymentReceivedDO> list = paymentReceivedDao.list(param);
			String taskNo = null;
			if (!list.isEmpty()) {
				taskNo = list.get(0).getPrCode();
			}
			paymentReceivedDO.setPrCode(DateFormatUtil.getWorkOrderno(maxNo, taskNo));

			paymentReceivedDO.setAuditSign(Constant.WAIT_AUDIT);
			paymentReceivedDO.setSign(sign);
			int row = paymentReceivedDao.save(paymentReceivedDO);
			if (row > 0) {
				List<PaymentReceivedItemDO> bodys = JSON.parseArray(paymentBodys, PaymentReceivedItemDO.class);
				for (PaymentReceivedItemDO bPdata : bodys) {
					bPdata.setPaymentReceivedId(paymentReceivedDO.getId());
					paymentReceivedItemService.save(bPdata);
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",paymentReceivedDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		} else {
			int rows = paymentReceivedDao.update(paymentReceivedDO);
			if (Objects.nonNull(deleItemIds)&&deleItemIds.length > 0){
				paymentReceivedItemService.batchRemove(deleItemIds);
			}
			if (rows > 0) {
				List<PaymentReceivedItemDO> bodys = JSON.parseArray(paymentBodys, PaymentReceivedItemDO.class);
				for (PaymentReceivedItemDO bPdata : bodys) {
					if (Objects.nonNull(bPdata.getId())) {
						paymentReceivedItemService.update(bPdata);
					} else {
						bPdata.setPaymentReceivedId(paymentReceivedDO.getId());
						paymentReceivedItemService.save(bPdata);
					}
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",paymentReceivedDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		}
	}

	@Override
	public R audit(Long id,String sign) {

		PaymentReceivedDO paymentReceivedDO = this.get(id);

		if(Objects.nonNull(paymentReceivedDO)){
			if(!Objects.equals(paymentReceivedDO.getAuditSign(),Constant.WAIT_AUDIT)){
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}
			Map<String,Object>  map= new HashMap<>();
			map.put("paymentReceivedId",id);
			map.put("sign",sign);

			List<PaymentReceivedItemDO> list = paymentReceivedItemService.list(map);
			Map<String,Object>  query= new HashMap<>();
			Long[] payItemId=new Long[list.size()];

			for (int i=0;i<list.size();i++){
				PaymentReceivedItemDO  paymentReceivedItemDO=list.get(i);
				Long sourcePayItemId = paymentReceivedItemDO.getSourcePayItemId();
				if(Objects.nonNull(sourcePayItemId)){
					payItemId[i]=sourcePayItemId;
				}
			}
			query.put("payItemId",payItemId);
		//验证合同金额
        String  checkResukt = this.checkContractAmoutn(sign, list, payItemId);
        if(!Objects.equals("ok",checkResukt)){
            return R.error(checkResukt);
        }

		if(Objects.nonNull(paymentReceivedDO)){
			if(Objects.equals(paymentReceivedDO.getAuditSign(), Constant.WAIT_AUDIT)){
				PaymentReceivedDO payDo = new PaymentReceivedDO();
				payDo.setAuditSign(Constant.OK_AUDITED);
				payDo.setAuditor(ShiroUtils.getUserId());
				payDo.setAuditTime(new Date());
				payDo.setId(id);
				payDo.setSign(sign);
				int i = this.updateAuditSign(payDo);
                this.changeContractAmoutn(sign,list, payItemId);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}


    public void changeContractAmoutn(String sign,List<PaymentReceivedItemDO> list,Long[] payItemId){

		Long sourceTyp=list.get(0).getSourceType();

        Map<String,Object>  query= new HashMap<>();
        query.put("payItemId",payItemId);

        if(Objects.equals(sourceTyp,ConstantForGYL.CGHT)){
            //采购合同
            List<PurchasecontractPayDO> purchasecontractPayDOS = purchasecontractPayService.detailOfPayById(query);
            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
                for(PurchasecontractPayDO purchasecontractPayDo:purchasecontractPayDOS){
                    if(Objects.equals(pyItemId,purchasecontractPayDo.getId())){
                        purchasecontractPayDo.setAmountPaid(pyReceivedItemDo.getThisAmount().add(purchasecontractPayDo.getAmountPaid()));
                        purchasecontractPayDo.setUnpayAmount(purchasecontractPayDo.getUnpayAmount().subtract(pyReceivedItemDo.getThisAmount()));
                        purchasecontractPayService.update(purchasecontractPayDo);
                        break;
                    }
                }
            }
        }else if(Objects.equals(sourceTyp,ConstantForGYL.XSHT)){
            //销售合同
            List<SalescontractPayDO> salescontractPayDOS = this.detailOfSalePayById(query);

            for(PaymentReceivedItemDO pyReceivedItemDo:list){

                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();

                for(SalescontractPayDO salecontractPayDo:salescontractPayDOS){

                    if(Objects.equals(pyItemId,salecontractPayDo.getId())){
                        salecontractPayDo.setReceivedAmount(pyReceivedItemDo.getThisAmount().add(salecontractPayDo.getReceivedAmount()));
                        salecontractPayDo.setUnpayAmount(salecontractPayDo.getUnpayAmount().subtract(pyReceivedItemDo.getThisAmount()));
                        salescontractPayService.update(salecontractPayDo);
                    }
                }
            }
        }else if(Objects.equals(sourceTyp,ConstantForGYL.OTHER_RECIVEABLE_TYPE)||Objects.equals(sourceTyp,ConstantForGYL.OTHER_PAYABLE_TYPE)){
			List<OtherReceivablesItemDO> otherNoReceiptAmount = otherReceivablesItemService.otherNoReceiptAmount(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OtherReceivablesItemDO otherDo:otherNoReceiptAmount){
					if(Objects.equals(pyItemId,otherDo.getId())){
						otherDo.setPaidReceivedAmount((otherDo.getPaidReceivedAmount()==null?BigDecimal.ZERO:otherDo.getPaidReceivedAmount()).add(pyReceivedItemDo.getThisAmount()));
						otherDo.setNoReceiptPaymentAmount((otherDo.getNoReceiptPaymentAmount()==null?BigDecimal.ZERO:otherDo.getNoReceiptPaymentAmount()).subtract(pyReceivedItemDo.getThisAmount()));
						otherReceivablesItemService.update(otherDo);
					}
				}
			}
		}else if(Objects.equals(sourceTyp,ConstantForGYL.WWHT)){
			//委外合同
			List<OutsourcingContractPayDO> outContractPayAmount = outsourcingContractPayService.getOutContractPayAmount(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OutsourcingContractPayDO outsourcingContractPayDO:outContractPayAmount){
					if(Objects.equals(pyItemId,outsourcingContractPayDO.getId())){
						outsourcingContractPayDO.setUnpaidAmount(outsourcingContractPayDO.getUnpaidAmount().subtract(pyReceivedItemDo.getThisAmount()));
						outsourcingContractPayDO.setPaidAmount((outsourcingContractPayDO.getPaidAmount()==null?BigDecimal.ZERO:outsourcingContractPayDO.getPaidAmount()).add(pyReceivedItemDo.getThisAmount()));
						outsourcingContractPayService.update(outsourcingContractPayDO);
				}
				}
			}
		}
    }

    public String checkContractAmoutn(String sign,List<PaymentReceivedItemDO> list,Long[] payItemId){

		Long sourceType=list.get(0).getSourceType();

        Map<String,Object>  query= new HashMap<>();
        query.put("payItemId",payItemId);
        if(Objects.equals(sourceType,ConstantForGYL.CGHT)){
            //采购合同
            List<PurchasecontractPayDO> purchasecontractPayDO = purchasecontractPayService.detailOfPayById(query);
            if(!Objects.equals(payItemId.length,purchasecontractPayDO.size())){
                return  messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null);
            }
            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
                for(PurchasecontractPayDO purchasecontractPayDo:purchasecontractPayDO){
                    if(Objects.equals(pyItemId,purchasecontractPayDo.getId())){
                        int compareTo = pyReceivedItemDo.getThisAmount().compareTo(purchasecontractPayDo.getUnpayAmount());
                        if(compareTo>0){
                            return messageSourceHandler.getMessage("scm.canDelet.contractPayItemChange",null);
                        }
                        break;
                    }
                }
            }
        }else if(Objects.equals(sourceType,ConstantForGYL.XSHT)){
            //销售合同
            List<SalescontractPayDO> salescontractPayDO = this.detailOfSalePayById(query);
            if(!Objects.equals(payItemId.length,salescontractPayDO.size())){
                return  messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null);
            }
            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
                for(SalescontractPayDO salecontractPayDo:salescontractPayDO){
                    if(Objects.equals(pyItemId,salecontractPayDo.getId())){
                        int compareTo = pyReceivedItemDo.getThisAmount().compareTo(salecontractPayDo.getUnpayAmount());
                        if(compareTo>0){
                            return messageSourceHandler.getMessage("scm.canDelet.saleContractPayItemChange",null);
                        }
                        break;
                    }
                }
            }
      	}else if(Objects.equals(sourceType,ConstantForGYL.OTHER_RECIVEABLE_TYPE)||Objects.equals(sourceType,ConstantForGYL.OTHER_PAYABLE_TYPE)){
			//其他应收应付
			List<OtherReceivablesItemDO> otherNoReceiptAmount = otherReceivablesItemService.otherNoReceiptAmount(query);
			if(!Objects.equals(payItemId.length,otherNoReceiptAmount.size())){
				return  messageSourceHandler.getMessage("scm.theSourceList.beDeleted",null);
			}
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OtherReceivablesItemDO otherDo:otherNoReceiptAmount){
					if(Objects.equals(pyItemId,otherDo.getId())){
						int result = pyReceivedItemDo.getThisAmount().compareTo(otherDo.getNoReceiptPaymentAmount());
						if(result>0){
							if(Objects.equals(sign,ConstantForGYL.PAYMENT_ORDER)){
								String[]  msg={pyReceivedItemDo.getThisAmount().toPlainString(),otherDo.getNoReceiptPaymentAmount().toPlainString(),pyReceivedItemDo.getSourceCode()};
								return messageSourceHandler.getMessage("stock.payRecived.checkErrorPurchase",msg);
							}else{
								String[]  msg={pyReceivedItemDo.getThisAmount().toPlainString(),otherDo.getNoReceiptPaymentAmount().toPlainString(),pyReceivedItemDo.getSourceCode()};
								return messageSourceHandler.getMessage("stock.payRecived.checkErrorSales",msg);
							}
						}
						break;
					}
				}
			}
		}else if(Objects.equals(sourceType,ConstantForGYL.WWHT)){
        	//委外合同
			List<OutsourcingContractPayDO> outContractPayAmount = outsourcingContractPayService.getOutContractPayAmount(query);
			if(!Objects.equals(payItemId.length,outContractPayAmount.size())){
				return  messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null);
			}
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OutsourcingContractPayDO outsourcingContractPayDO:outContractPayAmount){
					if(Objects.equals(pyItemId,outsourcingContractPayDO.getId())){
						int compareTo = pyReceivedItemDo.getThisAmount().compareTo(outsourcingContractPayDO.getUnpaidAmount());
						if(compareTo>0){
							String[]  msg={pyReceivedItemDo.getThisAmount().toPlainString(),outsourcingContractPayDO.getUnpaidAmount().toPlainString(),pyReceivedItemDo.getSourceCode()};
							return messageSourceHandler.getMessage("stock.payRecived.checkErrorPurchase",msg);
						}
						break;
					}
				}
			}
		}
        	return "ok";
    }

	@Override
	public R rollBackAudit(Long id,String sign) {
		PaymentReceivedDO paymentReceivedDO = this.get(id);
		Map<String,Object>  map= new HashMap<>();
		map.put("paymentReceivedId",id);
		map.put("sign",sign);
		List<PaymentReceivedItemDO> list = paymentReceivedItemService.list(map);
		Map<String,Object>  query= new HashMap<>();
		Long[] payItemId=new Long[list.size()];
		for (int i=0;i<list.size();i++){
			PaymentReceivedItemDO  paymentReceivedItemDO=list.get(i);
			Long sourcePayItemId = paymentReceivedItemDO.getSourcePayItemId();
			if(Objects.nonNull(sourcePayItemId)){
				payItemId[0]=sourcePayItemId;
			}
		}
		query.put("payItemId",payItemId);

		if(Objects.nonNull(paymentReceivedDO)){
			if(Objects.equals(paymentReceivedDO.getAuditSign(),Constant.OK_AUDITED)){
				PaymentReceivedDO btDo=this.get(id);
				btDo.setAuditor(0L);
				btDo.setId(id);
				btDo.setSign(sign);
				btDo.setAuditTime(null);
				btDo.setAuditSign(Constant.WAIT_AUDIT);
				btDo.setUpdateTime(new Date());
				paymentReceivedDao.updateAll(btDo);
				this.changeBackAuditContractAmoutn(sign,list,payItemId);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	public void changeBackAuditContractAmoutn(String sign,List<PaymentReceivedItemDO> list,Long[] payItemId){

		Map<String,Object>  query= new HashMap<>();
		query.put("payItemId",payItemId);

		Long sourceType=list.get(0).getSourceType();

		if(Objects.equals(sourceType,ConstantForGYL.CGHT)){
			//采购合同
			List<PurchasecontractPayDO> purchasecontractPayDO = purchasecontractPayService.detailOfPayById(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(PurchasecontractPayDO purchasecontractPayDo:purchasecontractPayDO){
					if(Objects.equals(pyItemId,purchasecontractPayDo.getId())){
						purchasecontractPayDo.setAmountPaid(purchasecontractPayDo.getAmountPaid().subtract(pyReceivedItemDo.getThisAmount()));
						purchasecontractPayDo.setUnpayAmount(pyReceivedItemDo.getThisAmount().add(purchasecontractPayDo.getUnpayAmount()));
						purchasecontractPayService.update(purchasecontractPayDo);
					}
				}
			}
		}else if(Objects.equals(sourceType,ConstantForGYL.XSHT)){
			//销售合同
			List<SalescontractPayDO> salescontractPayDO = this.detailOfSalePayById(query);

			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(SalescontractPayDO salecontractPayDo:salescontractPayDO){
					if(Objects.equals(pyItemId,salecontractPayDo.getId())){
						salecontractPayDo.setReceivedAmount(salecontractPayDo.getReceivedAmount().subtract(pyReceivedItemDo.getThisAmount()));
						salecontractPayDo.setUnpayAmount(pyReceivedItemDo.getThisAmount().add(salecontractPayDo.getUnpayAmount()));
						salescontractPayService.update(salecontractPayDo);
					}
				}
			}
		}else if(Objects.equals(sourceType,ConstantForGYL.OTHER_RECIVEABLE_TYPE)||Objects.equals(sourceType,ConstantForGYL.OTHER_PAYABLE_TYPE)){
			List<OtherReceivablesItemDO> otherNoReceiptAmount = otherReceivablesItemService.otherNoReceiptAmount(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OtherReceivablesItemDO otherDo:otherNoReceiptAmount){
					if(Objects.equals(pyItemId,otherDo.getId())){
						otherDo.setPaidReceivedAmount((otherDo.getPaidReceivedAmount()==null?BigDecimal.ZERO:otherDo.getPaidReceivedAmount()).subtract(pyReceivedItemDo.getThisAmount()));
						otherDo.setNoReceiptPaymentAmount((otherDo.getNoReceiptPaymentAmount()==null?BigDecimal.ZERO:otherDo.getNoReceiptPaymentAmount()).add(pyReceivedItemDo.getThisAmount()));
						otherReceivablesItemService.update(otherDo);
					}
				}
			}
		}else if(Objects.equals(sourceType,ConstantForGYL.WWHT)){
			List<OutsourcingContractPayDO> outContractPayAmount = outsourcingContractPayService.getOutContractPayAmount(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(OutsourcingContractPayDO outsourcingContractPayDO:outContractPayAmount){
					if(Objects.equals(pyItemId,outsourcingContractPayDO.getId())){
						outsourcingContractPayDO.setUnpaidAmount(outsourcingContractPayDO.getUnpaidAmount().add(pyReceivedItemDo.getThisAmount()));
						outsourcingContractPayDO.setPaidAmount((outsourcingContractPayDO.getPaidAmount()==null?BigDecimal.ZERO:outsourcingContractPayDO.getPaidAmount()).subtract(pyReceivedItemDo.getThisAmount()));
						outsourcingContractPayService.update(outsourcingContractPayDO);
					}
				}
			}
		}
	}


	@Override
	public R removeReceived(Long[] ids) {

		Map<String,Object>  map= new HashMap<>();
		map.put("id",ids);
		int rows = this.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			this.batchRemove(ids);
			paymentReceivedItemService.removeByReceivedId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public R getdetail(Long id,String sign) {
		Map<String,Object>  map= new HashMap<>();
		map.put("id",id);
		Map<String,Object> receivedDetail = this.detailOfReceived(map);
		Map<String,Object>  query= new HashMap<>();
		query.put("paymentReceivedId",id);
		List<PaymentReceivedItemDO> list = paymentReceivedItemService.list(query);
		Long sourceType=list.get(0).getSourceType();

		Map<String, Object> totallAmount = paymentReceivedItemService.totallAmount(map);
		List<Map<String, Object>> detailOfItem=new ArrayList<>();
		//采购销售合同
		if(Objects.equals(sourceType,ConstantForGYL.CGHT)||Objects.equals(sourceType,ConstantForGYL.XSHT)){
			detailOfItem = paymentReceivedItemService.detailOfitem(map);
		}else if(Objects.equals(sourceType,ConstantForGYL.WWHT)){
			//委外合同
			detailOfItem = paymentReceivedItemService.detailOfOutsourcing(map);
		}else if(Objects.equals(sourceType,ConstantForGYL.OTHER_RECIVEABLE_TYPE)||Objects.equals(sourceType,ConstantForGYL.OTHER_PAYABLE_TYPE)){
			//其他应收应付
			detailOfItem = paymentReceivedItemService.detailOfAboutOther(map);
		}
		Map<String, Object> stringObjectMap=new HashMap<>();
		if(!detailOfItem.isEmpty()) {
			stringObjectMap = this.totaAmountOfStatistics(detailOfItem, sign);
		}

		Map<String,Object>  result= new HashMap<>();
		map.clear();
		if(Objects.nonNull(receivedDetail)) {

			map.put("receivedDetail",receivedDetail);
			map.put("detailOfItem",detailOfItem);
			//子表总金额
			map.put("totallAmount",totallAmount);
			map.put("allReceivablePayablesAmount", stringObjectMap.getOrDefault("receivablePayablesAmount", 0));
			map.put("allPaidReceivedAmount", stringObjectMap.getOrDefault("paidReceivedAmount", 0));
			map.put("allNoReceiptPaymentAmount",stringObjectMap.getOrDefault("noReceiptPaymentAmount",0));
			result.put("data",map);
		}
		return R.ok(result);
	}


	/**
	 * 查询合同时时金额统计
	 */
	public Map<String,Object> totaAmountOfStatistics(List<Map<String, Object>> list,String sign){

		Long  sourceType=Long.parseLong(list.get(0).get("sourceType").toString());
		Map<String,Object>  map= new HashMap<>();
		List nowList=new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).containsKey("sourceId")){
				nowList.add(list.get(i).get("sourceId"));
			}
		}
		nowList=new ArrayList(new HashSet(nowList));
		Long[] stockInheadIds=new Long[nowList.size()];
		for (int i = 0; i < nowList.size(); i++) {
			stockInheadIds[i]=Long.valueOf(String.valueOf(nowList.get(i)));
		}

		if(Objects.equals(ConstantForGYL.CGHT,sourceType)){
			//统计采购合同
			map= this.detailOfPurchaseContrat(stockInheadIds);
		}else if(Objects.equals(ConstantForGYL.XSHT,sourceType)){
			//销售合同
			map = this.detailOfSaleContrat(stockInheadIds);
		}else if(Objects.equals(ConstantForGYL.OTHER_RECIVEABLE_TYPE,sourceType)||Objects.equals(ConstantForGYL.OTHER_PAYABLE_TYPE,sourceType)){
			map = paymentReceivedDao.detailOfOtherAmount(stockInheadIds);
		}else if(Objects.equals(ConstantForGYL.WWHT,sourceType)){
			map = paymentReceivedDao.detailOfOutContract(stockInheadIds);
		}
		return map;
	}

	@Override
	public R checkSourseCount(String paymentBodys,Long id){

		List<PaymentReceivedItemDO> itemDos;
		if (StringUtils.isNotEmpty(paymentBodys)) {
			itemDos =JSON.parseArray(paymentBodys, PaymentReceivedItemDO.class);
		} else {
			return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon", null));
		}
		//合并数量及sourseId
		Map<Long,BigDecimal>  sourseIdCounts= new HashMap<>();
		for (PaymentReceivedItemDO itemDo : itemDos) {
			Long sourseId=itemDo.getSourceId();
			if(sourseId==null){
				continue;
			}
			if(sourseIdCounts.containsKey(sourseId)){
				sourseIdCounts.put(sourseId,sourseIdCounts.get(sourseId).add(itemDo.getThisAmount()));
				continue;
			}
			sourseIdCounts.put(sourseId,itemDo.getThisAmount());
		}
		List<PaymentReceivedItemDO> paymentReceivedItemDos=new ArrayList<>();
		for(Long sourseId:sourseIdCounts.keySet()){

			for(PaymentReceivedItemDO itemDo : itemDos){
				if(Objects.equals(itemDo.getSourceId(),sourseId)){
					itemDo.setThisAmount(sourseIdCounts.get(sourseId));
					paymentReceivedItemDos.add(itemDo);
					break;
				}
			}
		}
		//采购合同+++++销售合同
		for (PaymentReceivedItemDO itemDo : paymentReceivedItemDos) {

			if (Objects.nonNull(itemDo.getSourceId())) {
				Long sourceId = itemDo.getSourceId();
				//本次收款、付款金额
				BigDecimal thisAmount = itemDo.getThisAmount();
				Long sourceType = itemDo.getSourceType();
				if (Objects.nonNull(sourceType)) {
					if (Objects.equals(sourceType, ConstantForGYL.CGHT)) {
						//获取采购合同付款条件明细数量
						PurchasecontractPayDO purchasecontractPayDO = purchasecontractPayService.get(sourceId);
						if (purchasecontractPayDO != null) {
							BigDecimal unpayAmount = purchasecontractPayDO.getUnpayAmount() == null ? BigDecimal.ZERO : purchasecontractPayDO.getUnpayAmount();
							int boo = unpayAmount.compareTo(thisAmount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisAmount.toPlainString(), unpayAmount.toPlainString(), itemDo.getSourceCode().toString()};
								Map<String, Object> maps = new HashMap<>();
								maps.put("sourceId", sourceId);
								maps.put("sourceCount", unpayAmount);
								return R.error(500, messageSourceHandler.getMessage("stock.payRecived.checkErrorPurchase", args), maps);
							}
						} else {
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
						}
					} else if (Objects.equals(sourceType, ConstantForGYL.XSHT)) {
						//销售合同
						SalescontractPayDO salescontractPayDo = salescontractPayService.get(sourceId);
						if (salescontractPayDo != null) {
							//销售合同未收总金额
							BigDecimal unpayAmount = salescontractPayDo.getUnpayAmount() == null ? BigDecimal.ZERO : salescontractPayDo.getReceivableAmount();
							int boo = unpayAmount.compareTo(thisAmount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisAmount.toPlainString(), unpayAmount.toPlainString(), itemDo.getSourceCode().toString()};
								Map<String, Object> maps = new HashMap<>();
								maps.put("sourceId", sourceId);
								maps.put("sourceCount", unpayAmount);
								return R.error(500, messageSourceHandler.getMessage("stock.payRecived.checkErrorSales", args), maps);
							}
						} else {
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
						}
					}else if(Objects.equals(sourceType, ConstantForGYL.OTHER_PAYABLE_TYPE)){
						//其他应付
						OtherReceivablesItemDO otherReceivablesItemDO = otherReceivablesItemService.get(sourceId);
						if (otherReceivablesItemDO != null) {
							BigDecimal unpayAmount=otherReceivablesItemDO.getNoReceiptPaymentAmount()!=null?otherReceivablesItemDO.getNoReceiptPaymentAmount():(BigDecimal.ZERO);
							int boo = unpayAmount.compareTo(thisAmount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisAmount.toPlainString(), unpayAmount.toPlainString(), itemDo.getSourceCode().toString()};
								Map<String, Object> maps = new HashMap<>();
								maps.put("sourceId", sourceId);
								maps.put("sourceCount", unpayAmount);
								return R.error(500, messageSourceHandler.getMessage("stock.payRecived.checkErrorPurchase", args), maps);
							}
						}else{
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
						}
				}else if(Objects.equals(sourceType, ConstantForGYL.OTHER_RECIVEABLE_TYPE)){
						//其他应收
						OtherReceivablesItemDO otherReceivablesItemDO = otherReceivablesItemService.get(sourceId);
						if (otherReceivablesItemDO != null) {
							BigDecimal unpayAmount=otherReceivablesItemDO.getNoReceiptPaymentAmount()!=null?otherReceivablesItemDO.getNoReceiptPaymentAmount():(BigDecimal.ZERO);
							int boo = unpayAmount.compareTo(thisAmount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisAmount.toPlainString(), unpayAmount.toPlainString(), itemDo.getSourceCode().toString()};
								Map<String, Object> maps = new HashMap<>();
								maps.put("sourceId", sourceId);
								maps.put("sourceCount", unpayAmount);
								return R.error(500, messageSourceHandler.getMessage("stock.payRecived.checkErrorSales", args), maps);
							}
						}else{
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
						}
				}else if(Objects.equals(sourceType, ConstantForGYL.WWHT)){
					//委外合同
						OutsourcingContractPayDO outsourcingContractPayDO = outsourcingContractPayService.get(sourceId);
						if (outsourcingContractPayDO != null) {

							BigDecimal unpayAmount = outsourcingContractPayDO.getUnpaidAmount() == null ? BigDecimal.ZERO : outsourcingContractPayDO.getUnpaidAmount();
							int boo = unpayAmount.compareTo(thisAmount);
							if (Objects.equals(-1, boo)) {
								String[] args = {thisAmount.toPlainString(), unpayAmount.toPlainString(), itemDo.getSourceCode().toString()};
								Map<String, Object> maps = new HashMap<>();
								maps.put("sourceId", sourceId);
								maps.put("sourceCount", unpayAmount);
								return R.error(500, messageSourceHandler.getMessage("stock.payRecived.checkErrorPurchase", args), maps);
							}
						} else {
							return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
						}
                 }else {
						return R.error(messageSourceHandler.getMessage("scm.payRecived.EroorSourceTypeOfIntroduce", null));
				       }
				} else {
					return R.error(messageSourceHandler.getMessage("scm.purchase.haveNoMagOfSource", null));
				}
			}
		}
		return null;
	}



}
