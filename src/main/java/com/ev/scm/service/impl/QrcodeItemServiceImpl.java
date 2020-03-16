package com.ev.scm.service.impl;

import com.ev.scm.domain.StockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ev.scm.dao.QrcodeItemDao;
import com.ev.scm.domain.QrcodeItemDO;
import com.ev.scm.service.QrcodeItemService;



@Service
public class QrcodeItemServiceImpl implements QrcodeItemService {
	@Autowired
	private QrcodeItemDao qrcodeItemDao;
	
	@Override
	public QrcodeItemDO get(Long id){
		return qrcodeItemDao.get(id);
	}
	
	@Override
	public List<QrcodeItemDO> list(Map<String, Object> map){
		return qrcodeItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return qrcodeItemDao.count(map);
	}
	
	@Override
	public int save(QrcodeItemDO qrcodeItem){
		return qrcodeItemDao.save(qrcodeItem);
	}
	
	@Override
	public int update(QrcodeItemDO qrcodeItem){
		return qrcodeItemDao.update(qrcodeItem);
	}
	
	@Override
	public int remove(Long id){
		return qrcodeItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return qrcodeItemDao.batchRemove(ids);
	}

	@Override
	public int batchInsert(List<QrcodeItemDO> qrcodeItemDOList) {
		return qrcodeItemDao.batchInsert(qrcodeItemDOList);
	}

	/**
	 * 获取二维码历史明细
	 *
	 * @param qrCodeId 二维码主键
	 * @return
	 */
	@Override
	public List<Map<String, Object>> logDetail(Long qrCodeId) {
		List<QrcodeItemDO> qrcodeItemDOList = list(new HashMap<String,Object>(){{put("qrcodeId",qrCodeId);put("sort","create_time");put("order","asc");}});
		List<Map<String,Object>> qrCodeItemDos = new ArrayList<>();
		for(QrcodeItemDO qrcodeItemDO: qrcodeItemDOList){
			if(qrcodeItemDO.getOperateType()==0){
				Map<String,Object> map = new HashMap<>();
				map.put("dateType","入库日期");
				map.put("createTime",qrcodeItemDO.getCreateTime());
				map.put("countType","入库数量");
				map.put("count",qrcodeItemDO.getCount());
				qrCodeItemDos.add(map);
			}else{
				Map<String,Object> map = new HashMap<>();
				map.put("dateType","出库日期");
				map.put("createTime",qrcodeItemDO.getCreateTime());
				map.put("countType","出库数量");
				map.put("count",qrcodeItemDO.getCount());
				qrCodeItemDos.add(map);
			}
		}
		return qrCodeItemDos;
	}
}
