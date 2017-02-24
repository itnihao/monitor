package com.dataeye.monitor.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResourceLoad {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoad.class);

    private ResourceLoad () {
        init();
    }

    private static ResourceLoad instance;

    public synchronized static ResourceLoad getInstance(){
        if (instance == null) {
            instance = new ResourceLoad();
        }
        return instance;
    }

    private static ConcurrentMap<String, Map<String, String >> properties = new ConcurrentHashMap<>();


    public void init(){
        FileListener.register(new FileUpdate() {
            @Override
            public void update(String fileName) {
                if (fileName.endsWith(".properties")) {
                    reload(fileName);
                }
            }
        });
    }

    private void load(String filePath){
        Objects.requireNonNull(filePath, "File Path can not be null");

        Map<String, String> kvs = new HashMap<>();
        InputStream in = null;
        Properties props = new Properties();
        try {
            in = new FileInputStream(filePath);
            props.load(in);
            Enumeration names = props.propertyNames();
            while (names.hasMoreElements()) {
                String name =(String) names.nextElement();
                String value =(String) props.get(name);
                kvs.put(name, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        properties.putIfAbsent(filePath, kvs);
    }

    private void reload(String filePath){
        LOGGER.info("the file {} auto reload", filePath);
        properties.remove(filePath);
        load(filePath);
    }

    public String getValue(String filePath, String key){
        Map<String, String> kvs = properties.get(filePath);
        if (kvs == null) {
            load(filePath);
            kvs = properties.get(filePath);
            if (kvs == null) return null;
        }
        System.out.println("path" + filePath + " key:" + key + " value:" + kvs.get(key));
        return kvs.get(key);
    }
}
