package com.ev.scm.service;

import com.ev.scm.domain.QrcodeDO;

import java.util.List;
import java.util.Map;

/**
 * 库存二维码
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 15:12:03
 */
public interface QrcodeService {
	
	QrcodeDO get(Long id);
	
	List<QrcodeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(QrcodeDO qrcode);
	
	int update(QrcodeDO qrcode);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
