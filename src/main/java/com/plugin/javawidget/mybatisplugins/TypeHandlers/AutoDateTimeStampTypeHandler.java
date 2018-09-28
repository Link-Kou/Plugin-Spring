package com.plugin.javawidget.mybatisplugins.TypeHandlers;

import com.plugin.javawidget.mybatisplugins.Type.MyDate;
import com.plugin.javawidget.mybatisplugins.Type.MyDateTimestamp;
import com.plugin.json.serializer.GsonDateTimestamp;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedJdbcTypes({JdbcType.TIMESTAMP})
@MappedTypes({GsonDateTimestamp.class,MyDateTimestamp.class})
public class AutoDateTimeStampTypeHandler<E> extends BaseTypeHandler<MyDateTimestamp> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MyDateTimestamp timestamp, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, timestamp.getDateFormat());
    }

    @Override
    public MyDateTimestamp getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {
            if (resultSet.getTimestamp(s) != null) {
                return new MyDateTimestamp(resultSet.getTimestamp(s));
            } else {
                return new MyDateTimestamp();
            }
        } catch (Exception e) {
            return new MyDateTimestamp();
        }
    }

    @Override
    public MyDateTimestamp getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            if (resultSet.getTimestamp(i) != null) {
                return new MyDateTimestamp(resultSet.getTimestamp(i));
            } else {
                return new MyDateTimestamp();
            }
        } catch (Exception e) {
            return new MyDateTimestamp();
        }
    }

    @Override
    public MyDateTimestamp getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            if (callableStatement.getTimestamp(i) != null) {
                return new MyDateTimestamp(callableStatement.getTimestamp(i));
            } else {
                return new MyDateTimestamp();
            }
        } catch (Exception e) {
            return new MyDateTimestamp();
        }
    }

    @Override
    public MyDateTimestamp getResult(ResultSet rs, String columnName) throws SQLException {
        MyDateTimestamp result = new MyDateTimestamp();
        try {
            result = this.getNullableResult(rs, columnName);
        } catch (Exception var5) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var5, var5);
        }
        return result;
    }


}
