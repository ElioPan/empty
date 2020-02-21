package com.ev.scm.service;

import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockOutItemDO;

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
	 * @param stockDOS 保存的库存列表
	 * @param params 前端扫码参数列表[{......,qrCodeId:1}{......,qrCodeId:2}]
	 */
	void saveInQrCode(List<StockDO> stockDOS, List<StockInItemDO> params);

	/**
	 * 出库后调用方法修改二维码信息以及保存二维码库存变更记录
	 * @param params 前端扫码参数列表[{......,qrCodeId:1,count:5}{......,qrCodeId:2,count:5}]
	 */
	void saveOutQrCode(List<StockOutItemDO> stockOutItems, List<StockOutItemDO> params);
}
