package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.StockInDO;

import java.util.List;
import java.util.Map;

/**
 * 产品入库明细表主表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:37:49
 */
public interface StockInService {

	StockInDO get(Long id);



	List<StockInDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(StockInDO propurchaseInhead);

	int update(StockInDO propurchaseInhead);

	int remove(Long id);

	int batchRemove(Long[] ids);

	int dealOveraAudit(Long inHeadId);

	List<Map<String, Object>> getlistByIndata(Map<String, Object> map);

	List<Map<String, Object>> getHeadDetailById(Map<String, Object> map);

	/**
	 * 保存主子表信息
	 */
	Map<String, Object> dealWithMaster(StockInDO propurchaseInhead, String propurchaseInbodyDO);

	/**
	 * 处理审核
	 */
	String dealProcessAudit(StockInDO StockInDO, Long inHeadId, String code);

	/**
	 * 处理反审核
	 */
	String dealProcessCounterAudit(Long exitentryType, Long inHeadId);


	int countOfList(Map<String, Object> map);

	R addOtherIn(StockInDO StockInDO, String proInbodyList);

	R addAuditStatus(Long inHeadId, Long auditor);

	R changeOtherAudit(Long inHeadId);

	R editOtherStorages(StockInDO StockInDO, String proOldInBodyDO, String proNewInBodyDO, Long[] inBodyIds, Long inHeadId);

	R remoceOtherWaitAuite(Long inHeadId);

	R otherHeadDetailList(Map<String, Object> params);


	/**
	 * =================================================以下为优化后代码=========================
	 */

	R addAndChangeInStockType(StockInDO StockInDO, Long inStockTypeId, String detailBody, Long[] ItemIds);

	R auditAllTypeInStock(Long Id, Long auditor,Long storageTypeId);

	R disAuditInStock(Long Id, Long type);

	R deleBatch(Long[] ids);

	Map<String, Object> countForMap(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> deatilOfhead(Map<String, Object> map);

	int wetherHaveQrSign(Map<String, Object> map);

	List<Map<String, Object>>  getStockInDate(Map<String, Object> map);

	String checkSourceCounts(String stockInitemDos, Long storageType);





}