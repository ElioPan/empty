package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.mes.dao.MaterialsScrapDao;
import com.ev.mes.domain.MaterialsScrapDO;
import com.ev.mes.domain.MaterialsScrapItemDO;
import com.ev.mes.service.MaterialsScrapItemService;
import com.ev.mes.service.MaterialsScrapService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MaterialsScrapServiceImpl implements MaterialsScrapService {
	@Autowired
	private MaterialsScrapDao materialsScrapDao;
	@Autowired
	private MaterialsScrapItemService materialsScrapItemService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Override
	public MaterialsScrapDO get(Long id) {
		return materialsScrapDao.get(id);
	}

	@Override
	public List<MaterialsScrapDO> list(Map<String, Object> map) {
		return materialsScrapDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return materialsScrapDao.count(map);
	}

	@Override
	public int save(MaterialsScrapDO materialsScrap) {
		return materialsScrapDao.save(materialsScrap);
	}

	@Override
	public int update(MaterialsScrapDO materialsScrap) {
		return materialsScrapDao.update(materialsScrap);
	}

	@Override
	public int remove(Long id) {
		return materialsScrapDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return materialsScrapDao.batchRemove(ids);
	}

	@Override
	public R saveChangeScrap(String scrapDetail, String scrapItemDetail, Long[] ids) {

		if (Objects.nonNull(scrapDetail)) {
			Map<String,Object> parmy = new HashMap<String,Object>();
			List<MaterialsScrapDO> scrapDos = JSON.parseArray(scrapDetail, MaterialsScrapDO.class);
			MaterialsScrapDO scrapDo = scrapDos.get(0);

			if (Objects.nonNull(scrapDo.getId())) {
				//更新

				materialsScrapDao.update(scrapDo);

				if (Objects.nonNull(ids)&&ids.length > 0) {
					materialsScrapItemService.batchRemove(ids);
				}

				if (Objects.nonNull(scrapItemDetail)) {
					List<MaterialsScrapItemDO> scrapitemDos = JSON.parseArray(scrapItemDetail, MaterialsScrapItemDO.class);

					for (MaterialsScrapItemDO itemDO : scrapitemDos) {
						itemDO.setScrapId(scrapDo.getId());
						if(Objects.nonNull(itemDO.getId())){
							materialsScrapItemService.update(itemDO);
						}else{
							materialsScrapItemService.save(itemDO);
						}
					}
				}
				parmy.put("scrapId",scrapDo.getId());
				return R.ok(parmy);
			} else {
				//新增
				//<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
				String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.SCRAP_YLBF, new Date());
				Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
				params.put("maxNo", prefix);
				params.put("offset", 0);
				params.put("limit", 1);
				List<MaterialsScrapDO> list = materialsScrapDao.list(params);
				String suffix = null;
				if (list.size() > 0) {
					suffix = list.get(0).getCode();
				}
				scrapDo.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
				scrapDo.setCreateTime(Objects.nonNull(scrapDo.getCreateTime()) ? scrapDo.getCreateTime() : new Date());
				scrapDo.setCreateBy(Objects.nonNull(scrapDo.getCreateBy()) ? scrapDo.getCreateBy() : ShiroUtils.getUserId());
				scrapDo.setAuditSign(ConstantForMES.WAIT_AUDIT);

				materialsScrapDao.save(scrapDo);

				if (Objects.nonNull(scrapItemDetail)) {
					List<MaterialsScrapItemDO> scrapitemDos = JSON.parseArray(scrapItemDetail, MaterialsScrapItemDO.class);

					for (MaterialsScrapItemDO itemDO : scrapitemDos) {

						itemDO.setScrapId(scrapDo.getId());
						materialsScrapItemService.save(itemDO);
					}
				}

				parmy.put("scrapId",scrapDo.getId());
				return R.ok(parmy);
			}
		} else {
			//"所传主参数为空！"
			return R.ok(messageSourceHandler.getMessage("apis.mes.scrap.detaiNoull",null));
		}

	}


	@Override
	public R deletScrap(Long[] scrapIds) {

		Map<String, Object> parmy = new HashMap<String, Object>();
		parmy.put("id", scrapIds);
		int i = materialsScrapDao.canDelet(parmy);
		if (i == scrapIds.length) {
			materialsScrapDao.batchRemove(scrapIds);
			materialsScrapItemService.batchRemoveByScrapId(scrapIds);
			return R.ok();
		} else {
			//"单据已提交，不能删除！"
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public R getDetailOfScrap(Long scrapId) {

		if (Objects.nonNull(materialsScrapDao.get(scrapId))) {

			Map<String, Object> parmy = new HashMap<String, Object>();
			parmy.put("scrapId", scrapId);

			Map<String, Object> scrapDelet = materialsScrapDao.getOneDetail(parmy);
			List<Map<String, Object>> scrapItemDetails = materialsScrapItemService.getItemDetails(parmy);

			parmy.clear();
			if (Objects.nonNull(scrapDelet)) {
				parmy.put("scrapDelet", scrapDelet);
				parmy.put("scrapItemDetails", scrapItemDetails);
			}
			return R.ok(parmy);
		} else {
			//"此参数下数据为空！"
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public R reversAuditScrap(Long scrapId){

		MaterialsScrapDO scrapDO= materialsScrapDao.get(scrapId);
		if(Objects.nonNull(scrapDO)){
			if(!Objects.equals(scrapDO.getAuditSign(), ConstantForMES.WAIT_AUDIT)){

				MaterialsScrapDO scrapDO1=new MaterialsScrapDO();
				scrapDO1.setAuditSign(ConstantForMES.WAIT_AUDIT);
				scrapDO1.setAuditId(0L); //将审核人更新为0
				scrapDO1.setId(scrapId);
				materialsScrapDao.update(scrapDO1);

				return R.ok();
			}else{
				//"单据已是待审核，请勿重复操作！"
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}
		}else{
			//"所传参数下无数据！"
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public R submit(Long scrapId,Long auditId){

		MaterialsScrapDO scrapDO= materialsScrapDao.get(scrapId);
		if(Objects.nonNull(scrapDO)){
			if(!Objects.equals(scrapDO.getAuditSign(), ConstantForMES.OK_AUDITED)){

				MaterialsScrapDO scrapDO1=new MaterialsScrapDO();
				scrapDO1.setAuditSign(ConstantForMES.OK_AUDITED);
				scrapDO1.setAuditId(auditId);
				scrapDO1.setId(scrapId);
				materialsScrapDao.update(scrapDO1);

				return R.ok();
			}else{
				//"单据已审核，请勿重复操作！"
				return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
			}
		}else{
			//"所传参数下无数据！"
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}


}