package com.rpc.common.congration;


import com.rpc.common.annotation.RpcProvide;
import com.rpc.common.nacos.model.RpcServiceInstance;
import com.rpc.common.nacos.service.RegisterService;
import com.rpc.common.rpcmethod.RpcServiceMethod;
import com.rpc.common.rpcmethod.RpcServiceMethodCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import java.lang.reflect.Method;


@Slf4j
public class RpcServiceAutoProcess implements BeanPostProcessor {

    public RegisterService registerService;

    @Value("${rpc.address}")
    private String ip;

    @Value("${rpc.port}")
    private Integer port;

    public RpcServiceAutoProcess(RegisterService registerService){
        this.registerService = registerService;
    }


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
                instance.setIp(ip);
                instance.setPort(port);
                instance.setClusterName("");
                registerService.register((serviceName), instance);
            }
        }
        return bean;
    }


}
