package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.PurchaseStockOutService;
import com.ev.scm.service.StockInItemService;
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
public class PurchaseStockOutServiceImpl extends StockOutServiceImpl implements PurchaseStockOutService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private StockInItemService stockInItemService;
    @Autowired
    private StockOutItemService stockOutItemService;

    @Override
    public R checkSourceNumber(String item) {
        // 与源单数量对比
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        Map<Long, BigDecimal> count = Maps.newHashMap();
        Map<Long, Long> sourceIdAndItemId = Maps.newHashMap();
        for (StockOutItemDO itemDO : itemDOs) {
            Long sourceId = itemDO.getSourceId();
            if (count.containsKey(sourceId)) {
                count.put(sourceId, count.get(sourceId).add(itemDO.getCount()));
                continue;
            }
            sourceIdAndItemId.put(sourceId,itemDO.getId());
            count.put(itemDO.getSourceId(), itemDO.getCount());
        }
        StockInItemDO detailDO;
        BigDecimal contractCount;
        if (count.size() > 0) {
            for (Long sourceId : count.keySet()) {
                detailDO = stockInItemService.get(sourceId);
                contractCount = detailDO.getCount();
                // 查询源单已被选择数量
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",sourceIdAndItemId.get(sourceId));
                map.put("sourceId",sourceId);
                map.put("sourceType",ConstantForGYL.PURCHASE_INSTOCK);
                BigDecimal bySource = stockOutItemService.getCountBySource(map);
                BigDecimal countByOutSource = bySource==null?BigDecimal.ZERO:bySource;
                if (contractCount.compareTo(count.get(sourceId).add(countByOutSource))<0){
                    List<StockOutItemDO> collect = itemDOs.stream()
                            .filter(itemDO -> Objects.equals(itemDO.getSourceId(),sourceId))
                            .collect(Collectors.toList());
                    String [] args = {count.get(sourceId).toPlainString(),contractCount.subtract(countByOutSource).toPlainString(),collect.get(0).getSourceCode()};
                    return R.error(messageSourceHandler.getMessage("stock.number.error", args));
                }
            }
        }
        return null;
    }

}