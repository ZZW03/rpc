package com.rpc.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class  RpcClientProxy implements FactoryBean<Object>, InitializingBean {


    private Object object;

    private Class<?> interfaceClass;


    private Integer port;


    public RpcClientProxy(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public RpcClientProxy(){

    }

    public void generateProxy(){
        this.object= Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new JdkProxyInvocationHandler(port));
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
