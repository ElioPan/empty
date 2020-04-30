package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.scm.dao.CommutationInitializationDao;
import com.ev.scm.domain.CommutationInitializationDO;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.domain.PaymentReceivedItemDO;
import com.ev.scm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommutationInitializationServiceImpl implements CommutationInitializationService {
	@Autowired
	private CommutationInitializationDao commutationInitializationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private OtherReceivablesItemService otherReceivablesItemService;
	@Autowired
	private OtherReceivablesService otherReceivablesService;
	@Autowired
	private PurchasecontractService purchasecontractService;
	@Autowired
	private PurchasecontractPayService purchasecontractPayService;
	@Autowired
	private PaymentReceivedService paymentReceivedService;
	@Autowired
	private PaymentReceivedItemService paymentReceivedItemService;
	@Autowired
	private PurchasecontractItemService purchasecontractItemService;
	@Autowired
	private SalescontractService salescontractService;
	@Autowired
	private SalescontractPayService salescontractPayService;
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public List<Map<String, Object>> getDetail(Map<String, Object> map) {
		return commutationInitializationDao.getDetail(map);
	}
	@Override
	public CommutationInitializationDO get(Long id){
		return commutationInitializationDao.get(id);
	}
	
	@Override
	public List<CommutationInitializationDO> list(Map<String, Object> map){
		return commutationInitializationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return commutationInitializationDao.count(map);
	}
	
	@Override
	public int save(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.save(commutationInitialization);
	}

	@Override
	public List<Map<String, Object>> getListForMap(Map<String, Object> map) {
		return commutationInitializationDao.getListForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return commutationInitializationDao.countForMap(map);
	}

	@Override
	public int update(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.update(commutationInitialization);
	}

	@Override
	public int updateAll(CommutationInitializationDO commutationInitialization){
		return commutationInitializationDao.updateAll(commutationInitialization);
	}
	@Override
	public Map<String, Object> getDetailByClientOrSupplierId(Map<String, Object> map) {
		return commutationInitializationDao.getDetailByClientOrSupplierId(map);
	}
	@Override
	public int remove(Long id){
		return commutationInitializationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return commutationInitializationDao.batchRemove(ids);
	}

	@Override
	public R disposeAddAndChage(String body){

		List<CommutationInitializationDO> commutationInitializationDos= JSONObject.parseArray(body,CommutationInitializationDO.class);
		if(commutationInitializationDos.size()>0){
			//验重
			List<CommutationInitializationDO> commutationInitializationClinets = commutationInitializationDos.stream().filter(CommutationInitializationDO -> CommutationInitializationDO.getClientId() != null).collect(Collectors.toList());
			List<CommutationInitializationDO> commutationInitializationSupplier = commutationInitializationDos.stream().filter(CommutationInitializationDO -> CommutationInitializationDO.getSupplierId() != null ).collect(Collectors.toList());
			if(commutationInitializationClinets.size()>0){
				for(CommutationInitializationDO clent:commutationInitializationClinets){
					Map<String, Object> detail;
					Map<String,Object>  map= new HashMap<>(2);
					map.put("clientId",clent.getClientId());
					if(clent.getId()!=null){
						map.put("id",clent.getId());
						 detail = this.getDetailByClientOrSupplierId(map);
					}else{
						 detail = this.getDetailByClientOrSupplierId(map);
					}
					if(detail!=null){
						return R.error(messageSourceHandler.getMessage("scm.commutationInitialization.clientAlreadyExist", null));
					}
				}
			}
			if(commutationInitializationSupplier.size()>0){
				for(CommutationInitializationDO supplier:commutationInitializationSupplier){
					Map<String, Object> detail;
					Map<String,Object>  map= new HashMap<>(2);
					map.put("supplierId",supplier.getSupplierId());
					if(supplier.getId()!=null){
						map.put("id",supplier.getId());
						detail = this.getDetailByClientOrSupplierId(map);
					}else{
						detail = this.getDetailByClientOrSupplierId(map);
					}
					if(detail!=null){
						return R.error(messageSourceHandler.getMessage("scm.commutationInitialization.supplierAlreadyExist", null));
					}
				}
			}

			for(CommutationInitializationDO commuIniDo:commutationInitializationDos){
				if(Objects.nonNull(commuIniDo.getId())){
					commutationInitializationDao.update(commuIniDo);
				}else{
					commutationInitializationDao.save(commuIniDo);
				}
			}
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing", null));
		}
	}

	@Override
	public  R getClientAccountMessage(Map<String, Object> parameter){
		//其他应收
		List<Map<String, Object>> otherDate = this.disposeOtherDate(parameter, ConstantForGYL.OTHER_RECIVEABLE);
		//销售合同
		List<Map<String, Object>> saleContractorDate = this.disposeSaleContractorDate(parameter);
		//收款单
		List<Map<String, Object>> paymentDate= this.disposePaymentAmount(parameter, ConstantForGYL.ALL_BILL);
		//组合
		List<Map<String, Object>>  allDate= new ArrayList<>();
		Map<String,Object>  mapCommutation= new HashMap<>();
		mapCommutation.put("clientId",parameter.get("clientId"));

		List<Map<String, Object>> detail = this.getDetail(mapCommutation);
		mapCommutation.remove("clientId");
		if (detail.size() > 0) {
			mapCommutation.put("date", DateFormatUtil.getDateByParttern(detail.get(0).get("createTime").toString(), "yyyy-MM-dd"));
			mapCommutation.put("time", "2000-01-01 00:00:00");
			mapCommutation.put("clientName", detail.get(0).get("clientName"));
			mapCommutation.put("typeName", ConstantForGYL.REMANING_AMOUNT);
			mapCommutation.put("remainingAmount", detail.get(0).get("initialAmount"));
			allDate.add(mapCommutation);
		}

		if(otherDate.size()>0){
			allDate.addAll(otherDate);
		}
		if(saleContractorDate.size()>0){
			allDate.addAll(saleContractorDate);
		}
		if(paymentDate.size()>0){
			allDate.addAll(paymentDate);
		}

		Map<String,Object>  resulst= new HashMap<>();
		//排序   依照时间排序
		if(allDate.size()>0){

			Collections.sort(allDate, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {

					Long s0=DateFormatUtil.getDateByParttern(arg0.get("time").toString(), "yyyy-MM-dd").getTime();
					Long s1=DateFormatUtil.getDateByParttern(arg1.get("time").toString(), "yyyy-MM-dd").getTime();
					return s0.compareTo(s1);
				}
			});
			BigDecimal totailOughtAmount=BigDecimal.ZERO;
			BigDecimal totailReceivedAmount=BigDecimal.ZERO;
			BigDecimal remainingAmount=new BigDecimal(allDate.get(0).getOrDefault("remainingAmount","0").toString());
			for(Map<String, Object> oneDate:allDate){
				oneDate.remove("time");
				BigDecimal oughtAmount=new BigDecimal(oneDate.containsKey("oughtAmount")?oneDate.get("oughtAmount").toString():"0");
				BigDecimal receivedAmount=new BigDecimal(oneDate.containsKey("receivedAmount")?oneDate.get("receivedAmount").toString():"0");
				remainingAmount=remainingAmount.add(oughtAmount).subtract(receivedAmount);
				oneDate.put("remainingAmount",remainingAmount);
				totailOughtAmount=totailOughtAmount.add(new BigDecimal(oneDate.containsKey("oughtAmount")?oneDate.get("oughtAmount").toString():"0"));
				totailReceivedAmount=totailReceivedAmount.add(new BigDecimal(oneDate.containsKey("receivedAmount")?oneDate.get("receivedAmount").toString():"0"));
			}
			int pageno=Integer.parseInt(parameter.get("pageno").toString());
			int pagesize=Integer.parseInt(parameter.get("pagesize").toString());
			List<Map<String, Object>> quoteLists= PageUtils.startPage(allDate, pageno, pagesize);

			Map<String, Object> dsRet = new HashMap<>();
			dsRet.put("pageno",pageno);
			dsRet.put("pagesize",pagesize);
			dsRet.put("totalPages",((quoteLists!=null?quoteLists.size():0) + pagesize - 1) / pagesize);
			dsRet.put("totalRows",allDate!=null?allDate.size():0);
			dsRet.put("totailOughtAmount",totailOughtAmount);
			dsRet.put("totailReceivedAmount",totailReceivedAmount);
			dsRet.put("datas",quoteLists);
			resulst.put("data", dsRet);
		}
		return R.ok(resulst);
	}

    @Override
    public R getSupplierAccountMessage(Map<String, Object> parameter ){
        //其他应付
        List<Map<String, Object>> otherDate = this.disposeOtherDate(parameter, ConstantForGYL.OTHER_PAYABLE);
        //采购合同
        List<Map<String, Object>> purchaseContractorDate = this.disposePurchaseContractorDate(parameter);
        //付款单
        List<Map<String, Object>> paymentDate= this.disposePaymentAmount(parameter, ConstantForGYL.PAYMENT_ORDER);

		//组合
		List<Map<String, Object>>  allDate= new ArrayList<>();
        //初始化数据
		Map<String,Object>  mapCommutation= new HashMap<>();
		mapCommutation.put("supplierId",parameter.get("supplierId"));
		List<Map<String, Object>> detail = this.getDetail(mapCommutation);
		mapCommutation.remove("supplierId");
		Map<String,Object>  resulst= new HashMap<>();
		if (detail.size() > 0) {
			mapCommutation.put("date", DateFormatUtil.getDateByParttern(detail.get(0).get("createTime").toString(), "yyyy-MM-dd"));
			mapCommutation.put("time", "2000-01-01 00:00:00");
			mapCommutation.put("supplierName", detail.get(0).get("supplierName"));
			mapCommutation.put("typeName", ConstantForGYL.REMANING_AMOUNT);
			mapCommutation.put("remainingAmount", detail.get(0).get("initialAmount"));
			allDate.add(mapCommutation);
		}

        if(otherDate.size()>0){
            allDate.addAll(otherDate);
        }
        if(purchaseContractorDate.size()>0){
            allDate.addAll(purchaseContractorDate);
        }
        if(paymentDate.size()>0){
            allDate.addAll(paymentDate);
        }


        //排序   依照时间排序
        if(allDate.size()>0){

            Collections.sort(allDate, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {

                    Long s0=DateFormatUtil.getDateByParttern(arg0.get("time").toString(), "yyyy-MM-dd").getTime();
                    Long s1=DateFormatUtil.getDateByParttern(arg1.get("time").toString(), "yyyy-MM-dd").getTime();
                    return s0.compareTo(s1);
                }
            });
            BigDecimal totailOughtAmount=BigDecimal.ZERO;
            BigDecimal totailReceivedAmount=BigDecimal.ZERO;
            BigDecimal remainingAmount=new BigDecimal(allDate.get(0).getOrDefault("remainingAmount","0").toString());
            for(Map<String, Object> oneDate:allDate){
                oneDate.remove("time");
                BigDecimal oughtAmount=new BigDecimal(oneDate.containsKey("oughtAmount")?oneDate.get("oughtAmount").toString():"0");
                BigDecimal receivedAmount=new BigDecimal(oneDate.containsKey("receivedAmount")?oneDate.get("receivedAmount").toString():"0");
                remainingAmount=remainingAmount.add(oughtAmount).subtract(receivedAmount);
                oneDate.put("remainingAmount",remainingAmount);
                totailOughtAmount=totailOughtAmount.add(new BigDecimal(oneDate.containsKey("oughtAmount")?oneDate.get("oughtAmount").toString():"0"));
                totailReceivedAmount=totailReceivedAmount.add(new BigDecimal(oneDate.containsKey("receivedAmount")?oneDate.get("receivedAmount").toString():"0"));
            }
            int pageno=Integer.parseInt(parameter.get("pageno").toString());
            int pagesize=Integer.parseInt(parameter.get("pagesize").toString());
            List<Map<String, Object>> quoteLists= PageUtils.startPage(allDate, pageno, pagesize);

            Map<String, Object> dsRet = new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",((quoteLists!=null?quoteLists.size():0) + pagesize - 1) / pagesize);
            dsRet.put("totalRows",allDate!=null?allDate.size():0);
            dsRet.put("totailOughtAmount",totailOughtAmount);
            dsRet.put("totailReceivedAmount",totailReceivedAmount);
            dsRet.put("datas",quoteLists);
            resulst.put("data", dsRet);
        }
        return R.ok(resulst);
    }



	private List<Map<String,Object>> disposePurchaseContractorDate(Map<String,Object > parameter){

        Map<String,Object>  map= new HashMap<>();
        map.put("supplierId",parameter.get("supplierId"));
        map.put("startTime",parameter.get("startTime"));
        map.put("endTime",parameter.get("endTime"));
        map.put("auditSign",Constant.OK_AUDITED);
        List<Map<String, Object>> saleMaps = purchasecontractService.getDetailOfHead(map);

        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.CGHT);
        List<Map<String, Object>> saleDates=new ArrayList<>();
        if(saleMaps.size()>0){
            for(Map<String, Object> saleMap:saleMaps){
                Map<String,Object>  maps= new HashMap<>();
				maps.put("date",DateFormatUtil.getDateByParttern(saleMap.get("createTime").toString(), "yyyy-MM-dd"));
				maps.put("time",saleMap.get("createTime"));
				maps.put("code",saleMap.get("contractCode"));
				maps.put("supplierName",saleMap.containsKey("supplierIdName")?saleMap.get("supplierIdName"):null);
				maps.put("typeName",dictionaryDO.getName());
				maps.put("oughtAmount",new BigDecimal(saleMap.containsKey("invoicedAmount")?saleMap.get("InvoicedAmount").toString():"0").add(new BigDecimal(saleMap.containsKey("uninvoicedAmount")?saleMap.get("uninvoicedAmount").toString():"0")));
                saleDates.add(maps);
            }
        }
        return saleDates;
    }


	private List<Map<String,Object>> disposeOtherDate(Map<String,Object > parameter ,String sign){

		Map<String,Object>  map= new HashMap<>();
		if(parameter.containsKey("supplierId")){
            map.put("clientSupplierId",parameter.get("supplierId"));
        }else{
            map.put("clientSupplierId",parameter.get("clientId"));
        }
        map.put("startTime",parameter.get("startTime"));
		map.put("endTime",parameter.get("endTime"));
		map.put("sign", sign);
		List<Map<String, Object>> otherMaps = otherReceivablesService.listForMap(map);
		long dictionaryId=0;
		if(Objects.equals(sign,ConstantForGYL.OTHER_RECIVEABLE)){
			dictionaryId=ConstantForGYL.OTHER_RECIVEABLE_TYPE;
		}else if(Objects.equals(sign,ConstantForGYL.OTHER_PAYABLE)){
			dictionaryId=ConstantForGYL.OTHER_PAYABLE_TYPE;
		}
		DictionaryDO dictionaryDO = dictionaryService.get(dictionaryId);
		List<Map<String, Object>> otherDate=new ArrayList<>();
		if(otherMaps.size()>0){
			for(Map<String, Object> otherMap:otherMaps){
				Map<String,Object>  maps= new HashMap<>();
				maps.put("date",DateFormatUtil.getDateByParttern(otherMap.get("createTime").toString(), "yyyy-MM-dd"));
				maps.put("time",otherMap.get("createTime"));
				maps.put("code",otherMap.get("code"));
				maps.put("clientName",otherMap.containsKey("clientName")?otherMap.get("clientName"):null);
                maps.put("supplierName",otherMap.containsKey("supplierName")?otherMap.get("supplierName"):null);
//				maps.put("typeName",dictionaryDO.getName());
				if(parameter.containsKey("supplierId")){
					maps.put("typeName",ConstantForGYL.OTHER_PAYABLE_NAME);
				}else{
					maps.put("typeName",ConstantForGYL.OTHER_RECIVEABLE_NAME);
				}
				maps.put("oughtAmount",otherMap.get("amount"));
				otherDate.add(maps);
			}
		}
		return otherDate;
	}

	private List<Map<String,Object>> disposeSaleContractorDate(Map<String,Object > parameter){

		Map<String,Object>  map= new HashMap<>();
		map.put("clientId",parameter.get("clientId"));
		map.put("startTime",parameter.get("startTime"));
		map.put("endTime",parameter.get("endTime"));
		map.put("auditSign",Constant.OK_AUDITED);
		List<Map<String, Object>> saleMaps = salescontractService.getDetailOfHead(map);

		DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.XSHT);
		List<Map<String, Object>> saleDates=new ArrayList<>();
		if(saleMaps.size()>0){
			for(Map<String, Object> saleMap:saleMaps){
				Map<String,Object>  maps= new HashMap<>();
				maps.put("date",DateFormatUtil.getDateByParttern(saleMap.get("createTime").toString(), "yyyy-MM-dd"));
				maps.put("time",saleMap.get("createTime"));
				maps.put("code",saleMap.get("contractCode"));
				maps.put("clientName",saleMap.get("clientName"));
				maps.put("typeName",dictionaryDO.getName());
				maps.put("oughtAmount",new BigDecimal(saleMap.containsKey("invoicedAmount")?saleMap.get("invoicedAmount").toString():"0").add(new BigDecimal(saleMap.containsKey("uninvoicedAmount")?saleMap.get("uninvoicedAmount").toString():"0")));
				saleDates.add(maps);
			}
		}
		return saleDates;
	}

	private List<Map<String,Object>> disposePaymentAmount(Map<String,Object > parameter,String sign){

		Map<String,Object>  map= new HashMap<>();
        if(parameter.containsKey("supplierId")){
            map.put("cusSupId",parameter.get("supplierId"));
        }else{
            map.put("cusSupId",parameter.get("clientId"));
        }
		map.put("startTime",parameter.get("startTime"));
		map.put("endTime",parameter.get("endTime"));
		map.put("sign",sign);
		map.put("auditSign", Constant.OK_AUDITED);
		List<Map<String, Object>>paymentMaps = paymentReceivedService.detailOfReceivedAmount(map);

		List<Map<String, Object>> paymentDates=new ArrayList<>();

		if(paymentMaps.size()>0){
			Long[] ids=new Long[paymentMaps.size()];
			for(int i=0;i<paymentMaps.size();i++){
				ids[i]=Long.parseLong(paymentMaps.get(i).get("id").toString());
			}
			Map<String,Object> paymentIdMap= new HashMap<>();
			paymentIdMap.put("paymentId",ids);
			List<PaymentReceivedItemDO> inOfPaymentAmount = paymentReceivedItemService.getInOfPaymentAmount(paymentIdMap);

			Map<Object,Object>  idAmount= new HashMap<>();
			for(Long id :ids){
				BigDecimal thisAmount=BigDecimal.ZERO;
				for(PaymentReceivedItemDO PaymentDO:inOfPaymentAmount){
					if(Objects.equals(id,PaymentDO.getPaymentReceivedId())){
						thisAmount=thisAmount.add(PaymentDO.getThisAmount());
					}
				}
				idAmount.put(id,thisAmount);
			}
			for(Map<String, Object> paymentMap:paymentMaps){
				Map<String,Object>  maps= new HashMap<>();
				maps.put("date",DateFormatUtil.getDateByParttern(paymentMap.get("createTime").toString(), "yyyy-MM-dd"));
				maps.put("time",paymentMap.get("createTime"));
				maps.put("code",paymentMap.get("prCode"));

                if(parameter.containsKey("supplierId")){
					maps.put("supplierName",paymentMap.get("cusSupName"));
					maps.put("typeName",ConstantForGYL.PAYMEN_PAIED_NAME);
				}else{
					maps.put("clientName",paymentMap.get("cusSupName"));
					maps.put("typeName",ConstantForGYL.PAYMEN_RECIVED_NAME);
				}
				maps.put("receivedAmount",idAmount.get(paymentMap.get("id")));
				paymentDates.add(maps);
			}
		}
		return paymentDates;
	}














}
