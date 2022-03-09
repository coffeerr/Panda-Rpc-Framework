package com.panda.rpc.provider;

import com.panda.rpc.enumeration.RpcError;
import com.panda.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/9 9:16 上午
 */

public class ServiceProviderImpl implements ServiceProvider {
    public static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    public static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}", interfaces, serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object o = serviceMap.get(serviceName);
        if (o == null) {
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        return o;
    }
}
