package org.swift.framework.helper;

import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.swift.framework.annotation.Inject;
import org.swift.framework.util.CollectUtil;
import org.swift.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * create by ww
 * 负责service的注入
 **/
public class IocHelper {

    static {
        //获取所有的Bean类与Bean实例之间的映射（Bean Map）
        Map<Class<?>, Object> register = BeanHelper.getBeanRegister();
        if (CollectUtil.isNotEmpty(register)) {
            //遍历register
            for (Map.Entry<Class<?>, Object> beanEntry : register.entrySet()) {
                //抓取bean类 和 bean实体
                Class<?> clazz = beanEntry.getKey();
                Object objectInstance = beanEntry.getValue();
                //获取bean的所以成员变量Field
                Field[] fields = clazz.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(fields)) {
                    for (Field field : fields) {
                        //判断是否有inject注解
                        if(field.isAnnotationPresent(Inject.class)) {
                            //在Register中获取实例
                            Class<?> filedClass = field.getType();
                            Object fieldInstance = register.get(filedClass);
                            if (fieldInstance != null) {
                                //反射初始化
                                ReflectionUtil.setField(objectInstance, field, fieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }

}
