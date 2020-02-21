package com.ev.scm.service.impl;

import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockOutItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.scm.dao.QrcodeDao;
import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.service.QrcodeService;


@Service
public class QrcodeServiceImpl implements QrcodeService {
    @Autowired
    private QrcodeDao qrcodeDao;

    @Override
    public QrcodeDO get(Long id) {
        return qrcodeDao.get(id);
    }

    @Override
    public List<QrcodeDO> list(Map<String, Object> map) {
        return qrcodeDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return qrcodeDao.count(map);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return qrcodeDao.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return qrcodeDao.countForMap(map);
    }

    @Override
    public int save(QrcodeDO qrcode) {
        return qrcodeDao.save(qrcode);
    }

    @Override
    public int update(QrcodeDO qrcode) {
        return qrcodeDao.update(qrcode);
    }

    @Override
    public int remove(Long id) {
        return qrcodeDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return qrcodeDao.batchRemove(ids);
    }

	/**
	 * 入库后调用方法保存二维码信息
	 * @param stockDOS 保存的库存列表
	 * @param params 前端扫码参数列表[{......,qrCodeId:1}{......,qrCodeId:2}]
	 */
    @Override
    public void saveInQrCode(List<StockDO> stockDOS, List<StockInItemDO> params) {
        for (StockInItemDO stockInItemDO : params) {
            for (StockDO stockDO : stockDOS) {
                /**
                 * 库位相同，物料相同，批号相同的分组
                 */
                if (Objects.equals(stockDO.getMaterielId(), stockInItemDO.getMaterielId()) &&
                        (stockDO.getWarehouse() + "-" + stockDO.getWarehLocation()).equals(stockInItemDO.getWarehouse() + "-" + stockInItemDO.getWarehLocation()) &&
                        stockDO.getBatch().equals(stockInItemDO.getBatch())) {
                    QrcodeDO qrcodeDO = qrcodeDao.get(stockInItemDO.getQrcodeId());
                    qrcodeDO.setStockId(stockDO.getId());
                    qrcodeDao.update(qrcodeDO);
                    break;
                }
            }
        }
    }

	/**
	 *
	 * @param params 前端扫码参数列表[{......,qrCodeId:1,count:5}{......,qrCodeId:2,count:5}]
	 */
	@Override
    public void saveOutQrCode(List<StockOutItemDO> stockOutItems, List<StockOutItemDO> params) {
		for(StockOutItemDO stockOutItemDO : params){
			for(StockOutItemDO stockOutItemDOSaved : stockOutItems){
				//循环更新相应的数据
			}

		}
    }

}
