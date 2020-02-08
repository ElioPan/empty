package com.ev.framework.dynamicdb.handlers;


import com.ev.framework.dynamicdb.dbhelper.Converter;

import java.sql.ResultSet;

public class BeanHandler<T> implements Handler<T> {

	/**
	 * The Class of beans produced by this handler.
	 */
	private final Class<T> type;

	/**
	 * Creates a new instance of BeanHandler.
	 *
	 * @param type
	 *            The Class that objects returned from <code>handle()</code> are
	 *            created from.
	 */
	public BeanHandler(Class<T> type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see handlers.Handler#handle(java.sql.ResultSet)
	 */
	@Override
	public T handle(ResultSet rs) {
		try {
			// 此处的rs.next()可谓是智慧闪现的最佳体现啊
			return rs.next() ? Converter.convert2Bean(rs, this.type) : null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
