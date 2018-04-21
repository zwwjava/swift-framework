package org.swift.framework.mvc;

import java.lang.reflect.Method;

/**
 * 封装后台访问类
 * create by ww
 **/
public class URLHandler {

    //Controller类
    private Class<?> controllerClass;

    //Action类
    private Method ActionMethod;

    public URLHandler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.ActionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return ActionMethod;
    }
}
