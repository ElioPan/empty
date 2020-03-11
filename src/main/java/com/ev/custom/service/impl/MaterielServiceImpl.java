package com.ev.custom.service.impl;

import com.ev.custom.dao.MaterielDao;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.domain.StockDO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class MaterielServiceImpl implements MaterielService {
	@Autowired
	private MaterielDao materielDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public MaterielDO get(Integer id){
		return materielDao.get(id);
	}
	
	@Override
	public List<MaterielDO> list(Map<String, Object> map){
		return materielDao.list(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return materielDao.listForMap(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return materielDao.count(map);
	}
	
	@Override
	public int save(MaterielDO materiel){
	    materiel.setAuditSign(ConstantForMES.WAIT_AUDIT);
		return materielDao.save(materiel);
	}
	
	@Override
	public int update(MaterielDO materiel){
		return materielDao.update(materiel);
	}
	
	@Override
	public int remove(Integer id){
		return materielDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return materielDao.batchRemove(ids);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return materielDao.countForMap(params);
	}

	@Override
	public Map<String, Object> getDetail(Integer id) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
		param.put("id", id);
		List<Map<String, Object>> listForMap = materielDao.listForMap(param);
		if (listForMap.size() > 0) {
			Map<String, Object> map = listForMap.get(0);
			map.put("isUse", this.checkUse(id));
			return map;
		}
		return null;
	}

	@Override
	public int checkSave(MaterielDO materiel) {
		return materielDao.checkSave(materiel);
	}
	
	@Override
	public List<Map<String, Object>> stockListForMap(Map<String, Object> map) {
		return materielDao.stockListForMap(map);
	}

	@Override
	public int stockCountForMap(Map<String, Object> map) {
		return materielDao.stockCountForMap(map);
	}

	@Override
	public List<StockDO> stockList(List<Long> stockIds) {
		return materielDao.stockList(stockIds);
	}

	@Override
	public int checkUse(Integer id) {
		return materielDao.checkUse(id);
	}
	
	@Override
	public List<Map<String, Object>> stockCount(Map<String, Object> map) {
		return materielDao.stockCount(map);
	}

	@Override
	public R logicRemove(Integer id) {
		if (this.get(id).getAuditSign().equals(ConstantForMES.OK_AUDITED)) {
			return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
		}
		if (this.checkUse(id) > 0) {
			return R.error(messageSourceHandler.getMessage("common.delete.disabled", null));

		}
		MaterielDO materielDO = new MaterielDO();
		materielDO.setId(id);
		materielDO.setDelFlag(1);
		return this.update(materielDO)>0?R.ok():R.error();
	}

	@Override
	public R logicBatchRemove(Integer[] ids) {
		int count = 0;
		for (Integer id : ids) {
			if (this.get(id).getAuditSign().equals(ConstantForMES.OK_AUDITED)) {
				return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
			}
			if (this.checkUse(id) > 0) {
				return R.error(messageSourceHandler.getMessage("common.delete.disabled", null));
			}
		}
		MaterielDO materielDO;
		for (Integer id : ids) {
			materielDO = new MaterielDO();
			materielDO.setId(id);
			materielDO.setDelFlag(1);
			count += this.update(materielDO);
		}
		return count == ids.length?R.ok():R.error();
	}

    @Override
    public int audit(Integer id) {
        MaterielDO materielDO = new MaterielDO();
        materielDO.setId(id);
        materielDO.setAuditSign(ConstantForMES.OK_AUDITED);
        materielDO.setAuditor(ShiroUtils.getUserId());
        return this.update(materielDO);
    }

    @Override
    public int reverseAudit(Integer id) {
        MaterielDO materielDO = new MaterielDO();
        materielDO.setId(id);
        materielDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
        return this.update(materielDO);
    }

    @Override
    public List<String> getAllCode() {
        return materielDao.getAllCode();
    }

    @Override
    public void batchSave(List<MaterielDO> materielDOs) {
        materielDao.batchSave(materielDOs);
    }

}
