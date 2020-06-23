package com.ev.custom.service.impl;

import com.ev.custom.dao.UpkeepRecordPartDao;
import com.ev.custom.dao.UpkeepRecordProjectDao;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.custom.domain.UpkeepRecordPartDO;
import com.ev.custom.domain.UpkeepRecordProjectDO;
import com.ev.custom.service.MaterielService;
import com.ev.custom.service.UpkeepRecordPartService;
import com.ev.custom.service.UpkeepRecordService;
import com.ev.framework.utils.StringUtils;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@EnableTransactionManagement
@Service
public class UpkeepRecordPartServiceImpl implements UpkeepRecordPartService {
	@Autowired
	private UpkeepRecordPartDao upkeepRecordPartDao;
	@Autowired
	private UpkeepRecordProjectDao upkeepRecordProjectDao;
	@Autowired
	private UpkeepRecordService upkeepRecordService;
	private MaterielService materielService;

	@Override
	public UpkeepRecordPartDO get(Integer id){
		return upkeepRecordPartDao.get(id);
	}
	
	@Override
	public List<UpkeepRecordPartDO> list(Map<String, Object> map){
		return upkeepRecordPartDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepRecordPartDao.count(map);
	}
	
	@Override
	public int save(UpkeepRecordPartDO upkeepRecordPart){
		return upkeepRecordPartDao.save(upkeepRecordPart);
	}
	
	@Override
	public int update(UpkeepRecordPartDO upkeepRecordPart){
		return upkeepRecordPartDao.update(upkeepRecordPart);
	}
	
