package com.ev.custom.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.service.PatrolPlanDetailService;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ev.custom.dao.PatrolProjectDao;
import com.ev.custom.domain.PatrolProjectDO;
import com.ev.custom.service.PatrolProjectService;


@Service
public class PatrolProjectServiceImpl implements PatrolProjectService {
	@Autowired
	private PatrolProjectDao patrolProjectDao;
    @Autowired
    private PatrolPlanDetailService patrolPlanDetailService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
	
	@Override
	public PatrolProjectDO get(Long id){
		return patrolProjectDao.get(id);
	}
	
	@Override
	public List<PatrolProjectDO> list(Map<String, Object> map){
		return patrolProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolProjectDao.count(map);
	}
	
	@Override
	public int save(PatrolProjectDO patrolProject){
		Map<String,Object> param= Maps.newHashMapWithExpectedSize(2);
		String code = patrolProject.getCode();
		if (StringUtils.isBlank(code) || code.startsWith(ConstantForDevice.XJBZ)) {
			param.put("maxNo", ConstantForDevice.XJBZ);
			param.put("offset", 0);
			param.put("limit", 1);

			List<PatrolProjectDO> list = this.list(param);
			patrolProject.setCode(DateFormatUtil.getWorkOrderno(ConstantForDevice.XJBZ, list.size() > 0 ? list.get(0).getCode() : null, 6));
		}else {
			patrolProject.setCode(code.trim());
		}

		param.put("code", patrolProject.getCode());
		List<PatrolProjectDO> list = this.list(param);
		if(list.size()>0){
			return -1;
		}

//		PatrolProjectDO projectName = this.patrolProjectDao.getByName(patrolProject.getName());
		param.put("name", patrolProject.getName());
		List<PatrolProjectDO> lists = this.list(param);
		if(lists.size()>0){
			return -2;
		}
		return patrolProjectDao.save(patrolProject);
	}
	
	@Override
	public int update(PatrolProjectDO patrolProject){
		Map<String,Object> param= Maps.newHashMapWithExpectedSize(2);
		PatrolProjectDO patrolProjectDO = this.get(patrolProject.getId());
		if(!patrolProjectDO.getName().equals(patrolProject.getName())){
			param.put("name", patrolProject.getName());
			List<PatrolProjectDO> lists = this.list(param);
			if(lists.size()>0){
				return -1;
			}
		}
		return patrolProjectDao.update(patrolProject);
	}
	
	@Override
	public int remove(Long id){
		return patrolProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolProjectDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> listApi(int pageno, int pagesize, String fuzzySearch) {
//		Map<String,Object> dictMap = this.dictionaryService.getDictMap();
		Map<String,Object> results = Maps.newHashMap();
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("offset",pageno-1);
		params.put("limit",pagesize);
		params.put("fuzzySearch",fuzzySearch);
//		params.put("delFlag", 0);
		List<PatrolProjectDO> projects = patrolProjectDao.list(params);
		int total = patrolProjectDao.count(params);
		if(projects!=null && projects.size()>0){
			List<Map<String,Object>> datas = new ArrayList<>();
			for(PatrolProjectDO project:projects){
				Map<String,Object> map = Maps.newHashMapWithExpectedSize(4);
				map.put("id", project.getId());
				map.put("code", project.getCode() == null ? "" :project.getCode());
//				map.put("type", project.getType() == null ? "" :dictMap.get(project.getType().toString()));
				map.put("name", project.getName()== null ? "" :project.getName());
				map.put("function", project.getFunction() == null ? "" :project.getFunction());
				datas.add(map);
			}
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(datas);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
			results.put("data",dsRet);
		}
		return results;
	}

	@Override
	public R removeProject(Long projectId) {
//		PatrolProjectDO projectDO = new PatrolProjectDO();
//		projectDO.setId(projectId);
//		projectDO.setDelFlag(1);
        Map<String, Object> map = Maps.newHashMap();
        map.put("linkProjectId", StringUtils.sqlLike(","+projectId+","));
        if (patrolPlanDetailService.count(map)>0){
            return R.error(messageSourceHandler.getMessage("common.child.delete.disabled",null));
        }
		return this.remove(projectId)>0?R.ok():R.error();
	}

	@Override
	public R batchRemoveProject(Long[] projectId) {
        Map<String, Object> map;
        for (Long aLong : projectId) {
            map = Maps.newHashMap();
            map.put("linkProjectId", StringUtils.sqlLike(","+aLong+","));
            if (patrolPlanDetailService.count(map)>0){
                return R.error(messageSourceHandler.getMessage("common.child.delete.disabled",null));
            }
        }
        return this.batchRemove(projectId)>0?R.ok():R.error();
	}

}
