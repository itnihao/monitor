package com.dataeye.omp.module.cmdb.summary;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.business.Business;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.device.DeviceDao;
import com.dataeye.utils.ServerUtils;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther wendy
 * @since 2015/12/29 15:48
 */
@Service
public class SummaryDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BusinessDao businessDao;

    /**
     * 查询设备概览数据
     * @return
     */
    public List<Summary> getDeviceSummaryTopData()
            throws ServerException {
        List<Summary> list = new ArrayList<>();
        try{
            //总设备数
            int totalDevice = deviceDao.getTotalDevcieRecord();
            Summary summary = new Summary(1, "总设备", totalDevice);
            list.add(summary);

            //已用
            String sql = "select count(1) from server_list where id in "
                    + "(select svr_id from server_business)";
            int usedDevice = jdbcTemplate.queryForInt(sql);
            summary = new Summary(2, "已用", usedDevice);
            list.add(summary);

            //未用
            int unUsedDevice = totalDevice - usedDevice;
            summary = new Summary(3, "未用", unUsedDevice);
            list.add(summary);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return list;
    }

    /**
     * 查询计算每个业务所占设备数
     * @return
     * @throws ServerException
     */
    public List<Summary> getBusiSummaryData()
            throws ServerException {
        List<Summary> summaryList = new ArrayList<>();
        try{
            int deviceRecord = deviceDao.getTotalDevcieRecord();
            List<Business> businessList = businessDao.getBusinessList();
            if (deviceRecord > 0) {
                for (Business business : businessList) {
                    Summary summary = new Summary();
                    summary.setId(business.getId());
                    summary.setName(business.getName());

                    //获取每个业务设备数
                    String sql = "select count(1) from (select distinct svr_id,"
                            + "busi_id from server_business where busi_id=?) as a;";
                    int count = jdbcTemplate.queryForInt(sql, business.getId());
                    summary.setValue(count);
                    double value = new Double(count) / new Double(deviceRecord);

                    summary.setPercentage(ServerUtils.precent(value * 100));
                    summaryList.add(summary);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常"
        );

    }
        return summaryList;
    }

}
