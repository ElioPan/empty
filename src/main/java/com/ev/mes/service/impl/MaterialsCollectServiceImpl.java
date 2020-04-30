package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.mes.dao.MaterialsCollectDao;
import com.ev.mes.domain.MaterialsCollectDO;
import com.ev.mes.service.DispatchItemService;
import com.ev.mes.service.MaterialsCollectService;
import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class MaterialsCollectServiceImpl implements MaterialsCollectService {
	@Autowired
	private MaterialsCollectDao materialsCollectDao;
	@Autowired
	private DispatchItemService dispatchItemService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public MaterialsCollectDO get(Long id){
		return materialsCollectDao.get(id);
	}
	
	@Override
	public List<MaterialsCollectDO> list(Map<String, Object> map){
		return materialsCollectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return materialsCollectDao.count(map);
	}
	
	@Override
	public int save(MaterialsCollectDO materialsCollect){
		return materialsCollectDao.save(materialsCollect);
	}
	
	@Override
	public int update(MaterialsCollectDO materialsCollect){
		return materialsCollectDao.update(materialsCollect);
	}
	
	@Override
	public int remove(Long id){
		return materialsCollectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return materialsCollectDao.batchRemove(ids);
	}

	@Override
	public R saveAndSubmit(String detail,Long[] ids ,Long dispatchId,int sign){

		if(Objects.nonNull(dispatchItemService.get(dispatchId))){


			if(Objects.nonNull(detail)){
				List<MaterialsCollectDO> detailsDo= JSON.parseArray(detail,MaterialsCollectDO.class);

				if(Objects.nonNull(ids)&&ids.length>0){
					materialsCollectDao.batchRemove(ids);
				}

				for(MaterialsCollectDO materDo :detailsDo){
					if(sign==1){
						materDo.setStatus(ConstantForMES.MES_APPLY_APPROED);//已提交
					}else{
						materDo.setStatus(Constant.TS);//暂存
					}

					if(Objects.nonNull(materDo.getId())){
						materialsCollectDao.update(materDo);

					}else{
						materDo.setDispatchItemId(dispatchId);
						materialsCollectDao.save(materDo);
					}
				}
				return R.ok();
			}
			//所传采集信息为空！
			return R.error(messageSourceHandler.getMessage("apis.mes.colect.detailNoull",null));
		}else{
			//工单不存在！
			return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.dispathNoull",null));
		}
	}

	@Override
	public List<Map<String, Object>> getListssDetail(Map<String, Object> map) {
		return materialsCollectDao.getListssDetail(map);
	}

	@Override
	public R removeCollect(Long[]ids) {
		Map<String,Object> result=new HashMap<String,Object>(){{
			put("id",ids);
		}};

		int i = materialsCollectDao.isQuote(result);
		if(i==ids.length){
			materialsCollectDao.batchRemove(ids);
			return R.ok();
		}else{
			//单据已提交，不能删除！
			return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
		}
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return materialsCollectDao.countForMap(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return materialsCollectDao.listForMap(map);
	}


}
