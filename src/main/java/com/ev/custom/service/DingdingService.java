package com.ev.custom.service;

import java.util.List;

public interface DingdingService {
    public String submitApply(Long[] targetList, String processInstanceId);

    public String completeApprove(String processInstanceId, Boolean isApproved,String reason);

    public List getHistoryByProcessId(String processId);
}
