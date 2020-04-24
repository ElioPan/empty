package com.ev.framework.config;

import com.ev.framework.utils.ShiroUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
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

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object parameter = invocation.getArgs()[1];
            Date now = new Date();
            Long userId = ShiroUtils.getUserId();
            if (parameter instanceof DefaultSqlSession.StrictMap) {
                DefaultSqlSession.StrictMap<Object> objectStrictMap =  (DefaultSqlSession.StrictMap<Object>)parameter;
                Object objectList = objectStrictMap.get("list");
                if(objectList instanceof List){
                    List<Object> list = (List<Object>)objectList;
                    for (Object o : list) {
                        this.setFiled(o,invocation,now,userId);
                    }
                }
            }else{
                this.setFiled(parameter,invocation,now,userId);
            }
        } catch(ShiroException e){
            throw e;
        }catch (Exception e) {
            logger.error("Mybatis公共字段设置值时出错", e);
        }
        return invocation.proceed();
    }

    private void setFiled(Object parameter,Invocation invocation,Date now,Long userId) throws IllegalAccessException {
        Class<?> classParameter =  parameter.getClass();
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
                        field.set(parameter, userId);
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
                field.set(parameter, userId);
            }else if (FIELD_UPDATE_TIME.equalsIgnoreCase(fieldName)) {
                field.set(parameter, now);
            }
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
