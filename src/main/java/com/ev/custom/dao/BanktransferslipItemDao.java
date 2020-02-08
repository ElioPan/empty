package com.ev.custom.dao;

import com.ev.custom.domain.BanktransferslipItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-08 13:11:30
 */
@Mapper
public interface BanktransferslipItemDao {

	List<Map<String,Object>> get(Long tid);
	
	List<BanktransferslipItemDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(BanktransferslipItemDO banktransferslipItem);
	
	int update(BanktransferslipItemDO banktransferslipItem);
	
	int remove(Long tid);
	
	int batchRemove(Long[] tids);
}
