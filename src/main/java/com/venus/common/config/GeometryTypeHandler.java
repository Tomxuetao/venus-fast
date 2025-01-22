package com.venus.common.config;

import java.sql.*;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

public class GeometryTypeHandler extends BaseTypeHandler<Geometry> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType) throws SQLException {
        // 使用 WKBWriter 将 Geometry 转为 WKB 字节数组
        WKBWriter writer = new WKBWriter();
        byte[] wkb = writer.write(parameter);
        ps.setBytes(i, wkb);
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, String columnName) throws SQLException  {
        byte[] wkb = rs.getBytes(columnName);
        try {
            return (wkb != null) ? new WKBReader().read(wkb) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] wkb = rs.getBytes(columnIndex);
        try {
            return (wkb != null) ? new WKBReader().read(wkb) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Geometry getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] wkb = cs.getBytes(columnIndex);
        try {
            return (wkb != null) ? new WKBReader().read(wkb) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
