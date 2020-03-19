package com.ev.scm.dao;

import com.ev.scm.domain.OtherReceivablesDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 其他应收应付单主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 16:27:52
 */
@Mapper
public interface OtherReceivablesDao {

	OtherReceivablesDO get(Long id);
	
	List<OtherReceivablesDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OtherReceivablesDO otherReceivables);
	
	int update(OtherReceivablesDO otherReceivables);

	int updateAll(OtherReceivablesDO otherReceivables);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int countCanDelte(Map<String, Object> map);

	Map<String, Object> getHeadDetail(Long id);

	Map<String, Object> countForMap(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);


}
