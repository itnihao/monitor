package com.dataeye.omp.module.Menu;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @auther wendywang
 * @since 2016/1/11 11:57
 */
public class Menu {

    /**
     * 菜单id
     */
    @Expose
    int id;

    /**
     * 菜单名称
     */
    @Expose
    String name;

    /**
     * 标题
     */
    @Expose
    String title;


    /**
     * 下级菜单
     */
    @Expose
    List<Menu> subs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Menu> getSubs() {
        return subs;
    }

    public void setSubs(List<Menu> subs) {
        this.subs = subs;
    }

    public Menu(int id, String name, String title) {
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public Menu(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public Menu(){}

}
