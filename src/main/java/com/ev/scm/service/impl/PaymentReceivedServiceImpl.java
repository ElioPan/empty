package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.PaymentReceivedDao;
import com.ev.scm.domain.PaymentReceivedDO;
import com.ev.scm.domain.PaymentReceivedItemDO;
import com.ev.scm.domain.PurchasecontractPayDO;
import com.ev.scm.domain.SalescontractPayDO;
import com.ev.scm.service.PaymentReceivedItemService;
import com.ev.scm.service.PaymentReceivedService;
import com.ev.scm.service.PurchasecontractPayService;
import com.ev.scm.service.SalescontractPayService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SalescontractPayService salescontractPayService;

	
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

			Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("maxNo", sign);
			param.put("offset", 0);
			param.put("limit", 1);
			param.put("sign", sign);
			List<PaymentReceivedDO> list = paymentReceivedDao.list(param);
			paymentReceivedDO.setPrCode(DateFormatUtil.getWorkOrderno(sign,list.size()>0?list.get(0).getPrCode():null,4));
			paymentReceivedDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
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

        int checkResukt = this.checkContractAmoutn(sign, list, payItemId);
        if(Objects.equals(1,checkResukt)){
            return R.error(messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null));
        }else if(Objects.equals(2,checkResukt)){
            return R.error(messageSourceHandler.getMessage("scm.canDelet.contractPayItemChange",null));
        }else if(checkResukt==3){
            return R.error(messageSourceHandler.getMessage("scm.canDelet.saleContractPayItemChange",null));
        }

		if(Objects.nonNull(paymentReceivedDO)){
			if(Objects.equals(paymentReceivedDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
				PaymentReceivedDO payDo =payDo = new PaymentReceivedDO();
				payDo.setAuditSign(ConstantForGYL.OK_AUDITED);
				payDo.setAuditor(ShiroUtils.getUserId());
				payDo.setAuditTime(new Date());
				payDo.setId(id);
				payDo.setSign(sign);
				this.updateAuditSign(paymentReceivedDO);
				//回写
                changeContractAmoutn(sign,list, payItemId);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}


    public void changeContractAmoutn(String sign,List<PaymentReceivedItemDO> list,Long[] payItemId){

        Map<String,Object>  query= new HashMap<>();
        query.put("payItemId",payItemId);

        if(Objects.equals(sign,ConstantForGYL.PAYMENT_ORDER)){
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
        }else if(Objects.equals(sign,ConstantForGYL.ALL_BILL)){
            //销售合同
            List<SalescontractPayDO> salescontractPayDOS = this.detailOfSalePayById(query);

            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
                for(SalescontractPayDO salecontractPayDo:salescontractPayDOS){
                    if(Objects.equals(pyItemId,salecontractPayDo.getId())){
                        salecontractPayDo.setReceivedAmount(pyReceivedItemDo.getThisAmount().add(salecontractPayDo.getReceivedAmount()));
                        salecontractPayDo.setUnpayAmount(salecontractPayDo.getUnpayAmount().subtract(pyReceivedItemDo.getThisAmount()));
                        salescontractPayService.update(salecontractPayDo);
                        break;
                    }
                }
            }
        }

    }

    public int checkContractAmoutn(String sign,List<PaymentReceivedItemDO> list,Long[] payItemId){
        Map<String,Object>  query= new HashMap<>();
        query.put("payItemId",payItemId);
        if(Objects.equals(sign,ConstantForGYL.PAYMENT_ORDER)){
            //采购合同
            List<PurchasecontractPayDO> purchasecontractPayDO = purchasecontractPayService.detailOfPayById(query);
            if(!Objects.equals(payItemId.length,purchasecontractPayDO.size())){
//                return R.error(messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null));
                return  1;
            }
            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();

                for(PurchasecontractPayDO purchasecontractPayDo:purchasecontractPayDO){
                    if(Objects.equals(pyItemId,purchasecontractPayDo.getId())){
                        int compareTo = pyReceivedItemDo.getThisAmount().compareTo(purchasecontractPayDo.getUnpayAmount());
                        if(compareTo>0){
//                            return R.error(messageSourceHandler.getMessage("scm.canDelet.contractPayItemChange",null));
                            return 2;
                        }
                        break;
                    }
                }
            }
        }else if(Objects.equals(sign,ConstantForGYL.ALL_BILL)){
            //销售合同
            List<SalescontractPayDO> salescontractPayDO = this.detailOfSalePayById(query);
            if(!Objects.equals(payItemId.length,salescontractPayDO.size())){
//                return R.error(messageSourceHandler.getMessage("scm.canDelet.contractPayItemDelet",null));
                return  1;
            }
            for(PaymentReceivedItemDO pyReceivedItemDo:list){
                Long pyItemId=pyReceivedItemDo.getSourcePayItemId();

                for(SalescontractPayDO salecontractPayDo:salescontractPayDO){
                    if(Objects.equals(pyItemId,salecontractPayDo.getId())){
                        int compareTo = pyReceivedItemDo.getThisAmount().compareTo(salecontractPayDo.getUnpayAmount());
                        if(compareTo>0){
//                            return R.error(messageSourceHandler.getMessage("scm.canDelet.saleContractPayItemChange",null));
                            return 3;
                        }
                        break;
                    }
                }
            }
        }
        	return 0;
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
			if(Objects.equals(paymentReceivedDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
				PaymentReceivedDO btDo=new PaymentReceivedDO();
				btDo.setAuditSign(ConstantForGYL.WAIT_AUDIT);
				btDo.setAuditor(0L);
				btDo.setId(id);
				btDo.setSign(sign);
				this.updateAuditSign(btDo);
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

		if(Objects.equals(sign,ConstantForGYL.PAYMENT_ORDER)){
			//采购合同
			List<PurchasecontractPayDO> purchasecontractPayDO = purchasecontractPayService.detailOfPayById(query);
			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(PurchasecontractPayDO purchasecontractPayDo:purchasecontractPayDO){
					if(Objects.equals(pyItemId,purchasecontractPayDo.getId())){
						purchasecontractPayDo.setAmountPaid(purchasecontractPayDo.getAmountPaid().subtract(pyReceivedItemDo.getThisAmount()));
						purchasecontractPayDo.setUnpayAmount(pyReceivedItemDo.getThisAmount().add(purchasecontractPayDo.getUnpayAmount()));
						purchasecontractPayService.update(purchasecontractPayDo);
						break;
					}
				}
			}
		}else if(Objects.equals(sign,ConstantForGYL.ALL_BILL)){
			//销售合同
			List<SalescontractPayDO> salescontractPayDO = this.detailOfSalePayById(query);

			for(PaymentReceivedItemDO pyReceivedItemDo:list){
				Long pyItemId=pyReceivedItemDo.getSourcePayItemId();
				for(SalescontractPayDO salecontractPayDo:salescontractPayDO){
					if(Objects.equals(pyItemId,salecontractPayDo.getId())){
						salecontractPayDo.setReceivedAmount(salecontractPayDo.getReceivedAmount().subtract(pyReceivedItemDo.getThisAmount()));
						salecontractPayDo.setUnpayAmount(pyReceivedItemDo.getThisAmount().add(salecontractPayDo.getUnpayAmount()));
						salescontractPayService.update(salecontractPayDo);
						break;
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
	public R getdetail(Long id) {
		Map<String,Object>  map= new HashMap<>();
		map.put("id",id);
		Map<String,Object> receivedDetail = this.detailOfReceived(map);
		List<Map<String, Object>> detailOfItem = paymentReceivedItemService.detailOfitem(map);
		Map<String, Object> totallAmount = paymentReceivedItemService.totallAmount(map);
		Map<String,Object>  result= new HashMap<>();
		map.clear();
		if(Objects.nonNull(receivedDetail)) {
			map.put("receivedDetail",receivedDetail);
			map.put("detailOfItem",detailOfItem);
			//子表总金额
			map.put("totallAmount",totallAmount);
			result.put("data",map);
		}
		return R.ok(result);
	}





}
