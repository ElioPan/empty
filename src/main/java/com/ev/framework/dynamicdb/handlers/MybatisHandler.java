package com.ev.framework.dynamicdb.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.UserDO;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MybatisHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createBy = metaObject.getValue("createBy");
        Object createTime = metaObject.getValue("createTime");
        //获取当前登录用户
        UserDO user = ShiroUtils.getUser();
        if (null == createBy) {
            metaObject.setValue("createBy", user.getUserId());
        }
        if (null == createTime) {
            metaObject.setValue("createTime", new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //获取当前登录用户
        UserDO user = ShiroUtils.getUser();
        metaObject.setValue("lastUpdateNameId", user.getUserId());
        metaObject.setValue("lastUpdateTime", new Date());
    }
}
