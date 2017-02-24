package com.dataeye.omp.module.alarm;

import com.google.gson.annotations.Expose;

/**
 * @auther wendy
 * @since 2016/2/18 11:46
 */
public class FeatureObject {
    @Expose
    /**特性Id*/
    private int featureId;

    @Expose
    /**监控对象*/
    private String object;

    @Expose
    /**监控对象名称*/
    private String objectName;

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
