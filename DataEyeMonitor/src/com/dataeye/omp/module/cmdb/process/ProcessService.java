package com.dataeye.omp.module.cmdb.process;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.utils.HbaseProxyClient;
import com.qq.jutil.string.StringUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author wendy
 * @since 2016/3/5 17:29
 */
@Service
public class ProcessService {
    @Autowired
    private ProcessDao processDao;

    public PageData queryModuleList(String searchKey, int pageID,
                            int pageSize, int order, String orderBy)
            throws ServerException {
        List<Module> modules = processDao.queryModuleList();
        if (StringUtil.isNotEmpty(searchKey)) {
            filterModuleData(modules, searchKey);
        }

        if (StringUtil.isNotEmpty(orderBy)) {
            sortModuleData(modules, order, orderBy);
            System.out.println(modules.size());
        }

        PageData pageData = new PageData(pageSize,pageID);
        pageData.setTotalRecord(modules.size());
        assemblePageData(modules, pageData);
        return pageData;
    }

    public void filterModuleData(List<Module> modules, String searchKey) {
        List<Module> moduleList = new ArrayList<>();
        for (Module module : modules) {
            String busiName = module.getBusiName();
            String moduleName = module.getModuleName();

            if (StringUtil.isNotEmpty(busiName) && StringUtil.isNotEmpty(moduleName)) {
                if (busiName.toLowerCase().contains(searchKey.toLowerCase()) ||
                        moduleName.toLowerCase().contains(searchKey.toLowerCase())) {
                    moduleList.add(module);
                    continue;
                }
            }

            if (StringUtil.isNotEmpty(busiName) && StringUtil.isEmpty(moduleName)) {
                if (busiName.toLowerCase().contains(searchKey.toLowerCase())) {
                    moduleList.add(module);
                    continue;
                }
            }

            if (StringUtil.isEmpty(busiName) && StringUtil.isNotEmpty(moduleName)) {
                if (moduleName.toLowerCase().contains(searchKey.toLowerCase())) {
                    moduleList.add(module);
                }
            }
        }
        modules.clear();
        modules.addAll(moduleList);
    }


    public void sortModuleData(List<Module> modules, int order, String orderBy) {
        Comparator<Module> comparator = ModuleComparator.BUSINAME_ASC;
        if (Constant.OrderBy.ORDERBY_BUSINAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ModuleComparator.BUSINAME_ASC;
            } else {
                comparator = ModuleComparator.BUSINAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_MODULENAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ModuleComparator.MODULENAME_ASC;
            } else {
                comparator = ModuleComparator.MODULENAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_PROCESSNUM.equals(orderBy)) {
            System.out.println("moduleList" + modules.size() +
                    "orderBy"+ orderBy + "order" + order);
            if (Constant.Order.ASC == order) {
                comparator = ModuleComparator.PROCESSNUM_ASC;
            } else {
                comparator = ModuleComparator.PROCESSNUM_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_MAINPRINCIPAL.equals(orderBy)) {

            if (Constant.Order.ASC == order) {
                comparator = ModuleComparator.MAINPRINCIPAL_ASC;
            } else {
                comparator = ModuleComparator.MAINPRINCIPAL_DESC;
            }
        }

        Collections.sort(modules, comparator);
   }


    public void assemblePageData(List<?> list, PageData pageData) {
        // 获取分页数据
        int startIndex = (pageData.getPageID() - 1) * pageData.getPageSize();
        int endIndex = pageData.getPageID() * pageData.getPageSize() - 1;

        // startIndex就超限了
        if (startIndex >= list.size()) pageData.setContent(new ArrayList());

        endIndex = endIndex > (list.size() - 1) ? (list.size() - 1) : endIndex;
        List<?> resultList = list.subList(startIndex, endIndex + 1);
        pageData.setContent(resultList);
    }


    /**
     * 查询进程信息列表
     * @param searchKey 搜索关键字
     * @param pageID   当前页
     * @param pageSize  每页大小
     * @param order     升序，降序
     * @param orderBy   排序字段
     * @return  PageData
     * @throws ServerException
     */
    public PageData  queryProcessList(String searchKey, int pageID,
                                     int pageSize, int order, String orderBy)
            throws ServerException, IOException {
        List<ProcessInfo> processList = processDao.queryProcessInfo();

        if (StringUtil.isNotEmpty(searchKey)) {
            filterProcessBySearchKey(processList, searchKey);
        }

        setProcessStatus(processList);

        if (StringUtil.isNotEmpty(orderBy)) {
            sortProcess(processList, order, orderBy);
        }

        PageData pageData = new PageData(pageSize, pageID);
        pageData.setTotalRecord(processList.size());
        assemblePageData(processList, pageData);
        return pageData;

    }

    private void sortProcess(List<ProcessInfo> processList, int order, String orderBy) {
        Comparator<ProcessInfo> comparator = ProcessComparator.BUSINAME_DESC;
        if (Constant.OrderBy.ORDERBY_BUSINAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.BUSINAME_ASC;
            } else {
                comparator = ProcessComparator.BUSINAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_MODULENAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.MODULE_ASC;
            } else {
                comparator = ProcessComparator.MODULE_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_PROCESSNAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.PROCESSNAME_ASC;
            } else {
                comparator = ProcessComparator.PROCESSNAME_DESC;
            }
        }


        if (Constant.OrderBy.ORDERBY_HOSTNAME.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.PROCESSHOSTNAME_ASC;
            } else {
                comparator = ProcessComparator.PROCESSHOSTNAME_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_PORT.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.PROCESSPORT_ASC;
            } else {
                comparator = ProcessComparator.PROCESSSPORT_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_PRIVATEIP.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.PROCESSSPRIVATEIP_ASC;
            } else {
                comparator = ProcessComparator.PROCESSPRIVATEIP_DESC;
            }
        }

