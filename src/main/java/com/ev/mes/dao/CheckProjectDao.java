package com.ev.mes.dao;

import com.ev.mes.domain.CheckProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 检验项目
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:31
 */
@Mapper
public interface CheckProjectDao {

	CheckProjectDO get(Long id);
	
	List<CheckProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CheckProjectDO checkProject);
	
	int update(CheckProjectDO checkProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	int deletOfPro(Map<String,Object> map);


}
