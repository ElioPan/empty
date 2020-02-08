package com.ev.mes.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ev.mes.domain.MaterialInspectionDetailDO;

/**
 * 物料检验详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:35:57
 */
@Mapper
public interface MaterialInspectionDetailDao {

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
	 * @return
	 */
	int removeByHeadId(Long id);
}
