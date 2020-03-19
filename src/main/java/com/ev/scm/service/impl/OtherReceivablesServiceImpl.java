package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.OtherReceivablesDao;
import com.ev.scm.domain.OtherReceivablesDO;
import com.ev.scm.domain.OtherReceivablesItemDO;
import com.ev.scm.service.OtherReceivablesItemService;
import com.ev.scm.service.OtherReceivablesService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OtherReceivablesServiceImpl implements OtherReceivablesService {
	@Autowired
	private OtherReceivablesDao otherReceivablesDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private OtherReceivablesItemService otherReceivablesItemService;
	
	@Override
	public OtherReceivablesDO get(Long id){
		return otherReceivablesDao.get(id);
	}
	@Override
	public int countCanDelte(Map<String, Object> map) {
		return otherReceivablesDao.countCanDelte(map);
	}
	@Override
	public List<OtherReceivablesDO> list(Map<String, Object> map){
		return otherReceivablesDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return otherReceivablesDao.count(map);
	}
	
	@Override
	public int save(OtherReceivablesDO otherReceivables){
		return otherReceivablesDao.save(otherReceivables);
	}
	
	@Override
	public int update(OtherReceivablesDO otherReceivables){
		return otherReceivablesDao.update(otherReceivables);
	}

	@Override
	public int updateAll(OtherReceivablesDO otherReceivables){
		return otherReceivablesDao.updateAll(otherReceivables);
	}
	
	@Override
	public int remove(Long id){
		return otherReceivablesDao.remove(id);
	}
	@Override
	public Map<String, Object> getHeadDetail(Long id) {
		return otherReceivablesDao.getHeadDetail(id);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return otherReceivablesDao.countForMap(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return otherReceivablesDao.listForMap(map);
	}

	@Override
	public int batchRemove(Long[] ids){
		return otherReceivablesDao.batchRemove(ids);
	}

	@Override
	public R disposeAddAndChange(OtherReceivablesDO otherReceivablesDO,String  itemBodys,Long[] deleItemIds, String sign){

		if (Objects.isNull(otherReceivablesDO.getId())) {
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("maxNo", sign);
			param.put("offset", 0);
			param.put("limit", 1);
			param.put("sign", sign);
			List<OtherReceivablesDO> list = otherReceivablesDao.list(param);
			otherReceivablesDO.setCode(DateFormatUtil.getWorkOrderno(sign,list.size()>0?list.get(0).getCode():null,4));
			otherReceivablesDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			otherReceivablesDO.setSign(sign);
			int row = otherReceivablesDao.save(otherReceivablesDO);
			if (row > 0) {
				List<OtherReceivablesItemDO> bodys = JSON.parseArray(itemBodys, OtherReceivablesItemDO.class);
				for (OtherReceivablesItemDO bPdata : bodys) {
					bPdata.setReceivedId(otherReceivablesDO.getId());
					otherReceivablesItemService.save(bPdata);
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",otherReceivablesDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		} else {
			int rows = otherReceivablesDao.update(otherReceivablesDO);
			if (Objects.nonNull(deleItemIds)&&deleItemIds.length > 0){
				otherReceivablesItemService.batchRemove(deleItemIds);
			}
			if (rows > 0) {
				List<OtherReceivablesItemDO> bodys = JSON.parseArray(itemBodys, OtherReceivablesItemDO.class);
				for (OtherReceivablesItemDO bPdata : bodys) {
					if (Objects.nonNull(bPdata.getId())) {
						otherReceivablesItemService.update(bPdata);
					} else {
						bPdata.setReceivedId(otherReceivablesDO.getId());
						otherReceivablesItemService.save(bPdata);
					}
				}
				Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
				result.put("id",otherReceivablesDO.getId());
				return R.ok(result);
			} else {
				return R.error();
			}
		}
	}


	@Override
	public R disposeAudit(Long id ){
		OtherReceivablesDO otherReceivablesDO = this.get(id);
		if(Objects.nonNull(otherReceivablesDO)){
			if(Objects.equals(otherReceivablesDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}else{
				otherReceivablesDO.setAuditor(ShiroUtils.getUserId());
				otherReceivablesDO.setAuditSign(ConstantForGYL.OK_AUDITED);
				otherReceivablesDO.setAuditTime(new Date());
				this.updateAll(otherReceivablesDO);
			}
			return R.ok();
		}else{
			return R.error();
		}
	}

	@Override
	public R disposeReverseAudit(Long id ){
		OtherReceivablesDO otherReceivablesDO = this.get(id);
		if(Objects.nonNull(otherReceivablesDO)){
			if(Objects.equals(otherReceivablesDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}else{
				otherReceivablesDO.setAuditor(null);
				otherReceivablesDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
				otherReceivablesDO.setAuditTime(null);
				this.updateAll(otherReceivablesDO);
			}
			return R.ok();
		}else{
			return R.error();
		}
	}

	@Override
	public R dispoRemove(Long[] ids){
		Map<String,Object>  map= new HashMap<>();
		map.put("ids",ids);
		int watingAuditCounts=this.countCanDelte(map);
		if(Objects.equals(watingAuditCounts,ids.length)){
			this.batchRemove(ids);
			otherReceivablesItemService.batchRemoveByHeadId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}
	@Override
	public R getDetail(Long id){
		Map<String, Object> headDetail = this.getHeadDetail(id);
		List<Map<String, Object>> bodyDetail = otherReceivablesItemService.getBodyDetail(id);
		Map<String, Object> totailAmount = otherReceivablesItemService.totailAmount(id);
		Map<String,Object>  map= new HashMap<>();
		if(headDetail!=null){
			map.put("headDetail",headDetail);
			map.put("bodyDetail",bodyDetail);
			map.put("totailReceivablePayablesAmount",totailAmount.get("totailReceivablePayablesAmount"));
			map.put("totailPaidReceivedAmount",totailAmount.get("totailPaidReceivedAmount"));
			map.put("totailNoReceiptPaymentAmount",totailAmount.get("totailNoReceiptPaymentAmount"));
		}
		return R.ok(map);
	}





}