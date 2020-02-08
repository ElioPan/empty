package com.ev.framework.dynamicdb.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Handler<T> {

	public T handle(ResultSet rs) throws SQLException;

}
