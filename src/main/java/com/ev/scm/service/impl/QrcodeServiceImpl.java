package com.ev.scm.service.impl;

import com.ev.scm.domain.StockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.QrcodeDao;
import com.ev.scm.domain.QrcodeDO;
import com.ev.scm.service.QrcodeService;



@Service
public class QrcodeServiceImpl implements QrcodeService {
	@Autowired
	private QrcodeDao qrcodeDao;
	
	@Override
	public QrcodeDO get(Long id){
		return qrcodeDao.get(id);
	}
	
	@Override
	public List<QrcodeDO> list(Map<String, Object> map){
		return qrcodeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
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
	public int save(QrcodeDO qrcode){
		return qrcodeDao.save(qrcode);
	}
	
	@Override
	public int update(QrcodeDO qrcode){
		return qrcodeDao.update(qrcode);
	}
	
	@Override
	public int remove(Long id){
		return qrcodeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return qrcodeDao.batchRemove(ids);
	}

	@Override
	public void saveInQrCode(List<StockDO> stockDOS, List<Map<String, Object>> params) {

	}

	@Override
	public void saveOutQrCode(List<Map<String, Object>> params) {

	}

}
