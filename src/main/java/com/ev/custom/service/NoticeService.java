package com.ev.custom.service;

import com.ev.custom.domain.NoticeDO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 通知单
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 14:40:59
 */
public interface NoticeService {
	NoticeDO get(Long id);

	List<NoticeDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int save(NoticeDO notice);

	int update(NoticeDO notice);

	int remove(Long id);

	int batchRemove(Long[] ids);

	void saveAndSendSocket(String title, String content, Long billId, String contentDetail, Long type, Long fromUser, List<Long> toUsers) throws IOException, ParseException;
}
