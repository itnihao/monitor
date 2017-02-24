package com.dataeye.omp.module.alarm;

import java.util.Comparator;
import java.util.Map;

/**
 * 告警规则比较器
 * @auther wendy
 * @since 2016/2/19 11:25
 */
public class AlarmRuleComparator {

    /**
     * 点告警分类降序
     */
    public static final Comparator<Map> ALARM_OBJECT_TYPE_DESC = new Comparator<Map>() {
        @Override
        public int compare(Map o1, Map o2) {

            if ((Integer)o1.get("alarmObjectType") >(Integer) o2.get("alarmObjectType")) {
                return -1;
            }
            if ((Integer)o1.get("alarmObjectType") <(Integer) o2.get("alarmObjectType")) {
                return 1;
            }
            return 0;
        }
    };

    /**
     * 点告警分类升序
     */
    public static final Comparator<Map> ALARM_OBJECT_TYPE_ASC = new Comparator<Map>() {
        @Override
        public int compare(Map o1, Map o2) {

            if ((Integer)o1.get("alarmObjectType") >(Integer) o2.get("alarmObjectType")) {
                return -1;
            }
            if ((Integer)o1.get("alarmObjectType") <(Integer) o2.get("alarmObjectType")) {
                return 1;
            }
            return 0;
        }
    };


    /**
     * 点告警对象名称降序
     */
    public static final Comparator<Map> ALARM_OBJECT_NAME_DESC = new Comparator<Map>() {
        @Override
        public int compare(Map o1, Map o2) {
            if (((String)o1.get("alarmObjectName")).compareTo((String)o2.get("alarmObjectName")) > 0) {
                return -1;
            }
            if (((String)o1.get("alarmObjectName")).compareTo((String)o2.get("alarmObjectName"))  < 0) {
                return 1;
            }
            return 0;
        }
    };

    /**
     * 点告警对象名称升序
     */
    public static final Comparator<Map> ALARM_OBJECT_NAME_ASC = new Comparator<Map>() {
        @Override
        public int compare(Map o1, Map o2) {
            if (((String)o1.get("alarmObjectName")).compareTo((String)o2.get("alarmObjectName")) < 0) {
                return -1;
            }
            if (((String)o1.get("alarmObjectName")).compareTo((String)o2.get("alarmObjectName"))  > 0) {
                return 1;
            }
            return 0;
        }
    };
}
