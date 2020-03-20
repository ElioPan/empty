package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.scm.dao.CommutationInitializationDao;
import com.ev.scm.domain.CommutationInitializationDO;
import com.ev.scm.domain.PaymentReceivedItemDO;
import com.ev.scm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


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
		if(otherDate.size()>0){
			allDate.addAll(otherDate);
		}
		if(saleContractorDate.size()>0){
			allDate.addAll(otherDate);
		}
		if(paymentDate.size()>0){
			allDate.addAll(otherDate);
		}

		Map<String,Object>  resulst= new HashMap<>();
		//排序   依照时间排序
		if(allDate.size()>0){
			Map<String,Object>  mapCommutation= new HashMap<>();
			mapCommutation.put("clientId",parameter.get("clientId"));

			List<Map<String, Object>> detail = this.getDetail(mapCommutation);
			mapCommutation.remove("clientId");
			if (detail.size() > 0) {
				mapCommutation.put("date", detail.get(0).get("createTime"));
				mapCommutation.put("time", "2000-01-01 00:00:00");
				mapCommutation.put("clientName", detail.get(0).get("clientName"));
				mapCommutation.put("typeName", ConstantForGYL.REMANING_AMOUNT);
				mapCommutation.put("remainingAmount", detail.get(0).get("initialAmount"));
			}

			allDate.add(mapCommutation);
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
			for(Map<String, Object> oneDate:allDate){
				oneDate.remove("time");
				totailOughtAmount=totailOughtAmount.add(new BigDecimal(oneDate.containsKey("oughtAmount")?oneDate.get("oughtAmount").toString():"0"));
				totailReceivedAmount=totailOughtAmount.add(new BigDecimal(oneDate.containsKey("receivedAmount")?oneDate.get("receivedAmount").toString():"0"));
			}

			int pageno=Integer.parseInt(parameter.get("pageno").toString());
			int pagesize=Integer.parseInt(parameter.get("pagesize").toString());
			List<Map<String, Object>> quoteLists= PageUtils.startPage(allDate, pageno, pagesize);

			Map<String, Object> dsRet = new HashMap<>();
			dsRet.put("pageno",pageno);
			dsRet.put("pagesize",pagesize);
			dsRet.put("totalPages",((quoteLists!=null?quoteLists.size():0) + pagesize - 1) / pagesize);
			dsRet.put("totalRows",quoteLists!=null?quoteLists.size():0);
			dsRet.put("totailOughtAmount",totailOughtAmount);
			dsRet.put("totailReceivedAmount",totailReceivedAmount);
			dsRet.put("datas",quoteLists);
			resulst.put("data", dsRet);
		}
		return R.ok(resulst);
	}


	private List<Map<String,Object>> disposeOtherDate(Map<String,Object > parameter ,String sign){

		Map<String,Object>  map= new HashMap<>();
		map.put("clientId",parameter.get("clientId"));
		map.put("startTime",parameter.get("startTime"));
		map.put("endTime",parameter.get("endTime"));
		map.put("sign", sign);
		List<Map<String, Object>> otherMaps = otherReceivablesService.listForMap(map);
		int dictionaryId=0;
		if(Objects.equals(sign,ConstantForGYL.OTHER_RECIVEABLE)){
			dictionaryId=ConstantForGYL.OTHER_RECIVEABLE_TYPE.intValue();
		}else if(Objects.equals(sign,ConstantForGYL.OTHER_PAYABLE)){
			dictionaryId=ConstantForGYL.OTHER_PAYABLE_TYPE.intValue();
		}
		DictionaryDO dictionaryDO = dictionaryService.get(dictionaryId);
		List<Map<String, Object>> otherDate=new ArrayList<>();
		if(otherMaps.size()>0){
			for(Map<String, Object> otherMap:otherMaps){
				Map<String,Object>  maps= new HashMap<>();
//				maps.clear();
				maps.put("date",DateFormatUtil.getDateByParttern(otherMap.get("createTime").toString(), "yyyy-MM-dd"));
				maps.put("time",otherMap.get("createTime"));
				maps.put("code",otherMap.get("code"));
				maps.put("clientName",otherMap.get("clientName"));
				maps.put("typeName",dictionaryDO.getName());
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
		map.put("auditSign",ConstantForGYL.OK_AUDITED);
		List<Map<String, Object>> saleMaps = salescontractService.getDetailOfHead(map);

		DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.XSHT.intValue());
		List<Map<String, Object>> saleDates=new ArrayList<>();
		if(saleMaps.size()>0){
			for(Map<String, Object> saleMap:saleMaps){
				map.clear();
				map.put("date",DateFormatUtil.getDateByParttern(saleMap.get("createTime").toString(), "yyyy-MM-dd"));
				map.put("time",saleMap.get("createTime"));
				map.put("code",saleMap.get("contractCode"));
				map.put("clientName",saleMap.get("clientName"));
				map.put("typeName",dictionaryDO.getName());
				map.put("oughtAmount",new BigDecimal(saleMap.containsKey("invoicedAmount")?saleMap.get("invoicedAmount").toString():"0").add(new BigDecimal(saleMap.containsKey("uninvoicedAmount")?saleMap.get("uninvoicedAmount").toString():"0")));

				saleDates.add(map);
			}
		}
		return saleDates;
	}

	private List<Map<String,Object>> disposePaymentAmount(Map<String,Object > parameter,String sign){

		Map<String,Object>  map= new HashMap<>();
		map.put("cusSupId",parameter.get("clientId"));
		map.put("startTime",parameter.get("startTime"));
		map.put("endTime",parameter.get("endTime"));
		map.put("sign",sign);
		map.put("auditSign",ConstantForGYL.OK_AUDITED);
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
				map.clear();
				map.put("date",DateFormatUtil.getDateByParttern(paymentMap.get("createTime").toString(), "yyyy-MM-dd"));
				map.put("time",paymentMap.get("createTime"));
				map.put("code",paymentMap.get("prCode"));
				map.put("clientName",paymentMap.get("cusSupName"));
				map.put("typeName",paymentMap.get("prTypeName"));
				map.put("receivedAmount",idAmount.get(paymentMap.get("id")));
				paymentDates.add(map);
			}
		}
		return paymentDates;
	}















}
