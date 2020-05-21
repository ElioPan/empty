package com.ev.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ev.system.domain.UserDO;
import com.ev.test.vo.UserParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author xy
 * @Date 2020/4/23 13:47
 * @Description
 */
public interface TestMapper extends BaseMapper<UserDO> {
    List<UserDO> pageList(Page<UserParam> page, @Param("param") Map<String, Object> param);
}
