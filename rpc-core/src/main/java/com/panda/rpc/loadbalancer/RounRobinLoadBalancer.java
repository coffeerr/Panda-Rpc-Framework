package com.panda.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/13 12:41 下午
 */

public class RounRobinLoadBalancer implements LoadBalancer {
    private volatile int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        if (index > instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
