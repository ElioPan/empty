package com.ev.scm.service;

import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockInDO;

import java.util.List;
import java.util.Map;

/**
 * 产品入库明细表子表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:37:59
 */
public interface StockInItemService {

	StockInItemDO get(Long id);

	List<StockInItemDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(StockInItemDO propurchaseInbody);

	int update(StockInItemDO propurchaseInbody);

	int remove(Long id);

	int batchRemove(Long[] ids);

	String getProByHeadId(Long id, String code);

	int removeByInHeadId(Long[] id);

	String addOtherAudit(Long id);

	int updateChangeDetails(String proOldInBodyDO, String proNewInBodyDO, Long[] deletIds, Long headId);

	List<Map<String, Object>> getBodyDetalByHeadId(Map<String, Object> map);

	void ditalWithChange(StockInDO stockInDO, String proListInBodyDO, Long headId);

	int countOfList(Map<String, Object> map);

	List<Map<String, Object>> getlistForMap(Map<String, Object> map);

	List<Map<String, Object>> deatilOfBody(Map<String, Object> map);

	String addAllTypeInStock(Long id,Long Type);

}
