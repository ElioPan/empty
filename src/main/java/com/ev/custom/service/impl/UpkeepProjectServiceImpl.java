package com.ev.custom.service.impl;

import com.ev.custom.dao.UpkeepProjectDao;
import com.ev.custom.domain.UpkeepProjectDO;
import com.ev.custom.service.UpkeepPlanProjectService;
import com.ev.custom.service.UpkeepProjectService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class UpkeepProjectServiceImpl implements UpkeepProjectService {
	@Autowired
	private UpkeepProjectDao upkeepProjectDao;
    @Autowired
    private UpkeepPlanProjectService upkeepPlanProjectService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
	
	@Override
	public UpkeepProjectDO get(Long id){
		return upkeepProjectDao.get(id);
	}
	
	@Override
	public List<UpkeepProjectDO> list(Map<String, Object> map){
		return upkeepProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepProjectDao.count(map);
	}
	
	@Override
	public int save(UpkeepProjectDO upkeepProject){
		return upkeepProjectDao.save(upkeepProject);
	}
	
	@Override
	public int update(UpkeepProjectDO upkeepProject){
		return upkeepProjectDao.update(upkeepProject);
	}
	
	@Override
	public int remove(Long id){
		return upkeepProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepProjectDao.batchRemove(ids);
	}

	@Override
	public List<UpkeepProjectDO> objectList() {
		return upkeepProjectDao.objectList();
	}

	@Override
	public List<UpkeepProjectDO> getByProjectIds(Long[] projectIds) {
		return upkeepProjectDao.getByProjectIds(projectIds);
	}

	@Override
	public List<Map<String, Object>> findListsPros(Map<String, Object> map) {
		return upkeepProjectDao.findListsPro(map);
	}

	@Override
	public int countOfList(Map<String, Object> map) {
		return upkeepProjectDao.countOfList(map);
	}

    @Override
    public R delete(Long[] ids) {
        Map<String, Object> map;
        for (Long id : ids) {
            map = Maps.newHashMap();
            map.put("projectId",id);
            if (upkeepPlanProjectService.count(map)>0){
                return R.error(messageSourceHandler.getMessage("common.child.delete.disabled",null));
            }
        }
        return this.batchRemove(ids)>0?R.ok():R.error();
    }

	@Override
	public int countOfCode(Map<String, Object> map) {
		return upkeepProjectDao.countOfCode(map);
	}

	@Override
	public R addProject(UpkeepProjectDO upkeepProjectDO) {

		Map<String,Object>  map= new HashMap<>();
		int savesPro=0;
		if(Objects.nonNull(upkeepProjectDO.getId())){
			map.put("haveId",upkeepProjectDO.getId());
			map.put("code",upkeepProjectDO.getCode());

			int ii=this.countOfCode(map);
			if(this.countOfCode(map)>0){
				return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
			}
			savesPro=this.update(upkeepProjectDO);
		}else{
			map.put("code",upkeepProjectDO.getCode());
			if(this.countOfCode(map)>0){
				return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
			}
			savesPro=this.save(upkeepProjectDO);
		}
		if (savesPro > 0) {
			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
	}


}
