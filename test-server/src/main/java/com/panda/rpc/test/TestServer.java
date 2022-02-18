package com.panda.rpc.test;

import com.panda.rpc.api.HelloService;
import com.panda.rpc.server.RpcServer;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/18 3:53 下午
 */

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(9000,helloService);
    }
}
