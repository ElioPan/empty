package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.ConsumingStockOutService;
import com.ev.scm.service.OutsourcingStockOutService;
import com.ev.scm.service.StockOutItemService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OutsourcingStockOutServiceImpl extends StockOutServiceImpl implements OutsourcingStockOutService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private ProductionFeedingDetailService productionFeedingDetailService;
    @Autowired
    private ProductionFeedingService productionFeedingService;
    @Autowired
    private StockOutItemService stockOutItemService;
    @Autowired
    private ConsumingStockOutService consumingStockOutService;

    @Override
    public R checkSourceNumber(String item) {
        // 与源单数量对比
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        Map<Long, Long> sourceIdAndItemId = Maps.newHashMap();
        for (StockOutItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, itemDO.getId() == null ? BigDecimal.ZERO : count.get(sourceId).add(itemDO.getCount()));
            }
            sourceIdAndItemId.put(sourceId, itemDO.getId());
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        ProductionFeedingDetailDO detailDO;
        BigDecimal planFeeding;
        Map<String, Object> map;
        for (Long sourceId : count.keySet()) {
            detailDO = productionFeedingDetailService.get(sourceId);
            ProductionFeedingDO productionFeedingDO = productionFeedingService.get(detailDO.getHeadId());
            if (productionFeedingDO.getIsQuota() == 0 || productionFeedingDO.getIsQuota() == null) {
                continue;
            }
            planFeeding = detailDO.getPlanFeeding();
            // 查询源单已被选择数量
            map = Maps.newHashMap();
            map.put("id", sourceIdAndItemId.get(sourceId));
            map.put("sourceId", sourceId);
            map.put("sourceType", ConstantForGYL.WWTLD);
            BigDecimal bySource = stockOutItemService.getCountBySource(map);
            BigDecimal countByOutSource = bySource == null ? BigDecimal.ZERO : bySource;
            if (planFeeding.compareTo(count.get(sourceId).add(countByOutSource)) < 0) {
                List<StockOutItemDO> collect = itemDOs.stream()
                        .filter(itemDO -> Objects.equals(itemDO.getSourceId(), sourceId))
                        .collect(Collectors.toList());
                String[] args = {count.get(sourceId).toPlainString(), planFeeding.subtract(countByOutSource).toPlainString(), collect.get(0).getSourceCode()};
                return R.error(messageSourceHandler.getMessage("stock.number.error", args));
            }
        }
        return null;
    }

    @Override
    public R auditOutsourcingStockOut(Long id) {
        R audit = this.audit(id, ConstantForGYL.LYCK);
        if (audit == null) {
            consumingStockOutService.writeFeedingCount(id, true);
            return R.ok();
        }
        return audit;
    }

    @Override
    public R reverseAuditOutsourcingStockOut(Long id) {
        if (this.childCount(id) > 0) {
            return R.error(messageSourceHandler.getMessage("scm.childList.reverseAudit", null));
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("outId", id);
        List<StockOutItemDO> list = stockOutItemService.list(map);
        if (list.size() > 0) {
            for (StockOutItemDO itemDO : list) {
                if (itemDO.getChargeOffCount() != null && itemDO.getChargeOffCount().compareTo(BigDecimal.ZERO) != 0) {
                    return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
                }
            }
            R reverseAudit = this.reverseAuditForR(id, ConstantForGYL.WWCK);
            // 反写委外投料单数量
            if (reverseAudit == null) {
                consumingStockOutService.writeFeedingCount(id, false);
                return R.ok();
            }
            return reverseAudit;
        }
        return R.error();
    }

}
