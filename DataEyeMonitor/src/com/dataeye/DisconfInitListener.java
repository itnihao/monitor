package com.dataeye;

import com.dataeye.deconf.client.DeconfMgr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by wendy on 2016/5/10.
 */
public class DisconfInitListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DeconfMgr.getInstance().start();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DeconfMgr.getInstance().close();
    }
}
