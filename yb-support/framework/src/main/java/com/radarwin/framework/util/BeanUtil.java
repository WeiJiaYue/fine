package com.radarwin.framework.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by josh on 15/6/18.
 */
public class BeanUtil {

    private static Logger logger = LogManager.getLogger(BeanUtil.class);

    private static final String SET = "set";

    private static final String GET = "get";

    private static final String GET_CLASS = "getClass";

    private static final Map<Class, List<FieldInfo>> beanCache = new ConcurrentHashMap<>();

    private static final Map<String, BeanCopier> copyCache = new ConcurrentHashMap<>();

    /**
     * 复制属性
     *
     * @param source
     * @param target
     */
    public static void copyBean(Object source, Object target) {
        String key = source.getClass().getName() + target.getClass().getName();
        BeanCopier beanCopier = copyCache.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            copyCache.put(key, beanCopier);
        }
        beanCopier.copy(source, target, null);
    }

    /**
     * 把map转换成JavaBean
     *
     * @param cls
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T resolveMapToBean(Class<T> cls, Map map) {
        if (map == null || map.size() == 0) {
            return null;
        }

        T t = null;

        try {
            t = cls.newInstance();

            if (!beanCache.containsKey(cls)) {
                addBeanCache(cls);
            }

            List<FieldInfo> fieldList = beanCache.get(cls);

            for (FieldInfo fieldInfo : fieldList) {
                try {
                    if (map.containsKey(fieldInfo.getFieldName())) {
                        Object val = map.get(fieldInfo.getFieldName());
                        if (val != null && val.getClass().equals(fieldInfo.getFieldType())) {
                            fieldInfo.getMethodSet().invoke(t, val);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage(), ex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
        }
        return t;
    }

    /**
     * 把JavaBean转换成map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> resolveBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Class cls = obj.getClass();
        try {

            if (!beanCache.containsKey(cls)) {
                addBeanCache(cls);
            }

            List<FieldInfo> fieldList = beanCache.get(cls);

            for (FieldInfo fieldInfo : fieldList) {
                String key = fieldInfo.getFieldName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = fieldInfo.getMethodGet();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return map;
    }

    public static Map<String, Object> resolveBeanToEsMap(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Class cls = obj.getClass();
        try {

            if (!beanCache.containsKey(cls)) {
                addBeanCache(cls);
            }

            List<FieldInfo> fieldList = beanCache.get(cls);

            for (FieldInfo fieldInfo : fieldList) {
                String key = fieldInfo.getFieldName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法

                    Method getter = fieldInfo.getMethodGet();
                    Object value = getter.invoke(obj);

                    if (value == null || !JavaType.containsType(fieldInfo.getFieldType().getName())) {
                        continue;
                    }

                    if (fieldInfo.getFieldType().getName().equals(Date.class.getName())) {
                        map.put(key, ((Date) value).getTime());
                    } else {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return map;
    }

    /**
     * 添加JavaBean缓存
     *
     * @param cls
     * @throws NoSuchMethodException
     */
    private static void addBeanCache(Class cls) throws NoSuchMethodException {
        List<FieldInfo> fieldList = new ArrayList<>();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith(GET) && !GET_CLASS.equals(methodName)) {
                FieldInfo fieldInfo = new FieldInfo();
                String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                String setName = SET + methodName.substring(3);
                Method methodSet = cls.getMethod(setName, method.getReturnType());
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setMethodGet(method);
                fieldInfo.setMethodSet(methodSet);
                fieldInfo.setFieldType(method.getReturnType());
                fieldList.add(fieldInfo);
            }
        }
        beanCache.put(cls, fieldList);
    }

    public static void main(String[] args) {

    }

    static class FieldInfo {
        private String fieldName;

        private Method methodSet;

        private Method methodGet;

        private Class fieldType;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Method getMethodSet() {
            return methodSet;
        }

        public void setMethodSet(Method methodSet) {
            this.methodSet = methodSet;
        }

        public Method getMethodGet() {
            return methodGet;
        }

        public void setMethodGet(Method methodGet) {
            this.methodGet = methodGet;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }
    }
}


