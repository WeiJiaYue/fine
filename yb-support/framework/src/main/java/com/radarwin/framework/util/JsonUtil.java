package com.radarwin.framework.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 15/7/5.
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger(JsonUtil.class);

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 把json字符串 转换成 map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(json, Map.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return map;
    }

    /**
     * 把json字符串转换成对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> cls) {
        T t = null;
        try {
            t = mapper.readValue(json, cls);
        } catch (Exception e) {
            logger.error(e);
        }
        return t;
    }

    /**
     * 把对象转出json字符串
     *
     * @param o
     * @return
     */
    public static String objectToJson(Object o) {
        String s = null;
        try {
            s = mapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error(e);
        }
        return s;
    }
}
