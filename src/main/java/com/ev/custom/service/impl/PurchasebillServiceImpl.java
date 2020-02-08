package com.ev.custom.service.impl;

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
import com.ev.custom.dao.PurchasebillDao;
import com.ev.custom.dao.PurchasebillItemDao;
import com.ev.custom.domain.PurchasebillDO;
import com.ev.custom.domain.PurchasebillItemDO;
import com.ev.custom.service.PurchasebillService;
import com.google.common.collect.Maps;



@Service
public class PurchasebillServiceImpl implements PurchasebillService {
	@Autowired
	private PurchasebillDao purchasebillDao;
	
	@Autowired
	private PurchasebillItemDao purchasebillItemDao;
	
	@Override
	public R addPurchaseBill(PurchasebillDO purchasebillDO, String bodyItem) {
		purchasebillDO.setAuditStatus(ConstantForGYL.WAIT_AUDIT);
		String code = this.PurchaseBillCode();
		purchasebillDO.setBillNumber(code);
		purchasebillDao.save(purchasebillDO);
		Long id = purchasebillDO.getId();
		List<PurchasebillItemDO> bodyIs = JSON.parseArray(bodyItem, PurchasebillItemDO.class);
		for(PurchasebillItemDO pdata : bodyIs){
			pdata.setPurchasebillId(id);
			purchasebillItemDao.save(pdata);
		}
		return R.ok("操作成功！");
	}

	@Override
	public R removePurchaseBill(Long purchaseBillId) {
		Map<String,Object> map = purchasebillDao.get(purchaseBillId);
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能删除！");
		}
		purchasebillItemDao.remove(purchaseBillId);
		purchasebillDao.remove(purchaseBillId);
		return R.ok("删除成功！");
	}

	@Override
	public R listApi(Map<String, Object> map) {
		List<Map<String, Object>> data = purchasebillDao.listApi(map);
		Map<String, Object> purchasebillListMap = new HashMap<String,Object>();
		if(data!=null && data.size() > 0){
			DsResultResponse dsRet = new DsResultResponse();
			Integer pagesize = (Integer)map.get("pagesize");
			int total = purchasebillDao.countApi(map);
			dsRet.setDatas(data);
			dsRet.setPageno((int) map.get("pageno"));
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
			purchasebillListMap.put("data", dsRet);
			return R.ok(purchasebillListMap);
        }
		return R.error("无此信息！！");
	}

	@Override
	public R updatePurchaseBill(PurchasebillDO purchasebillDO, String bodyItem,Long[] deleteId) {
		Map<String,Object> map = purchasebillDao.get(purchasebillDO.getId());
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能修改！");
		}
		purchasebillDao.update(purchasebillDO);
		List<PurchasebillItemDO> bodyIs = JSON.parseArray(bodyItem, PurchasebillItemDO.class);
		for(PurchasebillItemDO idata : bodyIs){
			if(idata.getBid() == null){
				purchasebillItemDao.save(idata);
			}
			purchasebillItemDao.update(idata);
		}
		if(deleteId != null) {
			purchasebillItemDao.batchRemove(deleteId);
		}
		return R.ok("操作成功！");
	}

	@Override
	public R audit(Long id) {
		purchasebillDao.audit(id);
		return R.ok("审核通过！");
	}

	@Override
	public R reverseAudit(Long id) {
		purchasebillDao.reverseAudit(id);
		return R.ok("反审核通过！");
	}

	@Override
	public R getdetail(Long id) {
		Map<String,Object> purchasebill = purchasebillDao.get(id);
		List<Map<String,Object>> purbitdetail = purchasebillItemDao.get(id);
		if(purchasebill == null && purbitdetail.size() <= 0) {
			return R.error("无此信息！");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data",purchasebill);
		result.put("purchasebillItemdetail",purbitdetail);
		return R.ok(result);
	}
	
	@Override
	public PurchasebillDO get(Long id){
		return null;
	}
	
	@Override
	public List<PurchasebillDO> list(Map<String, Object> map){
		return purchasebillDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchasebillDao.count(map);
	}
	
	@Override
	public int save(PurchasebillDO purchasebill){
		return purchasebillDao.save(purchasebill);
	}
	
	@Override
	public int update(PurchasebillDO purchasebill){
		return purchasebillDao.update(purchasebill);
	}
	
	@Override
	public int remove(Long id){
		return purchasebillDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchasebillDao.batchRemove(ids);
	}
	
	public String PurchaseBillCode(){
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.BILL_CODE);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<PurchasebillDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getBillNumber();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}
}
