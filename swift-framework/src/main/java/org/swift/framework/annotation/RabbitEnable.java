package org.swift.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description - 类注解，确认该类时配置 mq监听的类
 * @Author zww
 * @Date 2019/9/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitEnable {
}
