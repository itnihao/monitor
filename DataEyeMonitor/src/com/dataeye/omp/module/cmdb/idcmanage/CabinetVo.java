package com.dataeye.omp.module.cmdb.idcmanage;


import com.google.gson.annotations.Expose;

import java.util.List;

public class CabinetVo {

    @Expose
    private String name;
    @Expose
    private List<HostNameAndIP> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HostNameAndIP> getList() {
        return list;
    }

    public void setList(List<HostNameAndIP> list) {
        this.list = list;
    }

}
