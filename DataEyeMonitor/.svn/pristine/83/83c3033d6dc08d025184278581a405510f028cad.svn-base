package com.dataeye.omp.module.alarm;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.business.Business;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther wendy
 * @since 2016/1/27 9:54
 */
@Service
public class FeatureDao {
    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplateMonitor;

    /**
     * 获取所有特性信息
     *
     * @return
     * @throws ServerException
     */
    public List<Feature> getFeatureList() throws ServerException {
        final List<Feature> featureList = new ArrayList<>();
        try {
            String sql = "select id, name, type, sys, category from feature_list ";
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Feature feature = new Feature();
                    feature.setId(rs.getInt("id"));
                    feature.setName(rs.getString("name"));
                    feature.setType(rs.getInt("type"));
                    feature.setSys(rs.getInt("sys"));
                    feature.setCategory(rs.getInt("category"));
                    featureList.add(feature);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return featureList;
    }

    /**
     * 获取特性和对象对应关系
     *
     * @return
     * @throws ServerException
     */
    public List<FeatureObject> getfeatureObjectList() throws ServerException {
        try {
            String sql = "select feature_id featureId, object,"
                    + "object_name objectName from feature_object";
            List<FeatureObject> featureObjects =
                    jdbcTemplateMonitor.queryForList(sql, FeatureObject.class);
            return featureObjects;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return null;
    }

    /**
     * 获取特性ID,名称
     *
     * @return
     * @throws ServerException
     */
    public Map<Integer, String> getfeatureIDNames() throws ServerException {
        final Map<Integer, String> map = new HashMap<>();
        try {
            String sql = "select id, name from feature_list";
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put(rs.getInt("id"), rs.getString("name"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return map;
    }

    public List<DicValue> getFeatureDicList() throws ServerException {
        try {
            List<Feature> featureList = getFeatureList();
            List<DicValue> dicValues = new ArrayList<>();
            for (Feature feature : featureList) {
                DicValue dicValue = new DicValue();
                dicValue.setId(feature.getId());
                dicValue.setLabel(feature.getName());
                dicValue.setValue(feature.getId());
                dicValues.add(dicValue);
            }
            return dicValues;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return null;
    }
}