package com.ev.mes.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.StockOutItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.dao.ProductionFeedingDao;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.google.common.collect.Maps;

@Service
public class ProductionFeedingServiceImpl implements ProductionFeedingService {
	@Autowired
	private ProductionFeedingDao productionFeedingDao;
	@Autowired
	private ProductionFeedingDetailService feedingDetailService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private StockOutItemService stockOutItemService;
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
        // 检查投料单下是否有出库单
		List<Integer> outStockIds = dictionaryService.listByType(ConstantForMES.FEEDING)
				.stream()
				.map(DictionaryDO::getId)
				.collect(Collectors.toList());
        Map<String,Object> map = Maps.newHashMap();
        map.put("sourceTypes",outStockIds);
        map.put("sourceId",id);
		List<StockOutItemDO> outStockList = stockOutItemService.list(map);

        if (this.isCited(id) || outStockList.size()>0) {
			return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
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

}
