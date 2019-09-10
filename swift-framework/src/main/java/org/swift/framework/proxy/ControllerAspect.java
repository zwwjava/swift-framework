package org.swift.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.annotation.Aspect;
import org.swift.framework.annotation.Controller;

import java.lang.reflect.Method;

/**
 * @Description - 拦截controller层
 * @Author zww
 * @Date 2018/9/28 14:42
 */
@Aspect(Controller.class)
public class ControllerAspect extends AbstractAspecProxy {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Exception {
        logger.debug("-------------begin----------");
        logger.debug(String.format("class : %s", cls.getName()));
        logger.debug(String.format("method : %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Exception {
        logger.debug(String.format("time %dms", System.currentTimeMillis() - begin));
        logger.debug("---------------end--------------");
    }
}
