package com.dataeye.omp.report.dbproxy.hbase;

import org.apache.hadoop.hbase.client.Put;

public class PutItem {
	private String htable;
	private Put put;
	 
	
	public PutItem(String htable, Put put){
		this.htable = htable;
		this.setPut(put);
	}

	public String getHtable() {
		return htable;
	}

	public void setHtable(String htable) {
		this.htable = htable;
	}

	public Put getPut() {
		return put;
	}

	public void setPut(Put put) {
		this.put = put;
	}
}
