package com.ev.framework.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BeanUtils extends org.springframework.beans.BeanUtils {
    private static final String AFTER_SUFFIX = "After";
    private static final String BEFORE_SUFFIX = "Before";
    private static final String DELETE = "delete";
    private static final String ADD = "add";
    /**
     * 对象深度克隆---使用序列化进行深拷贝
     *
     * @param obj
     * 要克隆的对象
     * @return
     * 注意：
     * 使用序列化的方式来实现对象的深拷贝，但是前提是，对象必须是实现了 Serializable接口才可以，Map本身没有实现
     * Serializable 这个接口，所以这种方式不能序列化Map，也就是不能深拷贝Map。但是HashMap是可以的，因为它实现了Serializable。
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }

    /**
     *
     * @param before 前一个对象
     * @param after 后一个对象
     * @param editable 对象类型
     * @param ignoreProperties 忽略的属性
     * @return 两个对象的相同参数的不同值
     */
    public static JSONObject findDifferent(Object before, Object after, Class<?> editable, String... ignoreProperties) {
        Assert.notNull(before, "Before must not be null");
        Assert.notNull(after, "After must not be null");
        
        Class<?> afterClass = after.getClass();
        Class<?> beforeClass = before.getClass();
        if (editable != null) {
            if (!editable.isInstance(after)&&!editable.isInstance(before)) {
                throw new IllegalArgumentException("After and Before class [" + afterClass.getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
        }else {
            if (!(afterClass == beforeClass)) {
                throw new IllegalArgumentException("After Class [" + afterClass.getName() +
                        "] not assignable to Before class [" + beforeClass.getName() + "]");
            }
        }
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        boolean isIgnore = ignoreList!=null;
        Field[] afterFields = afterClass.getDeclaredFields();
        Field[] beforeFields = beforeClass.getDeclaredFields();
        JSONObject jsonObject = new JSONObject();
        try {
        for (Field afterField : afterFields) {
            afterField.setAccessible(true);
            String afterFieldName = afterField.getName();
            if(isIgnore&&ignoreList.contains(afterFieldName)){
                continue;
            }
            for (Field beforeField : beforeFields) {
                beforeField.setAccessible(true);
                String beforeFieldName = beforeField.getName();
                if (afterFieldName.equals(beforeFieldName)) {
                    Object beforeObject = beforeField.get(before);
                    Object afterObject = afterField.get(after);
                    if (Objects.equals(afterObject, beforeObject)) {
                        break;
                    }
                    if (afterObject != null) {
                        jsonObject.put(afterFieldName + AFTER_SUFFIX, afterObject);
                    } else {
                        jsonObject.put(afterFieldName + AFTER_SUFFIX, DELETE);
                    }

                    if (beforeObject != null) {
                        jsonObject.put(beforeFieldName + BEFORE_SUFFIX, beforeObject);
                    }else {
                        jsonObject.put(beforeFieldName + BEFORE_SUFFIX, ADD);
                    }
                    break;
                }
            }
        }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject findDifferent(Object before, Object after) {
       return findDifferent(before,after, null, (String[]) null);
    }
    public static JSONObject findDifferent(Object before, Object after, Class<?> editable) {
        return findDifferent(before,after, editable, (String[]) null);
    }
    public static JSONObject findDifferent(Object before, Object after,String... ignoreProperties) {
        return findDifferent(before,after, null, ignoreProperties);
    }
}
