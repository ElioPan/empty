package com.ev.scm.dao;

import java.util.List;
import java.util.Map;

import com.ev.scm.domain.AllotDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调拨单主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
@Mapper
public interface AllotDao {

    AllotDO get(Long id);

    List<AllotDO> list(Map<String,Object> map);

    int count(Map<String,Object> map);

    int save(AllotDO allot);

    int update(AllotDO allot);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    Map<String,Object> getDetail(Long id);

    int batchInsert(List<AllotDO> list);
}
