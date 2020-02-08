package com.ev.custom.dao;

import com.ev.custom.domain.PaydetailsDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-29 16:36:57
 */
@Mapper
public interface PaydetailsDao {

	Map<String,Object> get(Long id);
	
	List<PaydetailsDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PaydetailsDO paydetails);
	
	int update(PaydetailsDO paydetails);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
