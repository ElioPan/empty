package com.ev.custom.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.R;
import com.ev.custom.dao.LeaveApplyDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.LeaveApplyDO;
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
public class LeaveApplyServiceImpl implements LeaveApplyService {
	@Autowired
	private LeaveApplyDao leaveApplyDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private DingdingService dingdingService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private CommentService commentService;
	@Autowired
	private LeaveApplyService leaveApplyService;

	@Override
	public LeaveApplyDO get(Long id){
		return leaveApplyDao.get(id);
	}

	@Override
	public List<LeaveApplyDO> list(Map<String, Object> map){
		return leaveApplyDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return leaveApplyDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return leaveApplyDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return leaveApplyDao.countForMap(map);
	}

	@Override
	public int save(LeaveApplyDO leaveApply){
		return leaveApplyDao.save(leaveApply);
	}

	@Override
	public int update(LeaveApplyDO leaveApply){
		return leaveApplyDao.update(leaveApply);
	}

	@Override
	public int remove(Long id){
		return leaveApplyDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return leaveApplyDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> leaveApplyMap = leaveApplyService.getOfDetail(id);
		results.put("leaveApply", leaveApplyMap);

		LeaveApplyDO leaveApplyDo =get(id);

		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", leaveApplyDo.getId());
			put("assocType", ConstantForDevice.LEAVE_APPLY_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//获取发送对象
		List<Map<String, Object>> targetList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", leaveApplyDo.getId());
			put("assocType", ConstantForDevice.LEAVE_APPLY_TARGET);
		}});
		results.put("targetList", targetList);
		//获取审批人
		List<Map<String, Object>> approveList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", leaveApplyDo.getId());
			put("assocType", ConstantForDevice.LEAVE_APPROVE_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("approveList", approveList);
		//获取回复信息
		Map<String, Object> commentMap = new HashMap<String, Object>() {{
			put("assocId", id);
			put("assocType", ConstantForDevice.LEAVE_APPLY_COMMENT);
		}};
		List<CommentDO> commentList = commentService.list(commentMap);
		results.put("commentList", commentList);
		return results;
	}

	@Override
	public void submit(LeaveApplyDO leaveApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		//驱动流程
		String processInstanceId = dingdingService.submitApply(approveList,leaveApplyDO.getProcessInstanceId()==null?null:leaveApplyDO.getProcessInstanceId().toString());
		leaveApplyDO.setProcessInstanceId(processInstanceId);
		leaveApplyDO.setStatus(dictionaryService.getByValue("apply_approving","apply_status").getId());
		saveLeaveApply(leaveApplyDO, approveList, taglocationappearanceImage );
	}

	@Override
	public void tempSave(LeaveApplyDO leaveApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {

		saveLeaveApply(leaveApplyDO, approveList,taglocationappearanceImage);
	}

	@Override
	public void approve(Long leaveApplyId, Integer isApproved, String reason) {
		LeaveApplyDO leaveApplyDO = get(leaveApplyId);
		String status = dingdingService.completeApprove(leaveApplyDO.getProcessInstanceId(),isApproved==1?true:false,reason);
		System.out.println("==============status====="+status+"==========================");
		if("down".equals(status)){
			leaveApplyDO.setStatus(ConstantForDevice.APPLY_REJECT);
			update(leaveApplyDO);
		}else if("up".equals(status)){
			leaveApplyDO.setStatus(ConstantForDevice.APPLY_COMPLETED);
			update(leaveApplyDO);
		}else{
			//TODO
		}
	}

	@Override
	public void commentLeaveApply(Long leaveApplyId, String comment) {
		CommentDO commentDo = new CommentDO(leaveApplyId, ConstantForDevice.LEAVE_APPLY_COMMENT,comment);
		commentService.save(commentDo);
	}


	public void saveLeaveApply(LeaveApplyDO leaveApplyDO, Long[] approveList,String[] tagLocationAppearanceAttachment){
		if(leaveApplyDO.getId() == null || leaveApplyDO.getId() == 0){
			save(leaveApplyDO);
			//审核人
			for(int i=0;i<approveList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(leaveApplyDO.getId(), ConstantForDevice.LEAVE_APPROVE_TARGET,approveList[i]);
				userAssocService.save(userAssocDO);
			}
//			//抄送给谁
//			for(int i=0;i<targetList.length;i++){
//				UserAssocDO userAssocDO = new UserAssocDO(leaveApplyDO.getId(),Constant.LEAVE_APPLY_TARGET,targetList[i]);
//				userAssocService.save(userAssocDO);
//			}
		}
		//附件信息操作
		contentAssocService.saveList(leaveApplyDO.getId(),tagLocationAppearanceAttachment, ConstantForDevice.LEAVE_APPLY_APPEARANCE_ATTACHMENT);

	}


	@Override
	public void saveChangeOfLeaveDetail(LeaveApplyDO leaveApplyDO, Long[] newApproveMen, String[] newTaglocatio,int sign) {
		Long leaveId = leaveApplyDO.getId();

		//sign==1 更新提交     sign==0 修改
		if (sign == 1) {
			String processInstanceId = dingdingService.submitApply(newApproveMen,leaveApplyDO.getProcessInstanceId()==null?null:leaveApplyDO.getProcessInstanceId().toString());
			leaveApplyDO.setProcessInstanceId(processInstanceId);
			leaveApplyDO.setStatus(ConstantForDevice.APPLY_APPROVING);
		} //146：暂存  状态   63审批中

		//修改LeaveAppply表信息
        leaveApplyService.update(leaveApplyDO);

        //删除审核人
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("userId", null);
            query.put("assocId", leaveId);
            query.put("assocType", ConstantForDevice.LEAVE_APPROVE_TARGET);

            userAssocService.removeByAssocIdAndUserId(query);

        //新增审核人
        if (newApproveMen.length>0) {
            for (int i = 0; i < newApproveMen.length; i++) {
                UserAssocDO userAssocDO = new UserAssocDO(leaveId, ConstantForDevice.LEAVE_APPROVE_TARGET, newApproveMen[i]);
                userAssocService.save(userAssocDO);
            }
        }
		//附件信息操作
		//删除路径 +增加路径保存
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] =leaveId;
		contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.LEAVE_APPLY_APPEARANCE_ATTACHMENT);

		if (newTaglocatio.length>0) {
			contentAssocService.saveList(leaveId, newTaglocatio, ConstantForDevice.LEAVE_APPLY_APPEARANCE_ATTACHMENT);
		}
