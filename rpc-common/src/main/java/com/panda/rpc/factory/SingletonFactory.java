package com.panda.rpc.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/9 3:19 下午
 */

public class SingletonFactory {
    public static Map<Class, Object> objectMap = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objectMap.get(clazz);
        synchronized (clazz) {
            if (null == instance) {
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}
