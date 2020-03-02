package com.ev.scm.dao;

import com.ev.scm.domain.StockOutItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InStockAccountingDao {

    List<Map<String, Object>> getBomItem(Long ContractItemId);

    List<StockOutItemDO> getStockOutDetail(Long ContractItemId);

    int batchUpdateStockOutItem(List<StockOutItemDO> stockOutItemDOs);

    Map<String, Object> getTotalTaxAmountCount(Long stockInItemId);

    List<Map<String, Object>> getUnitPrice(Long stockInItemId);


    int getCountOfSignIsO(Map<String, Object> map);

    int  getAnalysisDate(Map<String, Object> map);

}
