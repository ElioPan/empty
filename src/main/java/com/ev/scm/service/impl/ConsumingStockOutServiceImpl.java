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
public class ConsumingStockOutServiceImpl extends StockOutServiceImpl implements ConsumingStockOutService{
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private ProductionFeedingDetailService productionFeedingDetailService;
    @Autowired
    private ProductionFeedingService productionFeedingService;
    @Autowired
    private StockOutItemService stockOutItemService;

    @Override
    public R checkSourceNumber(String item,Long id) {
        // 与源单数量对比
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        for (StockOutItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (sourceId == null) {
                continue;
            }
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        // 获取原先单据的数量
        Map<Long, BigDecimal> oldCounts = Maps.newHashMap();
        if (id != null) {
            Map<String,Object> map = Maps.newHashMap();
            map.put("outId",id);
            List<StockOutItemDO> list = stockOutItemService.list(map);
            if (list.size() > 0) {
                oldCounts = list.stream()
                        .collect(Collectors.toMap(StockOutItemDO::getSourceId, StockOutItemDO::getCount, BigDecimal::add));
            }
        }


        ProductionFeedingDetailDO detailDO;
        BigDecimal feedingCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                ProductionFeedingDO productionFeedingDO = productionFeedingService.get(detailDO.getHeadId());
                if (productionFeedingDO.getIsQuota()==0 || productionFeedingDO.getIsQuota()==null) {
                    continue;
                }
                feedingCount = detailDO.getPlanFeeding();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("sourceId",sourceId);
                map.put("sourceType", ConstantForGYL.SCTLD);
                BigDecimal bySource = stockOutItemService.getCountBySource(map);

                BigDecimal oldCount = oldCounts.getOrDefault(sourceId,BigDecimal.ZERO);

                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource.subtract(oldCount);
                if (feedingCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    List<StockOutItemDO> collect = itemDOs.stream()
                            .filter(itemDO -> Objects.equals(itemDO.getSourceId(),sourceId))
                            .collect(Collectors.toList());
                    String sourceCount = feedingCount.subtract(countByOutSource).toPlainString();
                    String [] args = {count.get(sourceId).toPlainString(),sourceCount,collect.get(0).getSourceCode()};
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("sourceId",sourceId);
                    result.put("sourceCount",sourceCount);
                    return R.error(500,messageSourceHandler.getMessage("stock.number.error", args),result);
                }
            }
        }
        return null;
    }

    @Override
    public Map<Long, BigDecimal> getFeedingCountMap(Long id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("outId", id);
        List<StockOutItemDO> list = stockOutItemService.list(map);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        for (StockOutItemDO itemDO : list) {
            Long sourceId = itemDO.getSourceId();
            if (sourceId == null) {
                continue;
            }
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        return count;
    }

    @Override
    public R auditConsumingStockOut(Long id) {
        R audit = this.audit(id, ConstantForGYL.LYCK);
        if (audit==null){
            this.writeFeedingCount(id,true);
            return R.ok();
        }
        return audit;
    }

    @Override
    public R reverseAuditConsumingStockOut(Long id) {
        R audit = this.reverseAuditForR(id, ConstantForGYL.LYCK);
        if (audit==null){
            this.writeFeedingCount(id,false);
            return R.ok();
        }
        return audit;
    }

    @Override
    public void writeFeedingCount(Long id,boolean isAudit) {
        Map<Long, BigDecimal> count = this.getFeedingCountMap(id);
        if (count.size() > 0) {
            ProductionFeedingDetailDO detailDO;
            BigDecimal outCount;
            for (Long sourceId : count.keySet()) {
                detailDO = productionFeedingDetailService.get(sourceId);
                outCount = detailDO.getOutCount()==null?BigDecimal.ZERO:detailDO.getOutCount();
                if (isAudit) {
                    detailDO.setOutCount(outCount.add(count.get(sourceId)));
                }else {
                    detailDO.setOutCount(outCount.subtract(count.get(sourceId)));
                }
                productionFeedingDetailService.update(detailDO);
            }
        }

    }
}
