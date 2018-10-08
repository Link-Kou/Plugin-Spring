package com.plugin.javawidget.mybatisplugins.Type;


import com.google.gson.JsonElement;
import com.plugin.json.serializer.GsonDate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间的扩展
 * 支持 0000-00-00
 * @author lk
 * @version 1.0
 */
public class MyDate implements GsonDate<MyDate> {

    private final static String DATEDEF = "0000-00-00";
    private String datestr = DATEDEF;

    private transient DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MyDate() {

    }

    public MyDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datestr = datetimeformatter.format(localDate);
    }

    public MyDate(java.sql.Date date) {
        LocalDate localDate = date.toLocalDate();
        datestr = datetimeformatter.format(localDate);
    }

    public MyDate(LocalDate localDate) {
        datestr = datetimeformatter.format(localDate);
    }

    public MyDate(int year, int month, int dayOfMonth) {
        datestr = datetimeformatter.format(LocalDate.of(year, month, dayOfMonth));
    }

    public MyDate(String DateTime) {
        try {
            final LocalDate parse = LocalDate.parse(DateTime, datetimeformatter);
            datestr = datetimeformatter.format(parse);
        } catch (Exception e) {
            if(!datestr.equals(DateTime)){
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化时间
     *
     * @param source
     * @return void
     * @author lk
     */
    @Override
    public MyDate parse(String source) {
        return this;
    }

    /**
     * 是否为0000-00-00
     *
     * @return true 是 false 不是
     */
    @Override
    public boolean isZeroDate() {
        return datestr.equals(DATEDEF);
    }

    /**
     * 获取Date时间
     *
     * @return
     */
    @Override
    public Date getDate() {
        return Date.from(LocalDateTime.parse(this.datestr, datetimeformatter).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime
     * getTimestamp().toLocalTime();
     * getTimestamp().toLocalDate();
     *
     * @return
     */
    @Override
    public LocalDate getLocalDate() {
        return LocalDate.parse(this.datestr, datetimeformatter);
    }

    @Override
    public String getDateFormat() {
        return this.datestr;
    }

    /**
     * 编码
     *
     * @return
     */
    @Override
    public Object serialize() {
        return this.datestr;
    }

    /**
     * 转换
     *
     * @param jsonEnum
     * @return
     */
    @Override
    public MyDate deserialize(JsonElement jsonEnum) {
        return new MyDate(jsonEnum.getAsString());
    }
}
