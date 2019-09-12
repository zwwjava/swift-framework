package org.swift.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description - 注解在方法上，配置mq监听的详细信息
 * @Author zww
 * @Date 2019/9/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitListener {
    /**
     * 队列名称
     * @return
     */
    String queue();

    /**
     * 交换器名称
     * @return
     */
    String exchange();
}
