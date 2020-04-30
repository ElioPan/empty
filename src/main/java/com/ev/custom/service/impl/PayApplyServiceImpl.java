package com.ev.custom.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.R;
import com.ev.custom.dao.PayApplyDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.PayApplyDO;
import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EnableTransactionManagement
@Service
public class PayApplyServiceImpl implements PayApplyService {
	@Autowired
	private PayApplyDao payApplyDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private DingdingService dingdingService;

	@Autowired
	private CommentService commentService;
	
	@Override
	public PayApplyDO get(Long id){
		return payApplyDao.get(id);
	}
	
	@Override
	public List<PayApplyDO> list(Map<String, Object> map){
		return payApplyDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return payApplyDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return payApplyDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return payApplyDao.countForMap(map);
	}

	@Override
	public int save(PayApplyDO payApply){
		return payApplyDao.save(payApply);
	}
	
	@Override
	public int update(PayApplyDO payApply){
		return payApplyDao.update(payApply);
	}
	
	@Override
	public int remove(Long id){
		return payApplyDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return payApplyDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = new HashMap<String,Object>();
		PayApplyDO payApplyDO = get(id);
		Map<String,Object> qauery = new HashMap<String,Object>();
		qauery.put("id",id);
		Map<String, Object> payDetail = payApplyDao.getPayDetail(qauery);

		results.put("payApplyDO", payDetail);
		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", payApplyDO.getId());
			put("assocType", ConstantForDevice.PAY_APPLY_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//审核人
		List<Map<String, Object>> approveList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", payApplyDO.getId());
			put("assocType", ConstantForDevice.PAY_APPROVE_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("approveList", approveList);
		//获取发送对象
		List<Map<String,Object>> targetList = userAssocService.list(new HashMap<String,Object>(){{put("assocId",payApplyDO.getId());put("assocType", ConstantForDevice.PAY_APPLY_TARGET);}});
		results.put("targetList", targetList);
		//获取回复信息
		Map<String,Object> commentMap = new HashMap<String,Object>(){{put("assocId",id);put("assocType", ConstantForDevice.PAY_APPLY_COMMENT);}};
		List<CommentDO> commentList = commentService.list(commentMap);
		results.put("commentList", commentList);
		return results;
	}

	@Override
	public void submit(PayApplyDO payApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		String processInstanceId = dingdingService.submitApply(approveList,payApplyDO.getProcessInstanceId()==null?null:payApplyDO.getProcessInstanceId().toString());
		payApplyDO.setProcessInstanceId(processInstanceId);
		payApplyDO.setStatus(ConstantForDevice.APPLY_APPROVING);
		savePayApply(payApplyDO, approveList, taglocationappearanceImage );
	}

	@Override
	public void tempSave(PayApplyDO payApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		savePayApply(payApplyDO, approveList,  taglocationappearanceImage );
	}

	@Override
	public void approve(Long payApplyId, Integer isApproved, String reason) {
		PayApplyDO payApplyDO = get(payApplyId);
		String status = dingdingService.completeApprove(payApplyDO.getProcessInstanceId(),isApproved==1,reason);
		if("down".equals(status)){
			payApplyDO.setStatus(ConstantForDevice.APPLY_REJECT);
			update(payApplyDO);
		}else if("up".equals(status)){
			payApplyDO.setStatus(ConstantForDevice.APPLY_COMPLETED);
			update(payApplyDO);
		}else{
			//TODO
		}
	}

	@Override
	public void commentPayApply(Long payApplyId, String comment) {
		CommentDO commentDo = new CommentDO(payApplyId, ConstantForDevice.PAY_APPLY_COMMENT,comment);
		commentService.save(commentDo);
	}

	public void savePayApply(PayApplyDO payApplyDO, Long[] approveList, String[] tagLocationAppearanceAttachment){
		if(payApplyDO.getId() == null || payApplyDO.getId() == 0){
			save(payApplyDO);
			//审核人
			for(int i=0;i<approveList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(payApplyDO.getId(), ConstantForDevice.PAY_APPROVE_TARGET,approveList[i]);
				userAssocService.save(userAssocDO);
			}
//			//抄送给谁
//			for(int i=0;i<targetList.length;i++){
//				UserAssocDO userAssocDO = new UserAssocDO(payApplyDO.getId(),Constant.PAY_APPLY_TARGET,targetList[i]);
//				userAssocService.save(userAssocDO);
//			}
		}else{
			update(payApplyDO);
		}
		//附件信息操作
		contentAssocService.saveList(payApplyDO.getId(),tagLocationAppearanceAttachment, ConstantForDevice.PAY_APPLY_APPEARANCE_ATTACHMENT);
//		contentAssocService.deleteList(deleteTagAppearanceAttachment);
	}

    @Override
	public void saveChangeAndSbmit(PayApplyDO payApplyDO, Long[] newApproveMen, String[] newTaglocatio, int sign){

		if(sign==1){
			String processInstanceId = dingdingService.submitApply(newApproveMen,payApplyDO.getProcessInstanceId()==null?null:payApplyDO.getProcessInstanceId().toString());
			payApplyDO.setProcessInstanceId(processInstanceId);
			payApplyDO.setStatus(ConstantForDevice.APPLY_APPROVING);
		}//63审批中

			update(payApplyDO);

		//新增审核人++删除审核人
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("userId", null);
			query.put("assocId", payApplyDO.getId());
			query.put("assocType", ConstantForDevice.PAY_APPROVE_TARGET);

			userAssocService.removeByAssocIdAndUserId(query);

		if (newApproveMen.length>0) {
			for (int i = 0; i < newApproveMen.length; i++) {
				UserAssocDO userAssocDO = new UserAssocDO(payApplyDO.getId(), ConstantForDevice.PAY_APPROVE_TARGET, newApproveMen[i]);
				userAssocService.save(userAssocDO);
			}
		}

		//附件信息操作
		//删除路径 +增加路径保存
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] =payApplyDO.getId();
		contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.PAY_APPLY_APPEARANCE_ATTACHMENT);

		if (newTaglocatio.length>0) {
			contentAssocService.saveList(payApplyDO.getId(), newTaglocatio, ConstantForDevice.PAY_APPLY_APPEARANCE_ATTACHMENT);
		}
//			//新增抄送人++删除抄送人
//		if (deletTarget.length>0) {
//			Map<String, Object> query = new HashMap<String, Object>();
//			query.put("userId", deletTarget);
//			query.put("assocId", payApplyDO.getId());
//			userAssocService.removeByAssocIdAndUserId(query);
//		}
//		if (newTarget .length>0) {
//			for (int i = 0; i < newTarget.length; i++) {
//				UserAssocDO userAssocDO = new UserAssocDO(payApplyDO.getId(), Constant.PAY_APPLY_TARGET, newTarget[i]);
//				userAssocService.save(userAssocDO);
//			}
//		}
	}

	@Override
	public R removeBacth(Long[] ids){
		Map<String, Object> query = new HashMap<String, Object>();
		Long logIDs[] = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {

			PayApplyDO payApplyDO = payApplyDao.get(ids[i]);
			if (payApplyDO != null) {
				if (Objects.equals(Constant.TS, payApplyDO.getStatus()) || Objects.equals(ConstantForDevice.APPLY_REJECT, payApplyDO.getStatus())) {
					logIDs[i] = ids[i];
				}else{
					return R.error("单据已提交，不能删除！！");
				}
			}
		}
		payApplyDao.batchRemove(logIDs);
		return R.ok();
	}

}
