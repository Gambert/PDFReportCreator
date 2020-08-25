package com.shivsoftech.core;

import java.util.HashMap;
import java.util.Map;

public class AppContext {

    private static AppContext INSTANCE = new AppContext();

    private Map<String, Object> values = null;

    private AppContext() {
        values = new HashMap();
    }

    public static AppContext getInstance() {
        synchronized (AppContext.class) {
            return INSTANCE;
        }
    }

    public void set(String key, Object value) {
        this.values.put(key, value);
    }

    public Object get(String key) {
        return this.values.get(key);
    }

    @Override
    public String toString() {
        return "[ AppContext : " + values.toString() + " ]";
    }
}
