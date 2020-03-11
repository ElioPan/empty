package com.ev.mes.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.mes.dao.StationDao;
import com.ev.mes.domain.StationDO;
import com.ev.mes.service.StationService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private StationDao stationDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public StationDO get(Long id){
		return stationDao.get(id);
	}
	
	@Override
	public List<StationDO> list(Map<String, Object> map){
		return stationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return stationDao.count(map);
	}
	
	@Override
	public int save(StationDO station){
		return stationDao.save(station);
	}
	
	@Override
	public int update(StationDO station){
		return stationDao.update(station);
	}

	@Override
	public int updateAll(StationDO station){
		return stationDao.updateAll(station);
	}
	
	@Override
	public int remove(Long id){
		return stationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return stationDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return stationDao.listForMap(params);
	}

	@Override
	public R addOrUpdate(StationDO stationDO) {
		Long id = stationDO.getId();
		Map<String,Object> result = Maps.newHashMap();
		if (this.checkSave(stationDO) > 0) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (id == null) {
			stationDO.setCode(this.setCode());
			stationDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			this.save(stationDO);
			id = stationDO.getId();
		}else {
			StationDO station = this.get(id);
			if (station == null) {
				return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
			}
			if (Objects.equals(station.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
				return R.error(messageSourceHandler.getMessage("common.approved.update.disabled",null));
			}
			this.update(stationDO);
		}
		result.put("id",id);
		return R.ok(result);
	}

	@Override
	public R batchRemoveByIds(Long[] ids) {
		for (Long id : ids) {
			StationDO stationDO = this.get(id);
			if (stationDO == null) {
				return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
			}
			if (Objects.equals(stationDO.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
				return R.error(messageSourceHandler.getMessage("common.approved.delete.disabled",null));
			}
		}
		this.batchRemove(ids);
		return R.ok();
	}

	@Override
	public R audit(Long id) {
		StationDO stationDO = this.get(id);
		if (stationDO == null) {
			return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
		}
		if (Objects.equals(stationDO.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
		}
		stationDO.setAuditSign(ConstantForGYL.OK_AUDITED);
		this.update(stationDO);
		return R.ok();
	}

	@Override
	public R reverseAudit(Long id) {
		StationDO stationDO = this.get(id);
		if (stationDO == null) {
			return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
		}
		if (Objects.equals(stationDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)) {
			return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit",null));
		}
		stationDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
		this.update(stationDO);
		return R.ok();
	}

	@Override
	public String setCode() {
		//获取编号
		Map<String, Object> param = Maps.newHashMap();
		param.put("offset", 0);
		param.put("limit", 1);
		List<StationDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getCode();
		}
		return DateFormatUtil.getWorkOrderno(Constant.GW, taskNo, 3);
	}

	@Override
	public int checkSave(StationDO stationDO) {
		return stationDao.checkSave(stationDO);
	}

}
