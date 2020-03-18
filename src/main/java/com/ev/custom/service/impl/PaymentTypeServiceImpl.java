package com.ev.custom.service.impl;

import com.ev.custom.dao.PaymentTypeDao;
import com.ev.custom.domain.PaymentTypeDO;
import com.ev.custom.service.PaymentTypeService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {
	@Autowired
	private PaymentTypeDao paymentTypeDao;
	
	@Override
	public PaymentTypeDO get(Long id){
		return paymentTypeDao.get(id);
	}
	
	@Override
	public List<PaymentTypeDO> list(Map<String, Object> map){
		return paymentTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentTypeDao.count(map);
	}
	
	@Override
	public int save(PaymentTypeDO paymentType){
		return paymentTypeDao.save(paymentType);
	}
	
	@Override
	public int update(PaymentTypeDO paymentType){
		return paymentTypeDao.update(paymentType);
	}

	@Override
	public int updateAll(PaymentTypeDO paymentType){
		return paymentTypeDao.updateAll(paymentType);
	}
	
	@Override
	public int remove(Long id){
		return paymentTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paymentTypeDao.batchRemove(ids);
	}

	@Override
	public R disposeAddAndChage(PaymentTypeDO paymentTypeDO ){
		Map<String,Object>  maps= new HashMap<>();
		if(Objects.nonNull(paymentTypeDO.getId())){
			this.update(paymentTypeDO);
			maps.put("id",paymentTypeDO.getId());
			return R.ok(maps);
		}else{
			Map<String,Object> map = Maps.newHashMap();
			map.put("maxNo", Constant.SZRX);
			map.put("offset", 0);
			map.put("limit", 1);
			List<PaymentTypeDO> list = paymentTypeDao.list(map);
			paymentTypeDO.setCode(DateFormatUtil.getWorkOrderno(Constant.SZRX,list.size()>0?list.get(0).getCode():null,4));
			paymentTypeDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			this.save(paymentTypeDO);
			maps.put("id",paymentTypeDO.getId());
			return R.ok(maps);
		}
	}

	@Override
	public List<Map<String, Object>> listOfMap(Map<String, Object> map) {
		return paymentTypeDao.listOfMap(map);
	}


}
