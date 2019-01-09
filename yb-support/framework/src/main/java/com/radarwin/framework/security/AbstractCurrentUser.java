package com.radarwin.framework.security;

import java.util.Map;

/**
 * Created by josh on 15/9/9.
 */
public abstract class AbstractCurrentUser {

    public static ThreadLocal<Map<String, String>> userLocal = new ThreadLocal<>();

    public static void addUserLocal(Map<String, String> map) {
        userLocal.set(map);
    }

    public static void removeUserLocal() {
        userLocal.remove();
    }

    public abstract String getCurrentUserName();

    public abstract Object getObject();
}
