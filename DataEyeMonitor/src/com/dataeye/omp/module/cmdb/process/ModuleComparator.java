package com.dataeye.omp.module.cmdb.process;

import com.qq.jutil.string.StringUtil;

import java.util.Comparator;

/**
 * 比较器
 * @author wendywang
 * @since 2016/3/5 20:45
 */
public class ModuleComparator {

    /**
     * 按业务名称降序
     */
    public static final Comparator<Module> BUSINAME_DESC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (StringUtil.isNotEmpty(o1.getBusiName()) &&
                    StringUtil.isNotEmpty(o2.getBusiName())) {
                if (o1.getBusiName().compareTo(o2.getBusiName()) > 0) {
                    return -1;
                }
                if (o1.getBusiName().compareTo(o2.getBusiName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getBusiName())&&
                    StringUtil.isEmpty(o2.getBusiName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getBusiName())&&
                    StringUtil.isNotEmpty(o2.getBusiName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };


    /**
     * 按业务名称升序
     */
    public static final Comparator<Module> BUSINAME_ASC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (StringUtil.isNotEmpty(o1.getBusiName()) &&
                    StringUtil.isNotEmpty(o2.getBusiName())) {
                if (o1.getBusiName().compareTo(o2.getBusiName()) > 0) {
                    return 1;
                }
                if (o1.getBusiName().compareTo(o2.getBusiName()) < 0) {
                    return -1;
                }
            }else if(StringUtil.isNotEmpty(o1.getBusiName())&&
                    StringUtil.isEmpty(o2.getBusiName())){
                return 1;
            }else if(StringUtil.isEmpty(o1.getBusiName())&&
                    StringUtil.isNotEmpty(o2.getBusiName())){
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };

    /**
     * 按模块名称 降序
     */
    public static final Comparator<Module> MODULENAME_DESC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (StringUtil.isNotEmpty(o1.getModuleName()) &&
                    StringUtil.isNotEmpty(o2.getModuleName())) {
                if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) {
                    return -1;
                }
                if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getModuleName())&&
                    StringUtil.isEmpty(o2.getModuleName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getModuleName())&&
                    StringUtil.isNotEmpty(o2.getModuleName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };


    /**
     * 按模块名称升序
     */
    public static final Comparator<Module> MODULENAME_ASC = new Comparator<Module>() {
        @Override
            public int compare(Module o1, Module o2) {
                if (StringUtil.isNotEmpty(o1.getModuleName()) &&
                        StringUtil.isNotEmpty(o2.getModuleName())) {
                    if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) {
                        return 1;
                    }
                    if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) {
                        return -1;
                    }
                }else if(StringUtil.isNotEmpty(o1.getModuleName())&&
                        StringUtil.isEmpty(o2.getModuleName())){
                    return 1;
                }else if(StringUtil.isEmpty(o1.getModuleName())&&
                        StringUtil.isNotEmpty(o2.getModuleName())){
                    return -1;
                } else {
                    return 1;
                }
                return 0;
        }
    };


    /**
     * 按进程数量 降序
     */
    public static final Comparator<Module> PROCESSNUM_DESC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (o1.getProcessNum()>o2.getProcessNum()) {
                return -1;
            }
            if (o1.getProcessNum()<o2.getProcessNum()) {
                return 1;
            }
            return 0;
        }
    };


    /**
     * 按进程数量 升序
     */
    public static final Comparator<Module> PROCESSNUM_ASC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (o1.getProcessNum()>o2.getProcessNum()) {
                return 1;
            }
            if (o1.getProcessNum()<o2.getProcessNum()) {
                return -1;
            }
            return 1;
        }
    };

    /**
     * 按主要负责人 降序
     */
    public static final Comparator<Module> MAINPRINCIPAL_DESC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (StringUtil.isNotEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isNotEmpty(o2.getMainPrincipal())) {
                if (o1.getMainPrincipal().compareTo(o2.getMainPrincipal()) > 0) {
                    return -1;
                }
                if (o1.getMainPrincipal().compareTo(o2.getMainPrincipal()) < 0) {
                    return 1;
                }
            } else if (StringUtil.isNotEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isEmpty(o2.getMainPrincipal())) {
                return -1;
            } else if (StringUtil.isEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isNotEmpty(o2.getMainPrincipal())) {
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };


    /**
     * 按主要负责人升序
     */
    public static final Comparator<Module> MAINPRINCIPAL_ASC = new Comparator<Module>() {
        @Override
        public int compare(Module o1, Module o2) {
            if (StringUtil.isNotEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isNotEmpty(o2.getMainPrincipal())) {
                if (o1.getMainPrincipal().compareTo(o2.getMainPrincipal()) > 0) {
                    return 1;
                }
                if (o1.getMainPrincipal().compareTo(o2.getMainPrincipal()) < 0) {
                    return -1;
                }
            } else if (StringUtil.isNotEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isEmpty(o2.getMainPrincipal())) {
                return 1;
            } else if (StringUtil.isEmpty(o1.getMainPrincipal()) &&
                    StringUtil.isNotEmpty(o2.getMainPrincipal())) {
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };
}
