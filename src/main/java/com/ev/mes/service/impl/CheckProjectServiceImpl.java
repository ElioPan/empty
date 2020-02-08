package com.ev.mes.service.impl;

import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.mes.dao.CheckProjectDao;
import com.ev.mes.domain.CheckProjectDO;
import com.ev.mes.service.CheckPlanItemService;
import com.ev.mes.service.CheckProjectService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;



@Service
public class CheckProjectServiceImpl implements CheckProjectService {
	@Autowired
	private CheckProjectDao checkProjectDao;
	@Autowired
	private CheckPlanItemService checkPlanItemService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public CheckProjectDO get(Long id){
		return checkProjectDao.get(id);
	}
	
	@Override
	public List<CheckProjectDO> list(Map<String, Object> map){
		return checkProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return checkProjectDao.count(map);
	}
	
	@Override
	public int save(CheckProjectDO checkProject){
		return checkProjectDao.save(checkProject);
	}
	
	@Override
	public int update(CheckProjectDO checkProject){
		return checkProjectDao.update(checkProject);
	}
	
	@Override
	public int remove(Long id){
		return checkProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return checkProjectDao.batchRemove(ids);
	}

	@Override
	public void saveAndChangePro(CheckProjectDO checkProject){

		if(Objects.nonNull(checkProject.getId())){
			checkProjectDao.update(checkProject);
		}else{
			//<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
			String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.CHECK_JYXM, new Date());
			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
			params.put("maxNo", prefix);
			params.put("offset", 0);
			params.put("limit", 1);
			List<CheckProjectDO> list = checkProjectDao.list(params);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getCode();
			}
			checkProject.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));

			checkProjectDao.save(checkProject);
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return checkProjectDao.listForMap(map);
	}

	@Override
	public int countListForMap(Map<String, Object> map) {
		return checkProjectDao.countListForMap(map);
	}

	@Override
	public R deletOfPro(Long[] ids) {
		//检验是否被检验方案明细引用
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		results.put("id",ids);

		int i = checkPlanItemService.canDeletOfProject(results);
		if(i==0){
				//邏輯删除
			checkProjectDao.deletOfPro(results);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
		}
	}




}
