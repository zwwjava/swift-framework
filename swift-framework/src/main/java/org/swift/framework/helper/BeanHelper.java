package org.swift.framework.helper;

import org.swift.framework.util.ClassUtil;
import org.swift.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单例注册Bean
 * create by ww
 **/
public class BeanHelper {

    /**
     * bean注册中心
     */
    private static final Map<Class<?>, Object> BEAN_REGISTER = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getClassSet();
        for (Class<?> clazz : beanClassSet) {
            Object instance = ReflectionUtil.newInstance(clazz);
            BEAN_REGISTER.put(clazz, instance);
        }
    }

    /**
     * 获取所有注册的bean
     * @return
     */
    public static Map<Class<?>, Object> getBeanRegister() {
        return BEAN_REGISTER;
    }

    /**
     * 从注册中心获取bean实例
     * @param clazz
     * @return
     */
    public static Object getBeanInstance(Class<?> clazz) {
        if (!BEAN_REGISTER.containsKey(clazz)) {
            throw new RuntimeException("this bean do not register");
        }
        return BEAN_REGISTER.get(clazz);
    }

    /**
     * 从注册中心添加bean实例
     * @param clazz
     * @return
     */
    public static void putBeanInstance(Class<?> clazz, Object obj) {
        BEAN_REGISTER.put(clazz, obj);
    }

}
