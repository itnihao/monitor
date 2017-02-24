package com.dataeye.omp.module.jvm;

import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.module.monitor.server.Machine;
import com.qq.jutil.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by wendy on 2016/6/6.
 */
@Service
public class JvmMonitorService {

    @Autowired
    private JvmMonitorDao jvmMonitorDao;

    public PageData getJvmMonitoService(int pageID, int pageSize, int order,
                                        String orderBy, String searchKey) {
        List<JvmMonitorServer> servers = jvmMonitorDao.getServerList();

        if (StringUtil.isNotEmpty(searchKey)) {
            filterServerList(servers, searchKey);
        }

        PageData pageData = new PageData(pageSize, pageID);
        pageData.setTotalRecord(servers.size());

        Comparator<JvmMonitorServer> comparator = JvmMonitorServer.HOST_NAME_ASC;
        if (Constant.OrderBy.ORDERBY_HOSTNAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = JvmMonitorServer.HOST_NAME_ASC;
            } else {
                comparator = JvmMonitorServer.HOST_NAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_IP.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = JvmMonitorServer.IP_ASC;
            } else {
                comparator = JvmMonitorServer.IP_DESC;
            }
        }

        Collections.sort(servers, comparator);

        int startIndex = (pageData.getPageID() - 1) * pageData.getPageSize();
        int endIndex = pageData.getPageID() * pageData.getPageSize() - 1;
        // startIndex就超限了
        if (startIndex >= servers.size()) {
            return pageData;
        }
        endIndex = endIndex > (servers.size() - 1) ? (servers.size() - 1) : endIndex;
        List<JvmMonitorServer> resultList = servers.subList(startIndex, endIndex + 1);
        pageData.setContent(resultList);
        return pageData;

    }

    private void filterServerList(List<JvmMonitorServer> servers, String searchKey) {
        List<JvmMonitorServer> list = new ArrayList<>();
        for (JvmMonitorServer server : servers) {
            if (server.getHostName().contains(searchKey) ||
                    server.getIp().contains(searchKey)) {
                list.add(server);
            }
        }
        servers.clear();
        servers.addAll(list);
    }

}
