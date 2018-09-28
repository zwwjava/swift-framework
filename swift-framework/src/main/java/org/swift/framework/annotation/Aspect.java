package org.swift.framework.annotation;

import java.lang.annotation.*;

/**
 * @Description -切面注解
 * @Author zhaww
 * @Date 2018/9/28 13:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 注解
     * @return
     */
    Class<? extends Annotation> value();
}
