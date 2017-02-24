package com.dataeye.omp.module.alarm.basic;

import java.util.Comparator;

/**
 * Created by mr on 2016/3/10.
 */
public class AlarmBasicRuleComparator {

    public static final Comparator<AlarmBasicRule> Feature_NAME_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getFeatureName().compareTo(o2.getFeatureName()) > 0) return 1;
            if (o1.getFeatureName().compareTo(o2.getFeatureName()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> Feature_NAME_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getFeatureName().compareTo(o2.getFeatureName()) > 0) return -1;
            if (o1.getFeatureName().compareTo(o2.getFeatureName()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> OBJECT_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getObject().compareTo(o2.getObject()) > 0) return 1;
            if (o1.getObject().compareTo(o2.getObject()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> OBJECT_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getObject().compareTo(o2.getObject()) > 0) return -1;
            if (o1.getObject().compareTo(o2.getObject()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_OBJECT_TYPE_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmObjectType() > o2.getAlarmObjectType()) return 1;
            if (o1.getAlarmObjectType() < o2.getAlarmObjectType()) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_OBJECT_TYPE_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmObjectType() > o2.getAlarmObjectType()) return -1;
            if (o1.getAlarmObjectType() < o2.getAlarmObjectType()) return 1;
            return 0;
        }
    };


    public static final Comparator<AlarmBasicRule> SERVERS_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getServers().compareTo(o2.getServers()) > 0) return 1;
            if (o1.getServers().compareTo(o2.getServers()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> SERVERS_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getServers().compareTo(o2.getServers()) > 0) return -1;
            if (o1.getServers().compareTo(o2.getServers()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> BUSINESS_NAME_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getBusinessName().compareTo(o2.getBusinessName()) > 0) return 1;
            if (o1.getBusinessName().compareTo(o2.getBusinessName()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> BUSINESS_NAME_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getBusinessName().compareTo(o2.getBusinessName()) > 0) return -1;
            if (o1.getBusinessName().compareTo(o2.getBusinessName()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MODULE_NAME_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) return 1;
            if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MODULE_NAME_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) return -1;
            if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> GROUP_NAME_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getGroupName().compareTo(o2.getGroupName()) > 0) return 1;
            if (o1.getGroupName().compareTo(o2.getGroupName()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> GROUP_NAME_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getGroupName().compareTo(o2.getGroupName()) > 0) return -1;
            if (o1.getGroupName().compareTo(o2.getGroupName()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_SECTION_TYPE_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmSectionType() > o2.getAlarmSectionType()) return 1;
            if (o1.getAlarmSectionType() < o2.getAlarmSectionType()) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_SECTION_TYPE_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmSectionType() > o2.getAlarmSectionType()) return -1;
            if (o1.getAlarmSectionType() < o2.getAlarmSectionType()) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_THRESHOLD_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxThreshold().compareTo(o2.getMaxThreshold()) > 0) return 1;
            if (o1.getMaxThreshold().compareTo(o2.getMaxThreshold()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_THRESHOLD_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxThreshold().compareTo(o2.getMaxThreshold()) > 0) return -1;
            if (o1.getMaxThreshold().compareTo(o2.getMaxThreshold()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MIN_THRESHOLD_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMinThreshold().compareTo(o2.getMinThreshold()) > 0) return 1;
            if (o1.getMinThreshold().compareTo(o2.getMinThreshold()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MIN_THRESHOLD_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMinThreshold().compareTo(o2.getMinThreshold()) > 0) return -1;
            if (o1.getMinThreshold().compareTo(o2.getMinThreshold()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_MOM_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxMoM().compareTo(o2.getMaxMoM()) > 0) return 1;
            if (o1.getMaxMoM().compareTo(o2.getMaxMoM()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_MOM_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxMoM().compareTo(o2.getMaxMoM()) > 0) return -1;
            if (o1.getMaxMoM().compareTo(o2.getMaxMoM()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MIN_MOM_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMinMoM().compareTo(o2.getMinMoM()) > 0) return 1;
            if (o1.getMinMoM().compareTo(o2.getMinMoM()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MIN_MOM_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMinMoM().compareTo(o2.getMinMoM()) > 0) return -1;
            if (o1.getMinMoM().compareTo(o2.getMinMoM()) < 0) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_FREQUENCY_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxFrequency() > o2.getMaxFrequency()) return 1;
            if (o1.getMaxFrequency() < o2.getMaxFrequency()) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> MAX_FREQUENCY_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getMaxFrequency() > o2.getMaxFrequency()) return -1;
            if (o1.getMaxFrequency() < o2.getMaxFrequency()) return 1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_TYPE_ASC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmType().compareTo(o2.getAlarmType()) > 0) return 1;
            if (o1.getAlarmType().compareTo(o2.getAlarmType()) < 0) return -1;
            return 0;
        }
    };

    public static final Comparator<AlarmBasicRule> ALARM_TYPE_DESC = new Comparator<AlarmBasicRule>() {
        @Override
        public int compare(AlarmBasicRule o1, AlarmBasicRule o2) {
            if (o1.getAlarmType().compareTo(o2.getAlarmType()) > 0) return 1;
            if (o1.getAlarmType().compareTo(o2.getAlarmType()) < 0) return -1;
            return 0;
        }
    };
}
