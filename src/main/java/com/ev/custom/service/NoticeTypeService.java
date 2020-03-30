package com.ev.custom.service;

import com.ev.custom.domain.NoticeTypeDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 15:28:16
 */
public interface NoticeTypeService {
	
	NoticeTypeDO get(Long id);
	
	List<NoticeTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeTypeDO noticeType);
	
	int update(NoticeTypeDO noticeType);

	int updateAll(NoticeTypeDO noticeType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
