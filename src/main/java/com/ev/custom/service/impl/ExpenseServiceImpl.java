package com.ev.custom.service.impl;

import com.ev.custom.dao.ExpenseDao;
import com.ev.custom.domain.ExpenseDO;
import com.ev.custom.service.ExpenseService;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class ExpenseServiceImpl implements ExpenseService {
	@Autowired
	private ExpenseDao expenseDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public ExpenseDO get(Long id){
		return expenseDao.get(id);
	}
	
	@Override
	public List<ExpenseDO> list(Map<String, Object> map){
		return expenseDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return expenseDao.count(map);
	}
	
	@Override
	public int save(ExpenseDO expense){
		return expenseDao.save(expense);
	}
	
	@Override
	public int update(ExpenseDO expense){
		return expenseDao.update(expense);
	}
	
	@Override
	public int remove(Long id){
		return expenseDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return expenseDao.batchRemove(ids);
	}


	@Override
	public R add(ExpenseDO ExpenseDO){

		if (Objects.isNull(ExpenseDO.getId())) {
//			String prefix = DateFormatUtil.getWorkOrderno(Constant.FY, new Date());
//			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
//			params.put("maxNo", prefix);
//			params.put("offset", 0);
//			params.put("limit", 1);
//			List<ExpenseDO> list = expenseDao.list(params);
//			String suffix = null;
//			if (list.size() > 0) {
//				suffix = list.get(0).getCode();
//			}

			Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
			param.put("offset", 0);
			param.put("limit", 1);
			List<ExpenseDO> list = expenseDao.list(param);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getCode();
			}
			String code= DateFormatUtil.getWorkOrderno(Constant.FY, suffix,4);

			ExpenseDO.setCode(code);
			ExpenseDO.setAuditSign(Constant.WAIT_AUDIT);
			int row = expenseDao.save(ExpenseDO);
			if (row > 0) {
				return R.ok();
			} else {
				return R.error();
			}
		} else {
			int rows = expenseDao.update(ExpenseDO);
			if (rows > 0) {
				return R.ok();
			} else {
				return R.error();
			}
		}
	}

	@Override
	public R audit(Long id) {
		ExpenseDO expenseDO = expenseDao.get(id);
		if(Objects.nonNull(expenseDO)){
			if(Objects.equals(expenseDO.getAuditSign(),Constant.WAIT_AUDIT)){
				ExpenseDO pDo=new ExpenseDO();
				pDo.setAuditSign(Constant.OK_AUDITED);
				pDo.setAuditor(ShiroUtils.getUserId());
				pDo.setId(id);
				expenseDao.update(pDo);
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
		ExpenseDO expenseDO= expenseDao.get(id);

		if(Objects.nonNull(expenseDO)){
			if(Objects.equals(expenseDO.getAuditSign(), Constant.OK_AUDITED)){
				ExpenseDO pDo=new ExpenseDO();
				pDo.setAuditSign(Constant.WAIT_AUDIT);
				pDo.setAuditor(0L);
				pDo.setId(id);
				expenseDao.update(pDo);
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

		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",ids);
		int rows = expenseDao.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			expenseDao.deletOfExpense(map);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return expenseDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return expenseDao.countForMap(map);
	}


	@Override
	public R getDetail(Long id) {
		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",id);
		List<Map<String, Object>> maps = expenseDao.listForMap(map);
		map.clear();
		if(!maps.isEmpty()){
			map.put("data",maps.get(0));
		}
		return R.ok(map);
	}

}
