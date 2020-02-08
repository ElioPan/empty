package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProcessReportCheckItemDao;
import com.ev.mes.domain.ProcessReportCheckItemDO;
import com.ev.mes.service.ProcessReportCheckItemService;



@Service
public class ProcessReportCheckItemServiceImpl implements ProcessReportCheckItemService {
	@Autowired
	private ProcessReportCheckItemDao processReportCheckItemDao;
	
	@Override
	public ProcessReportCheckItemDO get(Long id){
		return processReportCheckItemDao.get(id);
	}
	
	@Override
	public List<ProcessReportCheckItemDO> list(Map<String, Object> map){
		return processReportCheckItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processReportCheckItemDao.count(map);
	}
	
	@Override
	public int save(ProcessReportCheckItemDO processReportCheckItem){
		return processReportCheckItemDao.save(processReportCheckItem);
	}
	
	@Override
	public int update(ProcessReportCheckItemDO processReportCheckItem){
		return processReportCheckItemDao.update(processReportCheckItem);
	}
	
	@Override
	public int remove(Long id){
		return processReportCheckItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processReportCheckItemDao.batchRemove(ids);
	}

	@Override
	public int removeByReportId(Long[] ids) {
		return processReportCheckItemDao.removeByReportId(ids);
	}

	@Override
	public List<Map<String, Object>> detailByCheckId(Long id) {
		return processReportCheckItemDao.detailByCheckId(id);
	}

}
