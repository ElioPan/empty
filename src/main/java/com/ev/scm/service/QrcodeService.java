package com.ev.scm.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.scm.domain.*;

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

	int batchInsert(List<QrcodeDO> qrcodeDOList);
	
	int update(QrcodeDO qrcode);

	int batchUpdate(List<QrcodeDO> qrcodeDOList);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	/**
	 * 判断二维码是否不符合单据类型
	 * @param inType 0：采购入库，1：生产入库，2：委外入库，3：其他入库
	 * @return 返回结果
	 */
	Boolean isMultipleType(Integer inType, MaterialInspectionDO materialInspectionDO);

	/**
	 * 判断是否多单入库
	 * @param isMultiple 1：是，0：否
	 * @param contractNo 合同编号
	 * @param materialInspectionDO 检验单
	 * @return
	 */
	Boolean isMultipleIn(Integer isMultiple, String contractNo, MaterialInspectionDO materialInspectionDO);

	/**
	 * 入库后调用方法保存二维码信息
	 * @param stockInDO 入库主表信息
	 * @param stockDOS 保存的库存列表
	 * @param params 前端扫码参数列表[{......,qrCodeId:1}{......,qrCodeId:2}]
	 */
	void saveInQrCode(StockInDO stockInDO, List<StockDO> stockDOS, List<StockInItemDO> params);

	/**
	 * 出库后调用方法修改二维码信息以及保存二维码库存变更记录
	 * @param params 前端扫码参数列表[{......,qrCodeId:1,count:5}{......,qrCodeId:2,count:5}]
	 */
	void saveOutQrCode(List<StockOutItemDO> stockOutItems, List<StockOutItemDO> params);

	/**
	 * 库存调拨后二维码修改逻辑
	 * @param stockDOS 调入的库存列表
	 * @param allotItemDOS 前端扫码参数列表
	 */
	void transferHandler(List<StockDO> stockDOS, List<AllotItemDO> allotItemDOS);
}
