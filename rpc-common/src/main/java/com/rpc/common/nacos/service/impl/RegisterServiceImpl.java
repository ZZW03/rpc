package com.rpc.common.nacos.service.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.rpc.common.nacos.model.RpcServiceInstance;
import com.rpc.common.nacos.service.RegisterService;
import com.rpc.common.nacos.model.namingServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    NamingService namingService = namingServiceBuilder.getNamingService();

    @Override
    public void register(String ServiceName,RpcServiceInstance rpcServiceInstance)  {
        try{
            namingService.registerInstance(ServiceName,rpcServiceInstance);
        }catch (NacosException e){
            log.error("服务注册失败", e);
        }
    }

    @Override
    public void unregister(String ServiceName,RpcServiceInstance rpcServiceInstance) {
        try {
            namingService.deregisterInstance(ServiceName,rpcServiceInstance);
        } catch (NacosException e) {
            log.error("服务注销失败",e);
        }
    }

    @Override
    public void clear(String ServiceName) {
        try {
            namingService.deregisterInstance(ServiceName,null);
        } catch (NacosException e) {
            log.error("服务注销失败",e);
        }
    }

    @Override
    public List<RpcServiceInstance> findAllByServiceName(String ServiceName) {
        try {


            return namingService.getAllInstances(ServiceName).stream().map(v-> new RpcServiceInstance(v.getServiceName(),v.getIp(),v.getPort(),v.getClusterName(),v.getMetadata())).collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("该服务不存在");
        }
        return null;
    }


}
