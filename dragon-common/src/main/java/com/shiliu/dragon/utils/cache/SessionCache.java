package com.shiliu.dragon.utils.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionCache {
    private static Map<String, Object> sessionCache = new ConcurrentHashMap();

    public SessionCache() {
    }
}
