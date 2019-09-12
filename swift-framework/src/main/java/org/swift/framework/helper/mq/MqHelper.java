package org.swift.framework.helper.mq;

import org.swift.framework.annotation.RabbitEnable;
import org.swift.framework.annotation.RabbitListener;
import org.swift.framework.helper.ClassHelper;
import org.swift.framework.util.log.SwiftLogger;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @Description - 对MQ消费者监听信息的处理
 * @Author zww
 * @Date 2019/9/12
 */
public class MqHelper {

    static {
        try {
            //拿到（所有） RabbitEnable 注解的类
            Set<Class<?>> rabbitEnableClassSet = ClassHelper.getClassSetByAnnotation(RabbitEnable.class);
            //拿到所有 RabbitListener 注解的方法
            for (Class<?> clazz : rabbitEnableClassSet) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RabbitListener.class)) {
                        RabbitListener rabbitListener = method.getAnnotation(RabbitListener.class);
                        //为每个 RabbitListener 方法开启一个线程，接收mq消息，执行处理方法
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    //接受mq信息
                                    RabbitTemplate.receiveMq(rabbitListener.queue(), clazz.newInstance(), method);
                                } catch (Exception e) {
                                    SwiftLogger.error("接收mq消息 时出现异常");
                                }
                            }
                        }.start();
                    }
                }
            }
        } catch (Exception e) {
            SwiftLogger.error("MqHelper failure ", e);
        }
    }

}
