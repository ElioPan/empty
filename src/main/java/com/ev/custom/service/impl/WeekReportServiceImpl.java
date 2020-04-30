package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.WeekReportDao;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.system.domain.UserDO;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EnableTransactionManagement
@Service
public class WeekReportServiceImpl implements WeekReportService {
	@Autowired
	private WeekReportDao weekReportDao;

	@Autowired
	private UserAssocService userAssocService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private ReportItemService reportItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public WeekReportDO get(Long id){
		return weekReportDao.get(id);
	}
	
	@Override
	public List<WeekReportDO> list(Map<String, Object> map){
		return weekReportDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return weekReportDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return weekReportDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return weekReportDao.countForMap(map);
	}

	@Override
	public int save(WeekReportDO weekReport){
		return weekReportDao.save(weekReport);
	}
	
	@Override
	public int update(WeekReportDO weekReport){
		return weekReportDao.update(weekReport);
	}
	
	@Override
	public int remove(Long id){
		return weekReportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return weekReportDao.batchRemove(ids);
	}

	@Override
	public void add(WeekReportDO weekReportDO, String weekReportItems, Long[] targetList, String[] taglocationappearanceImage) {
		saveWeekReport(weekReportDO,weekReportItems, targetList,taglocationappearanceImage);
	}

	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = new HashMap<String,Object>();
		List<UserDO> userList = userService.list(null);
		results.put("userList",null);

		WeekReportDO weekReport = weekReportDao.get(id);

		Map<String,Object> query = new HashMap<String,Object>();
		query.put("id",id);

		Map<String,Object> weekOfDetail = weekReportDao.weekOfDetail(query);
		results.put("weekReport", weekOfDetail);
		//获取附件
		Map<String, Object> contentMap = new HashMap<String, Object>() {{
			put("assocId", weekReport.getId());
			put("assocType", ConstantForDevice.WEEK_REPORT_APPEARANCE_ATTACHMENT);
			put("sort","id");
			put("order","ASC");
		}};
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);
		results.put("initFileList", contentAssocDOS);
		//获取发送对象
		List<Map<String, Object>> targetList = userAssocService.list(new HashMap<String, Object>() {{
			put("assocId", weekReport.getId());
			put("assocType", ConstantForDevice.WEEK_REPORT_TARGET);
			put("sort","id");
			put("order","ASC");
		}});
		results.put("targetList", targetList);

		//获取回复信息
		Map<String,Object> commentMap = new HashMap<String,Object>(){{put("assocId",id);put("assocType", ConstantForDevice.WEEK_REPORT_COMMENT);}};
//		List<CommentDO> commentList = commentService.list(commentMap);
		List<Map<String, Object>> commentList =commentService.listOfDetail(commentMap);
		results.put("commentList", commentList);
		//获取明细信息
		List<ReportItemDO> itemDOList = reportItemService.list(new HashMap<String,Object>(){{put("weekReportId",id);}});
		results.put("itemList", itemDOList);
		return results;
	}

	@Override
	public void edit(WeekReportDO weekReportDO,String weekReportItems, Long[] targetList, String[] tagLocationAppearanceAttachment, String[] deleteTagAppearanceAttachment) {
		saveWeekReport(weekReportDO,weekReportItems, targetList,tagLocationAppearanceAttachment);
	}

	@Override
	public void commentWeekReport(Long weekReportId, String comment) {
		CommentDO commentDo = new CommentDO(weekReportId, ConstantForDevice.WEEK_REPORT_COMMENT,comment);
		commentService.save(commentDo);
	}

	public void saveWeekReport(WeekReportDO weekReportDO, String weekReportItems, Long[] targetList, String[] tagLocationAppearanceAttachment) {

		List<ReportItemDO> items = JSON.parseArray(weekReportItems, ReportItemDO.class);
		if (weekReportDO.getId() == null || weekReportDO.getId() == 0) {
			for (ReportItemDO obj : items) {
				if (Objects.equals("星期一", obj.getWeek())) {
					weekReportDO.setStartTime(obj.getReportDate());

				} else if (Objects.equals("星期日", obj.getWeek())) {
					weekReportDO.setEndTime(obj.getReportDate());
				}
			}
			save(weekReportDO);
			//发送给谁
			for (int i = 0; i < targetList.length; i++) {
				UserAssocDO userAssocDO = new UserAssocDO(weekReportDO.getId(), ConstantForDevice.WEEK_REPORT_TARGET, targetList[i]);
				userAssocService.save(userAssocDO);
			}
		} else {
			update(weekReportDO);
		}
		//附件信息操作
		contentAssocService.saveList(weekReportDO.getId(), tagLocationAppearanceAttachment, ConstantForDevice.WEEK_REPORT_APPEARANCE_ATTACHMENT);
//		contentAssocService.deleteList(deleteTagAppearanceAttachment);
		//周报明细保存
		for (ReportItemDO obj : items) {
			if (obj.getId() != null) {
				reportItemService.update(obj);
			} else {
				obj.setWeekReportId(weekReportDO.getId());
				reportItemService.save(obj);
			}
		}
	}

