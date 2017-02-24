package com.dataeye.omp.module.Menu;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.module.cmdb.business.Business;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.device.DeviceDao;
import com.dataeye.omp.module.cmdb.device.Idc;
import com.dataeye.omp.module.cmdb.employee.Employee;
import com.dataeye.omp.module.monitor.server.Group;
import com.dataeye.omp.module.monitor.server.GroupDAO;
import com.xunlei.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther wendywang
 * @since 2016/1/11 11:41
 */
@Service
public class MenuDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private GroupDAO groupDao;

    public Map<String, Object> getMenus(String itemid,int uid) throws ServerException {
        Map<String, Object> menuMap = new HashMap<>();
        if ("server".equals(itemid)) {
            menuMap.put("title", "server");
            menuMap.put("name", "服务器监控");
            List<Menu> menus = new ArrayList<>();
            menuMap.put("items", menus);
            List<Idc> idcList = deviceDao.getAllIdcList();
            List<Business> busiList = businessDao.getBusinessList();
            List<Group> groupList = groupDao.getAllGroupList();
            setIdcMenu(menus,idcList);
            setBusinessMenu(menus, busiList);
            setGroupMenu(menus, groupList, uid);
            return menuMap;
        }
        return null;
    }

    /**
     * 机房视图菜单
     * @param menus
     * @param idcList
     */
    private void setIdcMenu(List<Menu> menus, List<Idc> idcList) {
        Menu menu = new Menu("机房视图","roomView");
        List<Menu> subMenu = new ArrayList<>();
        Menu all = new Menu(ServerCfg.ALL,"全部", "roomView");
        subMenu.add(all);
        for (Idc idc : idcList) {
            Menu m = new Menu(idc.getId(), idc.getName(), "roomView");
            subMenu.add(m);
        }
        menu.setSubs(subMenu);
        menus.add(menu);
    }

    /**
     * 业务视图菜单
     * @param menus
     * @param businessList
     */
    private void setBusinessMenu(List<Menu> menus, List<Business> businessList) {
        Menu menu = new Menu("业务视图","businessView");
        List<Menu> subMenu = new ArrayList<>();
        for (Business busi : businessList) {
            Menu m = new Menu(busi.getId(), busi.getName(), "businessView");
            subMenu.add(m);
        }
        menu.setSubs(subMenu);
        menus.add(menu);
    }

    /**
     * 分组视图菜单
     *
     * @param menus
     * @param groupList
     */
    private void setGroupMenu(List<Menu> menus, List<Group> groupList, int uid) {
        Menu menu = new Menu("分组视图", "groupingView");
        List<Menu> subMenu = new ArrayList<>();
        Menu groupMenu = new Menu("分组管理", "groupingManage");
        subMenu.add(groupMenu);

        for (Group group : groupList) {
            if (group.getUid() == uid) {
                Menu m = new Menu(group.getGroupID(),
                        group.getGroupName(), "groupingView");
                subMenu.add(m);
            }

        }
        menu.setSubs(subMenu);
        menus.add(menu);
    }

}
