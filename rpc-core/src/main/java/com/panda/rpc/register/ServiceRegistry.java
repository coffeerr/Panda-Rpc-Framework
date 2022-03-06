package com.panda.rpc.register;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/6 5:19 下午
 */

public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 发现服务
     *
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);

}
