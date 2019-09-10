package org.swift.chapter.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.annotation.Aspect;
import org.swift.framework.annotation.Controller;
import org.swift.framework.proxy.AbstractAspecProxy;

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
        logger.info("项目管家：-------------begin----------");
        logger.info(String.format("项目管家：执行类class : %s", cls.getName()));
        logger.info(String.format("项目管家：执行方法method : %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Exception {
        logger.info(String.format("time %dms", System.currentTimeMillis() - begin));
        logger.info("项目管家：---------------end--------------");
    }
}
