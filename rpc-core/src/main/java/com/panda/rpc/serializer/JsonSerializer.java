package com.panda.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.enumeration.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/22 4:26 下午
 */

public class JsonSerializer implements CommonSerializer {
    private final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("序列化时发生错误：" + e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        }catch (IOException e){
            logger.error("反序列化时有错误发生：{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }

    /**
     * 将RpcRequest中固定为Object的Parameters序列化对象存储为其特有对象
     *
     * @param obj
     * @return {@link Object}
     */
    private Object handleRequest(Object obj) {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            //rpcRequest.getParameters()[i] 是不是clazz的子类
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                try {
                    byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                    rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rpcRequest;
    }
}
