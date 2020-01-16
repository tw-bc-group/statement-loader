package com.thoughtworks.blockchain.statementloader.batch.mysql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlRowMapper implements RowMapper<JsonObject> {

    @Override
    public JsonObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        final JsonObject jsonObject = new JsonObject();
        int columnCount = rs.getMetaData().getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            final Object columnObj = rs.getObject(i + 1);
            jsonObject.add(rs.getMetaData().getColumnLabel(i + 1), new Gson().toJsonTree(columnObj));
        }
        return jsonObject;
    }
}
