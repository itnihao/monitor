package com.dataeye.common;


import java.util.List;

public class EmployeeResponse {

    private int id;
    private int status;
    private List<EmployeeInfo> content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<EmployeeInfo> getContent() {
        return content;
    }

    public void setContent(List<EmployeeInfo> content) {
        this.content = content;
    }
}
