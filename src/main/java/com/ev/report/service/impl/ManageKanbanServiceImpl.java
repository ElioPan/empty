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
}
