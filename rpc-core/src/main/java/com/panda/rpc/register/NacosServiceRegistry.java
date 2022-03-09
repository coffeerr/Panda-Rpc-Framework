package com.panda.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.panda.rpc.enumeration.RpcError;
import com.panda.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/9 8:56 上午
 */

public class NacosServiceRegistry implements ServiceRegistry {
    public static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    public static final String SERVER_ADDR = "127.0.0.1:8848";

    public static final NamingService nameingService;

    static {
        try {
            nameingService = new NacosNamingService(SERVER_ADDR);
        } catch (Exception e) {
            logger.error("连接Nacos发生错误:" + e);
            throw new RpcException(RpcError.FAIL_TO_CONNECT_NACOS);
        }
    }


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            nameingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务到Nacos发生错误:" + e);
            throw new RpcException(RpcError.FAIL_TO_CONNECT_NACOS);
        }

    }

    @Override
    public InetSocketAddress lookUpService(String serviceName) {
        try {
            List<Instance> allInstances = nameingService.getAllInstances(serviceName);
            Instance instance = allInstances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("订阅服务时发生错误：" + e);
            throw new RpcException(RpcError.FAIL_TO_GET_SERVICE);
        }
    }
}
