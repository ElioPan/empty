package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.LeaveApplyDO;

import java.util.List;
import java.util.Map;

/**
 * 请假管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 15:18:26
 */
public interface LeaveApplyService {
	
	LeaveApplyDO get(Long id);
	
	List<LeaveApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(LeaveApplyDO leaveApply);
	
	int update(LeaveApplyDO leaveApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object>  detail(Long id);

	void submit(LeaveApplyDO leaveApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment);

	void tempSave(LeaveApplyDO leaveApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage,String[] deleteTagAppearanceAttachment);

	void approve(Long leaveApplyId, Integer isApproved, String reason);

	void commentLeaveApply(Long leaveApplyId,String comment);

	void saveChangeOfLeaveDetail(LeaveApplyDO leaveApplyDO,Long[] newApproveMen,String[] taglocationappearanceImage,int sign);

	Map<String,Object> getOfDetail(Long id);

	R removeBacth(Long[] ids);

}
