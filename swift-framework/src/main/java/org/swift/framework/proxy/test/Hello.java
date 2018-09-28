package org.swift.framework.proxy.test;

public class Hello implements IHello {

    @Override
    public void sayHello() {
        System.out.println("hello world");
    }

    @Override
    public void sayHi() {
        System.out.println("hi world");
    }
}
