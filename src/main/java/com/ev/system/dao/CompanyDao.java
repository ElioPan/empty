package com.ev.system.dao;

import com.ev.system.domain.CompanyDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 公司信息表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:02:10
 */
@Mapper
public interface CompanyDao {

	Map<String,Object> get(Long id);
	
	List<CompanyDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CompanyDO company);
	
	int update(CompanyDO company);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	CompanyDO getServiceCode(Long id);
}
