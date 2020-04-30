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
import com.ev.custom.dao.BanktransferslipDao;
import com.ev.custom.dao.BanktransferslipItemDao;
import com.ev.custom.domain.BanktransferslipDO;
import com.ev.custom.domain.BanktransferslipItemDO;
import com.ev.custom.service.BanktransferslipService;
import com.google.common.collect.Maps;

@Service
public class BanktransferslipServiceImpl implements BanktransferslipService {
	@Autowired
	private BanktransferslipDao banktransferslipDao;
	
	@Autowired
	private BanktransferslipItemDao banktransferslipItemDao;

	@Override
	public R addBankTransferSlip(BanktransferslipDO banktransferslip, String bodyItem) {
		banktransferslip.setAuditStatus(Constant.WAIT_AUDIT);
		String code = this.banktransferslipCode();
		banktransferslip.setTransferNum(code);
		banktransferslipDao.save(banktransferslip);
		Long id = banktransferslip.getId();
		List<BanktransferslipItemDO> bodyIs = JSON.parseArray(bodyItem, BanktransferslipItemDO.class);
		for(BanktransferslipItemDO pdata : bodyIs){
			pdata.setBanktransferslipId(id);
			banktransferslipItemDao.save(pdata);
		}
		return R.ok("操作成功！");
	}

	@Override
	public R removeBankTransferSlip(Long id) {
		Map<String,Object> map = banktransferslipDao.get(id);
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能删除！");
		}
		banktransferslipItemDao.remove(id);
		banktransferslipDao.remove(id);
		return R.ok("删除成功！");
	}

	@Override
	public R updateBankTransferSlip(BanktransferslipDO banktransferslip, String bodyItem, Long[] deleteId) {
		Map<String,Object> map = banktransferslipDao.get(banktransferslip.getId());
		if(map == null) {
			return R.ok("无此信息！");
		}
		String value = map.get("auditStatus").toString();
		if("已审核".equals(value)) {
			return R.ok("已审核不能修改！");
		}
		banktransferslipDao.update(banktransferslip);
		List<BanktransferslipItemDO> bodyIs = JSON.parseArray(bodyItem, BanktransferslipItemDO.class);
		for(BanktransferslipItemDO idata : bodyIs){
			if(idata.getTid() == null){
				banktransferslipItemDao.save(idata);
			}
			banktransferslipItemDao.update(idata);
		}
		if(deleteId != null){
			banktransferslipItemDao.batchRemove(deleteId);
		}
		return R.ok("操作成功！");
	}
	
	@Override
	public R audit(Long id) {
		banktransferslipDao.audit(id);
		return R.ok("审核通过！");
	}

	@Override
	public R reverseAudit(Long id) {
		banktransferslipDao.reverseAudit(id);
		return R.ok("反审核通过！");
	}
	
	@Override
	public R listApi(Map<String, Object> map) {
		List<Map<String, Object>> data = banktransferslipDao.listApi(map);
		Map<String, Object> banktransferslipListMap = new HashMap<String,Object>();
		if(data!=null && data.size() > 0){
			DsResultResponse dsRet = new DsResultResponse();
			Integer pagesize = (Integer)map.get("pagesize");
			int total = banktransferslipDao.countApi(map);
			dsRet.setDatas(data);
			dsRet.setPageno((int) map.get("pageno"));
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
			banktransferslipListMap.put("data", dsRet);
			return R.ok(banktransferslipListMap);
        }
		return R.error("无此信息！！");
	}

	@Override
	public R getdetail(Long id) {
		Map<String,Object> banktransferslip = banktransferslipDao.get(id);
		List<Map<String,Object>> banktransferslipItemdetail = banktransferslipItemDao.get(id);
		if(banktransferslip == null && banktransferslipItemdetail.size() <= 0) {
			return R.error("无此信息！");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data",banktransferslip);
		result.put("banktransferslipItemdetail",banktransferslipItemdetail);
		return R.ok(result);
	}
	
	@Override
	public BanktransferslipDO get(Long id){
		return null;
	}
	
	@Override
	public List<BanktransferslipDO> list(Map<String, Object> map){
		return banktransferslipDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return banktransferslipDao.count(map);
	}
	
	@Override
	public int save(BanktransferslipDO banktransferslip){
		return banktransferslipDao.save(banktransferslip);
	}
	
	@Override
	public int update(BanktransferslipDO banktransferslip){
		return banktransferslipDao.update(banktransferslip);
	}
	
	@Override
	public int remove(Long id){
		return banktransferslipDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return banktransferslipDao.batchRemove(ids);
	}
	
	public String banktransferslipCode(){
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.ALL_BILL);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<BanktransferslipDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getTransferNum();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}
}
