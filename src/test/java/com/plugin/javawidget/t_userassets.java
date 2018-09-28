package com.plugin.javawidget;

/**
 * @author lk
 * @date 2018/9/20 16:32
 */
public class t_userassets {

    private String userid;
    private String carname;
    private String username;
    private String useramount;


    public String getUserid() {
        return this.userid;
    }

    public t_userassets setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getCarname() {
        return this.carname;
    }

    public t_userassets setCarname(String carname) {
        this.carname = carname;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public t_userassets setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getUseramount() {
        return this.useramount;
    }

    public t_userassets setUseramount(String useramount) {
        this.useramount = useramount;
        return this;
    }
}
