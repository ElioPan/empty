package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.report.dao.DeviceAccountingReportDao;
import com.ev.report.dao.ManageKanbanServiceDao;
import com.ev.report.service.DeviceAccountingReportService;
import com.ev.report.service.ManageKanbanService;
import com.ev.report.vo.DeviceVO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ManageKanbanServiceImpl implements ManageKanbanService {
    @Autowired
    private ManageKanbanServiceDao kanbanService;

    @Override
    public List<Map<String, Object>> feedingList() {
        return kanbanService.feedingList();
    }
}
