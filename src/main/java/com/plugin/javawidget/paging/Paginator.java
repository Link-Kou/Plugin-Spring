package com.plugin.javawidget.paging;


import com.google.gson.annotations.Expose;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigUtils;
import com.plugin.configproperty.ConfigValue;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

/**
 * 统一分页工具
 * {
 *     page:
 *     itemsPerPage:
 *     data:
 *     ......:
 * }
 */
public class Paginator<T> {

    //默认分页-每页数量

    @ConfigValue(@Value("${Globalparam.Paging.DEFAULT_ITEMS_PER_PAGE}"))
    private transient Config<Integer> DEFAULT_ITEMS_PER_PAGE;

    //默认分页-当前页
    @ConfigValue(@Value("${Globalparam.Paging.DEFAULT_PAGE}"))
    private transient Config<Integer> DEFAULT_PAGE;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页页数
     */
    private Integer itemsPerPage;

    @Valid
    private T data;

    /**
     * 总页数
     */
    private int total;


    /**
     * 数据
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 数据
     * @param data
     */
    public void setData(T data){
        this.data = data;
    }

    /**
     * 偏移，根据传递的当前页转换
     * @return
     */
    public int getOffset() {
        return getPage() > 0 ? getItemsPerPage() * (getPage() - 1) : 0;
    }

    /**
     * 当前页
     * @return
     */
    public int getPage() {
        return page == null ? DEFAULT_PAGE.get() : page;
    }

    /**
     * 当前页
     * @return
     */
    public Paginator setPage(int val) {
        this.page = val;
        return this;
    }

    /**
     * 每页数量
     * @return
     */
    public Paginator setItemsPerPage(int val) {
        this.itemsPerPage = val;
        return this;
    }

    /**
     * 每页数量
     * @return
     */
    public int getItemsPerPage() {
        return itemsPerPage == null ? DEFAULT_ITEMS_PER_PAGE.get() : itemsPerPage;
    }

    /**
     * 获取总页数
     * 用于解决dao输出总页数的时候处理使用
     * @return
     */
    public int getTotal(){
        return this.total;
    }

    /**
     * 设置总页数
     * 用于解决dao输出总页数的时候处理使用
     * @return
     */
    public void setTotal(int total){
        this.total = total;
    }



}
