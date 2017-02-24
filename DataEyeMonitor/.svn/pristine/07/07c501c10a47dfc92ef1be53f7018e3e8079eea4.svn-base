package com.dataeye.omp.module.cmdb.device;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataeye.common.PrivilegeControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;

/**
 * @author wendy
 * @since 2015/12/15 11:25
 */
@Controller
public class DeviceModule {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceDao deviceDao;

    /**
     * 添加设备
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/addDevice.do")
    public Object addDevice(HttpServletRequest req,
          HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.DEVID, Keys.DEV_TYPE, Keys.IDC_ID,
                Keys.HOST_NAME, Keys.NETCARD_NUM, Keys.CPU_TYPE,
                Keys.CPU_NUM, Keys.CPU_PHYSICAL_CORES, Keys.CPU_LOGIC_CORES,
                Keys.OS, Keys.KERNEL, Keys.MEMORY, Keys.DISKSIZE,
                Keys.DISKDETAILS, Keys.DEPTID,Keys.ADMIN, Keys.BACKUPADMIN, Keys.DESCS);

        Device device = new Device();
        device.convertParam2Device(param);
        deviceService.addDevice(device);
        return null;
    }

    /**
     * 检查IP是否被占用
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/checkIpUsed.do")
    public Object checkIpUsed(HttpServletRequest req,
                            HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.IP);
       // return deviceDao.checkIpExists(param.getIp());
        return null;
    }

    /**
     * 修改设备信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/updateDevice.do")
    public Object editDevice(HttpServletRequest req,
             HttpServletResponse rsp)
            throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(DEParameter.Keys.DEVID, Keys.DEV_TYPE, Keys.IDC_ID,
                Keys.HOST_NAME, Keys.NETCARD_NUM, Keys.CPU_TYPE,
                Keys.CPU_NUM, Keys.CPU_PHYSICAL_CORES, Keys.CPU_LOGIC_CORES,
                Keys.OS, Keys.KERNEL, Keys.MEMORY, Keys.DISKSIZE,
                Keys.DISKDETAILS, Keys.DEPTID,Keys.ADMIN, Keys.BACKUPADMIN, Keys.DESCS);

        Device device = new Device();
        device.convertParam2Device(param);
        deviceService.updateDevice(device);
        return null;
    }

    /**
     * 获取设备列表
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getDeviceList.do")
    public Object getDeviceList(HttpServletRequest req,
            HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        return deviceService.getDeviceList(
            param.getSearchType(),param.getSearchKey(),
            param.getPageID(), param.getPageSize());
    }

    /**
     * 根据Id删除设备信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/deleteDeviceById.do")
    public Object deleteDevice(HttpServletRequest req,
           HttpServletResponse rsp)throws Exception{
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
         deviceService.deleteDevice(param.getDeviceId());
        return null;
    }

    /**
     * 获取添加设备下拉选择数据接口
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getDeviceInitData.do")
    public Object getDeviceInitData(HttpServletRequest req,
           HttpServletResponse rsp)throws Exception{
        return deviceService.getDeviceInitData();
    }

    /**
     * 获取所有设备供添加机器下拉删选
     * @return
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getSelectDeviceList.do")
    public Object getSelectDeviceList(HttpServletRequest req,
                 HttpServletResponse rsp) throws Exception {
        return deviceService.getSelectDeviceList();
    }
}
