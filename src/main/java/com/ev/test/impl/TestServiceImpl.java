package com.ev.test.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ev.system.domain.UserDO;
import com.ev.test.TestService;
import com.ev.test.dao.TestMapper;
import com.ev.test.vo.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xy
 * @Date 2020/4/23 13:45
 * @Description
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, UserDO> implements TestService{
    @Autowired
    TestMapper testMapper;

    @Override
    public List<UserDO> pageList(UserParam userParam) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userParam.getUserId());
        return this.baseMapper.selectList(null);
    }
}
