package com.rpc.configuraion;

import com.register.nacos.model.RpcServiceInstance;
import com.register.nacos.service.RegisterService;
import com.rpc.annotation.RpcProvide;
import com.rpc.common.rpcmethod.RpcServiceMethod;
import com.rpc.common.rpcmethod.RpcServiceMethodCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.net.InetAddress;


@Slf4j
public class RpcServiceAutoProcess implements BeanPostProcessor {

    public RegisterService registerService;

    @Value("${server.port}")
    private Integer port;

    public RpcServiceAutoProcess(RegisterService registerService){
        this.registerService = registerService;
    }


    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //扫描存在当前注解的所有方法
        if (bean.getClass().isAnnotationPresent(RpcProvide.class)) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String serviceName = bean.getClass().getInterfaces()[0].getName();
                String key = serviceName + "." + method.getName();

                RpcServiceMethod rpcServiceMethod = new RpcServiceMethod();
                rpcServiceMethod.setMethodPath(bean);
                rpcServiceMethod.setMethod(method);
                RpcServiceMethodCache.METHOD_CACHE.put(key, rpcServiceMethod);


                RpcServiceInstance instance = new RpcServiceInstance();
                instance.setServiceName((serviceName));
                instance.setIp(InetAddress.getLocalHost().getHostAddress());
                instance.setPort(port);
                instance.setClusterName("");
                registerService.register((serviceName), instance);
            }
        }
        return bean;
    }


}
