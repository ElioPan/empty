package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.dao.FundInitializationDao;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.BankTransferItemService;
import com.ev.scm.service.FundInitializationService;
import javafx.beans.binding.IntegerBinding;
import jdk.internal.util.xml.impl.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.FetchProfile;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FundInitializationServiceImpl implements FundInitializationService {
	@Autowired
	private FundInitializationDao fundInitializationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private BankTransferItemService bankTransferItemService;


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
			List<FundInitializationDO> fundInitializationDOS = JSONObject.parseArray(body, FundInitializationDO.class);
             List<FundInitializationDO> fundInitializationDos = fundInitializationDOS.stream().filter(FundInitializationDO -> FundInitializationDO.getId() != null).collect(Collectors.toList());

               if(fundInitializationDos.size()>0){
                    FundInitializationDO fundInitializationDO = this.get(fundInitializationDos.get(0).getId());
                   if(Objects.equals(1,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
                   if(Objects.equals(0,fundInitializationDO.getUsingStart())){
                       return R.error(messageSourceHandler.getMessage("scm.FundInitialization.dataIsUsing", null));
                   }
               }
                //允许修改或者新增
                   for(FundInitializationDO fundInitializationDO:fundInitializationDOS){
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
		if ( getlist.size() > 0) {
			for(Map<String, Object> oneDetail:getlist){
				Map<String,Object>  maps= new HashMap<>();
				maps.put("transferOutAcc",oneDetail.get("id"));
				maps.put("settlementType", ConstantForGYL.EXPENDITURE);
				maps.put("transferDate",endTime);
				int outAmount = bankTransferItemService.totalOutOrInAmount(maps);
				maps.clear();
				maps.put("transferInAcc",oneDetail.get("id"));
				maps.put("settlementType",ConstantForGYL.INCOM);
				maps.put("transferDate",endTime);
				int inAmount = bankTransferItemService.totalOutOrInAmount(maps);
				//初始化金额
				BigDecimal initializationAmount=oneDetail.get("initialAmount")==null?BigDecimal.ZERO:new BigDecimal(oneDetail.get("initialAmount").toString());

				oneDetail.put("remainingAmount",initializationAmount.add(new BigDecimal(inAmount)).subtract(new BigDecimal(outAmount)));
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
        map.put("transferOutAcc",founId);
        List<Map<String, Object>> outBankDetails = bankTransferItemService.getBankOutDetail(map);
        map.put("transferInAcc",founId);
        List<Map<String, Object>> inBankDetails = bankTransferItemService.getBankDetail(map);

        List<Map<String, Object>>  bankDetails= new ArrayList<>();
        if(!outBankDetails.isEmpty()){
                for(Map<String, Object> oneMap:outBankDetails){
                   String fundDate=oneMap.get("fundDate").toString();
                   Map<String,Object>  query= new HashMap<>();
                    query.put("fundDate",fundDate);
                    query.put("transferOutAcc",founId);
                    int outAmount = bankTransferItemService.totalOutOrInAmount(query);
                    query.remove("transferOutAcc");
                    query.put("transferInAcc",founId);
                    int inAmount = bankTransferItemService.totalOutOrInAmount(query);
                    BigDecimal remainingAmount=initialAmount.add(new BigDecimal(inAmount)).subtract(new BigDecimal(outAmount));
                    oneMap.put("remainingAmount",remainingAmount);
                    oneMap.put("companyName",ConstantForGYL.company_ame);
                }
         }
        if(!inBankDetails.isEmpty()){
            for(Map<String, Object> oneMap:inBankDetails){
                String fundDate=oneMap.get("fundDate").toString();
                Map<String,Object>  query= new HashMap<>();
                query.put("fundDate",fundDate);
                query.put("transferOutAcc",founId);
                int outAmount = bankTransferItemService.totalOutOrInAmount(query);
                query.remove("transferOutAcc");
                query.put("transferInAcc",founId);
                int inAmount = bankTransferItemService.totalOutOrInAmount(query);
                BigDecimal remainingAmount=initialAmount.add(new BigDecimal(inAmount)).subtract(new BigDecimal(outAmount));
                oneMap.put("remainingAmount",remainingAmount);
                oneMap.put("companyName",ConstantForGYL.company_ame);
            }
        }
        if(!inBankDetails.isEmpty()&&!outBankDetails.isEmpty()){
            if(outBankDetails.addAll(inBankDetails)){
                bankDetails= outBankDetails;
            }
        }
        if(!inBankDetails.isEmpty()&&outBankDetails.isEmpty()){
            bankDetails= inBankDetails;
        }
        if(inBankDetails.isEmpty()&&!outBankDetails.isEmpty()){
            bankDetails= outBankDetails;
        }
        Map<String,Object>  resulst= new HashMap<>();
        if(!bankDetails.isEmpty()){
            Collections.sort(bankDetails, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                    Integer s0 = Integer.valueOf(arg0.get("id").toString());
                    Integer s1 = Integer.valueOf(arg1.get("id").toString());
                    return s1.compareTo(s0);
                }
            });
            List<Map<String, Object>> quoteLists= PageUtils.startPage(bankDetails, pageno, pagesize);
            BigDecimal outTransferAmount=BigDecimal.ZERO;
            BigDecimal inTransferAmount=BigDecimal.ZERO;
            if(quoteLists!=null){
                for(Map<String, Object> oneQuoteList:quoteLists){
                    outTransferAmount= outTransferAmount.add(new BigDecimal(oneQuoteList.containsKey("outTransferAmount")?oneQuoteList.get("outTransferAmount").toString():"0")) ;
                    inTransferAmount=inTransferAmount.add(new BigDecimal(oneQuoteList.containsKey("inTransferAmount")?oneQuoteList.get("inTransferAmount").toString():"0")) ;
                }
            }
            Map<String, Object> dsRet = new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",((quoteLists!=null?quoteLists.size():0) + pagesize - 1) / pagesize);
            dsRet.put("totalRows",quoteLists!=null?quoteLists.size():0);
            dsRet.put("totalOutTransferAmount",outTransferAmount);
            dsRet.put("totalInTransferAmount",inTransferAmount);
            dsRet.put("datas",quoteLists);
            resulst.put("data", dsRet);
        }
        return R.ok(resulst);
    }





}
