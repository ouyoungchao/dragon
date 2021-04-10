package com.shiliu.dragon.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionCache {
    private static Map<String, Object> sessionCache = new ConcurrentHashMap();

    public SessionCache() {
    }

    public static boolean addSession(String key, Object object) {
        if (key != null && !key.trim().isEmpty() && object != null) {
            sessionCache.put(key, object);
            return true;
        } else {
            throw new IllegalArgumentException("seesion param error");
        }
    }

    public static Object getValueFromCache(String key) {
        if (key != null && !key.trim().isEmpty()) {
            return sessionCache.get(key);
        } else {
            throw new IllegalArgumentException("seesion param error");
        }
    }

    public static void removeFromCache(String key) {
        if (key != null && !key.trim().isEmpty()) {
            sessionCache.remove(key);
        } else {
            throw new IllegalArgumentException("seesion param error");
        }
    }
}
