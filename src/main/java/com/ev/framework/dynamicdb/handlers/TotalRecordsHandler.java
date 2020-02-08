package com.ev.framework.dynamicdb.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TotalRecordsHandler  implements Handler  {
    @Override
    public Long handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getLong(1);
        }
        return null;
    }
}
