package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.FourMinuteTask;
import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.utils.ClassReflection;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * agent脚本定时任务管理类，总的启动入口
 * Created by wendy on 2016/7/4.
 */
public class TaskManager {
    private static final int idleThreadNum = 5;
    private static final String scanPackage = "com.dataeye.monitor.task";

    private TaskManager(){}
    private static TaskManager taskManager = new TaskManager();

    private static Set<Class<?>> oneMinuteTasks=null;
    private static Set<Class<?>> fourMinuteTasks = null;

    private ScheduledExecutorService excutor = null;

    public static TaskManager getInstance() {
        return taskManager;
    }

    private int threadNum;

    private void init() {
        oneMinuteTasks = ClassReflection.getClassFilterByAnnotation(
                OneMinuteTask.class, scanPackage);
        fourMinuteTasks = ClassReflection.getClassFilterByAnnotation(
                FourMinuteTask.class, scanPackage);
        threadNum = oneMinuteTasks.size() + fourMinuteTasks.size() + idleThreadNum;
        excutor = Executors.newScheduledThreadPool(threadNum);
    }

    public void start() {
        try {
            init();
            executeOneMinuteTask();
            executeFourMinuteTask();
        } catch (Exception e) {
            ExceptionHandler.error("task manager error", e);
        }
    }

    private void executeFourMinuteTask()
            throws InstantiationException, IllegalAccessException {
        for (Class c : fourMinuteTasks) {
            Runnable runnable = null;
            runnable = (Runnable) c.newInstance();
            excutor.scheduleAtFixedRate(runnable, 0, 4, TimeUnit.MINUTES);
        }
    }

    private void executeOneMinuteTask()
            throws InstantiationException, IllegalAccessException {
        for (Class c : oneMinuteTasks) {
            Runnable runnable = null;
            runnable = (Runnable) c.newInstance();
            excutor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MINUTES);
        }
    }

}
