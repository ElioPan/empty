package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.R;
import com.ev.mes.dao.ProcessReportDao;
import com.ev.mes.dao.WorkingProcedurePlanDao;
import com.ev.mes.domain.DispatchItemDO;
import com.ev.mes.domain.DispatchWorkingHungDO;
import com.ev.mes.domain.ProcessReportDO;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.service.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class ProcessReportServiceImpl implements ProcessReportService {
	@Autowired
	private ProcessReportDao processReportDao;
	@Autowired
	private DispatchItemService dispatchItemService;
	@Autowired
	private DispatchWorkingHungService dispatchWorkingHungService;
	@Autowired
	private WorkingProcedureDetailService workingProcedureDetailService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private WorkingProcedurePlanDao workingProcedurePlanDao;
	@Autowired
	private WorkingProcedurePlanService workingProcedurePlanService;
	@Autowired
	private ReworkRepairMiddleService reworkRepairMiddleService;
	@Autowired
	private ProcessReportCheckService processReportCheckService;
	
	@Override
	public ProcessReportDO get(Long id){
		return processReportDao.get(id);
	}
	
	@Override
	public List<ProcessReportDO> list(Map<String, Object> map){
		return processReportDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processReportDao.count(map);
	}
	
	@Override
	public int save(ProcessReportDO processReport){
		return processReportDao.save(processReport);
	}
	
	@Override
	public int update(ProcessReportDO processReport){
		return processReportDao.update(processReport);
	}
	
	@Override
	public int remove(Long id){
		return processReportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processReportDao.batchRemove(ids);
	}

	@Override
	public R saveAndchangeSbumit(String reportDetai,int sign){

		if(Objects.nonNull(reportDetai)){

			List<ProcessReportDO> rocessReportDO = JSON.parseArray(reportDetai, ProcessReportDO.class);
			ProcessReportDO rocessReportDO1=rocessReportDO.get(0);

			if(sign==1){
				rocessReportDO1.setStatus(ConstantForMES.MES_APPLY_APPROED);//已提交

				if(Objects.nonNull(dispatchItemService.get(rocessReportDO1.getDispatchItemId()))){
					//反写已完工数量 +将如果存在的开工记录补充完整
					this.countOfRetrography(rocessReportDO1);
				}else{
					//"所传工单明细主键下无数据！"
					return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
				}
			}else{
				rocessReportDO1.setStatus(Constant.TS); //暂存
			}

			if(Objects.nonNull(rocessReportDO1.getId())){
				//修改
				processReportDao.update(rocessReportDO1);
				return R.ok();
			}else{
				//新增
				//<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
				String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.DISPATCH_GXBG, new Date());
				Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
				params.put("maxNo", prefix);
				params.put("offset", 0);
				params.put("limit", 1);
				List<ProcessReportDO> list = processReportDao.list(params);
				String suffix = null;
				if (list.size() > 0) {
					suffix = list.get(0).getCode();
				}
				rocessReportDO1.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
				rocessReportDO1.setCreateTime(new Date());

				processReportDao.save(rocessReportDO1);
				return R.ok();
			}
		}else{
			//"所传数据为空，请检查参数！"
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public Map<String, Object> reportDetailById(Map<String, Object> map) {
		return processReportDao.reportDetailById(map);
	}


	//反写已完工数量
	public  void  countOfRetrography(ProcessReportDO rocessReportDO){
		DispatchItemDO dispatchItemDO = dispatchItemService.get(rocessReportDO.getDispatchItemId());

		BigDecimal complationCount= rocessReportDO.getCompletionCount().add(dispatchItemDO.getCompletionCount());
		int result= complationCount.compareTo(dispatchItemDO.getPlanCount());
		if(result==-1){
			//更新工单已完工数量
			dispatchItemDO.setCompletionCount(complationCount);
			dispatchItemDO.setActualEndTime(new Date());
			dispatchItemService.update(dispatchItemDO);

		}else if(result==0 ||result==1){
			//1.更新工单数量 将工单状态改为已结案 并记录实际完工时间 并将开工记录挂起(补充完整)（不新建挂起记录）
			dispatchItemDO.setCompletionCount(complationCount);
			dispatchItemDO.setActualEndTime(new Date());
			dispatchItemDO.setStatus(ConstantForMES.CLOSE_CASE);//结案

			this.closeWorkRecord(dispatchItemDO.getId());
			dispatchItemService.update(dispatchItemDO);

			// 2.反写完工数量累加至工序计划明细中，并验证工单是否全部结案，若全部结案且完工数量等于或大于计划数量时 将时间反写至供需计划明细中

			WorkingProcedureDetailDO workingProcedureDetailDO = workingProcedureDetailService.get(dispatchItemDO.getForiegnId());
			BigDecimal WorkcomplationCount= complationCount.add(Objects.nonNull(workingProcedureDetailDO.getCompletionCount())?workingProcedureDetailDO.getCompletionCount():BigDecimal.ZERO);
			workingProcedureDetailDO.setCompletionCount(WorkcomplationCount);

    			int results=WorkcomplationCount.compareTo(workingProcedureDetailDO.getPlanCount());//实际完成数量和工序计划数量比较


			Map<String ,Object> paramy=new HashMap<String ,Object>();
			paramy.put("workDetailId",workingProcedureDetailDO.getId());
			int dispatchRows = processReportDao.countOfDisItemAboutWorkDetail(paramy);//派工的工单数量

			paramy.put("status",ConstantForMES.CLOSE_CASE);
			int CloseLines = processReportDao.countOfDisItemAboutWorkDetail(paramy);//已结案的工单数量

			if(Objects.equals(dispatchRows,CloseLines)&&(results==0 ||results==1)){
				workingProcedureDetailDO.setRealityEndTime(new Date());
			}
			workingProcedureDetailService.update(workingProcedureDetailDO);

		}
	}


	//将如果存在的开工记录补充完整
	public void closeWorkRecord(Long  Id){
		Map<String, Object> wHungTime = dispatchWorkingHungService.getWHungTime(1,Id);//查询是否已有开工记录
		if(wHungTime!=null){
			//更新 hung_time ,计算man_hour;+++新增挂起记录
			Date startTime = DateFormatUtil.getDateByParttern(wHungTime.get("startTime").toString(), "yyyy-MM-dd HH:mm:ss");
			BigDecimal dateHour = DatesUtil.dateHour( startTime,new Date());

			DispatchWorkingHungDO disWorkingHungDO2=new DispatchWorkingHungDO();
			disWorkingHungDO2.setId(Long.valueOf(wHungTime.get("id").toString()));
			disWorkingHungDO2.setHangTime(new Date());
			disWorkingHungDO2.setManHour(dateHour);
			//更新原有挂机记录的 挂机结束时间（hung_time）和时长
			dispatchWorkingHungService.update(disWorkingHungDO2);
		}
	}

	@Override
	public R deletOfReports(Long[] ids){
		Map<String,Object> pamry=new HashMap<String,Object>();
		pamry.put("id",ids);
		int i = processReportDao.canDeletOfReport(pamry);
		//無报检单+无返工返修的工序计划  允许删除
		pamry.put("processReportId",ids);
		int i1 = processReportCheckService.countDeleAboutCheck(pamry);

		pamry.put("type",ConstantForMES.REWORK_REPORT_YPE);
		pamry.put("sourceId",ids);
		int i2 = reworkRepairMiddleService.canDelReportAboutMiddle(pamry);

		if(i1!=0||i2!=0){
			return  R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
		}else{
			processReportDao.batchRemove(ids);
			return R.ok();
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return processReportDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return processReportDao.countForMap(map);
	}


	@Override
	public R reworkRepair(Long id,int sign ) {

		//关联查出工序计划的id，及此条报工单关联的明细计划下明细的id  +++ 报工单的返工数量
		Map<String,Object>  msgForSynthesize= new HashMap<>();
		Map<String,Object>  signs= new HashMap<>();
		int reworkCount;
		Map<String, Object> map = new HashMap<>();
		if(sign==0){
			map.put("reportId", id);
			signs.put("type",ConstantForMES.REWORK_REPORT_YPE);
			signs.put("sourceId",id);
			//检验中间表
			int count = reworkRepairMiddleService.count(signs);
			if(count>0){
				return  R.error(messageSourceHandler.getMessage("apis.mes.reworkRepair.banRepeated", null));
			}
			msgForSynthesize = processReportDao.getMsgForSynthesize(map);
			if(msgForSynthesize!=null){
				if(Objects.equals("0",msgForSynthesize.get("reportReworkCount").toString())){
					reworkCount=Integer.parseInt(msgForSynthesize.get("checkReworkCount").toString());
				}else{
					reworkCount=Integer.parseInt(msgForSynthesize.get("reportReworkCount").toString());
				}
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing", null));
			}
		}else{
			map.put("checkId", id);
			signs.put("type",ConstantForMES.REWORK_CHECK_TYPE);
			signs.put("sourceId",id);
			int count = reworkRepairMiddleService.count(signs);
			if(count>0){
				return  R.error(messageSourceHandler.getMessage("apis.mes.reworkRepair.banRepeated", null));
			}
			msgForSynthesize = processReportDao.getMsgForSynthesize(map);
			if(msgForSynthesize!=null){
				reworkCount=Integer.parseInt(msgForSynthesize.get("checkReworkCount").toString());
			}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing", null));
			}
		}

		if (msgForSynthesize!=null) {
			if (reworkCount == 0) {

				return R.error(messageSourceHandler.getMessage("apis.mes.reportAndCheck.rework", null));

			} else if (msgForSynthesize!=null && msgForSynthesize.containsKey("planId")&& msgForSynthesize.containsKey("planitemId")) {
				//给数据

				Map<String, Object> detailInfo = workingProcedurePlanService.getDetailInfo(Long.parseLong(msgForSynthesize.get("planId").toString()), Long.parseLong(msgForSynthesize.get("planitemId").toString()));

				//headInfo
				Map<String, Object> headInfo = (Map<String, Object>)detailInfo.get("headInfo");
				headInfo.put("planStatusId", ConstantForMES.PLAN);
				headInfo.put("planStatusName", "计划");
				//bodyInfo
				Map<String, Object> bodyInfo = (Map<String, Object>) detailInfo.get("bodyInfo");
				bodyInfo.put("planCount", reworkCount);
				bodyInfo.put("processTypeId", ConstantForMES.REWPRKER);
				bodyInfo.put("processTypeName", "返工返修");
				map.clear();
				map.put("headInfo",headInfo);
				map.put("bodyInfo",bodyInfo);
				map.put("sourceInfo",signs);
				map.put("planId",msgForSynthesize.get("planId"));
				return R.ok(map);
			} else {
				return R.error(messageSourceHandler.getMessage("apis.mes.reportAndCheck.haveNoUpstream", null));
			}
		} else {
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing", null));
		}
	}










}
