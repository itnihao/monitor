package com.dataeye.omp.common;

import com.google.gson.annotations.Expose;

/**
 * 一个基本key-value对象
 * 
 * @author ivan
 * @since 2013-7-18 下午3:22:12
 */
public class ValueItem {
	@Expose
	private Object value;
	@Expose
	private Object item;

	public ValueItem() {
	}

	public ValueItem(Object value, Object item) {
		this.value = value;
		this.item = item;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public Object getValue() {
		return value;
	}

	public Object getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "[" + value + "," + item + "]";
	}
}
