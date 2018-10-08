package org.swift.framework.mvc;

import org.swift.framework.util.CastUtil;
import org.swift.framework.util.CollectUtil;

import java.util.Map;

/**
 * 请求参数对象
 * create by ww
 **/
public class Param {

    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名称获取Long型参数值
     * @param name
     * @return
     */
    public Long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     * @return
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEmpty() {
        return CollectUtil.isEmpty(paramMap);
    }
}
