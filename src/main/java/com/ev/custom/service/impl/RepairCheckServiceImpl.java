package com.ev.custom.service.impl;

import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.service.RepairEventService;
import com.ev.custom.service.RepairRecordService;
import com.ev.framework.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.framework.config.ConstantForDevice;
import com.ev.custom.dao.RepairCheckDao;
import com.ev.custom.domain.RepairCheckDO;
import com.ev.custom.service.RepairCheckService;



@Service
public class RepairCheckServiceImpl implements RepairCheckService {
	@Autowired
	private RepairCheckDao repairCheckDao;
	@Autowired
	private RepairEventService repairEventService;
	@Autowired
	private RepairRecordService recordService;
	
	@Override
	public RepairCheckDO get(Long id){
		return repairCheckDao.get(id);
	}
	
	@Override
	public List<RepairCheckDO> list(Map<String, Object> map){
		return repairCheckDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return repairCheckDao.count(map);
	}
	
	@Override
	public int save(RepairCheckDO repairCheck){
		return repairCheckDao.save(repairCheck);
	}
	
	@Override
	public int update(RepairCheckDO repairCheck){
		return repairCheckDao.update(repairCheck);
	}
	
	@Override
	public int remove(Long id){
		return repairCheckDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return repairCheckDao.batchRemove(ids);
	}

	@Override
	public void completeCheck(RepairEventDO event,RepairCheckDO check) {
		int count = 0;
		RepairRecordDO recordDO = null;
		if (check.getId()==null) {
			// 取出有关该次维修事件的列表
			Long eventId = event.getId();
			List<RepairRecordDO> recordDOs = recordService.getByEventId(eventId);
			if (recordDOs.size()>0) {
				// 取出最后一条维修记录
				recordDO = recordDOs.get(0);
				Long recordId = recordDO.getId();
				check.setRecordId(recordId);
				check.setEventId(eventId);
				count = this.repairCheckDao.save(check);
			}
		}
		if (check.getId()!=null) {
			count = update(check);
			recordDO = recordService.get(check.getRecordId());
		}
		if (check.getResult().equals(Constant.TS)) {
			return;
		}
		if (count>0) {
			// 若验收结果为不通过
			if (Objects.equals(check.getResult(), ConstantForDevice.RESULT_UNPASS)) {
				// 设置为维修事件为待处理 Constant.WAITING_DEAL 
				event.setStatus(ConstantForDevice.WAITING_DEAL);
				this.repairEventService.update(event);
				// 设置维修记录为不通过 Constant.RESULT_UNPASS
				recordDO.setStatus(ConstantForDevice.RESULT_UNPASS);
				this.recordService.update(recordDO);
			}
			// 若验收结果为通过
			if (Objects.equals(check.getResult(), ConstantForDevice.RESULT_PASS)) {
				// 设置为已验收  Constant.WAITING_CHECK 
				event.setStatus(ConstantForDevice.ALREADY_CHECK);
				this.repairEventService.update(event);
				// 设置维修记录为通过 Constant.RESULT_PASS
				recordDO.setStatus(ConstantForDevice.RESULT_PASS);
				this.recordService.update(recordDO);
			}
		}
	}

	@Override
	public RepairCheckDO getByEventId(Long id) {
		return this.repairCheckDao.getByEventId(id);
	}

	@Override
	public List<Map<String,Object>> listForMap(Map<String, Object> param) {
		return this.repairCheckDao.listForMap(param);
	}

}
