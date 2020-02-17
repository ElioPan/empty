package com.ev.custom.service;

import com.ev.custom.domain.NoticeDO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

/**
 * 通知单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-11 17:00:08
 */
public interface NoticeService {
	
	NoticeDO get(Long id);
	
	List<NoticeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeDO notice);
	
	int update(NoticeDO notice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	void saveAndSendSocket(String title, String content,String contentDetail, Long type, Long fromUser, Long toUser);
}