//        //删除抄送人
//        if (deletTarget.length>0) {
//            Map<String, Object> query = new HashMap<String, Object>();
//            query.put("userId", deletTarget);
//            query.put("assocId", leaveId);
//            query.put("assocType", Constant.LEAVE_APPLY_TARGET);
//            userAssocService.removeByAssocIdAndUserId(query);
//        }
//        //新增抄送人
//        if (newTarget.length>0) {
//            for (int i = 0; i < newTarget.length; i++) {
//                UserAssocDO userAssocDO = new UserAssocDO(leaveId, Constant.LEAVE_APPLY_TARGET, newTarget[i]);
//                userAssocService.save(userAssocDO);
//            }
//        }
	}

	@Override
	public Map<String, Object> getOfDetail(Long id) {
		return leaveApplyDao.getOfDetail(id);
	}


	public void saveNewOfsubmit(LeaveApplyDO leaveApplyDO, Long[] approveList, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment,int  sign){

		//sign==1 新增提交     sign==0 修改提交
		if(sign==1){
			leaveApplyDO.setStatus(ConstantForDevice.APPLY_APPROVING); //146：暂存  状态   63审批中
			save(leaveApplyDO);
			//审核人
			for(int i=0;i<approveList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(leaveApplyDO.getId(), ConstantForDevice.LEAVE_APPROVE_TARGET,approveList[i]);
				userAssocService.save(userAssocDO);
			}
			//抄送给谁
			for(int i=0;i<targetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(leaveApplyDO.getId(), ConstantForDevice.LEAVE_APPLY_TARGET,targetList[i]);
				userAssocService.save(userAssocDO);
			}
			//附件信息操作
			contentAssocService.saveList(leaveApplyDO.getId(),tagLocationAppearanceAttachment, ConstantForDevice.LEAVE_APPLY_APPEARANCE_ATTACHMENT);
		}
	}

	@Override
	public R removeBacth(Long[] ids){

		Long logIDs[] = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			LeaveApplyDO applyDO = leaveApplyService.get(ids[i]);
			if (applyDO != null) {
				if (Objects.equals(Constant.TS, applyDO.getStatus()) || Objects.equals(ConstantForDevice.APPLY_REJECT, applyDO.getStatus())) {
					logIDs[i] = ids[i];
				}else{
					return R.error("单据已提交，不能删除！");
				}
			}
		}
		leaveApplyService.batchRemove(logIDs);
		return R.ok();
	}

}
