package org.swift.framework.mvc;

import org.apache.commons.lang3.ArrayUtils;
import org.swift.framework.helper.BeanHelper;
import org.swift.framework.helper.ConfigHelper;
import org.swift.framework.helper.ControllerHelper;
import org.swift.framework.helper.HelperLoader;
import org.swift.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求分发器
 * create by ww
 **/
@WebServlet(urlPatterns =  "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关Helper类
        HelperLoader.init();
        //获取ServletContext 对象，用于注册Servlet
        ServletContext servletContext = config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath());
        //注册处理静态资源的默认Servlet
        ServletRegistration resourcesServlet = servletContext.getServletRegistration("default");
        resourcesServlet.addMapping(ConfigHelper.getAppAssetPath());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //请求方法和路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();
        //获取Action处理器
        URLHandler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            //获取Controller类和类实例
            Class<?> controller = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBeanInstance(controller);
            //创建请求参数对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            //获取传递的参数，解码成UTF-8
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)) {
                String[] params = body.split("&");
                if (ArrayUtils.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] paramCouple = param.split("=");
                        if (ArrayUtils.isNotEmpty(paramCouple) && paramCouple.length == 2) {
                            String paramName = paramCouple[0];
                            String paramValue = paramCouple[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            //调用action方法
            Method action = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean, action, param);
            //处理action方法返回值
            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)) {
                    if (path.startsWith("/")) {
                        //重定向
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            //转发 参数
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        //转发
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req,resp);
                    }
                }
            } else if (result instanceof Data) {
                //返回json数据
                Data data = (Data)result;
                Object model = data.getModel();
                if (model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }

    }

}
