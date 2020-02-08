package com.ev.framework.dynamicdb.handlers;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class MapListHandler implements ListHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see handlers.Handler#handle(java.sql.ResultSet)
	 */
	@Override
	public List<Map> handle(ResultSet rs) throws SQLException {
		try {
			List list = new ArrayList();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount(); //Map rowData;
			while (rs.next()) { //rowData = new HashMap(columnCount);
				Map rowData = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnLabel(i), rs.getObject(i));
				}
				list.add(rowData);
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LinkedHashMap> handleLinked(ResultSet rs) throws SQLException {
		try {
			List list = new ArrayList();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount(); //Map rowData;
			while (rs.next()) { //rowData = new HashMap(columnCount);
				LinkedHashMap rowData = new LinkedHashMap();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnLabel(i), rs.getObject(i));
				}
				list.add(rowData);
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
