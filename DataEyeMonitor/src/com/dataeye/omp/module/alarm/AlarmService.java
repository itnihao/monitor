package com.dataeye.omp.module.alarm;

import clojure.lang.Obj;
import com.dataeye.common.CachedObjects;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.Separator;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.device.DeviceDao;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.omp.module.cmdb.employee.Employee;
import com.dataeye.omp.module.monitor.server.GroupDAO;
import org.hsqldb.lib.StringUtil;
import org.hsqldb.persist.CachedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @auther wendywang
 * @since 2016/2/19 2:35
 */
@Service
public class AlarmService {

    @Autowired
    private AlarmDAO alarmDAO;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private GroupDAO groupDao;



    /**
     * 获取告警规则列表
     * @param searchKey
     * @param pageID
     * @param pageSize
     * @param orderBy
     * @param order
     * @return
     * @throws ServerException
     */
    public PageData getAlarmRuleList(String searchKey, int pageID, int pageSize,
            String orderBy, int order) throws ServerException {
        List<AlarmRule> alarmRules = alarmDAO.getAlarmRuleList();
        List<Map<String, Object>> rules = assembleRules(alarmRules);
        PageData pageData = assemblePageData(rules, searchKey,
                pageID, pageSize, orderBy, order);
        return pageData;
    }

    /**
     * 根据机器和业务重组告警规则数据
     * @param alarmRules
     * @return
     * @throws ServerException
     */
    public List<Map<String, Object>> assembleRules(List<AlarmRule> alarmRules)
            throws ServerException {

        Map<Integer, String> deviceIDNames = deviceDao.getDeviceIDNames();
        Map<Integer, String> busiIDNames = businessDao.getBusiIDNames();
        Map<Integer, String> featurIDNames = featureDao.getfeatureIDNames();
        Map<Integer, String> groupNames = groupDao.getGroupIDNames();

        Map<String, List<AlarmRule>> rules = new HashMap<>();
        for (AlarmRule ar : alarmRules) {
            ar.setFeatureName(featurIDNames.get(ar.getFeatureID()));

            AlarmObject ao = ar.getAlarmObject();
            int alarmObjectType = ao.getAlarmObjectType();
            String key = "";
            switch (alarmObjectType) {
                case Constant.ALARM_TYPE_DEVICE: {
                    List<Integer> servers = ao.getServers();
                    for (Integer svrId : servers) {
                        String hostName = deviceIDNames.get(svrId);
                        key = hostName + Separator.DEFAULT + svrId
                                + Separator.DEFAULT + alarmObjectType;

                        addRules(key, ar, rules);
                    }
                    break;
                }

                case Constant.ALARM_TYPE_BUSINESS:{
                    int busiId = ao.getBusinessID();
                    String busiName = busiIDNames.get(ao.getBusinessID());
                    key = busiName + Separator.DEFAULT + busiId
                            + Separator.DEFAULT + alarmObjectType;
                    addRules(key, ar, rules);
                    break;
                }

                case Constant.ALARM_TYPE_GROUP:{
                    int groupID = ao.getGroupID();
                    String groupName = groupNames.get(ao.getGroupID());
                    key = groupName + Separator.DEFAULT + groupID
                            + Separator.DEFAULT + alarmObjectType;
                    addRules(key, ar, rules);
                    break;
                }
            }
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (String s : rules.keySet()) {
            int idx1 = s.indexOf(Separator.DEFAULT);
            int idx2 = s.lastIndexOf(Separator.DEFAULT);

            Map<String, Object> map = new HashMap<>();
            String alarmObjectName = s.substring(0, idx1);
            int alarmObjectId = Integer.parseInt(s.substring(idx1+1, idx2));
            int alarmObjectType = Integer.parseInt(s.substring(idx2+1));

            map.put("alarmObjectName", alarmObjectName);
            map.put("alarmObjectId", alarmObjectName + alarmObjectId);
            map.put("alarmObjectType", alarmObjectType);
            map.put("details", rules.get(s));
            list.add(map);
        }
        return list;
    }

    public void addRules(String key, AlarmRule ar,
                         Map<String, List<AlarmRule>> rules) {
        if (rules.get(key) != null) {
            rules.get(key).add(ar);
        } else {
            List<AlarmRule> list = new ArrayList<>();
            list.add(ar);
            rules.put(key, list);
        }
    }

    /**
     * 排序，分页
     * @param list
     * @param searchKey
     * @param pageID
     * @param pageSize
     * @param orderBy
     * @param order
     * @return
     */
    public PageData assemblePageData(List<Map<String, Object>> list,
                                  String searchKey, int pageID, int pageSize,
                                  String orderBy, int order){
        Comparator<Map> comparator = AlarmRuleComparator.ALARM_OBJECT_NAME_ASC;
        if (Constant.OrderBy.ORDERBY_ALARM_OBJECT_NAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = AlarmRuleComparator.ALARM_OBJECT_NAME_ASC;
            } else {
                comparator = AlarmRuleComparator.ALARM_OBJECT_NAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDREBY_ALARM_OBJECT_TYPE.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = AlarmRuleComparator.ALARM_OBJECT_TYPE_ASC;
            } else {
                comparator = AlarmRuleComparator.ALARM_OBJECT_TYPE_DESC;
            }
        }

        Collections.sort(list, comparator);

        List<Map<String, Object>> subList = new ArrayList<>();
        if (!StringUtil.isEmpty(searchKey)) {
            for (Map<String, Object> map : list) {
                String alarmObjectName = (String) map.get("alarmObjectName");
                if (alarmObjectName.toLowerCase().indexOf(searchKey.toLowerCase()) >= 0) {
                    subList.add(map);
                }
            }
        }else {
            subList = list;
        }

        int startIndex = (pageID - 1) * pageSize;
        int endIndex = (pageID * pageSize) > subList.size() ? subList.size() : (pageID * pageSize);

        PageData pageData = new PageData();
        if (startIndex > subList.size()) {
            return pageData;
        }

        pageData.setTotalRecord(subList.size());
        pageData.setTotalPage();

        pageData.setContent(subList.subList(startIndex, endIndex));

        return pageData;

    }

    /**
     * 告警设置下拉表单初始化数据
     * @return
     * @throws ServerException
     */
    public Map<String, Object> getAlarmSelectData(int uid)
            throws ServerException {
        List<DicValue> featureList = featureDao.getFeatureDicList();

        List<FeatureObject> featureObjects =
                featureDao.getfeatureObjectList();

        List<DicValue> deviceList = deviceDao.getSelectDeviceList();

        List<DicValue> busiDicList = businessDao.getBusiDicList();
        List<DicValue> moduleList = businessDao.getModuleDicList();
        List<DicValue> groupList = groupDao.getGroupDicListByUID(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("featureList", featureList);
        map.put("objectList", featureObjects);
        map.put("deviceList", deviceList);
        map.put("businessList", busiDicList);
        map.put("moduleList", moduleList);
        map.put("groupList", groupList);
        return map;
    }

    public static void main(String[] args) {


        String s = "dctest";
        System.out.println(s.indexOf("dc"));

        String s1 = "[1,2,5]";
       // List<Integer> list= CachedObjects.GSON.fromJson(s1,);
       // System.out.println(list.toString());

    }


}
