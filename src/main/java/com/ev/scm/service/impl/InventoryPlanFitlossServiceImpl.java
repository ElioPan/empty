package com.ev.scm.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.scm.dao.InventoryPlanFitlossDao;
import com.ev.scm.domain.InventoryPlanDO;
import com.ev.scm.domain.InventoryPlanFitlossDO;
import com.ev.scm.service.InventoryPlanFitlossService;
import com.ev.scm.service.InventoryPlanService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@EnableTransactionManagement
@Service
public class InventoryPlanFitlossServiceImpl implements InventoryPlanFitlossService {
	@Autowired
	private InventoryPlanFitlossDao checkProfitlossDao;
	@Autowired
	private InventoryPlanService checkHeadService;

	
	@Override
	public InventoryPlanFitlossDO get(Integer id){
		return checkProfitlossDao.get(id);
	}
	
	@Override
	public List<InventoryPlanFitlossDO> list(Map<String, Object> map){
		return checkProfitlossDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return checkProfitlossDao.count(map);
	}
	
	@Override
	public int save(InventoryPlanFitlossDO checkProfitloss){
		return checkProfitlossDao.save(checkProfitloss);
	}
	
	@Override
	public int update(InventoryPlanFitlossDO checkProfitloss){
		return checkProfitlossDao.update(checkProfitloss);
	}
	
	@Override
	public int remove(Integer id){
		return checkProfitlossDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return checkProfitlossDao.batchRemove(ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveProfitORLoss(List<Map<String, Object>> listMap, Long documentType)  {
			Map<String,Object> query = Maps.newHashMap();
		    Long headId=0L;
			String code = null;
			if (Objects.equals(documentType,ConstantForGYL.PYDJ)) {
				code = "PY" + DateFormatUtil.getWorkOrderno();
			} else {
				code = "PK" + DateFormatUtil.getWorkOrderno();
			}
			int rows = 0;
			for (Map<String, Object> mapDetail : listMap) {
				headId=Long.parseLong(mapDetail.get("headId").toString());
				InventoryPlanFitlossDO cFLDo = new InventoryPlanFitlossDO();
				cFLDo.setHeadId(headId);
				cFLDo.setBodyId(Long.parseLong(mapDetail.get("itemId").toString()));
				cFLDo.setCode(code);
				cFLDo.setDocumentType(documentType);
				rows += checkProfitlossDao.save(cFLDo);
			}
			if (rows == listMap.size()) {
				//并验证更改方案的状态为25
//				this.countOfYKCount(headId);
				return true;
			} else {
				return false;
			}
	}

	/**
	 * 判断盘盈亏是否已经生成了其他入库（>0是）
	 * @param map
	 * @return
	 */
	@Override
	public int countOfOtherByPY(Map<String, Object> map) {
		return checkProfitlossDao.countOfOtherByPY(map);
	}

	//判断盈亏单是否已经生成
	@Override
	public int countOfYKCount(Long planId) {
		Map<String,Object> queryPY = Maps.newHashMap();
		Map<String,Object> queryPK = Maps.newHashMap();
		queryPY.put("headId",planId);
		queryPY.put("documentType",32);
		queryPK.put("headId",planId);
		queryPK.put("documentType",28);
		int rows=checkProfitlossDao.countOfYKCount(queryPY);
		int lines=checkProfitlossDao.countOfYKCount(queryPK);
		if(rows==1&&lines==1){
			//更新方案表状态为192  执行结束
			InventoryPlanDO checkHeadDO =new InventoryPlanDO();
			checkHeadDO.setId(planId);
			checkHeadDO.setCheckStatus(ConstantForGYL.EXECUTE_OVER);
			checkHeadService.update(checkHeadDO);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public int countOfOutByPK(Map<String, Object> map) {
		return checkProfitlossDao.countOfOutByPK(map);
	}


}
