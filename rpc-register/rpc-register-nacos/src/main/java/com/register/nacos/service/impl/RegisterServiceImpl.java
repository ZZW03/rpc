package com.register.nacos.service.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.register.nacos.model.RpcServiceInstance;
import com.register.nacos.model.NamingServiceBuilder;
import com.register.nacos.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService , InitializingBean {

    @Value("${nacos.config.server-addr}")
    private  String serverAddr;

    @Value("${nacos.config.namespace}")
    private  String namespace;

    NamingServiceBuilder namingServiceBuilder = null;

    @Override
    public void register(String ServiceName, RpcServiceInstance rpcServiceInstance)  {
        try{
            namingServiceBuilder.getNamingService().registerInstance(ServiceName,rpcServiceInstance);
        }catch (NacosException e){
            log.error("服务注册失败", e);
        }
    }

    @Override
    public void unregister(String ServiceName,RpcServiceInstance rpcServiceInstance) {
        try {
            namingServiceBuilder.getNamingService().deregisterInstance(ServiceName,rpcServiceInstance);
        } catch (NacosException e) {
            log.error("服务注销失败",e);
        }
    }

    @Override
    public void clear(String ServiceName) {
        try {
            namingServiceBuilder.getNamingService().deregisterInstance(ServiceName,null);
        } catch (NacosException e) {
            log.error("服务注销失败",e);
        }
    }

    @Override
    public List<RpcServiceInstance> findAllByServiceName(String ServiceName) {
        try {

            return namingServiceBuilder.getNamingService().getAllInstances(ServiceName).stream().map(v-> new RpcServiceInstance(v.getServiceName(),v.getIp(),v.getPort(),v.getClusterName(),v.getMetadata())).collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("该服务不存在");
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        namingServiceBuilder = new NamingServiceBuilder(serverAddr,namespace);
    }
}