	@Override
	public int remove(Integer id){
		return upkeepRecordPartDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return upkeepRecordPartDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> spartByRecordId(Map<String, Object> map) {
		return upkeepRecordPartDao.SpartByRecordId(map);
	}

	@Override
	public List<Map<String, Object>> spartByRecordIdAgain(Map<String, Object> map) {
		return upkeepRecordPartDao.spartByRecordIdAgain(map);
	}

	@Override
	public int dealWithSpareDetails(Map<String, Object> map) {
		List<Map<String, Object>> oneRecordDetailspartOne = upkeepRecordPartDao.SpartByRecordId(map);

		if (oneRecordDetailspartOne.size() > 0) {
			int count = 0;
			for (Map<String, Object> mapsSpart : oneRecordDetailspartOne) {

				MaterielDO materielDO = materielService.get(Long.parseLong(mapsSpart.get("part_id").toString()));
				Double price =0.0;
				if(materielDO!=null){
					price=materielDO.getSalePrice().doubleValue();
				}

				Double dd=0.0;
				if (Objects.equals(dd, Double.parseDouble(mapsSpart.get("spart_sum").toString()))) {

					Double spart_amount = Double.parseDouble(mapsSpart.get("spart_amount").toString());

					Double spart_sum = Double.valueOf((spart_amount * price));

					UpkeepRecordPartDO upkeepRecordPartDO = new UpkeepRecordPartDO();
					upkeepRecordPartDO.setId(Long.valueOf(mapsSpart.get("id").toString()));
					upkeepRecordPartDO.setSpartPrice(Double.valueOf(price));
					upkeepRecordPartDO.setSpartSum(spart_sum);
					//upkeepRecordPartDO.setSpartUnit(mapsSpart.get("unitName").toString());

					count += upkeepRecordPartDao.update(upkeepRecordPartDO);
				}
			}
			if (count >= 0) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	@Override
	public void updateRecorPartDetail(String project, String sparts, UpkeepRecordDO upkeepRecordDO) {

		Long recordId=upkeepRecordDO.getId();

		Long planId=upkeepRecordService.get(recordId)!=null?upkeepRecordService.get(recordId).getPlanId():null;

		//保存工单下保养项目
		this.doSaveAndChangePro(project,recordId,planId);

		JSONObject jsonObject;
		if(StringUtils.isEmpty(sparts)){
			jsonObject = null;
		}else{
			jsonObject = JSONObject.fromObject(sparts);
		}
		JSONArray jsonArray;
		if(jsonObject == null){
			jsonArray= new JSONArray();
		}else{
			jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));
		}

		//将所有工单下备件全部删除+保存
		upkeepRecordPartDao.removeByRecordId(recordId);
		for (int i = 0; i < jsonArray.size(); i++) {
			Map<String, Object> mapsDetail = (Map<String, Object>) jsonArray.get(i);
			UpkeepRecordPartDO upkeepRecordPartDO = new UpkeepRecordPartDO();
			upkeepRecordPartDO.setPlanId(planId);
			upkeepRecordPartDO.setRecordId(recordId);
			upkeepRecordPartDO.setPartId(Long.parseLong(mapsDetail.containsKey("partId")?mapsDetail.get("partId").toString():null));
			upkeepRecordPartDO.setSpartAmount(mapsDetail.containsKey("spart_amount")?mapsDetail.get("spart_amount").toString():"0");
			upkeepRecordPartDO.setSpartSum(Double.parseDouble(mapsDetail.containsKey("spart_sum")?mapsDetail.get("spart_sum").toString():"0"));
			upkeepRecordPartDO.setRemark(mapsDetail.containsKey("remark")?mapsDetail.get("remark").toString():"");
			upkeepRecordPartDO.setSpartPrice(mapsDetail.containsKey("spartPrice")?Double.parseDouble(mapsDetail.get("spartPrice").toString()):0.0);
			upkeepRecordPartDao.save(upkeepRecordPartDO);
		}
		upkeepRecordService.update(upkeepRecordDO);
	}

	@Override
	public int removeByRecordId(Long id) {
		return upkeepRecordPartDao.removeByRecordId(id);
	}

	@Override
	public int batchRemoveByRecordId(Long[] ids) {
		return upkeepRecordPartDao.batchRemoveByRecordId(ids);
	}


	/**
	 * 更新保存工单下保养项目
	 * @param project
	 */
   public void doSaveAndChangePro(String project,Long recoldId,Long planId){
	   //{"dataList":[{"projectId":3,"manhour":5,"result":1(1正常，0异常),"remark":"备注"},{"projectId":3,"manhour":5,"result":1,"remark":"备注"}]}
	   //{"dataList":[{"projectId":3,"manhour":5,"manHourCost":100,"result":1(1正常，0异常),"remark":"备注"},{"projectId":3,"manhour":5,"manHourCost":100,"result":1(1正常，0异常),"remark":"备注"}]}
	   //先删除所有保项目
	   upkeepRecordProjectDao.removeByRecordId(recoldId);

	   JSONObject jsonObject = JSONObject.fromObject(project);
	   JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));
	   for (int i = 0; i < jsonArray.size(); i++) {
		   Map<String, Object> mapsDetail = (Map<String, Object>) jsonArray.get(i);

		   UpkeepRecordProjectDO uRPDo = new UpkeepRecordProjectDO();
		   uRPDo.setPlanId(planId);
		   uRPDo.setRecordId(recoldId);
		   uRPDo.setProjectId(Long.parseLong(mapsDetail.containsKey("projectId")?mapsDetail.get("projectId").toString():null));
		   uRPDo.setManhour(Integer.parseInt(mapsDetail.containsKey("manhour")?mapsDetail.get("manhour").toString():null));
		   uRPDo.setResult(Integer.parseInt(mapsDetail.containsKey("result")?mapsDetail.get("result").toString():null));
		   uRPDo.setRemark(mapsDetail.containsKey("remark")?mapsDetail.get("remark").toString():"");
		   uRPDo.setManHourCost(mapsDetail.containsKey("manHourCost")?new BigDecimal(Double.parseDouble(mapsDetail.get("manHourCost").toString())):new BigDecimal(0));

		   upkeepRecordProjectDao.save(uRPDo);
	   }


   }



}
