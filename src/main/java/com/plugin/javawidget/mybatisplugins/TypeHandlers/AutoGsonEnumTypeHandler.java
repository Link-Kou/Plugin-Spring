package com.plugin.javawidget.mybatisplugins.TypeHandlers;

import com.plugin.javawidget.mybatisplugins.Type.MyGsonEnum;
import com.plugin.json.serializer.GsonEnum;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.*;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lk
 * @date 2018/9/25 17:02
 */
@MappedJdbcTypes({JdbcType.TINYINT})
@MappedTypes({GsonEnum.class,Enum.class})
public class AutoGsonEnumTypeHandler<E> extends BaseTypeHandler<GsonEnum> {



    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GsonEnum gsonEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, (Integer) gsonEnum.serialize());
    }

    @Override
    public GsonEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {
            final int anInt = resultSet.getInt(s);
            return new MyGsonEnum(anInt);
        } catch (Exception e) {
            return new MyGsonEnum(null);
        }
    }

    @Override
    public GsonEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            final int anInt = resultSet.getInt(i);
            return new MyGsonEnum(anInt);
        } catch (Exception e) {
            return new MyGsonEnum(null);
        }
    }

    @Override
    public GsonEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            final int anInt = callableStatement.getInt(i);
            return new MyGsonEnum(anInt);
        } catch (Exception e) {
            return new MyGsonEnum(null);
        }
    }

    @Override
    public GsonEnum getResult(ResultSet rs, String columnName) throws SQLException {
        MyGsonEnum result = new MyGsonEnum(null);
        try {
            result = (MyGsonEnum) this.getNullableResult(rs, columnName);
        } catch (Exception var5) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var5, var5);
        }
        return result;
    }

}
