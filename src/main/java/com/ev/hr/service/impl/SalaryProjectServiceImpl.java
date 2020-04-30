package com.ev.hr.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.hr.vo.SalaryProjectPageParam;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.hr.dao.SalaryProjectDao;
import com.ev.hr.domain.SalaryProjectDO;
import com.ev.hr.service.SalaryProjectService;



@Service
public class SalaryProjectServiceImpl implements SalaryProjectService {
	@Autowired
	private SalaryProjectDao salaryProjectDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public SalaryProjectDO get(Long id){
		return salaryProjectDao.get(id);
	}
	
	@Override
	public List<SalaryProjectDO> list(Map<String, Object> map){
		return salaryProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salaryProjectDao.count(map);
	}
	
	@Override
	public int save(SalaryProjectDO salaryProject){
		return salaryProjectDao.save(salaryProject);
	}
	
	@Override
	public int update(SalaryProjectDO salaryProject){
		return salaryProjectDao.update(salaryProject);
	}

	@Override
	public int updateAll(SalaryProjectDO salaryProject){
		return salaryProjectDao.updateAll(salaryProject);
	}
	
	@Override
	public int remove(Long id){
		return salaryProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return salaryProjectDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> pageList(SalaryProjectPageParam pageParam) {
		Map<String,Object> result = Maps.newHashMap();
		pageParam.setSalaryItemName(StringUtils.sqlLike(pageParam.getSalaryItemName()));
		List<Map<String, Object>> data = salaryProjectDao.pageList(pageParam);
		int total = salaryProjectDao.pageCount(pageParam);
		result.put("data", new DsResultResponse(pageParam.getPageno(),pageParam.getPagesize(),total,data));
		return R.ok(result);
	}

	@Override
	public Map<String, Object> getById(Long id) {
		Map<String, Object> detail = this.salaryProjectDao.getById(id);
		if (detail != null) {
			return R.ok(detail);
		}
		return R.error();
	}

	@Override
	public R delete(List<Long> ids) {
		// TODO 引用后不能删除
		this.batchRemove(ids.toArray(new Long[0]));
		return R.ok();
	}

	@Override
	public R saveAndVerify(SalaryProjectDO saveParam) {
		// 验重
		boolean checkRepeat = this.checkRepeat(saveParam);
		if(checkRepeat){
			return R.error(messageSourceHandler.getMessage("hr.duplicate.sortNoOrName",null));
		}
		if(saveParam.getId()==null){
			salaryProjectDao.save(saveParam);
		}else {
			salaryProjectDao.update(saveParam);
		}
		return R.ok();
	}

	private boolean checkRepeat(SalaryProjectDO saveParam) {
		return salaryProjectDao.checkRepeat(saveParam)>0;
	}

	@Override
	public Map<String, Object> getListByType(String type) {
		return null;
	}

}
