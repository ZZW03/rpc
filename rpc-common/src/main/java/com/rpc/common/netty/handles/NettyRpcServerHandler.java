package com.rpc.common.netty.handles;

import com.rpc.common.nacos.model.RpcServiceInstance;
import com.rpc.common.nacos.service.RegisterService;
import com.rpc.common.nacos.service.impl.RegisterServiceImpl;
import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.common.netty.holder.SocketHolder;
import com.rpc.common.netty.model.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.rpc.common.netty.enums.ReqTypeEnum.*;

@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcDomain> {

    static Map<Long, IpDetails> map = new ConcurrentHashMap<>();



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcDomain msg) throws Exception {
        RpcHeader header = msg.getHeader();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ipPort = remoteAddress.getAddress().getHostAddress() + ":" + header.getPort();
        ReqTypeEnum reqTypeEnum = ReqTypeEnum.getReqTypeByCode(header.getReqType());
        if (reqTypeEnum == REQUEST ){
            log.error("REQUEST {}",msg);
            RpcRequest request = (RpcRequest) msg.getBody();
            RegisterService registerService = new RegisterServiceImpl();
            List<RpcServiceInstance> allByServiceName = registerService.findAllByServiceName(request.getClassName());
            if(allByServiceName.isEmpty()){
                log.error("找不到这个服务");
            }
            RpcServiceInstance service = allByServiceName.get(0);
            NioSocketChannel nioSocketChannel = SocketHolder.get(service.getIp() + ":" + service.getPort());
            if(allByServiceName.isEmpty()){
                log.error("该服务未连接，请稍后再尝试");
            }
            ChannelFuture channelFuture = nioSocketChannel.writeAndFlush(msg);

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("Id {} Message Send To {} Success", header.getReqId(), ipPort);
                        map.put(header.getReqId(), new IpDetails(remoteAddress.getAddress().getHostAddress(), header.getPort()));
                    } else {
                        log.error("Id {} Message Send To {} Failed", header.getReqId(), ipPort);
                    }
                }
            });



        }else if(reqTypeEnum == RESPONSE){

            log.info("Id {} Message Response Received",header.getReqId());
            IpDetails ipDetails = map.get(header.getReqId());
            NioSocketChannel nioSocketChannel = SocketHolder.get(ipDetails.getIp() + ":" + ipDetails.getPort());
            ChannelFuture channelFuture = nioSocketChannel.writeAndFlush(msg);


        }else if (reqTypeEnum == ENROLL){

            SocketHolder.put(ipPort,(NioSocketChannel) ctx.channel());
            log.info("client {} enroll success", ipPort);
        }



    }
}
