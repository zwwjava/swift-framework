package org.swift.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Description -切面代理
 * @Author zhaww
 * @Date 2018/9/28 14:22
 */
public abstract class AbstractAspecProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAspecProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(cls, method, params)) {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            logger.error("proxy failure", e);
            error(cls, method, params, e);
            throw e;
        } finally {
            end();
        }
        return result;
    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Exception {
        String methodName = method.getName();
        if (!("toString").equals(methodName) || !("hashCode").equals(methodName))  {
            return true;
        }
        return false;
    }

    private void begin() {

    }

    private void end() {

    }

    public void before(Class<?> cls, Method method, Object[] params)  throws Exception {

    }

    public void after(Class<?> cls, Method method, Object[] params)  throws Exception {

    }

    public void error(Class<?> cls, Method method, Object[] params, Exception e)  throws Exception {
    }

}
