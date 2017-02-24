package com.dataeye.omp.report.dbproxy.hbase;
 
public class StatItem {
	private String htable;
	private String rowKey;
	private String statKey;
	private long value = 0;
	 
	
	public StatItem(String htable, String rowKey, String statKey, long value){
		this.htable = htable;
		this.rowKey = rowKey;
		this.statKey = statKey;
		this.value = value;
	}

	public String getHtable() {
		return htable;
	}

	public void setHtable(String htable) {
		this.htable = htable;
	}

	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public String getStatKey() {
		return statKey;
	}
	public void setStatKey(String statKey) {
		this.statKey = statKey;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	
	public void increase(long increaseValue){
		this.value += increaseValue;
	}

	@Override
	public String toString() {
		return "StatItem [htable=" + htable + ", rowKey=" + rowKey
				+ ", statKey=" + statKey + ", value=" + value + "]";
	}
	
	
}
