package com.dataeye.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dataeye.common.CachedObjects;
import com.dataeye.omp.constant.Constant.Separator;
import com.dataeye.omp.constant.Constant.ServerCfg;

/**
 * <pre>
 * 反射工具类
 * 
 * @author Ivan <br>
 * @date 2015年4月1日 上午9:45:58 <br>
 *
 */
public class ReflectUtils {
	/**
	 * 
	 * <pre>
	 * 获得类中所有的属性以及父类或者父接口的属性
	 * 
	 * @param clazz
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月1日 上午10:09:02 <br>
	 */
	public static Set<String> getAllFieldsIncludeParent(Class<?> clazz) {
		Set<String> fieldSet = new HashSet<String>();
		// 获取父类属性
		Field[] fieldArr = clazz.getSuperclass().getDeclaredFields();
		for (Field f : fieldArr) {
			fieldSet.add(f.getName());
		}
		// 获取自身的属性
		fieldArr = clazz.getDeclaredFields();
		for (Field f : fieldArr) {
			fieldSet.add(f.getName());
		}
		return fieldSet;
	}

	/**
	 * 
	 * <pre>
	 * 获取所有方法名包括父类以及父接口的
	 * 
	 * @param clazz
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月1日 上午10:24:31 <br>
	 */
	public static Set<String> getAllMethodIncludeParent(Class<?> clazz) {
		Set<String> methodSet = new HashSet<String>();
		Method[] methodArr = clazz.getMethods();
		for (Method method : methodArr) {
			methodSet.add(method.getName());
		}
		return methodSet;
	}

	/**
	 * 
	 * <pre>
	 * 通过反射调用某个对象属性的set方法,并且
	 * 
	 * @param clazz
	 *            某个类
	 * @param attr
	 *            某个属性
	 * @param obj
	 *            这个类的某个对象
	 * @param value
	 *            设置成什么值
	 * @return
	 * @author Ivan<br>
	 * @date 2015年2月25日 下午4:12:58 <br>
	 */
	public static <T> boolean callMethodSet(Class<T> clazz, String attr, T obj, String value) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(attr, clazz);
			Method setter = pd.getWriteMethod();// 获得set方法
			Object object = autoConv(clazz, attr, value);
			if (object != null) {
				setter.invoke(obj, object);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 获取某个类的某个属性的类型
	 * 
	 * @param clazz
	 * @param attr
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月1日 下午4:44:28 <br>
	 */
	public static Class<?> getFieldType(Class<?> clazz, String attr) {
		try {
			Field field = clazz.getDeclaredField(attr);
			if (field != null) {
				return field.getType();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 调用某个对象的get方法
	 * 
	 * @param clz
	 * @param obj
	 * @param attr
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月1日 下午5:25:34 <br>
	 */
	public static Object getFieldValue(Object obj, String attr) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(attr, obj.getClass());
			Method getter = pd.getReadMethod();// 获取set方法
			return getter.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 自动类型转换
	 * 
	 * @param value
	 *            原始值
	 * @param clazz
	 *            转换为什么类型的
	 * @param defaultValue
	 *            如果转换失败了返回一个缺省值
	 * @return
	 * @throws UnSupportedClassType
	 * @author Ivan<br>
	 * @date 2015年2月25日 下午5:13:24 <br>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T autoConv(Class<T> clz, String key, String value) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(key, clz);
			Method setter = pd.getWriteMethod();// 获得set方法
			// 获取set方法的参数列表
			Class<?>[] parameterTypes = setter.getParameterTypes();
			Type typeClz = clz.getDeclaredField(key).getGenericType();
			if (parameterTypes == null || parameterTypes.length != 1) {// 只处理带一个参数的
				return null;
			}
			Class<?> clazz = parameterTypes[0];// 获取参数类型
			if (clazz == String.class) {// String
				return (T) value;
			}
			if (clazz == int.class || clazz == Integer.class) {// int|Integer
				try {
					Integer x = new Integer(value);
					return (T) x;
				} catch (Exception e) {
				}
				return (T) new Integer(ServerCfg.INVALID_NUMBER);
			}
			if (clazz == long.class || clazz == Long.class) {// long|Long
				try {
					Long x = new Long(value);
					return (T) x;
				} catch (Exception e) {
				}
				return (T) new Long(ServerCfg.INVALID_NUMBER);

			}
			if (clazz == float.class || clazz == Float.class) {// float|Float
				try {
					Float x = new Float(value);
					return (T) x;
				} catch (Exception e) {
				}
				return (T) new Float(ServerCfg.INVALID_NUMBER);
			}
			if (clazz == double.class || clazz == Double.class) {// double|Double
				try {
					Double x = new Double(value);
					return (T) x;
				} catch (Exception e) {
				}
				return (T) new Double(ServerCfg.INVALID_NUMBER);
			}
			if (clazz == boolean.class || clazz == Boolean.class) {// boolean|Boolean
				try {
					Boolean x = new Boolean(value);
					return (T) x;
				} catch (Exception e) {
				}
				return (T) new Boolean("false");
			}
			if (clazz == List.class) {// List<?>,这里有点复杂，因为还有关于泛型的处理，目前只支持Integer|String
				ParameterizedType pt = (ParameterizedType) clz.getDeclaredField(key).getGenericType();
				Type type = pt.getActualTypeArguments()[0];
				Class<?> typeClass = (Class<?>) type;
				if (typeClass == Integer.class) {
					List<Integer> list = new ArrayList<Integer>();
					String[] strArr = value.split(Separator.DEFAULT);
					for (String str : strArr) {
						list.add(Integer.parseInt(str));
					}
					return (T) list;
				} else if (typeClass == String.class) {
					List<String> list = new ArrayList<String>();
					String[] strArr = value.split(Separator.DEFAULT);
					for (String str : strArr) {
						list.add(str);
					}
					return (T) list;
				} else {
					//支持了 List<T>类型
					List<T> list = (List<T>) CachedObjects.GSON.fromJson(value, pt);
					return (T)list;
				}
			}
			// 支持传对象到后台
			if (clazz == typeClz) {
				Object object = CachedObjects.GSON.fromJson(value, typeClz);
				return (T)object;
			}
			throw new RuntimeException("AutoConv failed,UnSupportedClassType:" + clazz.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
