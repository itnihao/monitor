package com.dataeye.monitor;


import com.dataeye.monitor.task.TaskManager;
import com.xunlei.netty.httpserver.Bootstrap;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Launcher {

    public static void init(){
        System.out.println("start ..............");
        TaskManager.getInstance().start();
    }

    public static void main(String[] args) throws IOException {
        Bootstrap.main(args, new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, "classpath:applicationContext.xml");
    }
}
