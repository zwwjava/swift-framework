package org.swift.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类反射工具
 * create by ww
 **/
public class ReflectionUtil {

    /**
     * 返回一个类实例
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 反射执行方法
     * @param object
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object object, Method method, Object... args) {
        Object result = null;
        /**
         * Set the {@code accessible} flag for this object to
         *      * the indicated boolean value.  A value of {@code true} indicates that
         *      * the reflected object should suppress Java language access
         *      * checking when it is used.  A value of {@code false} indicates
         *      * that the reflected object should enforce Java language access checks.
         */
        try {
            method.setAccessible(true);
            result = method.invoke(object,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置成员变量的值
     * @param object
     * @param field
     * @param arg
     */
    public static void setField(Object object, Field field, Object arg) {
        try {
            field.setAccessible(true);
            field.set(object, arg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
