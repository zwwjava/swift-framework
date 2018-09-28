package org.swift.framework.util;

public enum HtmlViewType {
    ORIGINAL(0, "原始样式"),
    HORIZON_CENTER(1, "默认");

    private Integer code;
    private String name;

    HtmlViewType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
