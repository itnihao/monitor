package com.dataeye.omp.common;

import com.dataeye.utils.ReflectUtils;

import java.util.Comparator;
import java.util.Set;

public abstract class MyComparator<T> implements Comparator<T> {
	public abstract Class<T> getRealClass();

	private String field;

	public MyComparator(String fields) {
		this.field = fields;
	}

	public Set<String> getAllFields() {
		return ReflectUtils.getAllFieldsIncludeParent(getRealClass());
	}

	@Override
	public int compare(T o1, T o2) {
		Class<?> clz = ReflectUtils.getFieldType(getRealClass(), field);
		if (clz == Integer.class || clz == int.class) {
			Object v1 = ReflectUtils.getFieldValue(o1, field);
			int int1 = (Integer) v1;
			Object v2 = ReflectUtils.getFieldValue(o2, field);
			int int2 = (Integer) v1;

		} else if (clz == Long.class || clz == long.class) {

		}
		return 0;
	}
}
