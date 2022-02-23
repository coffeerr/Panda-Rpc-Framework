package com.panda.rpc.serializer;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/22 4:24 下午
 */

public interface CommonSerializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[]bytes,Class<?> clazz);
    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code){
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
