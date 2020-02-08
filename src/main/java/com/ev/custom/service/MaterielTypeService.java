package com.ev.custom.service;

import com.ev.custom.domain.MaterielTypeDO;

import java.util.List;
import java.util.Map;

/**
 * 物料类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-08 14:33:15
 */
public interface MaterielTypeService {
	
	MaterielTypeDO get(Integer id);
	
	List<MaterielTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterielTypeDO materielType);
	
	int update(MaterielTypeDO materielType);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int checkSave(MaterielTypeDO materielType);

	int checkDelete(Integer[] ids);

    List<Map<String,Object>> listForMap(Map<String, Object> params);
}
