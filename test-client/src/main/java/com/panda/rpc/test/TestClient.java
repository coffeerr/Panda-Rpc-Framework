package com.panda.rpc.test;

import com.panda.rpc.api.HelloObject;
import com.panda.rpc.api.HelloService;
import com.panda.rpc.client.RpcClientProxy;
import com.panda.rpc.entity.RpcResponse;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/18 3:46 下午
 */

public class TestClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9000;
        HelloObject helloObject = new HelloObject(99,"这是来自异世界的问候！");
        RpcClientProxy rpcClientProxy = new RpcClientProxy(host,port);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res =  helloService.hello(helloObject);
        System.out.println(res);
    }
}
