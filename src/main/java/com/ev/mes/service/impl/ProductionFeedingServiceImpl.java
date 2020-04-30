package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.service.DictionaryService;
import com.ev.custom.service.FacilityLocationService;
import com.ev.custom.service.FacilityService;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.ProductionFeedingDao;
import com.ev.mes.domain.*;
import com.ev.mes.service.*;
import com.ev.mes.vo.FeedingAlterationVO;
import com.ev.mes.vo.FeedingDetailVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductionFeedingServiceImpl implements ProductionFeedingService {
	@Autowired
	private ProductionFeedingDao productionFeedingDao;
	@Autowired
	private ProductionFeedingDetailService feedingDetailService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private ProductionFeedingAlterationService alterationService;
	@Autowired
	private MaterielService materielService;
	@Autowired
	private FacilityService facilityService;
	@Autowired
	private FacilityLocationService facilityLocationService;
	@Autowired
	private StationService stationService;
	@Autowired
	private ProcessService processService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

	@Override
	public ProductionFeedingDO get(Long id) {
		return productionFeedingDao.get(id);
	}

	@Override
	public List<ProductionFeedingDO> list(Map<String, Object> map) {
		return productionFeedingDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return productionFeedingDao.count(map);
	}

	@Override
	public int save(ProductionFeedingDO productionFeeding) {
		return productionFeedingDao.save(productionFeeding);
	}

	@Override
	public int update(ProductionFeedingDO productionFeeding) {
		return productionFeedingDao.update(productionFeeding);
	}

	@Override
	public int remove(Long id) {
		return productionFeedingDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return productionFeedingDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return productionFeedingDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return productionFeedingDao.countForMap(params);
	}

	@Override
	public boolean isNonAudit(Long id) {
		ProductionFeedingDO feedingDO = this.get(id);
		return !Objects.equals(feedingDO.getStatus(), ConstantForMES.WAIT_AUDIT);
	}

	@Override
	public R audit(Long id) {
        ProductionFeedingDO feedingDO = this.get(id);
        if (!Objects.equals(feedingDO.getStatus(), ConstantForMES.WAIT_AUDIT)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
        }
//        if (!Objects.equals(ShiroUtils.getUserId(), feedingDO.getAuditor())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user", null));
//        }
        List<ProductionFeedingDetailDO> list = this.getFeedingDetailList(id);
        if (list.size() == 0) {
            return R.error(messageSourceHandler.getMessage("scm.child.isEmpty", null));
        }
        ProductionFeedingDO productionFeedingDO = new ProductionFeedingDO();
        productionFeedingDO.setId(id);
        productionFeedingDO.setAuditor(ShiroUtils.getUserId());
        productionFeedingDO.setStatus(ConstantForMES.OK_AUDITED);
        return this.update(productionFeedingDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public R reverseAudit(Long id) {
        ProductionFeedingDO feedingDO = this.get(id);
        if (!Objects.equals(ConstantForMES.OK_AUDITED, feedingDO.getStatus())) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit", null));
        }
        // 检查投料单下是否有下游单据
		List<Long> outStockIds = dictionaryService.listByType(ConstantForMES.FEEDING)
				.stream()
				.map(DictionaryDO::getId)
				.collect(Collectors.toList());
		Map<String,Object> map = Maps.newHashMap();
		map.put("sourceTypes",outStockIds);
		map.put("sourceId",id);

        if (this.isCited(id) || productionFeedingDao.childCount(map)>0) {
			return R.error(messageSourceHandler.getMessage("scm.childList.reverseAudit", null));
        }

        ProductionFeedingDO productionFeedingDO = new ProductionFeedingDO();
        productionFeedingDO.setId(id);
        productionFeedingDO.setStatus(ConstantForMES.WAIT_AUDIT);
        return this.update(productionFeedingDO) > 0 ? R.ok() : R.error();
	}

	@Override
	public boolean isCited(Long id) {
		List<ProductionFeedingDetailDO> feedingDetailList = this.getFeedingDetailList(id);
		for (ProductionFeedingDetailDO productionFeedingDetailDO : feedingDetailList) {
			if (Objects.nonNull(productionFeedingDetailDO.getScrapCount())
					|| Objects.nonNull(productionFeedingDetailDO.getOutCount())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ProductionFeedingDO getByOutsourcingContractItemId(Long id) {
		return productionFeedingDao.getByOutsourcingContractItemId(id);
	}

	@Override
	public List<Map<String, Object>> listForMapToOutsourcingContract(Map<String, Object> map) {
		return productionFeedingDao.listForMapToOutsourcingContract(map);
	}

	@Override
	public int countForMapToOutsourcingContract(Map<String, Object> map) {
		return productionFeedingDao.countForMapToOutsourcingContract(map);
	}

	@Override
	public int countBySource(Map<String, Object> map) {
		return productionFeedingDao.countBySource(map);
	}

	private List<ProductionFeedingDetailDO> getFeedingDetailList(Long id) {
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
		param.put("headId", id);
		return feedingDetailService.list(param);
	}

	@Override
	public Map<String, Object> getDetailInfo(Long id) {
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(12);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id", id);
		params.put("offset", 0);
		params.put("limit", 1);
		List<Map<String, Object>> listForMap = this.listForMap(params);
		results.put("feedingHeadInfo", listForMap.size() > 0 ? listForMap.get(0) : null);
		params.clear();
		params.put("headId", id);
		List<Map<String, Object>> feedingDetailList = feedingDetailService.listForMap(params);
		results.put("feedingBodyInfo", feedingDetailList);
		return results;
	}

	@Override
	public int removeHeadAndBody(Long id) {
		int remove = this.remove(id);
		if (remove > 0) {
			feedingDetailService.removeByHeadId(id);
		}
		return remove;
	}

	@Override
	public void batchRemoveHeadAndBody(Long[] ids) {
		for (Long id : ids) {
			this.removeHeadAndBody(id);
		}
	}

	@Override
	public R add(ProductionFeedingDO feedingDO, String childArray) {
		this.setFeedingNo(feedingDO);
		feedingDO.setStatus(ConstantForMES.WAIT_AUDIT);
		int save = this.save(feedingDO);
        if (StringUtils.isEmpty(childArray)) {
            return save > 0 ? R.ok() : R.error();
        }
		List<ProductionFeedingDetailDO> detailArray = JSON.parseArray(childArray, ProductionFeedingDetailDO.class);
		if (save > 0) {
			Long feedingId = feedingDO.getId();
			for (ProductionFeedingDetailDO feedingDetailDO : detailArray) {
				feedingDetailDO.setHeadId(feedingId);
				feedingDetailService.save(feedingDetailDO);
			}
            Map<String,Object> result = Maps.newHashMap();
            result.put("id", feedingDO.getId());
            return R.ok(result);
		}
		return R.error();
	}

	@Override
	public void setFeedingNo(ProductionFeedingDO feedingDO) {
		// 获取编号
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForMES.SCTL_PREFIX);
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<ProductionFeedingDO> list = this.list(param);
		String taskNo = null;
		if (list.size() > 0) {
			taskNo = list.get(0).getFeedingNo();
		}
		feedingDO.setFeedingNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
	}

	@Override
	public R edit(ProductionFeedingDO feedingDO, String childArray, Long[] ids) {
        if (this.isNonAudit(feedingDO.getId())) {
            return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
        }
        if (ids.length > 0) {
            feedingDetailService.batchRemove(ids);
        }
        int update = this.update(feedingDO);
        if (StringUtils.isEmpty(childArray)) {
            return update > 0 ? R.ok() : R.error();
        }
        Long feedingId = feedingDO.getId();
        List<ProductionFeedingDetailDO> detailArray = JSON.parseArray(childArray, ProductionFeedingDetailDO.class);
        if (update > 0) {
            for (ProductionFeedingDetailDO feedingDetailDO : detailArray) {
                if (Objects.isNull(feedingDetailDO.getId())) {
                    feedingDetailDO.setHeadId(feedingId);
                    feedingDetailService.save(feedingDetailDO);
                    continue;
                }
                feedingDetailService.update(feedingDetailDO);
            }
            return R.ok();
        }
        return R.error();
    }

	@Override
	public R change(ProductionFeedingDO feedingDO, String childArray, Long[] ids) {
		Long feedingId = feedingDO.getId();
		ProductionFeedingAlterationDO alterationDO = new ProductionFeedingAlterationDO();
		ProductionFeedingDO productionFeedingDO = this.get(feedingId);
		alterationDO.setCode(productionFeedingDO.getFeedingNo());
		alterationDO.setFeedingId(feedingId);
		alterationDO.setType(feedingDO.getProductionPlanId() != null ? ConstantForMES.SCTL.longValue() : ConstantForGYL.WWTLD);

		Map<String, Object> params = Maps.newHashMap();
		params.put("headId", feedingId);
		//投料单列表详情
		List<FeedingDetailVO> detailVOList = alterationService.listForFeedingItem(params);

		// 检查投料单下是否有下游单据
		if(ids.length>0) {
			List<Long> outStockIds = dictionaryService.listByType(ConstantForMES.FEEDING)
					.stream()
					.map(DictionaryDO::getId)
					.collect(Collectors.toList());
			Map<String, Object> map = Maps.newHashMap();
			map.put("sourceTypes", outStockIds);
			map.put("sourceIds", Arrays.asList(ids));
			if (productionFeedingDao.childCount(map) > 0) {
				return R.error(messageSourceHandler.getMessage("common.item.delete.haveChild", null));
			}
		}

		List<ProductionFeedingDetailDO> detailArray = JSON.parseArray(childArray, ProductionFeedingDetailDO.class);
		List<FeedingAlterationVO> itemList = this.getFeedingItemVOS(childArray,ids, detailVOList);

		if (itemList.size() > 0) {
			alterationDO.setAlterationContent(itemList.toString());
			alterationService.save(alterationDO);
		}
		int update = this.update(feedingDO);
		if (update > 0) {
			for (ProductionFeedingDetailDO feedingDetailDO : detailArray) {
				if (Objects.isNull(feedingDetailDO.getId())) {
					feedingDetailDO.setHeadId(feedingId);
					feedingDetailService.save(feedingDetailDO);
					continue;
				}
				feedingDetailService.update(feedingDetailDO);
			}
			return R.ok();
		}

		return R.ok();
	}

	private List<FeedingAlterationVO> getFeedingItemVOS(String childArray, Long[] ids, List<FeedingDetailVO> detailVOList) {
		List<FeedingAlterationVO> feedingAlterationVOList = Lists.newArrayList();
		FeedingAlterationVO feedingAlterationVO;
		if(ids.length>0){
			for (Long id : ids) {
				for (FeedingDetailVO detailVO : detailVOList) {
					if(detailVO.getId().equals(id)){
						feedingAlterationVO = new FeedingAlterationVO();
						feedingAlterationVO.setId(id);
						feedingAlterationVO.setMaterielId(detailVO.getMaterielId());
						feedingAlterationVO.setCountBefore(detailVO.getPlanFeeding().toPlainString());
						feedingAlterationVO.setFacilityBefore(detailVO.getFacilityName());
						feedingAlterationVO.setLocationBefore(detailVO.getLocationName());
						feedingAlterationVO.setProcessBefore(detailVO.getProcessName());
						feedingAlterationVO.setStationBefore(detailVO.getStationName());
						feedingAlterationVO.setIsCollectBefore(detailVO.getIsCollectName());
						feedingAlterationVO.setCountAfter("已删除");
						feedingAlterationVO.setFacilityAfter("已删除");
						feedingAlterationVO.setLocationAfter("已删除");
						feedingAlterationVO.setProcessAfter("已删除");
						feedingAlterationVO.setStationAfter("已删除");
						feedingAlterationVO.setIsCollectAfter("已删除");
						feedingAlterationVOList.add(feedingAlterationVO);
						break;
					}
				}
			}

			feedingDetailService.batchRemove(ids);
		}
		List<FeedingDetailVO> detailDOList = JSON.parseArray(childArray, FeedingDetailVO.class);
		List<Long> facilityIdList = detailDOList
				.stream()
				.map(FeedingDetailVO::getFacilityId)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList());
		List<Long> locationIdList = detailDOList
				.stream()
				.map(FeedingDetailVO::getLocationId)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList());
		List<Long> processIdList = detailDOList
				.stream()
				.map(FeedingDetailVO::getProcessId)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList());
		List<Long> stationIdList = detailDOList
				.stream()
				.map(FeedingDetailVO::getStationId)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList());
		Map<String,Object> map = Maps.newHashMap();
		Map<Long, String> facilityMap = Maps.newHashMap();
		if (facilityIdList.size() > 0) {
			map.put("idList",facilityIdList);
			List<FacilityDO> facilityList = facilityService.list(map);
			facilityMap = facilityList
					.stream()
					.collect(Collectors.toMap(FacilityDO::getId, FacilityDO::getName));
		}
		Map<Long, String> locationMap = Maps.newHashMap();
		if (locationIdList.size() > 0) {
			map.put("idList",locationIdList);
			List<FacilityLocationDO> locationList = facilityLocationService.list(map);
			locationMap = locationList
					.stream()
					.collect(Collectors.toMap(FacilityLocationDO::getId, FacilityLocationDO::getName));
		}
		Map<Long, String> processMap = Maps.newHashMap();
		if (processIdList.size() > 0) {
			map.put("idList",processIdList);
			List<ProcessDO> processList = processService.list(map);
			processMap = processList
					.stream()
					.collect(Collectors.toMap(ProcessDO::getId, ProcessDO::getName));
		}
		Map<Long, String> stationMap = Maps.newHashMap();
		if (stationIdList.size() > 0) {
			map.put("idList",stationIdList);
			List<StationDO> stationList = stationService.list(map);
			stationMap = stationList
					.stream()
					.collect(Collectors.toMap(StationDO::getId, StationDO::getName));
		}

		for (FeedingDetailVO afterDetailDO : detailDOList) {
			Long afterDetailId = afterDetailDO.getId();
			if (afterDetailId == null) {
				feedingAlterationVO = new FeedingAlterationVO();
				feedingAlterationVO.setId(afterDetailId);
				feedingAlterationVO.setCountBefore("新增");
				feedingAlterationVO.setFacilityBefore("新增");
				feedingAlterationVO.setLocationBefore("新增");
				feedingAlterationVO.setProcessBefore("新增");
				feedingAlterationVO.setStationBefore("新增");
				feedingAlterationVO.setIsCollectBefore("新增");
				feedingAlterationVO.setMaterielId(afterDetailDO.getMaterielId());
				feedingAlterationVO.setCountAfter(afterDetailDO.getPlanFeeding().toPlainString());
				feedingAlterationVO.setFacilityAfter(facilityMap.getOrDefault(afterDetailDO.getFacilityId(),""));
				feedingAlterationVO.setLocationAfter(locationMap.getOrDefault(afterDetailDO.getLocationId(),""));
				feedingAlterationVO.setProcessAfter(processMap.getOrDefault(afterDetailDO.getProcessId(),""));
				feedingAlterationVO.setStationAfter(stationMap.getOrDefault(afterDetailDO.getStationId(),""));
				String isCollectName = afterDetailDO.getIsCollect()==1?"是":"否";
				feedingAlterationVO.setIsCollectAfter(isCollectName);
				feedingAlterationVOList.add(feedingAlterationVO);
				continue;
			}
			for (FeedingDetailVO beforeDetailDO : detailVOList) {
				Long beforeDetailId = beforeDetailDO.getId();
				feedingAlterationVO = new FeedingAlterationVO();
				feedingAlterationVO.setId(afterDetailId);
				boolean isUpdate = false;
				if (beforeDetailId.equals(afterDetailId)) {
					feedingAlterationVO.setMaterielId(beforeDetailDO.getMaterielId());
					feedingAlterationVO.setCountBefore(beforeDetailDO.getPlanFeeding().toPlainString());
					feedingAlterationVO.setFacilityBefore(beforeDetailDO.getFacilityName());
					feedingAlterationVO.setLocationBefore(beforeDetailDO.getLocationName());
					feedingAlterationVO.setProcessBefore(beforeDetailDO.getProcessName());
					feedingAlterationVO.setStationBefore(beforeDetailDO.getStationName());
					feedingAlterationVO.setIsCollectBefore(beforeDetailDO.getIsCollectName());

					if(afterDetailDO.getPlanFeeding().compareTo(beforeDetailDO.getPlanFeeding())!=0){
						isUpdate = true;
						feedingAlterationVO.setCountAfter(afterDetailDO.getPlanFeeding().toPlainString());
					}
					if(!Objects.equals(afterDetailDO.getFacilityId(),beforeDetailDO.getFacilityId())){
						isUpdate = true;
						feedingAlterationVO.setFacilityAfter(facilityMap.getOrDefault(afterDetailDO.getFacilityId(),""));
					}
					if(!Objects.equals(afterDetailDO.getLocationId(),beforeDetailDO.getLocationId())){
						isUpdate = true;
						feedingAlterationVO.setLocationAfter(locationMap.getOrDefault(afterDetailDO.getLocationId(),""));
					}
					if(!Objects.equals(afterDetailDO.getProcessId(),beforeDetailDO.getProcessId())){
						isUpdate = true;
						feedingAlterationVO.setProcessAfter(processMap.getOrDefault(afterDetailDO.getProcessId(),""));
					}
					if(!Objects.equals(afterDetailDO.getStationId(),beforeDetailDO.getStationId())){
						isUpdate = true;
						feedingAlterationVO.setStationAfter(stationMap.getOrDefault(afterDetailDO.getStationId(),""));
					}
					if(!Objects.equals(afterDetailDO.getIsCollect(),beforeDetailDO.getIsCollect())){
						isUpdate = true;
						String isCollectName = afterDetailDO.getIsCollect()==1?"是":"否";
						feedingAlterationVO.setIsCollectAfter(isCollectName);
					}

					if(isUpdate){
						feedingAlterationVOList.add(feedingAlterationVO);
					}
					break;
				}
			}
		}
		return feedingAlterationVOList;
	}

	@Override
	public Map<String, Object> changeDetail(Long id) {
		Map<String,Object> results = Maps.newHashMap();
		ProductionFeedingAlterationDO alterationDO = alterationService.get(id);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id", alterationDO.getFeedingId());
		params.put("offset", 0);
		params.put("limit", 1);
		List<Map<String, Object>> listForMap = this.listForMap(params);
		if (listForMap.size() > 0) {
			results.put("feedingHeadInfo",  listForMap.get(0));
			String alterationContent = alterationDO.getAlterationContent();
			List<FeedingAlterationVO> alterationVOList = JSON.parseArray(alterationContent, FeedingAlterationVO.class);
			List<Long> materielIdList = alterationVOList
					.stream()
					.map(FeedingAlterationVO::getMaterielId)
					.distinct()
					.collect(Collectors.toList());
			Map<String,Object> param = Maps.newHashMap();
			param.put("materielIdList",materielIdList);
			List<Map<String, Object>> mapList = materielService.listForMap(param);
			Map<Long, Map<String, Object>> materielMap = mapList
					.stream()
					.collect(Collectors.toMap(k -> Long.parseLong(k.get("id").toString()), v -> v));
			JSONArray alterationVOJSON = JSON.parseArray(alterationContent);
			Map<String, Object> stringObjectMap;
			Long materielId;
			for (int i = 0; i < alterationVOJSON.size(); i++) {
				JSONObject jsonObject = alterationVOJSON.getJSONObject(i);
				materielId = jsonObject.getLong("materielId");
				stringObjectMap = materielMap.get(materielId);
				jsonObject.put("unitUomName", stringObjectMap.getOrDefault("unitUomName", ""));
				jsonObject.put("name", stringObjectMap.getOrDefault("name", ""));
				jsonObject.put("serialNo", stringObjectMap.getOrDefault("serialNo", ""));
				jsonObject.put("specification", stringObjectMap.getOrDefault("specification", ""));
			}

			results.put("feedingBodyInfo", alterationVOJSON);
		}

		return R.ok(results);
	}

}
