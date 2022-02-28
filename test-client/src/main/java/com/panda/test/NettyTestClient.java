package com.panda.test;

import com.panda.rpc.RpcClient;
import com.panda.rpc.RpcClientProxy;
import com.panda.rpc.api.HelloObject;
import com.panda.rpc.api.HelloService;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.netty.client.NettyClient;
import com.panda.rpc.serializer.JsonSerializer;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 4:14 下午
 */

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyClient("127.0.0.1",9000);
        rpcClient.setSerializer(new JsonSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);

        HelloObject helloObject = new HelloObject(13,"这是一条来自客户端的信息");
        proxy.hello(helloObject);

    }
}
