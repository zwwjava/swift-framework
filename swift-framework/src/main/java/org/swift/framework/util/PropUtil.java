package org.swift.framework.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 参数读取工具类
 */
public final class PropUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropUtil.class);

    /**
     * 加载属性工具
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {
        Properties prop = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream == null) {
                logger.error(fileName + "file is not found");
                throw new FileNotFoundException(fileName + "file is not found");
            }
            prop = new Properties();
            prop.load(inputStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    /**
     * 从参数值获得字符串值，默认值为“”
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties,String key) {
        return getString(properties ,key, "");
    }

    /**
     * 从参数值获得字符串值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String result = defaultValue;
        if(properties.containsKey(key)) {
            result = properties.getProperty(key);
        }
        return result;
    }

    /**
     * 从参数值获得整型，默认值0
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties,String key) {
        return getInt(properties ,key, 0);
    }

    /**
     * 从参数值获得布尔值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties, String key, int defaultValue) {
        int result = defaultValue;
        if(properties.containsKey(key)) {
            result = CastUtil.castInt(properties.getProperty(key));
        }
        return result;
    }

    /**
     * 从参数值获得布尔值，默认值为false
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties,String key) {
        return getBoolean(properties ,key, false);
    }

    /**
     * 从参数值获得字符串值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean result = defaultValue;
        if(properties.containsKey(key)) {
            result = CastUtil.castBoolean(properties.getProperty(key));
        }
        return result;
    }

}
