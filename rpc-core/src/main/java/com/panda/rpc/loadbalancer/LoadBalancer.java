package com.panda.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/13 12:39 下午
 */

public interface LoadBalancer {

    /**
     * @param instances
     * @return [com.alibaba.nacos.api.naming.pojo.Instance]
     * @description 从一系列Instance中选择一个
     * @date [2021-03-15 16:00]
     */
    Instance select(List<Instance> instances);

}