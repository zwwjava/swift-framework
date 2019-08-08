package org.swift.framework.helper;

import org.swift.framework.annotation.Controller;
import org.swift.framework.annotation.Inject;
import org.swift.framework.annotation.Service;
import org.swift.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 解析包下Class文件
 * 用来存放项目根目录下所有的类的class信息
 * create by ww
 **/
public class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    /**
     * 在初始化类时，需要先执行静态区的代码
     * 获取包名下所有类
     */
    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSetByPackageName(basePackage);
    }

    /**
     * 拿到获取到的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 拿到所以的服务类
     * @return
     */
    public static Set<Class<?>> getServiceClass() {
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> clazz: CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class)) {
                serviceSet.add(clazz);
            }
        }
        return serviceSet;
    }

    /**
     * 拿到所以的Controller类
     * @return
     */
    public static Set<Class<?>> getControllerClass() {
        Set<Class<?>> controllerSet = new HashSet<Class<?>>();
        for (Class<?> clazz: CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllerSet.add(clazz);
            }
        }
        return controllerSet;
    }

    /**
     * 拿到所以的Bean类  包括 controller service
     * @return
     */
    public static Set<Class<?>> getBeanClass() {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        for (Class<?> clazz: CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                beanSet.add(clazz);
            }
        }
        return beanSet;
    }

    /**
     * 拿到引用包名下某父类（或接口）的所有子类（或实现类）
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        for (Class<?> clazz: CLASS_SET) {
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                beanSet.add(clazz);
            }
        }
        return beanSet;
    }

    /**
     * 拿到带有某注解的所有类
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        for (Class<?> clazz: CLASS_SET) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                beanSet.add(clazz);
            }
        }
        return beanSet;
    }

}
