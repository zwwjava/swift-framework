package org.swift.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json工具
 * create by ww
 **/
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将POJO转为json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJson(T obj) {
        String json = "";
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("将POJO转为json失败");
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 将json转为Bean
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        T bean = null;
        try {
            bean = OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("将json转为Bean失败");
            e.printStackTrace();
        }
        return bean;
    }

}
