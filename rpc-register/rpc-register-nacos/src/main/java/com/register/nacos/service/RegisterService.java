package com.register.nacos.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.register.nacos.model.RpcServiceInstance;


import java.util.List;

public interface RegisterService {
    /**
     * 注册服务
     * @param ServiceName
     * @param rpcServiceInstance
     * @throws NacosException
     */
    void register(String ServiceName, RpcServiceInstance rpcServiceInstance);

    /**
     * 注销服务
     * @param ServiceName
     * @param rpcServiceInstance
     */
    void unregister(String ServiceName,RpcServiceInstance rpcServiceInstance);

    /**
     * 注销某个服务中的所有服务
     * @param ServiceName
     */
    void clear(String ServiceName);


    List<RpcServiceInstance> findAllByServiceName(String ServiceName);
}
