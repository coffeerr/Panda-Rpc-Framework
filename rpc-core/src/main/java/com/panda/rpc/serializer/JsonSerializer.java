package com.panda.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.enumeration.SerializeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 2:07 下午
 */

public class JsonSerializer implements CommonSerializer {
    private final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            logger.info("序列化时发生错误：" + e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {

            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (Exception e) {
            logger.info("序列化时发生错误：" + e);
            return null;
        }
    }

    @Override
    public int getCode() {
        return SerializeCode.valueOf("JSON").getCode();
    }

    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParameters().length; i++) {
            Class<?> paramType = rpcRequest.getParamTypes()[i];
            if (!paramType.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, paramType);
            }
        }
        return rpcRequest;
    }
}
