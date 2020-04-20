package com.ev.report.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManageKanbanServiceDao {
    List<Map<String, Object>> feedingList();

    List<Map<String, Object>> getProductionStatistics(Map<String, Object> param);

    List<Map<String, Object>> badProductList(Map<String, Object> param);
}
