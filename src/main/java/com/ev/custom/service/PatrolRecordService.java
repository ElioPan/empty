package com.ev.custom.service;

import com.ev.custom.domain.PatrolRecordDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 巡检记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
public interface PatrolRecordService {
	
	PatrolRecordDO get(Long id);
	
	List<PatrolRecordDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	int save(PatrolRecordDO patrolRecord);
	
	int update(PatrolRecordDO patrolRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<String,Object> recordDetail(Long id);

	List<Map<String,Object>> listApi(Map<String, Object>params);

    Map<String,Object> addRecord(PatrolRecordDO record, String detailArray, String[] signInImage, String[] taglocationappearanceImage);

    Map<String,Object> handleRecord(PatrolRecordDO record,String content, String detailArray,String[] taglocationappearanceImage,String []signInImage);

//    Map<String,Object> checkRecord(PatrolCheckDO check, PatrolRecordDO record);

	List<Map<String,Object>> recordList(Map<String, Object> params);

//	PatrolRecordDO getByPlanId(Long id);

//	Map<String, Object> addRecordByPlan(Long inFormId,Long patrolPlanId);
	
	Map<String,Object> params(Map<String,Object> params);

	Map<String,Object> recordCheckDetail(Long id);

	/**
	 * 关闭巡检单
	 */
	int closeRecord(Long id, String closeReason);

	/**
	 * 批量关闭巡检单
	 */
	int closeRecord(Long[] ids, String closeReason);

	/**
	 * 通过巡检计划单生成巡检记录单
	 */
	Map<String, Object> addRecordByPlan(Long planId, Date beginTime, Date endTime);

	/**
	 * 删除巡检图片
	 */
	void removeImage(Long id);

	/**
	 * 
	 * 保存填写的巡检记录
	 */
	Map<String, Object> saveHandleRecord(String content, String detailArray, String[] signInImage,
			String[] taglocationappearanceImage, PatrolRecordDO record);

	/**
	 * 保存新增的巡检记录
	 */
	Map<String, Object> saveRecord(PatrolRecordDO record, String detailArray, String[] signInImage,
			String[] taglocationappearanceImage);

	/**
	 * 获取巡检待处理的数量（主页使用）
	 */
	Map<String, Object> getBacklog(Long userId, Long deptId);
}
