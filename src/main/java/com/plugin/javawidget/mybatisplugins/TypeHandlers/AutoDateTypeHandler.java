package com.plugin.javawidget.mybatisplugins.TypeHandlers;

import com.plugin.javawidget.mybatisplugins.Type.MyDate;
import com.plugin.json.serializer.GsonDate;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes({JdbcType.DATE})
@MappedTypes({GsonDate.class,MyDate.class})
public class AutoDateTypeHandler<E> extends BaseTypeHandler<MyDate> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MyDate timestamp, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, timestamp.getDateFormat());
    }

    @Override
    public MyDate getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {
            if (resultSet.getDate(s) != null) {
                return new MyDate(resultSet.getDate(s));
            } else {
                return new MyDate();
            }
        } catch (Exception e) {
            return new MyDate();
        }
    }

    @Override
    public MyDate getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            if (resultSet.getDate(i) != null) {
                return new MyDate(resultSet.getDate(i));
            } else {
                return new MyDate();
            }
        } catch (Exception e) {
            return new MyDate();
        }
    }

    @Override
    public MyDate getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            if (callableStatement.getDate(i) != null) {
                return new MyDate(callableStatement.getDate(i));
            } else {
                return new MyDate();
            }

        } catch (Exception e) {
            return new MyDate();
        }
    }

    @Override
    public MyDate getResult(ResultSet rs, String columnName) throws SQLException {
        MyDate result = new MyDate();
        try {
            result = this.getNullableResult(rs, columnName);
        } catch (Exception var5) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var5, var5);
        }
        return result;
    }


}
