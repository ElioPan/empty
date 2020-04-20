package com.ev.custom.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.DailyReportDao;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.DailyReportDO;
import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.CommentService;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.DailyReportService;
import com.ev.custom.service.UserAssocService;
import com.ev.system.domain.UserDO;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EnableTransactionManagement
@Service
public class DailyReportServiceImpl implements DailyReportService {
	@Autowired
	private DailyReportDao dailyReportDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Override
	public DailyReportDO get(Long id){
		return dailyReportDao.get(id);
	}
	
	@Override
	public List<DailyReportDO> list(Map<String, Object> map){
		return dailyReportDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dailyReportDao.count(map);
	}
	
	@Override
	public int save(DailyReportDO dailyReport){
		return dailyReportDao.save(dailyReport);
	}
	
	@Override
	public int update(DailyReportDO dailyReport){
		return dailyReportDao.update(dailyReport);
	}
	
	@Override
	public int remove(Long id){
		return dailyReportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return dailyReportDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return dailyReportDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return dailyReportDao.countForMap(map);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String, Object> results = new HashMap<String, Object>();
		List<UserDO> userList = userService.list(null);
		results.put("userList", userList);
		Map<String, Object> query = new HashMap<String, Object>(){{
			put("id",id);
		}};
		DailyReportDO dailyReport = get(id);

		Map<String, Object> dailyOfDetail = dailyReportDao.dailyOfDetail(query);

		results.put("dailyReport", dailyOfDetail);
		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", dailyReport.getId());
			put("assocType", Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};

		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//获取发送对象
		List<Map<String, Object>> targetList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", dailyReport.getId());
			put("assocType", Constant.DAILY_REPORT_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("targetList", targetList);
		//获取回复信息
		Map<String, Object> commentMap = new HashMap<String, Object>() {{
			put("assocId", id);
			put("assocType", Constant.DAILY_REPORT_COMMENT);
		}};
//		List<CommentDO> commentList = commentService.list(commentMap);
		List<Map<String, Object>> commentList =commentService.listOfDetail(commentMap);
		results.put("commentList", commentList);
		return results;
	}

	@Override
	public void add(DailyReportDO dailyReportDO, Long[] targetList, String[] tagLocationAppearanceAttachment) {
		saveDailyReport(dailyReportDO,targetList,tagLocationAppearanceAttachment,null);
	}

	@Override
	public void edit(DailyReportDO dailyReportDO, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment) {
		saveDailyReport(dailyReportDO,targetList,tagLocationAppearanceAttachment,deleteTagAppearanceAttachment);
	}

	@Override
	public void commentDailyReport(Long dailyReportId, String comment) {
		CommentDO commentDo = new CommentDO(dailyReportId,Constant.DAILY_REPORT_COMMENT,comment);
		commentService.save(commentDo);
	}

	public void saveDailyReport(DailyReportDO dailyReportDO, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment){
		if(dailyReportDO.getId() == null || dailyReportDO.getId() == 0){
			dailyReportDao.save(dailyReportDO);

			for(int i=0;i<targetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(dailyReportDO.getId(),Constant.DAILY_REPORT_TARGET,targetList[i]);
				userAssocService.save(userAssocDO);
			}
		}else{
			dailyReportDao.update(dailyReportDO);
		}
		contentAssocService.saveList(dailyReportDO.getId(),tagLocationAppearanceAttachment,Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);
		contentAssocService.deleteList(deleteTagAppearanceAttachment);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveChangeDetail(DailyReportDO dailyReportDO, Long[] newTargetList,Long[] deletTargetList, String[] newAttachment, String[] deleteAttachment,int sign){

		if(sign==1){
			//使用情况：修改后直接提交
			dailyReportDO.setStatus(Constant.APPLY_APPROED); //148 已提交
		}
		    //更新日志信息
			update(dailyReportDO);

		//删除发送人
		if(deletTargetList.length>0){
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("userId", deletTargetList);
			query.put("assocId", dailyReportDO.getId());
			query.put("assocType", Constant.DAILY_REPORT_TARGET);
			userAssocService.removeByAssocIdAndUserId(query);
		}
			//保存新增发送人
		if(newTargetList.length>0){

			for(int i=0;i<newTargetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(dailyReportDO.getId(),Constant.DAILY_REPORT_TARGET,newTargetList[i]);
				userAssocService.save(userAssocDO);
			}
		}

		   //删除路径 +增加路径保存
		if(newAttachment.length>0){
			contentAssocService.saveList(dailyReportDO.getId(),newAttachment,Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);
		}
		if(deleteAttachment.length>0){
			contentAssocService.deleteList(deleteAttachment);
		}
	}

	@Override
	public void allPowerfulMelthod(DailyReportDO dailyReportDO,  Long[] targetList,String[] taglocationappearanceImage,int sign){

		if(sign==1){dailyReportDO.setStatus(Constant.APPLY_APPROED);} //148 已提交

		Long id=dailyReportDO.getId();
		dailyReportDao.update(dailyReportDO);

		//删除发送人
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("userId", null);
			query.put("assocId", dailyReportDO.getId());
			query.put("assocType", Constant.DAILY_REPORT_TARGET);
			userAssocService.removeByAssocIdAndUserId(query);

			//保存新增发送人
		if(targetList.length>0){
			for(int i=0;i<targetList.length;i++){
				UserAssocDO userAssocDO = new UserAssocDO(id,Constant.DAILY_REPORT_TARGET,targetList[i]);
				userAssocService.save(userAssocDO);
			}
		}
		//删除路径
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
			ids[0] =id;
		contentAssocService.removeByAssocIdAndType(ids, Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);

		if(taglocationappearanceImage.length>0){
			contentAssocService.saveList(id,taglocationappearanceImage,Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);
		}

	}

	@Override
	public int countOfQuantyForward(Map<String, Object> map) {
		return dailyReportDao.countOfQuantyForward(map);
	}

	@Override
	public R listOfCanDelet(Map<String, Object> map,Long[] ids) {
		List<Map<String, Object>> mapList = dailyReportDao.listOfCanDelet(map);
		Map<String, Object> result = new HashMap<>();

		if(Objects.equals(mapList.size(),ids.length)){
			Long[] deleIds=new Long[mapList.size()];
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> maps=mapList.get(i);
				deleIds[i]= Long.parseLong(maps.get("id").toString());
			}
			dailyReportDao.batchRemove(deleIds);

			Map<String, Object> query = new HashMap<>();
			query.put("assocId",deleIds );
			query.put("assocType", Constant.DAILY_REPORT_TARGET);
			userAssocService.batchRemoveByAssocIdAadType(query);

			contentAssocService.removeByAssocIdAndType(deleIds, Constant.DAILY_REPORT_APPEARANCE_ATTACHMENT);

			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
	}

	/*
	   验证当前登录人当天是是否重建日志
	 */
	@Override
	public Boolean duplicateDetectionOrNot(){
		int i = dailyReportDao.haveOrNot(ShiroUtils.getUserId());
		if(i==0) {
			return true ;
		}else{
			return false;
		}
	}




}
