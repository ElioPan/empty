package com.ev.custom.service;

import com.ev.custom.domain.NoticeAssocDO;
import com.ev.scm.domain.QrcodeItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 15:30:06
 */
public interface NoticeAssocService {
	
	NoticeAssocDO get(Long id);
	
	List<NoticeAssocDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeAssocDO noticeAssoc);
	
	int update(NoticeAssocDO noticeAssoc);

	int updateAll(NoticeAssocDO noticeAssoc);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchInsert(List<NoticeAssocDO> noticeAssocDOList);
}
