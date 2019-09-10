package org.swift.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.annotation.Aspect;
import org.swift.framework.annotation.Service;
import org.swift.framework.proxy.AbstractAspecProxy;
import org.swift.framework.proxy.Proxy;
import org.swift.framework.proxy.ProxyManager;
import org.swift.framework.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @Description - aop注解初始化助手
 * @Author zhaww
 * @Date 2018/9/28 14:59
 */
public class AopHelper {

    private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            //每个代理类 对应的多个被代理类
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            //每个被代理类 对应的多个代理对象
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.putBeanInstance(targetClass, proxy); //用代理类来替代原生实现类
            }
        } catch (Exception e) {
            logger.error("aop failure ", e);
        }
    }



    //获取所有代理类
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        addAspectProxy(proxyMap);
        addTransaction(proxyMap);
        return proxyMap;
    }

    //添加切面代理
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AbstractAspecProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    //添加事物代理
    private static void addTransaction(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            //添加所有 aop 代理注解
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    //每个类 对应的多个Aspect 注解
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> cla : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(cla)) {
                    targetMap.get(cla).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(cla, proxyList);
                }
            }
        }
        return targetMap;
    }

}
