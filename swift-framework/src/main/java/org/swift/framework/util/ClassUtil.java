package org.swift.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.helper.ConfigHelper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 */
public class ClassUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);
    private static ClassLoader myClassLoader;

    /**
     * 获取类加载器
     * @return
     */
    public static ClassLoader getClassLoader() {
        if (myClassLoader == null) {
            myClassLoader = Thread.currentThread().getContextClassLoader();
        }
        return myClassLoader;
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }
    /**
     * 加载初始化并返回类
     * @param className
     * @param initialized
     * @return
     */
    public static Class<?> loadClass(String className, boolean initialized) {
        Class<?> claz;
        try {
            claz = Class.forName(className, initialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("类加载出错");
            throw new RuntimeException(e);
        }
        return claz;
    }

    /**
     * 加载包路径下所有类的Class 信息
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSetByPackageName(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            //获取所有资源
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    //文件协议
                    String protocol = url.getProtocol();
                    if(("file").equals(protocol)) {
                        String packageUrl = url.getPath().replace("%20"," ");//替换空格
                        //递归添加子类的包的类
                        addClass(classSet, packageUrl, ConfigHelper.getAppBasePackage());
                    } else if(("jar").equals(protocol)) {
                        JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
                        if(jarUrlConnection != null) {
                            JarFile jarFile = jarUrlConnection.getJarFile();
                            if(jarFile != null) {
                                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                                while (jarEntrys.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntrys.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("解析包路径下的类出错");
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 递归添加路径下的类
     * @param classSet
     * @param packageUrl    子类路径
     * @param packageName   父类路径
     */
    private static void addClass(Set<Class<?>> classSet, String packageUrl, String packageName) {
        //返回一个File[] 经过文件的拦截
        File[] files = new File(packageUrl).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)) {
                    className = packageName  + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtil.isNotEmpty(packageUrl)) {
                    subPackagePath = packageUrl + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." +  subPackageName;
                }
                //递归添加子文件
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> claz = loadClass(className, false);
        classSet.add(claz);
    }

}
