package com.dataeye.omp.module.cmdb.device;

import com.google.gson.annotations.Expose;

/**
 * 字典项
 * @auther wendy
 * @since 2015/12/27 13:48
 */
public class DicItem {

    @Expose
    private int id;

    @Expose
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
