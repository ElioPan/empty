package com.ev.scm.dao;

import com.ev.scm.domain.SalescontractItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-10 10:18:10
 */
@Mapper
public interface SalescontractItemDao {

    SalescontractItemDO get(Long sid);

    List<SalescontractItemDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SalescontractItemDO salescontractItem);

    int update(SalescontractItemDO salescontractItem);

    int remove(Long sid);

    int batchRemove(Long[] sids);

    void batchRemoveBySalesContractIds(Long[] salesContractIds);

    List<Map<String, Object>> listByContract(Map<String, Object> map);

    Map<String, Object> countByContract(Map<String, Object> map);
//	int removeSid(Long sid);
}
