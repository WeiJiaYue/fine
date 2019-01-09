package com.radarwin.framework.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by josh on 15/6/20.
 */
public class PropertyReader {

    private static final String path = "/config/";

    public static final String JDBC_FILE = "jdbc.properties";

    public static final String CACHE_FILE = "cache.properties";

    public static final String ES_FILE = "elasticsearch.properties";

    private static final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public static String get(String key, String fileName) {
        if (cache.containsKey(fileName)) {
            Map<String, String> map = cache.get(fileName);
            return map.get(key);
        }

        Map<String, String> map = new HashMap<>();
        Properties prop = new Properties();
        InputStream in = PropertyReader.class.getResourceAsStream(path + fileName);
        try {
            if (in != null) {
                prop.load(new InputStreamReader(in, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Map.Entry<Object, Object>> set = prop.entrySet();
        Iterator<Map.Entry<Object, Object>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            String k = entry.getKey().toString();
            String v = (entry.getValue() == null ? null : entry.getValue().toString());
            map.put(k, v);
        }
        cache.put(fileName, map);
        return map.get(key);
    }
}
