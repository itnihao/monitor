package com.dataeye.omp.module.jvm;

import com.google.gson.annotations.Expose;

/**
 * 进程信息
 * Created by wendy on 2016/6/6.
 */
public class JpsInfo {

    @Expose
    private int pid;

    @Expose
    private String detail;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
