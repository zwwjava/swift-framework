package org.swift.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.swift.framework.annotation.Action;
import org.swift.framework.mvc.Request;
import org.swift.framework.mvc.URLHandler;
import org.swift.framework.util.CollectUtil;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手
 * create by ww
 **/
public class ControllerHelper {

    private static final Map<Request, URLHandler> ACTION_REGISTER = new HashMap<Request, URLHandler>();

    static {
        //获取所有controller类
        Set<Class<?>> controllerSet = ClassHelper.getControllerClass();
        if (CollectUtil.isNotEmpty(controllerSet)) {
            //遍历所有controller
            for (Class<?> controller : controllerSet) {
                //获取controller下所有method
                Method[] methods = controller.getDeclaredMethods();
                //遍历方法
                for (Method method : methods) {
                    //判断方法是否Action注解
                    if (method.isAnnotationPresent(Action.class)) {
                        Action action = method.getAnnotation(Action.class);
                        String urlMapping = action.value();
                        //验证映射规则   \w等价于"[A-Za-z0-9_]"
                        if (urlMapping.matches("\\w+:/\\w*")) {
                            String[] array = urlMapping.split(":");
                            if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                                //请求方法类型 和 请求路径
                                String requestMethod = array[0];
                                String requestPath = array[1];
                                //请求路径
                                Request request = new Request(requestMethod, requestPath);
                                //请求后端类
                                URLHandler handler = new URLHandler(controller, method);
                                ACTION_REGISTER.put(request, handler);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取handler
     * @param request
     * @return
     */
    public static URLHandler getHandler(Request request) {
        if (request == null) {
            return null;
        }
        return ACTION_REGISTER.get(request);
    }

    /**
     * 获取handler
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static URLHandler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return getHandler(request);
    }

}
