package com.dataeye.omp.module.alarm;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.omp.module.cmdb.device.DicValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 特性接口
 * @auther wendy
 * @since 2016/1/27 9:53
 */
@Controller
public class FeatureModule {

    @Autowired
    private FeatureDao featureDao;

    /**
     * 获取特性列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getFeatureSelectData.do")
    public Object getFeatureSelectData(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        List<Feature> featureList = featureDao.getFeatureList();
        List<DicValue> dicValues = new ArrayList<>();
        for (Feature feature : featureList) {
            DicValue dicValue = new DicValue();
            dicValue.setId(feature.getId());
            dicValue.setLabel(feature.getName());
            dicValue.setValue(feature.getId());
            dicValues.add(dicValue);
        }
        return dicValues;
    }

    /**
     * 获取特性对应的对象
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getFeatureObject.do")
    public Object getObjectSelectData(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        return featureDao.getfeatureObjectList();
    }

}
