package org.swift.framework.helper;

import org.swift.framework.config.ConfigConstant;
import org.swift.framework.util.PropUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 */
public class ConfigHelper {

    /**
     * 加载默认文件参数
     */
    private static final Properties CONFIG_PROP = PropUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.BASE_PACKAGE);
    }

    public static String getAppJspPath() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.JSP_PATH);
    }

    public static String getAppAssetPath() {
        return PropUtil.getString(CONFIG_PROP, ConfigConstant.ASSET_PATH);
    }

}
