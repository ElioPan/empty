package com.ev.mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.MaterielDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.BomDao;
import com.ev.mes.domain.BomDO;
import com.ev.mes.domain.BomDetailDO;
import com.ev.mes.service.BomDetailService;
import com.ev.mes.service.BomService;
import com.ev.mes.vo.BomEntity;
import com.google.common.collect.Maps;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BomServiceImpl implements BomService {
	@Autowired
	private BomDao bomDao;
	@Autowired
	private BomDetailService bomDetailService;
	@Autowired
	private MaterielService materielService;
	@Autowired
	private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

	@Override
	public BomDO get(Long id) {
		return bomDao.get(id);
	}

	@Override
	public List<BomDO> list(Map<String, Object> map) {
		return bomDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return bomDao.count(map);
	}

	@Override
	public int save(BomDO bom) {
		return bomDao.save(bom);
	}

	@Override
	public int update(BomDO bom) {
		return bomDao.update(bom);
	}

	@Override
	public int remove(Long id) {
		return bomDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return bomDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> getDetailInfo(Long id) {
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(4);
		// 获取bom表头信息
		results.put("bomHeadInfo", this.getDetail(id));
		
		// 获取附件
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(2);
		param.put("assocId", id);
		param.put("assocType", ConstantForMES.BOM_FILE);
		List<ContentAssocDO> bomFileList = contentAssocService.list(param);
		results.put("bomFile", bomFileList);
		
		param.clear();
		param.put("bomId", id);
		results.put("bomDetail", bomDetailService.listForMap(param));
		return results;
	}

	@Override
	public Map<String, Object> getDetail(Long id) {
		return bomDao.getDetail(id);
	}

	@Override
	public R add(BomDO bom, String childBomArray, String uploadAttachments) {
        String serialNo = bom.getSerialno().trim();
        bom.setSerialno(serialNo);
        if(StringUtils.isEmpty(serialNo) || serialNo.startsWith(Constant.BOM)){
            Map<String,Object> param = Maps.newHashMap();
            param.put("maxNo",Constant.BOM);
            param.put("offset", 0);
            param.put("limit", 1);

            List<BomDO> list = this.list(param);
            bom.setSerialno(DateFormatUtil.getWorkOrderno(Constant.BOM,list.size()>0?list.get(0).getSerialno():null,4));
        }

	    if (this.isNoRepeat(bom.getSerialno())) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
		}
		bom.setAuditSign(ConstantForMES.WAIT_AUDIT);
		int save = this.save(bom);
		List<BomDetailDO> bodys = JSON.parseArray(childBomArray, BomDetailDO.class);
		if (save > 0) {
			Long bomId = bom.getId();
			for (BomDetailDO bomDetailDO : bodys) {
                bomDetailDO.setBomId(bomId);
                bomDetailService.save(bomDetailDO);
            }
            if (StringUtils.isNoneBlank(uploadAttachments)) {
                contentAssocService.saveList(bomId, JSONArray.parseArray(uploadAttachments), ConstantForMES.BOM_FILE);
            }
            Map<String,Object> result =Maps.newHashMap();
            result.put("id", bomId);
            return R.ok(result);
		}
		return R.error();
	}

	/**
	 * 编号是否重复
	 * 
	 */
	@Override
	public boolean isNoRepeat(String serialno) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("serialno", serialno);
		param.put("offset", 0);
		param.put("limit", 1);
		return this.count(param) > 0;
	}

	@Override
	public R edit(BomDO bom, String childBomArray, Long[] ids, String uploadAttachments) {
        if (this.isAudit(bom.getId())) {
            return R.error(messageSourceHandler.getMessage("common.approved.update.disabled",null));
        }
		Long bomId = bom.getId();
		BomDO bomDO = this.get(bomId);
		if (Objects.equals(bom.getSerialno(), bomDO.getSerialno()) || !this.isNoRepeat(bom.getSerialno())) {
			int count = this.update(bom);
			List<BomDetailDO> bodys = JSON.parseArray(childBomArray, BomDetailDO.class);
			if (count > 0) {
				if (ids.length > 0) {
					bomDetailService.batchRemove(ids);
				}
				for (BomDetailDO bomDetailDO : bodys) {
					if (Objects.isNull(bomDetailDO.getId())) {
						bomDetailDO.setBomId(bomId);
						bomDetailService.save(bomDetailDO);
						continue;
					}
					bomDetailService.update(bomDetailDO);
				}
                Long[] bomIds = {bomId};
                contentAssocService.removeByAssocIdAndType(bomIds, ConstantForMES.BOM_FILE);
                if (StringUtils.isNoneBlank(uploadAttachments)) {
                    contentAssocService.saveList(bomId, JSONArray.parseArray(uploadAttachments), ConstantForMES.BOM_FILE);
                }
                return R.ok();
			}
		}
        return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
	}

	@Override
	public int removeHeadAndBody(Long id) {
		BomDO bomDO = new BomDO();
		bomDO.setDelFlag(1);
		bomDO.setId(id);
		int remove = this.update(bomDO);
		int removeByHeadId = 0;
		if (remove > 0) {
			removeByHeadId = bomDetailService.removeByHeadId(id);
		}
		return removeByHeadId;
	}

	@Override
	public void batchRemoveHeadAndBody(Long[] ids) {
		for (Long id : ids) {
			this.removeHeadAndBody(id);
		}
	}

	@Override
	public boolean isAudit(Long id) {
		BomDO bomDO = this.get(id);
		return Objects.equals(bomDO.getAuditSign(), ConstantForMES.OK_AUDITED);
	}

	@Override
	public int audit(Long id) {
		BomDO bomDO = new BomDO();
		bomDO.setId(id);
		bomDO.setAuditSign(ConstantForMES.OK_AUDITED);
		return this.update(bomDO);
	}

	@Override
	public int reverseAudit(Long id) {
		BomDO bomDO = new BomDO();
		bomDO.setId(id);
		bomDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
		return this.update(bomDO);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return bomDao.countForMap(params);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return bomDao.listForMap(params);
	}

	@Override
	public R importExcel(MultipartFile file) {
		if (file.isEmpty()) {
			return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(1);
		params.setHeadRows(1);
		List<BomEntity> bomEntityList;
		try {
			bomEntityList = ExcelImportUtil.importExcel(file.getInputStream(), BomEntity.class, params);
			bomEntityList = bomEntityList.stream().filter(bomEntity -> bomEntity.getSerialno() != null).collect(Collectors.toList());
		} catch (Exception e) {
			return R.error(messageSourceHandler.getMessage("file.upload.error", null));
		}
		if (bomEntityList.size() > 0) {
			for (BomEntity bomEntity : bomEntityList) {
				if (StringUtils.isEmpty(bomEntity.getSerialno())
						|| StringUtils.isEmpty(bomEntity.getProductCode())
						|| StringUtils.isEmpty(bomEntity.getMaterielCode())
						|| !NumberUtils.isNumber(bomEntity.getProductCount())
						|| !NumberUtils.isNumber(bomEntity.getMaterielCount())
						|| !NumberUtils.isNumber(bomEntity.getWasteRate())
				) {
					return R.error(messageSourceHandler.getMessage("basicInfo.correct.param", null));
				}
			}
			Map<String, List<BomEntity>> groupBomEntity = bomEntityList
					.stream()
					.collect(Collectors.groupingBy(BomEntity::getSerialno));
			List<String> productCodeList = bomEntityList
					.stream()
					.map(BomEntity::getProductCode)
					.collect(Collectors.toList());
			List<String> materielCodeList = bomEntityList
					.stream()
					.map(BomEntity::getMaterielCode)
					.collect(Collectors.toList());
			productCodeList.addAll(materielCodeList);
			List<String> allCode = productCodeList
					.stream()
					.distinct()
					.collect(Collectors.toList());
			Map<String, Object> param = Maps.newHashMap();
			param.put("codes", allCode);
			List<MaterielDO> materielDOList = materielService.list(param);
			if (materielDOList.size() != allCode.size()) {
				List<String> collect = materielDOList
						.stream()
						.map(MaterielDO::getSerialNo)
						.collect(Collectors.toList());
				allCode.removeAll(collect);
				String[] notExist = {allCode.toString()};
				return R.error(messageSourceHandler.getMessage("basicInfo.materiel.notExist", notExist));
			}
			Map<String, Integer> idForCode = materielDOList
					.stream()
					.collect(Collectors.toMap(MaterielDO::getSerialNo, MaterielDO::getId));
			BomDO bom;
			BomDetailDO bomDetail;
			BomEntity bomEntity;
			Long bomId;
			for (String bomCode : groupBomEntity.keySet()) {
				List<BomEntity> bomEntities = groupBomEntity.get(bomCode);
				// 取出BOM主表
				bomEntity = bomEntities.get(0);
				bom = new BomDO();
				bom.setSerialno(bomEntity.getSerialno());
				bom.setName(bomEntity.getName());
				bom.setVersion(bomEntity.getVersion());
				bom.setMaterielId(idForCode.get(bomEntity.getProductCode()));
				bom.setCount(new BigDecimal(bomEntity.getProductCount()));
				bom.setAuditSign(ConstantForMES.WAIT_AUDIT);
				bom.setUseStatus(1);
				this.save(bom);
				bomId = bom.getId();
				// 取出BOM子表
				for (BomEntity entity : bomEntities) {
					bomDetail = new BomDetailDO();
					bomDetail.setBomId(bomId);
					bomDetail.setMaterielId(idForCode.get(entity.getMaterielCode()));
					bomDetail.setStandardCount(new BigDecimal(entity.getMaterielCount()));
					bomDetail.setWasteRate(new BigDecimal(entity.getWasteRate()));
					String isKeyComponents = entity.getIsKeyComponents();
					isKeyComponents = isKeyComponents == null ? "否" : isKeyComponents;
					bomDetail.setIsKeyComponents("否".equals(isKeyComponents) ? 0 : 1);
					bomDetailService.save(bomDetail);
				}

			}
			return R.ok();
		}
		return R.error();
	}

}
