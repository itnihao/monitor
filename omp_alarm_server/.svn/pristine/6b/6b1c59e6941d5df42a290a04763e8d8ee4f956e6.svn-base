package com.dataeye.omp.utils;

import com.dataeye.omp.common.Constant.OsType;

/**
 * 文件操作工具类
 * @author chenfanglin
 * @date 2016年2月29日 下午6:21:18
 */
public class FileUtils {
   
	/**
	 * 这个方法主要是用来读取放在classpath下面的文件 获取这些文件的绝对路径 比如语言文件放在classpath下面的i18n目录,
     * 传递参数i18n即可得到i18n目录的绝对路径
	 * @param resource
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:21:13
	 */
    public static String getPathOfClasspathResource(String resource) {
        String path = ServerUtils.class.getResource("/").getPath();
        // 这里要根据操作系统判断
        // windows系统得到的路径形如/E:/Project/DataEyeWebPlus/WebRoot/WEB-INF/classes/
        // 要去掉前面的/
        if (ServerUtils.getOsType().equals(OsType.WINDOWS)) {
            path = path.replaceFirst("/", "");
        }
        return path + resource;
    }

}
