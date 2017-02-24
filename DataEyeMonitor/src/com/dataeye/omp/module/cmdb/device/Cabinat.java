package com.dataeye.omp.module.cmdb.device;

/**
 * 机柜信息
 * @auther wendy
 * @since 2015/12/27 13:45
 */
public class  Cabinat {
    private int id;

    private int idcId;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcId() {
        return idcId;
    }

    public void setIdcId(int idcId) {
        this.idcId = idcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
