package com.rpc.common.netty.model;

import lombok.Data;

import java.io.Serializable;

/**
 * RpcRequestParams类
 * 当前类的作用就是包含了当前项目进行远程通信的时候要调用的服务的信息
 * 最后应该要靠反射来调用具体方法
 * 当前类就是RPC的请求调用信息
 * 1: 包含要调用的类名称
 * 2：包含要调用的类的方法名称
 * 3：包含请求参数和请求参数类型（用map）
 */

@Data
public class RpcRequest implements Serializable {

    //调用的服务名称（类似于类名）
    private String className;

    //调用的目标方法名称
    private String methodName;

    //请求参数
    private Object[] params;

    //参数类型method.getParameterTypes()返回什么就用什么类型
    //考虑到要用反射
    private Class<?>[] paramsTypes;

}

