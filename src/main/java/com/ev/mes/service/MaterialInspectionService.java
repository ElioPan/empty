package com.ev.mes.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ev.framework.utils.R;
import com.ev.mes.domain.MaterialInspectionDO;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:34:12
 */
public interface MaterialInspectionService {

	MaterialInspectionDO get(Long id);

	List<MaterialInspectionDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(MaterialInspectionDO materialInspection);

	int update(MaterialInspectionDO materialInspection);

	int updateAll(MaterialInspectionDO materialInspection);

	int remove(Long id);

	int batchRemove(Long[] ids);

	boolean isAudit(Long id);

	int audit(Long id);

	int reverseAudit(Long id);

	Map<String, Object> getDetailInfo(Long id);

	int removeHeadAndBody(Long id);

	void batchRemoveHeadAndBody(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	Map<String, Object> countForMap(Map<String, Object> params);

	void setInspectionNo(MaterialInspectionDO inspectionDO, Integer inspectionType);

	R add(MaterialInspectionDO inspectionDO, String childArray);

	R edit(MaterialInspectionDO inspectionDO, String childArray, Long[] ids);

    BigDecimal getCountBySource(Map<String, Object> sourceParam);
}
