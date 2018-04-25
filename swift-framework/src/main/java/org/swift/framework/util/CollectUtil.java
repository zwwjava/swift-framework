package org.swift.framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * 容器工具类
 */
public class CollectUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?,?> map) {
        return map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?,?> map) {
        return !isEmpty(map);
    }

}
