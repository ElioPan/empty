package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.FundInitializationDao;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.service.BankTransferItemService;
import com.ev.scm.service.FundInitializationService;
import com.ev.scm.service.PaymentReceivedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class FundInitializationServiceImpl implements FundInitializationService {
	@Autowired
	private FundInitializationDao fundInitializationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private BankTransferItemService bankTransferItemService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private PaymentReceivedItemService paymentReceivedItemService;


	@Override
	public FundInitializationDO get(Long id){
		return fundInitializationDao.get(id);
	}
	
	@Override
	public List<FundInitializationDO> list(Map<String, Object> map){
		return fundInitializationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return fundInitializationDao.count(map);
	}
	
	@Override
	public int save(FundInitializationDO fundInitialization){
		return fundInitializationDao.save(fundInitialization);
	}
	
	@Override
	public int update(FundInitializationDO fundInitialization){
		return fundInitializationDao.update(fundInitialization);
	}

	@Override
	public int updateAll(FundInitializationDO fundInitialization){
		return fundInitializationDao.updateAll(fundInitialization);
	}
	
	@Override
	public int remove(Integer id){
		return fundInitializationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return fundInitializationDao.batchRemove(ids);
	}



	@Override
	public R disposeAddAndChage(String body ){

		if(!StringUtils.isEmpty(body)){
			List<FundInitializationDO> fundInitializationDoS = JSONObject.parseArray(body, FundInitializationDO.class);
            List<FundInitializationDO> fundInitializationHaveId = fundInitializationDoS.stream().filter(FundInitializationDO -> FundInitializationDO.getId() != null).collect(Collectors.toList());
            List<FundInitializationDO> fundInitializationHaveNoId = fundInitializationDoS.stream().filter(FundInitializationDO -> FundInitializationDO.getId() == null).collect(Collectors.toList());

               if(fundInitializationHaveId.size()>0){
                    FundInitializationDO fundInitializationDO = this.get(fundInitializationHaveId.get(0).getId());
                   if(Objects.equals(1,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
                   if(Objects.equals(0,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
               }
               //校验账号是否重复
            for(FundInitializationDO fundInitializationDO:fundInitializationHaveNoId){
                   Map<String,Object>  map= new HashMap<>();
                   map.put("accountNumber",fundInitializationDO.getAccountNumber());
                List<FundInitializationDO> list = this.list(map);
                if(list.size()>0){
                    String[] msg={fundInitializationDO.getAccountNumber().toString()};
                    return R.error(messageSourceHandler.getMessage("scm.fundInitialization.alreadyExist", msg));
                }
            }
              //允许修改或者新增
                   for(FundInitializationDO fundInitializationDO:fundInitializationDoS){
                       if(Objects.isNull(fundInitializationDO.getId())){
                        //新增
                           this.save(fundInitializationDO);
                       }else{
                           //更新
                           this.update(fundInitializationDO);
                       }
                   }
                   return  R.ok();
		}else{
			return R.error();
		}
	}

    @Override
    public R disposeStartUsing(Long[] ids){
        Map<String,Object>  map= new HashMap<>();
        map.put("ids",ids);
        map.put("usingStart",0);
        fundInitializationDao.updateUsingStart(map);
        return R.ok();
    }


    @Override
    public R disposeForbidden(Long[] ids){
        Map<String,Object>  map= new HashMap<>();
        map.put("ids",ids);
        map.put("usingStart",1);
        fundInitializationDao.updateUsingStart(map);
        return R.ok();
    }

    @Override
    public Map<String, Object> countOfList(Map<String, Object> map) {
        return fundInitializationDao.countOfList(map);
    }

    @Override
    public List<Map<String, Object>> getlist(Map<String, Object> map) {
        return fundInitializationDao.getlist(map);
    }


	@Override
	public R disposeFundBalance( int pageno,int pagesize,Map<String, Object> map, String  endTime){

		List<Map<String, Object>> getlist = this.getlist(map);
		Map<String, Object> countOfList = this.countOfList(map);
        Map<String,Object>  params= new HashMap<>();
		Map<String,Object>  mapId= new HashMap<>();
        if ( getlist.size() > 0) {

            for(Map<String, Object> oneDetail:getlist){
                String id=oneDetail.get("id").toString();
                if(mapId.containsKey("id")){
                    mapId.put(id,id);
                    continue;
                }
                mapId.put(id,id);
            }
            Set<String> strId = mapId.keySet();
            Long[] ids= new Long[mapId.size()];
            int i=0;
            for(String ss :strId){
                ids[i]= Long.parseLong(ss);
                i+=1;
            }

            //付款
            Map<String,Object>  query= new HashMap<>();
            query.put("sign", ConstantForGYL.PAYMENT_ORDER);
            query.put("auditSign",ConstantForGYL.OK_AUDITED);
            query.put("accountNumber",ids);
            query.put("endTime", map.get("endTime"));
            List<Map<String, Object>> outMaps= paymentReceivedItemService.getInCountById(query);
            //收款
            query.put("sign", ConstantForGYL.ALL_BILL);
            List<Map<String, Object>> inMaps=paymentReceivedItemService.getInCountById(query);

			for(Map<String, Object> oneDetail:getlist){
				Map<String,Object>  maps= new HashMap<>();
				maps.put("transferOutAcc",oneDetail.get("id"));
//				maps.put("settlementType", ConstantForGYL.EXPENDITURE);
				maps.put("transferDate",endTime);
				int outAmount = bankTransferItemService.totalOutOrInAmount(maps);
				maps.clear();
				maps.put("transferInAcc",oneDetail.get("id"));
//				maps.put("settlementType",ConstantForGYL.INCOM);
				maps.put("transferDate",endTime);
				int inAmount = bankTransferItemService.totalOutOrInAmount(maps);
				//初始化金额
				BigDecimal initializationAmount=oneDetail.get("initialAmount")==null?BigDecimal.ZERO:new BigDecimal(oneDetail.get("initialAmount").toString());

                BigDecimal totailOutAmount=BigDecimal.ZERO;
                if(outMaps.size()>0){
                        for(Map<String, Object> outMap:outMaps){
                            if(Objects.equals(Long.parseLong(oneDetail.get("id").toString()),Long.parseLong(outMap.get("accountNumber").toString()))){
                                totailOutAmount=totailOutAmount.add(new BigDecimal(outMap.get("thisAmount").toString()));
                            }
                        }
                }
                BigDecimal totailInAmount=BigDecimal.ZERO;
                if(inMaps.size()>0){
                        for(Map<String, Object> inMap:inMaps){
                            if(Objects.equals(Long.parseLong(oneDetail.get("id").toString()),Long.parseLong(inMap.get("accountNumber").toString()))){
                                totailInAmount=totailInAmount.add(new BigDecimal(inMap.get("thisAmount").toString()));
                            }
                        }
                }
				oneDetail.put("remainingAmount",initializationAmount.add(new BigDecimal(inAmount)).add(totailInAmount).subtract(totailOutAmount).subtract(new BigDecimal(outAmount)));
				oneDetail.put("companyName",ConstantForGYL.company_ame);
			}
            int total= Integer.parseInt(countOfList.get("totailCount").toString());
			params.put("total",total);
			params.put("totailInitialAmount",countOfList.get("totailInitialAmount"));
			params.put("data", new DsResultResponse(pageno,pagesize,total,getlist));
		}
		return R.ok(params);
	}


    @Override
    public R disposeFinancialDetails(int pageno,int pagesize,Map<String, Object> map,Long founId){

        FundInitializationDO fundInitializationDO = this.get(founId);
        BigDecimal initialAmount=fundInitializationDO.getInitialAmount();

        List<Map<String, Object>>  balanceDetails= new ArrayList<>();
        Map<String,Object>  initiaData= new HashMap<>();
        if(fundInitializationDO!=null){
            initiaData.put("id",0);
            initiaData.put("fundDate","2000-01-01 00:00:00");
            initiaData.put("createTime","2000-01-01 00:00:00");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            initiaData.put("transferDate",formatter.format(fundInitializationDO.getPeriod()));
            initiaData.put("businessTypeName","期初余额");
            initiaData.put("remainingAmount",fundInitializationDO.getInitialAmount());
            initiaData.put("companyName",ConstantForGYL.company_ame);
            initiaData.put("transferAccName",fundInitializationDO.getAccountNumber());
            DictionaryDO dictionaryDO = dictionaryService.get(fundInitializationDO.getBank().intValue());
            if(dictionaryDO!=null){initiaData.put("backName",dictionaryDO.getName());}
            balanceDetails.add(initiaData);
        }
        //收支
        map.put("transferOutAcc",founId);
        List<Map<String, Object>> outBankDetails = bankTransferItemService.getBankOutDetail(map);
        map.put("transferInAcc",founId);
        List<Map<String, Object>> inBankDetails = bankTransferItemService.getBankDetail(map);

        //收款  付款
        Map<String,Object>  query= new HashMap<>();
        query.put("accountNumber",founId);
        query.put("auditSign",ConstantForGYL.OK_AUDITED);
        query.put("sign",ConstantForGYL.ALL_BILL);
        query.put("startTime",map.get("startTime"));
        query.put("endTime",map.get("endTime"));
        List<Map<String, Object>> inDetails = paymentReceivedItemService.getsPaymentDetails(query);
        query.put("sign",ConstantForGYL.PAYMENT_ORDER);
        List<Map<String, Object>> outDetails = paymentReceivedItemService.getsPaymentDetails(query);

        List<Map<String, Object>> payInDetails = this.disposeInAmount(inDetails);
        List<Map<String, Object>> payOutDetails = this.disposeOutAmount(outDetails);
        List<Map<String, Object>> bankInDetails = this.disposeInAmount(inBankDetails);
        List<Map<String, Object>> bankOutDetails = this.disposeOutAmount(outBankDetails);

        if(payInDetails.size()>0){
            balanceDetails.addAll(payInDetails);
        }
        if(payOutDetails.size()>0){
            balanceDetails.addAll(payOutDetails);
        }
        if(bankInDetails.size()>0){
            balanceDetails.addAll(bankInDetails);
        }
        if(bankOutDetails.size()>0){
            balanceDetails.addAll(bankOutDetails);
        }
        Map<String,Object>  resulst= new HashMap<>();
        if(balanceDetails.size()>0){

//            List<Map<String, Object>>  balanceDetail =balanceDetails.stream().sorted((v1,v2)-> (DateFormatUtil.getDateByParttern(v1.get("fundDate").toString(), "yyyy-MM-dd").getTime())
//                        >(DateFormatUtil.getDateByParttern(v2.get("fundDate").toString(), "yyyy-MM-dd").getTime())?1:-1).sorted((v1,v2)->
//                    (DateFormatUtil.getDateByParttern(v1.get("createTime").toString(), "yyyy-MM-dd HH:mm:ss").getTime()) >(DateFormatUtil.getDateByParttern(v2.get("createTime").toString(), "yyyy-MM-dd HH:mm:ss").getTime())?1:-1).collect(Collectors.toList());

//            Collections.sort(balanceDetails, new Comparator<Map<String, Object>>() {
//                @Override
//                public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
//                    Long s0= DateFormatUtil.getDateByParttern(arg0.get("fundDate").toString(), "yyyy-MM-dd").getTime();
//                    Long s1=DateFormatUtil.getDateByParttern(arg1.get("fundDate").toString(), "yyyy-MM-dd").getTime();
//                    return s0.compareTo(s1);
//                }
//            });

            List<Map<String, Object>>  balanceDetail =balanceDetails.stream()
                    .sorted((v1,v2)->v1.get("fundDate").toString().compareTo(v2.get("fundDate").toString()))
                    .sorted((v1,v2)->v1.get("createTime").toString().compareTo(v2.get("createTime").toString())).collect(Collectors.toList());

            List<Map<String, Object>> quoteLists= PageUtils.startPage(balanceDetail, pageno, pagesize);
            BigDecimal totalOutTransferAmount=BigDecimal.ZERO;
            BigDecimal totalInTransferAmount=BigDecimal.ZERO;
            if(quoteLists!=null){
                for(Map<String, Object> oneQuoteList:quoteLists){
                    oneQuoteList.remove("fundDate");
                    oneQuoteList.put("companyName",ConstantForGYL.company_ame);
                    initialAmount=initialAmount.add(new BigDecimal(oneQuoteList.containsKey("inTransferAmount")?oneQuoteList.get("inTransferAmount").toString():"0")).subtract(new BigDecimal(oneQuoteList.containsKey("outTransferAmount")?oneQuoteList.get("outTransferAmount").toString():"0"));
                    oneQuoteList.put("remainingAmount",initialAmount);
                    totalOutTransferAmount= totalOutTransferAmount.add(new BigDecimal(oneQuoteList.containsKey("outTransferAmount")?oneQuoteList.get("outTransferAmount").toString():"0")) ;
                    totalInTransferAmount=totalInTransferAmount.add(new BigDecimal(oneQuoteList.containsKey("inTransferAmount")?oneQuoteList.get("inTransferAmount").toString():"0")) ;
                }
            }
            Map<String, Object> dsRet = new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",((quoteLists!=null?quoteLists.size():0) + pagesize - 1) / pagesize);
            dsRet.put("totalRows",balanceDetail!=null?balanceDetail.size():0);
            dsRet.put("totalOutTransferAmount",totalOutTransferAmount);
            dsRet.put("totalInTransferAmount",totalInTransferAmount);
            dsRet.put("datas",quoteLists);
            resulst.put("data", dsRet);
        }
        return R.ok(resulst);
    }


    private   List<Map<String, Object>> disposeInAmount(List<Map<String, Object>> inDetails){
        List<Map<String, Object>> inMapDetails=new ArrayList<>();
        if(inDetails.size()>0){
            Map<String,BigDecimal>  mapIdsAmount= new HashMap<>();
            for(Map<String, Object> maps:inDetails){
                String signId=maps.get("signId").toString();
                if(mapIdsAmount.containsKey(signId)){
                    mapIdsAmount.put(signId,mapIdsAmount.get(signId).add(new BigDecimal(maps.get("inTransferAmount").toString())));
                   continue;
                }
                mapIdsAmount.put(signId,new BigDecimal(maps.get("inTransferAmount").toString()));
            }
                Set<String>  ids=mapIdsAmount.keySet();
                for(String str:ids){
                    for(Map<String, Object> strMap:inDetails){
                        if(Objects.equals(strMap.get("signId").toString(),str)){
                            strMap.put("inTransferAmount",mapIdsAmount.get(str));
                            inMapDetails.add(strMap);
                            break;
                        }
                    }
                }
        }
        return  inMapDetails;
    }

    private   List<Map<String, Object>> disposeOutAmount(List<Map<String, Object>> outDetails){
        List<Map<String, Object>> outMapDetails=new ArrayList<>();
        if(!outDetails.isEmpty()){
            Map<String,BigDecimal>  mapIdsAmount= new HashMap<>();
            for(Map<String, Object> maps:outDetails){
                String signId=maps.get("signId").toString();
                if(mapIdsAmount.containsKey(signId)){
                    mapIdsAmount.put(signId,mapIdsAmount.get(signId).add(new BigDecimal(maps.get("outTransferAmount").toString())));
                    continue;
                }
                mapIdsAmount.put(signId,new BigDecimal(maps.get("outTransferAmount").toString()));
            }
                Set<String>  ids=mapIdsAmount.keySet();
                for(String str:ids){
                    for(Map<String, Object> strMap:outDetails){
                        if(Objects.equals(strMap.get("signId").toString(),str)){
                            strMap.put("outTransferAmount",mapIdsAmount.get(str));
                            outMapDetails.add(strMap);
                            break;
                        }
                    }
                }
        }
        return  outMapDetails;
    }



}
