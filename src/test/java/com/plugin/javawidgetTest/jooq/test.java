package com.plugin.javawidgetTest.jooq;

import com.plugin.javawidget.driven.converters.JsonResult;
import com.plugin.javawidget.enums.SystemMsgEnums;
import com.plugin.json.Json;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.tuple;


public class test {


    private class json extends Json {

        protected <T> T fromJson(String json, Type type) {
            return super.fromJson(json, type);
        }

        protected String toJson(Object src) {
            return super.toJson(src);
        }
    }


    @Test
    public void name() {

        final Tuple3<String, String, String> tuple = tuple("", "", "");
        final Tuple tuple1 = tuple;
        final Object v11 = ((Tuple3) tuple1).v1;

        new JsonResult(SystemMsgEnums.OPS_FAILURE).isEnums(SystemMsgEnums.OPS_SUCCESS);

        List<Integer> s1 = Arrays.asList(1, 2);
        List<Integer> s2 = Arrays.asList(1, 3);
        List<Integer> s3 = Arrays.asList(1, 3);

        s1.stream()
                .flatMap(v1 -> s2.stream()
                        .map(v2 -> tuple(v1, v2)))
                .flatMap(v1 -> s3.stream()
                        .map(v2 -> tuple(v1, v2)))
                .filter(t -> {
                    return t.v1.v1.equals(t.v1.v2) && t.v1.v2.equals(t.v2);
                })
                .map(t -> tuple(t.v1.v1, t.v1.v2, t.v2))
                .forEach(System.out::println);
    }

    @Test
    public void CROSSJOIN() {
        /**
         * 场景:
         * 查询出来三张表的数据进行合并,三张表中都有userid
         */
        Seq<t_userinfo> s1 = Seq.of(
                new t_userinfo().setUserid("123").setUsername("小米"),
                new t_userinfo().setUserid("345").setUsername("小明"),
                new t_userinfo().setUserid("678").setUsername("小李")
        );
        Seq<t_usercar> s2 = Seq.of(
                new t_usercar().setUserid("123").setCarname("宝马"),
                new t_usercar().setUserid("345").setCarname("奥迪")
        );
        Seq<t_userpay> s3 = Seq.of(
                new t_userpay().setUserid("123").setUseramount("1000000"),
                new t_userpay().setUserid("345").setUseramount("10000"),
                new t_userpay().setUserid("678").setUseramount("1000")
        );

        final List<t_userassets> collect = s1.crossJoin(s2)
                .crossJoin(s3)
                .filter(t -> {
                    return t.v1.v1.getUserid().equals(t.v1.v2.getUserid()) && t.v1.v1.getUserid().equals(t.v2.getUserid());
                })
                .map(t -> new t_userassets()
                        .setUserid(t.v1.v1.getUserid())
                        .setUsername(t.v1.v1.getUsername())
                        .setCarname(t.v1.v2.getCarname())
                        .setUseramount(t.v2.getUseramount())
                )
                .collect(Collectors.toList());
        collect.forEach(x -> {
                    System.out.print(x.getUserid());
                    System.out.print(x.getUsername());
                    System.out.print(x.getCarname());
                    System.out.println(x.getUseramount());
                });
    }

    @Test
    public void INNERJOIN() {
        //两边都存在就返回
        Seq<Integer> s1 = Seq.of(1, 2);
        Seq<Integer> s2 = Seq.of(1, 3, 2);
        Seq<Integer> s3 = Seq.of(1, 3, 2);
        Seq<Integer> s4 = Seq.of(1, 3, 2);
        s1.innerJoin(s2, (t, u) -> Objects.equals(t, u))
                .innerJoin(s3, (t, u) -> Objects.equals(t.v1, u))
                .innerJoin(s4, (t, u) -> Objects.equals(t.v1.v1, u))
                .map(
                        t -> tuple(t.v1.v1.v1, t.v1.v1.v2, t.v1.v2, t.v2)
                ).filter(x -> x.v1 == 1)
                .forEach((x) -> {
                    System.out.println(x.v1);
                    System.out.println(x.v2);
                });
    }

    @Test
    public void LEFTJOIN() {

        /**
         * 场景:
         * 查询出来三张表的数据进行合并,三张表中都有userid
         */
        Seq<t_userinfo> s1 = Seq.of(
                new t_userinfo().setUserid("123").setUsername("小米"),
                new t_userinfo().setUserid("345").setUsername("小明"),
                new t_userinfo().setUserid("678").setUsername("小李")
        );
        Seq<t_usercar> s2 = Seq.of(
                new t_usercar().setUserid("123").setCarname("宝马"),
                new t_usercar().setUserid("345").setCarname("奥迪")
        );
        Seq<t_userpay> s3 = Seq.of(
                new t_userpay().setUserid("123").setUseramount("1000000"),
                new t_userpay().setUserid("345").setUseramount("10000"),
                new t_userpay().setUserid("678").setUseramount("1000")
        );

        s1.leftOuterJoin(s2, (t, u) -> Objects.equals(t.getUserid(), u.getUserid()))
                .leftOuterJoin(s3,(t,u) -> Objects.equals(t.v1.getUserid(),u.getUserid()))
                .map(t -> tuple(t.v1.v1, t.v1.v2,t.v2))
                .forEach((x) -> {
                    System.out.println(x.v1.getUserid());
                    System.out.println(
                            Optional.ofNullable(x.v2).orElse(new t_usercar()).getCarname()
                    );
                    System.out.println(
                            Optional.ofNullable(x.v3).orElse(new t_userpay()).getUseramount()
                    );
                });
    }

    @Test
    public void RIGHTJOIN() {

        /**
         * 场景:
         * 查询出来三张表的数据进行合并,三张表中都有userid
         */
        Seq<t_userinfo> s1 = Seq.of(
                new t_userinfo().setUserid("123").setUsername("小米"),
                new t_userinfo().setUserid("345").setUsername("小明"),
                new t_userinfo().setUserid("678").setUsername("小李")
        );
        Seq<t_usercar> s2 = Seq.of(
                new t_usercar().setUserid("123").setCarname("宝马"),
                new t_usercar().setUserid("345").setCarname("奥迪")
        );
        Seq<t_userpay> s3 = Seq.of(
                new t_userpay().setUserid("123").setUseramount("1000000"),
                new t_userpay().setUserid("345").setUseramount("10000"),
                new t_userpay().setUserid("678").setUseramount("1000")
        );

        s1.rightOuterJoin(s2, (t, u) -> Objects.equals(t.getUserid(), u.getUserid()))
                .rightOuterJoin(s3,(t,u) -> Objects.equals(t.v1.getUserid(),u.getUserid()))
                //t.v1有为空的情况
                .map(t -> tuple(t.v1 == null ? null : t.v1.v1, t.v1 == null ?  null : t.v1.v2,t.v2))
                .forEach((x) -> {
                    System.out.println(
                            Optional.ofNullable(x.v1).orElse(new t_userinfo()).getUserid());
                    System.out.println(
                            Optional.ofNullable(x.v2).orElse(new t_usercar()).getCarname()
                    );
                    System.out.println(
                            Optional.ofNullable(x.v3).orElse(new t_userpay()).getUseramount()
                    );
                });
    }
}
