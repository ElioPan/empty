package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.custom.dao.ReimApplyDao;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@EnableTransactionManagement
public class ReimApplyServiceImpl implements ReimApplyService {

	@Autowired
	private ReimApplyDao reimApplyDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private DingdingService dingdingService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ReimApplyItemService reimApplyItemService;
	
	@Override
	public ReimApplyDO get(Long id){
		return reimApplyDao.get(id);
	}
	
	@Override
	public List<ReimApplyDO> list(Map<String, Object> map){
		return reimApplyDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return reimApplyDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return reimApplyDao.listForMap(map);
	}


	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return reimApplyDao.countForMap(map);
	}

	@Override
	public int save(ReimApplyDO reimApply){
		return reimApplyDao.save(reimApply);
	}
	
	@Override
	public int update(ReimApplyDO reimApply){
		return reimApplyDao.update(reimApply);
	}
	
	@Override
	public int remove(Long id){
		return reimApplyDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return reimApplyDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = new HashMap<String,Object>();
		Map<String,Object> query = new HashMap<String,Object>();
		ReimApplyDO reimApplyDO = get(id);
		query.put("id",id);

		Map<String, Object> reimHead = reimApplyDao.getReimHead(query);

		results.put("reimApplyDO", reimHead);
		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", reimApplyDO.getId());
			put("assocType", Constant.REIM_APPLY_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//审核人
		List<Map<String, Object>> approveList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", reimApplyDO.getId());
			put("assocType", Constant.REIM_APPROVE_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("approveList", approveList);
		//获取发送对象
		List<Map<String,Object>> targetList = userAssocService.list(new HashMap<String,Object>(){{put("assocId",reimApplyDO.getId());put("assocType",Constant.REIM_APPLY_TARGET);}});
		results.put("targetList", targetList);
		//获取回复信息
		Map<String,Object> commentMap = new HashMap<String,Object>(){{put("assocId",id);put("assocType",Constant.REIM_APPLY_COMMENT);}};
		List<CommentDO> commentList = commentService.list(commentMap);
		results.put("commentList", commentList);

		//获取明细
		List<Map<String, Object>> reimItem = reimApplyDao.getReimItem(query);
		Map<String, Object> sumOfCount = reimApplyDao.getSumOfCount(query);
		results.put("itemList", reimItem);
		results.put("sumOfCount", sumOfCount);
		return results;
	}

	@Override
	public void submit(ReimApplyDO reimApplyDO, String reimApplyItems, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		//驱动流程
		String processInstanceId = dingdingService.submitApply(approveList,reimApplyDO.getProcessInstanceId()==null?null:reimApplyDO.getProcessInstanceId().toString());
		reimApplyDO.setProcessInstanceId(processInstanceId);
		reimApplyDO.setStatus(Constant.APPLY_APPROVING);
		saveReimApply(reimApplyDO, reimApplyItems, approveList,  taglocationappearanceImage );
	}

	@Override
	public void tempSave(ReimApplyDO reimApplyDO, String reimApplyItems, Long[] approveList, Long[] targetList, String[] taglocationappearanceImage, String[] deleteTagAppearanceAttachment) {
		saveReimApply(reimApplyDO, reimApplyItems, approveList,taglocationappearanceImage);
	}

	@Override
	public void approve(Long reimApplyId, Integer isApproved, String reason) {
		ReimApplyDO reimApplyDO = get(reimApplyId);
		String status = dingdingService.completeApprove(reimApplyDO.getProcessInstanceId(),isApproved==1?true:false,reason);
		if("down".equals(status)){
			reimApplyDO.setStatus(Constant.APPLY_REJECT);
			update(reimApplyDO);
		}else if("up".equals(status)){
			reimApplyDO.setStatus(Constant.APPLY_COMPLETED);
			update(reimApplyDO);
		}else{
			//TODO
		}
	}

	@Override
	public void commentReimApply(Long reimApplyId, String comment) {
		CommentDO commentDo = new CommentDO(reimApplyId,Constant.REIM_APPLY_COMMENT,comment);
		commentService.save(commentDo);
	}


	public void saveReimApply(ReimApplyDO reimApplyDO, String reimApplyItems, Long[] approveList, String[] tagLocationAppearanceAttachment){
		if(reimApplyDO.getId() == null || reimApplyDO.getId() == 0){
			save(reimApplyDO);
			//审核人
			for(int i=0;i<approveList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(reimApplyDO.getId(),Constant.REIM_APPROVE_TARGET,approveList[i]);
				userAssocService.save(userAssocDO);
				//抄送给谁
//			for(int i=0;i<targetList.length;i++){
//				UserAssocDO userAssocDO = new UserAssocDO(reimApplyDO.getId(),Constant.REIM_APPLY_TARGET,targetList[i]);
//				userAssocService.save(userAssocDO);
//			}
			}
		}else{
			update(reimApplyDO);
		}
		//附件信息操作
		if(tagLocationAppearanceAttachment!=null&&tagLocationAppearanceAttachment.length>0){
			contentAssocService.saveList(reimApplyDO.getId(),tagLocationAppearanceAttachment,Constant.REIM_APPLY_APPEARANCE_ATTACHMENT);
		}
//		contentAssocService.saveList(reimApplyDO.getId(),tagLocationAppearanceAttachment,Constant.REIM_APPLY_APPEARANCE_ATTACHMENT);
		//明细保存
		List<ReimApplyItemDO> items = JSON.parseArray(reimApplyItems,ReimApplyItemDO.class);
		for(ReimApplyItemDO obj : items){
			if(obj.getId()!=null && obj.getId()!=0){
				reimApplyItemService.update(obj);
			}else{
				obj.setReimApplyId(reimApplyDO.getId());
				reimApplyItemService.save(obj);
			}
		}
	}


	@Override
	public void saveChangeAndSbmit(ReimApplyDO reimApplyDO, String newReimApplyItems, Long[] newApproveMen,  String[] newTaglocatio,Long[] detailIds ,int sign) {

		if (sign == 1) {
			String processInstanceId = dingdingService.submitApply(newApproveMen,reimApplyDO.getProcessInstanceId()==null?null:reimApplyDO.getProcessInstanceId().toString());
			reimApplyDO.setProcessInstanceId(processInstanceId);
			reimApplyDO.setStatus(Constant.APPLY_APPROVING);

		}//审批中状态

		update(reimApplyDO);

		//处理审核人

			Map<String, Object> query = new HashMap<String, Object>();
			query.put("userId", null);
			query.put("assocId", reimApplyDO.getId());
			query.put("assocType", Constant.REIM_APPROVE_TARGET);
			userAssocService.removeByAssocIdAndUserId(query);

		if (newApproveMen.length>0) {
			for (int i = 0; i < newApproveMen.length; i++) {
				UserAssocDO userAssocDO = new UserAssocDO(reimApplyDO.getId(), Constant.REIM_APPROVE_TARGET, newApproveMen[i]);
				userAssocService.save(userAssocDO);
			}
		}

		//删除路径 +增加路径保存
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] =reimApplyDO.getId();
		contentAssocService.removeByAssocIdAndType(ids, Constant.REIM_APPLY_APPEARANCE_ATTACHMENT);

		if (newTaglocatio.length>0) {
			contentAssocService.saveList(reimApplyDO.getId(), newTaglocatio, Constant.REIM_APPLY_APPEARANCE_ATTACHMENT);
		}

		//明细保存  新增+修改+删除
		//1.根据明细主键删除
		if(detailIds.length>0){reimApplyItemService.batchRemove(detailIds);}
		//新增+保存
		if (newReimApplyItems != null && !"".equals(newReimApplyItems)) {
			List<ReimApplyItemDO> items = JSON.parseArray(newReimApplyItems, ReimApplyItemDO.class);
			for (ReimApplyItemDO obj : items) {
				if(obj.getId()!=null ){
					reimApplyItemService.update(obj);
				}else{
					obj.setReimApplyId(reimApplyDO.getId());
					reimApplyItemService.save(obj);
				}
			}
//		//新增抄送人++删除抄送人
//		if (deletTarget.length>0) {
//			Map<String, Object> query = new HashMap<String, Object>();
//			query.put("userId", deletTarget);
//			query.put("assocId", reimApplyDO.getId());
//			query.put("assocType", Constant.PAY_APPLY_TARGET);
//			userAssocService.removeByAssocIdAndUserId(query);
//		}
//		if (newTarget.length>0) {
//			for (int i = 0; i < newTarget.length; i++) {
//				UserAssocDO userAssocDO = new UserAssocDO(reimApplyDO.getId(), Constant.PAY_APPLY_TARGET, newTarget[i]);
//				userAssocService.save(userAssocDO);
//			}
//		}

		}

	}


	@Override
	public R removeBacth(Long[] ids){

		Long logIDs[] = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			ReimApplyDO reimApplyDO = reimApplyDao.get(ids[i]);
			if (reimApplyDO != null) {
				if (Objects.equals(Constant.TS, reimApplyDO.getStatus()) || Objects.equals(Constant.APPLY_REJECT, reimApplyDO.getStatus())) {
					logIDs[i] = ids[i];
				}else {
					return R.error("单据已提交，不能删除！");
				}
			}
		}
		reimApplyDao.batchRemove(logIDs);
		return R.ok();
	}


}
