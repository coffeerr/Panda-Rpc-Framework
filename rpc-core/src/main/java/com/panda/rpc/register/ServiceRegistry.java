package com.panda.rpc.register;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/9 8:54 上午
 */

public interface ServiceRegistry {

    /**
     * 注册
     *
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 订阅服务
     *
     * @param serviceName
     * @return
     */
    InetSocketAddress lookUpService(String serviceName);
}
