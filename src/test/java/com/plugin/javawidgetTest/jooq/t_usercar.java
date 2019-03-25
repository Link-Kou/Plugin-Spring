package com.plugin.javawidgetTest.jooq;

/**
 * @author lk
 * @date 2018/9/20 16:20
 */
public class t_usercar {

    private String userid;
    private String carname;


    public String getUserid() {
        return this.userid;
    }

    public t_usercar setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getCarname() {
        return this.carname;
    }

    public t_usercar setCarname(String carname) {
        this.carname = carname;
        return this;
    }
}
