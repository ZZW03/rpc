package com.rpc.common.rpcmethod;

import lombok.Data;

import java.lang.reflect.Method;


@Data
public class RpcServiceMethod {

    /**
     * 自定义规范：调用RPC的时候通过
     * 类路径 + 方法名称的方式去注册中心得到方法
     */
    private Object methodPath;

    /**
     * 实际被调用方法
     */
    private Method method;
}
