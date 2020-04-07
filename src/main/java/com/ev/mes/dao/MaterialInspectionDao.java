package com.ev.mes.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ev.mes.domain.MaterialInspectionDO;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:34:12
 */
@Mapper
public interface MaterialInspectionDao {

	MaterialInspectionDO get(Long id);

	List<MaterialInspectionDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(MaterialInspectionDO materialInspection);

	int update(MaterialInspectionDO materialInspection);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	Map<String, Object> countForMap(Map<String, Object> params);

    BigDecimal getCountBySource(Map<String, Object> sourceParam);
}
