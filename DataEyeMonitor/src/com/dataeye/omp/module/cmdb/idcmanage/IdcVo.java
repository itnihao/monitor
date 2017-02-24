package com.dataeye.omp.module.cmdb.idcmanage;


import com.google.gson.annotations.Expose;

import java.util.List;

public class IdcVo {

    @Expose
    private String name;
    @Expose
    private List<CabinetVo> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CabinetVo> getList() {
        return list;
    }

    public void setList(List<CabinetVo> list) {
        this.list = list;
    }

}
