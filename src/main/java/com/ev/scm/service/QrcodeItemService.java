package com.ev.scm.service;

import com.ev.scm.domain.QrcodeItemDO;
import com.ev.scm.domain.StockDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 14:28:20
 */
public interface QrcodeItemService {
	
	QrcodeItemDO get(Long id);
	
	List<QrcodeItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(QrcodeItemDO qrcodeItem);
	
	int update(QrcodeItemDO qrcodeItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
