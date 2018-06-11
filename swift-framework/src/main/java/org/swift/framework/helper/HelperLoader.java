package org.swift.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.util.ClassUtil;

/**
 * 初始化相应的Helper类
 * create by ww
 **/
public class HelperLoader {

    private static final Logger logger = LoggerFactory.getLogger(HelperLoader.class);

    /**
     * 初始化相应的Helper类
     */
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                ControllerHelper.class,
                IocHelper.class
        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName());
            System.out.println("初始化类成功" + clazz.getName());
            logger.info("初始化类成功" + clazz.getName());
        }
    }

}
