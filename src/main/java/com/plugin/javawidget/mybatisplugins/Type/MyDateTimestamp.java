package com.plugin.javawidget.mybatisplugins.Type;


import com.google.gson.JsonElement;
import com.plugin.json.serializer.GsonDateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间的扩展
 * 支持 0000-00-00 00:00:00
 * @author lk
 * @version 1.0
 */
public class MyDateTimestamp implements GsonDateTimestamp<MyDateTimestamp> {

    private final static String DATEDEF = "0000-00-00 00:00:00";
    private String datestr = DATEDEF;

    private transient DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public MyDateTimestamp() {

    }

    public MyDateTimestamp(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        datestr = datetimeformatter.format(localDateTime);
    }

    public MyDateTimestamp(Timestamp date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        datestr = datetimeformatter.format(localDateTime);
    }

    public MyDateTimestamp(LocalDateTime localDateTime) {
        datestr = datetimeformatter.format(localDateTime);
    }

    public MyDateTimestamp(int year, int month, int dayOfMonth, int hour, int minute) {
        datestr = datetimeformatter.format(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
    }

    public MyDateTimestamp(String DateTime) {
        try {
            final LocalDateTime parse = LocalDateTime.parse(DateTime, datetimeformatter);
            datestr = datetimeformatter.format(parse);
        }catch (Exception e){
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
    public MyDateTimestamp parse(String source) {
        return this;
    }

    /**
     * 是否为0000-00-00 00-00-00
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
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.parse(this.datestr, datetimeformatter);
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
    public MyDateTimestamp deserialize(JsonElement jsonEnum) {
        return new MyDateTimestamp(jsonEnum.getAsString());
    }
}
