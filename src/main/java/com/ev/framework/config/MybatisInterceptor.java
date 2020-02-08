package com.ev.framework.config;

import com.ev.framework.utils.ShiroUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

    private final static String FIELD_CREATE_BY = "createBy";
    private final static String FIELD_CREATE_TIME = "createTime";
    private final static String FIELD_UPDATE_BY = "updateBy";
    private final static String FIELD_UPDATE_TIME = "updateTime";
    private final static String FIELD_DEL_FLAG = "delFlag";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object parameter = invocation.getArgs()[1];
            Date now = new Date();
            Class classParameter = (Class) parameter.getClass();
            Field[] fields = classParameter.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (FIELD_CREATE_BY.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                            field.set(parameter, ShiroUtils.getUserId());
                        }
                    }
                }else if (FIELD_CREATE_TIME.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                            field.set(parameter, now);
                        }
                    }
                }else if (FIELD_DEL_FLAG.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                            field.set(parameter, 0);
                        }
                    }
                } else if (FIELD_UPDATE_BY.equalsIgnoreCase(fieldName)) {
                    field.set(parameter, ShiroUtils.getUserId());
                }else if (FIELD_UPDATE_TIME.equalsIgnoreCase(fieldName)) {
                    field.set(parameter, now);
                }
            }
        } catch(ShiroException e){
            throw e;
        }catch (Exception e) {
            logger.error("Mybatis公共字段设置值时出错", e);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
