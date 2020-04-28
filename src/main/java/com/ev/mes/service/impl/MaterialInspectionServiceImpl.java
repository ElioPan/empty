package com.ev.mes.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.StringUtils;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.mes.dao.MaterialInspectionDao;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.mes.domain.MaterialInspectionDetailDO;
import com.ev.mes.service.MaterialInspectionDetailService;
import com.ev.mes.service.MaterialInspectionService;
import com.google.common.collect.Maps;

@Service
public class MaterialInspectionServiceImpl implements MaterialInspectionService {
	@Autowired
	private MaterialInspectionDao materialInspectionDao;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private MaterialInspectionDetailService materialInspectionDetailService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

	@Override
	public MaterialInspectionDO get(Long id) {
		return materialInspectionDao.get(id);
	}

	@Override
	public List<MaterialInspectionDO> list(Map<String, Object> map) {
		return materialInspectionDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return materialInspectionDao.count(map);
	}

	@Override
	public int save(MaterialInspectionDO materialInspection) {
		return materialInspectionDao.save(materialInspection);
	}

	@Override
	public int update(MaterialInspectionDO materialInspection) {
		return materialInspectionDao.update(materialInspection);
	}

	@Override
	public int remove(Long id) {
		return materialInspectionDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return materialInspectionDao.batchRemove(ids);
	}

	@Override
	public boolean isAudit(Long id) {
		MaterialInspectionDO materialInspectionDO = this.get(id);
		return Objects.equals(materialInspectionDO.getStatus(), ConstantForMES.OK_AUDITED);
	}

	@Override
	public int audit(Long id) {
		MaterialInspectionDO materialInspectionDO = get(id);
		materialInspectionDO.setAuditor(ShiroUtils.getUserId());
		materialInspectionDO.setStatus(ConstantForMES.OK_AUDITED);
		return this.update(materialInspectionDO);
	}

	@Override
	public int reverseAudit(Long id) {
		MaterialInspectionDO materialInspectionDO = get(id);
		materialInspectionDO.setStatus(ConstantForMES.WAIT_AUDIT);
		materialInspectionDO.setAuditor(null);
		return this.updateAll(materialInspectionDO);
	}
	@Override
	public int updateAll(MaterialInspectionDO materialInspectionDO) {
		return materialInspectionDao.updateAll(materialInspectionDO);
	}

	@Override
	public Map<String, Object> getDetailInfo(Long id) {
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id", id);
		params.put("offset", 0);
		params.put("limit", 1);
		List<Map<String, Object>> listForMap = this.listForMap(params);
		results.put("headInfo", listForMap.size() > 0 ? listForMap.get(0) : null);
		params.clear();
		params.put("headId", id);
		results.put("bodyInfo", materialInspectionDetailService.listForMap(params));
		return results;
	}

	@Override
	public R edit(MaterialInspectionDO inspectionDO, String childArray, Long[] ids) {
        if (this.isAudit(inspectionDO.getId())) {
            return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
        }
        if (ids.length > 0) {
            materialInspectionDetailService.batchRemove(ids);
        }
        if (StringUtils.isEmpty(childArray)) {
            this.update(inspectionDO);
            return R.ok();
        }
        int update = this.update(inspectionDO);
        List<MaterialInspectionDetailDO> bodys = JSON.parseArray(childArray, MaterialInspectionDetailDO.class);
        for (MaterialInspectionDetailDO materialInspectionDetailDO : bodys) {
            if (materialInspectionDetailDO.getId() != null) {
                materialInspectionDetailService.update(materialInspectionDetailDO);
                continue;
            }
            materialInspectionDetailDO.setHeadId(inspectionDO.getId());
            materialInspectionDetailService.save(materialInspectionDetailDO);
        }
        return update > 0 ? R.ok() : R.error();
	}

	@Override
	public BigDecimal getCountBySource(Map<String, Object> sourceParam) {
		return materialInspectionDao.getCountBySource(sourceParam);
	}

	@Override
	public int removeHeadAndBody(Long id) {
		int remove = this.remove(id);
		if (remove > 0) {
			materialInspectionDetailService.removeByHeadId(id);
		}
		return remove;
	}

	@Override
	public void batchRemoveHeadAndBody(Long[] ids) {
		for (Long id : ids) {
			this.removeHeadAndBody(id);
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return materialInspectionDao.listForMap(params);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> params) {
		return materialInspectionDao.countForMap(params);
	}

	@Override
	public R add(MaterialInspectionDO inspectionDO, String childArray) {
		List<MaterialInspectionDetailDO> bodys = JSON.parseArray(childArray, MaterialInspectionDetailDO.class);
		this.setInspectionNo(inspectionDO, inspectionDO.getInspectionType());
		inspectionDO.setStatus(ConstantForMES.WAIT_AUDIT);
		int save = this.save(inspectionDO);
		if (save > 0) {
			Long headId = inspectionDO.getId();
			for (MaterialInspectionDetailDO materialInspectionDetailDO : bodys) {
				materialInspectionDetailDO.setHeadId(headId);
				materialInspectionDetailService.save(materialInspectionDetailDO);
			}
            Map<String,Object> result = Maps.newHashMap();
            result.put("id", inspectionDO.getId());
            return R.ok(result);
		}
		return R.error();
	}

	@Override
	public void setInspectionNo(MaterialInspectionDO inspectionDO, Long inspectionType) {
		DictionaryDO dictionaryDO = dictionaryService.get(inspectionType);
		// 获取编号
		String maxNo = DateFormatUtil.getWorkOrderno(dictionaryDO.getValue());
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<MaterialInspectionDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getInspectionNo();
		}
		inspectionDO.setInspectionNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));

	}
}
