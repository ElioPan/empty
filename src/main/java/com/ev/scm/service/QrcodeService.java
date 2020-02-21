package com.ev.scm.service;

import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.domain.StockDO;

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

	/**
	 * 入库后调用方法保存二维码信息
	 * @param stockDOS
	 * @param params
	 */
	void saveInQrCode(List<StockDO> stockDOS, List<Map<String,Object>> params);

	/**
	 * 出库后调用方法修改二维码信息以及保存二维码库存变更记录
	 * @param params
	 */
	void saveOutQrCode(List<Map<String,Object>> params);
}
