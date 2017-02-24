package com.dataeye.omp.common;

import com.google.gson.annotations.Expose;

/**
 * @author Ivan <br>
 * @date 2015年8月14日 上午10:46:19 <br>
 *
 */
public class KeyValue<K, V> {
	@Expose
	private K key;
	@Expose
	private V value;

	public KeyValue() {
	}

	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public K getKey() {
		return key;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "(" + key + "," + value + ")";
	}
}
