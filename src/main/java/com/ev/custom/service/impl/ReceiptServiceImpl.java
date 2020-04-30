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
import com.ev.custom.dao.ReceiptDao;
import com.ev.custom.dao.ReceiptItemDao;
import com.ev.custom.domain.ReceiptDO;
import com.ev.custom.domain.ReceiptItemDO;
import com.ev.custom.service.ReceiptService;
import com.google.common.collect.Maps;


@Service
public class ReceiptServiceImpl implements ReceiptService {
	@Autowired
	private ReceiptDao receiptDao;
	
	@Autowired
	private ReceiptItemDao receiptItemDao;
	
	@Override
	public R addReceipt(ReceiptDO receiptDO, String bodyItem) {
		receiptDO.setAuditStatus(Constant.WAIT_AUDIT);
		String code = this.receiptCode();
		receiptDO.setReceiptNum(code);
		receiptDao.save(receiptDO);
		Long id = receiptDO.getId();
		List<ReceiptItemDO> bodyIs = JSON.parseArray(bodyItem, ReceiptItemDO.class);
		for(ReceiptItemDO pdata : bodyIs){
			pdata.setReceiptId(id);
			receiptItemDao.save(pdata);
		}
		return R.ok("操作成功！");
	}
	
	@Override
	public R removeReceipt(Long id) {
		Map<String,Object> map = receiptDao.get(id);
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能删除！");
		}
		receiptItemDao.remove(id);
		receiptDao.remove(id);
		return R.ok("删除成功！");
	}

	@Override
	public R updateReceipt(ReceiptDO receiptDO, String bodyItem, Long[] deleteId) {
		Map<String,Object> map = receiptDao.get(receiptDO.getId());
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能修改！");
		}
		receiptDao.update(receiptDO);
		List<ReceiptItemDO> bodyIs = JSON.parseArray(bodyItem, ReceiptItemDO.class);
		for(ReceiptItemDO idata : bodyIs){
			if(idata.getRid() == null){
				receiptItemDao.save(idata);
			}
			receiptItemDao.update(idata);
		}
		if(deleteId != null){
			receiptItemDao.batchRemove(deleteId);
		}
		return R.ok("操作成功！");
	}
	
	@Override
	public R audit(Long id) {
		receiptDao.audit(id);
		return R.ok("审核通过！");
	}

	@Override
	public R reverseAudit(Long id) {
		receiptDao.reverseAudit(id);
		return R.ok("反审核通过！");
	}
	
	@Override
	public R listApi(Map<String, Object> map) {
		List<Map<String, Object>> data = receiptDao.listApi(map);
		Map<String, Object> salesbillListMap = new HashMap<String,Object>();
		if(data!=null && data.size() > 0){
			DsResultResponse dsRet = new DsResultResponse();
			Integer pagesize = (Integer)map.get("pagesize");
			int total = receiptDao.countApi(map);
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
		Map<String,Object> receipt = receiptDao.get(id);
		List<Map<String,Object>> receiptitdetail = receiptItemDao.get(id);
		if(receipt == null && receiptitdetail.size() <= 0) {
			return R.error("无此信息！");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data",receipt);
		result.put("receiptItemdetail",receiptitdetail);
		return R.ok(result);
	}
	
	@Override
	public ReceiptDO get(Long id){
		return null;
	}
	
	@Override
	public List<ReceiptDO> list(Map<String, Object> map){
		return receiptDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return receiptDao.count(map);
	}
	
	@Override
	public int save(ReceiptDO receipt){
		return receiptDao.save(receipt);
	}
	
	@Override
	public int update(ReceiptDO receipt){
		return receiptDao.update(receipt);
	}
	
	@Override
	public int remove(Long id){
		return receiptDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return receiptDao.batchRemove(ids);
	}
	
	public String receiptCode(){
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.ALL_BILL);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<ReceiptDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getReceiptNum();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}
}
