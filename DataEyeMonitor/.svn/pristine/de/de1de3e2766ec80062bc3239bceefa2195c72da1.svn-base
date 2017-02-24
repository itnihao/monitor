package com.dataeye.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dataeye.omp.constant.Constant.OsType;
import com.dataeye.omp.constant.Constant.ServerCfg;

/**
 * @author Ivan <br>
 * @date 2015年4月3日 下午4:29:08 <br>
 */
public class FileUtils {
    /**
     * <pre>
     * 这个方法主要是用来读取放在classpath下面的文件 获取这些文件的绝对路径 比如语言文件放在classpath下面的i18n目录,
     * 传递参数i18n即可得到i18n目录的绝对路径
     *
     * @param resource
     * @author Ivan<br>
     * @date 2015年4月3日 下午3:49:40 <br>
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

    /**
     * <pre>
     * 下载Excel文件
     *
     * @param response
     * @param filePath
     * @param fileNameShow
     * @throws Exception
     * @author Ivan<br>
     * @date 2015年1月4日 下午1:25:33 <br>
     */
    public static void downloadExcel(HttpServletResponse response, File file) throws Exception {
        if (file == null) {
            return;
        }
        response.setContentType(ServerCfg.CONTENT_TYPE_EXCEL);
        if (file.length() < Integer.MAX_VALUE) {
            response.setContentLength((int) file.length());
        }
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(file.getName(), ServerCfg.ENCODING_UTF8));
        ServletOutputStream servletOutputStream = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            servletOutputStream = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(servletOutputStream);
            byte[] buff = new byte[1024];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <pre>
     * 这个方法用来删除指定目录的文件
     *
     * @param filePath
     * @author Stran<br>
     * @date 2015年11月13日 下午2:29:40 <br>
     */
    public static void removeFile(String filePath) {
        if (filePath == null) {
            System.out.println("filePath参数为null");
            return;
        }

        File file = new File(filePath);
        try {
            // 文件或目录不存在退出
            if (!file.exists()) {
                return;
            }
            // 如果是文件,进行删除并退出
            if (file.isFile()) {
                file.delete();
                return;
            }

            // 如果目录中有文件递归删除文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                removeFile(files[i].getAbsolutePath());
            }

            // 删除目录
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
