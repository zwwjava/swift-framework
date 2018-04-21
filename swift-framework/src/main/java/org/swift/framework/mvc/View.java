package org.swift.framework.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 * create by ww
 **/
public class View {

    //视图路径
    private String path;

    //模型数据
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String kay, Object value) {
        model.put(kay, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
