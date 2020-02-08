package com.ev.custom.dao;

import com.ev.custom.domain.BanktransferslipDO;

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
public interface BanktransferslipDao {

	Map<String,Object> get(Long id);
	
	List<BanktransferslipDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(BanktransferslipDO banktransferslip);
	
	int update(BanktransferslipDO banktransferslip);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int audit(Long id);
	
	int reverseAudit(Long id);
	
	List<Map<String,Object>> listApi(Map<String,Object> map);
	
	int countApi(Map<String,Object> map);
}
