package com.dataeye.omp.common;

import java.util.List;

import com.google.gson.Gson;

/**
 * 进程信息
 * @author chenfanglin
 * @date 2016年3月7日 下午2:36:14
 */
public class ProcessList {
	public static Gson gson = new Gson();
    private List<ProcessInfo> process_list;

    public List<ProcessInfo> getProcess_list() {
        return process_list;
    }

    public void setProcess_list(List<ProcessInfo> process_list) {
        this.process_list = process_list;
    }
    public static ProcessList parseJson(String json){
		return gson.fromJson(json, ProcessList.class);
	}
}
