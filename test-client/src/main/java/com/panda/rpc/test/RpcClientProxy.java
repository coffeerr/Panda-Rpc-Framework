package com.panda.rpc.test;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/16 9:32 下午
 */

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return (RpcResponse)rpcClient.sendRequest(rpcRequest,host,port);
    }
}
