package com.ev.custom.service.impl;

import com.ev.framework.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.custom.dao.PaymentformDao;
import com.ev.custom.dao.PaymentformItemDao;
import com.ev.custom.domain.PaymentformDO;
import com.ev.custom.domain.PaymentformItemDO;
import com.ev.custom.service.PaymentformService;
import com.google.common.collect.Maps;

@Service
public class PaymentformServiceImpl implements PaymentformService {
	@Autowired
	private PaymentformDao paymentformDao;
	
	@Autowired
	private PaymentformItemDao paymentformItemDao;

	@Override
	public R addPaymentform(PaymentformDO paymentform, String bodyItem) {
		paymentform.setAuditStatus(Constant.WAIT_AUDIT);
		String code = this.paymentformCode();
		paymentform.setPayNum(code);
		paymentformDao.save(paymentform);
		Long id = paymentform.getId();
		List<PaymentformItemDO> bodyIs = JSON.parseArray(bodyItem, PaymentformItemDO.class);
		for(PaymentformItemDO pdata : bodyIs){
			pdata.setPaymentformId(id);
			paymentformItemDao.save(pdata);
		}
		return R.ok("操作成功！");
	}

	@Override
	public R removePaymentform(Long id) {
		Map<String,Object> map = paymentformDao.get(id);
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能删除！");
		}
		paymentformItemDao.remove(id);
		paymentformDao.remove(id);
		return R.ok("删除成功！");
	}
	
	@Override
	public R updatePaymentform(PaymentformDO paymentform, String bodyItem, Long[] deleteId) {
		Map<String,Object> map = paymentformDao.get(paymentform.getId());
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能修改！");
		}
		paymentformDao.update(paymentform);
		List<PaymentformItemDO> bodyIs = JSON.parseArray(bodyItem, PaymentformItemDO.class);
		for(PaymentformItemDO idata : bodyIs){
			if(idata.getPid() == null){
				paymentformItemDao.save(idata);
			}
			paymentformItemDao.update(idata);
		}
		if(deleteId != null){
			paymentformItemDao.batchRemove(deleteId);
		}
		return R.ok("操作成功！");
	}

	@Override
	public R audit(Long id) {
		paymentformDao.audit(id);
		return R.ok("审核通过！");
	}

	@Override
	public R reverseAudit(Long id) {
		paymentformDao.reverseAudit(id);
		return R.ok("反审核通过！");
	}
	
	@Override
	public R listApi(Map<String, Object> map) {
		List<Map<String, Object>> data = paymentformDao.listApi(map);
		Map<String, Object> salesbillListMap = new HashMap<String,Object>();
		if(data!=null && data.size() > 0){
			DsResultResponse dsRet = new DsResultResponse();
			Integer pagesize = (Integer)map.get("pagesize");
			int total = paymentformDao.countApi(map);
			dsRet.setDatas(data);
			dsRet.setPageno((int) map.get("pageno"));
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
			salesbillListMap.put("data", dsRet);
			return R.ok(salesbillListMap);
        }
		return R.error("无此信息！！");
	}

	@Override
	public R getdetail(Long id) {
		Map<String,Object> paymentform = paymentformDao.get(id);
		List<Map<String,Object>> paymentformItemdetail = paymentformItemDao.get(id);
		if(paymentform == null && paymentformItemdetail.size() <= 0) {
			return R.error("无此信息！");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data",paymentform);
		result.put("paymentformItemdetail",paymentformItemdetail);
		return R.ok(result);
	}
	
	@Override
	public PaymentformDO get(Long id){
		return null;
	}
	
	@Override
	public List<PaymentformDO> list(Map<String, Object> map){
		return paymentformDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return paymentformDao.count(map);
	}
	
	@Override
	public int save(PaymentformDO paymentform){
		return paymentformDao.save(paymentform);
	}
	
	@Override
	public int update(PaymentformDO paymentform){
		return paymentformDao.update(paymentform);
	}
	
	@Override
	public int remove(Long id){
		return paymentformDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return paymentformDao.batchRemove(ids);
	}
	
	public String paymentformCode(){
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.ALL_BILL);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<PaymentformDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getPayNum();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}
}
