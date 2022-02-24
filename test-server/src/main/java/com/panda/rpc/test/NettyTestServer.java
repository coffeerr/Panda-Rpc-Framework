package com.panda.rpc.test;

import com.panda.rpc.api.HelloService;
import com.panda.rpc.netty.server.NettyServer;
import com.panda.rpc.registry.DefaultServiceRegistry;
import com.panda.rpc.registry.ServiceRegistry;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 4:08 下午
 */

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        nettyServer.start(9000);
    }
}
