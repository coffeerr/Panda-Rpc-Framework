package com.panda.rpc.registry;

public interface ServiceRegistry {

    /**
     * 注册
     *
     * @param service 服务
     */
    <T> void register (T service);

    /**
     * 获取服务
     * @param serviceName
     * @return {@link Object}
     */
    Object getService(String serviceName);
;}
