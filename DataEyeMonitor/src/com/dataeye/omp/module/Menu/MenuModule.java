package com.dataeye.omp.module.Menu;

import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.module.cmdb.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 菜单接口
 * @auther wendywang
 * @since 2016/1/11 11:41
 */
@Controller
public class MenuModule {

    @Autowired
    private MenuDao menuDao;

    @RequestMapping("/getOmpMenu.do")
    public Object getMenu(HttpServletRequest req,
           HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.ITEMID);
        HttpSession session = req.getSession();
        Employee employee = (Employee) session.getAttribute(Constant.SessionName.CURRENT_USER);
        Map<String, Object> menuMaps = new HashMap<>();
        if (employee == null) {
//            rsp.sendRedirect("/pages/login.jsp");
        } else {
            menuMaps = menuDao.getMenus(param.getItemId(), employee.getId());
        }
        return menuMaps;

    }

}
