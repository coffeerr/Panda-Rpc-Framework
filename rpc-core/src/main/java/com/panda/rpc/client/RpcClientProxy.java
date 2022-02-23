package com.panda.rpc.client;

import com.panda.rpc.RpcClient;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.socket.client.SocketRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-02-04 16:46]
 * @description Rpc客户端动态代理
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient){
        this.rpcClient = rpcClient;
    }

    //抑制编译器产生警告信息
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        //创建代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法：{}#{}", method.getDeclaringClass().getName(), method.getName());
        //客户端向服务端传输的对象,Builder模式生成,利用反射获取相关信息
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        //进行远程调用的客户端
        RpcClient client = rpcClient;
        return client.sendRequest(rpcRequest);
    }
}
