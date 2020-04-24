package com.ev.test;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ev.system.domain.UserDO;
import com.ev.test.vo.UserParam;

import java.util.List;

/**
 * @Author xy
 * @Date 2020/4/23 13:37
 * @Description
 */
public interface TestService extends IService<UserDO> {
    List<UserDO> pageList(UserParam userParam);
}
