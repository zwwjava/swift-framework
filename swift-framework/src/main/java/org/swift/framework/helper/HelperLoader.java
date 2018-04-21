package org.swift.framework.helper;

import org.swift.framework.util.ClassUtil;

/**
 * 初始化相应的Helper类
 * create by ww
 **/
public class HelperLoader {

    /**
     * 初始化相应的Helper类
     */
    public static void init() {
        Class<?>[] classList = {
                BeanHelper.class,
                ClassHelper.class,
                ControllerHelper.class,
                IocHelper.class
        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName());
        }
    }

}
