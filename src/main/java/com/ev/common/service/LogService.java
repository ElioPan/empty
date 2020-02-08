package com.ev.common.service;

import org.springframework.stereotype.Service;

import com.ev.common.domain.LogDO;
import com.ev.common.domain.PageDO;
import com.ev.framework.utils.Query;
@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
