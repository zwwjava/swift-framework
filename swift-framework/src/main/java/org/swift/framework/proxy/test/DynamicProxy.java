package org.swift.framework.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

    Object originalObj;

    public DynamicProxy() {
    }

    public DynamicProxy(Object originalObj) {
        this.originalObj = originalObj;
    }

    Object bind(Object originalObj) {
        this.originalObj = originalObj;
        return Proxy.newProxyInstance(originalObj.getClass().getClassLoader(), originalObj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("welcome");
        return method.invoke(originalObj,args);
    }
}
