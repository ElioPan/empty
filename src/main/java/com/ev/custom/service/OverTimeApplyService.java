package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.OverTimeApplyDO;

import java.util.List;
import java.util.Map;

/**
 * 加班申请管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 08:55:04
 */
public interface OverTimeApplyService {
	
	OverTimeApplyDO get(Long id);
	
	List<OverTimeApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(OverTimeApplyDO overTimeApply);
	
	int update(OverTimeApplyDO overTimeApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object>  detail(Long id);

	void submit(OverTimeApplyDO overTimeApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment);

	void tempSave(OverTimeApplyDO overTimeApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage,String[] deleteTagAppearanceAttachment);

	void approve(Long overTimeApplyId, Integer isApproved, String reason);

	void commentOverTimeApply(Long overTimeApplyId,String comment);

	void saveChangeAndSbmit(OverTimeApplyDO overTimeApplyDO, Long[] newApproveList,String[] newAttachment,int sign);

	R listOfCanDelet(Map<String, Object> map,Long[]ids);


}
