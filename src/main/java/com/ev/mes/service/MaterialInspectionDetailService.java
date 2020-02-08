package com.ev.mes.service;

import java.util.List;
import java.util.Map;

import com.ev.mes.domain.MaterialInspectionDetailDO;

/**
 * 物料检验详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:35:57
 */
public interface MaterialInspectionDetailService {

	MaterialInspectionDetailDO get(Long id);

	List<MaterialInspectionDetailDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(MaterialInspectionDetailDO materialInspectionDetail);

	int update(MaterialInspectionDetailDO materialInspectionDetail);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> listForMap(Map<String, Object> params);

	/**
	 * @param id
	 */
	int removeByHeadId(Long id);
}
