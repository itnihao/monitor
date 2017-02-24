package com.dataeye.omp.module.alarm.customize;

/**
 * Created by rodbate on 2016/3/4.
 */

import com.google.gson.annotations.Expose;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 自定义告警信息
 */
public class CustomizeAlarm {

    //自定义告警id
    @Expose
    private long id;

    //业务id
    @Expose
    private long businessID;
    
    @Expose
    private String businessName;

    //告警项
    @Expose
    private String alarmItem;

    //主要负责人id
    @Expose
    private int mainEmployee;
    
    @Expose
    private String mainEmployeeName;

    //其他负责人
    @Expose
    private String others;
    
    @Expose
    private List<Map<String, String>> othersName;

    //添加自定义告警时间
    @Expose
    private long createTime;

    //更新自定义告警时间
    @Expose
    private long updateTime;

    //自定义告警监控状态 0--暂停  1--启动
    @Expose
    private int status;

    //自定义告警备注
    @Expose
    private String remark;

    //告警对象字符串形式
    @Expose
    private String alarmObject;

    //告警对象Key-value形式
    @Expose
    private List<Map<String, String>> alarmObjectMap;

    //最近一个月的告警次数(以30天为标准)
    @Expose
    private long alarmTimesLatestMonth;

    //自定义告警规则
    @Expose
    private CustomizeAlarmRule rule;


    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBusinessID() {
		return businessID;
	}

	public void setBusinessID(long businessID) {
		this.businessID = businessID;
	}

	public String getAlarmItem() {
        return alarmItem;
    }

    public void setAlarmItem(String alarmItem) {
        this.alarmItem = alarmItem;
    }

    public int getMainEmployee() {
        return mainEmployee;
    }

    public void setMainEmployee(int mainEmployee) {
        this.mainEmployee = mainEmployee;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    

    public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAlarmObject() {
        return alarmObject;
    }

    public void setAlarmObject(String alarmObject) {
        this.alarmObject = alarmObject;
    }

    public long getAlarmTimesLatestMonth() {
        return alarmTimesLatestMonth;
    }

    public void setAlarmTimesLatestMonth(long alarmTimesLatestMonth) {
        this.alarmTimesLatestMonth = alarmTimesLatestMonth;
    }

    public CustomizeAlarmRule getRule() {
        return rule;
    }

    public void setRule(CustomizeAlarmRule rule) {
        this.rule = rule;
    }

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getMainEmployeeName() {
		return mainEmployeeName;
	}

	public void setMainEmployeeName(String mainEmployeeName) {
		this.mainEmployeeName = mainEmployeeName;
	}

	public List<Map<String, String>> getAlarmObjectMap() {
		return alarmObjectMap;
	}

	public void setAlarmObjectMap(List<Map<String, String>> alarmObjectMap) {
		this.alarmObjectMap = alarmObjectMap;
	}

	public List<Map<String, String>> getOthersName() {
		return othersName;
	}

	public void setOthersName(List<Map<String, String>> othersName) {
		this.othersName = othersName;
	}

	
	
	
    
}
