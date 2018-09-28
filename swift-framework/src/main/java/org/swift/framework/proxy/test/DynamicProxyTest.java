package org.swift.framework.proxy.test;

public class DynamicProxyTest {
    public static void main(String[] args) {
        IHello hello = new Hello();
//        DynamicProxy dynamicProxy = new DynamicProxy(hello);
//        IHello hel = (IHello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), dynamicProxy);

//        IHello hel = (IHello) new DynamicProxy().bind(new Hello());
//        hel.sayHello();
//        hel.sayHi();

        IHello helloProxy = CGLibProxy.getCgLibProxyInstance().getProxy(Hello.class);
//        Hello helloProxy = (Hello) Enhancer.create(Hello.class, cgLibProxy);
        helloProxy.sayHello();
        helloProxy.sayHi();

        /*int[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        int[] destination = {4,5,6};
        System.arraycopy(ints, 0, destination,1,1);
        System.out.println(ints);
        System.out.println(destination.toString());*/
    }
}
