package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.ReimApplyDO;

import java.util.List;
import java.util.Map;

/**
 * 报销申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:37
 */
public interface ReimApplyService {
	
	ReimApplyDO get(Long id);
	
	List<ReimApplyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);
	
	int save(ReimApplyDO reimApply);
	
	int update(ReimApplyDO reimApply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> detail(Long id);

	void submit(ReimApplyDO reimApplyDO, String reimApplyItems, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment);

	void tempSave(ReimApplyDO reimApplyDO, String reimApplyItems, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage,String[] deleteTagAppearanceAttachment);

	void approve(Long reimApplyId, Integer isApproved, String reason);

	void commentReimApply(Long reimApplyId,String comment);

	void saveChangeAndSbmit(ReimApplyDO reimApplyDO,String newReimApplyItems, Long[] newApproveMen, String[] newTaglocatio,Long[] detailIds ,int sign);

	R removeBacth(Long[] ids);
}
