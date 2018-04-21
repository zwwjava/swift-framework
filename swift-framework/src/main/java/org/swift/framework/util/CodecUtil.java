package org.swift.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具
 * create by ww
 **/
public class CodecUtil {

    private static final Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 对资源进行编码
     * @param source
     * @return
     */
    public static String encodeURL(String source) {
        String target = "";
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("编码失败");
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 对资源进行解码
     * @param source
     * @return
     */
    public static String decodeURL(String source) {
        String target = "";
        try {
            target = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("解码失败");
            e.printStackTrace();
        }
        return target;
    }

}
