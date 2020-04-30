package com.ev.custom.service.impl;

import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.R;
import com.ev.custom.dao.OverTimeApplyDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.OverTimeApplyDO;
import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.*;
import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EnableTransactionManagement
@Service
public class OverTimeApplyServiceImpl implements OverTimeApplyService {
	@Autowired
	private OverTimeApplyDao overTimeApplyDao;

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
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public OverTimeApplyDO get(Long id){
		return overTimeApplyDao.get(id);
	}
	
	@Override
	public List<OverTimeApplyDO> list(Map<String, Object> map){
		return overTimeApplyDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return overTimeApplyDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return overTimeApplyDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return overTimeApplyDao.countForMap(map);
	}

	@Override
	public int save(OverTimeApplyDO overTimeApply){
		return overTimeApplyDao.save(overTimeApply);
	}
	
	@Override
	public int update(OverTimeApplyDO overTimeApply){
		return overTimeApplyDao.update(overTimeApply);
	}
	
	@Override
	public int remove(Long id){
		return overTimeApplyDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return overTimeApplyDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = new HashMap<String,Object>();

		OverTimeApplyDO overTimeApplyDO = get(id);
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("id",id);

		Map<String, Object> oneOfDetail = overTimeApplyDao.getOneOfDetail(query);

		results.put("overTimeApply", oneOfDetail);
		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", overTimeApplyDO.getId());
			put("assocType", ConstantForDevice.OVER_TIME_APPLY_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//获取发送对象
		List<Map<String,Object>> targetList = userAssocService.list(new HashMap<String,Object>(){{put("assocId",overTimeApplyDO.getId());put("assocType", ConstantForDevice.OVER_TIME_APPLY_TARGET);}});
		results.put("targetList", targetList);
		//获取审核人
		List<Map<String, Object>> approveList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", overTimeApplyDO.getId());
			put("assocType", ConstantForDevice.OVER_TIME_APPROVE_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("approveList", approveList);
		//获取回复信息
		Map<String,Object> commentMap = new HashMap<String,Object>(){{put("assocId",id);put("assocType", ConstantForDevice.OVER_TIME_APPLY_COMMENT);}};
		List<CommentDO> commentList = commentService.list(commentMap);
		results.put("commentList", commentList);
		return results;
	}

	@Override
	public void submit(OverTimeApplyDO overTimeApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		//驱动流程
		String processInstanceId = dingdingService.submitApply(approveList,overTimeApplyDO.getProcessInstanceId()==null?null:overTimeApplyDO.getProcessInstanceId().toString());
		overTimeApplyDO.setProcessInstanceId(processInstanceId);
		overTimeApplyDO.setStatus(dictionaryService.getByValue("apply_approving","apply_status").getId());
		saveOverTimeApply(overTimeApplyDO, approveList,taglocationappearanceImage );
	}

	@Override
	public void tempSave(OverTimeApplyDO overTimeApplyDO, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		saveOverTimeApply(overTimeApplyDO, approveList, taglocationappearanceImage);
	}

	@Override
	public void approve(Long overTimeApplyId, Integer isApproved, String reason) {
		OverTimeApplyDO overTimeApplyDO = get(overTimeApplyId);
		String status = dingdingService.completeApprove(overTimeApplyDO.getProcessInstanceId(),isApproved==1?true:false,reason);
		if("down".equals(status)){
			overTimeApplyDO.setStatus(ConstantForDevice.APPLY_REJECT);
			update(overTimeApplyDO);
		}else if("up".equals(status)){
			overTimeApplyDO.setStatus(ConstantForDevice.APPLY_COMPLETED);
			update(overTimeApplyDO);
		}else{
			//TODO
		}
	}

	@Override
	public void commentOverTimeApply(Long overTimeApplyId, String comment) {
		CommentDO commentDo = new CommentDO(overTimeApplyId, ConstantForDevice.OVER_TIME_APPLY_COMMENT,comment);
		commentService.save(commentDo);
	}

	public void saveOverTimeApply(OverTimeApplyDO overTimeApplyDO, Long[] approveList, String[] tagLocationAppearanceAttachment){
		if(overTimeApplyDO.getId() == null || overTimeApplyDO.getId() == 0){
			save(overTimeApplyDO);
			//审核人
			for(int i=0;i<approveList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(overTimeApplyDO.getId(), ConstantForDevice.OVER_TIME_APPROVE_TARGET,approveList[i]);
				userAssocService.save(userAssocDO);
			}
//			//抄送给谁
//			for(int i=0;i<targetList.length;i++){
//				UserAssocDO userAssocDO = new UserAssocDO(overTimeApplyDO.getId(),Constant.OVER_TIME_APPLY_TARGET,targetList[i]);
//				userAssocService.save(userAssocDO);
//			}
		}else{
			update(overTimeApplyDO);
		}
		//附件信息操作
		contentAssocService.saveList(overTimeApplyDO.getId(),tagLocationAppearanceAttachment, ConstantForDevice.OVER_TIME_APPLY_APPEARANCE_ATTACHMENT);
//		contentAssocService.deleteList(deleteTagAppearanceAttachment);
	}


	@Override
	public void saveChangeAndSbmit(OverTimeApplyDO overTimeApplyDO, Long[] newApproveList, String[] newAttachment, int sign) {

		if (sign == 1) {
			String processInstanceId = dingdingService.submitApply(newApproveList,overTimeApplyDO.getProcessInstanceId()==null?null:overTimeApplyDO.getProcessInstanceId().toString());
			overTimeApplyDO.setProcessInstanceId(processInstanceId);
			overTimeApplyDO.setStatus(ConstantForDevice.APPLY_APPROVING);
		}//63审核中

		update(overTimeApplyDO);

		//审核人  新增+删除
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("userId", null);
		query.put("assocId", overTimeApplyDO.getId());
		query.put("assocType", ConstantForDevice.OVER_TIME_APPROVE_TARGET);
		userAssocService.removeByAssocIdAndUserId(query);
		if (newApproveList.length > 0) {
			for (int i = 0; i < newApproveList.length; i++) {
				UserAssocDO userAssocDO = new UserAssocDO(overTimeApplyDO.getId(), ConstantForDevice.OVER_TIME_APPROVE_TARGET, newApproveList[i]);
				userAssocService.save(userAssocDO);
			}
		}
		//删除路径 +增加路径保存
		Long[] ids = new Long[1];
		ids[0] = overTimeApplyDO.getId();
		contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.OVER_TIME_APPLY_APPEARANCE_ATTACHMENT);

		if (newAttachment.length > 0) {
			contentAssocService.saveList(overTimeApplyDO.getId(), newAttachment, ConstantForDevice.OVER_TIME_APPLY_APPEARANCE_ATTACHMENT);
		}
	}

	@Override
	public R listOfCanDelet(Map<String, Object> map,Long[]ids) {

		List<Map<String, Object>> mapList = overTimeApplyDao.listOfCanDelet(map);
		Map<String, Object> result = new HashMap<>();
		if(Objects.equals(mapList.size(),ids.length)){
			Long[] deleIds=new Long[mapList.size()];
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> maps=mapList.get(i);
				deleIds[i]= Long.parseLong(maps.get("id").toString());
			}
			overTimeApplyDao.batchRemove(deleIds);

			Map<String, Object> query = new HashMap<>();
			query.put("assocId",deleIds );
			query.put("assocType", ConstantForDevice.OVER_TIME_APPROVE_TARGET);
			userAssocService.batchRemoveByAssocIdAadType(query);

			contentAssocService.removeByAssocIdAndType(deleIds, ConstantForDevice.OVER_TIME_APPLY_APPEARANCE_ATTACHMENT);

			return R.ok();
		}
		//"单据已提交，不能删除！"
		return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
	}


}
