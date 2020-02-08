package com.ev.scm.dao;

import com.ev.scm.domain.SalesbillDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-30 13:24:10
 */
@Mapper
public interface SalesbillDao {

    SalesbillDO get(Long id);
	
	List<SalesbillDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SalesbillDO salesbill);

	int update(SalesbillDO salesbill);

	int remove(Long id);

	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    Map<String,Object> getDetail(Long id);
}