        if (Constant.OrderBy.ORDERBY_PROCESSSTATUS.equals(orderBy)) {
            if (Constant.Order.ASC == order) {
                comparator = ProcessComparator.PROCESSSTATUS_ASC;
            } else {
                comparator = ProcessComparator.PROCESSSTATUS_DESC;
            }
        }

        Collections.sort(processList, comparator);
    }

    private void filterProcessBySearchKey(List<ProcessInfo> processList, String searchKey) {
        List<ProcessInfo> list = new ArrayList<>();
        for (ProcessInfo process : processList) {
            String busiName = process.getBusiName();
            String moduleName = process.getModuleName();
            String processName = process.getProcessName();
            String serverName = process.getServerName();
            String ip = process.getPrivateIp();

//            if (StringUtil.isNotEmpty(busiName)) {
//                if (busiName.toLowerCase().indexOf(searchKey.toLowerCase()) >= 0) {
//                    list.add(process);
//                    continue;
//                } else {
//
//                }
//            } else {
//                if (StringUtil.isNotEmpty(moduleName)) {
//                    if (moduleName.toLowerCase().indexOf(searchKey.toLowerCase()) >= 0) {
//                        list.add(process);
//                        continue;
//                    }
//                }else {
//                    if (StringUtil.isNotEmpty(processName)) {
//                        if (processName.toLowerCase().indexOf(searchKey.toLowerCase()) >=0) {
//                            list.add(process);
//                            continue;
//                        }
//                    } else {
//                        if (StringUtil.isNotEmpty(serverName)) {
//                            if (serverName.toLowerCase().indexOf(searchKey.toLowerCase()) >= 0) {
//                                list.add(process);
//                                continue;
//                            }
//                        } else {
//                            if (StringUtil.isNotEmpty(ip)) {
//                                if (serverName.toLowerCase().indexOf(searchKey.toLowerCase()) >= 0) {
//                                    list.add(process);
//                                    continue;
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }



            if (StringUtil.isNotEmpty(busiName) && StringUtil.isNotEmpty(moduleName)
                    && StringUtil.isNotEmpty(processName) && StringUtil.isNotEmpty(serverName)
                    && StringUtil.isNotEmpty(ip)) {
                if (busiName.toLowerCase().contains(searchKey.toLowerCase())
                        || moduleName.toLowerCase().contains(searchKey.toLowerCase())
                        || processName.toLowerCase().contains(searchKey.toLowerCase())
                        || serverName.toLowerCase().contains(searchKey.toLowerCase())
                        || ip.toLowerCase().contains(searchKey.toLowerCase()) ) {
                    list.add(process);
                    continue;
                }
            }

            if (StringUtil.isNotEmpty(busiName) && StringUtil.isNotEmpty(moduleName)
                    && StringUtil.isEmpty(processName)&&StringUtil.isNotEmpty(serverName)
                    &&StringUtil.isNotEmpty(ip)) {
                if (busiName.toLowerCase().contains(searchKey.toLowerCase())
                        || moduleName.toLowerCase().contains(searchKey.toLowerCase())
                        || serverName.toLowerCase().contains(searchKey.toLowerCase())
                        || ip.toLowerCase().contains(searchKey.toLowerCase())) {
                    list.add(process);
                    continue;
                }
            }

            if (StringUtil.isNotEmpty(busiName) && StringUtil.isEmpty(moduleName)
                    && StringUtil.isNotEmpty(processName)&&StringUtil.isNotEmpty(serverName)
                    &&StringUtil.isNotEmpty(ip)) {
                if (busiName.toLowerCase().contains(searchKey.toLowerCase())
                        || processName.toLowerCase().contains(searchKey.toLowerCase())
                        || serverName.toLowerCase().contains(searchKey.toLowerCase())
                        || ip.toLowerCase().contains(searchKey.toLowerCase()) ) {
                    list.add(process);
                    continue;
                }
            }

            if (StringUtil.isEmpty(busiName) && StringUtil.isNotEmpty(moduleName)
                    && StringUtil.isNotEmpty(processName)&&StringUtil.isNotEmpty(serverName)
                    &&StringUtil.isNotEmpty(ip)) {
                if (moduleName.toLowerCase().contains(searchKey.toLowerCase())
                        || processName.toLowerCase().contains(searchKey.toLowerCase())
                        || serverName.toLowerCase().contains(searchKey.toLowerCase())
                        || ip.toLowerCase().contains(searchKey.toLowerCase())) {
                    list.add(process);
                    continue;
                }
            }


            if (StringUtil.isNotEmpty(busiName) && StringUtil.isNotEmpty(moduleName)
                    && StringUtil.isNotEmpty(processName)&&StringUtil.isEmpty(serverName)
                    &&StringUtil.isNotEmpty(ip)) {
                if (moduleName.toLowerCase().contains(searchKey.toLowerCase())
                        || processName.toLowerCase().contains(searchKey.toLowerCase())
                        || busiName.toLowerCase().contains(searchKey.toLowerCase())
                        || ip.toLowerCase().contains(searchKey.toLowerCase())) {
                    list.add(process);
                    continue;
                }
            }

            if (StringUtil.isNotEmpty(busiName) && StringUtil.isNotEmpty(moduleName)
                    && StringUtil.isNotEmpty(processName)&&StringUtil.isNotEmpty(serverName)
                    &&StringUtil.isEmpty(ip)) {
                if (moduleName.toLowerCase().contains(searchKey.toLowerCase())
                        || processName.toLowerCase().contains(searchKey.toLowerCase())
                        || busiName.toLowerCase().contains(searchKey.toLowerCase())
                        || serverName.toLowerCase().contains(searchKey.toLowerCase())) {
                    list.add(process);
                }
            }
        }
        processList.clear();
        processList.addAll(list);
    }



    /**
     * 查询进程监控状态
     * @param processList  进程列表
     */
    public void setProcessStatus(List<ProcessInfo> processList) throws IOException {
        long start = System.currentTimeMillis();
        for (ProcessInfo process : processList) {
            String rowKey = String.valueOf(process.getServeID());
            Result result = HbaseProxyClient.getOneRecord(
                    Constant.Table.OMP_PROCESS_CURRENT_STATE, rowKey);

            NavigableMap<byte[], byte[]> dataMap =
                    result.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));

            int port = process.getPort();
            String processName = process.getProcessName();

            String exQulifier = port + ServerCfg.ROWKEY_SPLITER +
                    processName + ServerCfg.ROWKEY_SPLITER;

            String portStatus = exQulifier + Constant.Process.PORT;
            String processStatus = exQulifier + Constant.Process.PROCESS;


            if (dataMap != null) {
                byte[] b = dataMap.get(Bytes.toBytes(portStatus));
                byte[] b1 = dataMap.get(Bytes.toBytes(processStatus));

                //TODO 没有开端口的进程
                if (b != null && b1 != null) {
                    Integer value = Integer.parseInt(Bytes.toString(b).split("_")[0]);
                    Integer value1 = Integer.parseInt(Bytes.toString(b1).split("_")[0]);
                    process.setProcessStatus(value | value1);
                } else {
                    process.setProcessStatus(Constant.Process.NOT_MONITOR);
                }
            } else {
                process.setProcessStatus(Constant.Process.NOT_MONITOR);
            }


        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void main(String[] args) {
        String s = "omp_report_server";
        String s1 = "server";
        System.out.println(s.contains(s1));

    }



}
