package com.dataeye.monitor.task;

import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * test report data
 * Created by wendy on 2016/7/12.
 */
@Service
public class TestReportData extends BaseCmd {

    private Logger logger = LoggerFactory.getLogger("test_log");

    @CmdMapper("/test/test")
    public Object testReportData(XLHttpRequest req, XLHttpResponse rsp) {
        logger.info(req.getContentString());
        return null;
    }

}
