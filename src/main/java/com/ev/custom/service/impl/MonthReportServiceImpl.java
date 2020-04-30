package com.ev.custom.service.impl;

import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.MonthReportDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.MonthReportDO;
import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.CommentService;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.MonthReportService;
import com.ev.custom.service.UserAssocService;
import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class MonthReportServiceImpl implements MonthReportService {
	@Autowired
	private MonthReportDao monthReportDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Override
	public MonthReportDO get(Long id){
		return monthReportDao.get(id);
	}
	
	@Override
	public List<MonthReportDO> list(Map<String, Object> map){
		return monthReportDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return monthReportDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return monthReportDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return monthReportDao.countForMap(map);
	}

	@Override
	public int save(MonthReportDO monthReport){
		return monthReportDao.save(monthReport);
	}
	
	@Override
	public int update(MonthReportDO monthReport){
		return monthReportDao.update(monthReport);
	}
	
	@Override
	public int remove(Long id){
		return monthReportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return monthReportDao.batchRemove(ids);
	}

	@Override
	public void add(MonthReportDO monthReportDO,Long[] targetList, String[] taglocationappearanceImage) {
		saveMonthReport(monthReportDO, targetList,taglocationappearanceImage,null);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = new HashMap<String,Object>();
//		List<UserDO> userList = userService.list(null);
//
//		results.put("userList",userList);
		MonthReportDO monthReport = get(id);

		Map<String,Object> query = new HashMap<String,Object>();
		query.put("id",id);

		Map<String, Object> oneMonthDetail = monthReportDao.getOneMonthDetail(query);
		results.put("monthReport",oneMonthDetail);

		//获取附件
		Map<String,Object> contentMap = new HashMap<String,Object>(){{put("assocId",monthReport.getId());put("assocType", ConstantForDevice.MONTH_REPORT_APPEARANCE_ATTACHMENT);}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//获取发送对象
		List<Map<String,Object>> targetList = userAssocService.list(new HashMap<String,Object>(){{put("assocId",monthReport.getId());put("assocType", ConstantForDevice.MONTH_REPORT_TARGET);}});
		results.put("targetList", targetList);
		//获取回复信息
		Map<String,Object> commentMap = new HashMap<String,Object>(){{put("assocId",id);put("assocType", ConstantForDevice.MONTH_REPORT_COMMENT);}};
//		List<CommentDO> commentList = commentService.list(commentMap);
		List<Map<String, Object>> commentList =commentService.listOfDetail(commentMap);
		results.put("commentList", commentList);
		return results;
	}

	@Override
	public void edit(MonthReportDO monthReportDO, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment) {
		saveMonthReport(monthReportDO,targetList,tagLocationAppearanceAttachment,deleteTagAppearanceAttachment);
	}

	@Override
	public void commentMonthReport(Long monthReportId, String comment) {
		CommentDO commentDo = new CommentDO(monthReportId, ConstantForDevice.MONTH_REPORT_COMMENT,comment);
		commentService.save(commentDo);
	}

	@Override
	public int countOfQuantyForward(Map<String, Object> map) {
		return monthReportDao.countOfQuantyForward(map);
	}

	public void saveMonthReport(MonthReportDO monthReportDO, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment){
		if(monthReportDO.getId() == null || monthReportDO.getId() == 0){
			save(monthReportDO);
			//发送给谁
			for(int i=0;i<targetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(monthReportDO.getId(), ConstantForDevice.MONTH_REPORT_TARGET,targetList[i]);
				userAssocService.save(userAssocDO);
			}
		}else{
			update(monthReportDO);
		}
		//附件信息操作
		contentAssocService.saveList(monthReportDO.getId(),tagLocationAppearanceAttachment, ConstantForDevice.MONTH_REPORT_APPEARANCE_ATTACHMENT);
		contentAssocService.deleteList(deleteTagAppearanceAttachment);
	}

	@Override
	public void allPowerfulMelthod(MonthReportDO monthReportDO, Long[] targetList, String[] taglocationappearanceImage, int sign){

		if(sign==1){monthReportDO.setStatus(ConstantForDevice.APPLY_APPROED);} //148 已提交

		Long id=monthReportDO.getId();
		monthReportDao.update(monthReportDO);

		//删除发送人
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("userId", null);
		query.put("assocId", monthReportDO.getId());
		query.put("assocType", ConstantForDevice.MONTH_REPORT_TARGET);
		userAssocService.removeByAssocIdAndUserId(query);

		//保存新增发送人
		if(targetList.length>0){
			for(int i=0;i<targetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(id, ConstantForDevice.MONTH_REPORT_TARGET,targetList[i]);
				userAssocService.save(userAssocDO);
			}
		}
		//删除路径
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] =id;
		contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.MONTH_REPORT_APPEARANCE_ATTACHMENT);

		if(taglocationappearanceImage.length>0){
			contentAssocService.saveList(id,taglocationappearanceImage, ConstantForDevice.MONTH_REPORT_APPEARANCE_ATTACHMENT);
		}
	}


	@Override
	public R listOfCanDelet(Map<String, Object> map,Long[]ids) {
		List<Map<String, Object>> mapList = monthReportDao.listOfCanDelet(map);
		Map<String, Object> result = new HashMap<String, Object>();
		if(Objects.equals(mapList.size(),ids.length)){
			Long[] deleIds=new Long[mapList.size()];
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> maps=mapList.get(i);
				deleIds[i]= Long.parseLong(maps.get("id").toString());

			}
			monthReportDao.batchRemove(deleIds);

			Map<String, Object> query = new HashMap<String, Object>();
			query.put("assocId",deleIds );
			query.put("assocType", ConstantForDevice.MONTH_REPORT_TARGET);
			userAssocService.batchRemoveByAssocIdAadType(query);

			contentAssocService.removeByAssocIdAndType(deleIds, ConstantForDevice.MONTH_REPORT_APPEARANCE_ATTACHMENT);

			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));

	}

	@Override
	public Boolean duplicateDetectionOrNot() {
		int i = monthReportDao.monthOfHaveOrNot(ShiroUtils.getUserId());
		if(i==0) {
			return true;
		}else{
			return false;
		}
	}


}
