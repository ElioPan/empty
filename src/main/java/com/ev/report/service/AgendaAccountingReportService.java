package com.ev.report.service;

import com.ev.custom.domain.DictionaryDO;
import com.ev.report.vo.CommonVO;
import com.ev.framework.utils.R;
import com.ev.report.vo.UserForReportVO;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;

/**
 * 日程管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface AgendaAccountingReportService {

    R execute(CommonVO commonVO);

    R overtime(CommonVO commonVO);

    R overtimeItem(CommonVO commonVO);

    R leaveGroup(CommonVO commonVO);

    R leaveTypeGroup(CommonVO commonVO);

    R leave(CommonVO commonVO, Long typeId);

    R applyForReimbursement(CommonVO commonVO, Long typeId);

    R applyForReimbursementGroup(CommonVO commonVO);

    R applyForReimbursementTypeGroup(CommonVO commonVO);

    R payment(CommonVO commonVO);

    R paymentItem(CommonVO commonVO);

    List<UserForReportVO> getUserList(Map<String, Object> param);

    Triple<List<UserForReportVO>, List<Long>, List<Long>> getUserInfo(int pageNo, int pageSize, Long userId, Long deptId);

    Map<String, Object> getResult(List<UserForReportVO> userForReportVOS, List<Map<String, Object>> itemList, Double total, int size, int pageNo, int pageSize);

    Map<String, Object> getResult(List<DictionaryDO> dictionaryDOS, List<Map<String, Object>> applyForReimbursementGroupType, Long userId);
}
