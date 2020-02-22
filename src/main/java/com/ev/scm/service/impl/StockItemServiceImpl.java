package com.ev.scm.service.impl;

import com.ev.scm.dao.StockItemDao;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.service.StockItemService;
import com.ev.scm.service.StockService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@EnableTransactionManagement
@Service
public class StockItemServiceImpl implements StockItemService {
	@Autowired
	private StockItemDao stockItemDao;
	@Autowired
	private StockService stockService;

	@Override
	public StockItemDO get(Long id) {
		return stockItemDao.get(id);
	}

	@Override
	public List<StockItemDO> list(Map<String, Object> map) {
		return stockItemDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return stockItemDao.count(map);
	}

	@Override
	public int save(StockItemDO stockDetail) {
		return stockItemDao.save(stockDetail);
	}

	@Override
	public int update(StockItemDO stockDetail) {
		return stockItemDao.update(stockDetail);
	}

	@Override
	public int remove(Long id) {
		return stockItemDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return stockItemDao.batchRemove(ids);
	}

	@Override
	public int getStockIdByHeadId(Long exitentryType,Long inheadId) {

		Map<String,Object> peamry = new HashedMap();
		peamry.put("inheadId",inheadId);
		peamry.put("exitentryType",exitentryType);

		List<Long> stockIdList = stockItemDao.getStockIdByHeadId(peamry);
		if (stockIdList.size() > 0) {
			for (Long idL : stockIdList) {
				int counts = this.countOfStockId(idL);
				if ( counts > 1) {
					return -1; //不允许反审核
				}
			}
			return 1;//允许反审核
		} else {
			return 0;
		}
	}

	@Override
	public int getStockIdByHeadIds(Long exitentryType,Long inheadId) {

		Map<String,Object> peamry = new HashedMap();
			peamry.put("inheadId",inheadId);
			peamry.put("exitentryType",exitentryType);

		List<Long> stockIdList = stockItemDao.getStockIdByHeadId(peamry);

		if (stockIdList.size() > 0) {
			Long ids[]=new Long[stockIdList.size()];
			for (int i=0;i<stockIdList.size() ;i++) {
				ids[i]=stockIdList.get(i);
			}
			peamry.clear();
			peamry.put("id",ids);
			int rows=stockItemDao.countOfStockIds(peamry);
			if ( rows == stockIdList.size()) {
				return 1;//允许反审核

			}else{
				return -1; //不允许反审核
			}
		} else {
			return 0;
		}
	}

	@Override
	public int batchSave(List<StockItemDO> stockItemDOs) {
		return stockItemDao.batchSave(stockItemDOs);
	}


	@Override
	public int countOfStockId(Long id) {
		return stockItemDao.countOfStockId(id);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int dealDateOveraAudite(Long exitentryType, Long inHeeadId) {

		try {
			Map<String, Object> peamry = new HashedMap();
			peamry.put("inheadId", inHeeadId);
			peamry.put("exitentryType", exitentryType);

			//获取stockid，
			List<Long> stockIdList = stockItemDao.getStockIdByHeadId(peamry);

			if (stockIdList.size() > 0) {

				Long[] longs = new Long[stockIdList.size()];

				for (int i = 0; i < stockIdList.size(); i++) {
					longs[i] = stockIdList.get(i);
				}
				//删除stock数据
				stockService.batchRemove(longs);
				//删除明细表
				stockItemDao.removeByInheadId(peamry);

				return 1; //删除成功
			} else {
				return 0;//删除失败
			}
		} catch (Exception e) {
			return 0;//删除失败
		}
	}

	@Override
	public int removeByInheadId(Long exitentryType,Long id) {

		Map<String,Object> peamry = new HashedMap();
			peamry.put("inheadId",id);
			peamry.put("exitentryType",exitentryType);

		return stockItemDao.removeByInheadId(peamry);
	}

}