package com.dataeye.common;

import java.lang.annotation.*;

/**
 * 
 * <pre>
 * 接口访问控制
 * @author Ivan          <br>
 * @date 2015年4月8日 下午8:14:41 <br>
 * @version 1.0
 * <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface PrivilegeControl {
	/**
	 * 
	 * <pre>
	 * 接口访问权限
	 * @author Ivan          <br>
	 * @date 2015年4月8日 下午8:15:04 <br>
	 * @version 1.0
	 * <br>
	 */
		public enum Scope {
		/**
		 * 不需要权限控制的接口,例如登录接口
		 */
		Public,
		/**
		 * 必须要有登录态才允许访问,例如获取渠道区服版本列表
		 */
		AfterLogin,
		/**
		 * 必须要经过授权以后才能访问，例如获取新增玩家的分布数据
		 */
		AfterAuth,
		/**
		 * 需要IP鉴权
		 */
		FIXIP;

	}

	/**
	 * 
	 * <pre>
	 * 获取当前接口的访问权限,默认为Public
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午8:15:24
	 * <br>
	 */
	public Scope scope() default Scope.Public;

	/**
	 * 
	 * <pre>
	 * 设置当前接口属于哪些权限
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午8:19:37
	 * <br>
	 */
	public String[] priv() default "";

	/**
	 * 
	 * <pre>
	 * 设置当前接口是否是写操作的接口
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午8:20:06
	 * <br>
	 */
	public boolean write() default false;

}
