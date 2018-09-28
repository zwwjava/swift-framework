package org.swift.framework.proxy.test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxy implements MethodInterceptor {

    private static CGLibProxy cgLibProxy = new CGLibProxy();

    public static CGLibProxy getCgLibProxyInstance() {
        return cgLibProxy;
    }

    public <T> T getProxy(Class<T> cls) {
        //Hello helloProxy = (Hello) Enhancer.create(Hello.class, cgLibProxy);
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("after");
        return result;
    }
}
