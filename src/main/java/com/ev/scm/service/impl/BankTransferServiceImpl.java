package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.BankTransferDao;
import com.ev.scm.domain.BankTransferDO;
import com.ev.scm.domain.BankTransferItemDO;
import com.ev.scm.domain.FundInitializationDO;
import com.ev.scm.service.BankTransferItemService;
import com.ev.scm.service.BankTransferService;
import com.ev.scm.service.FundInitializationService;
import com.google.common.collect.Maps;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.metadata.ManagedOperation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class BankTransferServiceImpl implements BankTransferService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private BankTransferDao bankTransferDao;
	@Autowired
	private BankTransferItemService bankTransferItemService;
	@Autowired
	private FundInitializationService fundInitializationService;

	
	@Override
	public BankTransferDO get(Long id){
		return bankTransferDao.get(id);
	}
	
	@Override
	public List<BankTransferDO> list(Map<String, Object> map){
		return bankTransferDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bankTransferDao.count(map);
	}
	
	@Override
	public int save(BankTransferDO bankTransfer){
		return bankTransferDao.save(bankTransfer);
	}
	
	@Override
	public int update(BankTransferDO bankTransfer){
		return bankTransferDao.update(bankTransfer);
	}
	
	@Override
	public int remove(Long id){
		return bankTransferDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return bankTransferDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return bankTransferDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return bankTransferDao.countForMap(map);
	}

	@Override
	public R addBankTransfer(BankTransferDO bankTransferDO,String transferBodys, Long[] deleItemIds){

		if (Objects.isNull(bankTransferDO.getId())) {

			String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.BANK_TRANSFER);
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("maxNo", maxNo);
			param.put("offset", 0);
			param.put("limit", 1);
			List<BankTransferDO> list = bankTransferDao.list(param);
			String taskNo = null;
			if (!list.isEmpty()) {
				taskNo = list.get(0).getTransferCode();
			}
			bankTransferDO.setTransferCode(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
			bankTransferDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			int row = bankTransferDao.save(bankTransferDO);

			if (row > 0) {
				List<BankTransferItemDO> bodys = JSON.parseArray(transferBodys, BankTransferItemDO.class);
				//验证是否禁用
				String forbiddenResult = this.checkForbidden(bodys);
				if(!Objects.equals("ok",forbiddenResult)){
					return R.error(forbiddenResult);
				}
				//验证是否超支
				String overspendResult = this.checkOverspend(bodys,bankTransferDO.getId());
				if(!Objects.equals("ok",overspendResult)){
					return R.error(overspendResult);
				}

				for (BankTransferItemDO bPdata : bodys) {
					bPdata.setTransferId(bankTransferDO.getId());
					bankTransferItemService.save(bPdata);
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",bankTransferDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		} else {
			int rows = bankTransferDao.update(bankTransferDO);
			if (Objects.nonNull(deleItemIds)&&deleItemIds.length > 0){
				bankTransferItemService.batchRemove(deleItemIds);
			}
			if (rows > 0) {
				List<BankTransferItemDO> bodys = JSON.parseArray(transferBodys, BankTransferItemDO.class);
				for (BankTransferItemDO bPdata : bodys) {
					if (Objects.nonNull(bPdata.getId())) {
						bankTransferItemService.update(bPdata);
					} else {
						bPdata.setTransferId(bankTransferDO.getId());
						bankTransferItemService.save(bPdata);
					}
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",bankTransferDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		}
	}

	public String checkForbidden(List<BankTransferItemDO> bodys){
		for (BankTransferItemDO bPdata : bodys) {
			Long fundInitializationId=0L;
			if(bPdata.getTransferInAcc()!=null){
				fundInitializationId=bPdata.getTransferInAcc();
			}else if(bPdata.getTransferOutAcc()!=null){
				fundInitializationId=bPdata.getTransferOutAcc();
			}
			FundInitializationDO fundInitializationDO = fundInitializationService.get(fundInitializationId);
			if(fundInitializationDO!=null){
				if(Objects.equals(1,fundInitializationDO.getUsingStart())){
					String[] arg={fundInitializationDO.getAccountNumber().toString()};
					return messageSourceHandler.getMessage("scm.FundInitialization.forbidden",arg);
				}
			}else{
				return messageSourceHandler.getMessage("scm.FundInitialization.TheOriginalAccountDoesNotExist",null);
			}
		}
			return "ok";
	}

	@Override
	public String checkOverspend(List<BankTransferItemDO> bodys,Long transferId){
        List<BankTransferItemDO> bodysSettlementType = bodys.stream().filter(BankTransferItemDO -> Objects.nonNull(BankTransferItemDO.getTransferOutAcc()) ).collect(Collectors.toList());
		if(bodysSettlementType.size()>0){
			for (BankTransferItemDO bPdata : bodysSettlementType) {
				Long fundInitializationId=bPdata.getTransferOutAcc();
                FundInitializationDO fundInitializationDO = fundInitializationService.get(fundInitializationId);
                if(Objects.isNull(fundInitializationDO)){
                    return messageSourceHandler.getMessage("scm.FundInitialization.TheOriginalAccountDoesNotExist",null);
                }
                //本次某账号总支出
                BigDecimal transferOutAmount=BigDecimal.ZERO;
                for (BankTransferItemDO bDo : bodysSettlementType) {
                    if(Objects.equals(bDo.getTransferOutAcc(),fundInitializationId)){
                        transferOutAmount=transferOutAmount.add(bDo.getTransferAmount());
                    }
                }
				//  总收入   总支出
				Map<String,Object>  map= new HashMap<>();
				map.put("transferOutAcc",fundInitializationId);
//				map.put("settlementType",ConstantForGYL.EXPENDITURE);
				if(transferId!=null){map.put("transferId",transferId);}
                int outAmount = bankTransferItemService.totalOutOrInAmount(map);
                map.clear();
                map.put("transferInAcc",fundInitializationId);
//                map.put("settlementType",ConstantForGYL.INCOM);
                if(transferId!=null){map.put("transferId",transferId);}
                int inAmount = bankTransferItemService.totalOutOrInAmount(map);
                //初始化金额
                BigDecimal initializationAmount=fundInitializationDO.getInitialAmount()==null?BigDecimal.ZERO:fundInitializationDO.getInitialAmount();
                int results =(initializationAmount.add(new BigDecimal(inAmount))).compareTo(transferOutAmount.add(new BigDecimal(outAmount)));
                if(Objects.equals(-1,results)){
                    String[] arg={fundInitializationDO.getAccountNumber(),(initializationAmount.add(new BigDecimal(inAmount)).subtract(new BigDecimal(outAmount))).toPlainString(),transferOutAmount.toPlainString(),};
                    return  messageSourceHandler.getMessage("scm.FundInitialization.BeyondTheBalanceOfPayments",arg);
                }
			}
		}
		return "ok";
	}

	@Override
	public R audit(Long id) {
		BankTransferDO bankTransferDO = this.get(id);
		if(Objects.nonNull(bankTransferDO)){
			if(Objects.equals(bankTransferDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
				BankTransferDO btDo=new BankTransferDO();
				btDo.setAuditSign(ConstantForGYL.OK_AUDITED);
				btDo.setAuditor(ShiroUtils.getUserId());
				btDo.setAuditTime(new Date());
				btDo.setId(id);
				this.update(btDo);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}

	}

	@Override
	public R rollBackAudit(Long id) {
		BankTransferDO bankTransferDO = this.get(id);
		if(Objects.nonNull(bankTransferDO)){
			if(Objects.equals(bankTransferDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
				BankTransferDO btDo=new BankTransferDO();
				btDo.setAuditSign(ConstantForGYL.WAIT_AUDIT);
				btDo.setAuditor(0L);
				btDo.setId(id);
				this.update(btDo);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public R removeTransfer(Long[] ids) {

		Map<String,Object>  map= new HashMap<>();
		map.put("id",ids);
		int rows = bankTransferDao.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			bankTransferDao.batchRemove(ids);
			bankTransferItemService.removeByTransferId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}


	@Override
	public R getdetail(Long id) {
		Map<String,Object>  map= new HashMap<>();
		map.put("id",id);
		Map<String,Object> transferDetail = bankTransferDao.detailOfTransfer(map);
		List<Map<String, Object>> detailOfItem = bankTransferItemService.detailOfItem(map);
		int totalAmount=bankTransferItemService.totalAmount(map);
		Map<String,Object>  result= new HashMap<>();
		map.clear();
		if(Objects.nonNull(transferDetail)) {
			map.put("transferDetail",transferDetail);
			map.put("detailOfItem",detailOfItem);
			//子表总金额
			map.put("totalAmount",totalAmount);
			result.put("data",map);
		}
		return R.ok(result);
	}




}
