package com.rpc.common.netty.handles;

import com.rpc.common.nacos.model.RpcServiceInstance;
import com.rpc.common.nacos.service.RegisterService;
import com.rpc.common.nacos.service.impl.RegisterServiceImpl;
import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.common.netty.holder.FutureHolder;
import com.rpc.common.netty.holder.SocketHolder;
import com.rpc.common.netty.model.*;
import com.rpc.common.rpcmethod.RpcServiceMethodCache;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.rpc.common.netty.enums.ReqTypeEnum.REQUEST;
import static com.rpc.common.netty.enums.ReqTypeEnum.RESPONSE;

@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcDomain> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcDomain msg) throws Exception {
        RpcHeader header = msg.getHeader();
        ReqTypeEnum reqTypeEnum = ReqTypeEnum.getReqTypeByCode(header.getReqType());
        if (reqTypeEnum == REQUEST) {


            RpcDomain<RpcResponse> rpcDomain = new RpcDomain<>();
            header.setReqType(RESPONSE.getCode());

            Object data = RpcServiceMethodCache
                    .getInstance()
                    .rpcMethodInvoke((RpcRequest) msg.getBody());

            rpcDomain.setBody(new RpcResponse(data,"200"));
            rpcDomain.setHeader(header);
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(rpcDomain);
            if (channelFuture.isSuccess()) {
                log.info("message send success");
            }else {
                log.error("message send failed");
            }

        } else if (reqTypeEnum == RESPONSE) {


            CompletableFuture<Object> future = FutureHolder.remove(header.getReqId());

            if (msg.getBody() == null){
                log.info("ID {} response is received",msg.getHeader().getReqId());
            }

            if (future != null) {
                // 如果 Future 存在，完成它
                future.complete("123");  // 将响应数据设置为 Future 的结果
            } else {
                // 处理未找到 Future 的情况（例如，可能请求超时或被丢弃）
                log.warn("No future found for request ID: {}", header.getReqId());
            }
        }
    }


}
