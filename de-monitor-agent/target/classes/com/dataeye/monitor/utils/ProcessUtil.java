package com.dataeye.monitor.utils;
import com.dataeye.monitor.task.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {
    private static Logger logger = LoggerFactory.getLogger("agent_log");
    public static Process process(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static String execute(String command) {
        long start = System.currentTimeMillis();
        Process process = null;
        BufferedReader inputReader = null;
        BufferedReader errorReader = null;
        StringBuffer resut = new StringBuffer();
        String[] cmds = new String[]{"/bin/sh", "-c", command};
        try {
            process = Runtime.getRuntime().exec(cmds);
            inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = inputReader.readLine()) != null) {
                resut.append(line).append("\n");
            }
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuffer error = new StringBuffer();
            while ((line = errorReader.readLine()) != null) {
                error.append(line);
            }

            int exitValue = process.waitFor();

            if (exitValue == 1) {
                ExceptionHandler.error("command: [" + command + "] execute error , error exits value: 1"
                        , error.toString());
                logger.error("command：" + command + "error:" + error.toString());
            }
            process.destroy();
        } catch (Exception e) {
            ExceptionHandler.error("command [" + command + "] not exits:", e);
            logger.error("command not exits: " + e.toString());
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }

                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();

        if (end - start > 60 * 1000) {
            ExceptionHandler.error("command: [" + command + "] execute time over one minute", "");
        }
        return resut.toString();
    }


    public static void main(String[] args)  {
        try {
            Process process = null;
            process = Runtime.getRuntime().exec("ll");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuffer resut = new StringBuffer();
            while ((line = inputReader.readLine()) != null) {
                resut.append(line).append("\n");
            }
            int exitValue = process.waitFor();
            System.out.println("==============" + exitValue);
            System.out.println(resut.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