//
	@Override
	public void saveWeekChangeAndSbmit(WeekReportDO weekReportDO, String newWeekReportItems,Long[] newSenderIds, String[] newAttachment, int sign) {

		if (sign == 1) {
			weekReportDO.setStatus(ConstantForDevice.APPLY_APPROED);//148已提交
		}

		weekReportDao.update(weekReportDO);

		//新增发送人+删除发送人

			Map<String, Object> query = new HashMap<String, Object>();
			query.put("userId", null);
			query.put("assocId", weekReportDO.getId());
			query.put("assocType", ConstantForDevice.WEEK_REPORT_TARGET);
			userAssocService.removeByAssocIdAndUserId(query);

		if (newSenderIds.length>0) {

			for (int i = 0; i < newSenderIds.length; i++) {
				UserAssocDO userAssocDO = new UserAssocDO(weekReportDO.getId(), ConstantForDevice.WEEK_REPORT_TARGET, newSenderIds[i]);
				userAssocService.save(userAssocDO);
			}
		}

		//删除路径 +增加路径保存
		Map<String,Object> querys =new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] =weekReportDO.getId();
		contentAssocService.removeByAssocIdAndType(ids, ConstantForDevice.WEEK_REPORT_APPEARANCE_ATTACHMENT);

		if (newAttachment.length>0) {
			contentAssocService.saveList(weekReportDO.getId(), newAttachment, ConstantForDevice.WEEK_REPORT_APPEARANCE_ATTACHMENT);
		}

		//周报明细   新增+修改

		if (newWeekReportItems != null && !"".equals(newWeekReportItems)) {
			List<ReportItemDO> items = JSON.parseArray(newWeekReportItems, ReportItemDO.class);
			for (ReportItemDO obj : items) {
					reportItemService.update(obj);
			}
		}
	}

	@Override
	public int countOfQuantyForward(Map<String, Object> map) {
		return weekReportDao.countOfQuantyForward(map);
	}


	@Override
	public R listOfCanDelet(Map<String, Object> map,Long[]ids) {

		List<Map<String, Object>> mapList = weekReportDao.listOfCanDelet(map);
		Map<String, Object> result = new HashMap<String, Object>();
		if(Objects.equals(mapList.size(),ids.length)){
			Long[] deleIds=new Long[mapList.size()];
			for(int i=0;i<mapList.size();i++){
				Map<String, Object> maps=mapList.get(i);
				deleIds[i]= Long.parseLong(maps.get("id").toString());
			}
			weekReportDao.batchRemove(deleIds);

			Map<String, Object> query = new HashMap<String, Object>();
			query.put("assocId",deleIds );
			query.put("assocType", ConstantForDevice.WEEK_REPORT_TARGET);
			userAssocService.batchRemoveByAssocIdAadType(query);

			contentAssocService.removeByAssocIdAndType(deleIds, ConstantForDevice.WEEK_REPORT_APPEARANCE_ATTACHMENT);

			return R.ok();
		}
		//"单据已提交，不能删除！"
		return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
	}


	/*
	    验证周报是否可以建立
	 */
	@Override
	public Boolean duplicateDetectionOrNot(String weekReportItems) {

		List<ReportItemDO> items = JSON.parseArray(weekReportItems, ReportItemDO.class);
		String startTimeOfMonday=null;
		String endTimeOfSunday=null;
			for (ReportItemDO obj : items) {
				if (Objects.equals("星期一", obj.getWeek())) {

					startTimeOfMonday=DateFormatUtil.getFormateDate(obj.getReportDate(),"yyyy-MM-dd");

				} else if (Objects.equals("星期日", obj.getWeek())) {

					endTimeOfSunday= DateFormatUtil.getFormateDate(obj.getReportDate(),"yyyy-MM-dd");

				}
			}
		Map<String, Object> query = new HashMap<String, Object>() ;
		query.put("startTimeOfMonday", startTimeOfMonday);
		query.put("endTimeOfSunday", endTimeOfSunday);
		query.put("createBy", ShiroUtils.getUserId());

		int i = weekReportDao.weekHaveOrNot(query);
		if (i == 0) {
			return true;
		} else {
			return false;
		}
	}



}
