package com.ev.custom.dao;

import com.ev.custom.domain.QualityReportDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:15:40
 */
@Mapper
public interface QualityReportDao {

	QualityReportDO get(Long id);
	
	List<QualityReportDO> list(Map<String,Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	int count(Map<String,Object> map);
	
	int save(QualityReportDO qualityReport);
	
	int update(QualityReportDO qualityReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
