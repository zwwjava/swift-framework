package org.swift.framework.proxy;

/**
 * @Description -代理接口
 * @Author zhaww
 * @Date 2018/9/28 13:46
 */
public interface Proxy {
    /**
     * 执行链式代理
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
