package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.TaskMainDO;
import com.ev.custom.domain.TaskReplyDO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 任务
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-08 13:03:04
 */
public interface TaskMainService {
	
	TaskMainDO get(Long id);
	
	List<Map<String, Object>> getTaskReplyInfo(Long id, Long status);
	
	List<TaskMainDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	R saveTaskInfo(TaskMainDO taskMain, Long[] ccList, Long heldPerson, Long checkPerson, String linkOrderNo,
			Long linkOrderType, Long linkStageType, String[] taglocationappearanceImage) throws IOException, ParseException;
	
	Map<String,Object> detail(Long id);

	int count(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	int countBackLog(Map<String, Object> params);
	
	List<Map<String, Object>> countWeekBackLog(Map<String, Object> params);
	
	void getUserWaitingCount(Long userId, Long status, String idPath, Map<String, Object> results);
	
	int save(TaskMainDO taskMain);
	
	void add(Long taskMainId, Long[] ccList,Long heldPerson, Long checkPerson, String[] taglocationappearanceImage);
	
	void dealSave(TaskReplyDO taskReplyDO/* ,Long[] ccList */);
	
	void checkSave(TaskReplyDO taskReplyDO/* ,Long[] ccList */);
	
	int replySave(TaskReplyDO taskReplyDO, Long[] ccList);
	
	int update(TaskMainDO taskMain);
	
	void edit(TaskMainDO taskMain, Long[] ccList,Long heldPerson, Long checkPerson, String[] taglocationappearanceImage,String[] deletetagAppearanceImage);
	
	int remove(Long id);
	
	void removeSatellite(Long[] ids, Long[] assocTypes, String imageType);
	
	int batchRemove(Long[] ids);


	boolean nonWaitingDeal(Long status);

	boolean nonTS(Long status);

	boolean nonWaitingCheck(Long status);

	/**
	 * 是否为处理人
	 */
	boolean isDealBy(Long id);

	boolean isAlreadyCheck(Long status);

	
}
