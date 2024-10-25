package com.rpc.common.rpcmethod;


import com.rpc.common.netty.model.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RpcServiceMethodCache {

    /**
     * rpc方法cache
     * 规则：使用 class.getClass()+"."+methodName的方式保存方法路径
     */
    public static Map<String, RpcServiceMethod> METHOD_CACHE =new ConcurrentHashMap<>();

    /**
     * 使用饿汉式单例
     */
    private static RpcServiceMethodCache INSTANCE = new RpcServiceMethodCache();

    private RpcServiceMethodCache(){}

    public static RpcServiceMethodCache getInstance(){
        return INSTANCE;
    }

    public Object rpcMethodInvoke(RpcRequest request){
        String key=request.getClassName()+"."+request.getMethodName();
        RpcServiceMethod rpcServiceMethod= METHOD_CACHE.get(key);
        if(Objects.isNull(rpcServiceMethod)){
            log.error("rpcServiceMethod is null");
            return null;
        }
        Object methodPath =rpcServiceMethod.getMethodPath();
        Method method=rpcServiceMethod.getMethod();
        try {
            return method.invoke(methodPath,request.getParams());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
