package com.dataeye.common;

import com.dataeye.omp.common.ValueItem;

import java.util.List;

public class Department {

	private int Count;
	
	private List<ValueItem> List;

	/**
	 * @return the count
	 */
	public int getCount() {
		return Count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		Count = count;
	}

	/**
	 * @return the list
	 */
	public List<ValueItem> getList() {
		return List;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ValueItem> list) {
		List = list;
	}
	
}
