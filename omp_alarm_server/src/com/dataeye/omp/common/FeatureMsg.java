package com.dataeye.omp.common;

import java.util.List;

import com.google.gson.Gson;

/**
 * 上报的特性数据
 * 
 * @author chenfanglin
 * @date 2016年1月26日 下午3:08:00
 */
public class FeatureMsg {
	public static Gson gson = new Gson();
	
	private List<Feature> feature_list;

	public List<Feature> getFeature_list() {
		return feature_list;
	}

	public void setFeature_list(List<Feature> feature_list) {
		this.feature_list = feature_list;
	}
	
	public static FeatureMsg parseJson(String json){
		return gson.fromJson(json, FeatureMsg.class);
	}
}
