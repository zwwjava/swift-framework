package org.swift.chapter.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.proxy.AbstractAspecProxy;

import java.lang.reflect.Method;

/**
 * @Description - 拦截controller层
 * @Author zww
 * @Date 2018/9/28 14:42
 */
//@Aspect(Controller.class)
public class ControllerAspectA extends AbstractAspecProxy {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspectA.class);
    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Exception {
        logger.info("代理1 -------------begin----------");
        logger.info(String.format("代理1 class : %s", cls.getName()));
        logger.info(String.format("代理1 method : %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Exception {
        logger.info(String.format("代理1 time %dms", System.currentTimeMillis() - begin));
        logger.info("代理1 ---------------end--------------");
    }
}
