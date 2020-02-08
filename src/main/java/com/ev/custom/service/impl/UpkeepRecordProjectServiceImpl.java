package com.ev.custom.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.UpkeepRecordProjectDao;
import com.ev.custom.domain.UpkeepRecordProjectDO;
import com.ev.custom.service.UpkeepRecordProjectService;



@Service
public class UpkeepRecordProjectServiceImpl implements UpkeepRecordProjectService {
	@Autowired
	private UpkeepRecordProjectDao upkeepRecordProjectDao;
	
	@Override
	public UpkeepRecordProjectDO get(Long id){
		return upkeepRecordProjectDao.get(id);
	}
	
	@Override
	public List<UpkeepRecordProjectDO> list(Map<String, Object> map){
		return upkeepRecordProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepRecordProjectDao.count(map);
	}
	
	@Override
	public int save(UpkeepRecordProjectDO upkeepRecordProject){
		return upkeepRecordProjectDao.save(upkeepRecordProject);
	}
	
	@Override
	public int update(UpkeepRecordProjectDO upkeepRecordProject){
		return upkeepRecordProjectDao.update(upkeepRecordProject);
	}
	
	@Override
	public int remove(Long id){
		return upkeepRecordProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepRecordProjectDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> projectDetailByRecordID(Map<String, Object> map) {
		return upkeepRecordProjectDao.projectDetailByRecordID(map);
	}

	@Override
	public int updateRecorProDetail(String str) {
		//{"dataList":[{"projectId":3,"manhour":5,"result":1(1正常，0异常),"remark":"备注"},{"projectId":3,"manhour":5,"result":1,"remark":"备注"}]}
		JSONObject jsonObject = JSONObject.fromObject(str);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));

		int counts = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			Map<String, Object> mapsDetail = (Map<String, Object>) jsonArray.get(i);

			UpkeepRecordProjectDO uRPDo = new UpkeepRecordProjectDO();
			uRPDo.setId(Long.parseLong(mapsDetail.get("projectId").toString()));
			uRPDo.setManhour(Integer.parseInt(mapsDetail.get("manhour").toString()));
			uRPDo.setResult(Integer.parseInt(mapsDetail.get("result").toString()));
			uRPDo.setRemark(mapsDetail.get("remark").toString());

			counts += upkeepRecordProjectDao.update(uRPDo);
		}
		return counts;
	}

	@Override
	public List<Map<String, Object>> getMsgByDeviceId(Map<String, Object> map) {
		return upkeepRecordProjectDao.getMsgforDevice(map);
	}

	@Override
	public int removeByRecordId(Long id) {
		return upkeepRecordProjectDao.removeByRecordId(id);
	}

	@Override
	public int batchRemoveByRecordId(Long[] ids) {
		return upkeepRecordProjectDao.batchRemoveByRecordId(ids);
	}

}
