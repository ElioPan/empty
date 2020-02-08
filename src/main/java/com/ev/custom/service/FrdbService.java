package com.ev.custom.service;

import com.ev.custom.domain.FrdbDO;

import java.util.List;
import java.util.Map;

/**
 * 故障库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 14:49:11
 */
public interface FrdbService {
	
	FrdbDO get(Long id);
	
	List<FrdbDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FrdbDO frdb);
	
	int update(FrdbDO frdb);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);


	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	Map<String,Object> detail(Long id);

	void add(FrdbDO frdb, String[] taglocationappearanceImage);

	void edit(FrdbDO frdb, String[] taglocationappearanceImage,String[] deletetag_appearanceImage);
}
