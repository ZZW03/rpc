package com.rpc.client;

import com.rpc.common.netty.enums.AlgorithmTypeEnum;
import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.holder.FutureHolder;
import com.rpc.common.netty.model.RpcCommonConstants;
import com.rpc.common.netty.model.RpcDomain;
import com.rpc.common.netty.model.RpcHeader;
import com.rpc.common.netty.model.RpcRequest;
import com.rpc.common.util.SnowflakeIdWorker;
import com.rpc.netty.NettyClient;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class JdkProxyInvocationHandler implements InvocationHandler {


    private Integer port;


    public JdkProxyInvocationHandler() {
    }

    public JdkProxyInvocationHandler(Integer port) {
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcDomain<RpcRequest> rpcDomain = new RpcDomain<>();

        RpcHeader header = new RpcHeader();
        header.setAlgorithmType(AlgorithmTypeEnum.JSON.getCode());
        header.setPort(port);
        header.setReqId(SnowflakeIdWorker.nextId());
        header.setReqType(ReqTypeEnum.REQUEST.getCode());
        header.setVersionId(RpcCommonConstants.VERSION_ID);
        rpcDomain.setHeader(header);
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsTypes(method.getParameterTypes());
        request.setParams(args);
        rpcDomain.setBody(request);

        ChannelFuture myChannel = NettyClient.getMyChannel();

        // 创建 CompletableFuture
        CompletableFuture<Object> future = new CompletableFuture<>();

        // 设置超时
        future.completeOnTimeout(null, 50000, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> {
                    log.error("Request timed out or failed: {}", ex.getMessage());
                    return null; // 返回 null 来避免 ClassCastException
                });

        // 发送请求
        if (myChannel.channel().isActive() && myChannel.channel().isWritable()) {
            myChannel.channel().writeAndFlush(rpcDomain);
        } else {
            log.warn("Channel is not active or writable: {}", myChannel.channel().isActive());
        }

        FutureHolder.put(header.getReqId(), future);
        // 获取结果，注意处理超时或失败的情况
        Object result = future.get(); // 这里将会返回 null 如果请求超时

        if (result == null) {
            throw new TimeoutException("Request timed out");
        }

        return result;
    }

}
