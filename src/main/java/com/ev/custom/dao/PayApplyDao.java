package com.ev.custom.dao;

import com.ev.custom.domain.PayApplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 付款申请
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:36
 */
@Mapper
public interface PayApplyDao {

	PayApplyDO get(Long id);
	
	List<PayApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(PayApplyDO payApply);
	
	int update(PayApplyDO payApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> getPayDetail (Map<String, Object> map);
}
