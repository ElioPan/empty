package com.ev.scm.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.scm.dao.QrcodeDao;
import com.ev.scm.domain.*;
import com.ev.scm.service.QrcodeItemService;
import com.ev.scm.service.QrcodeService;
import com.ev.scm.service.StockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class QrcodeServiceImpl implements QrcodeService {
    @Autowired
    private QrcodeDao qrcodeDao;

    @Autowired
    private StockOutService stockOutService;

    @Autowired
    private QrcodeItemService qrcodeItemService;

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
    public int batchInsert(List<QrcodeDO> qrcodeDOList) {
        return qrcodeDao.batchInsert(qrcodeDOList);
    }

    @Override
    public int update(QrcodeDO qrcode) {
        return qrcodeDao.update(qrcode);
    }

    @Override
    public int batchUpdate(List<QrcodeDO> qrcodeDOList) {
        return qrcodeDao.batchUpdate(qrcodeDOList);
    }

    @Override
    public int remove(Long id) {
        return qrcodeDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return qrcodeDao.batchRemove(ids);
    }

    @Override
    public Boolean isMultipleType(Integer inType, MaterialInspectionDO materialInspectionDO) {
        if(inType==0){
            if(!Objects.equals(materialInspectionDO.getSourceType(), ConstantForGYL.CGHT)){
                return false;
            }
        }else if(inType==1){
            /*if(!Objects.equals(materialInspectionDO.getSourceType(), ConstantForGYL.)){
                return false;
            }*/
        }else if(inType==2){
            if(!Objects.equals(materialInspectionDO.getSourceType(), ConstantForGYL.WWHT)){
                return false;
            }
        }else if(inType==3){
            //TODO
        }
        return true;
    }

    /**
     * 判断是否多单入库
     * @param isMultiple 1：是，0：否
     * @param contractNo 合同编号
     * @param materialInspectionDO 检验单
     * @return 返回结果
     */
    @Override
    public Boolean isMultipleIn(Integer isMultiple, String contractNo, MaterialInspectionDO materialInspectionDO) {
        if(isMultiple==0 && !Objects.equals(contractNo,materialInspectionDO.getSourceNo())){
            return false;
        }
        return true;
    }

    /**
	 * 入库后调用方法保存二维码信息
	 * @param stockDOS 保存的库存列表
	 * @param params 前端扫码参数列表[{......,qrCodeId:1}{......,qrCodeId:2}]
	 */
    @Override
    public void saveInQrCode(List<StockDO> stockDOS, List<StockInItemDO> params) {
        List<QrcodeDO> qrcodeDOList = new ArrayList<>();
        /**
         * 整理数量，批量更新
         */
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
                    qrcodeDOList.add(qrcodeDO);
                    break;
                }
            }
        }
        batchUpdate(qrcodeDOList);
    }

	/**
	 *
	 * @param params 前端扫码参数列表[{......,qrCodeId:1,count:5}{......,qrCodeId:2,count:5}]
	 */
	@Override
    public void saveOutQrCode(List<StockOutItemDO> stockOutItems, List<StockOutItemDO> params) {
        List<QrcodeDO> qrcodeDOList = new ArrayList<>();
        List<QrcodeItemDO> qrcodeItemDOList = new ArrayList<>();
        /**
         * 获取出库类型
         */
        StockOutDO stockOutDO = stockOutService.get(stockOutItems.get(0).getOutId());
		for(StockOutItemDO stockOutItemDO : params){
		    Long qrCodeId = stockOutItemDO.getQrcodeId();
            QrcodeDO qrcodeDO = qrcodeDao.get(qrCodeId);
			for(StockOutItemDO stockOutItemDOSaved : stockOutItems){
                if(Arrays.asList(stockOutItemDOSaved.getStockId().split(",")).contains(qrcodeDO.getStockId())){
                    qrcodeDO.setRemainCount(qrcodeDO.getRemainCount().subtract(stockOutItemDO.getCount()));
                    qrcodeDOList.add(qrcodeDO);
                    QrcodeItemDO qrcodeItemDO = new QrcodeItemDO(qrCodeId, stockOutDO.getOutboundType(), stockOutItemDOSaved.getOutId(), stockOutItemDOSaved.getId(), stockOutItemDO.getCount());
                    qrcodeItemDOList.add(qrcodeItemDO);
                    break;
                }

			}
		}
		batchUpdate(qrcodeDOList);
		qrcodeItemService.batchInsert(qrcodeItemDOList);
    }

    @Override
    public void transferHandler(List<StockDO> stockDOS, List<AllotItemDO> allotItemDOS) {
        //TODO

    }

}
