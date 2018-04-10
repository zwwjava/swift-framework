package org.swift.framework.util;

/**
 * 类型转换工具
 */
public final class CastUtil {

    /**
     * 转换为字符串类型 ，默认值“”
     * @param obj
     * @return
     */
    public static String castString(Object obj) {
        return castString(obj,"");
    }

    /**
     * 转换为字符串类型
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /**
     * 转换为Double类型 ，默认值0
     * @param obj
     * @return
     */
    public static double castDouble(Object obj) {
        return castDouble(obj,0);
    }

    /**
     * 转换为Double类型
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double castDouble(Object obj, double defaultValue) {
        double result = defaultValue;
        if(obj != null) {
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)) {
                try {
                    result = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    result = defaultValue;
                }
            }
        }
        return result;
    }

    /**
     * 转换为Long类型 ，默认值0
     * @param obj
     * @return
     */
    public static long castLong(Object obj) {
        return castLong(obj,0);
    }

    /**
     * 转换为Long类型
     * @param obj
     * @param defaultValue
     * @return
     */
    public static long castLong(Object obj, long defaultValue) {
        long result = defaultValue;
        if(obj != null) {
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)) {
                try {
                    result = Long.parseLong(str);
                } catch (NumberFormatException e) {
                    result = defaultValue;
                }
            }
        }
        return result;
    }

    /**
     * 转换为int类型 ，默认值0
     * @param obj
     * @return
     */
    public static int castInt(Object obj) {
        return castInt(obj,0);
    }

    /**
     * 转换为int类型
     * @param obj
     * @param defaultValue
     * @return
     */
    public static int castInt(Object obj, int defaultValue) {
        int result = defaultValue;
        if(obj != null) {
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)) {
                try {
                    result = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    result = defaultValue;
                }
            }
        }
        return result;
    }

    /**
     * 转换为布尔类型 ，默认值false
     * @param obj
     * @return
     */
    public static boolean castBoolean(Object obj) {
        return castBoolean(obj,false);
    }

    /**
     * 转换为布尔类型
     * @param obj
     * @param defaultValue
     * @return
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        return obj != null ? Boolean.parseBoolean(castString(obj)) : defaultValue;
    }

}
