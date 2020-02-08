package com.ev.framework.dynamicdb.dbhelper;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Converter<T> {

	public static <T> T convert2Bean(ResultSet rs, Class<T> bean) throws Exception {
		Field[] fields = bean.getDeclaredFields();
		T obj = bean.newInstance();
		for (Field field : fields) {
			String pname = field.getName();
			BeanUtils.setProperty(obj, pname, rs.getObject(pname));
		}

		return obj != null ? obj : null;
	}

	public static <T> List<T> convert2BeanList(ResultSet rs, Class<T> bean) throws Exception {
		Field[] fields = bean.getDeclaredFields();
		List<T> lists = new ArrayList<T>();
		while (rs.next()) {
			T obj = bean.newInstance();
			for (Field field : fields) {
				String pname = field.getName();
				BeanUtils.setProperty(obj, pname, rs.getObject(pname));
			}
			lists.add(obj);
		}
		return lists != null ? lists : null;
	}

}
