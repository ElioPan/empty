package com.ev.custom.dao;

import com.ev.custom.domain.NoticeAssocDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 15:30:06
 */
@Mapper
public interface NoticeAssocDao {

	NoticeAssocDO get(Long id);
	
	List<NoticeAssocDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeAssocDO noticeAssoc);

	int batchInsert(List<NoticeAssocDO> noticeAssocDOList);
	
	int update(NoticeAssocDO noticeAssoc);

	int updateAll(NoticeAssocDO noticeAssoc);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
