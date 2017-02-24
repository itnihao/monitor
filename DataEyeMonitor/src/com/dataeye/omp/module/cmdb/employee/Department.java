package com.dataeye.omp.module.cmdb.employee;

import com.google.gson.annotations.Expose;

/**
 * 部门信息
 * @auther wendy
 * @since 2015/12/27 13:48
 */
public class Department {

    private int id;

    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
