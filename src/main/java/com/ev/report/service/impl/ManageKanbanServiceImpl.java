package com.ev.report.service.impl;

import com.ev.report.dao.ManageKanbanServiceDao;
import com.ev.report.service.ManageKanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ManageKanbanServiceImpl implements ManageKanbanService {
    @Autowired
    private ManageKanbanServiceDao kanbanService;

    @Override
    public List<Map<String, Object>> feedingList() {
        return kanbanService.feedingList();
    }

    @Override
    public List<Map<String, Object>> getProductionStatistics(Map<String, Object> param) {
        return kanbanService.getProductionStatistics(param);
    }

    @Override
    public List<Map<String, Object>> badProductList(Map<String, Object> param) {
        return kanbanService.badProductList(param);
    }
}