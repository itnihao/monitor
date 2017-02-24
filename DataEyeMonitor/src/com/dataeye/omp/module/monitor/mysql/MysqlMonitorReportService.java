package com.dataeye.omp.module.monitor.mysql;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MysqlMonitorReportService {

    @Autowired
    private MysqlMonitorReportDao dao;


    public DEResponse switchToDaoMethods(int serverId, int port, String startDate,
                                         String endDate, int flag) throws ServerException {

        if (startDate.equals(endDate)) {
            return dao.getMysqlFeatureByMinute(serverId, port, startDate, flag);
        } else {
            return dao.getMysqlFeatureByHour(serverId, port, startDate, endDate, flag);
        }
    }
}
