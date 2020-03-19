package com.plugin.javawidgetTest.jooq;

/**
 * @author lk
 * @date 2018/9/20 16:21
 */
public class t_userpay {

    private String userid;
    private String useramount;

    public String getUserid() {
        return this.userid;
    }

    public t_userpay setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUseramount() {
        return this.useramount;
    }

    public t_userpay setUseramount(String useramount) {
        this.useramount = useramount;
        return this;
    }

    @Override
    public String toString() {
        return "t_userpay{" +
                "userid='" + userid + '\'' +
                ", useramount='" + useramount + '\'' +
                '}';
    }
}
