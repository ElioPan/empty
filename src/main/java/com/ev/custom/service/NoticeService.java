package com.ev.custom.service;

import com.ev.custom.domain.NoticeDO;

import java.util.List;
import java.util.Map;

/**
 * 通知单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-26 12:53:30
 */
public interface NoticeService {
	
	NoticeDO get(Long id);
	
	List<NoticeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeDO notice);
	
	int update(NoticeDO notice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
