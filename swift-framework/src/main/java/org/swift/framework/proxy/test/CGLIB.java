package org.swift.framework.proxy.test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLIB {
    private void test() {
        System.out.println("hello world");
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CGLIB.class);
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                Object result = methodProxy.invokeSuper(o, objects);
                System.out.println("after");
                return result;
            }
        });
        CGLIB cglib = (CGLIB)enhancer.create();
        cglib.test();
    }

}
