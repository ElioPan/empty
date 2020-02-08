package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.PayApplyDO;

import java.util.List;
import java.util.Map;

/**
 * 付款申请
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:36
 */
public interface PayApplyService {

	PayApplyDO get(Long id);

	List<PayApplyDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	int save(PayApplyDO payApply);

	int update(PayApplyDO payApply);

	int remove(Long id);

	int batchRemove(Long[] ids);

	Map<String, Object> detail(Long id);

	void submit(PayApplyDO payApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment);

	void tempSave(PayApplyDO payApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage,String[] deleteTagAppearanceAttachment);

	void approve(Long payApplyId, Integer isApproved, String reason);

	void commentPayApply(Long payApplyId,String comment);

	void saveChangeAndSbmit(PayApplyDO payApplyDO,Long[] newApproveMen,String[] newTaglocatio,int sign);

	R removeBacth(Long[] ids);
}
