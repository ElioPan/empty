package com.ev.mes.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ev.mes.domain.BomDetailDO;

/**
 * BOM子物料表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:34
 */
@Mapper
public interface BomDetailDao {

	BomDetailDO get(Long id);

	List<BomDetailDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(BomDetailDO bomDetail);

	int update(BomDetailDO bomDetail);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	/**
	 * @param id
	 * @return
	 */
	int removeByHeadId(Long id);
}
