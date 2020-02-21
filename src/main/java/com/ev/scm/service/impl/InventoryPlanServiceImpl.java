package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.scm.dao.InventoryPlanDao;
import com.ev.scm.dao.InventoryPlanFitlossDao;
import com.ev.scm.domain.InventoryPlanDO;
import com.ev.scm.domain.InventoryPlanItemDO;
import com.ev.scm.service.InventoryPlanFitlossService;
import com.ev.scm.service.InventoryPlanItemService;
import com.ev.scm.service.InventoryPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class InventoryPlanServiceImpl implements InventoryPlanService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private InventoryPlanDao inventoryPlanDao;
	@Autowired
	private InventoryPlanItemService inventoryPlanItemService;
	@Autowired
	private InventoryPlanService inventoryPlanService;
	@Autowired
	private InventoryPlanFitlossService inventoryPlanFitlossService;
	@Autowired
	private InventoryPlanFitlossDao inventoryPlanFitlossDao;
	
	@Override
	public InventoryPlanDO get(Long id){
		return inventoryPlanDao.get(id);
	}
	
	@Override
	public List<InventoryPlanDO> list(Map<String, Object> map){
		return inventoryPlanDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return inventoryPlanDao.count(map);
	}
	
	@Override
	public int save(InventoryPlanDO checkHead){
		return inventoryPlanDao.save(checkHead);
	}
	
	@Override
	public int update(InventoryPlanDO checkHead){
		return inventoryPlanDao.update(checkHead);
	}
	
	@Override
	public int remove(Long id){
		return inventoryPlanDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return inventoryPlanDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listByDates(Map<String, Object> map) {
		return inventoryPlanDao.listByDates(map);
	}

	@Override
	public List<Map<String, Object>> getProMsgByHeadId(Map<String, Object> map) {
		return inventoryPlanDao.getProMsgByHeadId(map);
	}

	@Override
	public int countOfListByDates(Map<String, Object> map) {
		return inventoryPlanDao.countOfListByDates(map);
	}

	@Override
	public int countOfStatus(Map<String, Object> map) {
		return inventoryPlanDao.countOfStatus(map);
	}

	@Override
	public R getMaterielCount(Long warehouse, String syntheticData){

		//同一商品+同一仓库+同一库位+同一批次 商品数量为总数量 为一行明细
		Map<String,Object> result = Maps.newHashMap();
		Map<String,Object> query = Maps.newHashMap();
		query.put("warehouse",warehouse);
		query.put("name",syntheticData);

		List<Map<String, Object>> proMsgCount = inventoryPlanItemService.getProMsgCount(query);
		if(proMsgCount.size()>0){
			result.put("data",proMsgCount);
			return R.ok(result);
		}else{
			return R.ok(result);
		}
	}

	@Override
	public R addInventoryPlan(InventoryPlanDO checkHeadDO, String checkBodys, Long[] deleItemIds) {
		//提箱前端当选择的仓库为“所有仓库”时 仓库字段放空值

		InventoryPlanDO inventoryPlanDO = inventoryPlanService.get(checkHeadDO.getId());

		if (Objects.nonNull(inventoryPlanDO)) {
			if (Objects.equals(inventoryPlanDO.getCheckStatus(), ConstantForGYL.EXECUTE_NON)) {

				int rows = inventoryPlanService.update(checkHeadDO);
				if (rows > 0) {
					if (Objects.nonNull(checkBodys)) {
						List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);

						for (InventoryPlanItemDO bodyNew : lsitBodyDO) {
							if (Objects.nonNull(bodyNew.getId())) {
								inventoryPlanItemService.update(bodyNew);
							} else {
								bodyNew.setHeadId(checkHeadDO.getId());
								inventoryPlanItemService.save(bodyNew);
							}
						}
					}
					if (deleItemIds.length > 0) {
						inventoryPlanItemService.batchRemove(deleItemIds);
					}
					return R.ok();
				} else {
					return R.error(messageSourceHandler.getMessage("apis.check.saveChangePlan", null));
				}
			} else {
				return R.error(messageSourceHandler.getMessage("apis.check.saveChangePlanTwo", null));
			}
	}else{
		checkHeadDO.setCode(purchaseContractCode());
		checkHeadDO.setCheckStatus(ConstantForGYL.EXECUTE_NON);
		int rows = inventoryPlanService.save(checkHeadDO);
		List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
		if (rows > 0) {

			for (InventoryPlanItemDO body : lsitBodyDO) {
				body.setHeadId(checkHeadDO.getId());
				inventoryPlanItemService.save(body);
			}
			return R.ok();
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.addCheck", null));
		}
	}

}

	@Override
	public R deletBatchPlanNow(Long planId){

		// 如果为执行中或者已执行完毕的状态则不允许删除
		InventoryPlanDO checkHeadDO = inventoryPlanService.get(planId);
		if ( Objects.equals(checkHeadDO.getCheckStatus(),ConstantForGYL.EXECUTE_NON)) {// 23-->190未执行

			//允许删除
			inventoryPlanService.remove(planId);
			inventoryPlanItemService.removeByPlanId(planId);
			return R.ok();
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.deletPlan",null));
		}
	}

	@Override
	public R doInventoryPlan(Long planId){

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("id", planId);

		List<Map<String, Object>> headDetails = inventoryPlanService.listByDates(params);
		List<Map<String, Object>> bodyDetails = inventoryPlanService.getProMsgByHeadId(params);
		params.remove("id");

		if (headDetails.size() > 0 ) {
			params.put("headDetails", headDetails);
			params.put("bodyDetails", bodyDetails);
		} else {
			params.put("headDetails", "");
			params.put("bodyDetails", "");
		}
		result.put("data", params);
		return R.ok(result);
	}

	private String purchaseContractCode() {
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.PLAN_PDFA);
		Map<String, Object> param = com.google.common.collect.Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<InventoryPlanDO> list = this.list(param);
		String taskNo = null;
		if (!list.isEmpty()) {
			taskNo = list.get(0).getCode();
		}
		return DateFormatUtil.getWorkOrderno(maxNo, taskNo);
	}


	@Override
	public R savePlanDetail(InventoryPlanDO checkHeadDO,String checkBodys){
		//只能修改状态为24，且未生成盈亏单的方案，未生成盈亏的盘点结果。
		Long planId =checkHeadDO.getId();
		InventoryPlanDO checkStatus = inventoryPlanService.get(planId);
		checkHeadDO.setId(planId);
		Map<String, Object> queryPy = Maps.newHashMap();
		Map<String, Object> queryPk =Maps.newHashMap();
		queryPy.put("headId", planId);
		queryPy.put("documentType", ConstantForGYL.PYDJ);
		queryPk.put("headId", planId);
		queryPk.put("documentType", ConstantForGYL.PKDJ);
		int rowsPy = inventoryPlanFitlossDao.countOfYKCount(queryPy);
		int linesPk = inventoryPlanFitlossDao.countOfYKCount(queryPk);

		if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus()) && rowsPy != 0) {
			//此盘点已生成盘盈单，不允许修改
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResult",null));
		} else if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus()) && linesPk != 0) {
			//此盘点已生成盘亏单，不允许修改！
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResultTwo",null));
		} else if ( Objects.equals(ConstantForGYL.EXECUTE_NON ,checkStatus.getCheckStatus())) {
			//允许 且将状态改为24
			// 23-->190未执行 24-->191执行中 25--->192执行结束
			checkHeadDO.setCheckStatus(ConstantForGYL.EXECUTE_NOW );
			int rows = inventoryPlanService.update(checkHeadDO);
			if (rows > 0) {
				if (null != checkBodys && !"".equals(checkBodys)) {
					List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
					for (InventoryPlanItemDO body : lsitBodyDO) {
						body.setHeadId(checkHeadDO.getId());
						inventoryPlanItemService.update(body);
					}
				}
				return R.ok();
			} else {
				//保存方案失败，请检查参数值！
				return R.error(messageSourceHandler.getMessage("apis.check.addCheck",null));
			}
		} else if (Objects.equals(ConstantForGYL.EXECUTE_NOW ,checkStatus.getCheckStatus())  && rowsPy == 0 && linesPk == 0) {
			//允许  不用改变此时24-->191执行中
			int rows = inventoryPlanService.update(checkHeadDO);
			if (rows > 0) {
				if (null != checkBodys && !"".equals(checkBodys)) {
					List<InventoryPlanItemDO> lsitBodyDO = JSON.parseArray(checkBodys, InventoryPlanItemDO.class);
					for (InventoryPlanItemDO body : lsitBodyDO) {
						body.setHeadId(checkHeadDO.getId());
						inventoryPlanItemService.update(body);
					}
				}
				return R.ok();
			} else {
				return R.error(messageSourceHandler.getMessage("apis.check.addCheck",null));
			}
		} else {
			return R.error(messageSourceHandler.getMessage("apis.check.saveChangeResultT",null));
		}
	}

	@Override
	public R buildWinStock(Long planId ){
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("id", planId);
		if (inventoryPlanService.count(params)!= 0) {
			params.remove("id");
			params.put("headId", planId);
			params.put("profitLoss", 1);
			params.put("checkStatus",  ConstantForGYL.EXECUTE_NOW);//24-->191执行中
			//根据盘点结果组织数据(所查询数据状态为24执行中)；
			//判断是否能够生成盘赢单：主表checkStatus为25时||profit_loss盈亏数量>0&&盘盈表有盘点方案主键的 不允许再次生成盘盈单
			int rows = inventoryPlanItemService.countOfWinLoss(params);//是否有盘盈(可以/需要生成盘盈单) （rows>0 是）
			InventoryPlanDO headDO = inventoryPlanService.get(planId);    //获取状态 是否是24
			List<Map<String, Object>> profitLossMsg = inventoryPlanItemService.getProfitLossMsg(params);//查找出盈数据

			params.put("documentType", ConstantForGYL.PYDJ );  //  32--->198//PYDJ  盘盈单据
			int otherInLines = inventoryPlanFitlossService.countOfOtherByPY(params);//是否已经生成其他入库 headId+documentType
			int linesPL = inventoryPlanFitlossService.count(params);   //是否已经生成盘赢单（lines>0 是）

			if (!(Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NON))) {  //   23-->190未执行
				if ( Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER) && rows > 0 && otherInLines == 0) {//  25--->192执行结束
						//	EXECUTE_NOW

					//允许返回生成其他入库的数据，但是盈亏单不更新
					params.put("checkStatus", ConstantForGYL.EXECUTE_OVER);//  25--->192执行结束
					List<Map<String, Object>> profitLossMsgOer = inventoryPlanItemService.getProfitLossMsg(params);
					result.put("BodyData", profitLossMsgOer);
					return R.ok(result);

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER) && rows > 0 && otherInLines > 0) {
					//盘盈数据已生成其他入库，无法再次生成！
					return R.error(messageSourceHandler.getMessage("apis.check.buildWinStock",null));

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER) && rows == 0) {
					//"此次盘点无盘盈！"
					return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockA",null));

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows > 0 && otherInLines == 0 && linesPL == 0) {//   24-->191执行中

					//将盘盈数据保存至盈亏表中,保存后并验证更改方案的状态为25
					Boolean aBoolean = inventoryPlanFitlossService.saveProfitORLoss(profitLossMsg, 32L);

					//返回生成其他入库的数据。
					params.remove("checkStatus");
					params.remove("documentType");
					List<Map<String, Object>> profitLossMsgNow = inventoryPlanItemService.getProfitLossMsg(params);

					result.put("BodyData", profitLossMsgNow);
					return R.ok(result);
				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows > 0 && otherInLines == 0 && linesPL > 0) {

					//返回生成其他入库的数据。
					result.put("BodyData", profitLossMsg);
					return R.ok(result);
				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows > 0 && otherInLines > 0) {
					//盘盈数据已生成其他入库，无法再次生成
					return R.error(messageSourceHandler.getMessage("apis.check.buildWinStock",null));
				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows == 0) {
					//此次盘点无盘盈
					return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockA",null));
				} else {

					return R.error();
				}
			} else {
				//盘点结果未保存
				return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockC",null));
			}
		} else {
			//请传入正确id！！
			return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
		}

	}

	@Override
	public R buildLossStock(Long planId ){
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("id", planId);
		if (inventoryPlanService.count(params) != 0) {
			params.remove("id");
			params.put("headId", planId);
			params.put("profitLoss", -1);
			params.put("checkStatus", ConstantForGYL.EXECUTE_NOW);

			//根据盘点结果组织数据(所查询数据状态为24执行中)；
			//判断是否能够生成盘赢单：主表checkStatus为25时||profit_loss盈亏数量>0&&盘盈表有盘点方案主键的 不允许再次生成盘盈单
			//是否有盘盈(可以/需要生成盘盈单) （rows>0 是）
			int rows = inventoryPlanItemService.countOfWinLoss(params);
			//获取状态 是否是24
			InventoryPlanDO headDO = inventoryPlanService.get(planId);
			List<Map<String, Object>> profitLossMsg = inventoryPlanItemService.getProfitLossMsg(params);//产找出盘亏数据

			params.put("documentType", ConstantForGYL.PKDJ);
			//入库子表中是否已经存在
			int otherInLines = inventoryPlanFitlossService.countOfOutByPK(params);
			//是否已经生成盘盈单（lines>0 是）
			int linesPL = inventoryPlanFitlossService.count(params);

			if (!(Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NON ))) {
				if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER)&& rows > 0 && otherInLines == 0) {

					//允许返回生成其他入库的数据，但是盈亏单不更新
					params.put("checkStatus", ConstantForGYL.EXECUTE_OVER);
					List<Map<String, Object>> profitLossMsgOer = inventoryPlanItemService.getProfitLossMsg(params);
					result.put("BodyData", profitLossMsgOer);
					return R.ok(result);

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER) && rows > 0 && otherInLines > 0) {
					//盘亏数据已生成其他出库，无法再次生成！
					return R.error(messageSourceHandler.getMessage("apis.check.buildLossStock",null));

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_OVER) && rows == 0) {
					//此次盘点无盘亏
					return R.ok(messageSourceHandler.getMessage("apis.check.buildLossStockA",null));

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW)&& rows > 0 && otherInLines == 0 && linesPL == 0) {
					//将盘盈数据保存至盈亏表中,保存后并验证更改方案的状态为25
					//Boolean aBoolean = inventoryPlanFitlossService.saveProfitORLoss(profitLossMsg, 28L);
					//返回生成其他入库的数据。
					params.remove("checkStatus");
					params.remove("documentType");
					List<Map<String, Object>> profitLossMsgNow = inventoryPlanItemService.getProfitLossMsg(params);

					result.put("BodyData", profitLossMsgNow);
					return R.ok(result);

				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows > 0 && otherInLines == 0 && linesPL > 0) {
					result.put("BodyData", profitLossMsg);
					return R.ok(result);
				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW)&& rows > 0 && otherInLines > 0) {
					//盘亏数据已生成其他出库，无法再次生成
					return R.error(messageSourceHandler.getMessage("apis.check.buildLossStock",null));
				} else if (Objects.equals(headDO.getCheckStatus(),ConstantForGYL.EXECUTE_NOW) && rows == 0) {
					//此次盘点无盘亏
					return R.ok(messageSourceHandler.getMessage("apis.check.buildLossStockA",null));
				} else {
					return R.error();
				}
			} else {
				return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockC",null));
			}
		} else {
			return R.ok(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
		}
	}


}
